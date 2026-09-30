import java.util.Arrays;
import java.util.Random;

/*
 * Clase ArraySorts
 * Contiene los 6 metodos de ordenamiento del proyecto, implementados
 * a mano para arreglos (int[]). Ninguno usa Arrays.sort() ni ninguna
 * otra utileria de ordenamiento de Java, para que la medicion de
 * tiempos refleje nuestras propias implementaciones.
 *
 * Cada metodo ordena el arreglo "in place" (modifica el mismo arreglo
 * que recibe, no regresa uno nuevo). Por eso, cuando se van a comparar
 * las 12 implementaciones, cada una debe recibir su PROPIA copia de
 * los datos originales (esto se resuelve en el Main, mas adelante).
 */
public class ArraySorts {

    // ==================================================================
    // 1. BUBBLE SORT
    // Peor caso y caso promedio: O(n^2)
    // Mejor caso (arreglo ya ordenado): O(n), gracias a la bandera
    // "huboIntercambio" que corta el ciclo antes de tiempo.
    // ==================================================================
    public static void bubbleSort(int[] arreglo) {
        int n = arreglo.length;

        for (int i = 0; i < n - 1; i++) {
            boolean huboIntercambio = false;

            for (int j = 0; j < n - 1 - i; j++) {
                if (arreglo[j] > arreglo[j + 1]) {
                    intercambiar(arreglo, j, j + 1);
                    huboIntercambio = true;
                }
            }

            if (!huboIntercambio) {
                break; // ya quedo ordenado, no hace falta seguir revisando
            }
        }
    }

    // ==================================================================
    // 2. SELECTION SORT
    // Siempre O(n^2) en los 3 casos: aunque el arreglo ya venga
    // ordenado, de todas formas recorre todo para buscar el menor.
    // ==================================================================
    public static void selectionSort(int[] arreglo) {
        int n = arreglo.length;

        for (int i = 0; i < n - 1; i++) {
            int indiceMenor = i;

            for (int j = i + 1; j < n; j++) {
                if (arreglo[j] < arreglo[indiceMenor]) {
                    indiceMenor = j;
                }
            }

            if (indiceMenor != i) {
                intercambiar(arreglo, i, indiceMenor);
            }
        }
    }

    // ==================================================================
    // 3. INSERTION SORT
    // Mejor caso (ya ordenado): O(n).
    // Caso promedio y peor caso (orden inverso): O(n^2).
    // ==================================================================
    public static void insertionSort(int[] arreglo) {
        int n = arreglo.length;

        for (int i = 1; i < n; i++) {
            int actual = arreglo[i];
            int j = i - 1;

            // vamos recorriendo hacia atras, recorriendo lo ya ordenado,
            // y movemos los elementos mas grandes un lugar a la derecha
            while (j >= 0 && arreglo[j] > actual) {
                arreglo[j + 1] = arreglo[j];
                j--;
            }
            arreglo[j + 1] = actual;
        }
    }

    // ==================================================================
    // 4. MERGE SORT
    // Siempre O(n log n) en los 3 casos (mejor, promedio y peor).
    // Es "divide y venceras": parte el arreglo a la mitad una y otra
    // vez, hasta llegar a pedazos de 1 elemento, y despues los va
    // combinando (mezclando) ya ordenados.
    // ==================================================================
    public static void mergeSort(int[] arreglo) {
        if (arreglo.length < 2) {
            return; // un arreglo de 0 o 1 elementos ya esta ordenado
        }
        mergeSortRecursivo(arreglo, 0, arreglo.length - 1);
    }

    private static void mergeSortRecursivo(int[] arreglo, int inicio, int fin) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSortRecursivo(arreglo, inicio, medio);
            mergeSortRecursivo(arreglo, medio + 1, fin);
            combinar(arreglo, inicio, medio, fin);
        }
    }

    // combina 2 mitades YA ORDENADAS en una sola parte ordenada
    private static void combinar(int[] arreglo, int inicio, int medio, int fin) {
        int[] mitadIzquierda = Arrays.copyOfRange(arreglo, inicio, medio + 1);
        int[] mitadDerecha = Arrays.copyOfRange(arreglo, medio + 1, fin + 1);

        int i = 0;
        int j = 0;
        int k = inicio;

        while (i < mitadIzquierda.length && j < mitadDerecha.length) {
            if (mitadIzquierda[i] <= mitadDerecha[j]) {
                arreglo[k] = mitadIzquierda[i];
                i++;
            } else {
                arreglo[k] = mitadDerecha[j];
                j++;
            }
            k++;
        }

        // copiamos lo que haya sobrado de cada mitad
        while (i < mitadIzquierda.length) {
            arreglo[k] = mitadIzquierda[i];
            i++;
            k++;
        }
        while (j < mitadDerecha.length) {
            arreglo[k] = mitadDerecha[j];
            j++;
            k++;
        }
    }

    // ==================================================================
    // 5. QUICK SORT
    // Caso promedio: O(n log n).
    // Peor caso teorico: O(n^2) (cuando las particiones quedan muy
    // desbalanceadas). Aqui se usan 2 tecnicas para evitar que ese
    // peor caso nos cause problemas en la practica:
    //
    //   a) Pivote ALEATORIO: evita que un arreglo ya ordenado, en
    //      orden inverso, o con muchos valores repetidos, fuerce
    //      siempre el peor caso (esto era una version anterior sin
    //      pivote aleatorio, y SI truena con 100,000 elementos entre
    //      1 y 5 -- fue detectado durante las pruebas de este mismo
    //      proyecto, por eso se corrigio).
    //
    //   b) Recursion solo en el lado MAS CHICO de la particion, y
    //      seguir con un ciclo (iterativo) en el lado mas grande. Con
    //      esto, la profundidad de la recursion nunca pasa de
    //      O(log n), sin importar que tan desbalanceada quede la
    //      particion, asi que nunca se desborda el stack.
    // ==================================================================
    private static final Random RANDOM = new Random();

    public static void quickSort(int[] arreglo) {
        if (arreglo.length < 2) {
            return;
        }
        quickSortRecursivo(arreglo, 0, arreglo.length - 1);
    }

    private static void quickSortRecursivo(int[] arreglo, int inicio, int fin) {
        while (inicio < fin) {
            int posicionPivote = particionar(arreglo, inicio, fin);

            // comparamos el tamanio de las 2 mitades y solo hacemos
            // recursion de verdad en la mas chica
            if (posicionPivote - inicio < fin - posicionPivote) {
                quickSortRecursivo(arreglo, inicio, posicionPivote - 1);
                inicio = posicionPivote + 1; // la mitad grande se resuelve en la siguiente vuelta del while
            } else {
                quickSortRecursivo(arreglo, posicionPivote + 1, fin);
                fin = posicionPivote - 1;
            }
        }
    }

    // acomoda todo lo menor o igual al pivote a su izquierda, y todo lo
    // mayor a su derecha. Regresa la posicion final del pivote.
    private static int particionar(int[] arreglo, int inicio, int fin) {
        // elegimos un indice al azar dentro del rango y lo mandamos al
        // final, para que actue como pivote (en vez de usar siempre
        // el ultimo elemento tal cual)
        int indiceAleatorio = inicio + RANDOM.nextInt(fin - inicio + 1);
        intercambiar(arreglo, indiceAleatorio, fin);

        int pivote = arreglo[fin];
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (arreglo[j] <= pivote) {
                i++;
                intercambiar(arreglo, i, j);
            }
        }

        intercambiar(arreglo, i + 1, fin);
        return i + 1;
    }

    // ==================================================================
    // 6. SHELL SORT
    // Con la secuencia de saltos n/2, n/4, ..., 1 (la clasica), el
    // peor caso es O(n^2), aunque en la practica suele comportarse
    // bastante mejor que insertion sort normal.
    // ==================================================================
    public static void shellSort(int[] arreglo) {
        int n = arreglo.length;

        for (int salto = n / 2; salto > 0; salto = salto / 2) {
            // esto es como un insertion sort, pero comparando elementos
            // que estan separados por "salto" posiciones, no vecinos
            for (int i = salto; i < n; i++) {
                int actual = arreglo[i];
                int j = i;

                while (j >= salto && arreglo[j - salto] > actual) {
                    arreglo[j] = arreglo[j - salto];
                    j -= salto;
                }
                arreglo[j] = actual;
            }
        }
    }

    // ==================================================================
    // UTILIDADES
    // ==================================================================

    private static void intercambiar(int[] arreglo, int i, int j) {
        int temporal = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temporal;
    }

    // se usa mas adelante para que cada hilo verifique que su propia
    // implementacion si dejo el arreglo bien ordenado
    public static boolean estaOrdenado(int[] arreglo) {
        for (int i = 0; i < arreglo.length - 1; i++) {
            if (arreglo[i] > arreglo[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
