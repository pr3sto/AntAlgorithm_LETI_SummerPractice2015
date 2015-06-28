package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

// окно "информация об алгоритме"
public class InfoFrame extends JFrame implements ActionListener {

    private final MainMenuFrame mainMenuFrame; // ссылка на главное окно

    // конструктор
    public InfoFrame(MainMenuFrame mainMenuFrame_) {
        super("Информация об алгоритме");

        mainMenuFrame = mainMenuFrame_;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
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

        // кнопка
        JButton backButton = new JButton("В главное меню");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(250, 480, 300, 60);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setActionCommand("BackButton");
        backButton.addActionListener(this);
        add(backButton);

        // картинка с информацией об алгоритме
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/info.png"));
        ImageIcon infoImage = new ImageIcon(img);
        // панель с прокруткой
        JScrollPane panel = new JScrollPane(new JLabel(infoImage));
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setBounds(0, 0, 795, 450);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // скорость прокрутки
        JScrollBar jsp = panel.getVerticalScrollBar();
        jsp.setUnitIncrement(25);
        add(panel);

        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }

    // нажатие кнопки
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equals("BackButton")) {
            setVisible(false);
            dispose();
            mainMenuFrame.setVisible(true);
        }
    }
}
