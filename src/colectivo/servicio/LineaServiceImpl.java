package colectivo.servicio;


import net.datastructures.TreeMap;
import colectivo.conexion.Factory;
import colectivo.dao.LineaDao;
import colectivo.dao.secuencial.LineaSecuencialDAO;
import colectivo.modelo.Linea;
/**
 * La clase LineaServiceImpl proporciona implementaciones concretas de los métodos definidos en la interfaz LineaService.
 * Se encarga de gestionar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para las líneas del sistema de colectivos.
 * Utiliza un objeto LineaDao para interactuar con el almacenamiento de datos y realizar operaciones en la capa de acceso a datos.
 * Esta implementación específica utiliza un LineaSecuencialDAO para acceder a las líneas de manera secuencial.
 * La creación de instancias de esta clase se realiza mediante el uso de Factory para obtener una instancia de LineaDao.
 * 
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 */
public class LineaServiceImpl implements LineaService {

	private LineaDao lineaDao; 
		
	public LineaServiceImpl(){
		lineaDao = (LineaSecuencialDAO)Factory.getInstancia("LINEA");
	}
	
	@Override
	public void insertar(Linea linea) {
		lineaDao.insertar(linea);				
	}

	@Override
	public void actualizar(Linea linea) {
		lineaDao.actualizar(linea);						
	}

	@Override
	public void borrar(Linea linea) {
		lineaDao.borrar(linea);	
	}

	@Override
	public TreeMap<String, Linea> buscarTodos() {
		return lineaDao.buscarTodos();
		
	}

}
