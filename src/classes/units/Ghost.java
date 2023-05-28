package classes.units;

import classes.map.GameMap;
import classes.panels.Game;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Ghost extends Unit{
    private final Random rng=new Random();
    private final LinkedList<Integer> moves;
    private static int ghostCounter=1;
    private final int id;
    private final ArrayList<ImageIcon> deadFaces=new ArrayList<>();
    public boolean isDead=false;
    public boolean stayInSpawn=true;
    public Timer sleep;
    private final int spawnY;
    private boolean moveUp;
    private boolean leaveSpawn=false;
    public void setLeaveSpawn(){
        leaveSpawn=true;
    }
    private boolean leave=false;
    private final int STARTING_MS;
    public int nextMs;
    public static void resetGhostCounter(){
        ghostCounter=1;
    }
    public Ghost(int x, int y, int movementSpeed) {
        super(x, y,movementSpeed);
        STARTING_MS=baseMs;
        nextMs=baseMs;
        moves=new LinkedList<>();
        this.direction=1;
        this.id=ghostCounter++;

        ImageIcon[] images = new ImageIcon[8];
            images[0]=Game.gameImages.get("ghost"+id+"_1right");
            images[1]=Game.gameImages.get("ghost"+id+"_2right");
            images[2]=Game.gameImages.get("ghost"+id+"_1down");
            images[3]=Game.gameImages.get("ghost"+id+"_2down");
            images[4]=Game.gameImages.get("ghost"+id+"_1left");
            images[5]=Game.gameImages.get("ghost"+id+"_2left");
            images[6]=Game.gameImages.get("ghost"+id+"_1up");
            images[7]=Game.gameImages.get("ghost"+id+"_2up");
        setFaces(images);
        this.deadFaces.add(new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+File.separator+"dead_ghost1.png"));
        this.deadFaces.add(new ImageIcon("src"+ File.separator+"images"+File.separator+"ghosts"+File.separator+File.separator+"dead_ghost2.png"));
        this.setIcon(faces.get("right1"));
        if(this.id==1)
        {
            stayInSpawn=false;
            spawnY= GameMap.ghostSpawnPoints.get(3)[1]*40;
        }
        else
            spawnY= GameMap.ghostSpawnPoints.get(this.id)[1]*40;
        moveUp= this.id != 4;
    }

    private void spawnAnimation(){
        changeFace();
        if(leaveSpawn)
            {
                if(this.y==spawnY)
                {
                    if(this.x==GameMap.getGateX()*40)
                    {
                        this.y-= STARTING_MS;
                        leave=true;
                        this.direction=MOVE_UP;
                    }else if(this.x-GameMap.getGateX()*40<0) {
                        this.direction=MOVE_RIGHT;
                        this.x+=STARTING_MS;

                    }else {
                        this.direction=MOVE_LEFT;
                        this.x-=STARTING_MS;
                    }
                }else if(leave){
                    if(this.y==(GameMap.getGateY()-1)*40){
                        stayInSpawn=false;
                        leaveSpawn=false;
                        leave=false;
                    }
                    else {
                        this.y-=STARTING_MS;
                    }
                }else{
                    if (y >= spawnY + 10)
                    {
                        this.direction=MOVE_UP;
                        moveUp = true;
                    }
                    else if (y <= spawnY - 10)
                    {
                        this.direction=MOVE_DOWN;
                        moveUp = false;
                    }
                    spawnMove();
                }
                this.setLocation(x,y);
            }else
            {
                if (y >= spawnY + 10)
                {
                    this.direction=MOVE_UP;
                    moveUp = true;
                }
                else if (y <= spawnY - 10)
                {
                    this.direction=MOVE_DOWN;
                    moveUp = false;
                }
                spawnMove();
            }
    }
    private void spawnMove() {
        if (moveUp)
        {
            this.y-=this.STARTING_MS;
        }
        else{
            this.y+=this.STARTING_MS;
        }
        this.setLocation(x,y);
    }

    @Override
    protected void animateFaces() {
        if(this.isDead)
        {

            if(firstFace)
            {
                setIcon(deadFaces.get(0));
            }else setIcon(deadFaces.get(1));
            firstFace=!firstFace;
            return;
        }

            super.animateFaces();
    }

    @Override
    public void move(GameMap map) {
        if(stayInSpawn){
            spawnAnimation();
            return;
        }
        if(x%40==0 & y%40==0)
        {
            if(nextMs!=baseMs)
            {
                baseMs=nextMs;
                setMs(baseMs);
            }
            changeDirection(map);
            changeFace();
        }
        super.move(map);
    }

    private void randomSort()
    {
        moves.clear();
        int x;
        for(int i=0;i<4;i++)
        {
            while(true)
            {
                x=rng.nextInt(4);
                if(!moves.contains(x-1))
                {
                    moves.add(x-1);
                    break;
                }
            }
        }
        for(int i=0;i<4;i++)
        {
            if(moves.get(i)<1)
                moves.set(i,moves.get(i)-1);
        }
        for(int i=0;i<4;i++)
        {
            if(this.direction==-1*moves.get(i))
            {
                moves.remove(i);
                break;
            }
        }
    }

    @Override
    protected void changeFace() {
        if(!isDead)
            super.changeFace();
    }

    public void changeDirection(GameMap map) {
        for(int d:moves)
        {
            if(checkWalls(d,map,x,y))
            {
                this.direction=d;
                break;
            }
        }
        randomSort();
    }

    public void kickGhost(int sight) {
        spawn(GameMap.ghostSpawnPoints.get(this.id)[0]*40,this.spawnY);
        this.isDead=false;
        this.stayInSpawn=true;
        if(sight!=0)
            this.setVisible(false);
    }

    public void becomeDead() {
        this.isDead=true;
    }

    public void becomeNormal() {
        this.isDead=false;
        changeFace();
            if (this.x % this.baseMs != 0) {
                if (this.direction == MOVE_LEFT) {
                    x += baseMs/2;
                } else if (this.direction == MOVE_RIGHT)
                    x -= baseMs/2;
                this.setLocation(x,y);
            }

            if (this.y % this.baseMs != 0) {
                if (this.direction == MOVE_UP) {
                    y += baseMs/2;
                } else if (this.direction == MOVE_DOWN)
                {
                    y -= baseMs/2;
                }
                this.setLocation(x,y);
            }



    }

    public int getId() {
        return id;
    }
}
