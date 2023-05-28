package classes.map.mapElements;

import java.awt.*;

public abstract class MapElement {
    MapElement(){

    }
    public void setFaceImg(Image faceImg) {
        this.faceImg = faceImg;
    }

    private Image faceImg;

    public void setFaceChar(char faceChar) {
        this.faceChar = faceChar;
    }

    private char faceChar;

    public char getFaceChar() {
        return faceChar;
    }

    public MapElement(Image i, char c)
    {
        this.faceImg=i;
        this.faceChar=c;
    }

    public Image getFaceImg() {
        return faceImg;
    }
}
