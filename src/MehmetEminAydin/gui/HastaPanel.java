package MehmetEminAydin.gui;

import MehmetEminAydin.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class HastaPanel extends JPanel {
    private CRS crs;
    private JTextField txtAd;
    private JTextField txtTCNo;
    private JTable hastaTable;
    private DefaultTableModel tableModel;
    
    public HastaPanel(CRS crs) {
        this.crs = crs;
        setLayout(new BorderLayout());
        
        // Üst panel - Hasta Ekleme Formu
        createInputPanel();
        
        // Alt panel - Hasta Listesi
        createTablePanel();
        
        // Tabloyu güncelle
        updateHastaTable();
    }
    
    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Yeni Hasta Ekle"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Ad alanı
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Ad Soyad:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        txtAd = new JTextField(20);
        inputPanel.add(txtAd, gbc);
        
        // TC No alanı
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("TC No:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        txtTCNo = new JTextField(20);
        inputPanel.add(txtTCNo, gbc);
        
        // Ekle butonu
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnEkle = new JButton("Hasta Ekle");
        btnEkle.addActionListener(e -> hastaEkle());
        inputPanel.add(btnEkle, gbc);
        
        add(inputPanel, BorderLayout.NORTH);
    }
    
    private void createTablePanel() {
        // Tablo modeli
        String[] columnNames = {"TC No", "Ad Soyad"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabloyu düzenlenemez yap
            }
        };
        
        hastaTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(hastaTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hasta Listesi"));
        
        // Popup menu ekle
        JPopupMenu popup = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Hastayı Sil");
        deleteItem.addActionListener(e -> silSeciliHasta());
        popup.add(deleteItem);
        
        hastaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            
            private void showPopup(MouseEvent e) {
                int row = hastaTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    hastaTable.setRowSelectionInterval(row, row);
                    popup.show(hastaTable, e.getX(), e.getY());
                }
            }
        });
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void hastaEkle() {
        try {
            String ad = txtAd.getText().trim();
            String tcNoStr = txtTCNo.getText().trim();
            
            // Validasyonlar
            if (ad.isEmpty() || tcNoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen tüm alanları doldurun.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // TC No kontrolü
            if (!tcNoStr.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, 
                    "TC No 11 haneli bir sayı olmalıdır.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            long tcNo = Long.parseLong(tcNoStr);
            
            // Hasta zaten var mı kontrolü
            try {
                crs.getPatient(tcNo);
                JOptionPane.showMessageDialog(this, 
                    "Bu TC No'ya sahip hasta zaten kayıtlı.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IDException e) {
                // Hasta yok, devam edebiliriz
            }
            
            // Yeni hasta oluştur ve ekle
            Patient yeniHasta = new Patient(ad, tcNo);
            crs.addPatient(yeniHasta);
            
            // Tabloyu güncelle
            updateHastaTable();
            
            // Formu temizle
            txtAd.setText("");
            txtTCNo.setText("");
            
            JOptionPane.showMessageDialog(this, 
                "Hasta başarıyla eklendi.", 
                "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "TC No geçerli bir sayı olmalıdır.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateHastaTable() {
        // Tabloyu temizle
        tableModel.setRowCount(0);
        
        // Hastaları tabloya ekle
        HashMap<Long, Patient> hastalar = crs.getPatients();
        for (Patient hasta : hastalar.values()) {
            Object[] row = {
                hasta.getNationalId(),
                hasta.getName()
            };
            tableModel.addRow(row);
        }
    }
    
    private void silSeciliHasta() {
        int selectedRow = hastaTable.getSelectedRow();
        if (selectedRow >= 0) {
            long tcNo = (long) hastaTable.getValueAt(selectedRow, 1);
            
            int choice = JOptionPane.showConfirmDialog(this,
                "Hasta ve randevuları silinecek. Emin misiniz?",
                "Hasta Sil", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                crs.removePatient(tcNo);
                updateHastaTable();
                System.out.println("Hasta silindi: " + tcNo);
            }
        }
    }
    
    // CRS nesnesini güncelle (veri yüklemesi sonrası çağrılır)
    public void updateCRS(CRS crs) {
        this.crs = crs;
        updateHastaTable();
    }
} 