import java.util.ArrayList;
import java.util.Random;

/*
 * Clase ListSorts
 * Es la version "hermana" de ArraySorts, pero para ArrayList<Integer>
 * en vez de int[]. La logica de cada algoritmo es identica a la de
 * ArraySorts -- lo unico que cambia es como se lee y se escribe un
 * elemento: en vez de arreglo[i], se usa lista.get(i) y lista.set(i,
 * valor). Para un ArrayList estas 2 operaciones son O(1), asi que la
 * complejidad teorica de cada algoritmo no cambia por usar ArrayList
 * en vez de arreglo.
 */
public class ListSorts {

    // ==================================================================
    // 1. BUBBLE SORT — igual que en ArraySorts
    // ==================================================================
    public static void bubbleSort(ArrayList<Integer> lista) {
        int n = lista.size();

        for (int i = 0; i < n - 1; i++) {
            boolean huboIntercambio = false;

            for (int j = 0; j < n - 1 - i; j++) {
                if (lista.get(j) > lista.get(j + 1)) {
                    intercambiar(lista, j, j + 1);
                    huboIntercambio = true;
                }
            }

            if (!huboIntercambio) {
                break;
            }
        }
    }

    // ==================================================================
    // 2. SELECTION SORT — igual que en ArraySorts
    // ==================================================================
    public static void selectionSort(ArrayList<Integer> lista) {
        int n = lista.size();

        for (int i = 0; i < n - 1; i++) {
            int indiceMenor = i;

            for (int j = i + 1; j < n; j++) {
                if (lista.get(j) < lista.get(indiceMenor)) {
                    indiceMenor = j;
                }
            }

            if (indiceMenor != i) {
                intercambiar(lista, i, indiceMenor);
            }
        }
    }

    // ==================================================================
    // 3. INSERTION SORT — igual que en ArraySorts
    // ==================================================================
    public static void insertionSort(ArrayList<Integer> lista) {
        int n = lista.size();

        for (int i = 1; i < n; i++) {
            int actual = lista.get(i);
            int j = i - 1;

            while (j >= 0 && lista.get(j) > actual) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, actual);
        }
    }

    // ==================================================================
    // 4. MERGE SORT
    // Aqui si hay una diferencia practica: para combinar() necesitamos
    // 2 sub-listas separadas. Se crean con "new ArrayList<>(subList)",
    // que hace una copia real (no una vista), para no batallar con
    // ConcurrentModificationException.
    // ==================================================================
    public static void mergeSort(ArrayList<Integer> lista) {
        if (lista.size() < 2) {
            return;
        }
        mergeSortRecursivo(lista, 0, lista.size() - 1);
    }

    private static void mergeSortRecursivo(ArrayList<Integer> lista, int inicio, int fin) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSortRecursivo(lista, inicio, medio);
            mergeSortRecursivo(lista, medio + 1, fin);
            combinar(lista, inicio, medio, fin);
        }
    }

    private static void combinar(ArrayList<Integer> lista, int inicio, int medio, int fin) {
        ArrayList<Integer> mitadIzquierda = new ArrayList<>(lista.subList(inicio, medio + 1));
        ArrayList<Integer> mitadDerecha = new ArrayList<>(lista.subList(medio + 1, fin + 1));

        int i = 0;
        int j = 0;
        int k = inicio;

        while (i < mitadIzquierda.size() && j < mitadDerecha.size()) {
            if (mitadIzquierda.get(i) <= mitadDerecha.get(j)) {
                lista.set(k, mitadIzquierda.get(i));
                i++;
            } else {
                lista.set(k, mitadDerecha.get(j));
                j++;
            }
            k++;
        }

        while (i < mitadIzquierda.size()) {
            lista.set(k, mitadIzquierda.get(i));
            i++;
            k++;
        }
        while (j < mitadDerecha.size()) {
            lista.set(k, mitadDerecha.get(j));
            j++;
            k++;
        }
    }

    // ==================================================================
    // 5. QUICK SORT
    // Misma correccion que se aplico en ArraySorts: pivote ALEATORIO,
    // y recursion solo en la mitad mas chica (con ciclo iterativo en
    // la mitad grande), para que nunca se desborde el stack sin
    // importar que tan feos vengan los datos (por ejemplo, 100,000
    // elementos con muchos valores repetidos).
    // ==================================================================
    private static final Random RANDOM = new Random();

    public static void quickSort(ArrayList<Integer> lista) {
        if (lista.size() < 2) {
            return;
        }
        quickSortRecursivo(lista, 0, lista.size() - 1);
    }

    private static void quickSortRecursivo(ArrayList<Integer> lista, int inicio, int fin) {
        while (inicio < fin) {
            int posicionPivote = particionar(lista, inicio, fin);

            if (posicionPivote - inicio < fin - posicionPivote) {
                quickSortRecursivo(lista, inicio, posicionPivote - 1);
                inicio = posicionPivote + 1;
            } else {
                quickSortRecursivo(lista, posicionPivote + 1, fin);
                fin = posicionPivote - 1;
            }
        }
    }

    private static int particionar(ArrayList<Integer> lista, int inicio, int fin) {
        int indiceAleatorio = inicio + RANDOM.nextInt(fin - inicio + 1);
        intercambiar(lista, indiceAleatorio, fin);

        int pivote = lista.get(fin);
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (lista.get(j) <= pivote) {
                i++;
                intercambiar(lista, i, j);
            }
        }

        intercambiar(lista, i + 1, fin);
        return i + 1;
    }

    // ==================================================================
    // 6. SHELL SORT — igual que en ArraySorts
    // ==================================================================
    public static void shellSort(ArrayList<Integer> lista) {
        int n = lista.size();

        for (int salto = n / 2; salto > 0; salto = salto / 2) {
            for (int i = salto; i < n; i++) {
                int actual = lista.get(i);
                int j = i;

                while (j >= salto && lista.get(j - salto) > actual) {
                    lista.set(j, lista.get(j - salto));
                    j -= salto;
                }
                lista.set(j, actual);
            }
        }
    }

    // ==================================================================
    // UTILIDADES
    // ==================================================================

    private static void intercambiar(ArrayList<Integer> lista, int i, int j) {
        int temporal = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temporal);
    }

    public static boolean estaOrdenado(ArrayList<Integer> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            if (lista.get(i) > lista.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
