package classes.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;

public class SelectButton extends JPanel implements FocusListener,Selectable {
    public DirectionButton left=new DirectionButton('l');

    public DirectionButton right= new DirectionButton('r');
    private final JLabel option;
    private final ArrayList<String> options;

    public JLabel getOption() {
        return option;
    }

    int pos=0;
    public SelectButton(String name, ArrayList<String> options)
    {
        JLabel title = labelMaker(new Dimension(500, 30), 30, Color.GRAY, name);
        this.option=labelMaker(new Dimension(300,70),40,Color.WHITE,options.get(0));
        JPanel container = new JPanel();
        panelMaker(container,new Dimension(500,130));
        panelMaker(this,new Dimension(776,130));
        this.setFocusable(true);
        container.add(title);
        container.add(left);
        container.add(option);
        container.add(right);
        this.options=options;
        add(container);
        this.addFocusListener(this);
    }
    void panelMaker(JPanel panel,Dimension d)
    {
        panel.setPreferredSize(d);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        panel.setBackground(null);
    }

    JLabel labelMaker(Dimension d, int fontSize, Color fontColor,String name)
    {
        JLabel label=new JLabel(name);
        label.setPreferredSize(d);
        label.setFont(new Font("Arial",Font.BOLD,fontSize));
        label.setForeground(fontColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBackground(null);
        return label;
    }

    @Override
    public void focusGained(FocusEvent e) {
        this.setBackground(Color.LIGHT_GRAY);
    }
    @Override
    public void focusLost(FocusEvent e) {
        this.setBackground(null);
    }

    private class DirectionButton extends JButton{
        private static final ImageIcon leftImage =new ImageIcon("src"+ File.separator+"images"+File.separator+"buttons"+File.separator+"left.png");
        private static final ImageIcon rightImage=new ImageIcon("src"+ File.separator+"images"+File.separator+"buttons"+File.separator+"right.png");
        public DirectionButton(char c) {
            this.setPreferredSize(new Dimension(70,70));
            this.setBackground(null);
            this.setBorder(null);
            this.setFocusPainted(false);
            this.setFocusable(false);
            if(c=='l')
            {
                this.setIcon(null);
                this.setEnabled(false);
            }
            if(c=='r')
            {
                this.setIcon(rightImage);
            }
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(c=='r')
                    {
                        option.setText(options.get(++pos));
                        if(options.size()==pos+1)
                        {
                            setEnabled(false);
                            setIcon(null);
                        }
                        else if(!left.isEnabled())
                        {
                            left.setEnabled(true);
                            left.setIcon(leftImage);
                        }
                    }
                    else
                    {
                        option.setText(options.get(--pos));
                        if(0==pos)
                        {
                            setEnabled(false);
                            setIcon(null);
                        }
                        else if(!right.isEnabled())
                        {
                            right.setEnabled(true);
                            right.setIcon(rightImage);
                        }
                    }
                }
            });
        }

    }
}
