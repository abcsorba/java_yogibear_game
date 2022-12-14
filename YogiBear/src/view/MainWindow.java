package view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import model.*;

public class MainWindow extends JFrame {

    private final Levels levels;
    private Board board;
    private final JLabel gameStatLabel = new JLabel();
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private int time = 0;

    private String name;
    private int maxLevel;
    private int elapsedTime;
    private boolean end;
    private boolean careerMode = true;
    
    private Database db = new Database();

    public MainWindow() throws IOException {
        levels = new Levels();
        gameStatLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        gameStatLabel.setVerticalTextPosition(JLabel.CENTER);

        setTitle("Yogi Bear");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/bearv2.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        JMenu menuGameLevel = new JMenu("Levels");
        JMenuItem newGame = new JMenuItem(new AbstractAction("New game") {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.print("HELLO");
                careerMode = true;
                levels.loadLevel(0);
                levels.setHealth(3);
                time = 0;
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                refreshGameStatLabel();
                board.refresh();
                pack();
            }
        });
        createGameLevelMenuItems(menuGameLevel);

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        JMenuItem topTen = new JMenuItem(new AbstractAction("Leaderboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.getTopTen();
            }
        });

        menuGame.add(menuGameLevel);
        menuGame.add(topTen);
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        menuBar.add(newGame);
        setJMenuBar(menuBar);

        setLayout(new BorderLayout(0, 10));

        try {
            add(board = new Board(levels), BorderLayout.CENTER);
        } catch (IOException ex) {
        }
        levels.loadLevel(0);
        add(gameStatLabel, BorderLayout.NORTH);
        refreshGameStatLabel();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);
                if (timerTask == null) {
                    timer.scheduleAtFixedRate(timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            time++;
                            levels.movePolice();
                            board.repaint();
                            refreshGameStatLabel();
                        }
                    }, 0, 1000);
                }
                int kk = ke.getKeyCode();
                Direction d = null;

                switch (kk) {
                    case KeyEvent.VK_LEFT:
                        d = Direction.LEFT;
                        levels.moveBear(d);
                        end = levels.checkWin();
                        break;
                    case KeyEvent.VK_RIGHT:
                        d = Direction.RIGHT;
                        levels.moveBear(d);
                        end = levels.checkWin();
                        break;
                    case KeyEvent.VK_UP:
                        d = Direction.UP;
                        levels.moveBear(d);
                        end = levels.checkWin();
                        break;
                    case KeyEvent.VK_DOWN:
                        d = Direction.DOWN;
                        levels.moveBear(d);
                        end = levels.checkWin();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        levels.loadLevel((0));
                        levels.setHealth(3);
                        time = 0;
                        timerTask.cancel();
                        timerTask = null;
                }
                if (careerMode && end) {
                    timerTask.cancel();
                    timerTask = null;
                    name = JOptionPane.showInputDialog("Enter your name to be saved on the leaderboard!");
                    maxLevel = levels.levMax;
                    elapsedTime = time;
                    time = 0;
                    db.insertToTable(name, maxLevel, elapsedTime);
//                    System.out.println(name);
//                    System.out.println(maxLevel);
//                    System.out.println(elapsedTime);
                }
                refreshGameStatLabel();
                board.repaint();
            }
        });

        setResizable(false);
        setLocationRelativeTo(null);
        board.refresh();
        pack();
        refreshGameStatLabel();
        setVisible(true);
    }
    
    /**
     * refreshes the label containing game information
     */
    private void refreshGameStatLabel() {
        gameStatLabel.setText("Level " + levels.getCurrentLevelNum() + ", picnic baskets left: " + levels.getNumOfPicnicBaskets() + ", Health: " + levels.getHealth() + ", Elapsed time: " + time + " seconds");
    }
    
    /**
     * creates a menuItems for selecting levels
     * @param menu where to create the menuItems
     */
    private void createGameLevelMenuItems(JMenu menu) {
        for (int i = 0; i < 10; i++) {
            int level = i;
            JMenuItem difficultyMenu = new JMenuItem(new AbstractAction("Level " + (level + 1)) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    careerMode = false;
                    levels.loadLevel(level);
                    board.refresh();
                    time = 0;
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    refreshGameStatLabel();
                    pack();
                }
            });
            menu.add(difficultyMenu);
        }
    }

    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {
        }
    }
}
