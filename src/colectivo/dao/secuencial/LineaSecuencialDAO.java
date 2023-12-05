package colectivo.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Scanner;

import colectivo.conexion.Factory;
import colectivo.dao.LineaDao;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.util.Time;
import net.datastructures.TreeMap;
import colectivo.dao.ParadaDao;
/**
 * Implementación de la interfaz LineaDao que utiliza un archivo secuencial para almacenar datos de líneas de colectivo.
 */
public class LineaSecuencialDAO implements LineaDao {

	private TreeMap<String, Linea> lineas;
	private Hashtable<String, Parada> paradas;
	private String name;
	private boolean actualizar;

	public LineaSecuencialDAO() {
		lineas = new TreeMap<String, Linea>();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		paradas = cargarParadas();
		name = rb.getString("linea");
		actualizar = true;
	}
	
	
	
	private TreeMap<String, Linea> readFromFile(String file) {
		Scanner inFile = null;
		TreeMap<String, Linea> lineas = new TreeMap<String, Linea>();

		try {
			inFile = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String registro;
		Linea linea;
		while (inFile.hasNext()) {
			registro = inFile.next();
			String[] campos = registro.split(";");
			linea = new Linea(campos[0],Time.toMins(campos[1]), Time.toMins(campos[2]), Integer.valueOf(campos[3]));	
			
			for (int i = 4; i < campos.length; i++) {
								linea.agregarParada(paradas.get(campos[i]));
			}

			
			lineas.put(campos[0], linea);
		}
		inFile.close();

		return lineas;
	}
	
	private String convertirMinutosAHorario(int minutos) {
	    int horas = minutos / 60;
	    int min = minutos % 60;
	    return String.format("%02d:%02d", horas, min);
	}

	private void writeToFile(TreeMap<String, Linea> lineas, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Linea e : lineas.values()) {
				outFile.format("%s;", e.getNombre());
				
				outFile.format("%s;", this.convertirMinutosAHorario(e.getComienza()));
				
				outFile.format("%s;", this.convertirMinutosAHorario(e.getFinaliza()));
				
				outFile.format("%s;", e.getFrecuencia());
				

			
				 for (int i=0;i< e.getParadas().size();i++) {
				      outFile.format("%s;", e.getParadas().get(i).getId());

				 }
				 
				 outFile.format("\n");
			
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error creating file.");
		} catch (FormatterClosedException formatterClosedException) {
			System.err.println("Error writing to file.");
		} finally {
			if (outFile != null)
				outFile.close();
		}
	}

	@Override
	public TreeMap<String, Linea> buscarTodos() {
		if (actualizar) {
			lineas = readFromFile(name);
			actualizar = false;
		}
		return lineas;
	}

	@Override
	public void insertar(Linea linea) {
		lineas.put(linea.getNombre(), linea);
		writeToFile(lineas, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Linea linea) {
		//lineas.put(linea.getNombre(), linea);
		writeToFile(lineas, name);
		actualizar = true;
	}

	@Override
	public void borrar(Linea linea) {
		lineas.remove(linea.getNombre());
		writeToFile(lineas, name);
		actualizar = true;
	}

	public Hashtable<String, Parada> cargarParadas() {
		Hashtable<String, Parada> paradas = new Hashtable<String, Parada>();
		ParadaDao paradaDAO = (ParadaSecuencialDAO) Factory.getInstancia("PARADA");
		TreeMap<String, Parada> ds = paradaDAO.buscarTodos();
		for (Parada d : ds.values()) {
			paradas.put(d.getId(), d);
			
		}
	
		return paradas;
	}
}
