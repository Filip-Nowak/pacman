package classes.units;

import classes.panels.Game;

import javax.swing.*;

public class Player extends Unit{
    public Player(int x, int y,int movementSpeed) {
        super(x, y,movementSpeed);
        ImageIcon[]images = new ImageIcon[8];
        for(int i=0;i<4;i++)
        {
            images[i*2]=Game.gameImages.get("player_stop");
        }
        images[1]= Game.gameImages.get("player_right");
        images[3]=Game.gameImages.get("player_down");
        images[5]=Game.gameImages.get("player_left");
        images[7]=Game.gameImages.get("player_up");
        setFaces(images);
        this.setIcon(faces.get("right1"));
    }

    @Override
    public void changeDirection(int direction) {
        super.changeDirection(direction);

        if(this.nextDirection==-1*this.direction)
        {
            this.direction=direction;
            changeFace();
        }

    }
    @Override
    protected void setFaces(ImageIcon[] imageIcons) {
        super.setFaces(imageIcons);
        this.faces.put("stop",Game.gameImages.get("player_stop"));
    }

    @Override
    protected void animateFaces() {
        if(direction==MOVE_STOP)
        {
            setIcon(faces.get("stop"));
            return;
        }
        super.animateFaces();
    }
}
