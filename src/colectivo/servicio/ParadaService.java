package colectivo.servicio;


import net.datastructures.TreeMap;

import colectivo.modelo.Parada;

public interface ParadaService {

	void insertar(Parada parada);

	void actualizar(Parada parada);

	void borrar(Parada parada);

	TreeMap<String, Parada> buscarTodos();

}
