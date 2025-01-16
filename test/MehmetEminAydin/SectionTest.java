package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;

public class SectionTest {
    @Test
    public void testSectionDoctorList() throws DuplicateInfoException {
        Section section = new Section(1, "Test Section");
        Doctor doctor = new Doctor("Dr. Test", 12345678901L, 1001);
        section.addDoctor(doctor);
        assertEquals(1, section.listDoctors().size());
    }
    
    @Test(expected = IDException.class)
    public void testGetNonexistentDoctor() throws IDException {
        Section section = new Section(1, "Test Section");
        section.getDoctor(9999);
    }
} 