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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import controlador.MedicoDAO;
import modelo.Medico;

public class VentanaMedico extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Campos de entrada
	private JTextField txtDni;
	private JTextField txtApellido;
	private JTextField txtNombre;
	private JTextField txtDomicilio;
	private JTextField txtMatricula;
	private JTextField txtEspecializacion;
	private JComboBox<String> cmbLocalidad;

	// Botones
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnBuscar;
	private JButton btnCancelar;

	// DAO
	private MedicoDAO medicoDao;

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
				VentanaMedico frame = new VentanaMedico();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaMedico() {
		medicoDao = new MedicoDAO();

		// Configuración básica de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Gestión de Médicos");
		setSize(820, 600);
		setMinimumSize(new Dimension(720, 520));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);

		// 1. Encabezado
		JPanel panelHeader = new JPanel(new BorderLayout(0, 4));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Gestión del Personal Médico", SwingConstants.LEFT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Alta, baja, modificación y búsqueda de matriculados y especialidades", SwingConstants.LEFT);
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

		// Fila 1: Nombre y Domicilio
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Nombre:"), gbc);

		txtNombre = crearTextField();
		gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtNombre, gbc);

		gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Domicilio:"), gbc);

		txtDomicilio = crearTextField();
		gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
		panelFormulario.add(txtDomicilio, gbc);

		// Fila 2: Localidad y Matrícula
		gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Localidad:"), gbc);

		cmbLocalidad = new JComboBox<>(new String[]{
				"CABA", "La Plata", "Rosario", "Córdoba", "Mar del Plata", "General Las Heras"
		});
		estilarComboBox(cmbLocalidad);
		cmbLocalidad.setSelectedIndex(-1);
		gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.5;
		panelFormulario.add(cmbLocalidad, gbc);

		gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Matrícula:"), gbc);

		txtMatricula = crearTextField();
		gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.5;
		panelFormulario.add(txtMatricula, gbc);

		// Fila 3: Especialización (Abarca ambas columnas o fila única)
		gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
		panelFormulario.add(crearLabel("Especialización:"), gbc);

		txtEspecializacion = crearTextField();
		gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.weightx = 1.0;
		panelFormulario.add(txtEspecializacion, gbc);

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
				int dniMedico = Integer.parseInt(txtDni.getText().trim());
				String apellido = txtApellido.getText().trim();
				String nombre = txtNombre.getText().trim();
				String domicilio = txtDomicilio.getText().trim();
				String localidad = (String) cmbLocalidad.getSelectedItem();
				String matricula = txtMatricula.getText().trim();
				String especializacion = txtEspecializacion.getText().trim();

				if (apellido.isEmpty() || nombre.isEmpty() || matricula.isEmpty() || especializacion.isEmpty() || localidad == null || localidad.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Medico nuevoMedico = new Medico(dniMedico, apellido, nombre, domicilio, localidad, matricula, especializacion);

				if (medicoDao.agregarMedico(nuevoMedico)) {
					JOptionPane.showMessageDialog(this, "Médico guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
				} else {
					JOptionPane.showMessageDialog(this, "Error al guardar el médico. Verifique la consola.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Modificar
		btnModificar.addActionListener(e -> {
			try {
				int dniMedico = Integer.parseInt(txtDni.getText().trim());
				String apellido = txtApellido.getText().trim();
				String nombre = txtNombre.getText().trim();
				String domicilio = txtDomicilio.getText().trim();
				String localidad = (String) cmbLocalidad.getSelectedItem();
				String matricula = txtMatricula.getText().trim();
				String especializacion = txtEspecializacion.getText().trim();

				if (apellido.isEmpty() || nombre.isEmpty() || matricula.isEmpty() || especializacion.isEmpty() || localidad == null || localidad.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}

				Medico medicoModificado = new Medico(dniMedico, apellido, nombre, domicilio, localidad, matricula, especializacion);

				if (medicoDao.modificarMedico(medicoModificado)) {
					JOptionPane.showMessageDialog(this, "Médico modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
				} else {
					JOptionPane.showMessageDialog(this, "Error al modificar el médico. Verifique si el DNI existe.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Eliminar
		btnEliminar.addActionListener(e -> {
			try {
				String dniText = txtDni.getText().trim();
				if (dniText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Ingrese el DNI del médico a eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int dniAEliminar = Integer.parseInt(dniText);

				int confirmacion = JOptionPane.showConfirmDialog(this,
						"¿Está seguro de que desea eliminar al médico DNI: " + dniAEliminar + "?",
						"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

				if (confirmacion == JOptionPane.YES_OPTION) {
					if (medicoDao.eliminarMedico(dniAEliminar)) {
						JOptionPane.showMessageDialog(this, "Médico eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
						limpiarCampos();
					} else {
						JOptionPane.showMessageDialog(this, "Error al eliminar el médico.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Buscar
		btnBuscar.addActionListener(e -> {
			try {
				String dniText = txtDni.getText().trim();
				if (dniText.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Ingrese el DNI del médico a buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
					return;
				}
				int dniABuscar = Integer.parseInt(dniText);

				Medico medicoEncontrado = medicoDao.buscarPorDni(dniABuscar);

				if (medicoEncontrado != null) {
					cargarMedicoEnCampos(medicoEncontrado);
					JOptionPane.showMessageDialog(this, "Médico encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "No se encontró ningún médico con DNI: " + dniABuscar, "Información", JOptionPane.INFORMATION_MESSAGE);
					limpiarCampos();
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Evento Cancelar / Limpiar
		btnCancelar.addActionListener(e -> limpiarCampos());
	}

	private void limpiarCampos() {
		txtDni.setText("");
		txtApellido.setText("");
		txtNombre.setText("");
		txtDomicilio.setText("");
		cmbLocalidad.setSelectedIndex(-1);
		txtMatricula.setText("");
		txtEspecializacion.setText("");
	}

	private void cargarMedicoEnCampos(Medico medico) {
		if (medico != null) {
			txtDni.setText(String.valueOf(medico.getDniMedico()));
			txtApellido.setText(medico.getApellido());
			txtNombre.setText(medico.getNombre());
			txtDomicilio.setText(medico.getDomicilio());
			cmbLocalidad.setSelectedItem(medico.getLocalidad());
			txtMatricula.setText(medico.getMatricula());
			txtEspecializacion.setText(medico.getEspecializacion());
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