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
import java.text.SimpleDateFormat;
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

import controlador.PacienteDAO;
import modelo.Paciente;

public class VentanaPacienteBuscar extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCampoBusqueda;
	private JTable tablaPacientes;
	private DefaultTableModel modeloTablaPacientes;

	private JButton btnBuscar;
	private JButton btnVolver;

	private PacienteDAO pacienteDao;
	private VentanaPaciente ventanaPacienteReferencia;

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
				VentanaPacienteBuscar frame = new VentanaPacienteBuscar();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public VentanaPacienteBuscar(VentanaPaciente ventanaPacienteReferencia) {
		this();
		this.ventanaPacienteReferencia = ventanaPacienteReferencia;
	}

	public VentanaPacienteBuscar() {
		pacienteDao = new PacienteDAO();

		// Configuración básica de la ventana
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Buscar Paciente");
		setSize(900, 600);
		setMinimumSize(new Dimension(750, 480));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setLayout(new BorderLayout(0, 15));
		setContentPane(contentPane);

		// 1. Encabezado
		JPanel panelHeader = new JPanel(new BorderLayout(0, 4));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Búsqueda de Pacientes", SwingConstants.LEFT);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Filtre por DNI o Apellido. Seleccione una fila para cargar sus datos en la ventana de gestión.", SwingConstants.LEFT);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubtitulo.setForeground(COLOR_TEXTO_MUTED);

		panelHeader.add(lblTitulo, BorderLayout.NORTH);
		panelHeader.add(lblSubtitulo, BorderLayout.CENTER);
		contentPane.add(panelHeader, BorderLayout.NORTH);

		// 2. Panel Central (Barra de Búsqueda + Tabla)
		JPanel panelCentral = new JPanel(new BorderLayout(0, 12));
		panelCentral.setOpaque(false);

		// --- Panel Filtro de Búsqueda ---
		JPanel panelBusqueda = new JPanel(new GridBagLayout());
		panelBusqueda.setBackground(COLOR_TARJETA);
		panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(50, 56, 66), 1),
				new EmptyBorder(12, 15, 12, 15)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 5, 0, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
		panelBusqueda.add(crearLabel("Buscar por DNI / Apellido:"), gbc);

		txtCampoBusqueda = crearTextField();
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
		panelBusqueda.add(txtCampoBusqueda, gbc);

		btnBuscar = crearBotonEstilizado("Buscar");
		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
		panelBusqueda.add(btnBuscar, gbc);

		panelCentral.add(panelBusqueda, BorderLayout.NORTH);

		// --- Tabla de Resultados ---
		String[] columnas = {"DNI", "Apellido", "Nombre", "Sexo", "Fecha Nac.",
				"Domicilio", "Localidad", "Peso", "Altura", "Grupo", "Factor"};

		modeloTablaPacientes = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaPacientes = new JTable(modeloTablaPacientes);
		estilarTabla(tablaPacientes);

		JScrollPane scrollTabla = new JScrollPane(tablaPacientes);
		scrollTabla.getViewport().setBackground(COLOR_TARJETA);
		scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(50, 56, 66), 1));
		panelCentral.add(scrollTabla, BorderLayout.CENTER);

		contentPane.add(panelCentral, BorderLayout.CENTER);

		// 3. Panel Inferior (Botón Volver)
		JPanel panelInferior = new JPanel(new GridBagLayout());
		panelInferior.setOpaque(false);

		btnVolver = crearBotonEstilizado("Volver / Cerrar");
		btnVolver.setPreferredSize(new Dimension(150, 36));

		GridBagConstraints gbcBottom = new GridBagConstraints();
		gbcBottom.anchor = GridBagConstraints.CENTER;
		panelInferior.add(btnVolver, gbcBottom);

		contentPane.add(panelInferior, BorderLayout.SOUTH);

		// 4. Configurar Listeners
		configurarListeners();

		// Cargar lista inicial
		cargarTodosLosPacientesEnTabla();
	}

	private void configurarListeners() {
		// Evento Buscar
		btnBuscar.addActionListener(e -> ejecutarBusqueda());
		txtCampoBusqueda.addActionListener(e -> ejecutarBusqueda()); // Enter en el campo de texto

		// Evento Volver
		btnVolver.addActionListener(e -> dispose());

		// Evento Seleccionar Fila en Tabla -> Cargar en VentanaPaciente
		tablaPacientes.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaPacientes.getSelectedRow() != -1) {
				int filaSeleccionada = tablaPacientes.getSelectedRow();
				if (filaSeleccionada >= 0) {
					int dniSeleccionado = (int) modeloTablaPacientes.getValueAt(filaSeleccionada, 0);

					if (ventanaPacienteReferencia != null) {
						try {
							Paciente pacienteSeleccionado = pacienteDao.buscarPorDni(dniSeleccionado);
							if (pacienteSeleccionado != null) {
								ventanaPacienteReferencia.cargarPacienteEnCampos(pacienteSeleccionado);
								dispose();
							}
						} catch (Exception ex) {
							System.err.println("Error al cargar paciente seleccionado: " + ex.getMessage());
							ex.printStackTrace();
							JOptionPane.showMessageDialog(this, "Error al cargar los datos del paciente.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
	}

	private void ejecutarBusqueda() {
		String criterioBusqueda = txtCampoBusqueda.getText().trim();
		if (criterioBusqueda.isEmpty()) {
			cargarTodosLosPacientesEnTabla();
			if (modeloTablaPacientes.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "No hay pacientes registrados.", "Información", JOptionPane.INFORMATION_MESSAGE);
			}
			return;
		}

		cargarPacientesEnTablaPorCriterio(criterioBusqueda);

		if (modeloTablaPacientes.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "No se encontraron pacientes con ese criterio.", "Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void cargarTodosLosPacientesEnTabla() {
		modeloTablaPacientes.setRowCount(0);
		List<Paciente> pacientes = pacienteDao.obtenerTodosPacientes();
		poblarTabla(pacientes);
	}

	private void cargarPacientesEnTablaPorCriterio(String criterio) {
		modeloTablaPacientes.setRowCount(0);
		List<Paciente> pacientes = pacienteDao.buscarPacientes(criterio);
		poblarTabla(pacientes);
	}

	private void poblarTabla(List<Paciente> pacientes) {
		if (pacientes != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (Paciente p : pacientes) {
				Vector<Object> fila = new Vector<>();
				fila.add(p.getDni());
				fila.add(p.getApellido());
				fila.add(p.getNombre());
				fila.add(p.getSexo());
				fila.add(p.getFechaNacimiento() != null ? dateFormat.format(p.getFechaNacimiento()) : "");
				fila.add(p.getDomicilio());
				fila.add(p.getLocalidad());
				fila.add(p.getPeso());
				fila.add(p.getAltura());
				fila.add(p.getGrupoSanguineo());
				fila.add(p.getFactorRh());
				modeloTablaPacientes.addRow(fila);
			}
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

	private JButton crearBotonEstilizado(String texto) {
		JButton btn = new JButton(texto);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btn.setForeground(COLOR_BOTON_TEXTO);
		btn.setBackground(COLOR_PRIMARIO);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(110, 34));

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
