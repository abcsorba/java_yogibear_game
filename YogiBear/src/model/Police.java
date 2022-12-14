/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author csab
 */
public class Police {

    private Direction d;
    private Position pos;

    public Police(Direction d, Position pos) {
        this.d = d;
        this.pos = pos;
    }

    public Direction getD() {
        return d;
    }

    public Position getPos() {
        return pos;
    }

    public void setD(Direction d) {
        this.d = d;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
