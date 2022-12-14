package model;

import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
import java.util.Scanner;
import res.ResourceLoader;

public class Levels {

    private final GameLevel gameL = new GameLevel();
    private Level level = new Level(gameL, 0);
    private int currentLevelNum = 0;
    private int health = 3;

    public String nameW;
    public int levMax;

    public Levels() {
        readLevels();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Level getLevel() {
        return level;
    }

    public int getCurrentLevelNum() {
        return currentLevelNum;
    }

    public String[][][] getGameLevel() {
        return gameL.gameLevel;
    }
    
    public int getNumOfPicnicBaskets() {
        return level.getNumOfPicnicBaskets();
    }
    
    /**
     * checks health stats and number of picnic baskets left and determines if the players game has ended
     * @return true if health is 0 or all the picnic baskets of the level have been collected 
     */
    public boolean checkWin() {
        if (level.getNumOfPicnicBaskets() == 0) {
            if (currentLevelNum == 10) {
                levMax = 10;
                health = 3;
                loadLevel(0);
                return true;
            } else {
                loadLevel(currentLevelNum++);
                return false;
            }
        }
        if (health == 0) {
            levMax = currentLevelNum;
            health = 3;
            loadLevel(0);
            return true;
        }
        return false;
    }
    
    /**
     * loads a level
     * @param levelNum the number of the level to be loaded
     */
    public void loadLevel(int levelNum) {
        readLevel(levelNum - 1);
        level = new Level(gameL, levelNum);
        currentLevelNum = levelNum + 1;
    }
    
    /**
     * responsible for moving the bear in the 4 directions
     * also checks if the bear is visible by a park ranger-returns the player to the original position
     * @param d the direction the bear is moving in
     */
    public void moveBear(Direction d) {
        Position pos = level.getStartingPoint();
        Position newPos;
        if (pos.translate(d).x >= 0 && pos.translate(d).x <= 11
                && pos.translate(d).y >= 0 && pos.translate(d).y <= 11
                && !level.getLevel()[pos.translate(d).x][pos.translate(d).y].equals("T")) {
            newPos = pos.translate(d);
        } else {
            newPos = pos;
        }
        int newX = newPos.x;
        int newY = newPos.y;

        Position up = newPos.translate(Direction.UP);
        Position down = newPos.translate(Direction.DOWN);
        Position left = newPos.translate(Direction.LEFT);
        Position right = newPos.translate(Direction.RIGHT);

        if ((up.x >= 0 && level.getLevel()[up.x][up.y].equals("O"))
                || (down.x <= 11 && level.getLevel()[down.x][down.y].equals("O"))
                || (left.y >= 0 && level.getLevel()[left.x][left.y].equals("O"))
                || (right.y <= 11 && level.getLevel()[right.x][right.y].equals("O"))) {
            level.moveBear(pos.x, pos.y, 1, 0);
            level.getLevel()[level.getStartingPoint().x][level.getStartingPoint().y] = "G";
            level.setStartingPoint(new Position(1, 0));
            health--;
        } else if (!level.getLevel()[newX][newY].equals("M") && !level.getLevel()[newX][newY].equals("T")) {
            if (level.getLevel()[newX][newY].equals("P")) {
                level.setNumOfPicnicBaskets(level.getNumOfPicnicBaskets() - 1);
            }
            level.moveBear(pos.x, pos.y, newX, newY);
            level.setStartingPoint(newPos);
        }
    }
    
    /**
     * responsible for moving the police left-right and up-down
     * also checks if the bear is visible
     */
    public void movePolice() {
        int i = 0;
        while (level.getPolice()[i] != null) {
            Police p = level.getPolice()[i];
            Position pos = level.getPolice()[i].getPos();
            int oldX = pos.x;
            int oldY = pos.y;
            Position nP = null;

            if (p.getD() == Direction.DOWN) {
                if (p.getPos().x == 10) {
                    p.setD((Direction.UP));
                }
                Position newP = pos.translate(p.getD());
                int newX = newP.x;
                int newY = newP.y;
                if (newP.x != 11) {
                    level.movePolice(oldX, oldY, newX, newY);
                }
                if (newP.x == 10) {
                    p.setD(Direction.UP);
                }
                p.setPos(newP);
                nP = newP;
            } else if (p.getD() == Direction.UP) {
                if (p.getPos().x == 1) {
                    p.setD((Direction.DOWN));
                }
                Position newP = pos.translate(p.getD());
                int newX = newP.x;
                int newY = newP.y;
                if (newP.x != 0) {
                    level.movePolice(oldX, oldY, newX, newY);
                }
                if (newP.x == 1) {
                    p.setD(Direction.DOWN);
                }
                p.setPos(newP);
                nP = newP;
            } else if (p.getD() == Direction.RIGHT) {
                if (p.getPos().y == 10) {
                    p.setD((Direction.LEFT));
                }
                Position newP = pos.translate(p.getD());
                int newX = newP.x;
                int newY = newP.y;
                if (newP.y != 11) {
                    level.movePolice(oldX, oldY, newX, newY);
                }
                if (newP.y == 10) {
                    p.setD(Direction.LEFT);
                }
                p.setPos(newP);
                nP = newP;
            } else if (p.getD() == Direction.LEFT) {
                if (p.getPos().y == 1) {
                    p.setD((Direction.RIGHT));
                }
                Position newP = pos.translate(p.getD());
                int newX = newP.x;
                int newY = newP.y;
                if (newP.y != 0) {
                    level.movePolice(oldX, oldY, newX, newY);
                }
                if (newP.y == 1) {
                    p.setD(Direction.RIGHT);
                }
                p.setPos(newP);
                nP = newP;
            }
            i++;
            Position up = nP.translate(Direction.UP);
            Position down = nP.translate(Direction.DOWN);
            Position left = nP.translate(Direction.LEFT);
            Position right = nP.translate(Direction.RIGHT);

            if ((up.x >= 0 && level.getLevel()[up.x][up.y].equals("B"))
                    || (down.x <= 11 && level.getLevel()[down.x][down.y].equals("B"))
                    || (left.y >= 0 && level.getLevel()[left.x][left.y].equals("B"))
                    || (right.y <= 11 && level.getLevel()[right.x][right.y].equals("B"))) {
                level.moveBear(pos.x, pos.y, 1, 0);
                level.getLevel()[level.getStartingPoint().x][level.getStartingPoint().y] = "G";
                level.setStartingPoint(new Position(1, 0));
                health--;
            }
        }
    }

    public void toString(int level) {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(gameL.gameLevel[level][i][j]);
            }
            System.out.println();
        }
    }

    // ------------------------------------------------------------------------
    // Utility methods to load game levels from res/ resource files.
    // ------------------------------------------------------------------------
    private void readLevel(int levelNum) {
        int l = levelNum + 1;
        int l2 = l + 1;
        InputStream is2 = ResourceLoader.loadResource("res/level" + l2 + ".txt");
        int i = 0;
        int pmen = 0;
        gameL.picnicBaskets[l] = 0;
        try ( Scanner sc2 = new Scanner(is2)) {
            String line;
            do {
                line = readNextLine(sc2);
                for (int c = 0; c < line.length(); c++) {
                    gameL.gameLevel[l][i][c] = line.charAt(c) + "";
                    switch (line.charAt(c) + "") {
                        case "P":
                            gameL.picnicBaskets[l]++;
                            break;
                        case "B":
                            gameL.bearOriginalPos[l] = new Position(i, c);
                            break;
                        case "V":
                            gameL.policemen[l][pmen++] = new Police(Direction.DOWN, new Position(i, c));
                            break;
                        case "H":
                            gameL.policemen[l][pmen++] = new Police(Direction.RIGHT, new Position(i, c));
                            break;
                        default:
                            break;
                    }
                }
                i++;
            } while (!line.isEmpty());
            if (is2 != null) {
                is2.close();
            }
        } catch (Exception e) {
            System.out.println("Ajjajjaj");
            e.printStackTrace(System.out);
        }
    }

    private void readLevels() {
        //ClassLoader cl = getClass().getClassLoader();
        InputStream is;// = cl.getResourceAsStream("res/levels.txt");
        for (int k = 0; k < 10; k++) {
            int l = k + 1;
            is = ResourceLoader.loadResource("res/level" + l + ".txt");
            int i = 0;
            int pmen = 0;
            try ( Scanner sc = new Scanner(is)) {
                String line;
                do {
                    line = readNextLine(sc);
                    for (int c = 0; c < line.length(); c++) {
                        gameL.gameLevel[k][i][c] = line.charAt(c) + "";
                        switch (line.charAt(c) + "") {
                            case "P":
                                gameL.picnicBaskets[k]++;
                                break;
                            case "B":
                                gameL.bearOriginalPos[k] = new Position(i, c);
                                break;
                            case "V":
                                gameL.policemen[k][pmen++] = new Police(Direction.DOWN, new Position(i, c));
                                break;
                            case "H":
                                gameL.policemen[k][pmen++] = new Police(Direction.RIGHT, new Position(i, c));
                                break;
                            default:
                                break;
                        }
                    }
                    i++;
                } while (!line.isEmpty());
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                System.out.println("Ajaj");
            }
        }
    }

    private String readNextLine(Scanner sc) {
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()) {
            line = sc.nextLine();
        }
        return line;
    }
}
