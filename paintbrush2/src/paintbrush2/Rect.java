package paintbrush2;


import java.awt.Color;


public class Rect extends Shape {

    public Rect(int x, int y, int w, int h, Color c, boolean dotted, boolean filled) {
        type = "rect";
        x1 = x;
        y1 = y;
        width = w;
        height = h;
        color = c;
        this.dotted = dotted;
        this.filled = filled;
    }

}
