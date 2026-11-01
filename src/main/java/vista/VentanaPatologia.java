package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controlador.PatologiaDAO;
import modelo.Patologia;

public class VentanaPatologia extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Campos de entrada
	private JTextField txtDniPaciente;
	private JTextField txtDescripcionPatologia;

	// Tabla y Modelo
	private JTable tablaPatologias;
	private DefaultTableModel modeloTablaPatologias;

	// Botones
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnBuscar;
	private JButton btnCancelar;

	// DAO
	private PatologiaDAO patologiaDao;

	// Paleta de Colores (Misma estética que el resto del sistema)
	private static final Color COLOR_FONDO = new Color(24, 26, 31);
	private static final Color COLOR_TARJETA = new Color(33, 37, 43);
	private static final Color COLOR_TEXTO = new Color(240, 240, 240);
	private static final Color COLOR_TEXTO_MUTED = new Color(160, 165, 175);
	private static final Color COLOR_INPUT = new Color(40, 44, 52);
	private static final Color COLOR_BORDE_INPUT = new Color(60, 66, 77);
	private static final Color COLOR_PRIMARIO = new Color(14, 116, 144);
	private static final Color COLOR_BOTON_HOVER = new Color(2, 132, 199);
	private static final Color COLOR_BOTON_TEXTO = Color.WHITE;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		EventQueue.invokeLater(() -> {
			try {
				VentanaPatologia frame = new VentanaPatologia();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaPatologia() {
		patologiaDao = new PatologiaDAO();

		// Configuración básica de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gestión de Patologías");
		setSize(850, 620);
		setMinimumSize(new Dimension(750, 500));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);

		// 1. Encabezado
		JPanel panelHeader = new JPanel(new BorderLayout(0, 4));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Gestión de Patologías", SwingConstants.LEFT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Registro y administración de diagnósticos de antecedente y patologías del paciente", SwingConstants.LEFT);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubtitulo.setForeground(COLOR_TEXTO_MUTED);

		panelHeader.add(lblTitulo, BorderLayout.NORTH);
		panelHeader.add(lblSubtitulo, BorderLayout.CENTER);
		contentPane.add(panelHeader, BorderLayout.NORTH);

		// 2. Panel Central (Formulario + Tabla)
		JPanel panelCentral = new JPanel(new BorderLayout(0, 15));
		panelCentral.setOpaque(false);

		// --- Formulario de Entrada ---
		JPanel panelFormulario = new JPanel(new GridBagLayout());
		panelFormulario.setBackground(COLOR_TARJETA);
		panelFormulario.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(50, 56, 66), 1),
				new EmptyBorder(15, 20, 15, 20)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Fila 0: DNI Paciente
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("DNI Paciente:"), gbc);

		txtDniPaciente = crearTextField();
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
		panelFormulario.add(txtDniPaciente, gbc);

		// Fila 1: Descripción Patología
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Patología:"), gbc);

		txtDescripcionPatologia = crearTextField();
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
		panelFormulario.add(txtDescripcionPatologia, gbc);

		panelCentral.add(panelFormulario, BorderLayout.NORTH);

		// --- Tabla de Patologías ---
		String[] columnas = {"ID Patología", "DNI Paciente", "Descripción"};
		modeloTablaPatologias = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaPatologias = new JTable(modeloTablaPatologias);
		estilarTabla(tablaPatologias);

		JScrollPane scrollTabla = new JScrollPane(tablaPatologias);
		scrollTabla.getViewport().setBackground(COLOR_TARJETA);
		scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(50, 56, 66), 1));
		panelCentral.add(scrollTabla, BorderLayout.CENTER);

		contentPane.add(panelCentral, BorderLayout.CENTER);

		// 3. Panel Inferior de Botones
		JPanel panelBotones = new JPanel(new GridBagLayout());
		panelBotones.setOpaque(false);

		GridBagConstraints gbcBtn = new GridBagConstraints();
		gbcBtn.insets = new Insets(0, 5, 0, 5);
		gbcBtn.weightx = 1.0;
		gbcBtn.fill = GridBagConstraints.HORIZONTAL;

		btnAgregar = crearBotonEstilizado("Agregar");
		btnModificar = crearBotonEstilizado("Modificar");
		btnEliminar = crearBotonEstilizado("Eliminar");
		btnBuscar = crearBotonEstilizado("Buscar");
		btnCancelar = crearBotonEstilizado("Limpiar / Cancelar");

		gbcBtn.gridx = 0; panelBotones.add(btnAgregar, gbcBtn);
		gbcBtn.gridx = 1; panelBotones.add(btnModificar, gbcBtn);
		gbcBtn.gridx = 2; panelBotones.add(btnEliminar, gbcBtn);
		gbcBtn.gridx = 3; panelBotones.add(btnBuscar, gbcBtn);
		gbcBtn.gridx = 4; panelBotones.add(btnCancelar, gbcBtn);

		contentPane.add(panelBotones, BorderLayout.SOUTH);

		// 4. Configurar Listeners
		configurarListeners();
	}

	private void configurarListeners() {
		// Evento Agregar
		btnAgregar.addActionListener(e -> {
			try {
				int dniPaciente = Integer.parseInt(txtDniPaciente.getText().trim());
				String descripcionPatologia = txtDescripcionPatologia.getText().trim();

				if (descripcionPatologia.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, ingrese la descripción de la patología.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Patologia nuevaPatologia = new Patologia(dniPaciente, descripcionPatologia);

				if (patologiaDao.agregarPatologia(nuevaPatologia)) {
					JOptionPane.showMessageDialog(this, "Patología guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarPatologiasEnTabla(dniPaciente);
				} else {
					JOptionPane.showMessageDialog(this, "Error al guardar la patología.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI del paciente debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Modificar
		btnModificar.addActionListener(e -> {
			int filaSeleccionada = tablaPatologias.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Seleccione una patología de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			try {
				int idPatologia = (int) modeloTablaPatologias.getValueAt(filaSeleccionada, 0);
				int dniPaciente = Integer.parseInt(txtDniPaciente.getText().trim());
				String descripcionPatologia = txtDescripcionPatologia.getText().trim();

				if (descripcionPatologia.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, ingrese la descripción de la patología.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Patologia patologiaModificada = new Patologia(idPatologia, dniPaciente, descripcionPatologia);

				if (patologiaDao.modificarPatologia(patologiaModificada)) {
					JOptionPane.showMessageDialog(this, "Patología modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarPatologiasEnTabla(dniPaciente);
				} else {
					JOptionPane.showMessageDialog(this, "Error al modificar la patología.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "DNI del paciente debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Eliminar
		btnEliminar.addActionListener(e -> {
			int filaSeleccionada = tablaPatologias.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Seleccione una patología de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idPatologia = (int) modeloTablaPatologias.getValueAt(filaSeleccionada, 0);
			int dniPacienteActual = Integer.parseInt(txtDniPaciente.getText().trim());

			int confirmacion = JOptionPane.showConfirmDialog(this,
					"¿Está seguro de que desea eliminar la patología ID: " + idPatologia + "?",
					"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

			if (confirmacion == JOptionPane.YES_OPTION) {
				if (patologiaDao.eliminarPatologia(idPatologia)) {
					JOptionPane.showMessageDialog(this, "Patología eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarPatologiasEnTabla(dniPacienteActual);
				} else {
					JOptionPane.showMessageDialog(this, "Error al eliminar la patología.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Evento Buscar
		btnBuscar.addActionListener(e -> {
			try {
				String dniText = txtDniPaciente.getText().trim();
				if (dniText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Ingrese un DNI de paciente para buscar sus patologías.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int dniPaciente = Integer.parseInt(dniText);
				cargarPatologiasEnTabla(dniPaciente);

				if (modeloTablaPatologias.getRowCount() == 0) {
					JOptionPane.showMessageDialog(this, "No se encontraron patologías para el DNI " + dniPaciente + ".", "Información", JOptionPane.INFORMATION_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI del paciente debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Cancelar / Limpiar
		btnCancelar.addActionListener(e -> {
			limpiarCampos();
			modeloTablaPatologias.setRowCount(0);
		});

		// Selección en Tabla -> Llenar campos automáticamente
		tablaPatologias.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaPatologias.getSelectedRow() != -1) {
				int fila = tablaPatologias.getSelectedRow();
				txtDniPaciente.setText(String.valueOf(modeloTablaPatologias.getValueAt(fila, 1)));
				txtDescripcionPatologia.setText(String.valueOf(modeloTablaPatologias.getValueAt(fila, 2)));
			}
		});
	}

	private void cargarPatologiasEnTabla(int dniPaciente) {
		modeloTablaPatologias.setRowCount(0);
		List<Patologia> patologias = patologiaDao.buscarPorDniPaciente(dniPaciente);

		if (patologias != null && !patologias.isEmpty()) {
			for (Patologia p : patologias) {
				Vector<Object> fila = new Vector<>();
				fila.add(p.getIdPatologia());
				fila.add(p.getDniPaciente());
				fila.add(p.getDescripcion());
				modeloTablaPatologias.addRow(fila);
			}
		}
	}

	private void limpiarCampos() {
		txtDniPaciente.setText("");
		txtDescripcionPatologia.setText("");
		tablaPatologias.clearSelection();
	}

	// --- Métodos Auxiliares UI ---
	private JLabel crearLabel(String texto) {
		JLabel label = new JLabel(texto);
		label.setFont(new Font("Segoe UI", Font.BOLD, 13));
		label.setForeground(COLOR_TEXTO);
		return label;
	}

	private JTextField crearTextField() {
		JTextField field = new JTextField();
		field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		field.setForeground(COLOR_TEXTO);
		field.setBackground(COLOR_INPUT);
		field.setCaretColor(COLOR_TEXTO);
		field.setPreferredSize(new Dimension(field.getPreferredSize().width, 34));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1),
				BorderFactory.createEmptyBorder(5, 8, 5, 8)
		));
		return field;
	}

	private JButton crearBotonEstilizado(String texto) {
		JButton btn = new JButton(texto);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btn.setForeground(COLOR_BOTON_TEXTO);
		btn.setBackground(COLOR_PRIMARIO);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(110, 36));

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btn.setBackground(COLOR_BOTON_HOVER);
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				btn.setBackground(COLOR_PRIMARIO);
			}
		});

		return btn;
	}

	private void estilarTabla(JTable tabla) {
		tabla.setBackground(COLOR_TARJETA);
		tabla.setForeground(COLOR_TEXTO);
		tabla.setGridColor(new Color(50, 56, 66));
		tabla.setRowHeight(28);
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.setSelectionBackground(COLOR_PRIMARIO);
		tabla.setSelectionForeground(Color.WHITE);

		tabla.getTableHeader().setBackground(new Color(20, 23, 28));
		tabla.getTableHeader().setForeground(COLOR_TEXTO);
		tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));
		((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
	}
}