package classes.panels;


import classes.Main;
import classes.buttons.MenuBtn;
import classes.buttons.SelectButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SelectGame extends Options {
    public ArrayList<Object>modes=new ArrayList<>();

    public SelectGame(ArrayList<Component> components) {
        super(components);
        this.setBackground(Color.BLACK);
        MenuBtn back = (MenuBtn) componentsList.get(0);
        MenuBtn start = (MenuBtn) componentsList.get(6);
        addActions(back,"start");
        addActions(start,"game");
        this.setAlignmentX(0);
    }

    @Override
    protected void addActions(MenuBtn menuBtn, String destiny) {
        if(destiny.equals("game")){
            menuBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setModes();
                    launch((String)modes.get(0),(int)modes.get(1),(int)modes.get(2),(int)modes.get(3),(int)modes.get(4));
                    ((CardLayout)Main.mainPanel.getLayout()).show(Main.mainPanel,destiny);
                }
            });
            return;
        }
        super.addActions(menuBtn, destiny);
    }
    private void setModes(){
        int sight,ms;
        try{
            sight=Integer.parseInt(((SelectButton)SelectGame.this.componentsList.get(5)).getOption().getText());
        }catch(NumberFormatException ex){
            sight=0;
        }
        String msString=((SelectButton)SelectGame.this.componentsList.get(4)).getOption().getText();
        ms = switch (msString) {
            case "slow" -> 2;
            case "normal" -> 4;
            case "fast" -> 10;
            default -> 20;
        };
        modes=new ArrayList<>(Arrays.asList(((SelectButton)SelectGame.this.componentsList.get(1)).getOption().getText(),
                Integer.parseInt(((SelectButton)SelectGame.this.componentsList.get(2)).getOption().getText()),
                Integer.parseInt(((SelectButton)SelectGame.this.componentsList.get(3)).getOption().getText()),
                ms,
                sight));
    }
}