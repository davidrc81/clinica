package modelo;


public class Medico {

    private int dniMedico;          
    private String apellido;          
    private String nombre;            
    private String domicilio;        
    private String localidad;        
    private String matricula;         
    private String especializacion;   

  
    public Medico() {
    }

   
    public Medico(int dniMedico, String apellido, String nombre, String domicilio,
                  String localidad, String matricula, String especializacion) {
        this.dniMedico = dniMedico;
        this.apellido = apellido;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.matricula = matricula;
        this.especializacion = especializacion;
    }

   

    public int getDniMedico() {
        return dniMedico;
    }

    public void setDniMedico(int dniMedico) {
        this.dniMedico = dniMedico;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void  setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

    /**
     * Sobreescribe el método toString() para proporcionar una representación
     * de cadena legible del objeto Medico.
     * @return Una cadena que representa el objeto Medico.
     */
    @Override
    public String toString() {
        return "Medico{" +
               "dniMedico=" + dniMedico +
               ", apellido='" + apellido + '\'' +
               ", nombre='" + nombre + '\'' +
               ", domicilio='" + domicilio + '\'' +
               ", localidad='" + localidad + '\'' +
               ", matricula='" + matricula + '\'' +
               ", especializacion='" + especializacion + '\'' +
               '}';
    }
}
