package MehmetEminAydin;

public class Patient extends Person {
    private static final long serialVersionUID = 1L;
    public Patient(String name, long national_id) {
        super(name, national_id);
    }
    
    @Override
    public String toString() {
        return "[Hasta İsim: " + getName() + ", TC: " + getNationalId() + "]";
    }
} 