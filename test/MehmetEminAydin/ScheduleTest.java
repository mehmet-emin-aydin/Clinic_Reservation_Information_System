package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class ScheduleTest {
    @Test
    public void testScheduleConstructor() {
        Schedule schedule = new Schedule(5);
        assertEquals(5, schedule.getMaxPatientPerDay());
    }
    
    @Test
    public void testAddRendezvous() {
        Schedule schedule = new Schedule(5);
        Patient patient = new Patient("Test Patient", 12345678901L);
        Doctor doctor = new Doctor("Dr. Test", 23456789012L, 1001);
        Date date = new Date();
        
        boolean result = schedule.addRendezvous(patient, doctor, date);
        assertTrue("Should be able to add rendezvous", result);
    }
} 