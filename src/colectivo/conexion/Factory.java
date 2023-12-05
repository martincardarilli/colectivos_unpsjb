package colectivo.conexion;

import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * Clase Factory para crear instancias de diversos objetos.
 *
 * <p>Esta clase utiliza una Hashtable para almacenar y recuperar instancias de objetos basadas en sus nombres.
 * Proporciona un método {@code getInstancia} para obtener una instancia de un objeto especificado.
 *
 * <p>Autores: maicac, martincardarilli, mpacheco.
 */
public class Factory {
	private static Hashtable<String, Object> instancias = new Hashtable<String, Object>();

	 /**
     * Obtiene una instancia del objeto especificado.
     *
     *@param objName El nombre del objeto del cual se solicita una instancia.
     * @return Una instancia del objeto especificado.
     * @throws RuntimeException Si ocurre alguna excepción durante el proceso de instanciación.
     */
	@SuppressWarnings("deprecation")
	public static Object getInstancia(String objName) {
		try {
			// verifico si existe un objeto relacionado a objName
			// en la hashtable
			Object obj = instancias.get(objName);
			// si no existe entonces lo instancio y lo agrego
			if (obj == null) {
				ResourceBundle rb = ResourceBundle.getBundle("factory");
				String sClassname = rb.getString(objName);
				obj = Class.forName(sClassname).newInstance();
				// agrego el objeto a la hashtable
				instancias.put(objName, obj);
			}
			return obj;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}