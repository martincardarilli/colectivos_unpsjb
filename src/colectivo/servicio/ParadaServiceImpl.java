package colectivo.servicio;


import net.datastructures.TreeMap;
import colectivo.conexion.Factory;
import colectivo.dao.ParadaDao;
import colectivo.dao.secuencial.ParadaSecuencialDAO;
import colectivo.modelo.Parada;

/**
 * La clase ParadaServiceImpl proporciona implementaciones concretas de los métodos definidos en la interfaz ParadaService.
 * Se encarga de gestionar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para las paradas del sistema de colectivos.
 * Utiliza un objeto ParadaDao para interactuar con el almacenamiento de datos y realizar operaciones en la capa de acceso a datos.
 * Esta implementación específica utiliza un ParadaSecuencialDAO para acceder a las paradas de manera secuencial.
 * La creación de instancias de esta clase se realiza mediante el uso de Factory para obtener una instancia de ParadaDao.
 * 
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 */
public class ParadaServiceImpl implements ParadaService {

	private ParadaDao paradaDao; 
		
	public ParadaServiceImpl(){
		paradaDao = (ParadaSecuencialDAO)Factory.getInstancia("PARADA");
	}
	
	@Override
	public void insertar(Parada parada) {
		paradaDao.insertar(parada);				
	}

	@Override
	public void actualizar(Parada parada) {
		paradaDao.actualizar(parada);						
	}

	@Override
	public void borrar(Parada parada) {
		paradaDao.borrar(parada);
		
	}

	@Override
	public TreeMap<String, Parada> buscarTodos() {
		return paradaDao.buscarTodos();
		
	}

}
