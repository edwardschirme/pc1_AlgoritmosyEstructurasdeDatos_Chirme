package pe.edu.utp.videoclip;

import java.nio.file.Path;

public class VideoClipMain { // I'll stick to VideoClipMain as the class name

    public static void main(String[] args) throws Exception {
        AppConfig cfg = AppConfig.load(
                Path.of("config.properties")
        );

        AudioData audio = WavReader.read(cfg.audio());

        MatrixImage[] images =
                ImageLibrary.loadFolder(
                        cfg.imagesFolder()
                );

        System.out.println("UTP | VIDEOCLIP MULTIIMAGEN + THREADS");
        System.out.println("-----------------------------------");
        System.out.println("Audio:      " + cfg.audio());
        System.out.println("Imágenes:   " + images.length);

        for (int i = 0; i < images.length; i++) {
            System.out.println(
                    "  [" + i + "] " + images[i].name()
            );
        }

        System.out.println("FPS:        " + cfg.fps());

        // Automatización: calculamos los frames basados en la duración real del audio
        double realDuration = audio.durationSeconds();
        int calculatedTotalFrames = (int) Math.ceil(realDuration * cfg.fps());

        System.out.println("Duración Real Audio: " + String.format("%.2f", realDuration) + " s");
        System.out.println("Frames Calculados:   " + calculatedTotalFrames);
        System.out.println("Modo:       " + cfg.mode());
        System.out.println("Threads:    " + cfg.threads());
        System.out.println();

        FrameGenerator.Result serial = null;
        FrameGenerator.Result parallel = null;

        if (cfg.mode().equals("serial")
                || cfg.mode().equals("both")) {

            System.out.println("Generando versión SERIAL...");

            serial = FrameGenerator.generateSerial(
                    audio,
                    images,
                    calculatedTotalFrames,
                    cfg.serialOutput()
            );

            renderVideo("serial"); //codigo agregado
            //fin codigo agregado
        }

        if (cfg.mode().equals("parallel")
                || cfg.mode().equals("both")) {

            System.out.println("Generando versión PARALELA...");

            parallel = FrameGenerator.generateParallel(
                    audio,
                    images,
                    calculatedTotalFrames,
                    cfg.threads(),
                    cfg.parallelOutput()
            );

            renderVideo("parallel"); //codigo agregado
            //fin codigo agregado
        }

        Metrics.print(serial, parallel);

        Metrics.save(
                Path.of("output/metrics.csv"),
                serial,
                parallel
        );

        System.out.println();
        System.out.println("¡Proceso completado!");
        System.out.println("Los videos MP4 han sido generados automáticamente.");
    }

    private static void renderVideo(String type) { //codigo agregado
        try {
            System.out.println("Llamando a FFmpeg para renderizar video " + type + "...");

            String framesPath = (type.equals("parallel"))
                ? "output\\frames_parallel\\frame_%03d.png"
                : "output\\frames_serial\\frame_%03d.png";

            String outputName = (type.equals("parallel"))
                ? "..\\videoclip_parallel.mp4"
                : "..\\videoclip_serial.mp4";

            // El audio ahora está en la carpeta input que está UN NIVEL ARRIBA del proyecto
            String audioPath = "..\\input\\audio\\audio_base.wav";

            String command = String.format(
                "ffmpeg -y -framerate 12 -i %s -i %s -vf \"pad=ceil(iw/2)*2:ceil(ih/2)*2\" -c:v libx264 -pix_fmt yuv420p -shortest %s",
                framesPath, audioPath, outputName
            );

            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Video " + type + " renderizado con éxito.");
            } else {
                System.err.println("FFmpeg terminó con error. Código: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Error al renderizar video " + type + ": " + e.getMessage());
            System.err.println("Asegúrate de que FFmpeg esté instalado y agregado al PATH de Windows.");
        }
    } //codigo agregado
}
