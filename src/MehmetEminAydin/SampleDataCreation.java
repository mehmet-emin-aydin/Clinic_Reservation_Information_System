package MehmetEminAydin;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;

public class SampleDataCreation {
    public static void main(String[] args) {
        try {
            CRS crs = new CRS();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            // 1. Hastaneler
            Hospital h1 = new Hospital(1, "Acıbadem Hastanesi");
            Hospital h2 = new Hospital(2, "Memorial Hastanesi");
            
            // 2. Bölümler
            Section s1_1 = new Section(11, "Kardiyoloji");
            Section s1_2 = new Section(12, "Nöroloji");
            Section s2_1 = new Section(21, "Ortopedi");
            Section s2_2 = new Section(22, "Göz Hastalıkları");
            
            h1.addSection(s1_1);
            h1.addSection(s1_2);
            h2.addSection(s2_1);
            h2.addSection(s2_2);
            
            // 3. Doktorlar
            Doctor d1 = new Doctor("Ahmet Yılmaz", 12345, 1001);
            Doctor d2 = new Doctor("Ayşe Demir", 23456, 1002);
            Doctor d3 = new Doctor("Mehmet Kaya", 34567, 1003);
            Doctor d4 = new Doctor("Fatma Şahin", 45678, 1004);
            Doctor d5 = new Doctor("Ali Öztürk", 56789, 1005);
            
            s1_1.addDoctor(d1);  // Kardiyoloji
            s1_1.addDoctor(d2);
            s1_2.addDoctor(d3);  // Nöroloji
            s2_1.addDoctor(d4);  // Ortopedi
            s2_2.addDoctor(d5);  // Göz Hastalıkları
            
            // 4. Hastalar
            Patient p1 = new Patient("Veli Yıldız", 11111111111L);
            Patient p2 = new Patient("Zeynep Çelik", 22222222222L);
            Patient p3 = new Patient("Mustafa Aydın", 33333333333L);
            
            // 5. Hastaneleri CRS'e ekle
            crs.addHospital(h1);
            crs.addHospital(h2);
            
            // 6. Hastaları CRS'e ekle
            crs.addPatient(p1);
            crs.addPatient(p2);
            crs.addPatient(p3);
            
            // 7. Randevular
            // Veli Yıldız'ın randevuları
            crs.makeRendezvous(11111111111L, 1, 11, 1001, sdf.parse("25/12/2024 10:00")); // Kardiyoloji - Ahmet Yılmaz
            crs.makeRendezvous(11111111111L, 2, 21, 1004, sdf.parse("26/12/2024 14:30")); // Ortopedi - Fatma Şahin
            
            // Zeynep Çelik'in randevuları
            crs.makeRendezvous(22222222222L, 1, 12, 1003, sdf.parse("27/12/2024 11:15")); // Nöroloji - Mehmet Kaya
            
            // Mustafa Aydın'ın randevuları
            crs.makeRendezvous(33333333333L, 2, 22, 1005, sdf.parse("28/12/2024 09:45")); // Göz - Ali Öztürk
            crs.makeRendezvous(33333333333L, 1, 11, 1002, sdf.parse("29/12/2024 15:00")); // Kardiyoloji - Ayşe Demir
            
            // Dosyaya kaydet
            crs.saveTablesToDisk("data.ser");
            System.out.println("Örnek veriler başarıyla oluşturuldu ve kaydedildi.");
            
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 