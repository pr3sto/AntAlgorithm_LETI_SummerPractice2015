package GUI.GraphEnter;

import javax.swing.*;
import java.awt.*;

public class VertexPanel extends JPanel {
    private int X = 0;
    private int Y = 0;

    private JLabel letter; // название

    public VertexPanel(int x, int y)
    {
        X = x;
        Y = y;

        setLayout(null);
        setBounds(x, y, 50, 50);
        setOpaque(false);

        // текстура
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/vertex.png"));
        JLabel picLabel = new JLabel(new ImageIcon(img));
        picLabel.setBounds(0, 0, 50, 50);
        add(picLabel);

        // название
        letter = new JLabel("A");
        letter.setBounds(20,15,20,20);
        add(letter);
    }

    public int getX(){
        return X;
    }

    public int getY(){
        return Y;
    }

    public Character getLetter() {
        return letter.getText().toCharArray()[0];
    }

    public void setX(int x0){
        X = x0;
    }

    public void setY(int y0){
        Y = y0;
    }

    public void setLetter(char n) {
        letter.setText(Character.toString(n));
    }
}