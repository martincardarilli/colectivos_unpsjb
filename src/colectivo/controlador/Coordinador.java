package colectivo.controlador;

import java.util.List;

import javax.swing.JOptionPane;

import java.util.ArrayList;

import colectivo.interfaz.Interfaz;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.modelo.Tramo;
import colectivo.negocio.Calculo;
import colectivo.negocio.Empresa;
import net.datastructures.TreeMap;
import colectivo.negocio.LineaExisteException;
import colectivo.negocio.ParadaExisteException;
import colectivo.util.Time;
import colectivo.gui.datos.consulta.ConsultaForm;

//import colectivo.gui.datos.consulta.ResultadoForm;

import colectivo.gui.datos.datos.LineaForm;
import colectivo.gui.datos.datos.LineaList;
import colectivo.gui.datos.datos.LineaParadasForm;
import colectivo.gui.datos.datos.ParadaForm;
import colectivo.gui.datos.datos.ParadaList;

/**
 * La clase AplicacionConsultas es la clase principal que inicia la aplicación de consultas para el sistema de colectivos.
 * Se encarga de inicializar y relacionar las instancias de las clases de lógica, vista y controlador,
 * así como de establecer las relaciones entre ellas para el funcionamiento de la aplicación.
 * Esta clase contiene el método `main` que se ejecuta al iniciar la aplicación.
 * 
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 * 
 */
public class Coordinador {

	private Empresa empresa;
	private Calculo calculo;

	private Interfaz interfaz;

	private ConsultaForm consultaForm;

	private LineaList lineaList;
	private LineaForm lineaForm;
	
	private LineaParadasForm lineaParadasForm;
	
	private ParadaList paradaList;
	private ParadaForm paradaForm;

	List<List<Tramo>> resultadoTEMP;

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Calculo getCalculo() {
		return calculo;
	}

	public void setCalculo(Calculo calculo) {
		this.calculo = calculo;
	}

	public Interfaz getInterfaz() {
		return interfaz;
	}

	public void setInterfaz(Interfaz interfaz) {
		this.interfaz = interfaz;
	}
	
	
	public void setVisibleInterfaz() {
		interfaz.setVisible(true);	
		consultaForm.verDatos(resultadoTEMP, Time.toMins(consultaForm.getHours()),listarLineas());
	}
	

	// Interfaz

	// LineaList

	// Empresa
	
	public String convertirMinutosAHorario(int minutos) {
	    int horas = minutos / 60;
	    int min = minutos % 60;
	    return String.format("%02d:%02d", horas, min);
	}
	
	public int convertirHorarioAMinutos(String horario) {
	    String[] partes = horario.split(":");
	    int horas = Integer.parseInt(partes[0]);
	    int minutos = Integer.parseInt(partes[1]);
	    return horas * 60 + minutos;
	}

	public Linea buscarLinea(Linea linea) {
		return empresa.buscarLinea(linea);
	}
	
	public Parada buscarParada(Parada parada) {
		return empresa.buscarParada(parada);
	}

	public TreeMap<String, Linea> listarLineas() {
		return empresa.getLineas();
	}

	public TreeMap<String, Parada> listarParadas() {
		return empresa.getParadas();
	}

	/* VERSION 2 GUI-ABM AplicacionConsultas.java */
	public List<Linea> listarLineas2() {
		// Obtener las líneas de la empresa como un TreeMap
		TreeMap<String, Linea> lineasTreeMap = empresa.getLineas();

		// Crear una lista para almacenar las líneas
		List<Linea> lineasList = new ArrayList<Linea>();

		// Agregar los elementos del TreeMap a la lista
		for (Linea linea : lineasTreeMap.values()) {
			lineasList.add(linea);
		}

		// Devolver la lista de líneas
		return lineasList;
	}

	/* VERSION 2 GUI-ABM AplicacionConsultas.java */
	public List<Parada> listarParadas2() {
		// Obtener las paradas de la empresa como un TreeMap
		TreeMap<String, Parada> paradasTreeMap = empresa.getParadas();

		// Crear una lista para almacenar las paradas
		List<Parada> paradasList = new ArrayList<>();

		// Agregar los elementos del TreeMap a la lista
		for (Parada parada : paradasTreeMap.values()) {
			paradasList.add(parada);
		}

		// Devolver la lista de paradas
		return paradasList;
	}

	public List<Tramo> listarTramos() {
		return empresa.getTramos();
	}

	// GUI Consulta

	public ConsultaForm getConsultaForm() {
		return consultaForm;
	}

	public void setConsultaForm(ConsultaForm consultaForm) {
		this.consultaForm = consultaForm;
	}

	// GUI Datos

	public LineaList getLineaList() {
		return lineaList;
	}

	public void setLineaList(LineaList lineaList) {
		this.lineaList = lineaList;
	}

	public LineaForm getLineaForm() {
		return lineaForm;
	}

	public void setLineaForm(LineaForm lineaForm) {
		this.lineaForm = lineaForm;
	}
	
	
	
	
	public LineaParadasForm getLineaParadasForm() {
		return lineaParadasForm;
	}

	public void setLineaParadasForm(LineaParadasForm lineaParadasForm) {
		this.lineaParadasForm = lineaParadasForm;
	}

	public ParadaForm getParadaForm() {
		return paradaForm;
	}

	public void setParadaList(ParadaList paradaList){
		this.paradaList = paradaList;
	}

	
	public void setParadaForm(ParadaForm paradaForm){
		this.paradaForm = paradaForm;
	}

	// DesktopFrame Consulta

	public void mostrarConsulta() {
		consultaForm.accion();
		consultaForm.setVisible(true);
	}

	// ConsultaForm

	public void cancelarConsulta() {
		consultaForm.setVisible(false);
	}

	public void masRapido(Parada parada1, Parada parada2) {
		
		System.out.println("INICIO");
		
		int opcion = Constantes.RECORRIDOS;
		// Realizar c�lculo
		List<List<Tramo>> recorridos = null;
		if (opcion == Constantes.RECORRIDOS)
			recorridos = calculo.recorridos(parada1,parada2, Time.toMins(consultaForm.getHours()), 2);

		// Mostrar resultado
		Interfaz.resultado(recorridos, Time.toMins(consultaForm.getHours()),listarLineas());

		//List<Tramo> resultado = calculo.masRapido(parada1, parada2);
		
		//System.out.println(recorridos.get(0));
		this.resultadoTEMP = recorridos;
		//this.consultar(parada1, parada2);
		if (!recorridos.isEmpty()) {			
		System.out.println("cantidad de recorridos posibles = "+recorridos.size());
		interfaz.mostrarResultado(recorridos.get(0));
		}else{
			System.out.println("recorridos.isEmpty() = true = esta vacio = size = 0 (no encontro caminos posibles)");
			JOptionPane.showMessageDialog(null, "No se encontro camino posible");
			//logger.error("Error al borrar linea");
			return;
		}

	}

	// esto tiraba throw import colectivo.negocio.LineaExisteException;
	/*private void consultar(Parada origen, Parada destino) {

		// Ingreso datos usuario
		int opcion = interfaz.opcion();

		// Realizar calculo
		calculo.cargarDatos( this.listarParadas(),this.listarLineas(),this.listarTramos());

		List<List<Tramo>> recorrido = null;
		if (opcion == Constantes.MAS_RAPIDO)
			recorrido = calculo.recorridos1(origen, destino,Time.toMins("10:25"), 2);
		// Mostrar resultado
		Interfaz.resultado(recorrido, Time.toMins("10:25"),listarLineas());

	}*/

	// DesktopFrame Datos

	public void mostrarLineaList() {
		lineaList.loadTable();
		lineaList.setVisible(true);
	}

	public void mostrarParadaList() {
		paradaList.loadTable();
		paradaList.setVisible(true);
	}
	
	// LineaList

	public void insertarLineaForm() {
		lineaForm.accion(Constantes.INSERTAR, null);
		lineaForm.setVisible(true);
	}

	public void modificarLineaForm(Linea linea) {
		lineaForm.accion(Constantes.MODIFICAR, linea);
		lineaForm.setVisible(true);
	}
	
	public void modificarLineaParadasForm(Linea linea) {
		//lineaParadasForm.accion(Constantes.MODIFICAR, linea);

		lineaParadasForm.cargarTodasLasParadas(this.listarParadas2()); // listaDeTodasLasParadas debe ser obtenida de algún lugar
		lineaParadasForm.cargarLinea(linea);
		lineaParadasForm.setVisible(true);
	}
	
	public void actualizarLineaConParadas(Linea linea) {
	    // Aquí actualizas la información de la línea en la base de datos o donde corresponda
	    empresa.modificarLinea(linea, linea.getNombre()); // Reutiliza el método de modificar línea
	    lineaList.loadTable();
	}

	public void borrarLineaForm(Linea linea) {
		lineaForm.accion(Constantes.BORRAR, linea);
		lineaForm.setVisible(true);
	}
	
	// paradaList

		public void insertarParadaForm() {
			paradaForm.accion(Constantes.INSERTAR, null);
			paradaForm.setVisible(true);
		}

		public void modificarParadaForm(Parada parada) {
			paradaForm.accion(Constantes.MODIFICAR, parada);
			paradaForm.setVisible(true);
		}

		public void borrarParadaForm(Parada parada) {
			paradaForm.accion(Constantes.BORRAR, parada);
			paradaForm.setVisible(true);
		}

	// LineaForm

	public void cancelarLinea() {
		lineaForm.setVisible(false);
	}

	public void insertarLinea(Linea linea) throws LineaExisteException {
		empresa.agregarLinea(linea);
		lineaForm.setVisible(false);
		lineaList.addRow(linea);
	}

	public void modificarLinea(Linea linea, String nombreOriginal) {
		System.out.println(linea);
		empresa.modificarLinea(linea, nombreOriginal);
		lineaList.setAccion(Constantes.MODIFICAR);
		lineaList.setLinea(linea);
		lineaForm.setVisible(false);
	}

	public void borrarLinea(Linea linea) {
		empresa.borrarLinea(linea);
		lineaList.setAccion(Constantes.BORRAR);
		lineaForm.setVisible(false);
	}
	
	// paradaForm

		public void cancelarparada() {
			lineaForm.setVisible(false);
		}

		public void insertarParada(Parada parada) throws ParadaExisteException {
			empresa.agregarParada(parada);
			paradaForm.setVisible(false);
			paradaList.addRow(parada);
		}

		public void modificarParada(Parada parada) {
			System.out.println(parada);
			empresa.modificarParada(parada);
			paradaList.setAccion(Constantes.MODIFICAR);
			paradaList.setParada(parada);
			paradaForm.setVisible(false);
		}

		public void borrarParada(Parada parada) {
			empresa.borrarParada(parada);
			paradaList.setAccion(Constantes.BORRAR);
			paradaForm.setVisible(false);
		}

}
