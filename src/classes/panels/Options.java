package classes.panels;

import classes.Main;
import classes.buttons.MenuBtn;
import classes.buttons.SelectButton;
import classes.buttons.Selectable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public abstract class Options extends JPanel{
    protected int pos=0;
    protected ArrayList<Component> componentsList;
    public Options(ArrayList<Component> componentsList)
    {
        this.setBackground(null);
        this.componentsList=componentsList;
        JPanel buttonsPanel = new JPanel(new GridLayout(componentsList.size(), 1));
        buttonsPanel.setBackground(null);

        for(Component element:componentsList)
        {
            buttonsPanel.add(element);
        }
        add(buttonsPanel);
        putKeys();
        if(!(this.componentsList.get(0) instanceof Selectable))
        {
            this.pos=1;
        }
    }
    public void resetPos(){
        this.pos=0;
    }
    public void putKeys(){
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0),"up");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0),"down");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0),"right");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0),"left");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"enter");
        putActions();
    }
    private void putActions() {
        this.getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(componentsList.get(pos)instanceof Selectable){
                    if(pos!=0 && componentsList.get(pos-1) instanceof Selectable)
                    {
                        componentsList.get(--pos).requestFocusInWindow();
                    }
                }
            }
        });
        this.getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(componentsList.get(pos)instanceof Selectable){
                    if(pos!=componentsList.size()-1)
                    {
                        componentsList.get(++pos).requestFocusInWindow();
                    }
                }
            }
        });
        this.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(componentsList.get(pos)instanceof MenuBtn){
                    ((MenuBtn) componentsList.get(pos)).doClick();
                }
            }
        });
        this.getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(componentsList.get((pos)) instanceof SelectButton){
                    if(((JButton)(((SelectButton) componentsList.get(pos)).left)).isEnabled())
                    {
                        ((JButton)(((SelectButton) componentsList.get(pos)).left)).doClick();
                    }
                }
            }
        });
        this.getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(componentsList.get((pos)) instanceof SelectButton){
                    if(((JButton)(((SelectButton) componentsList.get(pos)).right)).isEnabled())
                    {
                        ((JButton)(((SelectButton) componentsList.get(pos)).right)).doClick();
                    }
                }
            }
        });
    }
    protected void addActions(MenuBtn menuBtn, Game game, String destiny){
        if(destiny.equals("exit"))
        {
            menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.exitGame();
                    ((CardLayout) Main.mainPanel.getLayout()).show(Main.mainPanel, "start");
                    Main.mainPanel.startingScreen.getFirstButton().requestFocus();
                    Main.mainPanel.startingScreen.pos = 1;
                }
            });
        }else{
            menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.exitGame();
                    launch(game.getMode(),game.getAmountOfGhosts(),game.getAmountOfLives(),game.getMovementSpeed(),game.getSight());
                    ((CardLayout) Main.mainPanel.getLayout()).show(Main.mainPanel, "game");
                }
            });
        }


    }
    protected void addActions(MenuBtn menuBtn, String destiny)
    {
        switch (destiny) {
            case "select" -> menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((CardLayout) Main.mainPanel.getLayout()).show(Main.mainPanel, destiny);
                    Main.mainPanel.selectGame.getFirstButton().requestFocus();
                    Main.mainPanel.selectGame.pos = 0;
                }
            });
            case "start" -> menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((CardLayout) Main.mainPanel.getLayout()).show(Main.mainPanel, destiny);
                    Main.mainPanel.startingScreen.getFirstButton().requestFocus();
                    Main.mainPanel.startingScreen.pos = 1;

                }
            });
            case "exit" -> menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
    }

    protected void launch(String mode,int amountOfGhosts,int amountOfLives,int movementSpeed, int sight) {
        if(Main.mainPanel.getComponents().length>2)
            Main.mainPanel.remove(2);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Game g = (Game) Main.mainPanel.add(new Game(mode,amountOfGhosts,amountOfLives,movementSpeed,sight));
        g.getGameThread().start();
        Main.mainPanel.add(g,"game");
    }


    public MenuBtn getFirstButton() {
        for(Component c:componentsList)
        {
            if(c instanceof MenuBtn)
            {
                return (MenuBtn) c;
            }
        }
        return new MenuBtn("Xd");
    }

}
