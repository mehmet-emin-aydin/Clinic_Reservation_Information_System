package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;

public class HospitalTest {
    @Test
    public void testHospitalSectionList() {
        Hospital hospital = new Hospital(1, "Test Hospital");
        Section section = new Section(1, "Test Section");
        hospital.addSection(section);
        assertEquals(1, hospital.getSections().size());
    }
    
    @Test(expected = IDException.class)
    public void testGetNonexistentSection() throws IDException {
        Hospital hospital = new Hospital(1, "Test Hospital");
        hospital.getSection(9999);
    }
} 