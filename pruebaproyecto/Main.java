import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Clase Main
 * Punto de entrada del programa.
 */
public class Main {

    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println(" COMPARACION DE 12 IMPLEMENTACIONES DE ORDENAMIENTO (CONCURRENTE)");
        System.out.println("=================================================================");

        int cantidad = pedirCantidadElementos();
        boolean restringido = pedirModoDeDatos();

        int[] datos;
        if (restringido) {
            datos = DataGenerator.generarRestringidos(cantidad);
            System.out.println();
            System.out.println("Se generaron " + cantidad + " numeros aleatorios, restringidos entre 1 y 5.");
        } else {
            datos = DataGenerator.generarAleatorios(cantidad);
            System.out.println();
            System.out.println("Se generaron " + cantidad + " numeros aleatorios.");
        }

        System.out.println("Ejecutando las 12 implementaciones en hilos independientes...");
        System.out.println();

        ConcurrentHashMap<String, SortResult> resultados = EjecutorOrdenamientos.ejecutarTodas(datos);

        mostrarResultados(cantidad, resultados);
    }

    // arma y muestra la tabla comparativa final: las 12 implementaciones
    // ordenadas de MENOR a MAYOR tiempo, mas la complejidad Big O de
    // referencia al final
    public static void mostrarResultados(int cantidad, ConcurrentHashMap<String, SortResult> resultados) {
        List<SortResult> lista = new ArrayList<>(resultados.values());
        lista.sort((a, b) -> Double.compare(a.tiempoMs, b.tiempoMs));

        System.out.println();
        System.out.println("=================================================================");
        System.out.println(" RESULTADOS DE ORDENAMIENTO");
        System.out.println("=================================================================");
        System.out.println("Elementos: " + cantidad);
        System.out.println();

        System.out.printf("%-5s %-16s %-11s %13s   %s%n", "Pos.", "Algoritmo", "Estructura", "Tiempo (ms)", "Ordeno?");
        System.out.println("-----------------------------------------------------------------");

        int posicion = 1;
        int fallos = 0;
        for (SortResult resultado : lista) {
            System.out.printf("%-5d %-16s %-11s %13.3f   %s%n",
                    posicion,
                    resultado.nombreAlgoritmo,
                    resultado.estructura,
                    resultado.tiempoMs,
                    resultado.ordenoCorrectamente ? "Si" : "NO - ERROR");

            if (!resultado.ordenoCorrectamente) {
                fallos++;
            }
            posicion++;
        }

        System.out.println();

        if (!lista.isEmpty()) {
            SortResult masRapido = lista.get(0);
            System.out.println("Implementacion con menor tiempo registrado: "
                    + masRapido.nombreAlgoritmo + " (" + masRapido.estructura + ") con "
                    + String.format("%.3f", masRapido.tiempoMs) + " ms");
        }

        // esto no deberia pasar nunca (ya probamos las 6 a fondo en los
        // pasos anteriores), pero lo revisamos de todas formas para
        // detectar cualquier problema de inmediato en vez de confiar
        // ciegamente en el resultado
        if (fallos > 0) {
            System.out.println();
            System.out.println("ATENCION: " + fallos + " implementacion(es) NO quedaron bien ordenadas.");
        }

        ComplexityInfo.mostrarTabla();

        System.out.println();
        System.out.println("Nota: el menor tiempo medido en ESTA ejecucion no demuestra que esa");
        System.out.println("implementacion sea siempre la mas rapida. Los 12 hilos compiten por el");
        System.out.println("procesador al mismo tiempo, asi que los tiempos individuales pueden");
        System.out.println("variar de una corrida a otra. Repite la prueba varias veces para");
        System.out.println("confirmar si el patron se mantiene.");
    }

    // pregunta cuantos elementos ordenar, validando que sea un entero positivo
    public static int pedirCantidadElementos() {
        int cantidad = -1;

        while (cantidad <= 0) {
            System.out.print("Cuantos elementos vas a ordenar? ");

            if (teclado.hasNextInt()) {
                cantidad = teclado.nextInt();
                teclado.nextLine();

                if (cantidad <= 0) {
                    System.out.println("Debe ser un numero entero positivo, intenta de nuevo.");
                }
            } else {
                System.out.println("Eso no es un numero entero valido, intenta de nuevo.");
                teclado.next(); // descartamos lo que se haya escrito mal
            }
        }

        advertirSiEsMuyGrande(cantidad);
        return cantidad;
    }

    // avisa (sin bloquear) si la cantidad elegida puede tardar mucho con
    // los algoritmos O(n^2). Esto es justo lo que pide la actividad:
    // "documenta el comportamiento y justifica el tamanio elegido"
    public static void advertirSiEsMuyGrande(int cantidad) {
        if (cantidad >= 100_000) {
            System.out.println();
            System.out.println("Aviso: con " + cantidad + " elementos, Bubble Sort, Selection Sort");
            System.out.println("e Insertion Sort (los 3 son O(n^2)) pueden tardar varios segundos");
            System.out.println("o incluso minutos, sobre todo con datos en orden inverso o con");
            System.out.println("muchos valores repetidos. El programa las corre de todas formas,");
            System.out.println("tal como pide la actividad -- esto es un comportamiento esperado");
            System.out.println("que se debe documentar en el reporte, no un error del programa.");
        } else if (cantidad >= 20_000) {
            System.out.println();
            System.out.println("Aviso: con " + cantidad + " elementos, las implementaciones O(n^2)");
            System.out.println("pueden tardar varios segundos. Es normal.");
        }
    }

    // pregunta si el usuario quiere datos normales o restringidos (1 a 5)
    public static boolean pedirModoDeDatos() {
        System.out.println();
        System.out.println("Que tipo de datos quieres generar?");
        System.out.println("1. Numeros aleatorios normales");
        System.out.println("2. Numeros aleatorios restringidos entre 1 y 5 (para forzar muchos duplicados)");

        int opcion = -1;
        while (opcion != 1 && opcion != 2) {
            System.out.print("Elige una opcion (1 o 2): ");

            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();

                if (opcion != 1 && opcion != 2) {
                    System.out.println("Opcion no valida, elige 1 o 2.");
                }
            } else {
                System.out.println("Eso no es un numero valido, intenta de nuevo.");
                teclado.next();
            }
        }

        return opcion == 2;
    }
}
