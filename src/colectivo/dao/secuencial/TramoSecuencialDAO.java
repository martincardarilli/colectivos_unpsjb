package colectivo.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import net.datastructures.TreeMap;
import colectivo.conexion.Factory;
import colectivo.dao.ParadaDao;
import colectivo.dao.TramoDao;
import colectivo.modelo.Parada;
import colectivo.modelo.Tramo;

/**
 * Implementación de la interfaz TramoDao que utiliza un archivo secuencial para almacenar datos de tramos.
 */

public class TramoSecuencialDAO implements TramoDao {

	private List<Tramo> list;
	private String name;
	private TreeMap<String, Parada> paradas;
	private boolean actualizar;

	public TramoSecuencialDAO() {
		paradas = cargarParada();
		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("tramo");
		actualizar = true;
	}

	private List<Tramo> readFromFile(String file) {
		List<Tramo> list = new ArrayList<>();
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(file));
			inFile.useDelimiter("\\s*;\\s*");
			while (inFile.hasNext()) {
				Tramo e = new Tramo(); 
				e.setInicio(paradas.get(inFile.next()));
				e.setFin(paradas.get(inFile.next()));
				e.setTipo(inFile.nextInt());
				e.setTiempo(inFile.nextInt());
				list.add(e);
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.err.println("Error opening file.");
			fileNotFoundException.printStackTrace();
		} catch (NoSuchElementException noSuchElementException) {
			System.err.println("Error in file record structure");
			noSuchElementException.printStackTrace();
		} catch (IllegalStateException illegalStateException) {
			System.err.println("Error reading from file.");
			illegalStateException.printStackTrace();
		} finally {
			if (inFile != null)
				inFile.close();
		}
		return list;
	}

	private void writeToFile(List<Tramo> list, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Tramo e : list) {
				outFile.format("%s;%s;%d;%d;\n", e.getInicio(), e.getFin(), e.getTipo(), e.getTiempo());
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
	public List<Tramo> buscarTodos() {
		if (actualizar) {
			list = readFromFile(name);
			actualizar = false;
		}
		return list;
	}

	@Override
	public void insertar(Tramo tramo) {
		list.add(tramo);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void actualizar(Tramo tramo) {
		int pos = list.indexOf(tramo);
		list.set(pos, tramo);
		writeToFile(list, name);
		actualizar = true;
	}

	@Override
	public void borrar(Tramo tramo) {
		list.remove(tramo);
		writeToFile(list, name);
		actualizar = true;
	}

	private TreeMap<String, Parada> cargarParada() {
	    TreeMap<String, Parada> paradas = new TreeMap<String, Parada>();
	    ParadaDao paradaDao = (ParadaSecuencialDAO) Factory.getInstancia("PARADA");
	    TreeMap<String, Parada> ds = paradaDao.buscarTodos(); 
	    for (Parada d : ds.values()) {
	        paradas.put(d.getId(), d);
	    }
	    return paradas;
	}

}
