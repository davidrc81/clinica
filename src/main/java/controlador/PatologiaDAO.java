package controlador; // El paquete correcto para la capa de control/DAO

import modelo.Patologia; // Importa la clase de modelo Patologia
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Data Access Object (DAO) para la entidad Patologia.
 * Se encarga de la comunicación directa con la base de datos para
 * realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre la tabla 'patologia'.
 */
public class PatologiaDAO {

    /**
     * Constructor vacío. Los métodos de esta clase obtendrán la conexión
     * a la base de datos a través de la clase estática 'Conexion'.
     * No se establece una conexión directa aquí.
     */
    public PatologiaDAO() {
        // No se establece la conexión en el constructor; cada método la obtiene de 'Conexion'.
    }

    /**
     * Inserta una nueva patología en la base de datos.
     * El 'id_pato' es auto-incremental y será generado por la base de datos,
     * por lo que no se incluye en el INSERT.
     * @param patologia El objeto Patologia a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregarPatologia(Patologia patologia) { // Cambiado de 'p' a 'patologia' para claridad
        Connection conn = null;
        PreparedStatement ps = null; // Renombrado de 'stmt' a 'ps'
        String sql = "INSERT INTO patologia (dni_paciente, patologia) VALUES (?, ?)"; // Corregido nombre de tabla

        boolean exito = false;

        try {
            conn = Conexion.getConexion(); // Obtener la conexión de la clase Conexion
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement según el objeto Patologia
            ps.setInt(1, patologia.getDniPaciente()); // dniPaciente es int en modelo.Patologia
            ps.setString(2, patologia.getDescripcion()); // 'patologia' en BD es 'descripcion' en modelo.Patologia

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Patología para DNI " + patologia.getDniPaciente() + " agregada exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar patología: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Asegurar que el PreparedStatement se cierra siempre
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
            // La conexión 'conn' es gestionada por el Singleton 'Conexion', no se cierra aquí.
        }
        return exito;
    }

    /**
     * Modifica una patología existente en la base de datos por su ID.
     * @param patologia El objeto Patologia con los datos actualizados.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarPatologia(Patologia patologia) { // Cambiado de 'p' a 'patologia'
        Connection conn = null;
        PreparedStatement ps = null;
        // Corregidos los nombres de la tabla y las columnas en la sentencia SQL
        String sql = "UPDATE patologia SET dni_paciente = ?, patologia = ? WHERE id_pato = ?"; 
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement
            ps.setInt(1, patologia.getDniPaciente());
            ps.setString(2, patologia.getDescripcion()); // 'patologia' en BD es 'descripcion' en modelo.Patologia
            ps.setInt(3, patologia.getIdPatologia()); // ID de la patología a modificar

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Patología con ID " + patologia.getIdPatologia() + " modificada exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar patología: " + e.getMessage());
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
     * Elimina una patología de la base de datos por su ID.
     * @param idPatologia El ID de la patología a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPatologia(int idPatologia) { // Cambiado de 'id' a 'idPatologia' para claridad
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM patologia WHERE id_pato = ?"; // Corregido nombre de tabla y campo
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPatologia);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Patología con ID " + idPatologia + " eliminada exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar patología: " + e.getMessage());
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
     * Busca todas las patologías asociadas a un paciente según su DNI.
     * @param dniPaciente El DNI del paciente.
     * @return Una lista de objetos Patologia, o una lista vacía si no hay patologías.
     */
    public List<Patologia> buscarPorDniPaciente(int dniPaciente) { // Cambiado de String a int
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Patologia> lista = new ArrayList<>();
        // Corregido nombre de tabla y campos seleccionados
        String sql = "SELECT id_pato, dni_paciente, patologia FROM patologia WHERE dni_paciente = ?"; 

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return lista; // Devuelve lista vacía en caso de error
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dniPaciente); // dni_paciente es int
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Patologia patologia = new Patologia( // Instanciar tu clase modelo 'Patologia'
                    rs.getInt("id_pato"),         // Mapear 'id_pato' a idPatologia
                    rs.getInt("dni_paciente"),    // Mapear 'dni_paciente' a dniPaciente
                    rs.getString("patologia")     // Mapear 'patologia' a descripcion
                );
                lista.add(patologia);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar patologías por DNI de paciente: " + e.getMessage());
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
        return lista;
    }

    /**
     * Busca una patología por su ID. (Método añadido para completar CRUD)
     * @param idPatologia El ID de la patología a buscar.
     * @return El objeto Patologia si se encuentra, o null si no existe.
     */
    public Patologia buscarPorId(int idPatologia) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Patologia patologia = null;
        String sql = "SELECT id_pato, dni_paciente, patologia FROM patologia WHERE id_pato = ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPatologia);
            rs = ps.executeQuery();

            if (rs.next()) {
                patologia = new Patologia(
                    rs.getInt("id_pato"),
                    rs.getInt("dni_paciente"),
                    rs.getString("patologia")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar patología por ID: " + e.getMessage());
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
        return patologia;
    }

    // Este método ya no es necesario aquí porque la clase Conexion gestiona la única conexión.
    // public void cerrarConexion() throws SQLException {
    //     if (conexion != null && !conexion.isClosed()) {
    //         conexion.close();
    //     }
    // }
}