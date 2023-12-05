package colectivo.controlador;

import colectivo.gui.datos.consulta.ConsultaForm;

import colectivo.gui.datos.datos.LineaForm;
import colectivo.gui.datos.datos.LineaList;
import colectivo.gui.datos.datos.ParadaForm;
import colectivo.gui.datos.datos.ParadaList;
import colectivo.interfaz.Interfaz;
import colectivo.negocio.Calculo;
import colectivo.negocio.Empresa;
import colectivo.negocio.LineaExisteException;

/**
 * La clase AplicacionConsultas es la clase principal que inicia la aplicación
 * de consultas para el sistema de colectivos. Se encarga de inicializar y
 * relacionar las instancias de las clases de lógica, vista y controlador, así
 * como de establecer las relaciones entre ellas para el funcionamiento de la
 * aplicación. Esta clase contiene el método `main` que se ejecuta al iniciar la
 * aplicación.
 * 
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 * 
 */
public class AplicacionConsultas {

	// logica
	private Empresa empresa;
	private Calculo calculo;

	// vista
	private ConsultaForm consultaForm;

	private LineaList lineaList;
	private LineaForm lineaForm;
	
	private ParadaList paradaList;
	private ParadaForm paradaForm;

	// mapa
	private Interfaz interfaz;

	// controlador
	private Coordinador coordinador;

	public static void main(String[] args) throws LineaExisteException {
		AplicacionConsultas miAplicacion = new AplicacionConsultas();
		miAplicacion.iniciar();
	}

	private void iniciar() throws LineaExisteException {
		/* Se instancian las clases */
		
		/* PATRON DE DISEÑO: Singleton */
		empresa = Empresa.getEmpresa();
		
		calculo = new Calculo();
		
		coordinador = new Coordinador();
		
		consultaForm = new ConsultaForm();
		lineaList = new LineaList();
		lineaForm = new LineaForm();
		paradaList = new ParadaList();
		paradaForm = new ParadaForm();
		interfaz = new Interfaz();

		/* Se establecen las relaciones entre clases */
		coordinador.setConsultaForm(consultaForm);
		calculo.setCoordinador(coordinador);
		consultaForm.setCoordinador(coordinador);

		/* Se establecen relaciones con la clase coordinador */
		coordinador.setEmpresa(empresa);
		coordinador.setCalculo(calculo);


		coordinador.setLineaList(lineaList);
		lineaList.setCoordinador(coordinador);
		
		coordinador.setLineaForm(lineaForm);
		lineaForm.setCoordinador(coordinador);
		
		coordinador.setParadaList(paradaList);
		paradaList.setCoordinador(coordinador);
		
		coordinador.setParadaForm(paradaForm);
		paradaForm.setCoordinador(coordinador);


		coordinador.setInterfaz(interfaz);
		interfaz.setCoordinador(coordinador);


		calculo.cargarDatos(coordinador.listarParadas(),coordinador.listarLineas(),coordinador.listarTramos());
		coordinador.mostrarConsulta();

	}

}
