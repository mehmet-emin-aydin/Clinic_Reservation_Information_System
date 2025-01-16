package MehmetEminAydin;

import MehmetEminAydin.gui.MainFrame;
import MehmetEminAydin.console.ConsoleUI;
import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {
    private static final String WELCOME_MSG = "Klinik Rezervasyon Sistemi";
    private static final String[] OPTIONS = {
        "GUI Modu",
        "Konsol Modu"
    };
    
    public static void main(String[] args) {
        displayMenu();
        int choice = getUserChoice();
        executeUserChoice(choice);
    }
    
    private static void displayMenu() {
        System.out.println(WELCOME_MSG);
        for (int i = 0; i < OPTIONS.length; i++) {
            System.out.printf("%d. %s%n", i + 1, OPTIONS[i]);
        }
        System.out.print("Seçiminiz: ");
    }
    
    private static int getUserChoice() {
        return new Scanner(System.in).nextInt();
    }
    
    private static void executeUserChoice(int choice) {
        switch (choice) {
            case 1 -> launchGUI();
            case 2 -> launchConsole();
            default -> handleInvalidChoice();
        }
    }
    
    private static void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame().setVisible(true);
            } catch (Exception e) {
                System.err.printf("GUI başlatılırken hata: %s%n", e.getMessage());
            }
        });
    }
    
    private static void launchConsole() {
        new ConsoleUI().start();
    }
    
    private static void handleInvalidChoice() {
        System.out.println("Geçersiz seçim!");
        System.exit(1);
    }
} 