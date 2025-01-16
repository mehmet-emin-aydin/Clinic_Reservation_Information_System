package MehmetEminAydin.console;

import MehmetEminAydin.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.text.ParseException;

public class ConsoleUI {
    private CRS crs;
    private final Scanner scanner;
    private final SimpleDateFormat dateFormat;
    
    private static final String MENU_HEADER = "\n=== %s ===";
    private static final String INPUT_PROMPT = "Seçiminiz: ";
    private static final String BACK_OPTION = "0. Geri";
    private static final String INVALID_CHOICE = "Geçersiz seçim!";
    
    public ConsoleUI() {
        // Başlangıçta data.ser'den yüklemeyi dene
        try {
            this.crs = CRS.loadTablesFromDisk("data.ser");
            System.out.println("Mevcut veriler başarıyla yüklendi.");
        } catch (Exception e) {
            // Dosya yoksa veya okuma hatası varsa yeni CRS oluştur
        this.crs = new CRS();
            System.out.println("Yeni sistem başlatıldı.");
        }
        
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }
    
    public void start() {
        while (true) {
            displayMainMenu();
            int choice = readIntegerInput();
            if (!processMainMenuChoice(choice)) break;
        }
    }
    
    private void displayMainMenu() {
        System.out.printf(MENU_HEADER, "Klinik Rezervasyon Sistemi");
        String[] options = {
            "Hasta İşlemleri",
            "Hastane İşlemleri",
            "Randevu İşlemleri",
            "Randevu Listele",
            "Verileri Kaydet",
            "Verileri Yükle",
            "Çıkış"
        };
        displayMenuOptions(options);
    }
    
    private boolean processMainMenuChoice(int choice) {
        return switch (choice) {
            case 1 -> { hastaMenu(); yield true; }
            case 2 -> { hastaneMenu(); yield true; }
            case 3 -> { randevuMenu(); yield true; }
            case 4 -> { randevuListele(); yield true; }
            case 5 -> { verileriKaydet(); yield true; }
            case 6 -> { verileriYukle(); yield true; }
            case 7 -> false;
            default -> { System.out.println(INVALID_CHOICE); yield true; }
        };
    }
    
    private void displayMenuOptions(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.print(INPUT_PROMPT);
    }
    
    private int readIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Lütfen geçerli bir sayı girin: ");
            }
        }
    }
    
    private long readLongInput() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Lütfen geçerli bir sayı girin: ");
            }
        }
    }
    
    private void hastaMenu() {
        while (true) {
            System.out.printf(MENU_HEADER, "Hasta İşlemleri");
            String[] options = {
                "Hasta Ekle",
                "Hasta Bilgisi Görüntüle",
                "Tüm Hastaları Listele",
                "Hasta Sil"
            };
            displayMenuOptions(options);
            System.out.println(BACK_OPTION);
            
            int choice = readIntegerInput();
            if (choice == 0) break;
            
            switch (choice) {
                case 1 -> hastaEkle();
                case 2 -> hastaBilgisiGoruntule();
                case 3 -> hastalariListele();
                case 4 -> hastaSil();
                default -> System.out.println(INVALID_CHOICE);
            }
        }
    }
    
    private void hastaEkle() {
        System.out.print("Hasta Adı Soyadı: ");
        String name = scanner.nextLine();
        
        System.out.print("TC Kimlik No: ");
        long tcNo = readLongInput();
        
        try {
            Patient patient = new Patient(name, tcNo);
            crs.addPatient(patient);
            System.out.println("Hasta başarıyla eklendi.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void hastaBilgisiGoruntule() {
        System.out.print("TC Kimlik No: ");
        long tcNo = readLongInput();
        
        try {
            Patient patient = crs.getPatient(tcNo);
            System.out.println("\nHasta Bilgileri:");
            System.out.println("Ad Soyad: " + patient.getName());
            System.out.println("TC No: " + patient.getNationalId());
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void hastalariListele() {
        System.out.println("\nKayıtlı Hastalar:");
        for (Patient patient : crs.getPatients().values()) {
            System.out.println(patient);
        }
    }
    
    private void hastaSil() {
        System.out.print("Silinecek Hasta TC No: ");
        long tcNo = readLongInput();
        
        try {
            // Önce hastanın var olduğunu kontrol et
            Patient hasta = crs.getPatient(tcNo);
            crs.removePatient(tcNo);
            System.out.println("Hasta ve randevuları başarıyla silindi.");
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void hastaneMenu() {
        while (true) {
            System.out.printf(MENU_HEADER, "Hastane İşlemleri");
            String[] options = {
                "Hastane Ekle",
                "Bölüm Ekle",
                "Doktor Ekle",
                "Hastane Bilgisi Görüntüle",
                "Hastane Sil",
                "Bölüm Sil",
                "Doktor Sil"
            };
            displayMenuOptions(options);
            System.out.println(BACK_OPTION);
            
            int choice = readIntegerInput();
            if (choice == 0) break;
            
            switch (choice) {
                case 1 -> hastaneEkle();
                case 2 -> bolumEkle();
                case 3 -> doktorEkle();
                case 4 -> hastaneBilgisiGoruntule();
                case 5 -> hastaneSil();
                case 6 -> bolumSil();
                case 7 -> doktorSil();
                default -> System.out.println(INVALID_CHOICE);
            }
        }
    }
    
    private void hastaneEkle() {
        System.out.print("Hastane ID: ");
        int id = readIntegerInput();
        
        System.out.print("Hastane Adı: ");
        String ad = scanner.nextLine();
        
        try {
            Hospital hastane = new Hospital(id, ad);
            crs.addHospital(hastane);
            System.out.println("Hastane başarıyla eklendi.");
        } catch (DuplicateInfoException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void bolumEkle() {
        System.out.print("Hastane ID: ");
        int hastaneId = readIntegerInput();
        
        try {
            Hospital hastane = crs.getHospital(hastaneId);
            
            System.out.print("Bölüm ID: ");
            int bolumId = readIntegerInput();
            
            System.out.print("Bölüm Adı: ");
            String bolumAdi = scanner.nextLine();
            
            for (Section existingSection : hastane.getSections()) {
                if (existingSection.getId() == bolumId) {
                    System.out.println("Hata: Bu ID'ye sahip bölüm zaten mevcut: " + bolumId);
                    return;
                }
            }
            
            Section section = new Section(bolumId, bolumAdi);
            hastane.addSection(section);
            System.out.println("Bölüm başarıyla eklendi.");
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void doktorEkle() {
        System.out.print("Hastane ID: ");
        int hastaneId = readIntegerInput();
        
        try {
            Hospital hastane = crs.getHospital(hastaneId);
            
            System.out.print("Bölüm ID: ");
            int bolumId = readIntegerInput();
            
            Section section = hastane.getSection(bolumId);
            
            System.out.print("Doktor Adı Soyadı: ");
            String name = scanner.nextLine();
            
            System.out.print("TC Kimlik No: ");
            long tcNo = readLongInput();
            
            System.out.print("Diploma No: ");
            int diplomaId = readIntegerInput();
            
            try {
                crs.checkDoctorDiplomaId(diplomaId);
                
                Doctor doctor = new Doctor(name, tcNo, diplomaId);
                section.addDoctor(doctor);
                System.out.println("Doktor başarıyla eklendi.");
            } catch (DuplicateInfoException e) {
                System.out.println("Hata: " + e.getMessage());
            }
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void hastaneBilgisiGoruntule() {
        System.out.print("Hastane ID: ");
        int hastaneId = readIntegerInput();
        
        try {
            Hospital hospital = crs.getHospital(hastaneId);
            System.out.println("\nHastane Bilgileri:");
            System.out.println("Ad: " + hospital.getName());
            System.out.println("\nBölümler:");
            for (Section section : hospital.getSections()) {
                System.out.println("\n- " + section.getName() + " (ID: " + section.getId() + ")");
                System.out.println("  Doktorlar:");
                for (Doctor doctor : section.listDoctors()) {
                    System.out.println("  * " + doctor);
                }
            }
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void hastaneSil() {
        System.out.print("Silinecek Hastane ID: ");
        int hastaneId = readIntegerInput();
        
        try {
            // Önce hastanenin var olduğunu kontrol et
            Hospital hastane = crs.getHospital(hastaneId);
            crs.removeHospital(hastaneId);
            System.out.println("Hastane, bölümleri, doktorları ve ilgili randevular başarıyla silindi.");
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void bolumSil() {
        System.out.print("Hastane ID: ");
        int hastaneId = readIntegerInput();
        
        try {
            Hospital hastane = crs.getHospital(hastaneId);
            System.out.println("\nMevcut Bölümler:");
            for (Section bolum : hastane.getSections()) {
                System.out.printf("ID: %d - %s%n", bolum.getId(), bolum.getName());
            }
            
            System.out.print("\nSilinecek Bölüm ID: ");
            int bolumId = readIntegerInput();
            
            crs.removeSection(hastaneId, bolumId);
            System.out.println("Bölüm, doktorları ve ilgili randevular başarıyla silindi.");
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void doktorSil() {
        System.out.print("Hastane ID: ");
        int hastaneId = readIntegerInput();
        
        try {
            Hospital hastane = crs.getHospital(hastaneId);
            System.out.println("\nMevcut Bölümler:");
            for (Section bolum : hastane.getSections()) {
                System.out.printf("ID: %d - %s%n", bolum.getId(), bolum.getName());
            }
            
            System.out.print("\nBölüm ID: ");
            int bolumId = readIntegerInput();
            
            Section bolum = hastane.getSection(bolumId);
            System.out.println("\nMevcut Doktorlar:");
            for (Doctor doktor : bolum.listDoctors()) {
                System.out.printf("Diploma No: %d - Dr. %s%n", 
                    doktor.getDiplomaId(), doktor.getName());
            }
            
            System.out.print("\nSilinecek Doktor Diploma No: ");
            int diplomaNo = readIntegerInput();
            
            crs.removeDoctor(hastaneId, bolumId, diplomaNo);
            System.out.println("Doktor ve randevuları başarıyla silindi.");
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void randevuMenu() {
        while (true) {
            System.out.printf(MENU_HEADER, "Randevu İşlemleri");
            String[] options = {
                "Randevu Oluştur"
            };
            displayMenuOptions(options);
            System.out.println(BACK_OPTION);
            
            int choice = readIntegerInput();
            if (choice == 0) break;
            
            switch (choice) {
                case 1 -> randevuOlustur();
                default -> System.out.println(INVALID_CHOICE);
            }
        }
    }
    
    private void randevuOlustur() {
        try {
            // 1. Hasta bilgisi
            System.out.print("Hasta TC No: ");
            long tcNo = readLongInput();
            
            Patient hasta = crs.getPatient(tcNo);
            System.out.println("Hasta Adı: " + hasta.getName());
            
            // 2. Hastane seçimi
            System.out.println("\nMevcut Hastaneler:");
            System.out.println("------------------");
            for (Hospital hastane : crs.getHospitals().values()) {
                System.out.printf("ID: %d - %s%n", hastane.getId(), hastane.getName());
            }
            
            System.out.print("\nHastane ID: ");
            int hastaneId = readIntegerInput();
            Hospital hastane = crs.getHospital(hastaneId);
            
            // 3. Bölüm seçimi
            System.out.println("\nHastanedeki Bölümler:");
            System.out.println("---------------------");
            for (Section bolum : hastane.getSections()) {
                System.out.printf("ID: %d - %s%n", bolum.getId(), bolum.getName());
            }
            
            System.out.print("\nBölüm ID: ");
            int bolumId = readIntegerInput();
            Section bolum = hastane.getSection(bolumId);
            
            // 4. Doktor seçimi
            System.out.println("\nBölümdeki Doktorlar ve Mevcut Randevuları:");
            System.out.println("----------------------------------------");
            for (Doctor doktor : bolum.listDoctors()) {
                System.out.printf("Diploma No: %d - Dr. %s%n", 
                    doktor.getDiplomaId(), doktor.getName());
                
                // Doktorun mevcut randevularını listele
                System.out.println("Mevcut Randevuları:");
                boolean hasAppointment = false;
                for (Rendezvous randevu : crs.getRendezvous()) {
                    if (randevu.getDoctor().equals(doktor)) {
                        System.out.printf("  - %s%n", 
                            dateFormat.format(randevu.getDateTime()));
                        hasAppointment = true;
                    }
                }
                if (!hasAppointment) {
                    System.out.println("  - Henüz randevusu yok");
                }
                System.out.println();
            }
            
            System.out.print("\nDoktor Diploma No: ");
            int doktorId = readIntegerInput();
            
            // 5. Randevu tarihi
            System.out.println("\nRandevu Tarihi ve Saati");
            System.out.println("Örnek format: 25/12/2024 14:30");
            System.out.print("Tarih ve Saat: ");
            String tarih = scanner.nextLine();
            Date date = dateFormat.parse(tarih);
            
            // Seçilen doktoru al
            Doctor secilenDoktor = bolum.getDoctor(doktorId);
            
            // Randevu çakışması kontrolü
            for (Rendezvous randevu : crs.getRendezvous()) {
                if (randevu.getDoctor().equals(secilenDoktor)) {
                    long fark = Math.abs(randevu.getDateTime().getTime() - date.getTime());
                    long dakikaFark = fark / (60 * 1000); // milisaniyeyi dakikaya çevir
                    
                    if (dakikaFark < 15) {
                        System.out.println("Hata: Bu saatte randevu oluşturulamaz. Lütfen en az 15 dakika ara olan bir saat seçin.");
                        return;
                    }
                }
            }
            
            // 6. Randevu oluşturma
            boolean sonuc = crs.makeRendezvous(tcNo, hastaneId, bolumId, doktorId, date);
            if (sonuc) {
                System.out.println("Randevu başarıyla oluşturuldu.");
            } else {
                System.out.println("Randevu oluşturulamadı. Seçilen tarihte uygun yer yok.");
            }
            
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Hata: Geçersiz tarih formatı. Lütfen GG/AA/YYYY formatında giriniz.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void randevuListele() {
        System.out.print("TC No giriniz: ");
        long tcNo = readLongInput();
        
        System.out.println("\nRandevu Listesi:");
        System.out.println("----------------");
        
        boolean randevuBulundu = false;
        for (Rendezvous randevu : crs.getRendezvous()) {
            // Sadece girilen TC'ye ait randevuları göster
            if (randevu.getPatient().getNationalId() != tcNo) continue;
            
            randevuBulundu = true;
            Doctor doktor = randevu.getDoctor();
            
            // Doktorun bölümünü ve hastanesini bul
            String hastaneAdi = "";
            String bolumAdi = "";
            
            for (Hospital hastane : crs.getHospitals().values()) {
                for (Section bolum : hastane.getSections()) {
                    if (bolum.listDoctors().contains(doktor)) {
                        hastaneAdi = hastane.getName();
                        bolumAdi = bolum.getName();
                        break;
                    }
                }
                if (!hastaneAdi.isEmpty()) break;
            }
            
            System.out.printf("Tarih/Saat: %s%n", dateFormat.format(randevu.getDateTime()));
            System.out.printf("Hasta: %s (TC: %d)%n", randevu.getPatient().getName(), randevu.getPatient().getNationalId());
            System.out.printf("Hastane: %s%n", hastaneAdi);
            System.out.printf("Bölüm: %s%n", bolumAdi);
            System.out.printf("Doktor: Dr. %s (Diploma No: %d)%n", 
                doktor.getName(), doktor.getDiplomaId());
            System.out.println("----------------");
        }
        
        if (!randevuBulundu) {
            System.out.println("Bu TC numarasına ait randevu bulunmamaktadır.");
        }
    }
    
    private void verileriKaydet() {
        try {
            // Her zaman data.ser'e kaydet
            crs.saveTablesToDisk("data.ser");
            System.out.println("Veriler başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Hata: Veriler kaydedilemedi - " + e.getMessage());
        }
    }
    
    private void verileriYukle() {
        try {
            System.out.print("Dosya adı: ");
            String filename = scanner.nextLine();
            this.crs = CRS.loadTablesFromDisk(filename);
            System.out.println("Veriler başarıyla yüklendi.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Hata: Veriler yüklenemedi - " + e.getMessage());
        }
    }
} 