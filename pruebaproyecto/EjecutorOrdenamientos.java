import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/*
 * Clase EjecutorOrdenamientos
 * Este es el "motor de hilos" del proyecto. Su trabajo es:
 *
 *   1. Crear una copia INDEPENDIENTE de los datos originales para
 *      cada una de las 12 implementaciones (6 algoritmos x 2
 *      estructuras). Ninguna comparte su copia con otra.
 *   2. Envolver cada implementacion en su propio Thread (usando
 *      SortTask).
 *   3. Iniciar los 12 hilos.
 *   4. Esperar (join) a que TODOS terminen antes de continuar.
 *   5. Regresar el mapa con los 12 resultados ya medidos y verificados.
 *
 * Usa referencias a metodo (por ejemplo ArraySorts::bubbleSort) en vez
 * de repetir el mismo bloque de codigo 12 veces.
 */
public class EjecutorOrdenamientos {

    public static ConcurrentHashMap<String, SortResult> ejecutarTodas(int[] datosOriginales) {
        ConcurrentHashMap<String, SortResult> resultados = new ConcurrentHashMap<>();
        List<Thread> hilos = new ArrayList<>();

        // -------- 6 implementaciones usando ARREGLO (int[]) --------
        agregarTareaArreglo(hilos, resultados, "Bubble Sort", datosOriginales, ArraySorts::bubbleSort);
        agregarTareaArreglo(hilos, resultados, "Selection Sort", datosOriginales, ArraySorts::selectionSort);
        agregarTareaArreglo(hilos, resultados, "Insertion Sort", datosOriginales, ArraySorts::insertionSort);
        agregarTareaArreglo(hilos, resultados, "Merge Sort", datosOriginales, ArraySorts::mergeSort);
        agregarTareaArreglo(hilos, resultados, "Quick Sort", datosOriginales, ArraySorts::quickSort);
        agregarTareaArreglo(hilos, resultados, "Shell Sort", datosOriginales, ArraySorts::shellSort);

        // -------- 6 implementaciones usando ARRAYLIST --------
        agregarTareaLista(hilos, resultados, "Bubble Sort", datosOriginales, ListSorts::bubbleSort);
        agregarTareaLista(hilos, resultados, "Selection Sort", datosOriginales, ListSorts::selectionSort);
        agregarTareaLista(hilos, resultados, "Insertion Sort", datosOriginales, ListSorts::insertionSort);
        agregarTareaLista(hilos, resultados, "Merge Sort", datosOriginales, ListSorts::mergeSort);
        agregarTareaLista(hilos, resultados, "Quick Sort", datosOriginales, ListSorts::quickSort);
        agregarTareaLista(hilos, resultados, "Shell Sort", datosOriginales, ListSorts::shellSort);

        // iniciamos los 12 hilos -- aqui es donde de verdad empiezan a
        // correr, todos al mismo tiempo (concurrentemente)
        for (Thread hilo : hilos) {
            hilo.start();
        }

        // esperamos a que TODOS terminen antes de seguir. Sin este paso,
        // el programa podria intentar mostrar resultados de hilos que
        // todavia no acaban.
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException excepcion) {
                System.out.println("Un hilo fue interrumpido antes de terminar: " + excepcion.getMessage());
            }
        }

        return resultados;
    }

    // arma UNA tarea para un algoritmo que trabaja con int[], le crea su
    // propia copia de los datos, y la agrega a la lista de hilos (todavia
    // sin iniciar)
    private static void agregarTareaArreglo(List<Thread> hilos, ConcurrentHashMap<String, SortResult> resultados,
                                             String nombreAlgoritmo, int[] datosOriginales, Consumer<int[]> algoritmo) {
        int[] copia = Arrays.copyOf(datosOriginales, datosOriginales.length);

        SortTask tarea = new SortTask(
                nombreAlgoritmo,
                "Arreglo",
                () -> algoritmo.accept(copia),
                () -> ArraySorts.estaOrdenado(copia),
                resultados
        );

        hilos.add(new Thread(tarea));
    }

    // igual que el anterior, pero para un algoritmo que trabaja con
    // ArrayList<Integer>. Se convierte el arreglo original a una lista
    // nueva, propia de esta tarea.
    private static void agregarTareaLista(List<Thread> hilos, ConcurrentHashMap<String, SortResult> resultados,
                                           String nombreAlgoritmo, int[] datosOriginales, Consumer<ArrayList<Integer>> algoritmo) {
        ArrayList<Integer> copia = new ArrayList<>();
        for (int valor : datosOriginales) {
            copia.add(valor);
        }

        SortTask tarea = new SortTask(
                nombreAlgoritmo,
                "ArrayList",
                () -> algoritmo.accept(copia),
                () -> ListSorts.estaOrdenado(copia),
                resultados
        );

        hilos.add(new Thread(tarea));
    }
}
