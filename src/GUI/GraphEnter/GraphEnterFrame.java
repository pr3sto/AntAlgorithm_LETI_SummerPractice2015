package GUI.GraphEnter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import GUI.ParametersFrame;
import Graph.Graph;
import Graph.Edge;
import Staff.Pair;

public class GraphEnterFrame extends JFrame
        implements ActionListener, MouseMotionListener, MouseListener {

    private final ParametersFrame parametersFrame;
    Graph graph;

    private JPanel panel1;
    private JPanel GraphPanel;
    private JPanel TextPanel;

    private JButton btnAdd;
    private JButton btnDel;
    private JButton btnAddEdge;
    private JButton btnDelEdge;
    private JButton btnConfirm;

    private JLabel Info;

    private JTextField W;

    private boolean btnDelPressed = false;
    private boolean btnAddEdgePressed = false;
    private boolean btnDelEdgePressed = false;
    private boolean btnConfirmPressed = false;

    boolean check = false;

    boolean interrupt = false;

    int dx;
    int dy;

    int weight_tmp = 0;

    int counterVerMax = 0;
    int counterEdge = 0;
    int counterEdgeMax = 0;

    Runnable r1;
    Thread t1;


    VertexPanel[] vertices = new VertexPanel[10];
    Pair<Integer, Integer>[] edges = new Pair[100];
    int[] weights = new int[100];

    public GraphEnterFrame(ParametersFrame parametersFrame_) {
        super("Ввод графа");

        parametersFrame = parametersFrame_;

        initFrame();
    }

    public GraphEnterFrame(ParametersFrame parametersFrame_, Graph graph_) {
        super("Изменение графа");

        parametersFrame = parametersFrame_;
        graph = graph_;

        initFrame();

        for (int i = 0; i < graph.numberOfVertices; i++) {
            vertices[i] = new VertexPanel(graph.vertices.get(i).coordX, graph.vertices.get(i).coordY);
            vertices[i].SetName(graph.vertices.get(i).name);
            vertices[i].setName(String.valueOf(i));

            vertices[i].addMouseMotionListener(this);
            vertices[i].addMouseListener(this);
            GraphPanel.add(vertices[i]);
            counterVerMax++;
        }

        for (int i = 0; i < graph.edges.size(); i++) {
            edges[i] = new Pair<>(graph.edges.get(i).firstNode, graph.edges.get(i).secondNode);
            weights[i] = graph.edges.get(i).weight;
            counterEdgeMax++;
        }

    }

    private void initFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        setResizable(false);
        setLayout(null);

        // действие при закрытии окна
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
                interrupt = true;
               /* if (t1.getState() == Thread.State.RUNNABLE) {
                    System.out.print("lol");
                    t1.stop();
                }*/
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                parametersFrame.setVisible(true);
            }

            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {
            }

            public void windowOpened(WindowEvent event) {
            }
        });

        btnConfirm = new JButton("Ок");
        btnConfirm.setActionCommand("Confirm");
        btnConfirm.setBounds(170, 5, 50, 25);
        btnConfirm.addActionListener(this);

        btnAdd = new JButton("Добавить вершину");
        btnAdd.setActionCommand("Add vertex");
        btnAdd.addActionListener(this);

        btnDel = new JButton("Удалить вершину");
        btnDel.setActionCommand("Del vertex");
        btnDel.addActionListener(this);

        btnAddEdge = new JButton("Добавить ребро");
        btnAddEdge.setActionCommand("Add edge");
        btnAddEdge.addActionListener(this);
        btnAddEdge.setEnabled(false);

        btnDelEdge = new JButton("Удалить ребро");
        btnDelEdge.setActionCommand("Del edge");
        btnDelEdge.addActionListener(this);
        btnDelEdge.setEnabled(false);

        panel1 = new JPanel();
        panel1.setBorder(BorderFactory.createTitledBorder(""));
        panel1.setBounds(10, 10, 150, 550);
        panel1.add(btnAdd);
        panel1.add(btnDel);
        panel1.add(btnAddEdge);
        panel1.add(btnDelEdge);
        add(panel1);
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

        GraphPanel = new JPanel();
        GraphPanel.setBorder(BorderFactory.createTitledBorder(""));
        GraphPanel.setBounds(300, 10, 685, 660);
        GraphPanel.setLayout(null);

        TextPanel = new JPanel();
        TextPanel.setBorder(BorderFactory.createTitledBorder(""));
        TextPanel.setBounds(170, 10, 600, 40);
        TextPanel.setLayout(null);

        add(GraphPanel);
        add(TextPanel);

        Info = new JLabel("Выберите команду:");
        Info.setLayout(null);
        Info.setBounds(5, 5, 400, 30);
        TextPanel.add(Info);

        W = new JTextField();
        W.setBounds(125, 5, 40, 25);
        W.setLayout(null);
        // TextPanel.add(W);


        r1 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while(!interrupt) {
                    int free = 0;

                    Graphics gr = GraphPanel.getGraphics();
                    //Graphics gr1 = GraphPanel.getGraphics();

                    //gr.draw(new Line2D.Double(0, 0, 30, 40));

                    if (counterEdgeMax == 1) {
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {}
                    }


                    while (free < counterEdgeMax) {
                        if (edges[free] != null) {

                            int x1 = vertices[edges[free].first].GetX() + 25;
                            int y1 = vertices[edges[free].first].getY() + 25;
                            int x2 = vertices[edges[free].second].GetX() + 25;
                            int y2 = vertices[edges[free].second].getY() + 25;

                            int x11= (int)(x1 + 25 * (x2 - x1) /
                                    Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                            + Math.pow((double)(y2 - y1), 2.0)));

                            int y11 = (int)(y1 + 25 * (y2 - y1) /
                                    Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                            + Math.pow((double)(y2 - y1), 2.0)));

                            int x22 = (int)(x2 + 25 * (x1 - x2) /
                                    Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                            + Math.pow((double)(y1 - y2), 2.0)));

                            int y22 = (int)(y2 + 25 * (y1 - y2) /
                                    Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                            + Math.pow((double)(y1 - y2), 2.0)));

                            gr.setColor(Color.BLACK);
                            gr.drawLine(x11, y11, x22, y22);

                            int centerX = (int)(x1 + 50 * (x2 - x1) /
                                    Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                            + Math.pow((double)(y2 - y1), 2.0)));

                            int centerY = (int)(y1 + 50 * (y2 - y1) /
                                    Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                            + Math.pow((double)(y2 - y1), 2.0)));

                            gr.setFont(new Font("Arial", Font.BOLD, 15));

                            //if(centerX == (float)((x1+x2)/2)
                            //if(centerX == (float)((x1+x2)/2.0) || centerY == (float)((y1+y2)/2.0)) {
                            gr.setColor(Color.RED);


                            gr.drawString(Integer.toString(weights[free]), centerX, centerY);
                           //gr.drawString("e", 60, 60);
                            //}


                            //gr.drawLine(vertices[edges[free].first].GetX() + 25, vertices[edges[free].first].getY() + 25, vertices[edges[free].second].GetX() + 25, vertices[edges[free].second].GetY() + 25);
                        }

                        free++;
                    }



                    if (counterVerMax == 10)
                        btnAdd.setEnabled(false);

                    if (counterVerMax == 0)
                        btnDel.setEnabled(false);
                    else
                        btnDel.setEnabled(true);

                    if((counterEdge == (counterVerMax*(counterVerMax - 1)/2)) || (counterVerMax < 2)){
                        btnAddEdge.setEnabled(false);
                    }
                    else {
                        btnAddEdge.setEnabled(true);
                    }

                    if(counterEdge < 1)
                        btnDelEdge.setEnabled(false);


                }
            }
        };

        t1 = new Thread(r1);
        t1.start();

        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }


    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) {
        if(btnDelPressed) {
            Object ob = e.getSource();
            VertexPanel vertex = (VertexPanel) ob;
            int count = Integer.parseInt(vertex.getName());
            GraphPanel.remove(vertices[count]);
            vertices[count] = null;
            btnAdd.setEnabled(true);
            counterVerMax--;
            for(int i = 0; i < counterEdgeMax;i++) {
                if(edges[i] != null)
                    if(count == edges[i].first || count == edges[i].second){
                        edges[i] = null;
                        counterEdge--;
                    }
            }
            GraphPanel.repaint();
            btnDelPressed = false;

            Info.setText("Удалена вершина " + vertex.alphabet[count]);
        }

        if(btnAddEdgePressed){
            if(btnConfirmPressed) {
                Object ob = e.getSource();
                VertexPanel vertex = (VertexPanel) ob;
                int count = Integer.parseInt(vertex.getName());
                if (!check) {
                    Info.setText("<html>Кликните на 2 вершины, которые хотите связать ребром.<br>Выбрана вершина " + vertex.alphabet[count] + ". Выберите вторую вершину:</html>");
                    dx = count;
                    check = true;
                } else {
                    boolean tmp = false;
                    for(int i = 0; i < counterEdgeMax;i++) {
                        if(edges[i] != null) {
                            if ((dx == edges[i].first && count == edges[i].second) ||(count == edges[i].first && dx == edges[i].second) ) {
                                tmp = true;
                                break;
                            }else if(dx == count){
                                tmp = true;
                                break;
                            }
                        }
                    }
                    if(!tmp) {
                        Info.setText("Выбрана вершина " + vertex.alphabet[count] + ". Создано ребро " + vertex.alphabet[dx] + "--" + vertex.alphabet[count]);
                        int free = 0;
                        for (int i = 0; i < counterEdgeMax; i++) {
                            if (edges[i] == null) {
                                free = i;
                                break;
                            }
                            free++;
                        }
                        edges[free] = new Pair<Integer, Integer>(dx, count);
                        counterEdge++;
                        counterEdgeMax++;
                    }
                    else{
                        if(dx == count)
                            Info.setText("Выбрана вершина " + vertex.alphabet[count] + ". Действие отменено!!!");
                        else
                            Info.setText("Выбрана вершина " + vertex.alphabet[count] + ". Ребро " + vertex.alphabet[dx] + "--" + vertex.alphabet[count] + " уже существует!!!");
                    }
                    check = false;
                    btnAddEdgePressed = false;
                    btnConfirmPressed = false;
                    btnDelEdge.setEnabled(true);
                    setCursor(Cursor.DEFAULT_CURSOR);

                }
            }
        }
        if(btnDelEdgePressed){
            Object ob = e.getSource();
            VertexPanel vertex = (VertexPanel) ob;
            int count = Integer.parseInt(vertex.getName());
            if(!check){
                Info.setText("<html>Кликните на 2 вершины, которые связывает данное ребро.<br>Выбрана вершина " + vertex.alphabet[count] + ". Выберите вторую вершину:</html>");
                dx = count;
                check = true;
            }
            else{
                boolean tmp = false;
                for(int i = 0; i < counterEdgeMax;i++) {
                    if(edges[i] != null) {
                        if (dx == edges[i].first && count == edges[i].second) {
                            edges[i] = null;
                            tmp = true;
                            counterEdge--;
                        } else if (count == edges[i].first && dx == edges[i].second) {
                            edges[i] = null;
                            tmp = true;
                            counterEdge--;
                        }
                    }
                }
                if(tmp)
                    Info.setText("Выбрана вершина " + vertex.alphabet[count] + ". Удалено ребро " + vertex.alphabet[dx] + "--" + vertex.alphabet[count]);
                else
                    Info.setText("Данного ребра не существует!!!");
                check = false;
                btnDelEdgePressed = false;
                setCursor(Cursor.DEFAULT_CURSOR);
                GraphPanel.repaint();
            }

            /*if(counterEdge == 0)
                btnDelEdge.setEnabled(false);*/
        }
    }
    public void mouseEntered(MouseEvent e) {
        //GraphPanel.repaint();
    }
    public void mouseMoved(MouseEvent e) {
        //GraphPanel.repaint();
    }
    public void mouseReleased(MouseEvent e) {
        GraphPanel.repaint();
    }
    public void mousePressed(MouseEvent e) {
        // GraphPanel.repaint();
    }

    public void mouseDragged(MouseEvent e) {
        if(!btnAddEdgePressed && !btnDelEdgePressed) {
            Object source = e.getSource();
            VertexPanel vertex = (VertexPanel) source;
            int count = Integer.parseInt(vertex.getName());

            int newX = e.getX();
            int newY = e.getY();
            if (newX < 25) {
                dx = newX - 25 + vertices[count].GetX();
                if (dx < 0)
                    dx = 0;
                vertices[count].SetX(dx);
            } else if ((newX - 25 + vertices[count].GetX() + vertices[count].getWidth()) > GraphPanel.getWidth()) {
                dx = GraphPanel.getWidth() - vertices[count].getWidth();
                vertices[count].SetX(dx);
            } else {
                dx = newX - 25 + vertices[count].GetX();
                vertices[count].SetX(dx);
            }
            //Аналогично, для оси Y:
            if (newY < 25) {
                dy = newY - 25 + vertices[count].GetY();
                if (dy < 0)
                    dy = 0;
                vertices[count].SetY(dy);
            } else if ((newY - 25 + vertices[count].GetY() + vertices[count].getHeight()) > GraphPanel.getHeight()) {
                dy = GraphPanel.getHeight() - vertices[count].getHeight();
                vertices[count].SetY(dy);
            } else {
                dy = newY - 25 + vertices[count].GetY();
                vertices[count].SetY(dy);
            }

            vertices[count].setLocation(dx, dy);
            GraphPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Add vertex")) {


            setCursor(Cursor.DEFAULT_CURSOR);

            int free = 0;

           /* if (counterVerMax == 9)
                btnAdd.setEnabled(false);

            if (counterVerMax > 0)
                btnAddEdge.setEnabled(true);
            else
                btnAddEdge.setEnabled(false);*/

            for(int i = 0; i < 10; i++) {
                if(vertices[i] == null) {
                    free = i;
                    break;
                }
            }

            Info.setText("Добавлена вершина " + VertexPanel.alphabet[free]);
            TextPanel.remove(W);
            TextPanel.remove(btnConfirm);

            vertices[free] = new VertexPanel(0, 0);
            vertices[free].setName(String.valueOf(free));
            vertices[free].addMouseMotionListener(this);
            vertices[free].addMouseListener(this);
            vertices[free].SetName(VertexPanel.alphabet[free]);
            GraphPanel.add(vertices[free]);
            counterVerMax++;
            GraphPanel.repaint();
            btnDelPressed = false;
            btnAddEdgePressed = false;
            btnDelEdgePressed = false;
        }

        if(e.getActionCommand().equals("Del vertex")){
            TextPanel.remove(W);
            TextPanel.remove(btnConfirm);
            Info.setText("Кликните на вершину, которую хотите удалить:");
            setCursor(Cursor.DEFAULT_CURSOR);
            btnDelPressed = true;
            btnAddEdgePressed = false;
            btnDelEdgePressed = false;

            /*if (counterVerMax > 0)
                btnAddEdge.setEnabled(true);
            else
                btnAddEdge.setEnabled(false);*/
        }

        if(e.getActionCommand().equals("Add edge")){

            Info.setText("<html>Введите вес ребра:<br>(от 1 до 100)</html>");
            TextPanel.add(W);
            TextPanel.add(btnConfirm);

            btnDelPressed = false;
            btnAddEdgePressed = true;
            btnDelEdgePressed = false;

        }

        if(e.getActionCommand().equals("Del edge")){
            TextPanel.remove(W);
            TextPanel.remove(btnConfirm);

            Info.setText("Кликните на 2 вершины, которые связывает данное ребро:");
            btnDelPressed = false;
            btnAddEdgePressed = false;
            btnDelEdgePressed = true;
            setCursor(Cursor.HAND_CURSOR);
        }

        if(e.getActionCommand().equals("Confirm")){
            try {
                if (Integer.parseInt(W.getText()) <= 100 && Integer.parseInt(W.getText()) > 0) {
                    btnConfirmPressed = true;
                    int free = 0;
                    for (int i = 0; i < counterEdgeMax + 1; i++) {
                        if (edges[i] == null) {
                            free = i;
                            break;
                        }
                    }
                    weights[free] = Integer.parseInt(W.getText());
                    TextPanel.remove(W);
                    TextPanel.remove(btnConfirm);
                    Info.setText("Кликните на 2 вершины, которые хотите связать ребром:");
                    setCursor(Cursor.HAND_CURSOR);
                }

                W.setText("");

            }
            catch(Exception e1){
                W.setText("");
            }
        }
    }
}