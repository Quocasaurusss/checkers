package com.mycompany.checkers;

import static com.mycompany.checkers.App.grid;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


//Notes:
//I don't even have to click on a highlighted space to move a gamepiece ->
//When I click on a game piece, if I click the square underneath it then try to click it again, a nullPointerException occurs ->
//
public class Gameboard2 {
    protected Gamepiece[][] board;
    private int turnCount;
    private int teamTurn;
    private int redCount;
    private int blackCount;
    private ArrayList<Coordinate> validMoves;
    private ArrayList<Node> redTeam;
    private ArrayList<Circle> redTeamCircles;
    private ArrayList<Node> blackTeam;
    private ArrayList<Circle> blackTeamCircles;
    private ArrayList<Node> jumped;
    
    public Gameboard2() {
        board = new Gamepiece[8][4];

        redTeam = new ArrayList<>();
        blackTeam = new ArrayList<>();
        redTeamCircles = new ArrayList<>();
        blackTeamCircles = new ArrayList<>();

        /////////////////////////////
        /*we initialize the game board 
        with the necessary number of 
        red and black chess pieces*/
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new Gamepiece(1);
                Circle cir = new Circle(20);
                cir.setFill(Color.BLACK);
                cir.setStroke(Color.WHITE);
                cir.setStrokeWidth(2.0);
                blackTeam.add(cir);
                blackTeamCircles.add(cir);                
                if (i % 2 == 0){
                    grid.add(cir, j*2+1, i);
                }
                else {
                    grid.add(cir, j*2, i);    
                }
                grid.getChildren().get(grid.getChildren().size()-1).setTranslateX(5);
            }
        }
     	for (int i = 3; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = null;
            }
        }
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new Gamepiece(0);
                Circle cir = new Circle(20);
                cir.setFill(Color.RED);
                cir.setStroke(Color.WHITE);
                cir.setStrokeWidth(2.0);
                redTeam.add(cir);  
                redTeamCircles.add(cir);
                if (i % 2 == 0){
                    grid.add(cir, j*2+1, i);                
                }
                else {
                    grid.add(cir, j*2, i);
                }
                grid.getChildren().get(grid.getChildren().size()-1).setTranslateX(5);
            }
        }
        turnCount = 1;
        teamTurn = 1;
        redCount = 12;
        blackCount = 12;
        validMoves = new ArrayList<>();
    }

    /*called at the start of every turn
    to decide whether or not the game
    continues based off of whether 
    someone has won or the game is tied*/
    protected int GameState() {
        if (isWin()) {
            return 2;
        }
        if (isTie()) {
            return 3;
        }
        return 1;
    }

    private boolean isWin() {
        return redCount == 0 || blackCount == 0;
    }

    private boolean isTie() {
        return turnCount == 30;
    }

    public int teamTurn() {
        return teamTurn;
    }

    private boolean isOnBoard(int row, int col) {
        return row <= 7 && row >= 0 && col <= 3 && col >= 0;
    }

    /*returns a list of coordinates - consisting of row and column number - 
    in which a Gamepiece can move.*/
    public ArrayList<Coordinate> getMoves(int row, int col) {
        validMoves.clear();
        //An error arises when I click on an empty square because it represents null
        if (board[row][col].getCrown()){
            jumped = crownJumpSet(row, col);

            if (validMoves.isEmpty()) {
                return crownMoveSet(row, col);
            }
            else {
                return validMoves;
            }
        }
        
        else {
            jumped = jumpSet(row, col, board[row][col].getTeam());            


            if (validMoves.isEmpty()) {         
                return moveSet(row, col, board[row][col].getTeam());
            }
            else {
                return validMoves;
            }
        }
        
    }

    private ArrayList<Node> crownJumpSet(int row, int col) {
        boolean madeJumps = false;
        int nrow = row;
        int ncol = col;
        ArrayList<Node> jumpedList = new ArrayList<>();
        do {
            if (nrow % 2 == 0) { //CROWN ON EVEN ROW
                if (isOnBoard(nrow + 1, ncol) && board[nrow + 1][ncol] != null && !board[nrow + 1][ncol].isJumped() && isOnBoard(nrow + 2, ncol - 1) && board[nrow + 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol - 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow + 2, ncol - 1));
                    board[nrow + 1][ncol].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow+1 && nodeCol/2==ncol) {
                                jumpedList.add(n);
                            };
                        }
                    }     
                    madeJumps = true;
                    nrow = nrow + 2;
                    ncol = ncol - 1;
                }
                else if (isOnBoard(nrow + 1, ncol + 1) && board[nrow + 1][ncol + 1] != null && !board[nrow + 1][ncol + 1].isJumped() && isOnBoard(nrow + 2, ncol + 1) && board[nrow + 1][ncol + 1].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol + 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow + 2, ncol + 1));
                    board[nrow + 1][ncol + 1].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow+1 && nodeCol/2==ncol+1) {
                                jumpedList.add(n);
                            }
                        }
                    }
                    madeJumps = true;
                    nrow = nrow + 2;
                    ncol = ncol + 1;
                }
                else if (isOnBoard(nrow - 1, ncol) && board[nrow - 1][ncol] != null && !board[nrow - 1][ncol].isJumped() && isOnBoard(nrow - 2, ncol - 1) && board[nrow - 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol - 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow - 2, ncol - 1));
                    board[nrow - 1][ncol].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow-1 && nodeCol/2==ncol) {
                                jumpedList.add(n);
                            }
                        }
                    }
                    madeJumps = true;
                    nrow = nrow - 2;
                    ncol = ncol - 1;
                    
                }
                else if (isOnBoard(nrow - 1, ncol + 1) && board[nrow - 1][ncol + 1] != null && !board[nrow - 1][ncol + 1].isJumped() && isOnBoard(nrow - 2, ncol + 1) && board[nrow - 1][ncol + 1].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol + 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow - 2, ncol + 1));
                    board[nrow - 1][ncol + 1].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow-1 && nodeCol/2==ncol+1) {
                                jumpedList.add(n);
                            }
                        }
                    }
                    madeJumps = true;
                    nrow = nrow - 2;
                    ncol = ncol + 1;
                }
                else {
                    madeJumps = false;
                }
            }
        
            else{ //CROWN ON ODD ROW
                if (isOnBoard(nrow + 1, ncol) && board[nrow + 1][ncol] != null && !board[nrow + 1][ncol].isJumped() && isOnBoard(nrow + 2, ncol + 1) && board[nrow + 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol + 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow + 2, ncol + 1));
                    board[nrow + 1][ncol].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow+1 && nodeCol/2==ncol) {
                                jumpedList.add(n);
                            }
                        }
                    }
                    madeJumps = true;
                    nrow = nrow + 2;
                    ncol = ncol + 1;
                }
                else if (isOnBoard(nrow + 1, ncol - 1) && board[nrow + 1][ncol - 1] != null && !board[nrow + 1][ncol - 1].isJumped() && isOnBoard(nrow + 2, ncol - 1) && board[nrow + 1][ncol - 1].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol - 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow + 2, ncol - 1));
                    board[nrow + 1][ncol - 1].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow+1 && nodeCol/2==ncol-1) {
                                jumpedList.add(n);
                            }
                        }
                    }          
                    madeJumps = true;
                    nrow = nrow + 2;
                    ncol = ncol - 1;
                }
                else if (isOnBoard(nrow - 1, ncol) && board[nrow - 1][ncol] != null && !board[nrow - 1][ncol].isJumped() && isOnBoard(nrow - 2, ncol + 1) && board[nrow - 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol + 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow - 2, ncol + 1));
                    board[nrow - 1][ncol].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow-1 && nodeCol/2==ncol) {
                                jumpedList.add(n);
                            }
                        }
                    }
                    madeJumps = true;
                    nrow = nrow - 2;
                    ncol = ncol + 1;
                }
                else if (isOnBoard(nrow - 1, ncol - 1) && board[nrow - 1][ncol - 1] != null && !board[nrow - 1][ncol - 1].isJumped() && isOnBoard(nrow - 2, ncol - 1) && board[nrow - 1][ncol - 1].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol - 1] == null ) {
                    validMoves.clear();
                    validMoves.add(new Coordinate(nrow - 2, ncol - 1));
                    board[nrow - 1][ncol - 1].makeJumped();
                    for  (Node n : grid.getChildren()) {
                        if (grid.getChildren().indexOf(n) > 63){
                            int nodeRow = GridPane.getRowIndex(n);
                            int nodeCol = GridPane.getColumnIndex(n);                                                    
                            if (nodeRow == nrow-1 && nodeCol/2==ncol-1) {
                                jumpedList.add(n);
                            }
                        }
                    }       
                    madeJumps = true;
                    nrow = nrow - 2;
                    ncol = ncol - 1;
                }
                else {
                    madeJumps = false;
                }
            }
        }
        while (madeJumps == true);
        return jumpedList;
    }

    private ArrayList<Node> jumpSet(int row, int col, int team) {
        boolean madeJumps = false;
        int nrow = row;
        int ncol = col;
        ArrayList<Node> jumpedList = new ArrayList<>();
        do {
            if (team == 0) { //RED TEAM
                if (row % 2 == 0) { //ON EVEN ROW
                    if (isOnBoard(nrow - 1, ncol) && board[nrow - 1][ncol] != null && !board[nrow - 1][ncol].isJumped() && isOnBoard(nrow - 2, ncol - 1) && board[nrow - 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol - 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow - 2, ncol - 1));
                        board[nrow - 1][ncol].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);                                                    
                                if (nodeRow == nrow-1 && nodeCol/2==ncol) {
                                    jumpedList.add(n);
                                }
                            }
                        }
                        madeJumps = true;
                        nrow = nrow - 2;
                        ncol = ncol - 1;
                    }
                    else if (isOnBoard(nrow - 1, ncol + 1) && board[nrow - 1][ncol + 1] != null && !board[nrow - 1][ncol + 1].isJumped() && isOnBoard(nrow - 2, ncol + 1) && board[nrow - 1][ncol + 1].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol + 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow - 2, ncol + 1));
                        board[nrow - 1][ncol + 1].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow-1 && nodeCol/2==ncol+1) {
                                    jumpedList.add(n);
                                }
                            }
                        }                      
                        madeJumps = true;
                        nrow = nrow - 2;
                        ncol = ncol + 1;
                    }
                    else {
                        madeJumps = false;
                    }
                }
                else { //ODD row
                    if (isOnBoard(nrow - 1, ncol) && board[nrow - 1][ncol] != null && !board[nrow - 1][ncol].isJumped() && isOnBoard(nrow - 2, ncol + 1) && board[nrow - 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol + 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow - 2, ncol + 1));                      
                        board[nrow - 1][ncol].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow-1 && nodeCol/2==ncol) {
                                    jumpedList.add(n);
                                }
                            }
                        }
                        madeJumps = true;
                        nrow = nrow - 2;
                        ncol = ncol + 1;
                    }
                    else if (isOnBoard(nrow - 1, ncol - 1) && board[nrow - 1][ncol - 1] != null && !board[nrow - 1][ncol - 1].isJumped() && isOnBoard(nrow - 2, ncol - 1) && board[nrow - 1][ncol - 1].getTeam() != board[row][col].getTeam() && board[nrow - 2][ncol - 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow - 2, ncol - 1));
                        board[nrow - 1][ncol - 1].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow-1 && nodeCol/2==ncol-1) {
                                    jumpedList.add(n);
                                }
                            }
                        }                  
                        madeJumps = true;
                        nrow = nrow - 2;
                        ncol = ncol - 1;
                    }
                    else {
                        madeJumps = false;
                    }
                }
            }
    
            else { //BLACK TEAM
                if (row % 2 == 0) { //ON EVEN ROW
                    if (isOnBoard(nrow + 1, ncol) && board[nrow + 1][ncol] != null && !board[nrow + 1][ncol].isJumped() && isOnBoard(nrow + 2, ncol - 1) && board[nrow + 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol - 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow + 2, ncol - 1));
                        board[nrow + 1][ncol].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow+1 && nodeCol/2==ncol) {
                                    jumpedList.add(n);
                                }
                            }
                        }                        
                   
                        madeJumps = true;
                        nrow = nrow + 2;
                        ncol = ncol - 1;                       
                    }
                    else if (isOnBoard(nrow + 1, ncol + 1) && board[nrow + 1][ncol + 1] != null && !board[nrow + 1][ncol + 1].isJumped() && isOnBoard(nrow + 2, ncol + 1) && board[nrow + 1][ncol + 1].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol + 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow + 2, ncol + 1));                          
                        board[nrow + 1][ncol + 1].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow+1 && nodeCol/2==ncol+1) {
                                    jumpedList.add(n);
                                }
                            }   
                        }
                        madeJumps = true;
                        nrow = nrow + 2;
                        ncol = ncol + 1;
                                               
                    }
                    else {
                        madeJumps = false;
                    }
                }
    
                else if (row % 2 != 0) { //ON ODD ROW
                    if (isOnBoard(nrow + 1, ncol) && board[nrow + 1][ncol] != null && !board[nrow + 1][ncol].isJumped() && isOnBoard(nrow + 2, ncol + 1) && board[nrow + 1][ncol].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol + 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow + 2, ncol + 1));
                        board[nrow+1][ncol].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow+1 && nodeCol/2==ncol) {
                                    jumpedList.add(n);
                                }
                            }
                        }                            
                        madeJumps = true;
                        nrow = nrow + 2;
                        ncol = ncol + 1;                        
                    }
                    else if (isOnBoard(nrow + 1, ncol - 1) && board[nrow + 1][ncol - 1] != null && !board[nrow + 1][ncol - 1].isJumped() && isOnBoard(nrow + 2, ncol - 1) && board[nrow + 1][ncol - 1].getTeam() != board[row][col].getTeam() && board[nrow + 2][ncol - 1] == null ) {
                        validMoves.clear();
                        validMoves.add(new Coordinate(nrow + 2, ncol - 1));
                        board[nrow + 1][ncol - 1].makeJumped();
                        for  (Node n : grid.getChildren()) {
                            if (grid.getChildren().indexOf(n) > 63){
                                int nodeRow = GridPane.getRowIndex(n);
                                int nodeCol = GridPane.getColumnIndex(n);
                                if (nodeRow == nrow+1 && nodeCol/2==ncol-1) {
                                    jumpedList.add(n);
                                }
                            }
                        }                                              
                        madeJumps = true;
                        nrow = nrow + 2;
                        ncol = ncol - 1;
                    }
                    else {
                        madeJumps = false;
                    }
                }
            }
        }
        while (madeJumps == true);
        return jumpedList;
    }

    /*checking for possible moves based off of the moveset allowed for a crowned checkers piece*/
    private ArrayList<Coordinate> crownMoveSet(int row, int col) {
        if (row % 2 == 0) { //CROWN ON EVEN ROW
            if (isOnBoard(row + 1, col) && board[row + 1][col] == null) {
                validMoves.add(new Coordinate(row + 1, col));
            }
            if (isOnBoard(row + 1, col + 1) && board[row + 1][col + 1] == null) {
                validMoves.add(new Coordinate(row + 1, col + 1));
            }
            if (isOnBoard(row - 1, col) && board[row - 1][col] == null) {
                validMoves.add(new Coordinate(row - 1, col));
            }
            if (isOnBoard(row - 1, col + 1) && board[row - 1][col + 1] == null) {
                validMoves.add(new Coordinate(row - 1, col + 1));
            }
        }

        else { //CROWN ON ODD ROW
            if (isOnBoard(row + 1, col) && board[row + 1][col] == null) {
                validMoves.add(new Coordinate(row + 1, col));
            }
            if (isOnBoard(row + 1, col - 1) && board[row + 1][col - 1] == null) {
                validMoves.add(new Coordinate(row + 1, col - 1));
            }
            if (isOnBoard(row - 1, col) && board[row - 1][col] == null) {
                validMoves.add(new Coordinate(row - 1, col));
            }
            if (isOnBoard(row - 1, col - 1) && board[row - 1][col - 1] == null) {
                validMoves.add(new Coordinate(row - 1, col - 1));
            }
        }
        return validMoves;
    }

    /*checking for possible moves based off of the moveset allowed for a regular checkers piece*/
    private ArrayList<Coordinate> moveSet(int row, int col, int team) {
        if (team == 0) { //RED TEAM
            if (row % 2 == 0) { //ON EVEN ROW
                if (isOnBoard(row - 1, col + 1) && board[row - 1][col + 1] == null ) {
                    validMoves.add(new Coordinate(row - 1, col + 1));
                }
                if (isOnBoard(row - 1, col) && board[row - 1][col] == null) {
                    validMoves.add(new Coordinate(row - 1, col));
                }
            }
    
            else { //ON ODD ROW
                if (isOnBoard(row - 1, col) && board[row - 1][col] == null) {
                    validMoves.add(new Coordinate(row - 1, col));
                }
                if (isOnBoard(row - 1, col - 1) && board[row - 1][col - 1] == null) {
                    validMoves.add(new Coordinate(row - 1, col - 1));
                }
            }
        }
        
        else { //BLACK TEAM
            if (row % 2 == 0) { //ON EVEN ROW
                if (isOnBoard(row + 1, col + 1) && board[row + 1][col + 1] == null) {
                    validMoves.add(new Coordinate(row + 1, col + 1));
                }
                if (isOnBoard(row + 1, col) && board[row + 1][col] == null) {
                    validMoves.add(new Coordinate(row + 1, col));
                }
            }
    
            else { //ON ODD ROW
                if (isOnBoard(row + 1, col) && board[row + 1][col] == null) {
                    validMoves.add(new Coordinate(row + 1, col));
                }
                if (isOnBoard(row + 1, col - 1) && board[row + 1][col - 1] == null) {
                    validMoves.add(new Coordinate(row + 1, col - 1));
                }
            }
        }
        
        return validMoves;
    }

    public void makeMove(int startRow, int startCol, int destRow, int destCol) {
        board[destRow][destCol] = board[startRow][startCol];
        board[startRow][startCol] = null;
        if(destRow == 0 || destRow == 7) {
            board[destRow][destCol].makeCrown();
        }
    }
    
    public int getBlack(){
        return blackCount;
    }
    public int getRed(){
        return redCount;
    }
    public void setBlack(){
        blackCount--;
    }
    public void setRed(){
        redCount--;
    }
    
    public ArrayList<Node> getJumped(){
        return jumped;
    }
    
    public ArrayList<Node> getRedTeam(){
        return redTeam;
    }
    
    public ArrayList<Node> getBlackTeam(){
        return blackTeam;
    }
    
    public ArrayList<Circle> getRedTeamCircles(){
        return redTeamCircles;
    }
        public ArrayList<Circle> getBlackTeamCircles(){
        return blackTeamCircles;
    }
}

