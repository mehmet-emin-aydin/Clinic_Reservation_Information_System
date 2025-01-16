package MehmetEminAydin;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Rendezvous implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date dateTime;
    private Patient patient;
    private Doctor doctor;
    
    public Rendezvous(Patient patient, Doctor doctor, Date dateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
    }
    
    public Date getDateTime() {
        return dateTime;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    @Override
    public String toString() {
        return "Randevu: " + dateTime + " - Hasta: " + patient.getName() + " - Doktor: " + doctor.getName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Rendezvous other = (Rendezvous) obj;
        return Objects.equals(dateTime, other.dateTime) &&
               Objects.equals(patient, other.patient) &&
               Objects.equals(doctor, other.doctor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dateTime, patient, doctor);
    }
} 