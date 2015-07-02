package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import Graph.Graph;

public class ShowGraphFrame extends JFrame implements ActionListener {

    private final ParametersFrame parametersFrame; // ссылка на окно-родитель
    private Graph graph;                       // ссылка на граф

    // панель с графом
    class GraphPanel extends JPanel {

        public GraphPanel() {
            setBounds(300, 10, 685, 660);
            setLayout(null);
            TitledBorder graphPanelTitle = BorderFactory.createTitledBorder("");
            setBorder(graphPanelTitle);
        }

        final  BasicStroke stroke = new BasicStroke(2.0f);
        final  BasicStroke wideStroke = new BasicStroke(8.0f);

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke( stroke );


            // вершины
            for (int i = 0; i < graph.numberOfVertices; i++) {
                int x = graph.vertices.get(i).coordX;
                int y =  graph.vertices.get(i).coordY;
                String name = String.valueOf(graph.vertices.get(i).letter);

                g.drawOval(x, y, 50, 50);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString(name, x + 20, y + 30);
            }

            // ребра
            for (int i = 0; i < graph.edges.size(); i++) {
                int x1 = graph.vertices.get(graph.edges.get(i).firstNode).coordX + 25;
                int y1 = graph.vertices.get(graph.edges.get(i).firstNode).coordY + 25;
                int x2 = graph.vertices.get(graph.edges.get(i).secondNode).coordX + 25;
                int y2 = graph.vertices.get(graph.edges.get(i).secondNode).coordY + 25;

                int x11= (int)(x1 + 25 * (x2 - x1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int y11 = (int)(y1 + 25 * (y2 - y1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int x22 = (int)(x2 + 25 * (x1 - x2) /
                        Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                + Math.pow((double)(y1 - y2), 2.0)));

                int y22 = (int)(y2 + 25 * (y1 - y2) /
                        Math.sqrt(Math.pow((double)(x1 - x2), 2.0)
                                + Math.pow((double)(y1 - y2), 2.0)));


                g2.drawLine(x11, y11, x22, y22);

                int x= (int)(x1 + 70 * (x2 - x1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                int y = (int)(y1 + 70 * (y2 - y1) /
                        Math.sqrt(Math.pow((double)(x2 - x1), 2.0)
                                + Math.pow((double)(y2 - y1), 2.0)));

                // вес
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 15));
                g2.drawString(String.valueOf(graph.edges.get(i).weight), x - 8, y + 8);
                g2.setColor(Color.BLACK);
            }
        }
    }

    // конструктор
    public ShowGraphFrame(ParametersFrame parametersFrame_, Graph graph_) {
        super("Алгоритм");

        parametersFrame = parametersFrame_;
        graph = graph_;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        setResizable(false);
        setLayout(null);

        // действие при закрытии окна
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                parametersFrame.setVisible(true);
            }

            public void windowActivated(WindowEvent event) {
            }

            public void windowClosed(WindowEvent event) {
            }

            public void windowDeactivated(WindowEvent event) {
            }

            public void windowDeiconified(WindowEvent event) {
            }

            public void windowIconified(WindowEvent event) {
            }

            public void windowOpened(WindowEvent event) { }
        });

        // иконка
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/icon.png")));

        // боковая панель
        JPanel sidePanel = new JPanel();
        sidePanel.setBounds(10, 10, 280, 660);
        sidePanel.setLayout(null);
        TitledBorder sidePanelTitle = BorderFactory.createTitledBorder("");
        sidePanel.setBorder(sidePanelTitle);



        JButton backButton = new JButton("В главное меню");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(20, 270, 240, 60);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setActionCommand("BackButton");
        backButton.addActionListener(this);
        sidePanel.add(backButton);

        // панель для графа
        GraphPanel graphPanel = new GraphPanel();

        add(sidePanel);
        add(graphPanel);
        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);
    }

    // нажатие кнопки
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // кнопки
        if (source instanceof JButton) {
            JButton btn = (JButton) e.getSource();
            String cmd = btn.getActionCommand();

            switch(cmd) {

                case "BackButton":
                    setVisible(false);
                    dispose();
                    parametersFrame.setVisible(true);
                    break;
            }
        }
    }
}
