package GUI.GraphEnter;

import javax.swing.*;
import java.awt.*;

public class VertexPanel extends JPanel {
    private int imX = 0;
    private int imY = 0;

    private JLabel name; // название

    public static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public VertexPanel(int x, int y)
    {
        imX = x;
        imY = y;

        setLayout(null);
        setBounds(x, y, 50, 50);
        setOpaque(false);

        // текстура
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/vertex.png"));
        JLabel picLabel = new JLabel(new ImageIcon(img));
        picLabel.setBounds(0, 0, 50, 50);
        add(picLabel);

        // название
        name = new JLabel("A");
        name.setBounds(20,15,20,20);
        add(name);
    }

    public int GetX(){
        return imX;
    }

    public int GetY(){
        return imY;
    }

    public void SetX(int x0){
        imX = x0;
    }

    public void SetY(int y0){
        imY = y0;
    }

    public void SetName(char n) { name.setText(Character.toString(n)); }
}