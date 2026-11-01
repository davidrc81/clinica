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

import controlador.HistoriaClinicaDAO;
import modelo.HistoriaClinica;

public class VentanaHistoriaClinica extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Campos de entrada
	private JTextField txtIdHistoriaClinica;
	private JTextField txtIdMedico;
	private JTextField txtIdPaciente;
	private JTextField txtFechaHc;
	private JTextArea txaDiagnostico;

	// Tabla y Modelo
	private JTable tablaHistoriasClinicas;
	private DefaultTableModel modeloTablaHistoriasClinicas;

	// Botones de la interfaz
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnBuscar;
	private JButton btnCancelar;

	// DAO
	private HistoriaClinicaDAO historiaClinicaDao;

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
				VentanaHistoriaClinica frame = new VentanaHistoriaClinica();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaHistoriaClinica() {
		historiaClinicaDao = new HistoriaClinicaDAO();

		// Configuración básica de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gestión de Historia Clínica");
		setSize(850, 680);
		setMinimumSize(new Dimension(750, 580));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);

		// 1. Encabezado
		JPanel panelHeader = new JPanel(new BorderLayout(0, 4));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Gestión de Historias Clínicas", SwingConstants.LEFT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Registro de diagnósticos, consultas y seguimiento de pacientes", SwingConstants.LEFT);
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

		// Fila 0: ID HC y ID Médico
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("ID Historia Clínica:"), gbc);

		txtIdHistoriaClinica = crearTextField();
		txtIdHistoriaClinica.setEditable(false); // Solo lectura para el ID autoincremental
		txtIdHistoriaClinica.setFocusable(false);
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
		panelFormulario.add(txtIdHistoriaClinica, gbc);

		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("ID Médico:"), gbc);

		txtIdMedico = crearTextField();
		gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5;
		panelFormulario.add(txtIdMedico, gbc);

		// Fila 1: ID Paciente y Fecha
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("ID Paciente:"), gbc);

		txtIdPaciente = crearTextField();
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtIdPaciente, gbc);

		gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Fecha (AAAA-MM-DD):"), gbc);

		txtFechaHc = crearTextField();
		gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtFechaHc, gbc);

		// Fila 2: Diagnóstico (JTextArea)
		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panelFormulario.add(crearLabel("Diagnóstico:"), gbc);

		txaDiagnostico = new JTextArea(3, 20);
		txaDiagnostico.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txaDiagnostico.setForeground(COLOR_TEXTO);
		txaDiagnostico.setBackground(COLOR_INPUT);
		txaDiagnostico.setCaretColor(COLOR_TEXTO);
		txaDiagnostico.setLineWrap(true);
		txaDiagnostico.setWrapStyleWord(true);
		txaDiagnostico.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)
		));

		JScrollPane scrollDiagnostico = new JScrollPane(txaDiagnostico);
		scrollDiagnostico.setBorder(null);

		gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1.0; gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.BOTH;
		panelFormulario.add(scrollDiagnostico, gbc);

		panelCentral.add(panelFormulario, BorderLayout.NORTH);

		// --- Tabla de Historias Clínicas ---
		String[] columnasHistoriasClinicas = {"ID HC", "ID Médico", "ID Paciente", "Diagnóstico", "Fecha"};
		modeloTablaHistoriasClinicas = new DefaultTableModel(columnasHistoriasClinicas, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaHistoriasClinicas = new JTable(modeloTablaHistoriasClinicas);
		estilarTabla(tablaHistoriasClinicas);

		JScrollPane scrollTabla = new JScrollPane(tablaHistoriasClinicas);
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

		// 4. Configurar Listeners de Eventos
		configurarListeners();
	}

	private void configurarListeners() {
		// Evento Agregar
		btnAgregar.addActionListener(e -> {
			try {
				int idMedico = Integer.parseInt(txtIdMedico.getText().trim());
				int idPaciente = Integer.parseInt(txtIdPaciente.getText().trim());
				String diagnostico = txaDiagnostico.getText().trim();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaHc;
				try {
					fechaHc = dateFormat.parse(txtFechaHc.getText().trim());
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (diagnostico.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, ingrese el diagnóstico.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				HistoriaClinica nuevaHc = new HistoriaClinica(idMedico, idPaciente, diagnostico, fechaHc);

				if (historiaClinicaDao.agregarHistoriaClinica(nuevaHc)) {
					JOptionPane.showMessageDialog(this, "Historia Clínica guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarHistoriasClinicasEnTabla(idPaciente);
				} else {
					JOptionPane.showMessageDialog(this, "Error al guardar la Historia Clínica.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "ID Médico e ID Paciente deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Modificar
		btnModificar.addActionListener(e -> {
			int filaSeleccionada = tablaHistoriasClinicas.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Seleccione una historia clínica de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			try {
				int idHc = (int) modeloTablaHistoriasClinicas.getValueAt(filaSeleccionada, 0);
				int idMedico = Integer.parseInt(txtIdMedico.getText().trim());
				int idPaciente = Integer.parseInt(txtIdPaciente.getText().trim());
				String diagnostico = txaDiagnostico.getText().trim();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaHc;
				try {
					fechaHc = dateFormat.parse(txtFechaHc.getText().trim());
				} catch (ParseException ex) {
					JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (diagnostico.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, ingrese el diagnóstico.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				HistoriaClinica hcModificada = new HistoriaClinica(idHc, idMedico, idPaciente, diagnostico, fechaHc);

				if (historiaClinicaDao.modificarHistoriaClinica(hcModificada)) {
					JOptionPane.showMessageDialog(this, "Historia Clínica modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					cargarHistoriasClinicasEnTabla(idPaciente);
				} else {
					JOptionPane.showMessageDialog(this, "Error al modificar la Historia Clínica.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "ID Médico e ID Paciente deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Eliminar
		btnEliminar.addActionListener(e -> {
			int filaSeleccionada = tablaHistoriasClinicas.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(this, "Seleccione una historia clínica de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idHc = (int) modeloTablaHistoriasClinicas.getValueAt(filaSeleccionada, 0);

			int confirmacion = JOptionPane.showConfirmDialog(this,
					"¿Está seguro de que desea eliminar la Historia Clínica ID: " + idHc + "?",
					"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

			if (confirmacion == JOptionPane.YES_OPTION) {
				if (historiaClinicaDao.eliminarHistoriaClinica(idHc)) {
					JOptionPane.showMessageDialog(this, "Historia Clínica eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
					modeloTablaHistoriasClinicas.setRowCount(0);
				} else {
					JOptionPane.showMessageDialog(this, "Error al eliminar la Historia Clínica.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Evento Buscar
		btnBuscar.addActionListener(e -> {
			try {
				String idPacienteText = txtIdPaciente.getText().trim();
				if (idPacienteText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Ingrese un ID de paciente para buscar sus historias clínicas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int idPaciente = Integer.parseInt(idPacienteText);
				cargarHistoriasClinicasEnTabla(idPaciente);

				if (modeloTablaHistoriasClinicas.getRowCount() == 0) {
					JOptionPane.showMessageDialog(this, "No se encontraron historias clínicas para el paciente ID " + idPaciente + ".", "Información", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El ID de paciente debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Limpiar / Cancelar
		btnCancelar.addActionListener(e -> {
			limpiarCampos();
			modeloTablaHistoriasClinicas.setRowCount(0);
		});

		// Selección en Tabla -> Llenar campos automáticamente
		tablaHistoriasClinicas.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaHistoriasClinicas.getSelectedRow() != -1) {
				int fila = tablaHistoriasClinicas.getSelectedRow();
				txtIdHistoriaClinica.setText(String.valueOf(modeloTablaHistoriasClinicas.getValueAt(fila, 0)));
				txtIdMedico.setText(String.valueOf(modeloTablaHistoriasClinicas.getValueAt(fila, 1)));
				txtIdPaciente.setText(String.valueOf(modeloTablaHistoriasClinicas.getValueAt(fila, 2)));
				txaDiagnostico.setText(String.valueOf(modeloTablaHistoriasClinicas.getValueAt(fila, 3)));
				txtFechaHc.setText(String.valueOf(modeloTablaHistoriasClinicas.getValueAt(fila, 4)));
			}
		});
	}

	private void cargarHistoriasClinicasEnTabla(int idPaciente) {
		modeloTablaHistoriasClinicas.setRowCount(0);
		List<HistoriaClinica> historiasClinicas = historiaClinicaDao.buscarHistoriasClinicasPorIdPaciente(idPaciente);

		if (historiasClinicas != null && !historiasClinicas.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (HistoriaClinica hc : historiasClinicas) {
				Vector<Object> fila = new Vector<>();
				fila.add(hc.getIdHc());
				fila.add(hc.getIdMedico());
				fila.add(hc.getIdPaciente());
				fila.add(hc.getDiagnostico());
				fila.add(hc.getFechaHc() != null ? dateFormat.format(hc.getFechaHc()) : "");
				modeloTablaHistoriasClinicas.addRow(fila);
			}
		}
	}

	private void limpiarCampos() {
		txtIdHistoriaClinica.setText("");
		txtIdMedico.setText("");
		txtIdPaciente.setText("");
		txaDiagnostico.setText("");
		txtFechaHc.setText("");
		tablaHistoriasClinicas.clearSelection();
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

		tabla.getTableHeader().setBackground(new Color(20, 23, 28));
		tabla.getTableHeader().setForeground(COLOR_TEXTO);
		tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		tabla.getTableHeader().setPreferredSize(new Dimension(0, 32));
		((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
	}
}