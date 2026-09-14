package pe.edu.utp.videoclip;

public final class AudioAnalyzer {
    private AudioAnalyzer() {}

    public static double levelForFrame(
            AudioData audio,
            int frame,
            int totalFrames
    ) {
        short[] samples = audio.samples();
        if (samples == null || samples.length == 0) return 0.0;

        // Usamos el operador módulo (%) para que si el video es más largo que el audio,
        // el audio se repita (bucle).
        int effectiveFrame = frame % totalFrames;

        int start = (int)(
                (long)effectiveFrame * samples.length / totalFrames
        );

        int end = (int)(
                (long)(effectiveFrame + 1) * samples.length / totalFrames
        );

        if (end <= start) {
            end = Math.min(samples.length, start + 1);
        }

        double level = StudentWork.calculateAudioLevel(
                samples,
                start,
                end
        );

        return Math.max(0.0, Math.min(1.0, level));
    }
}
