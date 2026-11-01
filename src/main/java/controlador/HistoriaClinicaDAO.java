package controlador; // El paquete correcto para la capa de control/DAO

import modelo.HistoriaClinica; // Importa la clase de modelo HistoriaClinica
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date; // Para convertir java.util.Date a java.sql.Date

/**
 * Clase Data Access Object (DAO) para la entidad HistoriaClinica.
 * Se encarga de la comunicación directa con la base de datos para
 * realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre la tabla 'historia_clinica'.
 */
public class HistoriaClinicaDAO {

    /**
     * Constructor vacío. Los métodos de esta clase obtendrán la conexión
     * a la base de datos a través de la clase estática 'Conexion'.
     */
    public HistoriaClinicaDAO() {
        // No se establece la conexión en el constructor; cada método la obtiene de 'Conexion'.
    }

    /**
     * Inserta una nueva historia clínica en la base de datos.
     * El 'id_hc' es auto-incremental y será generado por la base de datos.
     * @param historiaClinica El objeto HistoriaClinica a insertar (sin idHc).
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregarHistoriaClinica(HistoriaClinica historiaClinica) {
        Connection conn = null;
        PreparedStatement ps = null;
        // id_hc es auto-incremental, no se incluye en el INSERT
        String sql = "INSERT INTO historia_clinica (id_medico, id_pac, diagnostico, fecha_hc) " +
                     "VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try {
            conn = Conexion.getConexion(); // Obtener la conexión de la clase Conexion
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement según el objeto HistoriaClinica
            ps.setInt(1, historiaClinica.getIdMedico());
            ps.setInt(2, historiaClinica.getIdPaciente());
            ps.setString(3, historiaClinica.getDiagnostico());
            // Convertir java.util.Date a java.sql.Date para la base de datos
            ps.setDate(4, new Date(historiaClinica.getFechaHc().getTime()));

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Historia Clínica para paciente DNI " + historiaClinica.getIdPaciente() + " agregada exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar historia clínica: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
            // No se cierra la conexión aquí si 'Conexion' es un Singleton
        }
        return exito;
    }

    /**
     * Modifica una historia clínica existente en la base de datos por su ID.
     * @param historiaClinica El objeto HistoriaClinica con los datos actualizados.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarHistoriaClinica(HistoriaClinica historiaClinica) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE historia_clinica SET id_medico = ?, id_pac = ?, diagnostico = ?, fecha_hc = ? " +
                     "WHERE id_hc = ?";
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement
            ps.setInt(1, historiaClinica.getIdMedico());
            ps.setInt(2, historiaClinica.getIdPaciente());
            ps.setString(3, historiaClinica.getDiagnostico());
            ps.setDate(4, new Date(historiaClinica.getFechaHc().getTime()));
            ps.setInt(5, historiaClinica.getIdHc()); // ID de la historia clínica a modificar

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Historia Clínica con ID " + historiaClinica.getIdHc() + " modificada exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar historia clínica: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        return exito;
    }

    /**
     * Elimina una historia clínica de la base de datos por su ID.
     * @param idHc El ID de la historia clínica a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarHistoriaClinica(int idHc) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM historia_clinica WHERE id_hc = ?";
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idHc);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Historia Clínica con ID " + idHc + " eliminada exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar historia clínica: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        return exito;
    }

    /**
     * Busca una historia clínica en la base de datos por su ID.
     * @param idHc El ID de la historia clínica a buscar.
     * @return El objeto HistoriaClinica si se encuentra, o null si no existe.
     */
    public HistoriaClinica buscarPorId(int idHc) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HistoriaClinica historiaClinica = null;
        String sql = "SELECT id_hc, id_medico, id_pac, diagnostico, fecha_hc FROM historia_clinica WHERE id_hc = ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idHc);
            rs = ps.executeQuery();

            if (rs.next()) {
                historiaClinica = new HistoriaClinica(
                    rs.getInt("id_hc"),
                    rs.getInt("id_medico"),
                    rs.getInt("id_pac"),
                    rs.getString("diagnostico"),
                    rs.getDate("fecha_hc") // getDate() retorna java.sql.Date, compatible con java.util.Date
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar historia clínica por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        return historiaClinica;
    }

    /**
     * Busca todas las historias clínicas en la base de datos asociadas a un DNI de paciente.
     * @param idPaciente El DNI del paciente cuyas historias clínicas se desean obtener.
     * @return Una lista de objetos HistoriaClinica, o una lista vacía si no hay historias clínicas para ese paciente.
     */
    public List<HistoriaClinica> buscarHistoriasClinicasPorIdPaciente(int idPaciente) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<HistoriaClinica> historiasClinicas = new ArrayList<>();
        String sql = "SELECT id_hc, id_medico, id_pac, diagnostico, fecha_hc FROM historia_clinica WHERE id_pac = ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return historiasClinicas;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            rs = ps.executeQuery();

            while (rs.next()) {
                HistoriaClinica historiaClinica = new HistoriaClinica(
                    rs.getInt("id_hc"),
                    rs.getInt("id_medico"),
                    rs.getInt("id_pac"),
                    rs.getString("diagnostico"),
                    rs.getDate("fecha_hc")
                );
                historiasClinicas.add(historiaClinica);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar historias clínicas por ID de paciente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        return historiasClinicas;
    }

    /**
     * Obtiene todas las historias clínicas de la base de datos.
     * @return Una lista de todos los objetos HistoriaClinica.
     */
    public List<HistoriaClinica> obtenerTodasHistoriasClinicas() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<HistoriaClinica> historiasClinicas = new ArrayList<>();
        String sql = "SELECT id_hc, id_medico, id_pac, diagnostico, fecha_hc FROM historia_clinica";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return historiasClinicas;
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                HistoriaClinica historiaClinica = new HistoriaClinica(
                    rs.getInt("id_hc"),
                    rs.getInt("id_medico"),
                    rs.getInt("id_pac"),
                    rs.getString("diagnostico"),
                    rs.getDate("fecha_hc")
                );
                historiasClinicas.add(historiaClinica);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las historias clínicas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        return historiasClinicas;
    }
}
