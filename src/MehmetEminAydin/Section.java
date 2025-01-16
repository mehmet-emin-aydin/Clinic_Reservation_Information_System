package MehmetEminAydin;

import java.io.Serializable;
import java.util.LinkedList;

public class Section implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String name;
    private LinkedList<Doctor> doctors;
    
    public Section(int id, String name) {
        this.id = id;
        this.name = name;
        this.doctors = new LinkedList<>();
    }
    
    public LinkedList<Doctor> listDoctors() {
        return doctors;
    }
    
    public Doctor getDoctor(int diplomaId) throws IDException {
        for (Doctor doctor : doctors) {
            if (doctor.getDiplomaId() == diplomaId) {
                return doctor;
            }
        }
        throw new IDException("Doktor bulunamadı: " + diplomaId);
    }
    
    public void addDoctor(Doctor doctor) throws DuplicateInfoException {
        for (Doctor existingDoctor : doctors) {
            if (existingDoctor.getDiplomaId() == doctor.getDiplomaId()) {
                throw new DuplicateInfoException("Bu diploma numarasına sahip doktor zaten mevcut: " + doctor.getDiplomaId());
            }
        }
        doctors.add(doctor);
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    public void removeDoctor(int diplomaId) {
        doctors.removeIf(d -> d.getDiplomaId() == diplomaId);
    }
} 