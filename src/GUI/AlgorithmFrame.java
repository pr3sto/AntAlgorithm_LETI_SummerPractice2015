package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

// окно "алгоритм"
public class AlgorithmFrame extends JFrame implements ActionListener {

    private final MainMenuFrame mainMenuFrame; // главное окно

    private JLabel startVertexLabel;
    private JComboBox startVertexComboBox;
    private JLabel endVertexLabel;
    private JComboBox endVertexComboBox;

    // конструктор
    public AlgorithmFrame(MainMenuFrame frame) {
        super("Алгоритм");

        mainMenuFrame = frame;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
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

        // боковая панель
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(10, 10, 280, 660);
        sidePanel.setLayout(null);
        TitledBorder title = BorderFactory.createTitledBorder("");
        sidePanel.setBorder(title);

        startVertexLabel = new JLabel("Начальная вершина:");
        startVertexLabel.setFont(new Font("Arial", Font.BOLD, 14));
        startVertexLabel.setBounds(20, 30, 240, 40);
        sidePanel.add(startVertexLabel);

        startVertexComboBox = new JComboBox();
        startVertexComboBox.setFont(new Font("Arial", Font.BOLD, 14));
        startVertexComboBox.setBounds(20, 70, 240, 40);
        startVertexComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startVertexComboBox.setName("StartVertexComboBox");
        startVertexComboBox.addActionListener(this);
        sidePanel.add(startVertexComboBox);

        endVertexLabel = new JLabel("Конечная вершина:");
        endVertexLabel.setFont(new Font("Arial", Font.BOLD, 14));
        endVertexLabel.setBounds(20, 120, 240, 40);
        sidePanel.add(endVertexLabel);

        endVertexComboBox = new JComboBox();
        endVertexComboBox.setFont(new Font("Arial", Font.BOLD, 14));
        endVertexComboBox.setBounds(20, 160, 240, 40);
        endVertexComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        endVertexComboBox.setName("EndVertexComboBox");
        endVertexComboBox.addActionListener(this);
        sidePanel.add(endVertexComboBox);

        // кнопки
        JButton nextStepButton = new JButton("Следующий шаг");
        nextStepButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextStepButton.setBounds(20, 260, 240, 60);
        nextStepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextStepButton.setActionCommand("NextStepButton");
        nextStepButton.addActionListener(this);
        sidePanel.add(nextStepButton);

        JButton autoStepButton = new JButton("Авто");
        autoStepButton.setFont(new Font("Arial", Font.BOLD, 14));
        autoStepButton.setBounds(20, 330, 240, 60);
        autoStepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        autoStepButton.setActionCommand("AutoStepButton");
        autoStepButton.addActionListener(this);
        sidePanel.add(autoStepButton);

        JButton clearButton = new JButton("Очистить");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBounds(20, 400, 240, 60);
        clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearButton.setActionCommand("ClearButton");
        clearButton.addActionListener(this);
        sidePanel.add(clearButton);

        JButton backButton = new JButton("В главное меню");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(20, 550, 240, 60);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setActionCommand("BackButton");
        backButton.addActionListener(this);
        sidePanel.add(backButton);

        // панель с графом
        JPanel graphPanel = new JPanel();
        graphPanel.setBounds(300, 10, 685, 660);
        graphPanel.setLayout(null);
        TitledBorder panelTitle = BorderFactory.createTitledBorder("");
        graphPanel.setBorder(panelTitle);

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

    // нажатие кнопки
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {

            // combo box
            case "StartVertexComboBox":
                break;
            case "EndVertexComboBox":
                break;

            // кнопки
            case "NextStepButton":
                disableComboBoxes();
                break;
            case "AutoStepButton":
                disableComboBoxes();
                break;
            case "ClearButton":
                enableComboBoxes();
                break;
            case "BackButton":
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                mainMenuFrame.setVisible(true);
                break;
        }
    }
}
