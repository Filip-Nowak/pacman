package classes.map.mapElements;

import javax.swing.*;

public class None extends MapElement{
    public None()
    {
        super(new ImageIcon().getImage(),'_');
    }
    public None(String fileName,char c)
    {
        super(new ImageIcon(fileName).getImage(),c);
    }
}
