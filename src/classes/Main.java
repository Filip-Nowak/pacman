package classes;

import classes.panels.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {
    static public MainCardPanel mainPanel;
    public Main()
    {
        this.setLayout(new BorderLayout());
        this.setTitle("dupa123");
        this.setSize(776,977);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        initComponents();
        this.setVisible(true);
        Image icon = Toolkit.getDefaultToolkit().getImage("src"+ File.separator+"images"+File.separator+"info"+File.separator+"icon.png");
        this.setIconImage(icon);
    }

    private void initComponents() {
        mainPanel=new MainCardPanel();
        mainPanel.setPanels();
        add(mainPanel);
    }
    public static void main(String[] args) {
        new Main();
    }
}