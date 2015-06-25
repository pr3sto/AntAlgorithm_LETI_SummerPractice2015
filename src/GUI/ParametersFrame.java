package GUI;

import java.io.*;
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

// окно "Параметры"
public class ParametersFrame extends JFrame
        implements ActionListener, ChangeListener, PropertyChangeListener {

    private final MainMenuFrame mainMenuFrame; // главное окно

    // группа "из файла"
    private JFileChooser fileChooser;
    private File file;
    private JTextField filePathTextField;
    private JButton chooseFileButton;

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

    // группа "ручной ввод"
    private JButton handEnterGraphButton;

    // конструктор
    public ParametersFrame(MainMenuFrame frame) {
        super("Параметры");

        mainMenuFrame = frame;

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
        ImageIcon icon = new ImageIcon("src/images/icon.png");
        setIconImage(icon.getImage());

        // панель Ввод графа
        JPanel enterGraphPanel = new JPanel();
        enterGraphPanel.setLayout(null);
        enterGraphPanel.setBounds(20, 10, 760, 290);
        TitledBorder title = BorderFactory.createTitledBorder("Ввод графа:");
        enterGraphPanel.setBorder(title);

        // кнопки выбора ввода
        JRadioButton fromFileRadioButton = new JRadioButton("Из файла:");
        fromFileRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        fromFileRadioButton.setSelected(true);
        fromFileRadioButton.setActionCommand("FromFileRadioButton");
        fromFileRadioButton.setBounds(20, 40, 100, 20);
        fromFileRadioButton.addActionListener(this);

        JRadioButton generateGraphRadioButton = new JRadioButton("Генерация:");
        generateGraphRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        generateGraphRadioButton.setActionCommand("GenerateRadioButton");
        generateGraphRadioButton.setBounds(20, 90, 100, 20);
        generateGraphRadioButton.addActionListener(this);

        JRadioButton handEnterGraphRadioButton = new JRadioButton("Ручной ввод:");
        handEnterGraphRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        handEnterGraphRadioButton.setActionCommand("HandEnterRadioButton");
        handEnterGraphRadioButton.setBounds(20, 240, 150, 20);
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
        chooseFileButton.addActionListener(this);
        chooseFileButton.setActionCommand("ChooseFileButton");
        enterGraphPanel.add(chooseFileButton);

        filePathTextField = new JTextField();
        filePathTextField.setEditable(false);
        filePathTextField.setBounds(310, 35, 430, 30);
        filePathTextField.setText("Путь к файлу . . .");
        enterGraphPanel.add(filePathTextField);


        // генерация
        numberOfVerticesLabel = new JLabel("Количество вершин:");
        numberOfVerticesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        numberOfVerticesLabel.setBounds(140, 80, 200, 40);
        enterGraphPanel.add(numberOfVerticesLabel);
        // слайдер - количество вершин
        numberOfVerticesSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        numberOfVerticesSlider.setBounds(310, 90, 425, 40);
        numberOfVerticesSlider.setMajorTickSpacing(1);
        numberOfVerticesSlider.setMinorTickSpacing(1);
        numberOfVerticesSlider.setPaintTicks(true);
        numberOfVerticesSlider.setPaintLabels(true);
        numberOfVerticesSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        numberOfVerticesSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        numberOfVerticesSlider.setName("NumberOfVerticesSlider");
        numberOfVerticesSlider.addChangeListener(this);
        enterGraphPanel.add(numberOfVerticesSlider);

        percentOfEdgesLabel = new JLabel("Процент ребер:");
        percentOfEdgesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        percentOfEdgesLabel.setBounds(140, 130, 200, 40);
        enterGraphPanel.add(percentOfEdgesLabel);
        // слайдер - процент ребер
        percentOfEdgesSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        percentOfEdgesSlider.setBounds(310, 140, 425, 40);
        percentOfEdgesSlider.setMajorTickSpacing(10);
        percentOfEdgesSlider.setMinorTickSpacing(5);
        percentOfEdgesSlider.setPaintTicks(true);
        percentOfEdgesSlider.setPaintLabels(true);
        percentOfEdgesSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        percentOfEdgesSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        percentOfEdgesSlider.setName("PercentOfEdgesSlider");
        percentOfEdgesSlider.addChangeListener(this);
        enterGraphPanel.add(percentOfEdgesSlider);

        rangeOfWeightsLabel = new JLabel("Диапозон весов:");
        rangeOfWeightsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeOfWeightsLabel.setBounds(140, 180, 200, 40);
        enterGraphPanel.add(rangeOfWeightsLabel);
        // поля - диапозон весов
        rangeFromLabel = new JLabel("От:");
        rangeFromLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeFromLabel.setBounds(310, 180, 200, 40);
        enterGraphPanel.add(rangeFromLabel);
        rangeToLabel = new JLabel("До:");
        rangeToLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeToLabel.setBounds(515, 180, 200, 40);
        enterGraphPanel.add(rangeToLabel);

        NumberFormat nf = NumberFormat.getIntegerInstance();
        NumberFormatter nff= new NumberFormatter(nf);
        nff.setMinimum(0);
        nff.setMaximum(100);

        rangeFromField = new JFormattedTextField(nff);
        rangeFromField.setValue(0);
        rangeFromField.setBounds(360, 185, 100, 30);
        rangeFromField.setName("RangeFromField");
        rangeFromField.addPropertyChangeListener(this);
        enterGraphPanel.add(rangeFromField);

        rangeToField = new JFormattedTextField(nff);
        rangeToField.setValue(100);
        rangeToField.setBounds(565, 185, 100, 30);
        rangeToField.setName("RangeToField");
        rangeToField.addPropertyChangeListener(this);
        enterGraphPanel.add(rangeToField);


        // ручной ввод
        handEnterGraphButton = new JButton("Ввести граф");
        handEnterGraphButton.setFont(new Font("Arial", Font.BOLD, 14));
        handEnterGraphButton.setBounds(140, 230, 150, 40);
        handEnterGraphButton.addActionListener(this);
        handEnterGraphButton.setActionCommand("EnterGraphButton");
        enterGraphPanel.add(handEnterGraphButton);


        // панель "дополнительные параметры"
        JPanel paramsPanel = new JPanel();
        paramsPanel.setLayout(null);
        paramsPanel.setBounds(20, 320, 760, 150);
        TitledBorder panelTitle = BorderFactory.createTitledBorder("Дополнительные параметры:");
        paramsPanel.setBorder(panelTitle);

        // ----------------------------
        Hashtable labelTable = new Hashtable();
        labelTable.put( 0, new JLabel("0.0") );
        labelTable.put( 5, new JLabel("0.5") );
        labelTable.put( 10, new JLabel("1.0") );
        // ----------------------------

        JLabel greedOfAlgorithmLabel = new JLabel("\"Жадность\" алгоритма:");
        greedOfAlgorithmLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        greedOfAlgorithmLabel.setBounds(20, 20, 200, 40);
        paramsPanel.add(greedOfAlgorithmLabel);
        // слайдер - жадность алгоритма
        JSlider greedOfAlgorithmSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        greedOfAlgorithmSlider.setBounds(310, 30, 425, 40);
        greedOfAlgorithmSlider.setLabelTable(labelTable);
        greedOfAlgorithmSlider.setMajorTickSpacing(1);
        greedOfAlgorithmSlider.setMinorTickSpacing(1);
        greedOfAlgorithmSlider.setPaintTicks(true);
        greedOfAlgorithmSlider.setPaintLabels(true);
        greedOfAlgorithmSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        greedOfAlgorithmSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        greedOfAlgorithmSlider.setName("GreedOfAlgorithmSlider");
        greedOfAlgorithmSlider.addChangeListener(this);
        paramsPanel.add(greedOfAlgorithmSlider);

        JLabel rateOfEvaporationLabel = new JLabel("Скорость испарения феромона:");
        rateOfEvaporationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rateOfEvaporationLabel.setBounds(20, 70, 300, 40);
        paramsPanel.add(rateOfEvaporationLabel);
        // слайдер - скорость испарения феромона
        JSlider rateOfEvaporationSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        rateOfEvaporationSlider.setBounds(310, 80, 425, 40);
        rateOfEvaporationSlider.setLabelTable(labelTable);
        rateOfEvaporationSlider.setMajorTickSpacing(1);
        rateOfEvaporationSlider.setMinorTickSpacing(1);
        rateOfEvaporationSlider.setPaintTicks(true);
        rateOfEvaporationSlider.setPaintLabels(true);
        rateOfEvaporationSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        rateOfEvaporationSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rateOfEvaporationSlider.setName("RateOfEvaporationSlider");
        rateOfEvaporationSlider.addChangeListener(this);
        paramsPanel.add(rateOfEvaporationSlider);


        // кнопки
        JButton backButton = new JButton("В главное меню");
        backButton.setBounds(80, 490, 300, 60);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(this);
        backButton.setActionCommand("BackButton");
        add(backButton);

        JButton showGraphButton = new JButton("Показать граф");
        showGraphButton.setBounds(420, 490, 300, 60);
        showGraphButton.setFont(new Font("Arial", Font.BOLD, 14));
        showGraphButton.addActionListener(this);
        showGraphButton.setActionCommand("ShowGraphButton");
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
    }
    private void disableHandEnterGraph() {
        handEnterGraphButton.setEnabled(false);
    }

    // разблокировать компоненты (radio button)
    private void enableEnterFromFileGraph() {
        chooseFileButton.setEnabled(true);
        filePathTextField.setEnabled(true);
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
                    break;

                case "PercentOfEdgesSlider":
                    break;

                case "GreedOfAlgorithmSlider":
                    break;

                case "RateOfEvaporationSlider":
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
                    if (Integer.parseInt(rangeFromField.getText()) > Integer.parseInt(rangeToField.getText())) {
                        rangeToField.setValue(100);
                    }
                } catch (Exception e1) { }
                break;

            case "RangeToField":
                try {
                    if (Integer.parseInt(rangeFromField.getText()) > Integer.parseInt(rangeToField.getText())) {
                        rangeFromField.setValue(0);
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
                }
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
