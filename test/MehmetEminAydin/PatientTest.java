package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;

public class PatientTest {
    @Test
    public void testPatientInheritance() {
        Patient patient = new Patient("Test Patient", 12345678901L);
        assertTrue(patient instanceof Person);
    }
    
    @Test
    public void testPatientToString() {
        Patient patient = new Patient("Test Patient", 12345678901L);
        assertEquals("[Hasta İsim: Test Patient, TC: 12345678901]", patient.toString());
    }
} 