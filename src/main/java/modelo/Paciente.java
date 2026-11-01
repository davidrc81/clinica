package modelo; 

import java.util.Date;


public class Paciente {
    
    private int dni;               
    private String apellido;       
    private String nombre;     
    private String sexo;           
    private Date fechaNacimiento;  
    private String domicilio;      
    private String localidad;       
    private float peso;           
    private float altura;          
    private String grupoSanguineo; 
    private String factorRh;       

   
    public Paciente() {
    }

 
    public Paciente(int dni, String apellido, String nombre, String sexo, Date fechaNacimiento,
                    String domicilio, String localidad, float peso, float altura,
                    String grupoSanguineo, String factorRh) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.peso = peso;
        this.altura = altura;
        this.grupoSanguineo = grupoSanguineo;
        this.factorRh = factorRh;
    }

    //  Getters y Setters 

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getFactorRh() {
        return factorRh;
    }

    public void setFactorRh(String factorRh) {
        this.factorRh = factorRh;
    }


    @Override
    public String toString() {
        return "Paciente{" +
               "dni=" + dni +
               ", apellido='" + apellido + '\'' +
               ", nombre='" + nombre + '\'' +
               ", sexo='" + sexo + '\'' +
               ", fechaNacimiento=" + fechaNacimiento +
               ", domicilio='" + domicilio + '\'' +
               ", localidad='" + localidad + '\'' +
               ", peso=" + peso +
               ", altura=" + altura +
               ", grupoSanguineo='" + grupoSanguineo + '\'' +
               ", factorRh='" + factorRh + '\'' +
               '}';
    }
}
