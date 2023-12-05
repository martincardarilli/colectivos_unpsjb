package colectivo.gui.datos.datos;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import colectivo.controlador.Constantes;
import colectivo.controlador.Coordinador;
import colectivo.modelo.Linea;
import colectivo.negocio.LineaExisteException;
import colectivo.negocio.LineaReferenciaException;
/**
 * Ventana de formulario
 *  para la gestión de datos de 
 *  líneas de colectivo.
 *  
 *  @author maiac
 *  @author martincardarilli
 *  @author mpacheco
 */
public class LineaForm extends JDialog {
	
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(LineaForm.class);

	private Coordinador coordinador;

	private JPanel contentPane;
	private JTextField jtfNombre;

	private JLabel lblErrorNombre;

	private JButton btnInsertar;
	private JButton btnModificar;
	private JButton btnBorrar;
	private JButton btnCancelar;
	
	  private JTextField jtfComienza;
	    private JTextField jtfFinaliza;
	    private JTextField jtfFrecuencia;
	
	public String nombreOriginal; // FIX porque nose...

	/**
	 * Create the frame.
	 */
	public LineaForm() {
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		 setModal(false); // Hace que el formulario no sea modal

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombre.setBounds(42, 24, 107, 14);
		contentPane.add(lblNombre);

		jtfNombre = new JTextField();
		jtfNombre.setBounds(159, 24, 86, 20);
		contentPane.add(jtfNombre);
		jtfNombre.setColumns(10);

		lblErrorNombre = new JLabel("");
		lblErrorNombre.setForeground(Color.RED);
		lblErrorNombre.setBounds(255, 24, 300, 14);
		contentPane.add(lblErrorNombre);
		
		
		
		
		  JLabel lblComienza = new JLabel("Comienza:");
	        lblComienza.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblComienza.setBounds(42, 54, 107, 14);
	        contentPane.add(lblComienza);

	        jtfComienza = new JTextField();
	        jtfComienza.setBounds(159, 54, 86, 20);
	        contentPane.add(jtfComienza);
	        jtfComienza.setColumns(10);

	        JLabel lblFinaliza = new JLabel("Finaliza:");
	        lblFinaliza.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblFinaliza.setBounds(42, 84, 107, 14);
	        contentPane.add(lblFinaliza);

	        jtfFinaliza = new JTextField();
	        jtfFinaliza.setBounds(159, 84, 86, 20);
	        contentPane.add(jtfFinaliza);
	        jtfFinaliza.setColumns(10);

	        JLabel lblFrecuencia = new JLabel("Frecuencia:");
	        lblFrecuencia.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblFrecuencia.setBounds(42, 114, 107, 14);
	        contentPane.add(lblFrecuencia);

	        jtfFrecuencia = new JTextField();
	        jtfFrecuencia.setBounds(159, 114, 86, 20);
	        contentPane.add(jtfFrecuencia);
	        jtfFrecuencia.setColumns(10);
		
		
		

		Handler handler = new Handler();

		btnInsertar = new JButton("Insertar");
		btnInsertar.setBounds(85, 202, 114, 32);
		contentPane.add(btnInsertar);
		btnInsertar.addActionListener(handler);

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(85, 202, 114, 32);
		contentPane.add(btnModificar);
		btnModificar.addActionListener(handler);

		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(85, 202, 114, 32);
		contentPane.add(btnBorrar);
		btnBorrar.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(225, 202, 114, 32);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		setModal(true);
	}

	public void accion(int accion, Linea linea) {
		btnInsertar.setVisible(false);
		btnModificar.setVisible(false);
		btnBorrar.setVisible(false);
		jtfNombre.setEditable(true);

		if (accion == Constantes.INSERTAR) {
			btnInsertar.setVisible(true);
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			System.out.println("accion");
			System.out.println(linea.getNombre());
			nombreOriginal = linea.getNombre(); // Guardar el nombre original
			btnModificar.setVisible(true);
			mostrar(linea);
		}

		if (accion == Constantes.BORRAR) {
			btnBorrar.setVisible(true);
			jtfNombre.setEditable(false);
			   jtfComienza.setEditable(false);
			      jtfFinaliza.setEditable(false);
			      jtfFrecuencia.setEditable(false);
			mostrar(linea);
		}
	}

	private void mostrar(Linea linea) {
	    jtfNombre.setText(linea.getNombre());
	    jtfComienza.setText(convertirMinutosAHorario(linea.getComienza()));
	    jtfFinaliza.setText(convertirMinutosAHorario(linea.getFinaliza()));
	    jtfFrecuencia.setText(String.valueOf(linea.getFrecuencia()));
	}

	private String convertirMinutosAHorario(int minutos) {
	    int horas = minutos / 60;
	    int min = minutos % 60;
	    return String.format("%02d:%02d", horas, min);
	}
	
	private int convertirHorarioAMinutos(String horario) {
	    String[] partes = horario.split(":");
	    int horas = Integer.parseInt(partes[0]);
	    int minutos = Integer.parseInt(partes[1]);
	    return horas * 60 + minutos;
	}

	    private void limpiar() {
	        jtfNombre.setText("");
	        jtfComienza.setText("");
	        jtfFinaliza.setText("");
	        jtfFrecuencia.setText("");
	    }

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			
			if (!registroValido())
				return;
			
			String nombre = jtfNombre.getText().trim();
			int comienza = convertirHorarioAMinutos(jtfComienza.getText().trim());
            int finaliza = convertirHorarioAMinutos(jtfFinaliza.getText().trim());
            int frecuencia = Integer.parseInt(jtfFrecuencia.getText().trim());

			Linea linea = new Linea(nombre, comienza, finaliza, frecuencia);

			if (event.getSource() == btnInsertar) {
				
				try {
					coordinador.insertarLinea(linea);
					logger.info("Insertar linea");
				} catch (LineaExisteException e) {
					JOptionPane.showMessageDialog(null, "Esta l�nea ya existe!");
					logger.error("Error al insertar linea");
					return;
				}
			}

			if (event.getSource() == btnModificar) {
				
				System.out.println();
				System.out.println("btnModificar");
				System.out.println(linea.getNombre());
				System.out.println(nombreOriginal);
				coordinador.modificarLinea(linea, nombreOriginal);
				logger.info("Modificar linea");
			}

			if (event.getSource() == btnBorrar) {
				int resp = JOptionPane.showConfirmDialog(null, "Est� seguro que borra este registro?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp)
					try {
						coordinador.borrarLinea(linea);
						logger.info("Borrar linea");
					} catch (LineaReferenciaException e) {
						JOptionPane.showMessageDialog(null, "Hay estaciones que hacen referencia a esta l�nea!");
						logger.error("Error al borrar linea");
						return;
					}
				return;
			}

			if (event.getSource() == btnCancelar) {
				coordinador.cancelarLinea();
				return;
			}
		}
	}

	 public boolean registroValido() {
	        // ... (Validación para el nombre se mantiene igual)

	        // Validar campos comienza, finaliza y frecuencia
	        if (jtfComienza.getText().trim().isEmpty() ||
	            jtfFinaliza.getText().trim().isEmpty() ||
	            jtfFrecuencia.getText().trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        // Aquí puedes agregar más validaciones si es necesario

	        return true;
	    }

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
