package colectivo.dao;

import java.util.List;

import colectivo.modelo.Tramo;

/**
 * Interfaz para acceder a la capa de datos de la entidad Tramo.
 */
public interface TramoDao {

	void insertar(Tramo tramo);

	void actualizar(Tramo tramo);

	void borrar(Tramo tramo);

	List<Tramo> buscarTodos();
}
