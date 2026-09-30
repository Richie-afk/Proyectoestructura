/*
 * Clase SortResult
 * Guarda el resultado de UNA de las 12 implementaciones: que
 * algoritmo fue, con que estructura de datos (Arreglo o ArrayList),
 * cuanto tiempo tardo, y si de verdad dejo la coleccion ordenada.
 */
public class SortResult {

    String nombreAlgoritmo;
    String estructura;
    double tiempoMs;
    boolean ordenoCorrectamente;

    public SortResult(String nombreAlgoritmo, String estructura, double tiempoMs, boolean ordenoCorrectamente) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.estructura = estructura;
        this.tiempoMs = tiempoMs;
        this.ordenoCorrectamente = ordenoCorrectamente;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-10s %10.2f ms   %s",
                nombreAlgoritmo, estructura, tiempoMs, ordenoCorrectamente ? "Si" : "NO (error)");
    }
}
