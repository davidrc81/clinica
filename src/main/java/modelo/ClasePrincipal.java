package modelo;

import java.awt.EventQueue;
import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import controlador.Conexion;
import vista.VentanaPrincipal;

public class ClasePrincipal {

	public static void main(String[] args) {
		// 1. Configurar Look and Feel nativo antes de inicializar componentes UI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("No se pudo aplicar el Look & Feel del sistema: " + e.getMessage());
		}

		System.out.println("Iniciando aplicación de gestión de enfermería...");

		// 2. Verificar la conexión a la base de datos al arrancar
		Connection testConexion = Conexion.getConexion();

		if (testConexion != null) {
			System.out.println("¡Conexión a la base de datos establecida correctamente!");

			// 3. Lanzar la interfaz principal en el Event Dispatch Thread (EDT)
			EventQueue.invokeLater(() -> {
				try {
					VentanaPrincipal ventana = new VentanaPrincipal();
					ventana.setVisible(true);
				} catch (Exception e) {
					System.err.println("Error al lanzar la Ventana Principal: " + e.getMessage());
					e.printStackTrace();
					JOptionPane.showMessageDialog(
							null,
							"Ocurrió un error al iniciar la interfaz de usuario.\nVerifique la consola para más detalles.",
							"Error de Inicio de Aplicación",
							JOptionPane.ERROR_MESSAGE
					);
				}
			});

		} else {
			System.err.println("Error: No se pudo establecer la conexión a la base de datos.");
			JOptionPane.showMessageDialog(
					null,
					"No se pudo conectar a la base de datos MySQL.\nLa aplicación no puede continuar.\n\n" +
					"Por favor, verifique que el servicio de MySQL esté corriendo y las credenciales sean correctas.",
					"Error Crítico de Conexión",
					JOptionPane.ERROR_MESSAGE
			);
			System.exit(1);
		}
	}
}