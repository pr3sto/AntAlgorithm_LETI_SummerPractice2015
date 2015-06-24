package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

// окно "главное меню"
public class MainMenuFrame extends JFrame implements ActionListener {

    private JPanel mainMenuPanel;
    private JButton startButton;
    private JButton settingsButton;
    private JButton infoButton;
    private JButton exitButton;

    // конструктор
    public MainMenuFrame() {
        super("Главное меню");

        // окно
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setResizable(false);

        // центрирование окна
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension( (int)dimension.getWidth() / 2, (int)dimension.getHeight()/2 ));
        getContentPane().add(emptyLabel, BorderLayout.CENTER);
        setLocation((int)dimension.getWidth()/4, (int)dimension.getHeight()/4);

        // иконка
        ImageIcon image = new ImageIcon("src/images/icon.png");
        setIconImage(image.getImage());

        // панель для кнопок
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(null);

        // кнопки
        startButton = new JButton("Запуск алгоритма");
        startButton.setBounds(175, 50, 250, 60);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.addActionListener(this);
        startButton.setActionCommand("Start");
        mainMenuPanel.add(startButton);

        settingsButton = new JButton("Параметры");
        settingsButton.setBounds(175, 120, 250, 60);
        settingsButton.setFont(new Font("Arial", Font.BOLD, 14));
        settingsButton.addActionListener(this);
        settingsButton.setActionCommand("Parametrs");
        mainMenuPanel.add(settingsButton);

        infoButton = new JButton("Информация об алгоритме");
        infoButton.setBounds(175, 190, 250, 60);
        infoButton.setFont(new Font("Arial", Font.BOLD, 14));
        infoButton.addActionListener(this);
        infoButton.setActionCommand("Info");
        mainMenuPanel.add(infoButton);

        exitButton = new JButton("Выход");
        exitButton.setBounds(175, 260, 250, 60);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(this);
        exitButton.setActionCommand("Exit");
        mainMenuPanel.add(exitButton);

        // добавление панели на окно
        getContentPane().add(mainMenuPanel);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equals("Start")) {
            dispose();
        } else if(cmd.equals("Parametrs")) {
            dispose();
        } else if(cmd.equals("Info")) {
            new InfoFrame(this);
            setVisible(false);
            dispose();
        } else if(cmd.equals("Exit")) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    // main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // изменить стиль окна
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e1) {
                String message = "Error while changing Look and Feel!";
                JOptionPane.showMessageDialog(null, message);
            }

            // показать главное окно
            new MainMenuFrame();
        });
    }
}