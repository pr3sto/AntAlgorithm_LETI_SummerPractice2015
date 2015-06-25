package GUI;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

// окно "главное меню"
public class MainMenuFrame extends JFrame implements ActionListener {

    // конструктор
    public MainMenuFrame() {
        super("Главное меню");

        // окно
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        setLayout(null);

        // иконка
        ImageIcon icon = new ImageIcon("src/images/icon.png");
        setIconImage(icon.getImage());

        // картинка для заголовка
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(null);
        TitledBorder title = BorderFactory.createTitledBorder("");
        titlePanel.setBorder(title);
        titlePanel.setBounds(20, 20, 760, 207);
        try {
            BufferedImage titleImage = ImageIO.read(new File("src/images/title.png"));
            JLabel picLabel = new JLabel(new ImageIcon(titleImage));
            picLabel.setBounds(0, 0, 760, 200);
            titlePanel.add(picLabel);
        } catch (IOException e) {
            String message = "Error while loading title.png!";
            JOptionPane.showMessageDialog(null, message);
        }
        getContentPane().add(titlePanel); // добавление заголовка на окно

        // панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBorder(title);
        buttonPanel.setBounds(230, 240, 340, 315);

        // кнопки
        JButton startButton = new JButton("Запуск алгоритма");
        startButton.setBounds(20, 20, 300, 60);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.addActionListener(this);
        startButton.setActionCommand("StartButton");
        buttonPanel.add(startButton);

        JButton settingsButton = new JButton("Параметры");
        settingsButton.setBounds(20, 90, 300, 60);
        settingsButton.setFont(new Font("Arial", Font.BOLD, 14));
        settingsButton.addActionListener(this);
        settingsButton.setActionCommand("ParametersButton");
        buttonPanel.add(settingsButton);

        JButton infoButton = new JButton("Информация об алгоритме");
        infoButton.setBounds(20, 160, 300, 60);
        infoButton.setFont(new Font("Arial", Font.BOLD, 14));
        infoButton.addActionListener(this);
        infoButton.setActionCommand("InfoButton");
        buttonPanel.add(infoButton);

        JButton exitButton = new JButton("Выход");
        exitButton.setBounds(20, 230, 300, 60);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(this);
        exitButton.setActionCommand("ExitButton");
        buttonPanel.add(exitButton);

        getContentPane().add(buttonPanel);// добавление панели на окно

        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }

    // нажатие кнопок
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {
            case "StartButton":
                new AlgorithmFrame(this);
                setVisible(false);
                dispose();
                break;

            case "ParametersButton":
                new ParametersFrame(this);
                setVisible(false);
                dispose();
                break;

            case "InfoButton":
                new InfoFrame(this);
                setVisible(false);
                dispose();
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