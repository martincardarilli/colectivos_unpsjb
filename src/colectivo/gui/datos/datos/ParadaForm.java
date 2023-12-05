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
import colectivo.modelo.Parada;
import colectivo.negocio.LineaExisteException;
import colectivo.negocio.LineaReferenciaException;
import colectivo.negocio.ParadaExisteException;
/**
 * Ventana de formulario
 *  para la gestión de datos de 
 *  líneas de colectivo.
 *  
 *  @author maiac
 *  @author martincardarilli
 *  @author mpacheco
 */
public class ParadaForm extends JDialog {
	
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ParadaForm.class);

	private Coordinador coordinador;

	private JPanel contentPane;
	

    private JTextField jtfID;
    private JTextField jtfDireccion;
    private JTextField jtfLat;
    private JTextField jtfLng;
	//private JLabel lblErrorNombre;

	private JButton btnInsertar;
	private JButton btnModificar;
	private JButton btnBorrar;
	private JButton btnCancelar;
	
	public String nombreOriginal; // FIX porque nose...

	/**
	 * Create the frame.
	 */
	public ParadaForm() {
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);



		
		
		
		
		 JLabel lblID = new JLabel("ID:");
	        lblID.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblID.setBounds(42, 24, 107, 14);
	        contentPane.add(lblID);

	        jtfID = new JTextField();
	        jtfID.setBounds(159, 24, 86, 20);
	        contentPane.add(jtfID);
	        jtfID.setColumns(10);

	        JLabel lblDireccion = new JLabel("Dirección:");
	        lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblDireccion.setBounds(42, 54, 107, 14);
	        contentPane.add(lblDireccion);

	        jtfDireccion = new JTextField();
	        jtfDireccion.setBounds(159, 54, 86, 20);
	        contentPane.add(jtfDireccion);
	        jtfDireccion.setColumns(10);

	        JLabel lblLat = new JLabel("Latitud:");
	        lblLat.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblLat.setBounds(42, 84, 107, 14);
	        contentPane.add(lblLat);

	        jtfLat = new JTextField();
	        jtfLat.setBounds(159, 84, 86, 20);
	        contentPane.add(jtfLat);
	        jtfLat.setColumns(10);

	        JLabel lblLng = new JLabel("Longitud:");
	        lblLng.setFont(new Font("Tahoma", Font.BOLD, 11));
	        lblLng.setBounds(42, 114, 107, 14);
	        contentPane.add(lblLng);

	        jtfLng = new JTextField();
	        jtfLng.setBounds(159, 114, 86, 20);
	        contentPane.add(jtfLng);
	        jtfLng.setColumns(10);

	   /*     lblError = new JLabel("");
	        lblError.setForeground(Color.RED);
	        lblError.setBounds(255, 144, 300, 14);
	        contentPane.add(lblError); */
		

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

	public void accion(int accion, Parada parada) {
		btnInsertar.setVisible(false);
		btnModificar.setVisible(false);
		btnBorrar.setVisible(false);
			
		//jtfID.setEditable(false);
	    jtfDireccion.setEditable(true);
	    jtfLat.setEditable(true);
	    jtfLng.setEditable(true);

		if (accion == Constantes.INSERTAR) {
			jtfID.setEditable(true);
			btnInsertar.setVisible(true);
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			jtfID.setEditable(false);
			//System.out.println(linea.getNombre());
			//nombreOriginal = linea.getNombre(); // Guardar el nombre original
			btnModificar.setVisible(true);
			mostrar(parada);
		}

		if (accion == Constantes.BORRAR) {
			btnBorrar.setVisible(true);
			jtfID.setEditable(false);
			jtfDireccion.setEditable(false);
			jtfLat.setEditable(false);
			jtfLng.setEditable(false);
			mostrar(parada);
		}
	}

	private void mostrar(Parada parada) {
	
		jtfID.setText(parada.getId());
	    jtfDireccion.setText(parada.getDireccion());
	    jtfLat.setText(String.valueOf(parada.getLat()));
	    jtfLng.setText(String.valueOf(parada.getLng()));
	}

	private void limpiar() {
		//jtfNombre.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			// TRIM String nombre = jtfId.getText().trim();
			
			if (!registroValido())
				return;
			
			String id = jtfID.getText();
			String direccion = jtfDireccion.getText();
			String lat = jtfLat.getText();
			String lng = jtfLng.getText();
			
			Parada parada = new Parada(id, direccion, Double.parseDouble(lat), Double.parseDouble(lng));

			if (event.getSource() == btnInsertar) {
				
				try {
					coordinador.insertarParada(parada);
					logger.info("Insertar linea");
				} catch (ParadaExisteException e) {
					JOptionPane.showMessageDialog(null, "Esta l�nea ya existe!");
					logger.error("Error al insertar linea");
					return;
				}
			}

			if (event.getSource() == btnModificar) {
				
				System.out.println();
				System.out.println("btnModificar");
				//System.out.println(linea.getNombre());
				System.out.println(nombreOriginal);
				coordinador.modificarParada(parada);
				logger.info("Modificar linea");
			}

			if (event.getSource() == btnBorrar) {
				int resp = JOptionPane.showConfirmDialog(null, "Est� seguro que borra este registro?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp)
					try {
						coordinador.borrarParada(parada);
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
	    String id = jtfID.getText().trim();
	    String direccion = jtfDireccion.getText().trim();
	    String lat = jtfLat.getText().trim();
	    String lng = jtfLng.getText().trim();

	    // Verificar que los campos no estén vacíos
	    if (id.isEmpty() || direccion.isEmpty() || lat.isEmpty() || lng.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    // Verificar que lat y lng sean números válidos
	    if (!esDoubleValido(lat) || !esDoubleValido(lng)) {
	        JOptionPane.showMessageDialog(this, "La latitud y la longitud deben ser números válidos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    return true;
	}

	private boolean esDoubleValido(String valor) {
	    try {
	        Double.parseDouble(valor);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

}
