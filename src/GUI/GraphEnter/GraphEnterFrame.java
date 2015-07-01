package GUI.GraphEnter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import GUI.ParametersFrame;
import Graph.*;
import Staff.Pair;

// окно рисования графа
public class GraphEnterFrame extends JFrame
        implements ActionListener, MouseMotionListener, MouseListener {

    private final ParametersFrame parametersFrame; // ссылка на окно-родитель

    Graph graph; // ссылка на граф

    // панель
    private JPanel graphPanel;

    // кнопки
    private JButton btnAdd;
    private JButton btnDel;
    private JButton btnAddEdge;
    private JButton btnDelEdge;
    private JButton btnConfirm;
    private boolean btnDelPressed = false;
    private boolean btnAddEdgePressed = false;
    private boolean btnDelEdgePressed = false;
    private boolean btnConfirmPressed = false;

    // для ввода веса
    private JLabel infoLabel;
    private JTextField weightTextField;

    int counterVerMax = 0;
    int counterEdgeMax = 0;
    int counterEdge = 0;

    int dx;
    int dy;

    boolean check = false;

    boolean interrupt = false; // для закрытия потока
    boolean removeEdges = false;

    VertexPanel[] vertices = new VertexPanel[10];   // вершины
    Pair<Integer, Integer>[] edges = new Pair[100]; // ребра
    int[] weights = new int[100];                   // веса

    // конструктор
    public GraphEnterFrame(ParametersFrame parametersFrame_, Graph graph_) {
        super("Рисование графа");

        parametersFrame = parametersFrame_;
        graph = graph_;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        setResizable(false);
        setLayout(null);

        // действие при закрытии окна
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
                interrupt = true; // для остановки потока
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                parametersFrame.setVisible(true);
            }
            public void windowActivated(WindowEvent event) { }
            public void windowClosed(WindowEvent event) { }
            public void windowDeactivated(WindowEvent event) { }
            public void windowDeiconified(WindowEvent event) { }
            public void windowIconified(WindowEvent event) { }
            public void windowOpened(WindowEvent event) { }
        });

        // иконка
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/icon.png")));


        // боковая панель
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(10, 10, 280, 660);
        sidePanel.setLayout(null);
        TitledBorder sidePanelTitle = BorderFactory.createTitledBorder("");
        sidePanel.setBorder(sidePanelTitle);

        btnAdd = new JButton("Добавить вершину");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setBounds(20, 200, 240, 60);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.setActionCommand("AddVertexButton");
        btnAdd.addActionListener(this);
        sidePanel.add(btnAdd);

        btnDel = new JButton("Удалить вершину");
        btnDel.setFont(new Font("Arial", Font.BOLD, 14));
        btnDel.setBounds(20, 270, 240, 60);
        btnDel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDel.setActionCommand("DeleteVertexButton");
        btnDel.addActionListener(this);
        sidePanel.add(btnDel);

        btnAddEdge = new JButton("Добавить ребро");
        btnAddEdge.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddEdge.setBounds(20, 340, 240, 60);
        btnAddEdge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAddEdge.setActionCommand("AddEdgeButton");
        btnAddEdge.addActionListener(this);
        btnAddEdge.setEnabled(false);
        sidePanel.add(btnAddEdge);

        btnDelEdge = new JButton("Удалить ребро");
        btnDelEdge.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelEdge.setBounds(20, 410, 240, 60);
        btnDelEdge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDelEdge.setActionCommand("DeleteEdgeButton");
        btnDelEdge.addActionListener(this);
        btnDelEdge.setEnabled(false);
        sidePanel.add(btnDelEdge);

        JButton saveAndExitButton = new JButton("Сохранить и выйти");
        saveAndExitButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveAndExitButton.setBounds(20, 510, 240, 60);
        saveAndExitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveAndExitButton.setActionCommand("SaveAndExitButton");
        saveAndExitButton.addActionListener(this);
        sidePanel.add(saveAndExitButton);

        JButton exitButton = new JButton("Выйти без сохранения");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBounds(20, 580, 240, 60);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setActionCommand("ExitButton");
        exitButton.addActionListener(this);
        sidePanel.add(exitButton);


        // панель для вывода информации
        JPanel textPanel = new JPanel();
        textPanel.setBounds(20, 20, 240, 140);
        textPanel.setLayout(null);
        textPanel.setBorder(BorderFactory.createTitledBorder(""));

        infoLabel = new JLabel("");
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel.setBounds(20, 20, 200, 50);
        textPanel.add(infoLabel);

        weightTextField = new JTextField();
        weightTextField.setBounds(30, 90, 70, 30);
        weightTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        weightTextField.setVisible(false);
        textPanel.add(weightTextField);

        btnConfirm = new JButton("Ок");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirm.setBounds(140, 90, 70, 30);
        btnConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirm.setActionCommand("Confirm");
        btnConfirm.addActionListener(this);
        btnConfirm.setVisible(false);
        textPanel.add(btnConfirm);


        // панель для рисовки графа
        graphPanel = new JPanel();
        graphPanel.setBorder(BorderFactory.createTitledBorder(""));
        graphPanel.setBounds(300, 10, 685, 660);
        graphPanel.setLayout(null);


        sidePanel.add(textPanel);
        add(sidePanel);
        add(graphPanel);


        // поток, обновляющий граф
        Runnable r1 = new Runnable() {
            public void run() {
                // задержка для прорисовки
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (!interrupt) {
                    int free = 0;

                    while (removeEdges) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Graphics gr = graphPanel.getGraphics();

                    // задержка для прорисовки
                    if (counterEdgeMax == 1) {
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    while (free < counterEdgeMax) {
                        if (edges[free] != null) {

                            int x1 = vertices[edges[free].first].getX() + 25;
                            int y1 = vertices[edges[free].first].getY() + 25;
                            int x2 = vertices[edges[free].second].getX() + 25;
                            int y2 = vertices[edges[free].second].getY() + 25;

                            int x11 = (int) (x1 + 25 * (x2 - x1) /
                                    Math.sqrt(Math.pow((double) (x2 - x1), 2.0)
                                            + Math.pow((double) (y2 - y1), 2.0)));

                            int y11 = (int) (y1 + 25 * (y2 - y1) /
                                    Math.sqrt(Math.pow((double) (x2 - x1), 2.0)
                                            + Math.pow((double) (y2 - y1), 2.0)));

                            int x22 = (int) (x2 + 25 * (x1 - x2) /
                                    Math.sqrt(Math.pow((double) (x1 - x2), 2.0)
                                            + Math.pow((double) (y1 - y2), 2.0)));

                            int y22 = (int) (y2 + 25 * (y1 - y2) /
                                    Math.sqrt(Math.pow((double) (x1 - x2), 2.0)
                                            + Math.pow((double) (y1 - y2), 2.0)));

                            gr.setColor(Color.BLACK);
                            gr.drawLine(x11, y11, x22, y22);

                            int centerX = (int) (x1 + 50 * (x2 - x1) /
                                    Math.sqrt(Math.pow((double) (x2 - x1), 2.0)
                                            + Math.pow((double) (y2 - y1), 2.0)));

                            int centerY = (int) (y1 + 50 * (y2 - y1) /
                                    Math.sqrt(Math.pow((double) (x2 - x1), 2.0)
                                            + Math.pow((double) (y2 - y1), 2.0)));

                            gr.setFont(new Font("Arial", Font.BOLD, 15));
                            gr.setColor(Color.RED);
                            gr.drawString(Integer.toString(weights[free]), centerX, centerY);
                        }

                        free++;
                    }

                    if (counterVerMax == 10)
                        btnAdd.setEnabled(false);

                    if (counterVerMax == 0)
                        btnDel.setEnabled(false);
                    else
                        btnDel.setEnabled(true);

                    if ((counterEdge == (counterVerMax * (counterVerMax - 1) / 2)) || (counterVerMax < 2))
                        btnAddEdge.setEnabled(false);
                    else
                        btnAddEdge.setEnabled(true);

                    if (counterEdge < 1)
                        btnDelEdge.setEnabled(false);
                }
            }};

        // запуск потока рисовки графа
        Thread t1 = new Thread(r1);
        t1.start();


        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);


        // если граф создан - нарисовать его
        if (graph.isCreated()) {
            // вершины
            for (int i = 0; i < graph.numberOfVertices; i++) {
                vertices[i] = new VertexPanel(graph.vertices.get(i).coordX, graph.vertices.get(i).coordY);
                vertices[i].setLetter(graph.vertices.get(i).letter);
                vertices[i].setName(String.valueOf(i));
                vertices[i].addMouseMotionListener(this);
                vertices[i].addMouseListener(this);
                graphPanel.add(vertices[i]);
                counterVerMax++;
            }

            // ребра и веса
            for (int i = 0; i < graph.edges.size(); i++) {
                edges[i] = new Pair<>(graph.edges.get(i).firstNode, graph.edges.get(i).secondNode);
                weights[i] = graph.edges.get(i).weight;
                counterEdgeMax++;
            }
        }
    }

    private void saveGraph() {
        graph.edges = new ArrayList<>();
        graph.vertices = new ArrayList<>();

        // вершины
        graph.numberOfVertices = counterVerMax;
        for (int i = 0; i < 10; i++) {
            if (vertices[i] != null)
                graph.vertices.add(new Vertex(vertices[i].getX(), vertices[i].getY(), vertices[i].getLetter()));
        }

        // ребра
        for (int i = 0; i < counterEdge; i++) {
            graph.edges.add(new Edge(weights[i], edges[i].first, edges[i].second));
        }

        graph.createMatrixFromEdgesAndVertices();
    }


    public void mouseEntered(MouseEvent e) { }
    public void mouseMoved(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) {
        graphPanel.repaint();
    }

    public void mouseClicked(MouseEvent e) {
        if(btnDelPressed) {
            removeEdges = true;

            Object ob = e.getSource();
            VertexPanel vertex = (VertexPanel) ob;
            int count = Integer.parseInt(vertex.getName());

            graphPanel.remove(vertices[count]);
            vertices[count] = null;
            btnAdd.setEnabled(true);
            counterVerMax--;

            for (int i = 0; i < counterEdgeMax;i++) {
                if (edges[i] != null)
                    if (count == edges[i].first || count == edges[i].second) {
                        edges[i] = null;
                        counterEdge--;
                    }
            }

            removeEdges = false;

            graphPanel.repaint();
            btnDelPressed = false;

            infoLabel.setText("<html><p align=\"center\">Удалена вершина " + Graph.alphabet[count] + "</p><html>");

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        if(btnAddEdgePressed) {
            removeEdges = true;

            if(btnConfirmPressed) {
                Object ob = e.getSource();
                VertexPanel vertex = (VertexPanel) ob;
                int count = Integer.parseInt(vertex.getName());

                if (!check) {
                    infoLabel.setText("<html><p align=\"center\">Выбрана вершина "
                            + Graph.alphabet[count] + ".<br>Выберите вторую вершину</p><html>");
                    dx = count;
                    check = true;
                } else {
                    boolean tmp = false;
                    for (int i = 0; i < counterEdgeMax;i++) {
                        if (edges[i] != null) {
                            if ((dx == edges[i].first && count == edges[i].second)
                                    || (count == edges[i].first && dx == edges[i].second)) {
                                tmp = true;
                                break;
                            } else if(dx == count) {
                                tmp = true;
                                break;
                            }
                        }
                    }
                    if(!tmp) {
                        infoLabel.setText("<html><p align=\"center\">Выбрана вершина " + Graph.alphabet[count]
                                + ".<br>Создано ребро " + Graph.alphabet[dx] + "-" + Graph.alphabet[count] + "</p><html>");
                        int free = 0;
                        for (int i = 0; i < counterEdgeMax; i++) {
                            if (edges[i] == null) {
                                free = i;
                                break;
                            }
                            free++;
                        }

                        edges[free] = new Pair<>(dx, count);
                        counterEdge++;
                        counterEdgeMax++;
                    } else {
                        if(dx == count)
                            infoLabel.setText("<html><p align=\"center\">Выбрана вершина "
                                    + Graph.alphabet[count] + ".<br>Действие отменено!</p><html>");
                        else
                            infoLabel.setText("<html><p align=\"center\">Выбрана вершина " + Graph.alphabet[count] + ".<br>Ребро "
                                    + Graph.alphabet[dx] + "-" + Graph.alphabet[count] + " уже существует!</p><html>");
                    }

                    check = false;
                    btnAddEdgePressed = false;
                    btnConfirmPressed = false;
                    btnDelEdge.setEnabled(true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                }
            }

            removeEdges = false;
        }

        if (btnDelEdgePressed) {
            removeEdges = true;

            Object ob = e.getSource();
            VertexPanel vertex = (VertexPanel) ob;
            int count = Integer.parseInt(vertex.getName());

            if(!check) {
                infoLabel.setText("<html><p align=\"center\">Выбрана вершина "
                        + Graph.alphabet[count] + ".<br>Выберите вторую вершину</p><html>");
                dx = count;
                check = true;
            } else {
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
                    infoLabel.setText("<html><p align=\"center\">Выбрана вершина " + Graph.alphabet[count] + ".<br>Удалено ребро "
                            + Graph.alphabet[dx] + "-" + Graph.alphabet[count] + "</p><html>");
                else
                    infoLabel.setText("<html><p align=\"center\">Данного ребра не существует!</p><html>");

                check = false;
                btnDelEdgePressed = false;
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                graphPanel.repaint();
            }

            removeEdges = false;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!btnAddEdgePressed && !btnDelEdgePressed && !btnDelPressed) {
            Object source = e.getSource();
            VertexPanel vertex = (VertexPanel) source;
            int count = Integer.parseInt(vertex.getName());

            int newX = e.getX();
            int newY = e.getY();

            if (newX < 25) {
                dx = newX - 25 + vertices[count].getX();
                if (dx < 0)
                    dx = 0;
                vertices[count].setX(dx);
            } else if ((newX - 25 + vertices[count].getX() + vertices[count].getWidth()) > graphPanel.getWidth()) {
                dx = graphPanel.getWidth() - vertices[count].getWidth();
                vertices[count].setX(dx);
            } else {
                dx = newX - 25 + vertices[count].getX();
                vertices[count].setX(dx);
            }

            if (newY < 25) {
                dy = newY - 25 + vertices[count].getY();
                if (dy < 0)
                    dy = 0;
                vertices[count].setY(dy);
            } else if ((newY - 25 + vertices[count].getY() + vertices[count].getHeight()) > graphPanel.getHeight()) {
                dy = graphPanel.getHeight() - vertices[count].getHeight();
                vertices[count].setY(dy);
            } else {
                dy = newY - 25 + vertices[count].getY();
                vertices[count].setY(dy);
            }

            vertices[count].setLocation(dx, dy);
            graphPanel.repaint();
        }
    }


    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {
            case "AddVertexButton":
                weightTextField.setVisible(false);
                btnConfirm.setVisible(false);

                int free = 0;
                for (int i = 0; i < 10; i++) {
                    if (vertices[i] == null) {
                        free = i;
                        break;
                    }
                }

                infoLabel.setText("<html><p align=\"center\">Добавлена вершина " + Graph.alphabet[free] + "</p><html>");
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                vertices[free] = new VertexPanel(0, 0);
                vertices[free].setName(String.valueOf(free));
                vertices[free].addMouseMotionListener(this);
                vertices[free].addMouseListener(this);
                vertices[free].setLetter(Graph.alphabet[free]);

                graphPanel.add(vertices[free]);
                counterVerMax++;
                graphPanel.repaint();

                btnDelPressed = false;
                btnAddEdgePressed = false;
                btnDelEdgePressed = false;
                break;

            case "DeleteVertexButton":
                weightTextField.setVisible(false);
                btnConfirm.setVisible(false);

                infoLabel.setText("<html><p align=\"center\">Кликните на вершину,<br>которую хотите удалить</p><html>");
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

                btnDelPressed = true;
                btnAddEdgePressed = false;
                btnDelEdgePressed = false;
                break;

            case "AddEdgeButton":
                weightTextField.setVisible(true);
                btnConfirm.setVisible(true);

                infoLabel.setText("<html><p align=\"center\">Введите вес ребра<br>(от 1 до 100)</p><html>");

                btnDelPressed = false;
                btnAddEdgePressed = true;
                btnDelEdgePressed = false;
                break;

            case "DeleteEdgeButton":
                weightTextField.setVisible(false);
                btnConfirm.setVisible(false);

                infoLabel.setText("<html><p align=\"center\">Кликните на 2 вершины,<br>которые связывает данное ребро</p><html>");
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

                btnDelPressed = false;
                btnAddEdgePressed = false;
                btnDelEdgePressed = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                break;

            case "Confirm":
                try {
                    if (Integer.parseInt(weightTextField.getText()) <= 100
                            && Integer.parseInt(weightTextField.getText()) > 0) {
                        btnConfirmPressed = true;
                        free = 0;
                        for (int i = 0; i < counterEdgeMax + 1; i++) {
                            if (edges[i] == null) {
                                free = i;
                                break;
                            }
                        }
                        weights[free] = Integer.parseInt(weightTextField.getText());

                        weightTextField.setVisible(false);
                        btnConfirm.setVisible(false);

                        infoLabel.setText("<html><p align=\"center\">Кликните на 2 вершины,<br>которые хотите связать ребром</p><html>");
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    }

                    weightTextField.setText("");

                } catch (Exception e1) {
                    weightTextField.setText("");
                }
                break;

            case "SaveAndExitButton":
                interrupt = true; // для остановки потока
                saveGraph();
                setVisible(false);
                dispose();
                parametersFrame.setVisible(true);
                break;

            case "ExitButton":
                interrupt = true; // для остановки потока
                setVisible(false);
                dispose();
                parametersFrame.setVisible(true);
                break;
        }
    }
}
