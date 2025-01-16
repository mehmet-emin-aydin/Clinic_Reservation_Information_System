package MehmetEminAydin;

import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected final long national_id;
    
    public Person(String name, long national_id) {
        this.name = name;
        this.national_id = national_id;
    }
    
    @Override
    public String toString() {
        return "İsim: " + name + ", TC: " + national_id;
    }
    
    public long getNationalId() {
        return national_id;
    }
    
    public String getName() {
        return name;
    }
} 