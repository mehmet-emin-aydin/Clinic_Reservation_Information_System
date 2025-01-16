package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class CRSTest {
    @Test
    public void testAddHospital() throws DuplicateInfoException {
        CRS crs = new CRS();
        Hospital hospital = new Hospital(1, "Test Hospital");
        crs.addHospital(hospital);
        try {
            assertEquals(hospital, crs.getHospital(1));
        } catch (IDException e) {
            fail("IDException was not expected");
        }
    }
    
    @Test
    public void testRemoveHospital() throws DuplicateInfoException {
        CRS crs = new CRS();
        Hospital hospital = new Hospital(1, "Test Hospital");
        crs.addHospital(hospital);
        crs.removeHospital(1);
        assertEquals(0, crs.getHospitals().size());
    }
    
    @Test
    public void testCascadingDelete() throws Exception {
        CRS crs = new CRS();
        
        // Hastane oluştur
        Hospital hospital = new Hospital(1, "Test Hospital");
        Section section = new Section(1, "Test Section");
        Doctor doctor = new Doctor("Dr. Test", 12345678901L, 1001);
        Patient patient = new Patient("Test Patient", 23456789012L);
        
        // İlişkileri kur
        section.addDoctor(doctor);
        hospital.addSection(section);
        crs.addHospital(hospital);
        crs.addPatient(patient);
        
        // Randevu oluştur
        Date date = new Date();
        crs.makeRendezvous(23456789012L, 1, 1, 1001, date);
        
        // Hastaneyi sil
        crs.removeHospital(1);
        
        // Kontroller
        assertEquals(0, crs.getHospitals().size());
        assertEquals(0, crs.getRendezvous().size());
    }
} 