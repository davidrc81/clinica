package controlador; // El paquete correcto para la capa de control/DAO

import modelo.Estudio; // Importa la clase de modelo Estudio
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date; // Para convertir java.util.Date a java.sql.Date

/**
 * Clase Data Access Object (DAO) para la entidad Estudio.
 * Se encarga de la comunicación directa con la base de datos para
 * realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre la tabla 'estudio'.
 */
public class EstudioDAO {

    /**
     * Constructor vacío. Los métodos de esta clase obtendrán la conexión
     * a la base de datos a través de la clase estática 'Conexion'.
     */
    public EstudioDAO() {
        // No se establece la conexión en el constructor; cada método la obtiene de 'Conexion'.
    }

    /**
     * Inserta un nuevo estudio en la base de datos.
     * El 'id_estudio' es auto-incremental y será generado por la base de datos.
     * @param estudio El objeto Estudio a insertar (sin idEstudio).
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregarEstudio(Estudio estudio) {
        Connection conn = null;
        PreparedStatement ps = null;
        // id_estudio es auto-incremental, no se incluye en el INSERT
        String sql = "INSERT INTO estudio (dni_pac, id_med, tipo, resultado, fecha_e) " +
                     "VALUES (?, ?, ?, ?, ?)";
        boolean exito = false;

        try {
            conn = Conexion.getConexion(); // Obtener la conexión de la clase Conexion
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement según el objeto Estudio
            ps.setInt(1, estudio.getDniPaciente());
            ps.setInt(2, estudio.getIdMedico());
            ps.setString(3, estudio.getTipo());
            ps.setString(4, estudio.getResultado());
            // Convertir java.util.Date a java.sql.Date para la base de datos
            ps.setDate(5, new Date(estudio.getFechaEstudio().getTime()));

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Estudio para paciente DNI " + estudio.getDniPaciente() + " agregado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar estudio: " + e.getMessage());
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
     * Modifica un estudio existente en la base de datos por su ID.
     * @param estudio El objeto Estudio con los datos actualizados.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarEstudio(Estudio estudio) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE estudio SET dni_pac = ?, id_med = ?, tipo = ?, resultado = ?, fecha_e = ? " +
                     "WHERE id_estudio = ?";
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement
            ps.setInt(1, estudio.getDniPaciente());
            ps.setInt(2, estudio.getIdMedico());
            ps.setString(3, estudio.getTipo());
            ps.setString(4, estudio.getResultado());
            ps.setDate(5, new Date(estudio.getFechaEstudio().getTime()));
            ps.setInt(6, estudio.getIdEstudio()); // ID del estudio a modificar

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Estudio con ID " + estudio.getIdEstudio() + " modificado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar estudio: " + e.getMessage());
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
     * Elimina un estudio de la base de datos por su ID.
     * @param idEstudio El ID del estudio a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarEstudio(int idEstudio) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM estudio WHERE id_estudio = ?";
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEstudio);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Estudio con ID " + idEstudio + " eliminado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar estudio: " + e.getMessage());
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
     * Busca un estudio en la base de datos por su ID.
     * @param idEstudio El ID del estudio a buscar.
     * @return El objeto Estudio si se encuentra, o null si no existe.
     */
    public Estudio buscarPorId(int idEstudio) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Estudio estudio = null;
        String sql = "SELECT id_estudio, dni_pac, id_med, tipo, resultado, fecha_e FROM estudio WHERE id_estudio = ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idEstudio);
            rs = ps.executeQuery();

            if (rs.next()) {
                estudio = new Estudio(
                    rs.getInt("id_estudio"),
                    rs.getInt("dni_pac"),
                    rs.getInt("id_med"),
                    rs.getString("tipo"),
                    rs.getString("resultado"),
                    rs.getDate("fecha_e") // getDate() retorna java.sql.Date, compatible con java.util.Date
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudio por ID: " + e.getMessage());
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
        return estudio;
    }

    /**
     * Busca todos los estudios en la base de datos asociados a un DNI de paciente.
     * @param dniPaciente El DNI del paciente cuyos estudios se desean obtener.
     * @return Una lista de objetos Estudio, o una lista vacía si no hay estudios para ese paciente.
     */
    public List<Estudio> buscarEstudiosPorDniPaciente(int dniPaciente) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Estudio> estudios = new ArrayList<>();
        String sql = "SELECT id_estudio, dni_pac, id_med, tipo, resultado, fecha_e FROM estudio WHERE dni_pac = ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return estudios;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dniPaciente);
            rs = ps.executeQuery();

            while (rs.next()) {
                Estudio estudio = new Estudio(
                    rs.getInt("id_estudio"),
                    rs.getInt("dni_pac"),
                    rs.getInt("id_med"),
                    rs.getString("tipo"),
                    rs.getString("resultado"),
                    rs.getDate("fecha_e")
                );
                estudios.add(estudio);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar estudios por DNI de paciente: " + e.getMessage());
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
        return estudios;
    }

    /**
     * Obtiene todos los estudios de la base de datos (sin filtrar por paciente).
     * @return Una lista de todos los objetos Estudio.
     */
    public List<Estudio> obtenerTodosEstudios() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Estudio> estudios = new ArrayList<>();
        String sql = "SELECT id_estudio, dni_pac, id_med, tipo, resultado, fecha_e FROM estudio";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return estudios;
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Estudio estudio = new Estudio(
                    rs.getInt("id_estudio"),
                    rs.getInt("dni_pac"),
                    rs.getInt("id_med"),
                    rs.getString("tipo"),
                    rs.getString("resultado"),
                    rs.getDate("fecha_e")
                );
                estudios.add(estudio);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los estudios: " + e.getMessage());
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
        return estudios;
    }
}
