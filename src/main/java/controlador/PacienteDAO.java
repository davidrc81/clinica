package controlador; // Asegúrate de que este sea el paquete correcto para tu DAO

import modelo.Paciente; // Importa la clase de modelo Paciente
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date; // Necesario para convertir java.util.Date a java.sql.Date

/**
 * Clase Data Access Object (DAO) para la entidad Paciente.
 * Se encarga de la comunicación directa con la base de datos para
 * realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) sobre la tabla 'paciente'.
 */
public class PacienteDAO {

    /**
     * Constructor vacío. Los métodos de esta clase obtendrán la conexión
     * a la base de datos a través de la clase estática 'Conexion'.
     * No se establece una conexión directa aquí para seguir el patrón de Conexion.
     */
    public PacienteDAO() {
        // No se establece la conexión en el constructor; cada método la obtiene de 'Conexion'.
        // Esto evita tener que propagar SQLException en el constructor si la conexión falla.
    }

    /**
     * Inserta un nuevo paciente en la base de datos.
     * @param paciente El objeto Paciente a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregarPaciente(Paciente paciente) { // Cambiado de 'p' a 'paciente' para claridad
        Connection conn = null;
        PreparedStatement ps = null; // Renombrado de 'stmt' a 'ps' (PreparedStatement)
        String sql = "INSERT INTO paciente (dni, ape, nom, sexo, fnac, dom, loc, peso, altura, grupo, factor) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Corregido nombre de tabla a 'paciente'

        boolean exito = false;

        try {
            conn = Conexion.getConexion(); // Obtener la conexión de la clase Conexion
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement según el objeto Paciente
            ps.setInt(1, paciente.getDni()); // DNI es int
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getNombre());
            ps.setString(4, paciente.getSexo());
            // Convertir java.util.Date a java.sql.Date
            ps.setDate(5, new Date(paciente.getFechaNacimiento().getTime())); 
            ps.setString(6, paciente.getDomicilio());
            ps.setString(7, paciente.getLocalidad());
            ps.setFloat(8, paciente.getPeso());   // Peso es float
            ps.setFloat(9, paciente.getAltura()); // Altura es float
            ps.setString(10, paciente.getGrupoSanguineo()); // Nombre de getter corregido
            ps.setString(11, paciente.getFactorRh());     // Nombre de getter corregido

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Paciente con DNI " + paciente.getDni() + " agregado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al agregar paciente: " + e.getMessage());
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
     * Modifica un paciente existente en la base de datos.
     * @param paciente El objeto Paciente con los datos actualizados.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarPaciente(Paciente paciente) { // Cambiado de 'p' a 'paciente'
        Connection conn = null;
        PreparedStatement ps = null;
        // Corregidos los nombres de las columnas en la sentencia SQL a snake_case
        String sql = "UPDATE paciente SET ape=?, nom=?, sexo=?, fnac=?, dom=?, loc=?, peso=?, altura=?, grupo=?, factor=? " +
                     "WHERE dni=?"; // Corregido nombre de tabla a 'paciente'
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros del PreparedStatement
            ps.setString(1, paciente.getApellido());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getSexo());
            ps.setDate(4, new Date(paciente.getFechaNacimiento().getTime())); // Fecha es Date
            ps.setString(5, paciente.getDomicilio());
            ps.setString(6, paciente.getLocalidad());
            ps.setFloat(7, paciente.getPeso());   // Peso es float
            ps.setFloat(8, paciente.getAltura()); // Altura es float
            ps.setString(9, paciente.getGrupoSanguineo()); // Nombre de getter corregido
            ps.setString(10, paciente.getFactorRh());     // Nombre de getter corregido
            ps.setInt(11, paciente.getDni()); // DNI es int y en la cláusula WHERE

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Paciente con DNI " + paciente.getDni() + " modificado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar paciente: " + e.getMessage());
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
     * Elimina un paciente de la base de datos por su DNI.
     * @param dni El DNI del paciente a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPaciente(int dni) { // DNI es int
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM paciente WHERE dni=?"; // Corregido nombre de tabla
        boolean exito = false;

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dni); // DNI es int

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
                System.out.println("Paciente con DNI " + dni + " eliminado exitosamente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
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
     * Busca un paciente en la base de datos por su DNI.
     * @param dni El DNI del paciente a buscar.
     * @return El objeto Paciente si se encuentra, o null si no existe.
     */
    public Paciente buscarPorDni(int dni) { // DNI es int
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Paciente paciente = null;
        // Seleccionados los nombres de columna correctos de la BD
        String sql = "SELECT dni, ape, nom, sexo, fnac, dom, loc, peso, altura, grupo, factor FROM paciente WHERE dni=?"; 

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dni); // DNI es int
            rs = ps.executeQuery();

            if (rs.next()) {
                paciente = new Paciente(); // Usar constructor vacío y setters
                paciente.setDni(rs.getInt("dni"));
                paciente.setApellido(rs.getString("ape"));
                paciente.setNombre(rs.getString("nom"));
                paciente.setSexo(rs.getString("sexo"));
                paciente.setFechaNacimiento(rs.getDate("fnac")); // getDate() para Date
                paciente.setDomicilio(rs.getString("dom"));
                paciente.setLocalidad(rs.getString("loc"));
                paciente.setPeso(rs.getFloat("peso"));     // getFloat() para float
                paciente.setAltura(rs.getFloat("altura")); // getFloat() para float
                paciente.setGrupoSanguineo(rs.getString("grupo")); // Nombre de setter corregido
                paciente.setFactorRh(rs.getString("factor"));     // Nombre de setter corregido
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar paciente por DNI: " + e.getMessage());
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
        return paciente;
    }

    /**
     * Obtiene todos los pacientes de la base de datos.
     * @return Una lista de objetos Paciente, o una lista vacía si no hay pacientes.
     */
    public List<Paciente> obtenerTodosPacientes() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT dni, ape, nom, sexo, fnac, dom, loc, peso, altura, grupo, factor FROM paciente";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return pacientes; // Devuelve lista vacía en caso de error
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setDni(rs.getInt("dni"));
                paciente.setApellido(rs.getString("ape"));
                paciente.setNombre(rs.getString("nom"));
                paciente.setSexo(rs.getString("sexo"));
                paciente.setFechaNacimiento(rs.getDate("fnac"));
                paciente.setDomicilio(rs.getString("dom"));
                paciente.setLocalidad(rs.getString("loc"));
                paciente.setPeso(rs.getFloat("peso"));
                paciente.setAltura(rs.getFloat("altura"));
                paciente.setGrupoSanguineo(rs.getString("grupo"));
                paciente.setFactorRh(rs.getString("factor"));
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pacientes: " + e.getMessage());
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
        return pacientes;
    }

    /**
     * Busca pacientes que coincidan con un criterio de búsqueda (DNI o parte del nombre/apellido).
     * @param criterio El DNI (exacto) o una parte del nombre/apellido.
     * @return Una lista de objetos Paciente que coinciden, o una lista vacía.
     */
    public List<Paciente> buscarPacientes(String criterio) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT dni, ape, nom, sexo, fnac, dom, loc, peso, altura, grupo, factor FROM paciente " +
                     "WHERE dni = ? OR ape LIKE ? OR nom LIKE ?";

        try {
            conn = Conexion.getConexion();
            if (conn == null) {
                System.err.println("Error: No se pudo obtener conexión a la base de datos.");
                return pacientes; // Devuelve lista vacía en caso de error
            }
            ps = conn.prepareStatement(sql);
            
            // Intentar convertir el criterio a int para la búsqueda por DNI
            try {
                int dniCriterio = Integer.parseInt(criterio);
                ps.setInt(1, dniCriterio); // Para dni = ?
            } catch (NumberFormatException e) {
                // Si no es un número, se pone un valor que no coincidirá con ningún DNI
                ps.setInt(1, -1); // Un DNI inválido para que la condición OR funcione
            }
            
            // Para búsqueda por LIKE
            ps.setString(2, "%" + criterio + "%"); // Para ape LIKE ?
            ps.setString(3, "%" + criterio + "%"); // Para nom LIKE ?

            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setDni(rs.getInt("dni"));
                paciente.setApellido(rs.getString("ape"));
                paciente.setNombre(rs.getString("nom"));
                paciente.setSexo(rs.getString("sexo"));
                paciente.setFechaNacimiento(rs.getDate("fnac"));
                paciente.setDomicilio(rs.getString("dom"));
                paciente.setLocalidad(rs.getString("loc"));
                paciente.setPeso(rs.getFloat("peso"));
                paciente.setAltura(rs.getFloat("altura"));
                paciente.setGrupoSanguineo(rs.getString("grupo"));
                paciente.setFactorRh(rs.getString("factor"));
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pacientes por criterio: " + e.getMessage());
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
        return pacientes;
    }
}
