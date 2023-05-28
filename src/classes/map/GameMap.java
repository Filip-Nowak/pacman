package classes.map;

import classes.map.mapElements.MapElement;
import classes.map.mapElements.None;
import classes.map.mapElements.Wall;
import classes.panels.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameMap {
    //private char[][] mapChar;

    private MapElement[][] mapElements;

    private Scanner scn;
    private int maxPoints;
    private static int gateX;
    private static int gateY;

    public static int getGateX() {
        return gateX;
    }

    public static int getGateY() {
        return gateY;
    }

    public int getSpawnPointX() {
        return spawnPointX;
    }

    public int getSpawnPointY() {
        return spawnPointY;
    }

    private int spawnPointX;
    private int spawnPointY;
    public static final HashMap<Integer,Integer[]> ghostSpawnPoints=new HashMap<>();
    public static ArrayList<Integer[]> possibleCherries=new ArrayList<>();

    public GameMap(String mode)
    {
        maxPoints=0;
        File mapFile = new File("src" + File.separator + "maps" + File.separator + File.separator + "map2.txt");
        try
        {
            scn= new Scanner(mapFile);
        }catch(FileNotFoundException e)
        {
            System.out.println("Nie znalezniono pliku");
        }
        //setMapChar();
        setObjectMap(mode);
    }
    public MapElement getMapElement(int x,int y) {
        if(x==-1)
            x=18;
        if(x==-2)
            x=17;
        if(x==19)
            x=0;
        if(x==20)
            x=1;
        if(y==-1)
            y=21;
        if(y==22)
            y=0;
        return mapElements[y][x];
    }


    private void setObjectMap(String mode) {
        mapElements=new MapElement[22][19];
        char[] line=new char[19];
        for(int i=0;i<mapElements.length;i++)
        {
            scn.nextLine().getChars(0,line.length,line,0);
            for(int j=0;j<mapElements[i].length;j++)
            {
                if(line[j]=='#')
                {
                    mapElements[i][j]=new Wall('#');
                }else if(line[j]=='*' || line[j]=='&'){
                    if (!mode.equals(Game.FOLLOW)) {
                        if(!mode.equals(Game.CLASSIC) || line[j]=='*'){
                            mapElements[i][j]=new None("src"+ File.separator+"images"+File.separator+"gameElements"+File.separator+"point.png",'*');
                        }else{
                            mapElements[i][j]=new None("src"+ File.separator+"images"+File.separator+"gameElements"+File.separator+"superPoint.png",'&');
                        }
                        maxPoints++;
                    }else{
                        mapElements[i][j]=new None();
                        possibleCherries.add(new Integer[]{j,i});
                    }

                }else if(line[j]=='_') {
                    mapElements[i][j]=new None();
                }else if(line[j]=='@')
                {
                    mapElements[i][j]=new None();
                    spawnPointY=i;
                    spawnPointX=j;
                }else if(line[j]=='!'){
                    mapElements[i][j]=new Wall('!');
                    gateX=j;
                    gateY=i;
                }else{
                    try{
                        mapElements[i][j]=new None();
                        ghostSpawnPoints.put(Integer.parseInt(Character.toString(line[j])),new Integer[]{j,i});
                    }catch (NumberFormatException e)
                    {
                        System.out.println(line[j]);
                    }
                }
            }
        }
    }
    public void setWallImages(GameMap map){
        for(int i=0;i<mapElements.length;i++) {
            for (int j = 0; j < mapElements[i].length; j++) {
                if(mapElements[i][j] instanceof Wall)
                    ((Wall)mapElements[i][j]).setFaceImg(j,i,map);
            }
        }
    }

    public int getMaxPoints() {
        return maxPoints;
    }
}
