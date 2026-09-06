## Creación de Project en Android Studio

Name: Workout
Package name: Com.app.workout
Path : /home/parmenides/AndroidStudioProjects/Workout
Mínimun SDK: API 33 ("Tiramisu"; Android 13)
Lenguaje: Kotlin DSL (build.gradle.kts)

app/scr/main/java/com/app/workout/ui/MainActivity.kt

**PROMPT**

Quiero hacer una app de Android en Copilot usando la IA. Te adjunto el prompt. dime sugerencias y como mejorarlo: 


Crea una aplicación/juego de Android donde el usuario responder operaciones aritméticas.
 
Interfaz:
	en la parte superior la "operación aritmética". 
	En la parte media se muestra la cifra escrita por el usuario.  
	En la parte inferior hay un teclado numérico para introducir la respuesta.
	Aplica un diseño basado en Material Design 3 con colores contrastados.
	**adjunto una imagen como referencia o esquema**

Funcionalidad:
Respecto a la operación matemática, se generaran 2 números (Operador y Operando), ambos menores de 12. Las operaciones aritméticas son suma, resta, multiplicación o división.
Caso que la operación sea una división, se Evitar divisiones por cero y la división será exacta, para evitar resultados decimales


Si el usuario introduce el resultado correcto, debe mostrarse un mensaje de celebración (por ejemplo, "¡Correcto!").
Si el usuario introduce un resultado incorrecto, debe mostrarse un mensaje de error (“Incorrecto. Intenta de nuevo.”).
Si el usuario introduce un valor no numérico, debe mostrarse un mensaje de error específico (“Introduce un número válido.”).

El ciclo de pregunta respuesta se repetirá 10 veces, tras lo cual se mostrará el numero de aciertos y fallos y el tiempo que ha necesitado para responder las 10 operaciones. 


**PROMPT GEMINI**
Actúa como un desarrollador experto en Android. Crea el código completo para una aplicación de entrenamiento matemático siguiendo estas especificaciones:

Tecnología:

    Lenguaje: Kotlin.
    UI: Jetpack Compose con Material Design 3.
    Arquitectura: Usa un ViewModel para gestionar la lógica y el estado del juego.

Interfaz (Basada en la imagen adjunta):
    Layout: Una Column principal con espaciado uniforme.
    Superior: Texto grande (DisplayLarge) con la operación (ej: "8 x 4").
    Centro: Un área de visualización de la respuesta actual con un subrayado o caja estilizada.
    Inferior: Un teclado numérico personalizado (Grid de 3x4) con botones circulares o redondeados. Incluye un botón para "Borrar".
    Feedback: Muestra un Snackbar o un mensaje animado para indicar "¡Correcto!" o "Incorrecto".

Lógica del Juego:

    Generación: 10 rondas de operaciones aleatorias (+, -, *, /). Números entre 1 y 12.
    Divisiones: Asegura que el resultado sea siempre un número entero y evita la división por cero (Genera divisor y cociente aleatorios, luego calcula el dividendo).
    Validación: Compara la entrada del usuario con el resultado real.
    Finalización: Al completar 10 operaciones, muestra una pantalla de "Resumen" con:

        Total de aciertos y errores.
        Tiempo total transcurrido (formato mm:ss).
        Botón para "Reiniciar juego".

Estilo: Usa una paleta de colores de alto contraste (fondo claro, números oscuros y botones con colores primarios de Material 3).


**CORRECCIÓN DE DISEÑO**

Quiero los botones ligeramente rectangulares(Horizontalmente.) el tamaño de fuente de las operaciones y de la respuesta el doble de grande. Cambia el diseño de Material Design 3 a un estilo Pixel / Retro / Vaporware.

**CORRECCIÓN DE DISEÑO 2**
Modifica el color de fondo a RGB #000000, Botones con fuente y borde verde neón y fondo color #000000. centra la posición de "operationText" entre el borde superior de la pantalla y "userAnswer"
Mientras un botón es pulsado cambiará el color del borde y la fuente de dicho botón, por unos colores neón aleatorios. 

**CORRECCIÓN DE DISEÑO 3**
Elimina el espacio que hay en operationText entre el símbolo de la operación, el operador y el operando. Color de Operation Text y UserAnswer blanco. Elimina el Texto "Borrar" del botón "Borrar" por el símbolo "◁". Dicho botón borrar tendrá fuente en negrita color rojo y borde del botón rojo. 

**CORRECCIÓN DE DISEÑO 3**
Posición del teclado numérico y de userAnswer ligeramente superior, ya que se solapan con la barra de navegación de Android . 
Mientras un botón es pulsado cambiará el color del borde y la fuente de dicho botón, por unos colores neón aleatorios. 

**Error. Los cambios en GameViewModel.tk no tienen efecto**

Los cambios ejecutados en GameViewModel.tk no tienen efecto al ejecutar la aplicación. Parece que en el proyecto hay dos paquetes de código casi idénticos: com.app.workoutu y com.app.workoutu2.

<	


Neumorphism, flat, Glassmorphism, fluent, 

Material Design 3
Flat Design: minimalismo radical, sin volumen ni ornamento.
Flat Design 2.0: flat con profundidad sutil (sombras, capas).
Neumorphism (Soft UI): relieve suave, estética “plástico moldeado”.
Skeuomorphism: imitación explícita de objetos físicos.
Glassmorphism: transparencias, desenfoque y capas flotantes.
Brutalism / Neo-Brutalism: aspereza deliberada, anti-estética pulida.
Swiss / International Style: tipografía limpia, rejillas estrictas.
Human Interface Guidelines (Apple): claridad, jerarquía y tactilidad. (Apple)
Fluent Design (Microsoft): luz, movimiento y profundidad.
Ant Design: empresarial, sistemático, alta densidad informativa.
Bauhaus digital: función sobre forma, geometría esencial.
Minimalist Design: reducción extrema de elementos.
Maximalism: exceso controlado, color y tipografía dominante.
Retro / Pixel / Vaporwave / Hacker: estética nostálgica y referencial.



/home/parmenides/AndroidStudioProjects/Workoutu2/matema.svg
