package classes.panels;

import classes.buttons.MenuBtn;
import classes.map.GameMap;
import classes.units.Ghost;
import classes.units.Player;
import classes.units.Unit;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;

public class Game extends JPanel {
    private Board board;
    public static final String CLASSIC="classic";
    public static final String TIME="time trial";
    public static final String FOLLOW="follow";
    private Stats stats;
    private final int MAX_POINTS;
    private boolean exit=false;
    private int gainedPoints;
    private boolean stop;
    private final GameThread gameThread;
    private WinnerScreen winnerScreen;
    private LooserScreen looserScreen;
    public void exitGame(){
        exit=true;
        if(superTimer!=null)
            superTimer.stop();
        //board.stopTimers();
    }

    public GameThread getGameThread() {
        return gameThread;
    }

    private Pause pausePanel;
    private PauseBtn pauseBtn;
    private final Timer superTimer;
    private int amountOfGhosts;
    private int amountOfLives;
    private int movementSpeed;
    private String mode;
    private int sight;
    private int playerLives;
    private  int pointScore;
    private Timer gameTime;
    private int seconds=0;
    private int minutes=0;
    private int cherryCounter;
    private void clickPause(){
        pauseBtn.doClick();
    }
    public static final HashMap<String,ImageIcon> gameImages;
    static {
        gameImages=new HashMap<>();
        gameImages.put("player_down",new ImageIcon("src"+ File.separator+"images"+ File.separator+"player"+ File.separator+"player_down.png"));
        gameImages.put("player_left",new ImageIcon("src"+ File.separator+"images"+ File.separator+"player"+ File.separator+"player_left.png"));
        gameImages.put("player_right",new ImageIcon("src"+ File.separator+"images"+ File.separator+"player"+ File.separator+"player_right.png"));
        gameImages.put("player_stop",new ImageIcon("src"+ File.separator+"images"+ File.separator+"player"+ File.separator+"player_stop.png"));
        gameImages.put("player_up",new ImageIcon("src"+ File.separator+"images"+ File.separator+"player"+ File.separator+"player_up.png"));
        for(int i=1;i<5;i++){
            gameImages.put("ghost"+i+"_1right",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_1right.png"));
            gameImages.put("ghost"+i+"_2right",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_2right.png"));
            gameImages.put("ghost"+i+"_1left",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_1left.png"));
            gameImages.put("ghost"+i+"_2left",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_2left.png"));
            gameImages.put("ghost"+i+"_1up",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_1up.png"));
            gameImages.put("ghost"+i+"_2up",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_2up.png"));
            gameImages.put("ghost"+i+"_1down",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_1down.png"));
            gameImages.put("ghost"+i+"_2down",new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+"g"+i+File.separator+"ghost_2down.png"));
        }
        gameImages.put("win",new ImageIcon("src"+ File.separator+"images"+File.separator+"info"+File.separator+"win.png"));
        gameImages.put("lost",new ImageIcon("src"+ File.separator+"images"+File.separator+"info"+File.separator+"lost.gif"));
        gameImages.put("live",new ImageIcon("src"+ File.separator+"images"+File.separator+"gameElements"+File.separator+"live.png"));
        gameImages.put("cherry",new ImageIcon("src"+File.separator+"images"+File.separator+"gameElements"+File.separator+"cherry.png"));
    }
    private void setMode(String mode,int amountOfGhosts,int amountOfLives,int movementSpeed, int sight){
        this.mode=mode;
        this.amountOfGhosts=amountOfGhosts;
        this.amountOfLives=amountOfLives;
        this.movementSpeed=movementSpeed;
        this.sight=sight;
    }
    private int x=0;
    private final int time;
    public Game(String mode,int amountOfGhosts,int amountOfLives,int movementSpeed, int sight)
    {
        Ghost.resetGhostCounter();
        setMode(mode,amountOfGhosts,amountOfLives,movementSpeed,sight);
        playerLives=amountOfLives;
        if(this.movementSpeed>7) {
            time=8000;
        }
        else {
            time=15000;
        }
        superTimer=new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!stop)
                {
                    x+=500;
                }
                if(x>=time)
                {
                    x=0;
                    leaveSuperMode();
                }
            }
        });
        initComponents();
        MAX_POINTS = board.getMap().getMaxPoints();
        gameThread=new GameThread();
        if(this.mode.equals(TIME)){
            gameTime=new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String msg="";
                    seconds++;
                    if(seconds==60)
                    {
                        minutes++;
                        seconds=0;
                    }
                    if(minutes<10)
                        msg="0";
                    msg+=minutes+":";
                    if(seconds<10)
                        msg+="0";
                    msg+=seconds;
                    stats.setScoreValue(msg);
                }
            });
        }
    }
    private void initComponents(){
        board=new Board();
        board.setBackground(Color.BLACK);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.stats=new Stats();
        pausePanel=new Pause(new ArrayList<>(Arrays.asList(new MenuBtn("resume"),new MenuBtn("restart"),new MenuBtn("exit"))));
        pauseBtn= new PauseBtn();
        winnerScreen=new WinnerScreen(new ArrayList<>(Arrays.asList(EndingScreen.createPanel("GG WP",gameImages.get("win"),Color.GREEN),new JLabel("Score: "),new MenuBtn("Exit"))));
        looserScreen= new LooserScreen(new ArrayList<>(Arrays.asList(EndingScreen.createPanel("PRZEGRALES", gameImages.get("lost"), Color.RED), new MenuBtn("retry"), new MenuBtn("exit"))));
        stats.pause.add(pauseBtn);
        board.setPreferredSize(new Dimension(760,880));
        board.setBounds(0,60,760,880);
        stats.setBounds(0,0,760,60);
        add(winnerScreen);
        add(looserScreen);
        add(pausePanel);
        add(stats);
        add(board);
        this.setLayout(null);
    }

    public int getAmountOfGhosts() {
        return amountOfGhosts;
    }

    public int getSight() {
        return sight;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getAmountOfLives() {
        return amountOfLives;
    }

    public String getMode() {
        return mode;
    }

    private class Pause extends Options {

        public Pause(ArrayList<Component> components) {
            super(components);
            this.setBounds(130, 100, 500, 700);
            this.setBackground(Color.BLACK);
            this.setVisible(false);
            this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 15));
            this.add(components.get(0));
            this.add(components.get(1));
            this.add(components.get(2));
            addActions((MenuBtn) components.get(2), Game.this,"exit");
            ((MenuBtn)components.get(0)).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    resumeGame();

                }
            });
            addActions((MenuBtn) components.get(1),Game.this,"restart");
        }
        @Override
        public void putKeys() {
            super.putKeys();
            this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"escape");
            this.getActionMap().put("escape", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getFirstButton().doClick();
                }
            });
        }

    }
    private static class EndingScreen extends Options{
        public EndingScreen(ArrayList<Component> components)
        {
            super(components);
            this.setBounds(80, 50, 600, 800);
            this.setBackground(Color.BLACK);
            this.setFocusable(false);
            this.setVisible(false);
        }

        private static JPanel createPanel(String text,ImageIcon file,Color fontColor) {
            JPanel panel =new JPanel();
            panel.setBackground(Color.BLACK);
            JLabel img = new JLabel();
            JLabel textLabel = new JLabel(text);
            textLabel.setForeground(fontColor);
            textLabel.setFont(new Font("Arial",Font.BOLD,40));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            textLabel.setBackground(Color.BLACK);
            textLabel.setOpaque(true);
            img.setPreferredSize(new Dimension(220,240));
            textLabel.setPreferredSize(new Dimension(320,200));
            panel.setPreferredSize(new Dimension(540,200));
            img.setIcon(file);
            panel.add(img);
            panel.add(textLabel);
            return panel;
        }

    }
    private class LooserScreen extends EndingScreen{
        public LooserScreen(ArrayList<Component>components){
            super(components);
            this.setBorder(BorderFactory.createLineBorder(Color.RED, 30));
            initComponents();
        }
        private void initComponents()
        {
            add(componentsList.get(0));
            add(componentsList.get(1));
            add(componentsList.get(2));
            addActions((MenuBtn) componentsList.get(2),"start");
            addActions((MenuBtn) componentsList.get(1),Game.this,"restart");
        }
    }
    private class WinnerScreen extends EndingScreen{
        JLabel endScore;
        public WinnerScreen(ArrayList<Component>components){
            super(components);
            this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 30));
            initComponents();
            this.pos=2;
        }

        private void initComponents() {
            ((JPanel)componentsList.get(0)).getComponent(1).setFont(new Font("Arial",Font.BOLD,80));
            add(componentsList.get(0));
            endScore=(JLabel) componentsList.get(1);
            endScore.setPreferredSize(new Dimension(500,100));
            endScore.setFont(new Font("Arial",Font.BOLD,50));
            endScore.setForeground(Color.GREEN);
            endScore.setHorizontalAlignment(SwingConstants.CENTER);
            add(endScore);
            add(getFirstButton());
            addActions(getFirstButton(),"start");
            if(mode.equals(TIME))
                endScore.setText("Time: ");
        }


        private void addScoreInfo() {
            switch (mode) {
                case CLASSIC, FOLLOW -> this.endScore.setText(this.endScore.getText() + pointScore);
                case TIME -> this.endScore.setText(this.endScore.getText() + stats.getTime());
            }
        }
    }
    private void resumeGame(){
        stop=false;
        board.player.timer.start();
        for (Ghost ghost : board.ghosts) {
            ghost.timer.start();
            if(ghost.stayInSpawn)
            {
                ghost.sleep.start();
            }
        }
        if(mode.equals(TIME))
            gameTime.start();
        board.putKeys();
        board.repaint();
    }
    public void stopGame(){
        stop=true;
        for(Ghost ghost:board.ghosts)
        {
            ghost.timer.stop();
            if(ghost.stayInSpawn)
            {
                ghost.sleep.stop();
            }
        }
        if(mode.equals(TIME)){
            gameTime.stop();
        }
        board.player.timer.stop();
    }
    private class PauseBtn extends JButton
    {
        public PauseBtn(){
            this.setPreferredSize(new Dimension(50,50));
            this.setIcon(new ImageIcon("src"+File.separator+"images"+File.separator+"buttons"+File.separator+"pause.png"));
            this.setFocusPainted(false);
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopGame();
                    pausePanel.resetPos();
                    pausePanel.setVisible(true);
                    pausePanel.setFocusable(true);
                    board.setFocusable(false);
                    board.removeKeys();
                    pausePanel.getFirstButton().requestFocusInWindow();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            });
        }
    }
    private void checkBoard(){
        int result=board.checkPoints();
        switch (result) {
            case 1 -> {
                gainedPoints++;
                if (mode.equals(CLASSIC)) {
                    pointScore += 10;
                }
            }
            case 2 -> {
                gainedPoints++;
                pointScore += 50;
                enterSuperMode();
            }
            case 3 -> {
                board.setRandomCherry();
                pointScore++;
                cherryCounter++;
                if (cherryCounter % 10 == 0) {
                    if (board.ghosts[0].getBaseMs() == 40) {
                        return;
                    }
                    int ms = board.ghosts[0].getBaseMs() + 1;
                    while (40 % ms != 0) {
                        ms++;
                    }
                    for (int i = 0; i < board.ghosts.length; i++) {
                        board.ghosts[i].nextMs = ms;
                    }
                }
            }
        }
        if(!mode.equals(TIME))
            stats.setScoreValue(Integer.toString(pointScore));
    }

    private void enterSuperMode() {
        superTimer.stop();
        x=0;
        superTimer.start();
        board.setBackground(Board.superColor);
        board.startTimers();
        board.killedGhosts=0;
        for(Ghost ghost:board.ghosts)
        {
            ghost.becomeDead();
        }
    }

    private void leaveSuperMode() {
        board.stopTimers();
        board.changeColorTimer.stop();
        superTimer.stop();
        board.setBackground(Board.normalColor);
        for(Ghost ghost:board.ghosts)
        {
            ghost.becomeNormal();
        }
    }

    private int inGame() {

        while (true) {
            try {
                if (!stop) {
                    Thread.sleep(30);
                    tick();
                    if (gainedPoints == MAX_POINTS && !mode.equals(FOLLOW)) {
                        return 1;
                    }
                    else if(checkLoose()) {
                        return 2;
                    }
                } else {
                    Thread.sleep(10);
                    if (exit)
                        return 0;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private boolean checkLoose()
    {
        Ghost ghost=board.checkTouch();
        if(ghost!=null){
            if(ghost.isDead){
                pointScore+=((++board.killedGhosts)*200);
                stats.setScoreValue(Integer.toString(pointScore));
                ghost.kickGhost(sight);
                sleepInSpawn(2000/ghost.getBaseMs(),ghost);
            }
            else{
                leaveSuperMode();
                return true;
            }
        }
        return false;
    }
    private void startGame()
    {
        repaint();
        int gameResult;
        loadGame();
        gameResult=inGame();
        if(gameResult==1)
        {
            if(mode.equals(TIME))
                gameTime.stop();
            winGame();
        } else if(gameResult==2)
        {

            if(playerLives!=1)
            {
                stats.removeLive();
                stats.repaint();
                stopGame();
                playerLives--;
                Ghost.resetGhostCounter();
                board.player.spawn(board.getMap().getSpawnPointX()*40, board.getMap().getSpawnPointY()*40);
                for(Ghost ghost:board.ghosts)
                {
                    ghost.spawn(GameMap.ghostSpawnPoints.get(ghost.getId())[0]*40,GameMap.ghostSpawnPoints.get(ghost.getId())[1]*40);
                    if(ghost.getId()!=1){
                        ghost.sleep.stop();
                        ghost.stayInSpawn=true;
                    }
                }
                stop=false;
                startGame();
            }else{
                if(mode.equals(FOLLOW)){
                    winGame();
                    return;
                }else if(mode.equals(TIME)){
                    gameTime.stop();
                }
                    lostGame();
            }
        }
    }

    private void lostGame() {
        stopGame();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        board.removeKeys();
        looserScreen.setVisible(true);
        looserScreen.getFirstButton().requestFocusInWindow();
    }
    private void winGame(){
        stopGame();
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        board.removeKeys();
        winnerScreen.addScoreInfo();
        winnerScreen.setVisible(true);
        winnerScreen.getFirstButton().requestFocusInWindow();

    }

    private void loadGame() {
        try {
            for(Ghost ghost:board.ghosts)
            {
                ghost.setVisible(true);
            }
            board.player.setVisible(true);
            Thread.sleep(1000);
            if(mode.equals(FOLLOW)){
                board.setRandomCherry();
            }
            board.player.timer.start();
            for(Ghost ghost:board.ghosts)
            {
                ghost.timer.start();
                if(ghost.getId()!=1) {
                    sleepInSpawn((ghost.getId()-1)*2500/ movementSpeed,ghost);
                }
            }if(mode.equals(TIME))
                gameTime.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void tick() {
        board.updateBoard();
        checkBoard();
        repaint();
    }
    public class GameThread extends Thread{
        @Override
        public void run() {
            startGame();
        }
    }
    private void sleepInSpawn(int time,Ghost ghost){
        ghost.sleep=new Timer(10, new ActionListener() {
            int x=0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!stop)
                    x+=10;
                if(x>=time)
                {
                    ghost.setLeaveSpawn();
                    ghost.sleep.stop();
                }
            }
        });
        ghost.sleep.start();
    }
    public class Board extends JPanel {
        private GameMap map;
        public int killedGhosts;
        public GameMap getMap() {
            return map;
        }
        public Player player;
        public Ghost [] ghosts;
        private Integer[] cherryPos;
        private Board()
        {
            this.setVisible(true);
            this.setBackground(null);
            this.setFocusable(true);
            this.requestFocusInWindow();
            map=new GameMap(mode);
            map.setWallImages(map);
            putKeys();
            player=(Player) add( new Player(map.getSpawnPointX()*40,map.getSpawnPointY()*40,movementSpeed));
            ghosts = new Ghost[amountOfGhosts];
            for(int i=0;i<ghosts.length;i++)
            {
                ghosts[i]=(Ghost) add(new Ghost(GameMap.ghostSpawnPoints.get(i+1)[0]*40,GameMap.ghostSpawnPoints.get(i+1)[1]*40,movementSpeed));
            }
            changeColorTimer=new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!stop)
                    {
                        x+=500;
                    }
                    if(x>=time-3162)
                    {
                        x=0;
                        setTimer(512);
                        swapColorTimer.start();
                        changeColorTimer.stop();
                    }
                    if(exit)
                        changeColorTimer.stop();
                }
            });
        }
        public void putKeys(){
            addKey("up",KeyEvent.VK_UP);
            addKey("down",KeyEvent.VK_DOWN);
            addKey("left",KeyEvent.VK_LEFT);
            addKey("right",KeyEvent.VK_RIGHT);
            addKey("escape",KeyEvent.VK_ESCAPE);
        }
        public void removeKeys(){
            this.getActionMap().clear();
        }
        private void keyHandler(int key) {
            if(key==KeyEvent.VK_DOWN)
            {
                player.changeDirection(Unit.MOVE_DOWN);
            }else if(key==KeyEvent.VK_UP)
            {
                player.changeDirection(Unit.MOVE_UP);
            }else if(key==KeyEvent.VK_RIGHT)
            {
                player.changeDirection(Unit.MOVE_RIGHT);
            }else if(key==KeyEvent.VK_LEFT)
            {
                player.changeDirection(Unit.MOVE_LEFT);
            }else if(key==KeyEvent.VK_ESCAPE)
            {
                clickPause();
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            initMap(g);
        }
        private void initMap(Graphics g) {
            for(int i=0;i<22;i++)
            {
                for(int j=0;j<19;j++)
                {
                    if(sight!=0){
                        if(mode.equals(Game.FOLLOW)){
                            if(cherryPos!=null){
                                if(i==cherryPos[1] && j==cherryPos[0] && (j-((player.getX()+20)/40)<sight &&j-((player.getX()+20)/40)>-sight&&i-((player.getY()+20)/40)<sight && i-((player.getY()+20)/40)>-sight)){
                                    g.drawImage(Game.gameImages.get("cherry").getImage(),j*40,i*40,null);
                                }
                            }
                        }
                        if(j-((player.getX()+20)/40)<sight &&j-((player.getX()+20)/40)>-sight&&i-((player.getY()+20)/40)<sight && i-((player.getY()+20)/40)>-sight)
                            g.drawImage(map.getMapElement(j,i).getFaceImg(),j*40,i*40,null);
                        else
                        {
                            g.setColor(Color.DARK_GRAY);
                            g.drawRect(j*40,i*40,40,40);
                            g.fillRect(j*40, i*40, 40, 40);
                        }
                    }else
                    {
                        if(mode.equals(Game.FOLLOW)){
                            if(cherryPos!=null)
                            {
                                if(i==cherryPos[1] && j==cherryPos[0]){
                                    g.drawImage(Game.gameImages.get("cherry").getImage(),j*40,i*40,null);
                                }
                                else {
                                    g.drawImage(map.getMapElement(j,i).getFaceImg(),j*40,i*40,null);
                                }
                            }else
                                g.drawImage(map.getMapElement(j,i).getFaceImg(),j*40,i*40,null);
                        }else
                            g.drawImage(map.getMapElement(j,i).getFaceImg(),j*40,i*40,null);
                    }
                }
            }
        }
        public Ghost checkTouch(){
            for(Ghost ghost:ghosts)
            {
                if(player.getX()-ghost.getX()>-25&&player.getX()-ghost.getX()<25&&player.getY()-ghost.getY()>-25&&player.getY()-ghost.getY()<25)
                {
                    return ghost;
                }
            }
            return null;
        }




        private void addKey(String name,int key)
        {
            this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key,0),name);
            this.getActionMap().put(name, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    keyHandler(key);
                }
            });
        }

        public void setRandomCherry(){
            Random rand=new Random();
            cherryPos=GameMap.possibleCherries.get(rand.nextInt(GameMap.possibleCherries.size()-1));
        }
        public int checkPoints(){
            if(!mode.equals(Game.FOLLOW)){
                if(checkHitBox('*'))
                {
                    ((map.getMapElement((player.getX()+20) /40,(player.getY()+20)/40))).setFaceChar('_');
                    ((map.getMapElement((player.getX()+20)/40, (player.getY()+20)/40))).setFaceImg(new ImageIcon("").getImage());
                    repaint();
                    return 1;

                }else if(checkHitBox('&'))
                {
                    ((map.getMapElement((player.getX()+20) /40,(player.getY()+20)/40))).setFaceChar('_');
                    ((map.getMapElement((player.getX()+20)/40, (player.getY()+20)/40))).setFaceImg(new ImageIcon("").getImage());
                    repaint();
                    return 2;
                }
            }else {
                if(checkHitBox('c')){
                    return 3;
                }
                repaint();
            }
            return 0;
        }
        boolean checkHitBox(char c){
            if(c=='c'){
                if((player.getX()+20)/40==cherryPos[0] && (player.getY()+20)/40==cherryPos[1])
                    return true;
            }
            return map.getMapElement((player.getX() + 20) / 40, (player.getY() + 20) / 40).getFaceChar() == c;
        }

        public void updateBoard() {
            player.move(map);
            for (Ghost ghost : ghosts) {
                if (ghost.isDead)
                    ghost.setMs(ghost.getBaseMs() / 2);
                else
                    ghost.setMs(ghost.getBaseMs());
                if (sight != 0)
                    ghost.setVisible(((ghost.getX() + 20) / 40) - ((player.getX() + 20) / 40) < sight && ((ghost.getX() + 20) / 40) - ((player.getX() + 20) / 40) > -sight && ((ghost.getY() + 20) / 40) - ((player.getY() + 20) / 40) < sight && ((ghost.getY() + 20) / 40) - ((player.getY() + 20) / 40) > -sight);
                ghost.move(map);
            }
        }


        public static final Color superColor = new Color(49, 2, 110);
        public static final Color normalColor = Color.BLACK;
        private boolean color = false;
        public Timer swapColorTimer;
        public Timer changeColorTimer;
        public void startTimers(){
            changeColorTimer.stop();
            if(swapColorTimer!=null)
                swapColorTimer.stop();
            timeChangeCounter=0;
            x=0;
            changeColorTimer.start();
        }
        public void swapColors(){
            if(color){
                this.setBackground(superColor);
                color=false;
            }else{
                color=true;
                this.setBackground(normalColor);
            }
        }
        public int timeChangeCounter=0;
        private int x=0;
        void setTimer(int time){
            swapColorTimer =new Timer(time, new ActionListener() {
                int n=0;
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!stop)
                    {
                        swapColors();
                        if(timeChangeCounter==6)
                        {
                            swapColorTimer.stop();
                            timeChangeCounter=0;
                        }
                        n++;
                        if(n==3){
                            swapColorTimer.stop();
                            setTimer( (time/2));
                            swapColorTimer.start();
                            timeChangeCounter++;
                        }
                    }
                    if(exit)
                        swapColorTimer.stop();

                }
            });
        }
        public void stopTimers() {
            if(swapColorTimer!=null)
                swapColorTimer.stop();
        }
    }
    public class Stats extends JPanel {
        JPanel score;
        JPanel lives;
        JPanel pause;
        JLabel scoreValue;
        public Stats()
        {
            this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            this.setBackground(null);
            this.setPreferredSize(new Dimension(776,60));
            lives=new JPanel();
            pause=new JPanel();
            score=new JPanel();
            scoreValue=new JLabel();
            String name;
            if(mode.equals(Game.TIME))
                name="Time";
            else
                name="Score";
            JLabel scoreText=new JLabel(name);
            score.setPreferredSize(new Dimension(350,60));
            lives.setPreferredSize(new Dimension(200,60));
            pause.setPreferredSize(new Dimension(200,60));
            pause.setLayout(new FlowLayout(FlowLayout.RIGHT,0,5));
            score.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
            pause.setBackground(Color.BLACK);
            score.setBackground(Color.BLACK);
            lives.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
            lives.setBackground(Color.BLACK);
            JLabel liveText=new JLabel("LIVES");
            liveText.setPreferredSize(new Dimension(200,10));
            liveText.setForeground(Color.YELLOW);
            liveText.setHorizontalAlignment(SwingConstants.CENTER);
            scoreText.setForeground(Color.YELLOW);
            scoreText.setFont(new Font("Arial",Font.BOLD,20));
            scoreText.setPreferredSize(new Dimension(350,20));
            scoreText.setHorizontalAlignment(SwingConstants.CENTER);
            scoreText.setVerticalAlignment(SwingConstants.TOP);
            scoreValue.setFont(new Font("Arial",Font.BOLD,20));
            scoreValue.setPreferredSize(new Dimension(350,20));
            scoreValue.setHorizontalAlignment(SwingConstants.CENTER);
            scoreValue.setVerticalAlignment(SwingConstants.TOP);
            scoreValue.setForeground(Color.YELLOW);
            score.add(scoreText);
            score.add(scoreValue);
            lives.add(liveText);
            for(int i=0;i<amountOfLives;i++)
            {
                ((JLabel)lives.add(new JLabel())).setIcon(Game.gameImages.get("live"));
            }
            add(lives);
            add(score);
            add(pause);
            if(mode.equals(Game.TIME))
            {
                setScoreValue("00:00");
            }else{
                setScoreValue("0");
            }
        }

        private void setScoreValue(String scoreTextValue) {
            this.scoreValue.setText(scoreTextValue);
        }

        private void removeLive(){
            lives.remove(lives.getComponents().length-1);
        }
        private String getTime(){
            return this.scoreValue.getText();
        }
    }

}
