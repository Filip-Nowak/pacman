package classes.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MenuBtn extends JButton implements FocusListener,Selectable {
    public MenuBtn(String name)
    {
        super(name);
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE,5));
        this.setPreferredSize(new Dimension(300,100));
        this.setFocusPainted(false);
        this.setFont(new Font("Arial", Font.BOLD, 24));
        this.setForeground(Color.WHITE);
        this.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        this.setBackground(Color.RED);
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.setBackground(Color.BLACK);
    }
}
