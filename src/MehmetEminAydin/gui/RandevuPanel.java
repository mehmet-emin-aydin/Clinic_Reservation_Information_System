package MehmetEminAydin.gui;

import MehmetEminAydin.*;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import com.toedter.calendar.JDateChooser; // JCalendar kütüphanesi için
import java.util.LinkedList;
import java.util.Calendar;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import java.awt.FlowLayout;

public class RandevuPanel extends JPanel {
    private CRS crs;
    private JTextField txtTCNo;
    private JComboBox<Hospital> cmbHastane;
    private JComboBox<Section> cmbBolum;
    private JComboBox<Doctor> cmbDoktor;
    private JDateChooser dateChooser;
    private JLabel lblHastaAd;
    private JSpinner timeSpinner;
    
    public RandevuPanel(CRS crs) {
        this.crs = crs;
        setLayout(new BorderLayout());
        createInputPanel();
    }
    
    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Randevu Oluştur"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // TC No alanı
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("TC No:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        txtTCNo = new JTextField(15);
        inputPanel.add(txtTCNo, gbc);
        
        // TC No Ara butonu
        gbc.gridx = 2; gbc.gridy = 0;
        JButton btnAra = new JButton("Ara");
        btnAra.addActionListener(e -> hastaAra());
        inputPanel.add(btnAra, gbc);
        
        // Hasta Adı (readonly)
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Hasta:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        lblHastaAd = new JLabel("-");
        inputPanel.add(lblHastaAd, gbc);
        
        // Hastane seçimi
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Hastane:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        cmbHastane = new JComboBox<>();
        cmbHastane.addActionListener(e -> hastaneSecildi());
        inputPanel.add(cmbHastane, gbc);
        
        // Bölüm seçimi
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Bölüm:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        cmbBolum = new JComboBox<>();
        cmbBolum.addActionListener(e -> bolumSecildi());
        inputPanel.add(cmbBolum, gbc);
        
        // Doktor seçimi
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Doktor:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        cmbDoktor = new JComboBox<>();
        inputPanel.add(cmbDoktor, gbc);
        
        // Tarih seçimi
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Tarih:"), gbc);
        
        // Tarih seçici için format güncelleme
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy HH:mm");
        // Saat ve dakika seçimini aktif et
        timeSpinner = new JSpinner(new SpinnerDateModel());
        DateEditor timeEditor = new DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        
        // Tarih ve saat paneli
        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateTimePanel.add(dateChooser);
        dateTimePanel.add(timeSpinner);
        
        gbc.gridx = 1; gbc.gridy = 5;
        inputPanel.add(dateTimePanel, gbc);
        
        // Randevu oluştur butonu
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnOlustur = new JButton("Randevu Oluştur");
        btnOlustur.addActionListener(e -> randevuOlustur());
        inputPanel.add(btnOlustur, gbc);
        
        add(inputPanel, BorderLayout.CENTER);
        
        // Combobox'ları doldur
        updateHastaneList();
    }
    
    private void hastaAra() {
        try {
            String tcNoStr = txtTCNo.getText().trim();
            if (tcNoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen TC No giriniz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            long tcNo = Long.parseLong(tcNoStr);
            Patient hasta = crs.getPatient(tcNo);
            lblHastaAd.setText(hasta.getName());
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Geçerli bir TC No giriniz.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IDException e) {
            JOptionPane.showMessageDialog(this, 
                "Hasta bulunamadı.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
            lblHastaAd.setText("-");
        }
    }
    
    private void hastaneSecildi() {
        Hospital seciliHastane = (Hospital) cmbHastane.getSelectedItem();
        cmbBolum.removeAllItems();
        cmbDoktor.removeAllItems();
        
        if (seciliHastane != null) {
            // Sadece doktoru olan bölümleri ekle
            for (Section bolum : seciliHastane.getSections()) {
                if (!bolum.listDoctors().isEmpty()) {
                    cmbBolum.addItem(bolum);
                }
            }
        }
    }
    
    private void bolumSecildi() {
        Section seciliBolum = (Section) cmbBolum.getSelectedItem();
        if (seciliBolum != null) {
            cmbDoktor.removeAllItems();
            for (Doctor doktor : seciliBolum.listDoctors()) {
                cmbDoktor.addItem(doktor);
            }
        }
    }
    
    private void randevuOlustur() {
        try {
            // Gerekli alanların kontrolü
            if (lblHastaAd.getText().equals("-")) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen önce hasta seçiniz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen tarih seçiniz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Seçilen tarihi al
            Date secilenTarih = dateChooser.getDate();
            // Seçilen saati al ve tarihe ekle
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(secilenTarih);
            Date secilenSaat = (Date) timeSpinner.getValue();
            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(secilenSaat);
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            Date randevuTarihi = calendar.getTime();
            
            // Randevu çakışması kontrolü
            Doctor secilenDoktor = (Doctor) cmbDoktor.getSelectedItem();
            for (Rendezvous randevu : crs.getRendezvous()) {
                if (randevu.getDoctor().equals(secilenDoktor)) {
                    long fark = Math.abs(randevu.getDateTime().getTime() - randevuTarihi.getTime());
                    long dakikaFark = fark / (60 * 1000);
                    
                    if (dakikaFark < 15) {
                        System.out.println("Hata: Bu saatte randevu oluşturulamaz. Lütfen en az 15 dakika ara olan bir saat seçin.");
                        return;
                    }
                }
            }
            
            Hospital hastane = (Hospital) cmbHastane.getSelectedItem();
            Section bolum = (Section) cmbBolum.getSelectedItem();
            
            if (hastane == null || bolum == null || secilenDoktor == null) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen hastane, bölüm ve doktor seçiniz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Randevu oluştur
            long tcNo = Long.parseLong(txtTCNo.getText().trim());
            boolean sonuc = crs.makeRendezvous(tcNo, 
                hastane.getId(), 
                bolum.getId(), 
                secilenDoktor.getDiplomaId(), 
                randevuTarihi);
            
            if (sonuc) {
                JOptionPane.showMessageDialog(this, 
                    "Randevu başarıyla oluşturuldu.", 
                    "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                formTemizle();
            } else {
                System.out.println("Hata: Seçilen tarihte uygun randevu bulunmamaktadır.");
            }
            
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
    
    private void formTemizle() {
        txtTCNo.setText("");
        lblHastaAd.setText("-");
        cmbHastane.setSelectedIndex(-1);
        cmbBolum.removeAllItems();
        cmbDoktor.removeAllItems();
        dateChooser.setDate(null);
    }
    
    public void updateHastaneList() {
        cmbHastane.removeAllItems();
        cmbBolum.removeAllItems();
        cmbDoktor.removeAllItems();
        
        // Sadece doktoru olan hastaneleri ekle
        for (Hospital hastane : crs.getHospitals().values()) {
            boolean hasDoctors = false;
            for (Section bolum : hastane.getSections()) {
                if (!bolum.listDoctors().isEmpty()) {
                    hasDoctors = true;
                    break;
                }
            }
            if (hasDoctors) {
                cmbHastane.addItem(hastane);
            }
        }
    }
    
    // CRS nesnesini güncelle (veri yüklemesi sonrası çağrılır)
    public void updateCRS(CRS crs) {
        this.crs = crs;
        updateHastaneList();
    }
} 