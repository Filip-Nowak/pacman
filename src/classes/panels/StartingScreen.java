package classes.panels;

import classes.buttons.MenuBtn;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class StartingScreen extends Options {
    public StartingScreen(ArrayList<Component> componentsList)
    {
        super(componentsList);
        JLabel title = (JLabel) componentsList.get(0);
        title.setPreferredSize(new Dimension(770,200));
        title.setIcon(new ImageIcon("src"+ File.separator+"images"+File.separator+"info"+File.separator+"title.png"));
        MenuBtn start = (MenuBtn) componentsList.get(1);
        MenuBtn exit = (MenuBtn) componentsList.get(2);
        this.setBackground(Color.BLACK);
        addActions(start,"select");
        addActions(exit,"exit");

    }

}