package paintbrush2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MyPanel extends JPanel {

    List<Shape> shapes; //list of type Shape to store all shapes lines rect oval
    Color current = Color.white; //color for the shape ure currently drawing
    String[] colors= {"White","Red","Green","Blue"};
    JComboBox myColors=new JComboBox();
    JButton rect, oval, line;
    JButton freeh, clear, undo, eraser;
    JButton save, open;
    JCheckBox dot, fill;
    float[] dPattern = {5.0f, 5.0f}; //parameter for stroke (for dotted)
    BasicStroke d = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dPattern, 0.0f); //create dotted stroke
    boolean dotted = false; //flag for dotted
    boolean filled = false; //flag for filled
    boolean free = false; //flag for freehand
    boolean cleared = false; //flag for clear all
    boolean undone = false; //flag for undo
    boolean erased = false; //flad for eraser
    int x1, x2, y1, y2, w, h; //to draw current shape
    String shape = "line"; //flag for deciding shape
    

    public MyPanel() {
        shapes = new ArrayList<>();
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        File file = new File("myImg.png");
        
        for(int i=0; i<colors.length;i++)
            myColors.addItem(colors[i]);

        myColors.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //current = Color.((JComboBox)e.getSource()).getSelectedItem();
                JComboBox cb=(JComboBox)e.getSource();
                String myColor=(String)cb.getSelectedItem();
                switch(myColor){
                    case "White":
                        current = Color.WHITE;
                        break;
                    case "Red":
                        current = Color.RED;
                        break;
                    case "Green":
                        current = Color.GREEN;
                        break;
                    case "Blue":
                        current = Color.BLUE;
                        break;
                }
            }
        });
        

        line = new JButton("Line");
        line.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shape = "line";
                resetFlagsandColor();
            }
        });

        rect = new JButton("Rect");
        rect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shape = "rect";
                resetFlagsandColor();
            }
        });

        oval = new JButton("Oval");
        oval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shape = "oval";
                free = false;
                resetFlagsandColor();

            }
        });

        dot = new JCheckBox("Dotted");
        dot.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    dotted = true;
                } else {
                    dotted = false;
                }
            }
        });

        fill = new JCheckBox("Filled");
        fill.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        });

        freeh = new JButton("FreeHand");
        freeh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                free = true;
                shape = "freehand";
                if (erased == true) {
                    erased = false;
                    current = Color.white;
                }
            }
        });

        clear = new JButton("Clear All");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleared = true;
                shapes.clear();
                updateUI();
            }
        });

        undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!shapes.isEmpty()) {
                    undone = true;
                    shapes.remove(shapes.size() - 1);
                    updateUI();
                }
            }
        });

        eraser = new JButton("Eraser");
        eraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erased = true;
                shape = "eraser";
            }
        });

        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                        BufferedImage buff = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = buff.createGraphics();

                        for (Shape s : shapes) {
                            g2d.setStroke(new BasicStroke());
                            g2d.setColor(s.getShapeColor()); //set shape color
                            if (s.isFilled() == true) //to check if it was drawn in filled
                            {
                                switch (s.getShapeType()) //check which shape
                                {
                                    case "line" ->
                                        g2d.drawLine(s.x1, s.y1, s.x2, s.y2);
                                    case "rect" ->
                                        g2d.fillRect(s.x1, s.y1, s.width, s.height);
                                    case "oval", "eraser" ->
                                        g2d.fillOval(s.x1, s.y1, s.width, s.height);
                                }
                            } else {
                                if (s.isDotted()) //check if dotted
                                {
                                    g2d.setStroke(d); //set stroke
                                }
                                switch (s.getShapeType()) //check which shape
                                {
                                    case "line" ->
                                        g2d.drawLine(s.x1, s.y1, s.x2, s.y2);
                                    case "rect" ->
                                        g2d.drawRect(s.x1, s.y1, s.width, s.height);
                                    case "oval" ->
                                        g2d.drawOval(s.x1, s.y1, s.width, s.height);
                                }
                            }
                        }

                        g2d.dispose();
                        ImageIO.write(buff, "png", file);
                        System.out.println("Done saving");

                    }
                } catch (IOException ex1) {
                    System.out.println(ex1);
                }
            }
        });

        open = new JButton("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser choose = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg");
                    choose.setFileFilter(filter);

                    choose.setCurrentDirectory(new File("."));
                    int result = choose.showOpenDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File select = new File(choose.getSelectedFile().getAbsolutePath());
                        System.out.println(select.getName());
                        if (!Desktop.isDesktopSupported()) {
                            System.out.println("Not supported");
                        } else {
                            Desktop d = Desktop.getDesktop();
                            d.open(select);
                        }
                    } else if (result == JFileChooser.CANCEL_OPTION) {
                        System.out.println("Cancelled");
                    }
                } catch (IOException ex2) {
                    System.out.println(ex2);
                }
            }
        });

        this.add(dot);
        this.add(fill);
        
        this.add(myColors);

        this.add(freeh);
        this.add(clear);
        this.add(undo);
        this.add(eraser);

        this.add(line);
        this.add(rect);
        this.add(oval);
        
        this.add(save);
        this.add(open);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                setxy1ByEvent(e);
                x2 = 0;
                y2 = 0;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setxy2ByEvent(e);
                switch (shape) { //to determine which shape ure currently trying to draw (based on what button u clicked)
                    case "line":
                        Shape ln = new Line(x1, y1, x2, y2, current, dotted);
                        addToShapesandUpdate(ln); //to add the shape in the list and repaint
                        break;
                    case "rect":
                        calculateWidthandHeight(); //to get width and height
                        Shape rt = new Rect(x1, y1, w, h, current, dotted, filled);
                        addToShapesandUpdate(rt);
                        break;
                    case "oval":
                        calculateWidthandHeight(); //to get width and height
                        Shape ov = new Oval(x1, y1, w, h, current, dotted, filled);
                        addToShapesandUpdate(ov);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("entered");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("exited");
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) { //to keep drawing as u drag
                switch (shape) {
                    case "freehand":
                        if (x2 != 0 && y2 != 0) { //3ashan mayefdalsh stuck f awel point lw homma lsa 0
                            x1 = x2; //to make it continuous
                            y1 = y2;
                        }
                        setxy2ByEvent(e);
                        Shape fh = new FreeHand(x1, y1, x2, y2, current, dotted);
                        addToShapesandUpdate(fh);
                        break;
                    case "line":
                        setxy2ByEvent(e);
                        updateUI();
                        break;
                    case "rect", "oval":
                        setxy2ByEvent(e);
                        calculateWidthandHeight();
                        updateUI();
                        break;
                    case "eraser":
                        filled = true;
                        h = 20; //size of eraser
                        w = 20;
                        current = Color.BLACK; //color of background
                        setxy2ByEvent(e);
                        Shape er = new Eraser(x2, y2, w, h, current, dotted, filled);
                        addToShapesandUpdate(er);
                        x1 = x2; //to make it continuous 
                        y1 = y2;
                        break;

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("moved");
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; //to use stroke functions

        for (Shape s : shapes) {
            g2d.setStroke(new BasicStroke());
            g.setColor(s.getShapeColor()); //set shape color
            if (s.isFilled() == true) //to check if it was drawn in filled
            {
                switch (s.getShapeType()) //check which shape
                {
                    case "line" ->
                        g.drawLine(s.x1, s.y1, s.x2, s.y2);
                    case "rect" ->
                        g.fillRect(s.x1, s.y1, s.width, s.height);
                    case "oval", "eraser" ->
                        g.fillOval(s.x1, s.y1, s.width, s.height);
                }
            } else {
                if (s.isDotted()) //check if dotted
                {
                    g2d.setStroke(d); //set stroke
                }
                switch (s.getShapeType()) //check which shape
                {
                    case "line" ->
                        g.drawLine(s.x1, s.y1, s.x2, s.y2);
                    case "rect" ->
                        g.drawRect(s.x1, s.y1, s.width, s.height);
                    case "oval" ->
                        g.drawOval(s.x1, s.y1, s.width, s.height);
                }
            }
        }

        g.setColor(current);
        if (cleared == true || undone == true) { //so it wont draw the current shape if we clicked clear all or undone
            System.out.println("removed");
            cleared = false;
            undone = false;
        } else {
            if (filled == true) {
                switch (shape) {
                    case "line" ->
                        g.drawLine(x1, y1, x2, y2);
                    case "rect" ->
                        g.fillRect(x1, y1, w, h);
                    case "oval", "eraser" ->
                        g.fillOval(x1, y1, w, h);
                }
            } else {
                if (dotted == true) {
                    g2d.setStroke(d); //set stroke if dotted
                } else {
                    g2d.setStroke(new BasicStroke());
                }
                switch (shape) {
                    case "line" ->
                        g.drawLine(x1, y1, x2, y2);
                    case "rect" ->
                        g.drawRect(x1, y1, w, h);
                    case "oval" ->
                        g.drawOval(x1, y1, w, h);
                }
            }
            g2d.setStroke(new BasicStroke()); //return stroke to normal so it wont keep drawing dotted 
        }
    }

    public void setxy1ByEvent(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
    }

    public void setxy2ByEvent(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
    }

    public void calculateWidthandHeight() {
        w = Math.abs(x2 - x1);
        h = Math.abs(y2 - y1);
    }

    public void addToShapesandUpdate(Shape s) {
        shapes.add(s);
        updateUI();
    }

    public void changeFlag(boolean flag) {
        if (flag == true) {
            flag = false;
        } else {
            flag = true;
        }
    }

    public void resetFlagsandColor() {
        free = false;
        if (erased == true) {
            erased = false;
            current = Color.white;
        }
    }

}
