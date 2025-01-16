package MehmetEminAydin;

import org.junit.Test;
import static org.junit.Assert.*;

public class PersonTest {
    @Test
    public void testPersonConstructor() {
        Person person = new Person("Test Person", 12345678901L);
        assertEquals("Test Person", person.getName());
        assertEquals(12345678901L, person.getNationalId());
    }
    
    @Test
    public void testPersonToString() {
        Person person = new Person("Test Person", 12345678901L);
        assertEquals("İsim: Test Person, TC: 12345678901", person.toString());
    }
} 