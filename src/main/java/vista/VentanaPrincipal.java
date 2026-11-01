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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Componentes principales
	private JButton btnMedicos;
	private JButton btnPacientes;
	private JButton btnEstudios;
	private JButton btnHclinicas;
	private JButton btnPatologias;

	// Paleta de Colores Dark Mode / Pro
	private static final Color COLOR_FONDO = new Color(24, 26, 31);
	private static final Color COLOR_TARJETA = new Color(33, 37, 43);
	private static final Color COLOR_TEXTO = new Color(240, 240, 240);
	private static final Color COLOR_TEXTO_MUTED = new Color(160, 165, 175);
	private static final Color COLOR_PRIMARIO = new Color(14, 116, 144);
	private static final Color COLOR_BOTON_HOVER = new Color(2, 132, 199);
	private static final Color COLOR_BOTON_TEXTO = Color.WHITE;

	public static void main(String[] args) {
		// Aplicar Look & Feel del sistema antes de iniciar
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaPrincipal() {
		// Configuración general de la ventana
		setTitle("Sistema de Gestión de Enfermería");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(720, 480);
		setMinimumSize(new Dimension(640, 420));
		setLocationRelativeTo(null); // Centrar en pantalla

		contentPane = new JPanel();
		contentPane.setBackground(COLOR_FONDO);
		contentPane.setBorder(new EmptyBorder(25, 30, 25, 30));
		contentPane.setLayout(new BorderLayout(0, 20));
		setContentPane(contentPane);

		// 1. Encabezado / Banner Superior
		JPanel panelHeader = new JPanel(new BorderLayout(0, 5));
		panelHeader.setOpaque(false);

		JLabel lblTitulo = new JLabel("Sistema de Gestión de Enfermería", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitulo.setForeground(COLOR_TEXTO);

		JLabel lblSubtitulo = new JLabel("Panel de Control Principal", SwingConstants.CENTER);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblSubtitulo.setForeground(COLOR_TEXTO_MUTED);

		panelHeader.add(lblTitulo, BorderLayout.NORTH);
		panelHeader.add(lblSubtitulo, BorderLayout.CENTER);
		contentPane.add(panelHeader, BorderLayout.NORTH);

		// 2. Panel Central de Acciones (Grid 2 columnas)
		JPanel panelBotones = new JPanel(new GridBagLayout());
		panelBotones.setBackground(COLOR_TARJETA);
		panelBotones.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(50, 56, 66), 1),
				new EmptyBorder(25, 30, 25, 30)
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		// Instanciar y personalizar botones
		btnPacientes = crearBotonEstilizado("Pacientes");
		btnMedicos = crearBotonEstilizado("Médicos");
		btnEstudios = crearBotonEstilizado("Estudios");
		btnPatologias = crearBotonEstilizado("Patologías");
		btnHclinicas = crearBotonEstilizado("Historias Clínicas");

		// Fila 1
		gbc.gridx = 0; gbc.gridy = 0;
		panelBotones.add(btnPacientes, gbc);
		gbc.gridx = 1; gbc.gridy = 0;
		panelBotones.add(btnMedicos, gbc);

		// Fila 2
		gbc.gridx = 0; gbc.gridy = 1;
		panelBotones.add(btnEstudios, gbc);
		gbc.gridx = 1; gbc.gridy = 1;
		panelBotones.add(btnPatologias, gbc);

		// Fila 3 (Botón principal ocupando ancho completo)
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.gridwidth = 2;
		panelBotones.add(btnHclinicas, gbc);

		contentPane.add(panelBotones, BorderLayout.CENTER);

		// 3. Asignación de Listeners
		btnPacientes.addActionListener(e -> new VentanaPaciente().setVisible(true));
		btnMedicos.addActionListener(e -> new VentanaMedico().setVisible(true));
		btnEstudios.addActionListener(e -> new VentanaEstudio().setVisible(true));
		btnPatologias.addActionListener(e -> new VentanaPatologia().setVisible(true));
		btnHclinicas.addActionListener(e -> new VentanaHistoriaClinica().setVisible(true));
	}

	/**
	 * Método auxilar para estilar botones de forma consistente.
	 */
	private JButton crearBotonEstilizado(String texto) {
		JButton btn = new JButton(texto);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setForeground(COLOR_BOTON_TEXTO);
		btn.setBackground(COLOR_PRIMARIO);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(180, 45));

		// Efecto Hover simple al pasar el cursor
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