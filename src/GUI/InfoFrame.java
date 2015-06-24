package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

// окно "информация об алгоритме"
public class InfoFrame extends JFrame implements ActionListener {

    private final MainMenuFrame mainMenuFrame;
    private JButton backButton;
    private ImageIcon iconImage;
    private ImageIcon infoImage;
    private JScrollPane panel;

    // конструктор
    public InfoFrame(MainMenuFrame frame) {
        super("Информация об алгоритме");

        mainMenuFrame = frame;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setResizable(false);
        setLayout(null);

        // центрирование окна
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension( (int)dimension.getWidth() / 2, (int)dimension.getHeight()/2 ));
        getContentPane().add(emptyLabel, BorderLayout.CENTER);
        setLocation((int) dimension.getWidth() / 4, (int) dimension.getHeight()/4);

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
        iconImage = new ImageIcon("src/images/icon.png");
        setIconImage(iconImage.getImage());

        // кнопка
        backButton = new JButton("В главное меню");
        backButton.setBounds(175, 306, 250, 60);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(this);
        backButton.setActionCommand("Back");
        add(backButton);

        // картинка с информацией об алгоритме
        infoImage = new ImageIcon("src/images/info.png");
        // панель с прокруткой
        panel = new JScrollPane(new JLabel(infoImage));
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setBounds(0, 0, 600, 300);
        // скорость прокрутки
        JScrollBar jsp = panel.getVerticalScrollBar();
        jsp.setUnitIncrement(25);
        add(panel);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(cmd.equals("Back")) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            mainMenuFrame.setVisible(true);
        }
    }
}
