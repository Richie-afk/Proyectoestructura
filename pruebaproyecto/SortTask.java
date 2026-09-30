import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/*
 * Clase SortTask
 * Es el Runnable que corre en cada uno de los 12 hilos. No sabe nada
 * de algoritmos de ordenamiento en especifico -- solo sabe ejecutar
 * una operacion (que ya viene lista para ordenar UNA copia de datos),
 * medir cuanto tardo, verificar el resultado, y guardarlo.
 *
 * Se hizo generica a proposito (en vez de escribir 12 clases casi
 * identicas) usando 2 piezas que se le pasan desde afuera:
 *
 *   - operacionDeOrdenar: un Runnable que YA sabe que algoritmo
 *     llamar y sobre que copia de datos (arreglo o ArrayList).
 *   - verificador: un Supplier<Boolean> que revisa si esa misma
 *     copia quedo bien ordenada, DESPUES de correr la operacion.
 *
 * Esto se arma en la clase EjecutorOrdenamientos usando referencias a
 * metodo (ArraySorts::bubbleSort, ListSorts::bubbleSort, etc.).
 */
public class SortTask implements Runnable {

    private final String nombreAlgoritmo;
    private final String estructura;
    private final Runnable operacionDeOrdenar;
    private final Supplier<Boolean> verificador;
    private final ConcurrentHashMap<String, SortResult> resultados;

    public SortTask(String nombreAlgoritmo, String estructura, Runnable operacionDeOrdenar,
                     Supplier<Boolean> verificador, ConcurrentHashMap<String, SortResult> resultados) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.estructura = estructura;
        this.operacionDeOrdenar = operacionDeOrdenar;
        this.verificador = verificador;
        this.resultados = resultados;
    }

    @Override
    public void run() {
        // IMPORTANTE: el cronometro solo envuelve el ordenamiento en si.
        // No se mide aqui la generacion de datos ni la creacion de
        // copias, porque eso ya se hizo ANTES de crear este hilo.
        long inicio = System.nanoTime();
        operacionDeOrdenar.run();
        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;
        boolean ordenoBien = verificador.get();

        SortResult resultado = new SortResult(nombreAlgoritmo, estructura, tiempoMs, ordenoBien);

        // ConcurrentHashMap es seguro para que varios hilos escriban aqui
        // al mismo tiempo sin pisarse los resultados entre ellos
        String clave = nombreAlgoritmo + " (" + estructura + ")";
        resultados.put(clave, resultado);
    }
}
