package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

// окно "информация об алгоритме"
public class InfoFrame extends JFrame implements ActionListener {

    private final MainMenuFrame mainMenuFrame; // главное окно

    // конструктор
    public InfoFrame(MainMenuFrame frame) {
        super("Информация об алгоритме");

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

        // кнопка
        JButton backButton = new JButton("В главное меню");
        backButton.setBounds(250, 480, 300, 60);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(this);
        backButton.setActionCommand("BackButton");
        add(backButton);

        // картинка с информацией об алгоритме
        ImageIcon infoImage = new ImageIcon("src/images/info.png");
        // панель с прокруткой
        JScrollPane panel = new JScrollPane(new JLabel(infoImage));
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setBounds(0, 0, 800, 450);
        // скорость прокрутки
        JScrollBar jsp = panel.getVerticalScrollBar();
        jsp.setUnitIncrement(25);
        add(panel);

        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }

    // нажатие кнопки
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equals("BackButton")) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            mainMenuFrame.setVisible(true);
        }
    }
}
