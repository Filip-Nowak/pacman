package classes.panels;

import classes.buttons.MenuBtn;
import classes.buttons.SelectButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainCardPanel extends JPanel {
    public StartingScreen startingScreen;
    public SelectGame selectGame;
    public MainCardPanel()
    {
        this.setLayout(new CardLayout());
    }
    public void setPanels()
    {
        this.add(new StartingScreen(new ArrayList<>(Arrays.asList(
                new JLabel(),
                new MenuBtn("Start"),
                new MenuBtn("EXIT")
        ))),"start");

        this.add(new SelectGame(new  ArrayList<>(Arrays.asList(new MenuBtn("BACK"),
                new SelectButton("MODE", new ArrayList<>(Arrays.asList(Game.CLASSIC, Game.TIME, Game.FOLLOW))),
                new SelectButton("GHOSTS", new ArrayList<>(Arrays.asList("1", "2", "3", "4"))),
                new SelectButton("LIVES", new ArrayList<>(Arrays.asList("3", "2", "1"))),
                new SelectButton("MOVEMENT SPEED", new ArrayList<>(Arrays.asList("slow", "normal", "fast", "very fast"))),
                new SelectButton("SIGHT", new ArrayList<>(Arrays.asList("global", "7", "5", "3"))),
                new MenuBtn("Start")))
        ),"select");
        this.startingScreen=(StartingScreen) this.getComponents()[0];
        this.selectGame=(SelectGame) this.getComponents()[1];
    }
}

