package colectivo.dao.secuencial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;
import net.datastructures.TreeMap;
import colectivo.dao.ParadaDao;
import colectivo.modelo.Parada;

/**
 * Implementación de la interfaz ParadaDao que utiliza un archivo secuencial para almacenar datos de paradas.
 */

public class ParadaSecuencialDAO implements ParadaDao {

	private TreeMap<String, Parada> paradas; 
	private String name;

	private boolean actualizar;

	public ParadaSecuencialDAO() {
		paradas = new TreeMap<String, Parada>(); 

		ResourceBundle rb = ResourceBundle.getBundle("secuencial");
		name = rb.getString("parada");
		actualizar = true;
	}

	private TreeMap<String, Parada> readFromFile(String file) {
		TreeMap<String, Parada> paradas = new TreeMap<String, Parada>();
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(file));
			inFile.useDelimiter("\\s*;\\s*");
			while (inFile.hasNext()) {
				Parada e = new Parada();
				e.setId(inFile.next());
				e.setDireccion(inFile.next());
				e.setLat(inFile.nextDouble());
				e.setLng(inFile.nextDouble());
				paradas.put(e.getId(), e); // Agregar a TreeMap
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
		return paradas;
	}

	private void writeToFile(TreeMap<String, Parada> paradas2, String file) {
		Formatter outFile = null;
		try {
			outFile = new Formatter(file);
			for (Parada e : paradas2.values()) { 
				outFile.format("%s;%s;%s;%s\n", e.getId(), e.getDireccion(), String.valueOf(e.getLat()), String.valueOf(e.getLng()));
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

	public TreeMap<String, Parada> buscarTodos() {
		if (actualizar) {
			paradas = readFromFile(name);
			actualizar = false;
		}
		return paradas;
	}

	public void insertar(Parada parada) {
		paradas.put(parada.getId(), parada); 
		writeToFile(paradas, name);
		actualizar = true;
	}

	public void actualizar(Parada parada) {
		String id = parada.getId();
		paradas.put(id, parada);
		writeToFile(paradas, name);
		actualizar = true;
	}

	public void borrar(Parada parada) {
		paradas.remove(parada.getId());
		writeToFile(paradas, name);
		actualizar = true;
	}

}
