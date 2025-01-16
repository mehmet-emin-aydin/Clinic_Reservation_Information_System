package MehmetEminAydin;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class CRS implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<Long, Patient> patients;
    private LinkedList<Rendezvous> rendezvous;
    private HashMap<Integer, Hospital> hospitals;
    
    public CRS() {
        patients = new HashMap<>();
        rendezvous = new LinkedList<>();
        hospitals = new HashMap<>();
    }
    
    public boolean makeRendezvous(long patientId, int hospitalId, int sectionId, int doctorId, Date date) 
            throws IDException {
        // Hasta kontrolü
        Patient patient = patients.get(patientId);
        if (patient == null) {
            throw new IDException("Hasta bulunamadı: " + patientId);
        }
        
        // Hastane kontrolü
        Hospital hospital = hospitals.get(hospitalId);
        if (hospital == null) {
            throw new IDException("Hastane bulunamadı: " + hospitalId);
        }
        
        // Bölüm kontrolü
        Section section = hospital.getSection(sectionId);
        
        // Doktor kontrolü
        Doctor doctor = section.getDoctor(doctorId);
        
        // Randevu ekleme
        boolean success = doctor.getSchedule().addRendezvous(patient, doctor, date);
        if (success) {
            Rendezvous r = new Rendezvous(patient, doctor, date);
            rendezvous.add(r);
        }
        return success;
    }
    
    public void saveTablesToDisk(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    
    public static CRS loadTablesFromDisk(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            CRS loadedCRS = (CRS) ois.readObject();
            
            // Yüklenen verileri temizle ve doğrula
            loadedCRS.cleanInvalidRendezvous();
            
            // Doktorları doğrula (bölümü olmayan doktorları sil)
            for (Hospital hospital : loadedCRS.hospitals.values()) {
                for (Section section : hospital.getSections()) {
                    section.listDoctors().removeIf(doctor -> {
                        // Diploma numarası çakışması kontrolü
                        int count = 0;
                        for (Hospital h : loadedCRS.hospitals.values()) {
                            for (Section s : h.getSections()) {
                                for (Doctor d : s.listDoctors()) {
                                    if (d.getDiplomaId() == doctor.getDiplomaId()) {
                                        count++;
                                    }
                                }
                            }
                        }
                        return count > 1; // Birden fazla aynı diploma no varsa sil
                    });
                }
            }
            
            // Hastaları doğrula (TC no çakışması kontrolü)
            loadedCRS.patients.values().removeIf(patient -> {
                int count = 0;
                for (Patient p : loadedCRS.patients.values()) {
                    if (p.getNationalId() == patient.getNationalId()) {
                        count++;
                    }
                }
                return count > 1;
            });
            
            return loadedCRS;
        }
    }
    
    // Yardımcı metodlar
    public void addPatient(Patient patient) {
        patients.put(patient.getNationalId(), patient);
    }
    
    public void addHospital(Hospital hospital) throws DuplicateInfoException {
        if (hospitals.containsKey(hospital.getId())) {
            throw new DuplicateInfoException("Bu ID'ye sahip hastane zaten mevcut: " + hospital.getId());
        }
        hospitals.put(hospital.getId(), hospital);
    }
    
    public Patient getPatient(long nationalId) throws IDException {
        Patient patient = patients.get(nationalId);
        if (patient == null) {
            throw new IDException("Hasta bulunamadı: " + nationalId);
        }
        return patient;
    }
    
    public Hospital getHospital(int hospitalId) throws IDException {
        Hospital hospital = hospitals.get(hospitalId);
        if (hospital == null) {
            throw new IDException("Hastane bulunamadı: " + hospitalId);
        }
        return hospital;
    }
    
    public LinkedList<Rendezvous> getRendezvous() {
        return rendezvous;
    }
    
    public HashMap<Long, Patient> getPatients() {
        return patients;
    }
    
    public HashMap<Integer, Hospital> getHospitals() {
        return hospitals;
    }
    
    public void checkDoctorDiplomaId(int diplomaId) throws DuplicateInfoException {
        // Tüm hastaneleri kontrol et
        for (Hospital hospital : hospitals.values()) {
            // Tüm bölümleri kontrol et
            for (Section section : hospital.getSections()) {
                // Tüm doktorları kontrol et
                for (Doctor doctor : section.listDoctors()) {
                    if (doctor.getDiplomaId() == diplomaId) {
                        throw new DuplicateInfoException("Bu diploma numarasına sahip doktor zaten sistemde kayıtlı: " + diplomaId);
                    }
                }
            }
        }
    }
    
    // Hastane silme
    public void removeHospital(int hospitalId) {
        Hospital hospital = hospitals.get(hospitalId);
        if (hospital != null) {
            // Hastanedeki tüm bölümlerdeki tüm doktorların randevularını sil
            for (Section section : hospital.getSections()) {
                for (Doctor doctor : section.listDoctors()) {
                    rendezvous.removeIf(r -> r.getDoctor().equals(doctor));
                }
            }
            hospitals.remove(hospitalId);
        }
    }
    
    // Bölüm silme
    public void removeSection(int hospitalId, int sectionId) throws IDException {
        Hospital hospital = getHospital(hospitalId);
        Section section = hospital.getSection(sectionId);
        
        // Bölümdeki tüm doktorların randevularını sil
        for (Doctor doctor : section.listDoctors()) {
            rendezvous.removeIf(r -> r.getDoctor().equals(doctor));
        }
        
        hospital.removeSection(sectionId);
    }
    
    // Doktor silme
    public void removeDoctor(int hospitalId, int sectionId, int diplomaId) throws IDException {
        Hospital hospital = getHospital(hospitalId);
        Section section = hospital.getSection(sectionId);
        Doctor doctor = section.getDoctor(diplomaId);
        
        // Doktorun tüm randevularını sil
        rendezvous.removeIf(r -> r.getDoctor().equals(doctor));
        
        section.removeDoctor(diplomaId);
    }
    
    // Hasta silme
    public void removePatient(long nationalId) {
        // Hastanın tüm randevularını sil
        rendezvous.removeIf(r -> r.getPatient().getNationalId() == nationalId);
        patients.remove(nationalId);
    }
    
    // Geçersiz randevuları temizle
    public void cleanInvalidRendezvous() {
        rendezvous.removeIf(r -> {
            // Hasta kontrolü
            if (!patients.containsKey(r.getPatient().getNationalId())) {
                return true;
            }
            
            // Doktor kontrolü - doktorun geçerli bir bölümde olup olmadığını kontrol et
            Doctor doctor = r.getDoctor();
            boolean doctorHasValidSection = false;
            
            for (Hospital hospital : hospitals.values()) {
                for (Section section : hospital.getSections()) {
                    if (section.listDoctors().contains(doctor)) {
                        doctorHasValidSection = true;
                        break;
                    }
                }
                if (doctorHasValidSection) break;
            }
            
            return !doctorHasValidSection;
        });
    }
} 