package colectivo.gui.datos.consulta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import colectivo.controlador.Coordinador;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;
import colectivo.modelo.Tramo;
import colectivo.util.Time;
import net.datastructures.TreeMap;

/**
 * La clase ConsultaForm representa la interfaz de usuario para realizar consultas de rutas en el sistema de colectivos.
 * Proporciona opciones para seleccionar paradas de origen y destino, ingresar la hora de subida y realizar consultas de rutas rápidas.
 * Muestra resultados y estadísticas de la consulta.
 *
 * @author maiac
 * @author martincardarilli
 * @author mpacheco
 */
public class ConsultaForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private Coordinador coordinador;

	private JPanel contentPane;

	private JButton btnRapido;
	private JLabel lblParada1;
	private JLabel lblParada2;
	private JComboBox<Parada> cbxParada1;
	private JComboBox<Parada> cbxParada2;
	private List<Parada> paradas;

	private JTextArea txtResultado;
	private JProgressBar progressBar;
	private JLabel lblBarraProgreso;

	private JLabel lblHoraLlegada;
	private JTextField txtHoraLlegada;

	/**
	 * Create the frame.
	 */
	public ConsultaForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 600);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu_1 = new JMenu("Aplicacion");
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Salir");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(NORMAL);
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_1);

		JMenu mnNewMenu = new JMenu("Datos");
		menuBar.add(mnNewMenu);

		JMenuItem mntmLineas = new JMenuItem("Lineas");
		mntmLineas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				coordinador.mostrarLineaList();
			}
		});
		mnNewMenu.add(mntmLineas);

		JMenuItem mntmParadas = new JMenuItem("Paradas");
		mntmParadas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				coordinador.mostrarParadaList();
			}
		});
		mnNewMenu.add(mntmParadas);

		//JMenuItem mntmTramos = new JMenuItem("Tramos");
		//mnNewMenu.add(mntmTramos);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Handler handler = new Handler();

		txtResultado = new JTextArea();
		JScrollPane scroll = new JScrollPane(txtResultado);
		scroll.setBounds(29, 253, 592, 171);
		contentPane.add(scroll);

		btnRapido = new JButton("Mas Rapido");
		btnRapido.setBounds(235, 155, 156, 32);
		contentPane.add(btnRapido);
		btnRapido.addActionListener(handler);

		lblParada1 = new JLabel("Parada Origen:");
		lblParada1.setBounds(34, 35, 118, 14);
		contentPane.add(lblParada1);

		lblParada2 = new JLabel("Parada Destino:");
		lblParada2.setBounds(34, 71, 118, 14);
		contentPane.add(lblParada2);

		cbxParada1 = new JComboBox<Parada>();
		cbxParada1.setBounds(162, 31, 390, 22);
		contentPane.add(cbxParada1);

		cbxParada2 = new JComboBox<Parada>();
		cbxParada2.setBounds(162, 67, 390, 22);
		contentPane.add(cbxParada2);

		lblHoraLlegada = new JLabel("Hora de subida:");
		lblHoraLlegada.setBounds(34, 107, 118, 14);
		contentPane.add(lblHoraLlegada);

		txtHoraLlegada = new JTextField();
		txtHoraLlegada.setBounds(162, 103, 100, 22);
		contentPane.add(txtHoraLlegada);
		txtHoraLlegada.setText("14:00");

		// Inicialización de lblBarraProgreso
		lblBarraProgreso = new JLabel("Calculando...");
		lblBarraProgreso.setBounds(235, 200, 200, 20);
		

		progressBar = new JProgressBar();
		progressBar.setBounds(29, 200, 592, 20);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(29, 200, 592, 20);
		

		
		setSize(663, 514);
		setTitle("Empresa: MVC");
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
	}

	public void accion() {
		if (paradas != null)
			return;
		paradas = coordinador.listarParadas2();
		for (int i = 0; i < paradas.size(); i++) {
			cbxParada1.addItem(paradas.get(i));
			cbxParada2.addItem(paradas.get(i));
		}
	}

	public void setText(String resultado) {
		txtResultado.setText(resultado);
	}
	
	public String getHours() {
		return txtHoraLlegada.getText();
	}

	
	/* DEPRECATED  */
	public String verDatosDEPRECATED(List<Tramo> recorrido) {
		
		//System.out.println(recorrido);
		
		String resultado = "";
		int tiempoTotal = 0;
		int tiempoColectivo = 0;
		int tiempoCaminando = 0;
		for (Tramo t : recorrido) {
			if (t != null) {
			resultado += t.getInicio().getId() + " [ " + this.mostrarLineas(t.getInicio()) + " ] "
					+ t.getInicio().getDireccion() + " ---> " + t.getFin().getId() + " [ "
					+ this.mostrarLineas(t.getFin()) + " ] " + t.getFin().getDireccion() + " : " + t.getTiempo()
					+ " minuto ";

			if (t.getTipo() == 2) {
				resultado += "caminando";

			} else {
				resultado += "por linea " + this.coincidentes(t.getInicio(), t.getFin());
			}

			resultado += "\n";

			tiempoTotal += t.getTiempo();
			if (t.getTipo() == 2)
				tiempoCaminando += t.getTiempo();
			else
				tiempoColectivo += t.getTiempo();
		}
		}
		resultado += "Tiempo Total: " + tiempoTotal + "\n";
		resultado += "Tiempo Colectivo: " + tiempoColectivo + "\n";
		resultado += "Tiempo Caminando: " + tiempoCaminando + "\n";
		// Obtener la hora de llegada desde el campo de texto
		String horaLlegada = txtHoraLlegada.getText();
		resultado += "Hora de llegada a destino: " + horaLlegada.substring(0, 3)
				+ (Integer.parseInt(horaLlegada.substring(horaLlegada.length() - 2)) + tiempoTotal) + "\n";
		return resultado;
	}
	

	/* NEW */
	public void verDatos(List<List<Tramo>> recorridos, int horario, TreeMap<String, Linea> lineas) {
		Linea linea;
		Tramo tramo;
		String nombreLinea;
		
		String texto = ""; 
		for (List<Tramo> tramos : recorridos) {

			texto += Time.toTime(horario) + " - Llega a la parada \n";
			for (int i = 0; i < tramos.size() - 1; i++) {
				tramo = tramos.get(i);
				linea = tramo.getInicio().getLineas().get(0);
				nombreLinea = linea.getNombre();
				if (lineas.get(linea.getNombre())==null)
					nombreLinea = "CAMINANDO";
				
				texto += Time.toTime(tramo.getTiempo()) + " - " + nombreLinea + " ("
						+ tramo.getInicio().getDireccion() + " " + " > " + tramo.getFin().getDireccion() + ") \n";
			}
			tramo = tramos.get(tramos.size() - 1);

			texto += Time.toTime(tramo.getTiempo()) + " - Fin de recorrido \n";

			texto += "Tiempo Total: " + Time.toTime(tramo.getTiempo() - horario);
			
			texto += "\n";
			texto += "\n";
			

		}
		
		texto += "Cantidad de recorridos posibles ";
		texto += recorridos.size();
		
		texto += "\n";

		
		
		txtResultado.setText(texto);
	}

	private String mostrarLineas(Parada parada) {
		List<Linea> lineas = parada.getLineas();
		List<String> nombresLineasUnicos = new ArrayList<>();

		for (Linea linea : lineas) {
			String nombreLinea = linea.getNombre();
			if (!nombresLineasUnicos.contains(nombreLinea)) {
				nombresLineasUnicos.add(nombreLinea);
			}
		}

		return String.join(", ", nombresLineasUnicos);
	}

	private String coincidentes(Parada origen, Parada destino) {
		List<Linea> lineasOrigen = origen.getLineas();
		List<Linea> lineasDestino = destino.getLineas();
		List<String> lineasComunes = new ArrayList<>();

		// Encuentra las líneas comunes entre las paradas de origen y destino
		for (Linea lineaOrigen : lineasOrigen) {
			for (Linea lineaDestino : lineasDestino) {
				if (lineaOrigen.equals(lineaDestino)) {
					String nombreLinea = lineaOrigen.getNombre();
					if (!lineasComunes.contains(nombreLinea)) {
						lineasComunes.add(nombreLinea);
					}
					break; // Romper el bucle interno una vez que se encuentra una coincidencia
				}
			}
		}

		// Construir la cadena resultante
		String resultado = String.join(" o ", lineasComunes);
		return resultado.isEmpty() ? "Ninguna" : resultado;
	}

	private boolean mismaLinea(Parada origen, Parada destino) {
		List<Linea> lineasOrigen = origen.getLineas();
		List<Linea> lineasDestino = destino.getLineas();

		for (Linea linea : lineasOrigen) {
			if (lineasDestino.contains(linea)) {
				return true;
			}
		}
		return false;
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == btnRapido) {
				iniciar();
			}
		}

		// la simulaacion del progreso (la barrita que se llena)
		private void simulateProgress() {
			Thread thread2 = new Thread(() -> {
				coordinador.masRapido((Parada) cbxParada1.getSelectedItem(), (Parada) cbxParada2.getSelectedItem());
			});

			thread2.start();

			Thread thread = new Thread(() -> {

				for (int i = 0; i <= 100; i++) {
					final int progressValue = i;
					SwingUtilities.invokeLater(() -> {
						// Actualizar el valor de la barra de progreso en el hilo de la interfaz de
						// usuario
						progressBar.setValue(progressValue);
					});
					try {

						// Simular trabajo en segundo plano con Thread.sleep y un valor aleatorio
						Thread.sleep((long) (Math.pow(Math.random(), 20) * 1000)); // HACE NUMEROS MAS EXTREMISTAS, MAS
																					// REALISTAS

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Cerrar la ventana al finalizar la simulación
				SwingUtilities.invokeLater(() -> {

					terminar();

				});
			});

			thread.start();
		}

		public void iniciar() {
			// Inicialización de lblBarraProgreso
			lblBarraProgreso = new JLabel("Calculando...");
			lblBarraProgreso.setBounds(277, 225, 200, 20);

			progressBar = new JProgressBar();
			progressBar.setBounds(29, 200, 592, 20);

			contentPane.add(lblBarraProgreso);

			contentPane.add(progressBar);
			setSize(663, 514);
			setTitle("Empresa: MVC");
			setResizable(false);
			getContentPane().setLayout(null);
			simulateProgress();
		}

		public void terminar() {
			// Reinicia la interfaz después de finalizar el cálculo
			progressBar.setValue(0);
			lblBarraProgreso.setVisible(false);
			progressBar.setVisible(false);
			btnRapido.setEnabled(true);
			coordinador.setVisibleInterfaz();
		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
}
