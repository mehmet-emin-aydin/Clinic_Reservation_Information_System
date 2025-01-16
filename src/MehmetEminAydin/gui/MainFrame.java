package MehmetEminAydin.gui;

import MehmetEminAydin.CRS;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.GridLayout;

public class MainFrame extends JFrame {
    private CRS crs;
    private final JPanel contentPanel;
    private final Map<String, JButton> navigationButtons;
    private final Map<String, String> panelKeys;
    private ListelemePanel listelemePanel;
    
    public MainFrame() {
        // Başlangıçta data.ser'den yüklemeyi dene
        try {
            this.crs = CRS.loadTablesFromDisk("data.ser");
        } catch (Exception e) {
            // Dosya yoksa veya okuma hatası varsa yeni CRS oluştur
            this.crs = new CRS();
        }
        
        this.contentPanel = new JPanel(new CardLayout());
        this.navigationButtons = new HashMap<>();
        this.panelKeys = new HashMap<>();
        
        initializeFrame();
        setupComponents();
    }
    
    private void initializeFrame() {
        setTitle("Klinik Rezervasyon Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void setupComponents() {
        setupNavigationButtons();
        setupContentPanels();
        setupMenuBar();
        setupMainLayout();
    }
    
    private void setupNavigationButtons() {
        String[][] buttonConfigs = {
            {"hasta", "Hasta İşlemleri", "HASTA"},
            {"randevu", "Randevu İşlemleri", "RANDEVU"},
            {"listeleme", "Randevu Listeleme", "LISTELEME"},
            {"hastane", "Hastane İşlemleri", "HASTANE"}
        };
        
        for (String[] config : buttonConfigs) {
            createNavigationButton(config[0], config[1], config[2]);
        }
    }
    
    private void createNavigationButton(String id, String text, String panelKey) {
        JButton button = new JButton(text);
        button.addActionListener(e -> showPanel(panelKey));
        navigationButtons.put(id, button);
        panelKeys.put(id, panelKey);
    }
    
    private void showPanel(String key) {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, key);
    }
    
    private void setupContentPanels() {
        // Panel oluşturma
        HastaPanel hastaPanel = new HastaPanel(crs);
        RandevuPanel randevuPanel = new RandevuPanel(crs);
        listelemePanel = new ListelemePanel(crs);
        
        // HastaneYonetimPanel'e hem RandevuPanel hem de ListelemePanel güncelleme callback'i ver
        HastaneYonetimPanel hastanePanel = new HastaneYonetimPanel(crs, () -> {
            randevuPanel.updateHastaneList();
            listelemePanel.updateHastaneList();
        });
        
        // Panelleri content panel'e ekleme
        contentPanel.add(hastaPanel, "HASTA");
        contentPanel.add(hastanePanel, "HASTANE");
        contentPanel.add(randevuPanel, "RANDEVU");
        contentPanel.add(listelemePanel, "LISTELEME");
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Dosya menüsü
        JMenu fileMenu = new JMenu("Dosya");
        
        JMenuItem saveItem = new JMenuItem("Kaydet");
        saveItem.addActionListener(e -> saveData());
        
        JMenuItem loadItem = new JMenuItem("Yükle");
        loadItem.addActionListener(e -> loadData());
        
        JMenuItem exitItem = new JMenuItem("Çıkış");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    private void setupMainLayout() {
        // Ana düzen
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Navigasyon paneli
        JPanel navigationPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Navigasyon butonlarını ekleme
        navigationButtons.values().forEach(navigationPanel::add);
        
        // Ana panele yerleştirme
        mainPanel.add(navigationPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void saveData() {
        try {
            // Her zaman data.ser'e kaydet
            crs.saveTablesToDisk("data.ser");
            JOptionPane.showMessageDialog(this, 
                "Veriler başarıyla kaydedildi.", 
                "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Veriler kaydedilirken hata oluştu: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                CRS newCrs = CRS.loadTablesFromDisk(fileChooser.getSelectedFile().getPath());
                this.crs = newCrs;
                
                // Tüm panelleri güncelle
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof HastaPanel) {
                        ((HastaPanel) comp).updateCRS(crs);
                    } else if (comp instanceof HastaneYonetimPanel) {
                        ((HastaneYonetimPanel) comp).updateCRS(crs);
                    } else if (comp instanceof RandevuPanel) {
                        ((RandevuPanel) comp).updateCRS(crs);
                    } else if (comp instanceof ListelemePanel) {
                        ((ListelemePanel) comp).updateCRS(crs);
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Veriler başarıyla yüklendi.", 
                    "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, 
                    "Veriler yüklenirken hata oluştu: " + e.getMessage(), 
                    "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 