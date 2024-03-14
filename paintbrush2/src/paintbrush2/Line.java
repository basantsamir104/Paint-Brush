package paintbrush2;


import java.awt.Color;


public class Line extends Shape {

   public Line(int x1, int y1, int x2, int y2, Color c, boolean dotted) {
            type = "line";
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            color = c;
            this.dotted = dotted;
        }
}
