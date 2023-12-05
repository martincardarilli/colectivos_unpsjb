package colectivo.gui.datos.datos;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import colectivo.controlador.Constantes;
import colectivo.controlador.Coordinador;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;

public class LineaParadasForm extends JDialog {
    private JComboBox<String>[] paradasComboBoxes = new JComboBox[20];
    private Linea linea;
    
    private List<Parada> todasLasParadas; // Lista de todas las paradas disponibles
    
	private JPanel contentPane;

	private Coordinador coordinador;
	
    public LineaParadasForm() {
    	setBounds(100, 100, 310, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		 setModal(false); // Hace que el formulario no sea modal

        for (int i = 0; i < paradasComboBoxes.length; i++) {
            paradasComboBoxes[i] = new JComboBox<>();
            paradasComboBoxes[i].setBounds(10, 30 + i * 30, 250, 20); // Ajusta según sea necesario
            contentPane.add(paradasComboBoxes[i]);
        }

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setBounds(10, 30 + paradasComboBoxes.length * 30, 80, 30); // Ajusta según sea necesario
        guardarButton.addActionListener(e -> guardarCambios());
        contentPane.add(guardarButton);
    }

    public void cargarTodasLasParadas(List<Parada> paradas) {
        this.todasLasParadas = paradas;

        for (JComboBox<String> comboBox : paradasComboBoxes) {
            comboBox.removeAllItems();
            for (Parada parada : todasLasParadas) {
                comboBox.addItem(parada.toString());
            }
        }
    }

    public void cargarLinea(Linea linea) {
        this.linea = linea;

        List<Parada> paradasDeLaLinea = linea.getParadas();
        for (int i = 0; i < paradasComboBoxes.length; i++) {
            JComboBox<String> comboBox = paradasComboBoxes[i];
            comboBox.removeAllItems(); // Limpiar el JComboBox antes de usarlo

            // Añadir todas las paradas disponibles en el JComboBox
            for (Parada parada : todasLasParadas) {
                comboBox.addItem(parada.toString());
            }

            // Seleccionar la parada específica si existe, de lo contrario dejarlo vacío
            if (i < paradasDeLaLinea.size()) {
                Parada parada = paradasDeLaLinea.get(i);
                comboBox.setSelectedItem(parada.toString());
            } else {
                comboBox.setSelectedIndex(-1); // Dejar el JComboBox vacío
            }
        }
    }

    private void guardarCambios() {
        List<Parada> nuevasParadas = new ArrayList<>();
        for (JComboBox<String> comboBox : paradasComboBoxes) {
            String paradaSeleccionada = (String) comboBox.getSelectedItem();
            // Encuentra la parada correspondiente y añádela a la lista
            Parada parada = encontrarParadaPorNombre(paradaSeleccionada);
            if (parada != null) {
                nuevasParadas.add(parada);
            }
        }
        linea.setParadas(nuevasParadas);
        System.out.println(coordinador);
        coordinador.actualizarLineaConParadas(linea); // Método a implementar en Coordinador
    }

    private Parada encontrarParadaPorNombre(String nombreParada) {
        for (Parada parada : todasLasParadas) {
            if (parada.toString().equals(nombreParada)) {
                return parada;
            }
        }
        return null;
    }
    
    public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}
    

}
