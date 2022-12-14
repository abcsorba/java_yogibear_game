package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;
import model.Levels;
import res.ResourceLoader;

public class Board extends JPanel {

    private Levels levels;
    private final Image bear, grass, mountain, policeman, picnicbasket, tree;
    private final double scale;
    private final int scaled_size;
    private final int n = 12;
    private final int tile_size = 32;

    public Board(Levels level) throws IOException {
        levels = level;
        scale = 2.0;
        scaled_size = (int) (scale * tile_size);
        bear = ResourceLoader.loadImage("res/bearv2.png");
        grass = ResourceLoader.loadImage("res/grassv2.png");
        tree = ResourceLoader.loadImage("res/treev2.png");
        picnicbasket = ResourceLoader.loadImage("res/picnicbasketv2.png");
        policeman = ResourceLoader.loadImage("res/policev2.png");
        mountain = ResourceLoader.loadImage("res/mountainv2.png");
    }

    /**
     * repaints and resizes the window
     * @return true
     */
    public boolean refresh() {
        Dimension dim = new Dimension(n * scaled_size, n * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }

    /**
     * draws on the window
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        int w = n;
        int h = n;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Image img = null;
                String b = levels.getLevel().getLevel()[j][i];
                switch (b) {
                    case "M":
                        img = mountain;
                        break;
                    case "B":
                        img = bear;
                        break;
                    case "G":
                        img = grass;
                        break;
                    case "T":
                        img = tree;
                        break;
                    case "O":
                    case "V":
                    case "H":
                        img = policeman;
                        break;
                    case "P":
                        img = picnicbasket;
                        break;
                }
                if (img == null) {
                    continue;
                }
                gr.drawImage(img, i * scaled_size, j * scaled_size, scaled_size, scaled_size, null);
            }
        }
    }
}
