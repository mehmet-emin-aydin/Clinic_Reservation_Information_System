package MehmetEminAydin.gui;

import MehmetEminAydin.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class HastaneYonetimPanel extends JPanel {
    private CRS crs;
    private JTree hastaneTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode rootNode;
    private Runnable updateCallback;
    
    // Hastane ekleme alanları
    private JTextField txtHastaneId;
    private JTextField txtHastaneAd;
    
    // Bölüm ekleme alanları
    private JTextField txtBolumId;
    private JTextField txtBolumAd;
    
    // Doktor ekleme alanları
    private JTextField txtDoktorAd;
    private JTextField txtDoktorTC;
    private JTextField txtDoktorDiploma;
    
    public HastaneYonetimPanel(CRS crs, Runnable updateCallback) {
        this.crs = crs;
        this.updateCallback = updateCallback;
        setLayout(new BorderLayout());
        
        // Sol tarafta ağaç görünümü
        createTreePanel();
        
        // Sağ tarafta işlem paneli
        createActionPanel();
        
        // Ağacı güncelle
        updateTree();
    }
    
    private void createTreePanel() {
        rootNode = new DefaultMutableTreeNode("Hastaneler");
        treeModel = new DefaultTreeModel(rootNode);
        hastaneTree = new JTree(treeModel);
        hastaneTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        // Popup menu ekle
        JPopupMenu popup = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Sil");
        deleteItem.addActionListener(e -> silSeciliOge());
        popup.add(deleteItem);
        
        hastaneTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            
            private void showPopup(MouseEvent e) {
                TreePath path = hastaneTree.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    hastaneTree.setSelectionPath(path);
                    popup.show(hastaneTree, e.getX(), e.getY());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(hastaneTree);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.WEST);
    }
    
    private void createActionPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Hastane Ekleme Paneli
        JPanel hastanePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        hastanePanel.add(new JLabel("Hastane ID:"), gbc);
        gbc.gridx = 1;
        txtHastaneId = new JTextField(10);
        hastanePanel.add(txtHastaneId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        hastanePanel.add(new JLabel("Hastane Adı:"), gbc);
        gbc.gridx = 1;
        txtHastaneAd = new JTextField(20);
        hastanePanel.add(txtHastaneAd, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JButton btnHastaneEkle = new JButton("Hastane Ekle");
        btnHastaneEkle.addActionListener(e -> hastaneEkle());
        hastanePanel.add(btnHastaneEkle, gbc);
        
        // Bölüm Ekleme Paneli
        JPanel bolumPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        bolumPanel.add(new JLabel("Bölüm ID:"), gbc);
        gbc.gridx = 1;
        txtBolumId = new JTextField(10);
        bolumPanel.add(txtBolumId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        bolumPanel.add(new JLabel("Bölüm Adı:"), gbc);
        gbc.gridx = 1;
        txtBolumAd = new JTextField(20);
        bolumPanel.add(txtBolumAd, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JButton btnBolumEkle = new JButton("Bölüm Ekle");
        btnBolumEkle.addActionListener(e -> bolumEkle());
        bolumPanel.add(btnBolumEkle, gbc);
        
        // Doktor Ekleme Paneli
        JPanel doktorPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        doktorPanel.add(new JLabel("Doktor Adı:"), gbc);
        gbc.gridx = 1;
        txtDoktorAd = new JTextField(20);
        doktorPanel.add(txtDoktorAd, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        doktorPanel.add(new JLabel("TC No:"), gbc);
        gbc.gridx = 1;
        txtDoktorTC = new JTextField(11);
        doktorPanel.add(txtDoktorTC, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        doktorPanel.add(new JLabel("Diploma No:"), gbc);
        gbc.gridx = 1;
        txtDoktorDiploma = new JTextField(10);
        doktorPanel.add(txtDoktorDiploma, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnDoktorEkle = new JButton("Doktor Ekle");
        btnDoktorEkle.addActionListener(e -> doktorEkle());
        doktorPanel.add(btnDoktorEkle, gbc);
        
        tabbedPane.addTab("Hastane Ekle", hastanePanel);
        tabbedPane.addTab("Bölüm Ekle", bolumPanel);
        tabbedPane.addTab("Doktor Ekle", doktorPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void hastaneEkle() {
        try {
            int id = Integer.parseInt(txtHastaneId.getText().trim());
            String ad = txtHastaneAd.getText().trim();
            
            if (ad.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Hastane adı boş olamaz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Hospital hastane = new Hospital(id, ad);
            try {
                crs.addHospital(hastane);
                updateTree();
                txtHastaneId.setText("");
                txtHastaneAd.setText("");
                
                JOptionPane.showMessageDialog(this, 
                    "Hastane başarıyla eklendi.", 
                    "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            } catch (DuplicateInfoException e) {
                System.out.println("Hata: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Geçerli bir hastane ID giriniz.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void bolumEkle() {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
                hastaneTree.getLastSelectedPathComponent();
                
            if (node == null || !(node.getUserObject() instanceof Hospital)) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen bir hastane seçiniz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(txtBolumId.getText().trim());
            String ad = txtBolumAd.getText().trim();
            
            if (ad.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Bölüm adı boş olamaz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Hospital hastane = (Hospital) node.getUserObject();
            Section bolum = new Section(id, ad);
            
            try {
                // Bölüm ID kontrolü
                for (Section existingSection : hastane.getSections()) {
                    if (existingSection.getId() == id) {
                        throw new DuplicateInfoException("Bu ID'ye sahip bölüm zaten mevcut: " + id);
                    }
                }
                
                hastane.addSection(bolum);
                updateTree();
                txtBolumId.setText("");
                txtBolumAd.setText("");
                
                JOptionPane.showMessageDialog(this, 
                    "Bölüm başarıyla eklendi.", 
                    "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            } catch (DuplicateInfoException e) {
                System.out.println("Hata: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Geçerli bir bölüm ID giriniz.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doktorEkle() {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
                hastaneTree.getLastSelectedPathComponent();
                
            if (node == null || !(node.getUserObject() instanceof Section)) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen bir bölüm seçiniz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String ad = txtDoktorAd.getText().trim();
            String tcStr = txtDoktorTC.getText().trim();
            String diplomaStr = txtDoktorDiploma.getText().trim();
            
            if (ad.isEmpty() || tcStr.isEmpty() || diplomaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Lütfen tüm alanları doldurunuz.", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            long tc = Long.parseLong(tcStr);
            int diplomaNo = Integer.parseInt(diplomaStr);
            
            try {
                // Önce diploma numarasını kontrol et
                crs.checkDoctorDiplomaId(diplomaNo);
                
                Section bolum = (Section) node.getUserObject();
                Doctor doktor = new Doctor(ad, tc, diplomaNo);
                
                bolum.addDoctor(doktor);
                updateTree();
                txtDoktorAd.setText("");
                txtDoktorTC.setText("");
                txtDoktorDiploma.setText("");
                
                updateCallback.run();
                
                JOptionPane.showMessageDialog(this, 
                    "Doktor başarıyla eklendi.", 
                    "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            } catch (DuplicateInfoException e) {
                System.out.println("Hata: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Geçerli bir TC No ve diploma numarası giriniz.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTree() {
        rootNode.removeAllChildren();
        
        // Hastaneleri ekle
        HashMap<Integer, Hospital> hastaneler = crs.getHospitals();
        for (Hospital hastane : hastaneler.values()) {
            DefaultMutableTreeNode hastaneNode = new DefaultMutableTreeNode(hastane);
            
            // Bölümleri ekle
            for (Section bolum : hastane.getSections()) {
                DefaultMutableTreeNode bolumNode = new DefaultMutableTreeNode(bolum);
                
                // Doktorları ekle
                for (Doctor doktor : bolum.listDoctors()) {
                    bolumNode.add(new DefaultMutableTreeNode(doktor));
                }
                
                hastaneNode.add(bolumNode);
            }
            
            rootNode.add(hastaneNode);
        }
        
        treeModel.reload();
        // Tüm düğümleri genişlet
        for (int i = 0; i < hastaneTree.getRowCount(); i++) {
            hastaneTree.expandRow(i);
        }
    }
    
    public void updateCRS(CRS crs) {
        this.crs = crs;
        updateTree();
    }
    
    private void silSeciliOge() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
            hastaneTree.getLastSelectedPathComponent();
        
        if (node == null) return;
        
        Object userObject = node.getUserObject();
        try {
            if (userObject instanceof Hospital) {
                Hospital hastane = (Hospital) userObject;
                int choice = JOptionPane.showConfirmDialog(this,
                    "Hastane ve tüm alt birimleri silinecek. Emin misiniz?",
                    "Hastane Sil", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    crs.removeHospital(hastane.getId());
                    System.out.println("Hastane ve alt birimleri silindi: " + hastane.getName());
                }
            }
            else if (userObject instanceof Section) {
                Section bolum = (Section) userObject;
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                Hospital hastane = (Hospital) parentNode.getUserObject();
                
                int choice = JOptionPane.showConfirmDialog(this,
                    "Bölüm ve doktorları silinecek. Emin misiniz?",
                    "Bölüm Sil", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    crs.removeSection(hastane.getId(), bolum.getId());
                    System.out.println("Bölüm silindi: " + bolum.getName());
                }
            }
            else if (userObject instanceof Doctor) {
                Doctor doktor = (Doctor) userObject;
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                Section bolum = (Section) parentNode.getUserObject();
                DefaultMutableTreeNode hospitalNode = (DefaultMutableTreeNode) parentNode.getParent();
                Hospital hastane = (Hospital) hospitalNode.getUserObject();
                
                int choice = JOptionPane.showConfirmDialog(this,
                    "Doktor ve randevuları silinecek. Emin misiniz?",
                    "Doktor Sil", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    crs.removeDoctor(hastane.getId(), bolum.getId(), doktor.getDiplomaId());
                    System.out.println("Doktor silindi: Dr. " + doktor.getName());
                }
            }
            
            updateTree();
            updateCallback.run(); // Diğer panelleri güncelle
            
        } catch (IDException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
} 