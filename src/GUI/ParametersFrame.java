package GUI;

import java.io.*;
import java.util.*;
import java.text.NumberFormat;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import Algorithm.Graph;
import Algorithm.Pair;

// окно "Параметры"
public class ParametersFrame extends JFrame
        implements ActionListener, ChangeListener, PropertyChangeListener {


    private final MainMenuFrame mainMenuFrame;    // главное окно
    private Pair<Double, Double> params;          // параметры (жадность, скорость испарения)
    private Graph graph;


    // параметры генерации
    private int numberOfVertices = 5;             // количестов вершин
    private int percentOfEdges = 50;              // процент ребер
    private Pair<Integer, Integer> rangeOfWeights
                        = new Pair<>(1,100);      // диапозон весов


    // группа "из файла"
    private JFileChooser fileChooser;
    private File file;
    private JTextField filePathTextField;
    private JButton chooseFileButton;
    private JButton createGraphFromFileButton;
    // группа "генерация"
    private JLabel numberOfVerticesLabel;
    private JSlider numberOfVerticesSlider;
    private JLabel percentOfEdgesLabel;
    private JSlider percentOfEdgesSlider;
    private JLabel rangeOfWeightsLabel;
    private JLabel rangeFromLabel;
    private JLabel rangeToLabel;
    private JFormattedTextField rangeFromField;
    private JFormattedTextField rangeToField;
    private JButton createGraphGenerateButton;
    // группа "ручной ввод"
    private JButton handEnterGraphButton;

    // кнопка "показать граф"
    private JButton showGraphButton;


    // конструктор
    public ParametersFrame(MainMenuFrame frame, Graph graph_, Pair<Double, Double> params_) {
        super("Параметры");

        mainMenuFrame = frame;
        params = params_;
        graph = graph_;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        setLayout(null);

        // закрытие окна
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
                event.getWindow().setVisible(false);
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

        // панель "ввод графа"
        JPanel enterGraphPanel = new JPanel();
        enterGraphPanel.setLayout(null);
        enterGraphPanel.setBounds(20, 10, 760, 290);
        TitledBorder title = BorderFactory.createTitledBorder("Ввод графа:");
        enterGraphPanel.setBorder(title);

        // кнопки выбора ввода
        JRadioButton fromFileRadioButton = new JRadioButton("Из файла:");
        fromFileRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        fromFileRadioButton.setBounds(20, 40, 90, 20);
        fromFileRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fromFileRadioButton.setActionCommand("FromFileRadioButton");
        fromFileRadioButton.addActionListener(this);
        fromFileRadioButton.setSelected(true);

        JRadioButton generateGraphRadioButton = new JRadioButton("Генерация:");
        generateGraphRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        generateGraphRadioButton.setBounds(20, 85, 100, 20);
        generateGraphRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generateGraphRadioButton.setActionCommand("GenerateRadioButton");
        generateGraphRadioButton.addActionListener(this);

        JRadioButton handEnterGraphRadioButton = new JRadioButton("Ручной ввод:");
        handEnterGraphRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        handEnterGraphRadioButton.setBounds(20, 240, 110, 20);
        handEnterGraphRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        handEnterGraphRadioButton.setActionCommand("HandEnterRadioButton");
        handEnterGraphRadioButton.addActionListener(this);

        // группа кнопок выбора
        ButtonGroup enterGraphGroup = new ButtonGroup();
        enterGraphGroup.add(fromFileRadioButton);
        enterGraphGroup.add(generateGraphRadioButton);
        enterGraphGroup.add(handEnterGraphRadioButton);
        // добавить на панель
        enterGraphPanel.add(fromFileRadioButton);
        enterGraphPanel.add(generateGraphRadioButton);
        enterGraphPanel.add(handEnterGraphRadioButton);


        // из файла
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        chooseFileButton = new JButton("Выбрать файл");
        chooseFileButton.setFont(new Font("Arial", Font.BOLD, 14));
        chooseFileButton.setBounds(140, 30, 150, 40);
        chooseFileButton.setActionCommand("ChooseFileButton");
        chooseFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chooseFileButton.addActionListener(this);
        enterGraphPanel.add(chooseFileButton);

        filePathTextField = new JTextField("Путь к файлу . . .");
        filePathTextField.setBounds(310, 35, 260, 30);
        filePathTextField.setEditable(false);
        filePathTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        enterGraphPanel.add(filePathTextField);

        createGraphFromFileButton = new JButton("Создать граф");
        createGraphFromFileButton.setFont(new Font("Arial", Font.BOLD, 14));
        createGraphFromFileButton.setBounds(590, 30, 150, 40);
        createGraphFromFileButton.setActionCommand("CreateGraphFromFileButton");
        createGraphFromFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createGraphFromFileButton.addActionListener(this);
        createGraphFromFileButton.setEnabled(false);
        enterGraphPanel.add(createGraphFromFileButton);


        // генерация
        numberOfVerticesLabel = new JLabel("Количество вершин:");
        numberOfVerticesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        numberOfVerticesLabel.setBounds(140, 75, 140, 40);
        enterGraphPanel.add(numberOfVerticesLabel);
        // слайдер - количество вершин
        numberOfVerticesSlider = new JSlider(JSlider.HORIZONTAL, 2, 10, numberOfVertices);
        numberOfVerticesSlider.setBounds(310, 85, 425, 40);
        numberOfVerticesSlider.setMajorTickSpacing(1);
        numberOfVerticesSlider.setMinorTickSpacing(1);
        numberOfVerticesSlider.setPaintTicks(true);
        numberOfVerticesSlider.setPaintLabels(true);
        numberOfVerticesSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        numberOfVerticesSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        numberOfVerticesSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        numberOfVerticesSlider.setName("NumberOfVerticesSlider");
        numberOfVerticesSlider.addChangeListener(this);
        enterGraphPanel.add(numberOfVerticesSlider);

        percentOfEdgesLabel = new JLabel("Процент ребер:");
        percentOfEdgesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        percentOfEdgesLabel.setBounds(140, 130, 120, 40);
        enterGraphPanel.add(percentOfEdgesLabel);
        // слайдер - процент ребер
        percentOfEdgesSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, percentOfEdges);
        percentOfEdgesSlider.setBounds(310, 140, 425, 40);
        percentOfEdgesSlider.setMajorTickSpacing(10);
        percentOfEdgesSlider.setMinorTickSpacing(5);
        percentOfEdgesSlider.setPaintTicks(true);
        percentOfEdgesSlider.setPaintLabels(true);
        percentOfEdgesSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        percentOfEdgesSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        percentOfEdgesSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        percentOfEdgesSlider.setName("PercentOfEdgesSlider");
        percentOfEdgesSlider.addChangeListener(this);
        enterGraphPanel.add(percentOfEdgesSlider);

        // ----- форматтер ввода ------
        NumberFormat nf = NumberFormat.getIntegerInstance();
        NumberFormatter nff= new NumberFormatter(nf);
        nff.setMinimum(1);
        nff.setMaximum(100);
        // ----------------------------

        // поля - диапозон весов
        rangeOfWeightsLabel = new JLabel("Диапозон весов:");
        rangeOfWeightsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeOfWeightsLabel.setBounds(140, 185, 120, 40);
        enterGraphPanel.add(rangeOfWeightsLabel);

        // от
        rangeFromLabel = new JLabel("От:");
        rangeFromLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeFromLabel.setBounds(310, 185, 30, 40);
        enterGraphPanel.add(rangeFromLabel);

        rangeFromField = new JFormattedTextField(nff);
        rangeFromField.setValue(rangeOfWeights.first);
        rangeFromField.setBounds(340, 190, 50, 30);
        rangeFromField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        rangeFromField.setName("RangeFromField");
        rangeFromField.addPropertyChangeListener(this);
        enterGraphPanel.add(rangeFromField);

        // до
        rangeToLabel = new JLabel("До:");
        rangeToLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeToLabel.setBounds(420, 185, 30, 40);
        enterGraphPanel.add(rangeToLabel);

        rangeToField = new JFormattedTextField(nff);
        rangeToField.setValue(rangeOfWeights.second);
        rangeToField.setBounds(450, 190, 50, 30);
        rangeToField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        rangeToField.setName("RangeToField");
        rangeToField.addPropertyChangeListener(this);
        enterGraphPanel.add(rangeToField);

        // кнопка "создать граф"
        createGraphGenerateButton = new JButton("Создать граф");
        createGraphGenerateButton.setFont(new Font("Arial", Font.BOLD, 14));
        createGraphGenerateButton.setBounds(590, 185, 150, 40);
        createGraphGenerateButton.setActionCommand("CreateGraphGenerateButton");
        createGraphGenerateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createGraphGenerateButton.addActionListener(this);
        enterGraphPanel.add(createGraphGenerateButton);


        // ручной ввод
        handEnterGraphButton = new JButton("Ввести граф");
        handEnterGraphButton.setFont(new Font("Arial", Font.BOLD, 14));
        handEnterGraphButton.setBounds(140, 230, 150, 40);
        handEnterGraphButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        handEnterGraphButton.setActionCommand("EnterGraphButton");
        handEnterGraphButton.addActionListener(this);
        enterGraphPanel.add(handEnterGraphButton);


        // панель "дополнительные параметры"
        JPanel paramsPanel = new JPanel();
        paramsPanel.setLayout(null);
        paramsPanel.setBounds(20, 320, 760, 150);
        TitledBorder panelTitle = BorderFactory.createTitledBorder("Дополнительные параметры:");
        paramsPanel.setBorder(panelTitle);

        // --- для шкалы  слайдеров ---
        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0.0"));
        labelTable.put(50, new JLabel("0.5"));
        labelTable.put(100, new JLabel("1.0"));
        // ----------------------------

        JLabel greedOfAlgorithmLabel = new JLabel("\"Жадность\" алгоритма:");
        greedOfAlgorithmLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        greedOfAlgorithmLabel.setBounds(20, 20, 160, 40);
        paramsPanel.add(greedOfAlgorithmLabel);
        // слайдер - жадность алгоритма
        Double tmp = params.first * 100;
        Integer greedInt = tmp.intValue();
        JSlider greedOfAlgorithmSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, greedInt);
        greedOfAlgorithmSlider.setBounds(310, 30, 425, 40);
        greedOfAlgorithmSlider.setLabelTable(labelTable);
        greedOfAlgorithmSlider.setMajorTickSpacing(50);
        greedOfAlgorithmSlider.setMinorTickSpacing(10);
        greedOfAlgorithmSlider.setPaintTicks(true);
        greedOfAlgorithmSlider.setPaintLabels(true);
        greedOfAlgorithmSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        greedOfAlgorithmSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        greedOfAlgorithmSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        greedOfAlgorithmSlider.setName("GreedOfAlgorithmSlider");
        greedOfAlgorithmSlider.addChangeListener(this);
        paramsPanel.add(greedOfAlgorithmSlider);

        JLabel rateOfEvaporationLabel = new JLabel("Скорость испарения феромона:");
        rateOfEvaporationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rateOfEvaporationLabel.setBounds(20, 70, 220, 40);
        paramsPanel.add(rateOfEvaporationLabel);
        // слайдер - скорость испарения феромона
        Double tmp1 = params.second*100;
        Integer evaporationSpeedInt = tmp1.intValue();
        JSlider rateOfEvaporationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, evaporationSpeedInt);
        rateOfEvaporationSlider.setBounds(310, 80, 425, 40);
        rateOfEvaporationSlider.setLabelTable(labelTable);
        rateOfEvaporationSlider.setMajorTickSpacing(50);
        rateOfEvaporationSlider.setMinorTickSpacing(10);
        rateOfEvaporationSlider.setPaintTicks(true);
        rateOfEvaporationSlider.setPaintLabels(true);
        rateOfEvaporationSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        rateOfEvaporationSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rateOfEvaporationSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rateOfEvaporationSlider.setName("RateOfEvaporationSlider");
        rateOfEvaporationSlider.addChangeListener(this);
        paramsPanel.add(rateOfEvaporationSlider);


        // кнопки
        JButton backButton = new JButton("В главное меню");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(80, 490, 300, 60);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setActionCommand("BackButton");
        backButton.addActionListener(this);
        add(backButton);

        showGraphButton = new JButton("Показать граф");
        showGraphButton.setFont(new Font("Arial", Font.BOLD, 14));
        showGraphButton.setBounds(420, 490, 300, 60);
        if (!graph.isCreated()) {
            showGraphButton.setText("Показать граф (нет графа)");
            showGraphButton.setEnabled(false);
        }
        showGraphButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showGraphButton.setActionCommand("ShowGraphButton");
        showGraphButton.addActionListener(this);
        add(showGraphButton);


        add(enterGraphPanel);
        add(paramsPanel);
        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);

        // по-умолчанию - выбор из файла
        enableEnterFromFileGraph();
        disableGenerationGraph();
        disableHandEnterGraph();
    }

    // заблокировать компоненты (radio button)
    private void disableEnterFromFileGraph() {
        chooseFileButton.setEnabled(false);
        filePathTextField.setEnabled(false);
        createGraphFromFileButton.setEnabled(false);
    }
    private void disableGenerationGraph() {
        numberOfVerticesLabel.setEnabled(false);
        numberOfVerticesSlider.setEnabled(false);
        percentOfEdgesLabel.setEnabled(false);
        percentOfEdgesSlider.setEnabled(false);
        rangeOfWeightsLabel.setEnabled(false);
        rangeFromLabel.setEnabled(false);
        rangeToLabel.setEnabled(false);
        rangeFromField.setEnabled(false);
        rangeToField.setEnabled(false);
        createGraphGenerateButton.setEnabled(false);
    }
    private void disableHandEnterGraph() {
        handEnterGraphButton.setEnabled(false);
    }

    // разблокировать компоненты (radio button)
    private void enableEnterFromFileGraph() {
        chooseFileButton.setEnabled(true);
        filePathTextField.setEnabled(true);
        if (file != null) createGraphFromFileButton.setEnabled(true);
    }
    private void enableGenerationGraph() {
        numberOfVerticesLabel.setEnabled(true);
        numberOfVerticesSlider.setEnabled(true);
        percentOfEdgesLabel.setEnabled(true);
        percentOfEdgesSlider.setEnabled(true);
        rangeOfWeightsLabel.setEnabled(true);
        rangeFromLabel.setEnabled(true);
        rangeToLabel.setEnabled(true);
        rangeFromField.setEnabled(true);
        rangeToField.setEnabled(true);
        createGraphGenerateButton.setEnabled(true);
    }
    private void enableHandEnterGraph() {
        handEnterGraphButton.setEnabled(true);
    }

    // слайдеры
    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();

        if (source instanceof JSlider) {
            JSlider slider = (JSlider)source;

            switch (slider.getName()) {
                case"NumberOfVerticesSlider":
                    numberOfVertices = slider.getValue();
                    break;

                case "PercentOfEdgesSlider":
                    percentOfEdges = slider.getValue();
                    break;

                case "GreedOfAlgorithmSlider":
                    params.first = new Integer(slider.getValue()).doubleValue()/100;
                    break;

                case "RateOfEvaporationSlider":
                    params.second = new Integer(slider.getValue()).doubleValue()/100;
                    break;

                default:
                    break;
            }
        }
    }

    // поля "от" и "до"
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        JFormattedTextField field = (JFormattedTextField)source;

        switch (field.getName()) {
            case "RangeFromField":
                try {
                    rangeOfWeights.first = Integer.parseInt(rangeFromField.getText());
                    if (Integer.parseInt(rangeFromField.getText()) > Integer.parseInt(rangeToField.getText())) {
                        rangeToField.setValue(100);
                    }
                } catch (Exception e1) { }
                break;

            case "RangeToField":
                try {
                    rangeOfWeights.second = Integer.parseInt(rangeToField.getText());
                    if (Integer.parseInt(rangeFromField.getText()) > Integer.parseInt(rangeToField.getText())) {
                        rangeFromField.setValue(1);
                    }
                } catch (Exception e1) { }
                break;
        }
    }

    // нажатие кнопок
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {

            // radio buttons
            case "FromFileRadioButton":
                enableEnterFromFileGraph();
                disableGenerationGraph();
                disableHandEnterGraph();
                break;

            case "GenerateRadioButton":
                enableGenerationGraph();
                disableEnterFromFileGraph();
                disableHandEnterGraph();
                break;

            case "HandEnterRadioButton":
                enableHandEnterGraph();
                disableGenerationGraph();
                disableEnterFromFileGraph();
                break;

            // кнопки
            case "ChooseFileButton":
                int returnVal = fileChooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    filePathTextField.setText(file.getPath());
                    createGraphFromFileButton.setEnabled(true);
                }
                break;

            case "CreateGraphFromFileButton":
                try {
                    if (graph.createGraphFromFile(new Scanner(file))) {
                        showGraphButton.setText("Показать граф");
                        showGraphButton.setEnabled(true);

                        JOptionPane.showMessageDialog(null,
                                "Граф успешно создан!", "Граф создан", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Невозможно считать граф из файла!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (FileNotFoundException e1) { }
                break;

            case "CreateGraphGenerateButton":
                graph.createGraph(numberOfVertices);
                graph.generateGraph(percentOfEdges, rangeOfWeights.first, rangeOfWeights.second);

                showGraphButton.setText("Показать граф");
                showGraphButton.setEnabled(true);

                JOptionPane.showMessageDialog(null,
                        "Граф успешно создан!", "Граф создан", JOptionPane.PLAIN_MESSAGE);
                break;

            case "EnterGraphButton":
                break;

            case "ShowGraphButton":
                break;

            case "BackButton":
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                mainMenuFrame.setVisible(true);
                break;

            default:
                break;
        }
    }
}
