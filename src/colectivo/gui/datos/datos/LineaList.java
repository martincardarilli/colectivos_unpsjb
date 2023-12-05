package colectivo.gui.datos.datos;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import colectivo.controlador.Constantes;
import colectivo.controlador.Coordinador;
import colectivo.modelo.Linea;
import colectivo.modelo.Parada;


/**
 * Ventana de lista de líneas de colectivo con opciones de inserción,
 *  modificación y eliminación.
 *  @author maiac
 *  @author martincardarilli
 *  @author mpacheco
 */
public class LineaList extends JDialog {

	private static final long serialVersionUID = 1L;
	private Coordinador coordinador;
	private int accion;
	private Linea linea;

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable tableLinea;
	private JButton btnInsertar;

	/**
	 * Create the frame.
	 */
	public LineaList() {
		setBounds(100, 100, 956, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnInsertar = new JButton("Insertar");
		btnInsertar.setBounds(38, 280, 114, 32);
		contentPane.add(btnInsertar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 25, 873, 244);
		contentPane.add(scrollPane);

		tableLinea = new JTable();
		 tableLinea.setModel(new DefaultTableModel(
		            new Object[][] {},
		            // Agregar columnas adicionales para las paradas
		            new String[] {
		            	    "Nombre", "Comienza", "Finaliza", "Frecuencia", "Modificar", "Borrar", 
		            	    "P1", "P2", "P3", "P4", "P5",
		            	    "P6", "P7", "P8", "P9", "P10",
		            	    "P11", "P12", "P13", "P14", "P15",
		            	    "P16", "P17", "P18", "P19", "P20"
		            	}) {
		            boolean[] columnEditables = new boolean[] { false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

		            public boolean isCellEditable(int row, int column) {
		                return columnEditables[column];
		            }
		        });

		tableLinea.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
		tableLinea.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));
		tableLinea.getColumn("Borrar").setCellRenderer(new ButtonRenderer());
		tableLinea.getColumn("Borrar").setCellEditor(new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(tableLinea);
		
		 configurarAnchoColumnas(); // Llama al método para configurar el ancho de las columnas

		Handler handler = new Handler();
		btnInsertar.addActionListener(handler);

		setModal(true);
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnInsertar)
				coordinador.insertarLineaForm();
		}
	}
	
	// Después de inicializar la tabla y antes de mostrarla en pantalla
	public void configurarAnchoColumnas() {
	    TableColumnModel columnModel = tableLinea.getColumnModel();

	    columnModel.getColumn(0).setPreferredWidth(50); // Nombre
	    columnModel.getColumn(1).setPreferredWidth(120); // Comienza
	    columnModel.getColumn(2).setPreferredWidth(120); // Finaliza
	    columnModel.getColumn(3).setPreferredWidth(50); // Frecuencia

	    // Configura el ancho de las columnas de paradas y botones, si es necesario
	    for (int i = 4; i < columnModel.getColumnCount(); i++) {
	        if (i <= 5) { // Para las columnas "Modificar" y "Borrar"
	            columnModel.getColumn(i).setPreferredWidth(80);
	        } else { // Para las columnas de las paradas (P1, P2, ...)
	            columnModel.getColumn(i).setPreferredWidth(50);
	        }
	    }
	}

	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableLinea.getModel()).setRowCount(0);
		for (Linea linea : coordinador.listarLineas2())
			if (linea instanceof Linea)
				addRow((Linea) linea);
	}
	
	private String convertirMinutosAHorario(int minutos) {
	    int horas = minutos / 60;
	    int min = minutos % 60;
	    return String.format("%02d:%02d", horas, min);
	}
	

	public void addRow(Linea linea) {
	    Object[] row = new Object[26]; // Asegúrate de que el tamaño del array coincida con el número de columnas
	    row[0] = linea.getNombre();
	    row[1] = convertirMinutosAHorario(linea.getComienza());
	    row[2] = convertirMinutosAHorario(linea.getFinaliza());
	    row[3] = linea.getFrecuencia();
	    row[4] = "edit";
	    row[5] = "drop";

	    int colIndex = 6; // Comenzar en la columna 4 para los ID de paradas
	    for (Parada parada : linea.getParadas()) {
	        if (colIndex < 26) {
	            row[colIndex++] = parada.getId();
	        } else {
	            break; // Si hay más de 20 paradas, solo se muestran las primeras 20
	        }
	    }

	    ((DefaultTableModel) tableLinea.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		/*tableLinea.setValueAt(linea.getId(), row, 0);*/
		tableLinea.setValueAt(linea.getNombre(), row, 0);
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			// setText((value == null) ? "" : value.toString());
			Icon icon = null;
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_edit.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_drop.png"));
			setIcon(icon);
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable table;
		private boolean isDeleteRow = false;
		private boolean isUpdateRow = false;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}

			label = (value == null) ? "" : value.toString();
			// button.setText(label);
			Icon icon = null;
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_edit.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_drop.png"));
			button.setIcon(icon);
			isPushed = true;
			this.table = table;
			isDeleteRow = false;
			isUpdateRow = false;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				String id = tableLinea.getValueAt(tableLinea.getSelectedRow(), 0).toString();
				/*Linea linea = (Linea) coordinador.buscarLinea(new Linea(id, null));*/
				Linea linea = (Linea) coordinador.buscarLinea(new Linea(id));
				if (label.equals("edit"))
					coordinador.modificarLineaForm(linea);
				else
					coordinador.borrarLineaForm(linea);
			}
			if (accion == Constantes.BORRAR)
				isDeleteRow = true;
			if (accion == Constantes.MODIFICAR)
				isUpdateRow = true;
			isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();

			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			if (isDeleteRow)
				tableModel.removeRow(table.getSelectedRow());

			if (isUpdateRow) {
				updateRow(table.getSelectedRow());
			}

		}
	}

	public void setCoordinador(Coordinador coordinador) {
		this.coordinador = coordinador;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

}
