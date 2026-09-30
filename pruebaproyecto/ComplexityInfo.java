/*
 * Clase ComplexityInfo
 * Guarda y muestra la complejidad TEORICA (notacion Big O) de cada
 * uno de los 6 algoritmos. Esta es la version resumida que se
 * imprime junto con los resultados; el analisis completo (con la
 * explicacion de cada caso y la comparacion contra los tiempos
 * medidos) va en el reporte de la actividad.
 */
public class ComplexityInfo {

    public static void mostrarTabla() {
        System.out.println();
        System.out.println("COMPLEJIDAD TEORICA (notacion Big O)");
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("%-16s %-14s %-16s %-14s%n", "Algoritmo", "Mejor caso", "Caso promedio", "Peor caso");
        System.out.println("-------------------------------------------------------------------");
        imprimirFila("Bubble Sort", "O(n)", "O(n^2)", "O(n^2)");
        imprimirFila("Selection Sort", "O(n^2)", "O(n^2)", "O(n^2)");
        imprimirFila("Insertion Sort", "O(n)", "O(n^2)", "O(n^2)");
        imprimirFila("Merge Sort", "O(n log n)", "O(n log n)", "O(n log n)");
        imprimirFila("Quick Sort", "O(n log n)", "O(n log n)", "O(n^2) *");
        imprimirFila("Shell Sort", "O(n log n)", "~O(n^1.3)", "O(n^2)");
        System.out.println();
        System.out.println("* Quick Sort se implemento con pivote ALEATORIO, lo que hace que el");
        System.out.println("  peor caso sea extremadamente improbable en la practica (ver reporte");
        System.out.println("  para el analisis completo, incluyendo por que era necesario).");
    }

    private static void imprimirFila(String algoritmo, String mejor, String promedio, String peor) {
        System.out.printf("%-16s %-14s %-16s %-14s%n", algoritmo, mejor, promedio, peor);
    }
}
