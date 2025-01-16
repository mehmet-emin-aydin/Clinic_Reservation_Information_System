package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;

public class DoctorTest {
    @Test
    public void testDoctorSchedule() {
        Doctor doctor = new Doctor("Dr. Test", 12345678901L, 1001);
        assertNotNull("Schedule should not be null", doctor.getSchedule());
    }
    
    @Test
    public void testDoctorDiplomaId() {
        Doctor doctor = new Doctor("Dr. Test", 12345678901L, 1001);
        assertEquals(1001, doctor.getDiplomaId());
    }
} 