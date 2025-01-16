package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class RendezvousTest {
    @Test
    public void testRendezvousConstructor() {
        Patient patient = new Patient("Test Patient", 12345678901L);
        Doctor doctor = new Doctor("Dr. Test", 23456789012L, 1001);
        Date date = new Date();
        
        Rendezvous rendezvous = new Rendezvous(patient, doctor, date);
        assertEquals(patient, rendezvous.getPatient());
        assertEquals(doctor, rendezvous.getDoctor());
        assertEquals(date, rendezvous.getDateTime());
    }
    
    @Test
    public void testRendezvousEquality() {
        Patient patient = new Patient("Test Patient", 12345678901L);
        Doctor doctor = new Doctor("Dr. Test", 23456789012L, 1001);
        Date date = new Date();
        
        Rendezvous r1 = new Rendezvous(patient, doctor, date);
        Rendezvous r2 = new Rendezvous(patient, doctor, date);
        
        assertEquals(r1.getDateTime(), r2.getDateTime());
        assertEquals(r1.getPatient(), r2.getPatient());
        assertEquals(r1.getDoctor(), r2.getDoctor());
    }
} 