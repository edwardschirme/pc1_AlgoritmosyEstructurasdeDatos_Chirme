# PC1 - Mini película con audio, varias imágenes y threads

## Integrantes
- Edwards Chirme

## Descripción del Proyecto
Este proyecto implementa la generación de una secuencia de imágenes procesadas basándose en la amplitud de un archivo de audio WAV. El sistema utiliza procesamiento paralelo con hilos (Threads) para optimizar la generación de frames y emplea FFmpeg para el ensamblaje final del video.

## Detalles Técnicos
- **Librerías utilizadas:** Java Standard Library, Maven.
- **Procesamiento:** Implementación de filtros de convolución (Blur, Sharpen, Sobel) y transformaciones geométricas (Rotación).
- **Sincronización:** El video se genera automáticamente ajustando la cantidad de frames a la duración real del audio.
- **Automatización:** Integración directa con FFmpeg para la creación del archivo MP4.

## Estructura de Entrega
- `/proyecto`: Código fuente y archivos de configuración.
- `/input`: Recursos de audio e imágenes utilizados.
- `/output`: Métricas de rendimiento (`metrics.csv`).
- `/capturas`: Evidencias de ejecución.
- `videoclip_serial.mp4`: Resultado del procesamiento secuencial.
- `videoclip_parallel.mp4`: Resultado del procesamiento paralelo.

## Implementaciones realizadas
Para completar el proyecto, se realizaron las siguientes modificaciones y mejoras:
- **Sincronización automática:** Se modificó el código para que el programa calcule la duración exacta del archivo de audio y determine la cantidad de frames necesarios. Si el audio es corto, el programa implementa un bucle para cubrir la duración total del video.
- **Efectos dinámicos:** Se reutilizo la lógica donde la imagen rota suavemente y cambia de filtro (como desenfoque o detección de bordes) dependiendo del nivel de amplitud del audio con ligeras modificaciones.
- **Automatización del renderizado:** Se integró la llamada a FFmpeg directamente desde Java, permitiendo que el video final en formato MP4 se genere automáticamente al ejecutar el programa.
