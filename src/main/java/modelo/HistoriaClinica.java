package modelo; 

import java.util.Date; 


public class HistoriaClinica {
    
    private int idHc;                
    private int idMedico;           
    private int idPaciente;          
    private String diagnostico;      
    private Date fechaHc;           

   
    public HistoriaClinica() {
    }

 
    public HistoriaClinica(int idMedico, int idPaciente, String diagnostico, Date fechaHc) {
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
        this.diagnostico = diagnostico;
        this.fechaHc = fechaHc;
    }

   
    public HistoriaClinica(int idHc, int idMedico, int idPaciente, String diagnostico, Date fechaHc) {
        this.idHc = idHc;
        this.idMedico = idMedico;
        this.idPaciente = idPaciente;
        this.diagnostico = diagnostico;
        this.fechaHc = fechaHc;
    }

    

    public int getIdHc() {
        return idHc;
    }

    public void setIdHc(int idHc) {
        this.idHc = idHc;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Date getFechaHc() {
        return fechaHc;
    }

    public void setFechaHc(Date fechaHc) {
        this.fechaHc = fechaHc;
    }

   
    @Override
    public String toString() {
        return "HistoriaClinica{" +
               "idHc=" + idHc +
               ", idMedico=" + idMedico +
               ", idPaciente=" + idPaciente +
               ", diagnostico='" + diagnostico + '\'' +
               ", fechaHc=" + fechaHc +
               '}';
    }
}