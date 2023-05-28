package classes.units;

import classes.map.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public abstract class Unit extends JLabel{
    protected int x;
    protected int y;
    private int ms;
    protected int baseMs;

    public int getBaseMs() {
        return baseMs;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }
    final public static int MOVE_UP=1;
    final public static int MOVE_DOWN=-1;
    final public static int MOVE_RIGHT=2;
    final public static int MOVE_LEFT=-2;
    final public static int MOVE_STOP=0;
    private final static HashMap<Integer,String> directions=new HashMap<>();
    protected int nextDirection;
    protected int direction;
    public Timer timer;
    private String stringDirection;
    protected boolean firstFace;
    static {
        directions.put(MOVE_DOWN,"down");
        directions.put(MOVE_LEFT,"left");
        directions.put(MOVE_RIGHT,"right");
        directions.put(MOVE_UP,"up");
        directions.put(MOVE_STOP,"stop");
    }
    protected HashMap<String,ImageIcon> faces=new HashMap<>();
    public Unit(int x,int y,int movementSpeed)
    {
        this.setPreferredSize(new Dimension(40,40));
        baseMs= movementSpeed;
        this.ms=baseMs;
        spawn(x,y);
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateFaces();
            }
        });
    }
    protected void animateFaces(){
        if(firstFace)
        {
            setIcon(faces.get(stringDirection+"2"));
        }else setIcon(faces.get(stringDirection+"1"));
        firstFace=!firstFace;
    }

    protected void setFaces(ImageIcon [] images) {
        this.faces.put("right1",images[0]);
        this.faces.put("right2",images[1]);
        this.faces.put("down1",images[2]);
        this.faces.put("down2",images[3]);
        this.faces.put("left1",images[4]);
        this.faces.put("left2",images[5]);
        this.faces.put("up1",images[6]);
        this.faces.put("up2",images[7]);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public void spawn(int x,int y)
    {
        this.x=x;
        this.y=y;
        this.setLocation(new Point(x,y));
    }
    public int getY() {
        return y;
    }


    public void changeDirection(int direction)
    {
        this.nextDirection=direction;
    }

    public void move(GameMap map)
    {
        if(x<=-40)
        {
            x=760;
            this.setLocation(x,y);
        }else if(x>=760)
        {
            x=-40;
            this.setLocation(x,y);
        }
        int noneDirection=0;
        if(x%40==0 && y%40==0)
        {
            if(checkWalls(nextDirection, map,this.x,this.y))
            {
                direction=nextDirection;
                changeFace();
            }
        }
        switch (direction) {
            case MOVE_UP -> {
                if (checkWalls(direction, map, this.x, this.y)) {
                    setY(getY() - ms);
                    this.setLocation(new Point(x, y));
                    noneDirection++;
                }
            }
            case MOVE_DOWN -> {
                if (checkWalls(direction, map, this.x, this.y)) {
                    setY(getY() + ms);
                    this.setLocation(new Point(x, y));
                    noneDirection++;
                }
            }
            case MOVE_LEFT -> {
                if (checkWalls(direction, map, this.x, this.y)) {
                    {
                        setX(getX() - ms);
                        this.setLocation(new Point(x, y));
                        noneDirection++;

                    }

                }
            }
            case MOVE_RIGHT -> {
                if (checkWalls(direction, map, this.x, this.y)) {
                    setX(getX() + ms);
                    this.setLocation(new Point(x, y));
                    noneDirection++;
                }
            }
            case MOVE_STOP -> setX(getX());
        }
        if(noneDirection==0)
        {
            this.direction=MOVE_STOP;
        }
    }
    protected void changeFace()
    {
        if(directions.get(direction).equals(stringDirection))
        {
            return;
        }
        switch (direction) {
            case MOVE_UP -> {
                {
                    this.setIcon(faces.get("up2"));
                    this.stringDirection = "up";
                }

            }
            case MOVE_DOWN -> {
                {
                    this.stringDirection = "down";
                    this.setIcon(faces.get("down2"));
                }

            }
            case MOVE_LEFT -> {
                {
                    this.stringDirection = "left";
                    this.setIcon(faces.get("left2"));
                }

            }
            case MOVE_RIGHT -> {
                {
                    this.stringDirection = "right";
                    this.setIcon(faces.get("right2"));
                }

            }
            case MOVE_STOP -> {
                this.stringDirection = "stop";
                this.setIcon(faces.get("stop"));
            }
        }
    }



    protected boolean checkWalls(int direction,GameMap map,int x,int y) {
            if(map.getMapElement(x/40,(y/40)+1).getFaceChar()!='#' && direction==MOVE_DOWN)
                return true;
            else if((map.getMapElement(x/40,(y/40)-1).getFaceChar()!='#' || y%40!=0)&& direction==MOVE_UP)
                return true;
            else if(map.getMapElement((x/40)+1,y/40).getFaceChar()!='#' && direction==MOVE_RIGHT)
                return true;
            else return ((map.getMapElement((x / 40) - 1, y / 40).getFaceChar() != '#' || x%40!=0) && direction == MOVE_LEFT);
    }
}
