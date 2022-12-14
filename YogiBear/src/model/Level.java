/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author csab
 */
public class Level {

    private String[][] level = new String[12][12];
    private int numOfPicnicBaskets;
    private Position startingPoint;
    private Police[] police = new Police[10];

    public Level(GameLevel gl, int l) {
        this.level = gl.gameLevel[l];
        this.numOfPicnicBaskets = gl.picnicBaskets[l];
        this.startingPoint = gl.bearOriginalPos[l];
        this.police = gl.policemen[l];
    }

    public int getNumOfPicnicBaskets() {
        return numOfPicnicBaskets;
    }

    public Position getStartingPoint() {
        return startingPoint;
    }

    public Police[] getPolice() {
        return police;
    }

    public String[][] getLevel() {
        return level;
    }
    
    public void setStartingPoint(Position startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setNumOfPicnicBaskets(int numOfPicnicBaskets) {
        this.numOfPicnicBaskets = numOfPicnicBaskets;
    }
    
    /**
     * moves the park rangers on the level
     * @param oldX x coordinate of current position
     * @param oldY y coordinate of current position
     * @param newX x coordinate of new position
     * @param newY y coordinate of new position
     */
    public void movePolice(int oldX, int oldY, int newX, int newY) {
        this.level[oldX][oldY] = "G";
        this.level[newX][newY] = "O";
    }
    
    //--- COULD BE DONE IN 1 FUNCTION (movePolice && moveBear) ---//
    //--- WITH 2 EXTRA STRING PARAMETERS --- //
    
    /**
     * moves the bear on the level
     * @param oldX x coordinate of current position
     * @param oldY y coordinate of current position
     * @param newX x coordinate of new position
     * @param newY y coordinate of new position
     */
    public void moveBear(int oldX, int oldY, int newX, int newY) {
        this.level[oldX][oldY] = "G";
        this.level[newX][newY] = "B";
    }
}
