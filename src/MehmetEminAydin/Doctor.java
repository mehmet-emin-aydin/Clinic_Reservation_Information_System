package MehmetEminAydin;

import java.io.Serializable;

public class Doctor extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int diploma_id;
    private Schedule schedule;
    
    public Doctor(String name, long national_id, int diploma_id) {
        super(name, national_id);
        this.diploma_id = diploma_id;
        this.schedule = new Schedule(10); // Varsayılan olarak günlük 10 hasta
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public int getDiplomaId() {
        return diploma_id;
    }
    
    @Override
    public String toString() {
        return "Dr. " + getName() + " (Diploma No: " + getDiplomaId() + ")";
    }
} 