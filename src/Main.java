import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame mainMenu = new JFrame("Главное меню");
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton buttonStart = new JButton("Запуск алгоритма");
        JButton buttonGraphSettings = new JButton("Ввод графа");
        JButton buttonInfo = new JButton("Информация об алгоритме");
        JButton buttonExit = new JButton("Выход");

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.PAGE_AXIS));
        mainMenuPanel.add(buttonStart);
        mainMenuPanel.add(buttonGraphSettings);
        mainMenuPanel.add(buttonInfo);
        mainMenuPanel.add(buttonExit);

        mainMenu.add(mainMenuPanel);
        mainMenu.pack();
        mainMenu.setVisible(true);
    }
}