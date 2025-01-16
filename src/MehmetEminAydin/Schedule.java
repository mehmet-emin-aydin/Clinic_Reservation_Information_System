package MehmetEminAydin;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList<Rendezvous> sessions;
    private int maxPatientPerDay;
    
    public Schedule(int maxPatientPerDay) {
        this.maxPatientPerDay = maxPatientPerDay;
        this.sessions = new LinkedList<>();
    }
    
    public int getMaxPatientPerDay() {
        return maxPatientPerDay;
    }
    
    public boolean addRendezvous(Patient patient, Doctor doctor, Date date) {
        // Aynı gün için randevu sayısını kontrol et
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(date);
        
        int count = 0;
        for (Rendezvous r : sessions) {
            Calendar existingDate = Calendar.getInstance();
            existingDate.setTime(r.getDateTime());
            
            if (existingDate.get(Calendar.YEAR) == newDate.get(Calendar.YEAR) &&
                existingDate.get(Calendar.DAY_OF_YEAR) == newDate.get(Calendar.DAY_OF_YEAR)) {
                count++;
            }
        }
        
        if (count >= maxPatientPerDay) {
            return false;
        }
        
        sessions.add(new Rendezvous(patient, doctor, date));
        return true;
    }
    
    public LinkedList<Rendezvous> getSessions() {
        return sessions;
    }
} 