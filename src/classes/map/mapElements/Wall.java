package classes.map.mapElements;

import classes.map.GameMap;

import javax.swing.*;
import java.io.File;

public class Wall extends MapElement{
    public Wall(char c)
    {
        this.setFaceChar(c);
    }
    public void setFaceImg(int x, int y, GameMap map) {
        if(this.getFaceChar()=='!'){
            this.setFaceChar('#');
            this.setFaceImg(new ImageIcon("src"+ File.separator+"images"+File.separator+"walls"+File.separator+"gate.png").getImage());
        }else{
        String file="src/images/walls/wall_";
        //super(new ImageIcon("src/images/wall1.png").getImage(),'#');
        if((map.getMapElement(x-1,y).getFaceChar()=='#' || map.getMapElement(x-1,y).getFaceChar()=='!') && x!=0){
            file+="l";
        }
        if((map.getMapElement(x+1,y).getFaceChar()=='#' || map.getMapElement(x+1,y).getFaceChar()=='!') && x!=18){
            file+="r";
        }
        if((map.getMapElement(x,y-1).getFaceChar()=='#'  || map.getMapElement(x,y-1).getFaceChar()=='!') && y!=0){
            file+="u";
        }
        if((map.getMapElement(x,y+1).getFaceChar()=='#' || map.getMapElement(x,y+1).getFaceChar()=='!') &&y!=21){
            file+="d";
        }
        file+=".png";
        this.setFaceImg(new ImageIcon(file).getImage());
    }}
}
