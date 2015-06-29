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
import Graph.Graph;
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

    // путь муравья
    private JLabel pathOfAntLabel;

    // панель с графом
    class GraphPanel extends JPanel {

        public GraphPanel() {
            setBounds(300, 10, 685, 660);
            setLayout(null);
            TitledBorder graphPanelTitle = BorderFactory.createTitledBorder("");
            setBorder(graphPanelTitle);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // вершины
            for (int i = 0; i < graph.numberOfVertices; i++) {
                int x = graph.vertices.get(i).coordX;
                int y =  graph.vertices.get(i).coordY;
                String name = String.valueOf(graph.vertices.get(i).name);

                g.drawOval(x - 20, y - 20, 40, 40);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString(name, x - 7, y + 5);
            }

            // ребра
            for (int i = 0; i < graph.edges.size(); i++) {
                int x1 = graph.vertices.get(graph.edges.get(i).firstNode).coordX;
                int y1 = graph.vertices.get(graph.edges.get(i).firstNode).coordY;
                int x2 = graph.vertices.get(graph.edges.get(i).secondNode).coordX;
                int y2 = graph.vertices.get(graph.edges.get(i).secondNode).coordY;

                int x11= (int)(x1 + 20 * (x2 - x1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int y11 = (int)(y1 + 20 * (y2 - y1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int x22 = (int)(x2 + 20 * (x1 - x2) /
                        Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                + Math.pow((double)(y1 - y2), 2.0)));

                int y22 = (int)(y2 + 20 * (y1 - y2) /
                        Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                + Math.pow((double)(y1 - y2), 2.0)));

                g.drawLine(x11, y11, x22, y22);

                int x= (int)(x1 + 70 * (x2 - x1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int y = (int)(y1 + 70 * (y2 - y1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                // вес
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 15));
                g.drawString(String.valueOf(graph.edges.get(i).weight), x - 8, y + 8);
                g.setColor(Color.BLACK);
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
            public void windowActivated(WindowEvent event) { }
            public void windowClosed(WindowEvent event) { }
            public void windowDeactivated(WindowEvent event) { }
            public void windowDeiconified(WindowEvent event) { }
            public void windowIconified(WindowEvent event) { }
            public void windowOpened(WindowEvent event) { }
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

        JLabel pathOfAntTextLabel = new JLabel("Найденный путь: ");
        pathOfAntTextLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pathOfAntTextLabel.setBounds(20, 10, 150, 30);
        antPathPanel.add(pathOfAntTextLabel);

        pathOfAntLabel = new JLabel();
        pathOfAntLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pathOfAntLabel.setBounds(20, 30, 250, 30);
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
                    startVertexComboBox.addItem(Graph.alphabet[i]);
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
        for (int i = 0; i < Graph.alphabet.length; i++)
            if (startVertexComboBox.getSelectedItem() == Graph.alphabet[i]) {
                index = i;
                break;
            }
        for(int i = 0; i < graph.numberOfVertices; ++i)
            if (graph.linksMatrix[index][i])
                endVertexComboBox.addItem(Graph.alphabet[i]);
        if (endVertexComboBox.getItemCount() != 0) endVertexComboBox.setSelectedIndex(0);
        endVertexComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        endVertexComboBox.setName("EndVertexComboBox");
        endVertexComboBox.addActionListener(this);
        sidePanel.add(endVertexComboBox);

        setAlgorithm(); // настроить алгоритм под списки вершин

        // кнопки
        JButton nextStepButton = new JButton("Следующий шаг");
        nextStepButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextStepButton.setBounds(20, 200, 240, 60);
        nextStepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextStepButton.setActionCommand("NextStepButton");
        nextStepButton.addActionListener(this);
        sidePanel.add(nextStepButton);

        // ---------------------------------------- для первой версии -------------------------------------------------
        nextStepButton.setEnabled(false);
        // ------------------------------------------------------------------------------------------------------------

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
        GraphPanel graphPanel = new GraphPanel();

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

        for (int i = 0; i < Graph.alphabet.length; i++) {
            if (startVertexComboBox.getSelectedItem() == Graph.alphabet[i])
                first = i;
            if (endVertexComboBox.getSelectedItem() == Graph.alphabet[i])
                second = i;
        }

        Pair<Integer, Integer> path = new Pair<>(first, second);

        algorithm = new AntAlgorithm(new Graph(graph), params.first, params.second,
                antParams.first, antParams.second, path);
    }

    private void showPathOfAnt(List<Integer> pathOfAnt) {
        String path = "";

        for (Integer i : pathOfAnt) {
            if (!path.equals("")) path += " - ";
            path += Graph.alphabet[i];
        }

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
                    for (int i = 0; i < Graph.alphabet.length; i++)
                        if (startVertexComboBox.getSelectedItem() == Graph.alphabet[i]) {
                            index = i;
                            break;
                        }
                    for(int i = 0; i < graph.numberOfVertices; ++i)
                        if (graph.linksMatrix[index][i])
                            endVertexComboBox.addItem(Graph.alphabet[i]);
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
                    break;

                case "AutoStepButton":
                    disableComboBoxes();
                    autoStepButton.setEnabled(false);
                    List<Integer> pathOfAnt = algorithm.autoAlgorithm();
                    showPathOfAnt(pathOfAnt);
                    break;

                case "ClearButton":
                    enableComboBoxes();
                    pathOfAntLabel.setText("");
                    autoStepButton.setEnabled(true);
                    setAlgorithm();
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
