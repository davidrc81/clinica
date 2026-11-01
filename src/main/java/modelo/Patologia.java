package modelo;


public class Patologia {
  
    private int idPatologia;      
    private int dniPaciente;      
    private String descripcion;   

   
    public Patologia() {
    }

    
    public Patologia(int dniPaciente, String descripcion) {
        this.dniPaciente = dniPaciente;
        this.descripcion = descripcion;
    }

 
    public Patologia(int idPatologia, int dniPaciente, String descripcion) {
        this.idPatologia = idPatologia;
        this.dniPaciente = dniPaciente;
        this.descripcion = descripcion;
    }

    // Getters y Setters para cada atributo 

    public int getIdPatologia() {
        return idPatologia;
    }

    public void setIdPatologia(int idPatologia) {
        this.idPatologia = idPatologia;
    }

    public int getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(int dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

  
    @Override
    public String toString() {
        return "Patologia{" +
               "idPatologia=" + idPatologia +
               ", dniPaciente=" + dniPaciente +
               ", descripcion='" + descripcion + '\'' +
               '}';
    }
}
