package GUI;

import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import Algorithm.AntAlgorithm;
import Graph.*;
import Staff.Pair;

// окно "алгоритм"
public class AlgorithmFrame extends JFrame implements ActionListener {

    private final MainMenuFrame mainMenuFrame; // ссылка на главное окно

    private Graph graph;                       // ссылка на граф
    private Pair<Double, Double> params;       // ссылка на параметры (жадность, скорость испарения)
    private Pair<Integer, Integer> antParams;  // ссылка на параметры муравьев
    private AntAlgorithm algorithm;            // алгоритм

    // списки вершин
    private JLabel startVertexLabel;
    private JComboBox startVertexComboBox;
    private JLabel endVertexLabel;
    private JComboBox endVertexComboBox;

    // кнопка "авто"
    private JButton autoStepButton;
    // кнопка "следующий шаг"
    private JButton nextStepButton;

    // путь муравья
    private JLabel pathOfAntLabel;
    private JLabel pathOfAntTextLabel;

    GraphPanel graphPanel;

    // панель с графом
    class GraphPanel extends JPanel {

        public GraphPanel() {
            setBounds(300, 10, 685, 660);
            setLayout(null);
            TitledBorder graphPanelTitle = BorderFactory.createTitledBorder("");
            setBorder(graphPanelTitle);
        }

        final  BasicStroke stroke_3 = new BasicStroke(3.0f);
        final  BasicStroke stroke_7 = new BasicStroke(7.0f);
        final  BasicStroke stroke_5 = new BasicStroke(5.0f);
        final  BasicStroke stroke_11 = new BasicStroke(11.0f);

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;

            // вершины
            for (int i = 0; i < graph.numberOfVertices; i++) {
                int x = graph.vertices.get(i).coordX;
                int y =  graph.vertices.get(i).coordY;
                String name = String.valueOf(graph.vertices.get(i).letter);

                g2.setStroke(stroke_5);
                g.drawOval(x, y, 50, 50);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString(name, x + 20, y + 30);
            }

            // ребра
            for (int i = 0; i < graph.edges.size(); i++) {
                int x1 = graph.vertices.get(graph.edges.get(i).firstNode).coordX + 25;
                int y1 = graph.vertices.get(graph.edges.get(i).firstNode).coordY + 25;
                int x2 = graph.vertices.get(graph.edges.get(i).secondNode).coordX + 25;
                int y2 = graph.vertices.get(graph.edges.get(i).secondNode).coordY + 25;

                int x11= (int)(x1 + 29 * (x2 - x1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int y11 = (int)(y1 + 29 * (y2 - y1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int x22 = (int)(x2 + 29 * (x1 - x2) /
                        Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                + Math.pow((double)(y1 - y2), 2.0)));

                int y22 = (int)(y2 + 29 * (y1 - y2) /
                        Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                + Math.pow((double)(y1 - y2), 2.0)));

                g2.setStroke(stroke_11);
                g2.setColor(Color.gray);
                g2.drawLine(x11, y11, x22, y22);
                g2.setStroke(stroke_7);
                g2.setColor(Color.white);
                g2.drawLine(x11, y11, x22, y22);

                int maxWeight = 0;
                for (Edge e : graph.edges)
                    maxWeight = Math.max(maxWeight, e.weight);


                int coef = (int)(algorithm.evaporationSpeed < 0.1 ? 1 : 10 * algorithm.evaporationSpeed);


                int green = (int)Math.max(0.0, ((graph.edges.get(i).pheromone <= 0.25) ? 255 : 255 - (graph.edges.get(i).pheromone - 0.25) * maxWeight/10 * coef * 255));
                int blue = (int)Math.max(0.0,((graph.edges.get(i).pheromone >= 0.25) ? 0 : 255 - graph.edges.get(i).pheromone * maxWeight/10 * coef * 255));
                g2.setStroke(stroke_3);
                g2.setColor(new Color(255, green, blue));
                g2.drawLine(x11, y11, x22, y22);

                int x= (int)(x1 + 70 * (x2 - x1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int y = (int)(y1 + 70 * (y2 - y1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                // вес
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.drawString(String.valueOf(graph.edges.get(i).weight), x - 8, y + 8);
                g2.setColor(Color.BLACK);
            }
        }
    }

    // конструктор
    public AlgorithmFrame(MainMenuFrame mainMenuFrame_, Graph graph_,
                          Pair<Double, Double> params_, Pair<Integer, Integer> antParams_) {
        super("Алгоритм");

        mainMenuFrame = mainMenuFrame_;
        graph = graph_;
        params = params_;
        antParams = antParams_;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        setResizable(false);
        setLayout(null);

        // действие при закрытии окна
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                mainMenuFrame.setVisible(true);
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

        // иконка
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/icon.png")));

        // панель пути
        JPanel antPathPanel = new JPanel();
        antPathPanel.setBounds(10, 10, 280, 80);
        antPathPanel.setLayout(null);
        TitledBorder pathPanelTitle = BorderFactory.createTitledBorder("");
        antPathPanel.setBorder(pathPanelTitle);
        add(antPathPanel);

        pathOfAntTextLabel = new JLabel();
        pathOfAntTextLabel.setHorizontalAlignment(JLabel.CENTER);
        pathOfAntTextLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pathOfAntTextLabel.setBounds(20, 10, 250, 30);
        antPathPanel.add(pathOfAntTextLabel);

        pathOfAntLabel = new JLabel();
        pathOfAntLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pathOfAntLabel.setHorizontalAlignment(JLabel.CENTER);
        pathOfAntLabel.setBounds(20, 35, 250, 30);
        antPathPanel.add(pathOfAntLabel);

        // боковая панель
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(10, 100, 280, 570);
        sidePanel.setLayout(null);
        TitledBorder sidePanelTitle = BorderFactory.createTitledBorder("");
        sidePanel.setBorder(sidePanelTitle);


        // списки вершин
        startVertexLabel = new JLabel("Начальная вершина:");
        startVertexLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        startVertexLabel.setBounds(20, 10, 240, 30);
        sidePanel.add(startVertexLabel);

        startVertexComboBox = new JComboBox();
        startVertexComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        startVertexComboBox.setBounds(20, 40, 240, 30);
        // заполнение списка
        for(int i = 0; i < graph.numberOfVertices; ++i)
            for(int j = 0; j < graph.numberOfVertices; ++j)
                if (graph.linksMatrix[i][j]) {
                    startVertexComboBox.addItem(graph.vertices.get(i).letter);
                    break;
                }
        if (startVertexComboBox.getItemCount() != 0) startVertexComboBox.setSelectedIndex(0);
        startVertexComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startVertexComboBox.setName("StartVertexComboBox");
        startVertexComboBox.addActionListener(this);
        sidePanel.add(startVertexComboBox);

        endVertexLabel = new JLabel("Конечная вершина:");
        endVertexLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        endVertexLabel.setBounds(20, 80, 240, 30);
        sidePanel.add(endVertexLabel);

        endVertexComboBox = new JComboBox();
        endVertexComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        endVertexComboBox.setBounds(20, 110, 240, 30);
        // заполнение списка
        int index = 0;
        for (int i = 0; i < graph.vertices.size(); i++)
            if (startVertexComboBox.getSelectedItem() == graph.vertices.get(i).letter) {
                index = i;
                break;
            }
        for(int i = 0; i < graph.numberOfVertices; ++i)
            if (graph.linksMatrix[index][i])
                endVertexComboBox.addItem(graph.vertices.get(i).letter);
        if (endVertexComboBox.getItemCount() != 0) endVertexComboBox.setSelectedIndex(0);
        endVertexComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        endVertexComboBox.setName("EndVertexComboBox");
        endVertexComboBox.addActionListener(this);
        sidePanel.add(endVertexComboBox);

        setAlgorithm(); // настроить алгоритм под списки вершин

        // кнопки
        nextStepButton = new JButton("Следующий шаг");
        nextStepButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextStepButton.setBounds(20, 200, 240, 60);
        nextStepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextStepButton.setActionCommand("NextStepButton");
        nextStepButton.addActionListener(this);
        sidePanel.add(nextStepButton);

        autoStepButton = new JButton("Авто");
        autoStepButton.setFont(new Font("Arial", Font.BOLD, 14));
        autoStepButton.setBounds(20, 270, 240, 60);
        autoStepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        autoStepButton.setActionCommand("AutoStepButton");
        autoStepButton.addActionListener(this);
        sidePanel.add(autoStepButton);

        JButton clearButton = new JButton("Очистить");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBounds(20, 340, 240, 60);
        clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearButton.setActionCommand("ClearButton");
        clearButton.addActionListener(this);
        sidePanel.add(clearButton);

        JButton backButton = new JButton("В главное меню");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(20, 470, 240, 60);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setActionCommand("BackButton");
        backButton.addActionListener(this);
        sidePanel.add(backButton);

        // панель для графа
        graphPanel = new GraphPanel();

        add(sidePanel);
        add(graphPanel);
        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }

    // заблокировать компоненты (combo boxes)
    private void disableComboBoxes() {
        startVertexLabel.setEnabled(false);
        startVertexComboBox.setEnabled(false);
        endVertexLabel.setEnabled(false);
        endVertexComboBox.setEnabled(false);
    }
    // разблокировать компоненты (combo boxes)
    private void enableComboBoxes() {
        startVertexLabel.setEnabled(true);
        startVertexComboBox.setEnabled(true);
        endVertexLabel.setEnabled(true);
        endVertexComboBox.setEnabled(true);
    }

    // задать вершины для алгоритма
    private void setAlgorithm() {
        Integer first = 0;
        Integer second = 0;

        for (int i = 0; i < graph.vertices.size(); i++) {
            if (startVertexComboBox.getSelectedItem() == graph.vertices.get(i).letter)
                first = i;
            if (endVertexComboBox.getSelectedItem() == graph.vertices.get(i).letter)
                second = i;
        }

        Pair<Integer, Integer> path = new Pair<>(first, second);

        graph.removePheromone();

        algorithm = new AntAlgorithm(new Graph(graph), params.first, params.second,
                antParams.first, antParams.second, path);
    }

    private void showPathOfAnt(List<Integer> pathOfAnt) {
        String path = "<html><p align=\"center\">";

        int weight = pathOfAnt.get(pathOfAnt.size()-1);

        pathOfAnt.remove(pathOfAnt.size() - 1);

        for (Integer i : pathOfAnt) {
            if (!path.equals("<html><p align=\"center\">")) path += " - ";
            path += graph.vertices.get(i).letter;
        }

        path += "<br>Вес пути: " + String.valueOf(weight) + "</p><html>";

        pathOfAntTextLabel.setText("Найденный путь: ");
        pathOfAntLabel.setText(path);
    }
    
    private void showAnt(List<Integer> pathOfAnt) {
    	String path = "<html><p align=\"center\">";
    	
    	int weight = pathOfAnt.get(pathOfAnt.size()-1);
    	pathOfAnt.remove(pathOfAnt.size() - 1);

        for (Integer i : pathOfAnt) {
            if (!path.equals("<html><p align=\"center\">")) path += " - ";
            path += graph.vertices.get(i).letter;
        }

        path += "<br>Вес пути: " + String.valueOf(weight) + "</p><html>";

        if (algorithm.getCount() <= antParams.first)
        	pathOfAntTextLabel.setText("\"Блиц\" муравей " 
        			+ algorithm.getCount() + ". Путь: ");
        else
        	pathOfAntTextLabel.setText("Муравей " 
        			+ algorithm.getCount() + ". Путь: ");
        	
        pathOfAntLabel.setText(path);
    }

    // нажатие кнопки
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // списки вершин
        if (source instanceof JComboBox) {
            JComboBox cb = (JComboBox) e.getSource();
            String cmd = cb.getName();

            switch (cmd) {
                case "StartVertexComboBox":
                    endVertexComboBox.removeAllItems();
                    int index = 0;
                    for (int i = 0; i < graph.vertices.size(); i++)
                        if (startVertexComboBox.getSelectedItem() == graph.vertices.get(i).letter) {
                            index = i;
                            break;
                        }
                    for(int i = 0; i < graph.numberOfVertices; ++i)
                        if (graph.linksMatrix[index][i])
                            endVertexComboBox.addItem(graph.vertices.get(i).letter);
                    setAlgorithm(); // алгоритм
                    break;

                case "EndVertexComboBox":
                    setAlgorithm(); // алгоритм
                    break;
            }
        }

        // кнопки
        if (source instanceof JButton) {
            JButton btn = (JButton) e.getSource();
            String cmd = btn.getActionCommand();

            switch(cmd) {
                case "NextStepButton":
                    disableComboBoxes();
                    if (algorithm.finished()) {
                        autoStepButton.setEnabled(false);
                        nextStepButton.setEnabled(false);
                        List<Integer> pathOfAnt = algorithm.findPath();
                        showPathOfAnt(pathOfAnt);
                    } else {
                    	List<Integer> pathOfAnt = algorithm.step();
                    	showAnt(pathOfAnt);
                    }

                    graphPanel.repaint();
                    break;

                case "AutoStepButton":
                    disableComboBoxes();
                    autoStepButton.setEnabled(false);
                    nextStepButton.setEnabled(false);
                    List<Integer> pathOfAnt = algorithm.autoAlgorithm();
                    showPathOfAnt(pathOfAnt);
                    graphPanel.repaint();
                    break;

                case "ClearButton":
                    enableComboBoxes();
                    pathOfAntLabel.setText("");
                    pathOfAntTextLabel.setText("");
                    autoStepButton.setEnabled(true);
                    nextStepButton.setEnabled(true);
                    setAlgorithm();
                    graphPanel.repaint();
                    break;

                case "BackButton":
                    setVisible(false);
                    dispose();
                    mainMenuFrame.setVisible(true);
                    break;
            }
        }
    }
}
