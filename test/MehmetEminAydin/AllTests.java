package MehmetEminAydin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    PersonTest.class,
    PatientTest.class,
    DoctorTest.class,
    SectionTest.class,
    HospitalTest.class,
    ScheduleTest.class,
    RendezvousTest.class,
    CRSTest.class
})
public class AllTests {
    // Test suite olduğu için içerik boş kalabilir
} 