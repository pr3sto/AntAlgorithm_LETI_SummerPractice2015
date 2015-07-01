package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import Graph.Graph;
import Staff.Pair;

// окно "главное меню"
public class MainMenuFrame extends JFrame implements ActionListener {

    private Graph graph = new Graph();                            // граф
    private Pair<Double, Double> params = new Pair<> (0.5,        // жадность
                                                      0.5);       // скорость испарения феромонов
    private Pair<Integer, Integer> antParams = new Pair<> (50,    // кол-во блиц муравьев
                                                         5000);   // общее кол-во муравьев

    // окно параметров создается один раз (для сохранения параметров при деактивации окна)
    private ParametersFrame parametersFrame = new ParametersFrame(this, graph, params, antParams);

    private JButton startButton;

    // конструктор
    public MainMenuFrame() {
        super("Главное меню");

        // скрыть созданное окно параметров
        parametersFrame.setVisible(false);

        // окно
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        setLayout(null);

        // иконка
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/icon.png")));

        // картинка для заголовка
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(null);
        TitledBorder title = BorderFactory.createTitledBorder("");
        titlePanel.setBorder(title);
        titlePanel.setBounds(20, 20, 760, 206);

        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/title.png"));
        JLabel picLabel = new JLabel(new ImageIcon(img));
        picLabel.setBounds(0, 0, 760, 200);
        titlePanel.add(picLabel);

        add(titlePanel); // добавление заголовка на окно

        // панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBorder(title);
        buttonPanel.setBounds(230, 240, 340, 315);

        // кнопки
        startButton = new JButton("Запуск алгоритма");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBounds(20, 20, 300, 60);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setActionCommand("StartButton");
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        JButton settingsButton = new JButton("Параметры");
        settingsButton.setFont(new Font("Arial", Font.BOLD, 14));
        settingsButton.setBounds(20, 90, 300, 60);
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        settingsButton.setActionCommand("ParametersButton");
        settingsButton.addActionListener(this);
        buttonPanel.add(settingsButton);

        JButton infoButton = new JButton("Информация об алгоритме");
        infoButton.setFont(new Font("Arial", Font.BOLD, 14));
        infoButton.setBounds(20, 160, 300, 60);
        infoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        infoButton.setActionCommand("InfoButton");
        infoButton.addActionListener(this);
        buttonPanel.add(infoButton);

        JButton exitButton = new JButton("Выход");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBounds(20, 230, 300, 60);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setActionCommand("ExitButton");
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);

        add(buttonPanel); // добавление панели на окно

        // при открытии окна
        addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent event) { }
            public void windowClosing(WindowEvent event) {  }
            public void windowActivated(WindowEvent event) {
                if (!graph.isCreated()) {
                    startButton.setText("Запуск алгоритма (нет графа)");
                    startButton.setEnabled(false);
                } else {
                    startButton.setText("Запуск алгоритма");
                    startButton.setEnabled(true);
                }
            }
            public void windowClosed(WindowEvent event) { }
            public void windowDeactivated(WindowEvent event) { }
            public void windowDeiconified(WindowEvent event) { }
            public void windowIconified(WindowEvent event) { }
        });

        pack();
        setLocationRelativeTo(null);       // центрирование окна
        setVisible(true);
    }

    // нажатие кнопок
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {
            case "StartButton":
                new AlgorithmFrame(this, graph, params, antParams);
                setVisible(false);
                break;

            case "ParametersButton":
                parametersFrame.setVisible(true);
                setVisible(false);
                break;

            case "InfoButton":
                new InfoFrame(this);
                setVisible(false);
                break;

            case "ExitButton":
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;

            default:
                break;
        }
    }

    // main
    public static void main(String[] args) {

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                // изменить стиль окна
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null,
                            "Error while changing Look and Feel!", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // показать главное окно
                new MainMenuFrame();
            }});
    }
}