package MehmetEminAydin.gui;

import MehmetEminAydin.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JOptionPane;

public class ListelemePanel extends JPanel {
    private CRS crs;
    private JTable randevuTable;
    private DefaultTableModel tableModel;
    private JDateChooser baslangicDate;
    private JDateChooser bitisDate;
    private JComboBox<Hospital> cmbHastane;
    private JComboBox<Section> cmbBolum;
    private JComboBox<Doctor> cmbDoktor;
    private JTextField txtTCNo;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public ListelemePanel(CRS crs) {
        this.crs = crs;
        setLayout(new BorderLayout());
        
        // Üst panel - Filtreler
        createFilterPanel();
        
        // Orta panel - Tablo
        createTablePanel();
        
        // Tabloyu güncelle
        updateTable();
    }
    
    private void createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtreler"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // TC No filtresi ekle
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("TC No:"), gbc);
        
        gbc.gridx = 1;
        txtTCNo = new JTextField(15);
        filterPanel.add(txtTCNo, gbc);
        
        // Tarih aralığı
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Başlangıç:"), gbc);
        
        gbc.gridx = 1;
        baslangicDate = new JDateChooser();
        baslangicDate.setDateFormatString("dd/MM/yyyy");
        filterPanel.add(baslangicDate, gbc);
        
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Bitiş:"), gbc);
        
        gbc.gridx = 3;
        bitisDate = new JDateChooser();
        bitisDate.setDateFormatString("dd/MM/yyyy");
        filterPanel.add(bitisDate, gbc);
        
        // Hastane seçimi
        gbc.gridx = 0; gbc.gridy = 2;
        filterPanel.add(new JLabel("Hastane:"), gbc);
        
        gbc.gridx = 1;
        cmbHastane = new JComboBox<>();
        cmbHastane.addActionListener(e -> hastaneSecildi());
        filterPanel.add(cmbHastane, gbc);
        
        // Bölüm seçimi
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Bölüm:"), gbc);
        
        gbc.gridx = 3;
        cmbBolum = new JComboBox<>();
        cmbBolum.addActionListener(e -> bolumSecildi());
        filterPanel.add(cmbBolum, gbc);
        
        // Doktor seçimi
        gbc.gridx = 0; gbc.gridy = 3;
        filterPanel.add(new JLabel("Doktor:"), gbc);
        
        gbc.gridx = 1;
        cmbDoktor = new JComboBox<>();
        filterPanel.add(cmbDoktor, gbc);
        
        // Filtrele butonu
        gbc.gridx = 3; gbc.gridy = 3;
        JButton btnFiltrele = new JButton("Filtrele");
        btnFiltrele.addActionListener(e -> updateTable());
        filterPanel.add(btnFiltrele, gbc);
        
        // Filtreleri temizle butonu
        gbc.gridx = 2; gbc.gridy = 3;
        JButton btnTemizle = new JButton("Temizle");
        btnTemizle.addActionListener(e -> filtreleriTemizle());
        filterPanel.add(btnTemizle, gbc);
        
        add(filterPanel, BorderLayout.NORTH);
        
        // Combobox'ları doldur
        updateHastaneList();
    }
    
    private void createTablePanel() {
        // Tablo modeli
        String[] columnNames = {
            "Tarih/Saat", 
            "Hasta TC", 
            "Hasta Adı", 
            "Hastane", 
            "Bölüm",
            "Doktor"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        randevuTable = new JTable(tableModel);
        
        // Tarih sütunu için özel renderer
        randevuTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Date) {
                    value = sdf.format((Date) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(randevuTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Popup menu ekle
        JPopupMenu popup = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Randevu İptal Et");
        deleteItem.addActionListener(e -> randevuIptalEt());
        popup.add(deleteItem);
        
        randevuTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            
            private void showPopup(MouseEvent e) {
                int row = randevuTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    randevuTable.setRowSelectionInterval(row, row);
                    popup.show(randevuTable, e.getX(), e.getY());
                }
            }
        });
    }
    
    private void hastaneSecildi() {
        Hospital seciliHastane = (Hospital) cmbHastane.getSelectedItem();
        cmbBolum.removeAllItems();
        cmbDoktor.removeAllItems();
        
        if (seciliHastane != null) {
            for (Section bolum : seciliHastane.getSections()) {
                cmbBolum.addItem(bolum);
            }
        }
    }
    
    private void bolumSecildi() {
        Section seciliBolum = (Section) cmbBolum.getSelectedItem();
        cmbDoktor.removeAllItems();
        
        if (seciliBolum != null) {
            for (Doctor doktor : seciliBolum.listDoctors()) {
                cmbDoktor.addItem(doktor);
            }
        }
    }
    
    private void updateTable() {
        tableModel.setRowCount(0);
        
        // TC No kontrolü
        String tcNoStr = txtTCNo.getText().trim();
        if (tcNoStr.isEmpty()) {
            System.out.println("Lütfen TC No giriniz.");
            return;
        }
        
        Long tcNo;
        try {
            tcNo = Long.parseLong(tcNoStr);
        } catch (NumberFormatException e) {
            System.out.println("Hata: Geçersiz TC No formatı");
            return;
        }
        
        Date baslangic = baslangicDate.getDate();
        Date bitis = bitisDate.getDate();
        Hospital hastane = (Hospital) cmbHastane.getSelectedItem();
        Section bolum = (Section) cmbBolum.getSelectedItem();
        Doctor doktor = (Doctor) cmbDoktor.getSelectedItem();
        
        for (Rendezvous randevu : crs.getRendezvous()) {
            // TC No filtresi - sadece girilen TC'ye ait randevuları göster
            if (randevu.getPatient().getNationalId() != tcNo) continue;
            
            // Diğer filtreler
            if (baslangic != null && randevu.getDateTime().before(baslangic)) continue;
            if (bitis != null && randevu.getDateTime().after(bitis)) continue;
            if (hastane != null && !randevu.getDoctor().equals(doktor)) continue;
            if (bolum != null && !randevu.getDoctor().equals(doktor)) continue;
            if (doktor != null && !randevu.getDoctor().equals(doktor)) continue;
            
            Object[] row = {
                randevu.getDateTime(),
                randevu.getPatient().getNationalId(),
                randevu.getPatient().getName(),
                hastane != null ? hastane.getName() : "Tüm Hastaneler",
                bolum != null ? bolum.getName() : "Tüm Bölümler",
                randevu.getDoctor().getName()
            };
            
            tableModel.addRow(row);
        }
    }
    
    private void filtreleriTemizle() {
        txtTCNo.setText("");
        baslangicDate.setDate(null);
        bitisDate.setDate(null);
        cmbHastane.setSelectedIndex(-1);
        cmbBolum.removeAllItems();
        cmbDoktor.removeAllItems();
        updateTable();
    }
    
    public void updateHastaneList() {
        cmbHastane.removeAllItems();
        for (Hospital hastane : crs.getHospitals().values()) {
            cmbHastane.addItem(hastane);
        }
    }
    
    public void updateCRS(CRS crs) {
        this.crs = crs;
        updateHastaneList();
        updateTable();
    }
    
    private void randevuIptalEt() {
        int selectedRow = randevuTable.getSelectedRow();
        if (selectedRow >= 0) {
            Date tarih = (Date) randevuTable.getValueAt(selectedRow, 0);
            long tcNo = (long) randevuTable.getValueAt(selectedRow, 1);
            String doktorAdi = (String) randevuTable.getValueAt(selectedRow, 5);
            
            int choice = JOptionPane.showConfirmDialog(this,
                "Randevu iptal edilecek. Emin misiniz?\n" +
                "Tarih: " + dateFormat.format(tarih) + "\n" +
                "TC: " + tcNo + "\n" +
                "Doktor: " + doktorAdi,
                "Randevu İptal", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                // Randevuyu bul ve sil
                for (Rendezvous r : crs.getRendezvous()) {
                    if (r.getDateTime().equals(tarih) && 
                        r.getPatient().getNationalId() == tcNo &&
                        r.getDoctor().getName().equals(doktorAdi)) {
                        crs.getRendezvous().remove(r);
                        System.out.println("Randevu iptal edildi: " + dateFormat.format(tarih));
                        updateTable();
                        break;
                    }
                }
            }
        }
    }
} 