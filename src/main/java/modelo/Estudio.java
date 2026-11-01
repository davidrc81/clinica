package modelo; 

import java.util.Date; 


public class Estudio {
    private int idEstudio;          
    private int dniPaciente;       
    private int idMedico;           
    private String tipo;            
    private String resultado;       
    private Date fechaEstudio;      

   
    public Estudio() {
    }

 
    public Estudio(int dniPaciente, int idMedico, String tipo, String resultado, Date fechaEstudio) {
        this.dniPaciente = dniPaciente;
        this.idMedico = idMedico;
        this.tipo = tipo;
        this.resultado = resultado;
        this.fechaEstudio = fechaEstudio;
    }

 
    public Estudio(int idEstudio, int dniPaciente, int idMedico, String tipo, String resultado, Date fechaEstudio) {
        this.idEstudio = idEstudio;
        this.dniPaciente = dniPaciente;
        this.idMedico = idMedico;
        this.tipo = tipo;
        this.resultado = resultado;
        this.fechaEstudio = fechaEstudio;
    }

   

    public int getIdEstudio() {
        return idEstudio;
    }

    public void setIdEstudio(int idEstudio) {
        this.idEstudio = idEstudio;
    }

    public int getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(int dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Date getFechaEstudio() {
        return fechaEstudio;
    }

    public void setFechaEstudio(Date fechaEstudio) {
        this.fechaEstudio = fechaEstudio;
    }

 
    @Override
    public String toString() {
        return "Estudio{" +
               "idEstudio=" + idEstudio +
               ", dniPaciente=" + dniPaciente +
               ", idMedico=" + idMedico +
               ", tipo='" + tipo + '\'' +
               ", resultado='" + resultado + '\'' +
               ", fechaEstudio=" + fechaEstudio +
               '}';
    }
}

