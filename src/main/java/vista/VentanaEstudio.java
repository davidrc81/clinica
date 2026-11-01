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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controlador.EstudioDAO;
import modelo.Estudio;

public class VentanaEstudio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Campos de entrada
	private JTextField txtDniPaciente;
	private JTextField txtIdMedico;
	private JTextField txtTipoEstudio;
	private JTextArea txtResultado;
	private JTextField txtFechaEstudio;

	// Tabla y Modelo
	private JTable tablaEstudios;
	private DefaultTableModel modeloTablaEstudios;

	// Botones
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnBuscar;
	private JButton btnCancelar;

	// DAO
	private EstudioDAO estudioDao;

	// Paleta de Colores (Misma estética que VentanaPrincipal)
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
				VentanaEstudio frame = new VentanaEstudio();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaEstudio() {
		estudioDao = new EstudioDAO();

		// Configuración básica de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gestión de Estudios Médicos");
		setSize(850, 650);
		setMinimumSize(new Dimension(750, 550));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);

		// 1. Encabezado
		JPanel panelHeader = new JPanel(new BorderLayout(0, 4));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Gestión de Estudios Médicos", SwingConstants.LEFT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Carga, consulta y actualización de resultados de estudios de pacientes", SwingConstants.LEFT);
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

		// Fila 0: DNI Paciente y Fecha Estudio
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("DNI Paciente:"), gbc);

		txtDniPaciente = crearTextField();
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
		panelFormulario.add(txtDniPaciente, gbc);

		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Fecha (AAAA-MM-DD):"), gbc);

		txtFechaEstudio = crearTextField();
		gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5;
		panelFormulario.add(txtFechaEstudio, gbc);

		// Fila 1: ID Médico y Tipo Estudio
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("ID Médico:"), gbc);

		txtIdMedico = crearTextField();
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtIdMedico, gbc);

		gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Tipo Estudio:"), gbc);

		txtTipoEstudio = crearTextField();
		gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtTipoEstudio, gbc);

		// Fila 2: Resultado (JTextArea)
		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panelFormulario.add(crearLabel("Resultado:"), gbc);

		txtResultado = new JTextArea(3, 20);
		txtResultado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtResultado.setForeground(COLOR_TEXTO);
		txtResultado.setBackground(COLOR_INPUT);
		txtResultado.setCaretColor(COLOR_TEXTO);
		txtResultado.setLineWrap(true);
		txtResultado.setWrapStyleWord(true);
		txtResultado.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)
		));

		JScrollPane scrollResultado = new JScrollPane(txtResultado);
		scrollResultado.setBorder(null);

		gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1.0; gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.BOTH;
		panelFormulario.add(scrollResultado, gbc);

		panelCentral.add(panelFormulario, BorderLayout.NORTH);

		// --- Tabla de Estudios ---
		String[] columnasEstudios = {"ID Estudio", "DNI Paciente", "ID Médico", "Tipo", "Resultado", "Fecha"};
		modeloTablaEstudios = new DefaultTableModel(columnasEstudios, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaEstudios = new JTable(modeloTablaEstudios);
		estilarTabla(tablaEstudios);

		JScrollPane scrollTabla = new JScrollPane(tablaEstudios);
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

		// 4. Asignación de Listeners de Eventos
		configurarListeners();
	}

	private void configurarListeners() {
		// Evento Agregar
		btnAgregar.addActionListener(e -> {
			try {
				int dniPaciente = Integer.parseInt(txtDniPaciente.getText().trim());
				int idMedico = Integer.parseInt(txtIdMedico.getText().trim());
				String tipoEstudio = txtTipoEstudio.getText().trim();
				String resultadoEstudio = txtResultado.getText().trim();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaEstudio;
				try {
					fechaEstudio = dateFormat.parse(txtFechaEstudio.getText().trim());
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (tipoEstudio.isEmpty() || resultadoEstudio.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Estudio nuevoEstudio = new Estudio(dniPaciente, idMedico, tipoEstudio, resultadoEstudio, fechaEstudio);

				if (estudioDao.agregarEstudio(nuevoEstudio)) {
					JOptionPane.showMessageDialog(this, "Estudio guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarEstudiosEnTabla(dniPaciente);
				} else {
					JOptionPane.showMessageDialog(this, "Error al guardar el estudio.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "DNI Paciente e ID Médico deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Modificar
		btnModificar.addActionListener(e -> {
			int filaSeleccionada = tablaEstudios.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Seleccione un estudio de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			try {
				int idEstudio = (int) modeloTablaEstudios.getValueAt(filaSeleccionada, 0);
				int dniPaciente = Integer.parseInt(txtDniPaciente.getText().trim());
				int idMedico = Integer.parseInt(txtIdMedico.getText().trim());
				String tipoEstudio = txtTipoEstudio.getText().trim();
				String resultadoEstudio = txtResultado.getText().trim();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaEstudio;
				try {
					fechaEstudio = dateFormat.parse(txtFechaEstudio.getText().trim());
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (tipoEstudio.isEmpty() || resultadoEstudio.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Estudio estudioModificado = new Estudio(idEstudio, dniPaciente, idMedico, tipoEstudio, resultadoEstudio, fechaEstudio);

				if (estudioDao.modificarEstudio(estudioModificado)) {
					JOptionPane.showMessageDialog(this, "Estudio modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarEstudiosEnTabla(dniPaciente);
				} else {
					JOptionPane.showMessageDialog(this, "Error al modificar el estudio.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "DNI Paciente e ID Médico deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Eliminar
		btnEliminar.addActionListener(e -> {
			int filaSeleccionada = tablaEstudios.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Seleccione un estudio de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idEstudio = (int) modeloTablaEstudios.getValueAt(filaSeleccionada, 0);

			int confirmacion = JOptionPane.showConfirmDialog(this,
					"¿Está seguro de que desea eliminar el estudio ID: " + idEstudio + "?",
					"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

			if (confirmacion == JOptionPane.YES_OPTION) {
				if (estudioDao.eliminarEstudio(idEstudio)) {
					JOptionPane.showMessageDialog(this, "Estudio eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					modeloTablaEstudios.setRowCount(0);
				} else {
					JOptionPane.showMessageDialog(this, "Error al eliminar el estudio.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Evento Buscar
		btnBuscar.addActionListener(e -> {
			try {
				String dniText = txtDniPaciente.getText().trim();
				if (dniText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Ingrese un DNI de paciente para buscar sus estudios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int dniPaciente = Integer.parseInt(dniText);
				cargarEstudiosEnTabla(dniPaciente);

				if (modeloTablaEstudios.getRowCount() == 0) {
					JOptionPane.showMessageDialog(this, "No se encontraron estudios para el DNI " + dniPaciente + ".", "Información", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Limpiar / Cancelar
		btnCancelar.addActionListener(e -> {
			limpiarCampos();
			modeloTablaEstudios.setRowCount(0);
		});

		// Selección en Tabla -> Llenar campos automáticamente
		tablaEstudios.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaEstudios.getSelectedRow() != -1) {
				int fila = tablaEstudios.getSelectedRow();
				txtDniPaciente.setText(String.valueOf(modeloTablaEstudios.getValueAt(fila, 1)));
				txtIdMedico.setText(String.valueOf(modeloTablaEstudios.getValueAt(fila, 2)));
				txtTipoEstudio.setText(String.valueOf(modeloTablaEstudios.getValueAt(fila, 3)));
				txtResultado.setText(String.valueOf(modeloTablaEstudios.getValueAt(fila, 4)));
				txtFechaEstudio.setText(String.valueOf(modeloTablaEstudios.getValueAt(fila, 5)));
			}
		});
	}

	private void cargarEstudiosEnTabla(int dniPaciente) {
		modeloTablaEstudios.setRowCount(0);
		List<Estudio> estudios = estudioDao.buscarEstudiosPorDniPaciente(dniPaciente);

		if (estudios != null && !estudios.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (Estudio e : estudios) {
				Vector<Object> fila = new Vector<>();
				fila.add(e.getIdEstudio());
				fila.add(e.getDniPaciente());
				fila.add(e.getIdMedico());
				fila.add(e.getTipo());
				fila.add(e.getResultado());
				fila.add(e.getFechaEstudio() != null ? dateFormat.format(e.getFechaEstudio()) : "");
				modeloTablaEstudios.addRow(fila);
			}
		}
	}

	private void limpiarCampos() {
		txtDniPaciente.setText("");
		txtIdMedico.setText("");
		txtTipoEstudio.setText("");
		txtResultado.setText("");
		txtFechaEstudio.setText("");
		tablaEstudios.clearSelection();
	}

	// --- Métodos Auxiliares de UI ---
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

		// Estilo del encabezado
		tabla.getTableHeader().setBackground(new Color(20, 23, 28));
		tabla.getTableHeader().setForeground(COLOR_TEXTO);
		tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));
		((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
	}
}