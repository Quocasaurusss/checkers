package com.mycompany.checkers;

public class Gamepiece {
    private boolean crown;
    private boolean isJumped;
    private int team;

    public Gamepiece(int teamColor) {
        //1 represents black team, 0 represents red team.
        crown = false;
        isJumped = false;
        team = teamColor;
    }
    
    protected int getTeam() {
        return team;
    }

    protected boolean getCrown() {
        return crown;
    }

    protected void makeCrown() {
        crown = true;
    }

    protected boolean isJumped() {
        return isJumped;
    }

    protected void makeJumped() {
        isJumped = true;
    }
    protected void removeJumped(){
        isJumped = false;
    }
}