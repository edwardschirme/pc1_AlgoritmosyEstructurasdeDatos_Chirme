package pe.edu.utp.videoclip;

/**
 * ARCHIVO PRINCIPAL QUE DEBE MODIFICAR EL ESTUDIANTE
 * Implementación para la PC1 de Algoritmos y Estructuras de Datos.
 */
public final class StudentWork {

    private StudentWork() {}

    //codigo editdo
    public static double calculateAudioLevel(
            short[] samples,
            int start,
            int end
    ) {
        if (samples == null || start >= end || start < 0) {
            return 0.0;
        }

        long sum = 0;
        int count = end - start;

        for (int i = start; i < end; i++) {
            sum += Math.abs(samples[i]);
        }

        double average = (double) sum / count;
        double level = average / 32767.0;

        return Math.max(0.0, Math.min(1.0, level));
    } //fin codigo editdo

    //codigo editdo
    public static int chooseImageIndex(
            double level,
            int frameNumber,
            int totalFrames,
            int imageCount
    ) {
        if (imageCount <= 1) return 0;

        int index = (int) ((double) frameNumber / totalFrames * imageCount);

        return Math.min(index, imageCount - 1);
    } //fin codigo editdo

    //codigo editdo
    public static MatrixImage applyEffects(
            MatrixImage base,
            double level,
            int frameNumber,
            int totalFrames
    ) {
        double angle = Math.sin(frameNumber * 0.1) * 3.0;
        MatrixImage processed = base.rotate(angle);

        if (level < 0.3) {
            processed = processed.brighten(1.1);
        } else if (level < 0.6) {
            processed = processed.sharpen();
        } else if (level < 0.8) {
            processed = processed.blur();
        } else {
            processed = processed.sobel();
        }

        if (frameNumber == totalFrames / 2) {
            processed = processed.invert();
        }

        return processed;
    } //fin codigo editdo
}
