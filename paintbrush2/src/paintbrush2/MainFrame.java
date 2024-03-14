package paintbrush2;


import javax.swing.JFrame;

public class MainFrame extends JFrame{
    
    public static void main(String[] args){
        JFrame f = new JFrame();
        MyPanel p = new MyPanel();
        f.setContentPane(p);
        f.setSize(500, 400);
        f.setTitle("bonus");
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
    }
    
}
