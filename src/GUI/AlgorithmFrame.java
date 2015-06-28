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
import Algorithm.Graph;

// окно "алгоритм"
public class AlgorithmFrame extends JFrame implements ActionListener {

    private final MainMenuFrame mainMenuFrame; // ссылка на главное окно
    private AntAlgorithm algorithm;            // ссылка на алгоритм
    private Graph graph;                       // ссылка на граф

    private Character[] alphabet = {'A',       // алфавит для списков вершин
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    // списки вершин
    private JLabel startVertexLabel;
    private JComboBox startVertexComboBox;
    private JLabel endVertexLabel;
    private JComboBox endVertexComboBox;

    private JButton autoStepButton;

    // путь муравья
    private List<Integer> pathOfAnt;
    private JLabel pathOfAntLabel;

    // конструктор
    public AlgorithmFrame(MainMenuFrame mainMenuFrame_,
                          Graph graph_, AntAlgorithm algorithm_) {
        super("Алгоритм");

        mainMenuFrame = mainMenuFrame_;
        algorithm = algorithm_;
        graph = graph_;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        setResizable(false);
        setLayout(null);

        // закрытие окна
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
        for(int i = 0; i < graph.numberOfVertices; ++i)
            startVertexComboBox.addItem(alphabet[i]);
        startVertexComboBox.setSelectedIndex(0);
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
        for(int i = 0; i < graph.numberOfVertices; ++i)
            if (graph.linksMatrix[0][i])
                endVertexComboBox.addItem(alphabet[i]);
        endVertexComboBox.setSelectedIndex(0);
        endVertexComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        endVertexComboBox.setName("EndVertexComboBox");
        endVertexComboBox.addActionListener(this);
        sidePanel.add(endVertexComboBox);

        setVertices(); // задать вершины для алгоритма


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

        // панель с графом
        JPanel graphPanel = new JPanel();
        graphPanel.setBounds(300, 10, 685, 660);
        graphPanel.setLayout(null);
        TitledBorder graphPanelTitle = BorderFactory.createTitledBorder("");
        graphPanel.setBorder(graphPanelTitle);

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
    private void setVertices() {
        int startVertex = 0;
        int endVertex = 0;

        for (int i = 0; i < alphabet.length; i++) {
            if (startVertexComboBox.getSelectedItem() == alphabet[i])
                startVertex = i;
            if (endVertexComboBox.getSelectedItem() == alphabet[i])
                endVertex = i;
        }

        algorithm.initPath(startVertex, endVertex);
    }

    private void showPathOfAnt(List<Integer> pathOfAnt) {
        String path = "";

        for (Integer i : pathOfAnt) {
            if (i != 0) path += " - ";
            path += alphabet[i];
        }

        pathOfAntLabel.setText(path);
    }

    // нажатие кнопки
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // списки вершин
        if (source instanceof JComboBox) {
            JComboBox cb = (JComboBox) e.getSource();
            String cmd = cb.getName();

            switch (cmd) {
                case "StartVertexComboBox":
                    int index1 = cb.getSelectedIndex();
                    endVertexComboBox.removeAllItems();
                    for(int i = 0; i < graph.numberOfVertices; ++i)
                        if (graph.linksMatrix[index1][i])
                            endVertexComboBox.addItem(alphabet[i]);
                    setVertices();
                    break;

                case "EndVertexComboBox":
                    setVertices();
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
                    pathOfAnt = algorithm.autoAlgorithm();
                    showPathOfAnt(pathOfAnt);
                    break;

                case "ClearButton":
                    enableComboBoxes();
                    pathOfAntLabel.setText("");
                    autoStepButton.setEnabled(true);
                    algorithm.refresh();
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
