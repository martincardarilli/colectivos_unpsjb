package colectivo.negocio;
import java.util.ArrayList;
import java.util.List;

import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.modelo.Tramo;
import colectivo.servicio.LineaService;
import colectivo.servicio.LineaServiceImpl;
import colectivo.servicio.ParadaService;
import colectivo.servicio.ParadaServiceImpl;
import colectivo.servicio.TramoService;
import colectivo.servicio.TramoServiceImpl;
import net.datastructures.TreeMap;

/**
 * La clase Empresa representa la entidad que gestiona las líneas, paradas y tramos del sistema de colectivos.
 * Contiene métodos para realizar operaciones CRUD en las líneas y ofrece acceso a la información de paradas y tramos.
 * Utiliza servicios para interactuar con la capa de persistencia y cargar los datos iniciales.
 *
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 */

public class Empresa {

    private static Empresa empresa = null;

    private String nombre;
    private TreeMap<String, Linea> lineas;
    private LineaService lineaService;
    private TreeMap<String, Parada> paradas;
    private ParadaService paradaService;
    private List<Tramo> tramos;
    private TramoService tramoService;

    public static Empresa getEmpresa() {
        if (empresa == null) {
            empresa = new Empresa();
        }
        return empresa;
    }

    private Empresa() {
        super();
        lineas = new TreeMap<String, Linea>();
        lineaService = new LineaServiceImpl();
        TreeMap<String, Linea> lineasData = lineaService.buscarTodos(); // Obtener datos de líneas

        for (Linea linea : lineasData.values()) { // Recorrer el TreeMap de líneas
            lineas.put(linea.getNombre(), linea);
        }

       
        paradas = new TreeMap<String, Parada>();
        paradaService = new ParadaServiceImpl();
        TreeMap<String, Parada> paradasData = paradaService.buscarTodos(); // Obtener datos de paradas

        for (Parada parada : paradasData.values()) { // Recorrer el TreeMap de paradas
            paradas.put(parada.getId(), parada);
           // System.out.println(parada); ACA SE DAN VUELTA Y DEJA DE QUEDAR ORDENADO ===== 1, 10, 11, ... , 19, 2, 20, ...
            // debido al KEY STRING
        }

        tramos = new ArrayList<Tramo>();
        tramoService = new TramoServiceImpl();
        tramos.addAll(tramoService.buscarTodos());
    }


    public void agregarLinea(Linea linea) throws LineaExisteException {
        if (lineas.get(linea.getNombre()) != null) {
            throw new LineaExisteException();
        }
        lineas.put(linea.getNombre(), linea); 
        lineaService.insertar(linea);
    }

    public void modificarLinea(Linea linea, String nombreOriginal) {   
        if (lineas.get(nombreOriginal) != null) {
        	System.out.println(lineas);
            //lineas.put(nombreOriginal, linea);
        	lineas.get(nombreOriginal).setNombre(linea.getNombre());
            lineaService.actualizar(linea);
            System.out.println(lineas);
        }
    }

    public void borrarLinea(Linea linea) {
        if (lineas.get(linea.getNombre()) != null) {
            lineas.remove(linea.getNombre());
            lineaService.borrar(linea);
        }
    }
    
    public Linea buscarLinea(Linea linea) {
        return lineas.get(linea.getNombre());
    }
    
    
    
    
    public void agregarParada(Parada parada) throws ParadaExisteException {
        if (paradas.get(parada.getId()) != null) {
            throw new ParadaExisteException();
        }
        paradas.put(parada.getId(), parada); 
        paradaService.insertar(parada);
    }

    public void modificarParada(Parada parada) {
    	

            //lineas.put(nombreOriginal, linea);
    	paradas.get(parada.getId());
        	paradaService.actualizar(parada);

    }

    public void borrarParada(Parada parada) {
    	
        if (paradas.get(parada.getId()) != null) {
        	paradas.remove(parada.getId());
            paradaService.borrar(parada);
        }
    }
    
    
  
    
    public Parada buscarParada(Parada parada) {
        return paradas.get(parada.getId());
    }
    
    
    
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TreeMap<String, Linea> getLineas() {

        return lineas;
    }

    public TreeMap<String, Parada> getParadas() {
        return paradas;
    }

    public List<Tramo> getTramos() {
        return new ArrayList<> (tramos);
    }
}