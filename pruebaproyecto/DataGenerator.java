import java.util.Random;

/*
 * Clase DataGenerator
 * Genera los datos de prueba usando Random, tal como pide la
 * actividad. Tiene 2 modos:
 *
 *   - generarAleatorios(): numeros aleatorios "normales", sin
 *     restriccion (entre 0 y 999,999).
 *
 *   - generarRestringidos(): numeros aleatorios pero limitados al
 *     rango 1 a 5, para forzar que existan muchos valores repetidos.
 *     Este es el modo que la actividad pide probar con 100,000
 *     elementos, y es justo el caso que nos revelo el bug de
 *     quickSort en el Paso 2.
 */
public class DataGenerator {

    private static final Random random = new Random();

    public static int[] generarAleatorios(int cantidad) {
        int[] datos = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            datos[i] = random.nextInt(1_000_000);
        }
        return datos;
    }

    public static int[] generarRestringidos(int cantidad) {
        int[] datos = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            datos[i] = random.nextInt(5) + 1; // numeros del 1 al 5
        }
        return datos;
    }
}
