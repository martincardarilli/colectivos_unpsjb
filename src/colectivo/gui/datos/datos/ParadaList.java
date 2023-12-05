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
public class ParadaList extends JDialog {

	 private static final long serialVersionUID = 1L;
	    private Coordinador coordinador;
	    private int accion;
	    private Parada parada;

	    private JPanel contentPane;
	    private JScrollPane scrollPane;
	    private JTable tableParada;
	    private JButton btnInsertar;

	/**
	 * Create the frame.
	 */
	  public ParadaList() {
	        setBounds(100, 100, 756, 366);
	        contentPane = new JPanel();
	        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	        setContentPane(contentPane);
	        contentPane.setLayout(null);

	        btnInsertar = new JButton("Insertar");
	        btnInsertar.setBounds(38, 280, 114, 32);
	        contentPane.add(btnInsertar);

	        scrollPane = new JScrollPane();
	        scrollPane.setBounds(38, 25, 673, 244);
	        contentPane.add(scrollPane);

	        tableParada = new JTable();
	        tableParada.setModel(
	                new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Dirección", "Latitud", "Longitud", "Modificar", "Borrar" }) {
	                    private static final long serialVersionUID = 1L;
	                    boolean[] columnEditables = new boolean[] { false, false, false, false, true, true };

	                    public boolean isCellEditable(int row, int column) {
	                        return columnEditables[column];
	                    }
	                });

	        tableParada.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
	        tableParada.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));
	        tableParada.getColumn("Borrar").setCellRenderer(new ButtonRenderer());
	        tableParada.getColumn("Borrar").setCellEditor(new ButtonEditor(new JCheckBox()));
	        scrollPane.setViewportView(tableParada);

	        Handler handler = new Handler();
	        btnInsertar.addActionListener(handler);

	        setModal(true);
	    }

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnInsertar)
				coordinador.insertarParadaForm();
		}
	}

	   public void loadTable() {
	        ((DefaultTableModel) tableParada.getModel()).setRowCount(0);
	        for (Parada parada : coordinador.listarParadas2()) {
	            addRow(parada);
	        }
	    }

	    public void addRow(Parada parada) {
	        Object[] row = new Object[tableParada.getModel().getColumnCount()];
	        row[0] = parada.getId();
	        row[1] = parada.getDireccion();
	        row[2] = parada.getLat(); // Asumiendo que Parada tiene un método getLat
	        row[3] = parada.getLng(); // Asumiendo que Parada tiene un método getLng
	        row[4] = "edit";
	        row[5] = "drop";
	        
	        ((DefaultTableModel) tableParada.getModel()).addRow(row);
	    }

	private void updateRow(int row) {
		/*tableLinea.setValueAt(linea.getId(), row, 0);*/
		tableParada.setValueAt(parada.getId(), row, 0);
		tableParada.setValueAt(parada.getDireccion(), row, 1);
		tableParada.setValueAt(parada.getLat(), row, 2);
		tableParada.setValueAt(parada.getLng(), row, 3);
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
				String id = tableParada.getValueAt(tableParada.getSelectedRow(), 0).toString();
				/*Linea linea = (Linea) coordinador.buscarLinea(new Linea(id, null));*/
				
				/*
				 * String id = jtfID.getText();
			String direccion = jtfDireccion.getText();
			String lat = jtfLat.getText();
			String lng = jtfLng.getText();
			
			Parada parada = new Parada(id, direccion, Double.parseDouble(lat), Double.parseDouble(lng));
			
			
				Parada parada = (Parada) coordinador.buscarParada(new Parada(id, null, 0, 0));
				
				Ver ParadaForm
				
				 */
				Parada parada = (Parada) coordinador.buscarParada(new Parada(id, null, 0, 0));
				if (label.equals("edit"))
					coordinador.modificarParadaForm(parada);
				else
					coordinador.borrarParadaForm(parada);
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

	public void setParada(Parada parada) {
		this.parada = parada;
	}

}
