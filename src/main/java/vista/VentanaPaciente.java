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
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import controlador.PacienteDAO;
import modelo.Paciente;

public class VentanaPaciente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Campos de entrada
	private JTextField txtDni;
	private JTextField txtApellido;
	private JTextField txtNombre;
	private JTextField txtDomicilio;
	private JTextField txtPeso;
	private JTextField txtAltura;
	private JComboBox<String> cmbSexo;
	private JDateChooser dateChooserFechaNacimiento;
	private JComboBox<String> cmbLocalidad;
	private JComboBox<String> cmbGrupoSanguineo;
	private JRadioButton rbnRhPositivo;
	private JRadioButton rbnRhNegativo;
	private ButtonGroup grupoFactorRh;

	// Botones
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnBuscar;
	private JButton btnCancelar;

	// DAO
	private PacienteDAO pacienteDao;

	// Paleta de Colores Dark Mode
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
				VentanaPaciente frame = new VentanaPaciente();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaPaciente() {
		pacienteDao = new PacienteDAO();

		// Configuración básica de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gestión de Pacientes");
		setSize(850, 650);
		setMinimumSize(new Dimension(750, 560));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);

		// 1. Encabezado
		JPanel panelHeader = new JPanel(new BorderLayout(0, 4));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Gestión de Pacientes", SwingConstants.LEFT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Registro, actualización de datos filiatorios e historia clínica", SwingConstants.LEFT);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubtitulo.setForeground(COLOR_TEXTO_MUTED);

		panelHeader.add(lblTitulo, BorderLayout.NORTH);
		panelHeader.add(lblSubtitulo, BorderLayout.CENTER);
		contentPane.add(panelHeader, BorderLayout.NORTH);

		// 2. Panel Formulario
		JPanel panelFormulario = new JPanel(new GridBagLayout());
		panelFormulario.setBackground(COLOR_TARJETA);
		panelFormulario.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(50, 56, 66), 1),
				new EmptyBorder(20, 25, 20, 25)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Fila 0: DNI y Apellido
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("DNI:"), gbc);

		txtDni = crearTextField();
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
		panelFormulario.add(txtDni, gbc);

		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Apellido:"), gbc);

		txtApellido = crearTextField();
		gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5;
		panelFormulario.add(txtApellido, gbc);

		// Fila 1: Nombre y Sexo
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Nombre:"), gbc);

		txtNombre = crearTextField();
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtNombre, gbc);

		gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Sexo:"), gbc);

		cmbSexo = new JComboBox<>(new String[] {"Masculino", "Femenino", "Otro"});
		estilarComboBox(cmbSexo);
		cmbSexo.setSelectedIndex(-1);
		gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(cmbSexo, gbc);

		// Fila 2: Fecha Nacimiento y Domicilio
		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Fecha Nacimiento:"), gbc);

		dateChooserFechaNacimiento = new JDateChooser();
		dateChooserFechaNacimiento.setDateFormatString("yyyy-MM-dd");
		dateChooserFechaNacimiento.setBackground(COLOR_INPUT);
		dateChooserFechaNacimiento.setPreferredSize(new Dimension(150, 34));
		gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.5;
		panelFormulario.add(dateChooserFechaNacimiento, gbc);

		gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Domicilio:"), gbc);

		txtDomicilio = crearTextField();
		gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.5;
		panelFormulario.add(txtDomicilio, gbc);

		// Fila 3: Localidad y Peso
		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Localidad:"), gbc);

		cmbLocalidad = new JComboBox<>(new String[]{
				"CABA", "La Plata", "Rosario", "Córdoba", "Mar del Plata", "General Las Heras"
		});
		estilarComboBox(cmbLocalidad);
		cmbLocalidad.setSelectedIndex(-1);
		gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.5;
		panelFormulario.add(cmbLocalidad, gbc);

		gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Peso (kg):"), gbc);

		txtPeso = crearTextField();
		gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.5;
		panelFormulario.add(txtPeso, gbc);

		// Fila 4: Altura y Grupo Sanguíneo
		gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Altura (m):"), gbc);

		txtAltura = crearTextField();
		gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.5;
		panelFormulario.add(txtAltura, gbc);

		gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Grupo Sanguíneo:"), gbc);

		cmbGrupoSanguineo = new JComboBox<>(new String[] {"A", "B", "AB", "O"});
		estilarComboBox(cmbGrupoSanguineo);
		cmbGrupoSanguineo.setSelectedIndex(-1);
		gbc.gridx = 3; gbc.gridy = 4; gbc.weightx = 0.5;
		panelFormulario.add(cmbGrupoSanguineo, gbc);

		// Fila 5: Factor Rh
		gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Factor Rh:"), gbc);

		JPanel panelRadioRh = new JPanel(new GridBagLayout());
		panelRadioRh.setOpaque(false);

		rbnRhPositivo = crearRadioButton("RH+");
		rbnRhNegativo = crearRadioButton("RH-");

		grupoFactorRh = new ButtonGroup();
		grupoFactorRh.add(rbnRhPositivo);
		grupoFactorRh.add(rbnRhNegativo);

		GridBagConstraints gbcRadio = new GridBagConstraints();
		gbcRadio.insets = new Insets(0, 0, 0, 15);
		panelRadioRh.add(rbnRhPositivo, gbcRadio);
		gbcRadio.insets = new Insets(0, 0, 0, 0);
		panelRadioRh.add(rbnRhNegativo, gbcRadio);

		gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 3; gbc.weightx = 1.0;
		panelFormulario.add(panelRadioRh, gbc);

		contentPane.add(panelFormulario, BorderLayout.CENTER);

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
				int dni = Integer.parseInt(txtDni.getText().trim());
				String apellido = txtApellido.getText().trim();
				String nombre = txtNombre.getText().trim();
				String sexo = (String) cmbSexo.getSelectedItem();
				Date fechaNacimiento = dateChooserFechaNacimiento.getDate();
				String domicilio = txtDomicilio.getText().trim();
				String localidad = (String) cmbLocalidad.getSelectedItem();
				float peso = Float.parseFloat(txtPeso.getText().trim());
				float altura = Float.parseFloat(txtAltura.getText().trim());
				String grupoSanguineo = (String) cmbGrupoSanguineo.getSelectedItem();

				String factorRh = "";
				if (rbnRhPositivo.isSelected()) {
					factorRh = "RH+";
				} else if (rbnRhNegativo.isSelected()) {
					factorRh = "RH-";
				} else {
					JOptionPane.showMessageDialog(this, "Debe seleccionar un Factor Rh (RH+ o RH-).", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (apellido.isEmpty() || nombre.isEmpty() || sexo == null || fechaNacimiento == null ||
					domicilio.isEmpty() || localidad == null || grupoSanguineo == null || factorRh.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (peso <= 0 || altura <= 0) {
					JOptionPane.showMessageDialog(this, "Peso y Altura deben ser valores positivos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Paciente nuevoPaciente = new Paciente(dni, apellido, nombre, sexo, fechaNacimiento,
						domicilio, localidad, peso, altura, grupoSanguineo, factorRh);

				if (pacienteDao.agregarPaciente(nuevoPaciente)) {
					JOptionPane.showMessageDialog(this, "Paciente guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
				} else {
					JOptionPane.showMessageDialog(this, "Error al guardar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "DNI, Peso y Altura deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Modificar
		btnModificar.addActionListener(e -> {
			try {
				int dni = Integer.parseInt(txtDni.getText().trim());
				String apellido = txtApellido.getText().trim();
				String nombre = txtNombre.getText().trim();
				String sexo = (String) cmbSexo.getSelectedItem();
				Date fechaNacimiento = dateChooserFechaNacimiento.getDate();
				String domicilio = txtDomicilio.getText().trim();
				String localidad = (String) cmbLocalidad.getSelectedItem();
				float peso = Float.parseFloat(txtPeso.getText().trim());
				float altura = Float.parseFloat(txtAltura.getText().trim());
				String grupoSanguineo = (String) cmbGrupoSanguineo.getSelectedItem();

				String factorRh = "";
				if (rbnRhPositivo.isSelected()) {
					factorRh = "RH+";
				} else if (rbnRhNegativo.isSelected()) {
					factorRh = "RH-";
				} else {
					JOptionPane.showMessageDialog(this, "Debe seleccionar un Factor Rh (RH+ o RH-).", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (apellido.isEmpty() || nombre.isEmpty() || sexo == null || fechaNacimiento == null ||
					domicilio.isEmpty() || localidad == null || grupoSanguineo == null || factorRh.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (peso <= 0 || altura <= 0) {
					JOptionPane.showMessageDialog(this, "Peso y Altura deben ser valores positivos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Paciente pacienteModificado = new Paciente(dni, apellido, nombre, sexo, fechaNacimiento,
						domicilio, localidad, peso, altura, grupoSanguineo, factorRh);

				if (pacienteDao.modificarPaciente(pacienteModificado)) {
					JOptionPane.showMessageDialog(this, "Paciente modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
				} else {
					JOptionPane.showMessageDialog(this, "Error al modificar el paciente. Verifique si el DNI existe.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "DNI, Peso y Altura deben ser números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Eliminar
		btnEliminar.addActionListener(e -> {
			try {
				String dniText = txtDni.getText().trim();
				if (dniText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Ingrese el DNI del paciente a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int dniAEliminar = Integer.parseInt(dniText);

				int confirmacion = JOptionPane.showConfirmDialog(this,
						"¿Está seguro de que desea eliminar al paciente DNI: " + dniAEliminar + "?",
						"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

				if (confirmacion == JOptionPane.YES_OPTION) {
					if (pacienteDao.eliminarPaciente(dniAEliminar)) {
						JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
						limpiarCampos();
					} else {
						JOptionPane.showMessageDialog(this, "Error al eliminar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Buscar
		btnBuscar.addActionListener(e -> {
			VentanaPacienteBuscar ventanaBuscar = new VentanaPacienteBuscar();
			ventanaBuscar.setVisible(true);
		});

		// Evento Cancelar / Limpiar
		btnCancelar.addActionListener(e -> limpiarCampos());
	}

	private void limpiarCampos() {
		txtDni.setText("");
		txtApellido.setText("");
		txtNombre.setText("");
		cmbSexo.setSelectedIndex(-1);
		dateChooserFechaNacimiento.setDate(null);
		txtDomicilio.setText("");
		cmbLocalidad.setSelectedIndex(-1);
		txtPeso.setText("");
		txtAltura.setText("");
		cmbGrupoSanguineo.setSelectedIndex(-1);
		grupoFactorRh.clearSelection();
	}

	public void cargarPacienteEnCampos(Paciente paciente) {
		if (paciente != null) {
			txtDni.setText(String.valueOf(paciente.getDni()));
			txtApellido.setText(paciente.getApellido());
			txtNombre.setText(paciente.getNombre());
			cmbSexo.setSelectedItem(paciente.getSexo());
			dateChooserFechaNacimiento.setDate(paciente.getFechaNacimiento());
			txtDomicilio.setText(paciente.getDomicilio());
			cmbLocalidad.setSelectedItem(paciente.getLocalidad());
			txtPeso.setText(String.valueOf(paciente.getPeso()));
			txtAltura.setText(String.valueOf(paciente.getAltura()));
			cmbGrupoSanguineo.setSelectedItem(paciente.getGrupoSanguineo());
			if ("RH+".equals(paciente.getFactorRh())) {
				rbnRhPositivo.setSelected(true);
			} else if ("RH-".equals(paciente.getFactorRh())) {
				rbnRhNegativo.setSelected(true);
			} else {
				grupoFactorRh.clearSelection();
			}
		} else {
			limpiarCampos();
		}
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

	private JRadioButton crearRadioButton(String texto) {
		JRadioButton rbn = new JRadioButton(texto);
		rbn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		rbn.setForeground(COLOR_TEXTO);
		rbn.setOpaque(false);
		rbn.setFocusPainted(false);
		return rbn;
	}

	private void estilarComboBox(JComboBox<String> combo) {
		combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		combo.setForeground(COLOR_TEXTO);
		combo.setBackground(COLOR_INPUT);
		combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 34));
		combo.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_INPUT, 1));
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
}
