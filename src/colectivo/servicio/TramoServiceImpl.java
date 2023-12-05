package colectivo.servicio;

import java.util.List;

import colectivo.conexion.Factory;
import colectivo.dao.TramoDao;
import colectivo.dao.secuencial.TramoSecuencialDAO;
import colectivo.modelo.Tramo;

/**
 * La clase TramoServiceImpl proporciona implementaciones concretas de los métodos definidos en la interfaz TramoService.
 * Se encarga de gestionar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los tramos del sistema de colectivos.
 * Utiliza un objeto TramoDao para interactuar con el almacenamiento de datos y realizar operaciones en la capa de acceso a datos.
 * Esta implementación específica utiliza un TramoSecuencialDAO para acceder a los tramos de manera secuencial.
 * La creación de instancias de esta clase se realiza mediante el uso de Factory para obtener una instancia de TramoDao.
 * 
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 * 
 */
public class TramoServiceImpl implements TramoService {

	private TramoDao tramoDao; 
		
	public TramoServiceImpl(){
		tramoDao = (TramoSecuencialDAO) Factory.getInstancia("TRAMO");
	}
	
	@Override
	public void insertar(Tramo tramo) {
		tramoDao.insertar(tramo);				
	}

	@Override
	public void actualizar(Tramo tramo) {
		tramoDao.actualizar(tramo);						
	}

	@Override
	public void borrar(Tramo tramo) {
		tramoDao.borrar(tramo);
		
	}

	@Override
	public List<Tramo> buscarTodos() {
		return tramoDao.buscarTodos();
		
	}

}
