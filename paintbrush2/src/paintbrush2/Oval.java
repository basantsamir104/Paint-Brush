package paintbrush2;


import java.awt.Color;


public class Oval extends Shape {

    public Oval(int x, int y, int w, int h, Color c, boolean dotted, boolean filled) {
        type = "oval";
        x1 = x;
        y1 = y;
        width = w;
        height = h;
        color = c;
        this.dotted = dotted;
        this.filled = filled;
    }

}
