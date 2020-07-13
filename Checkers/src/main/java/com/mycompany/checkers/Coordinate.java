package com.mycompany.checkers;

public class Coordinate {
    private int row;
    private int col;

    public Coordinate(int x, int y){
        row = x;
        col = y;
    }

    protected int getRow(){
        return row;
    }

    protected int getCol(){
        return col;
    }

}