package controlador; // El paquete correcto para la capa de control/DAO

import modelo.Medico; // Importa la clase de modelo Medico
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Clase Data Access Object (DAO) para la entidad Medico.
 * Se encarga de la comunicación directa con la base de datos para
 * realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre la tabla 'medico'.
 */
public class MedicoDAO {

    /**
     * Constructor vacío. Los métodos de esta clase obtendrán la conexión
     * a la base de datos a través de la clase estática 'Conexion'.
     */
    public MedicoDAO() {
        // No se establece la conexión en el constructor; cada método la obtiene de 'Conexion'.
        // Se asume que la conexión principal es gestionada por la clase Conexion (Singleton).
    }

    /**
     * Inserta un nuevo médico en la base de datos.
     * @param medico El objeto Medico a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregarMedico(Medico medico) {
        Connection conn = null;
        PreparedStatement ps = null;
        // Se insertan todos los campos excepto los auto-incrementales (dni_med es PK, pero no auto-increment)
        String sql = "INSERT INTO medico (dni_med, ape, nom, dom, loc, matricula, especializacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean exito = false;

        try {
            conn = Conexion.getConexion(); // Obtener la conexión de la clase Conexion
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement según el objeto Medico
            ps.setInt(1, medico.getDniMedico());
            ps.setString(2, medico.getApellido());
            ps.setString(3, medico.getNombre());
            ps.setString(4, medico.getDomicilio());
            ps.setString(5, medico.getLocalidad());
            ps.setString(6, medico.getMatricula());
            ps.setString(7, medico.getEspecializacion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Médico con DNI " + medico.getDniMedico() + " agregado exitosamente.");
            }

        } catch (SQLException e) {
            // Manejar la excepción de clave duplicada si el DNI ya existe
            if (e.getErrorCode() == 1062) { // Error code for Duplicate entry for key 'PRIMARY'
                System.err.println("Error: El DNI " + medico.getDniMedico() + " ya existe en la base de datos.");
                JOptionPane.showMessageDialog(null, "Error: El DNI " + medico.getDniMedico() + " ya existe. Intente con otro DNI o use la función Modificar.", "DNI Duplicado", JOptionPane.ERROR_MESSAGE);
            } else {
                System.err.println("Error al agregar médico: " + e.getMessage());
                e.printStackTrace();
            }
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
     * Modifica un médico existente en la base de datos.
     * @param medico El objeto Medico con los datos actualizados.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarMedico(Medico medico) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE medico SET ape=?, nom=?, dom=?, loc=?, matricula=?, especializacion=? " +
                     "WHERE dni_med=?";
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement
            ps.setString(1, medico.getApellido());
            ps.setString(2, medico.getNombre());
            ps.setString(3, medico.getDomicilio());
            ps.setString(4, medico.getLocalidad());
            ps.setString(5, medico.getMatricula());
            ps.setString(6, medico.getEspecializacion());
            ps.setInt(7, medico.getDniMedico()); // DNI en la cláusula WHERE

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Médico con DNI " + medico.getDniMedico() + " modificado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar médico: " + e.getMessage());
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
     * Elimina un médico de la base de datos por su DNI.
     * @param dniMedico El DNI del médico a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarMedico(int dniMedico) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM medico WHERE dni_med=?";
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dniMedico);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Médico con DNI " + dniMedico + " eliminado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar médico: " + e.getMessage());
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
     * Busca un médico en la base de datos por su DNI.
     * @param dniMedico El DNI del médico a buscar.
     * @return El objeto Medico si se encuentra, o null si no existe.
     */
    public Medico buscarPorDni(int dniMedico) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Medico medico = null;
        String sql = "SELECT dni_med, ape, nom, dom, loc, matricula, especializacion FROM medico WHERE dni_med=?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dniMedico);
            rs = ps.executeQuery();

            if (rs.next()) {
                medico = new Medico();
                medico.setDniMedico(rs.getInt("dni_med"));
                medico.setApellido(rs.getString("ape"));
                medico.setNombre(rs.getString("nom"));
                medico.setDomicilio(rs.getString("dom"));
                medico.setLocalidad(rs.getString("loc"));
                medico.setMatricula(rs.getString("matricula"));
                medico.setEspecializacion(rs.getString("especializacion"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar médico por DNI: " + e.getMessage());
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
        return medico;
    }

    /**
     * Busca todos los médicos en la base de datos.
     * @return Una lista de objetos Medico, o una lista vacía si no hay médicos.
     */
    public List<Medico> obtenerTodosMedicos() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT dni_med, ape, nom, dom, loc, matricula, especializacion FROM medico";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return medicos;
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setDniMedico(rs.getInt("dni_med"));
                medico.setApellido(rs.getString("ape"));
                medico.setNombre(rs.getString("nom"));
                medico.setDomicilio(rs.getString("dom"));
                medico.setLocalidad(rs.getString("loc"));
                medico.setMatricula(rs.getString("matricula"));
                medico.setEspecializacion(rs.getString("especializacion"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los médicos: " + e.getMessage());
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
        return medicos;
    }

    /**
     * Busca médicos que coincidan con un criterio de búsqueda (DNI o parte del nombre/apellido).
     * @param criterio El DNI (exacto) o una parte del nombre/apellido.
     * @return Una lista de objetos Medico que coinciden, o una lista vacía.
     */
    public List<Medico> buscarMedicos(String criterio) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT dni_med, ape, nom, dom, loc, matricula, especializacion FROM medico " +
                     "WHERE dni_med = ? OR ape LIKE ? OR nom LIKE ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return medicos;
            }
            ps = conn.prepareStatement(sql);
            
            // Intentar convertir el criterio a int para la búsqueda por DNI
            try {
                int dniCriterio = Integer.parseInt(criterio);
                ps.setInt(1, dniCriterio); // Para dni_med = ?
            } catch (NumberFormatException e) {
                // Si no es un número, se pone un valor que no coincidirá con ningún DNI
                ps.setInt(1, -1); 
            }
            
            // Para búsqueda por LIKE
            ps.setString(2, "%" + criterio + "%"); // Para ape LIKE ?
            ps.setString(3, "%" + criterio + "%"); // Para nom LIKE ?

            rs = ps.executeQuery();

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setDniMedico(rs.getInt("dni_med"));
                medico.setApellido(rs.getString("ape"));
                medico.setNombre(rs.getString("nom"));
                medico.setDomicilio(rs.getString("dom"));
                medico.setLocalidad(rs.getString("loc"));
                medico.setMatricula(rs.getString("matricula"));
                medico.setEspecializacion(rs.getString("especializacion"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar médicos por criterio: " + e.getMessage());
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
        return medicos;
    }
}
