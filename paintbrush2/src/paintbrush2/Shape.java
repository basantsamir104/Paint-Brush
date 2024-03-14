package paintbrush2;


import java.awt.Color;


public abstract class Shape {
    protected int x1,y1,x2,y2,width,height;
    protected String type;
    protected boolean dotted = false;
    protected boolean filled = false;
    protected Color color; 
    public String getShapeType(){
        return type;
    }
    public Color getShapeColor(){
        return color;
    }
    public boolean isDotted(){
        return dotted;
    }
    public boolean isFilled(){
        return filled;
    }
    public void addToArray(Shape ln){}
}
