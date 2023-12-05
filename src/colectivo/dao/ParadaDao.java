package colectivo.dao;

import net.datastructures.TreeMap;
import colectivo.modelo.Parada;

/**
 * Interfaz para acceder a la capa de datos de la entidad Parada.
 */
public interface ParadaDao {

	void insertar(Parada parada);

	void actualizar(Parada parada);

	void borrar(Parada parada);

	TreeMap<String, Parada> buscarTodos();

}
