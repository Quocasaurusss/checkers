package com.mycompany.checkers;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    static GridPane grid = new GridPane();
    ArrayList<Coordinate> moves = new ArrayList<>();  
    ArrayList<Rectangle> squares = new ArrayList<>();
    public ArrayList<Rectangle> old = new ArrayList<>();
    ArrayList<EventHandler> removeEvents = new ArrayList<>();
            
    @Override
    public void start(Stage stage) {
        
        
        //some grid styling.
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        //creating a scene to display on our stage. Every stage
        //needs at least one scene.
        Scene scene = new Scene(grid, 500, 500);
        

        Text turnText = new Text("R's turn.");
        grid.add(turnText, 3, 8);
        grid.getChildren().get(grid.getChildren().size()-1).setTranslateY(10);
        grid.getChildren().get(grid.getChildren().size()-1).setTranslateX(25);
        
        //creating an 8x8 checkers board for the front-end.
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                //if the row is even, spaces are organized in a certain way.
                if (row % 2 == 0){
                    if (col % 2 == 1){
                        Rectangle square = new Rectangle(50, 50);
                        square.setFill(Color.BLACK);
                        grid.add(square, col, row);
                        squares.add(square);
                    }
                    else {
                        Rectangle square = new Rectangle(50, 50);
                        square.setFill(Color.RED);
                        grid.add(square, col, row);
                        squares.add(square);
                    }
                }
                //if the row is odd, spaces are organized in another way.
                else {
                    if (col % 2 == 1){
                        Rectangle square = new Rectangle(50, 50);
                        square.setFill(Color.RED);
                        grid.add(square, col, row);
                        squares.add(square);
                    }
                    else {
                        Rectangle square = new Rectangle(50, 50);
                        square.setFill(Color.BLACK);
                        grid.add(square, col, row);
                        squares.add(square);
                    }
                }

            }
        }
            
        //creating a checkers board within the game logic.
        Gameboard2 board = new Gameboard2();
        
        //when something on the grid is clicked.
        grid.getChildren().forEach(n -> {
            n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouse) {
                    int row = GridPane.getRowIndex(n);
                    int col = GridPane.getColumnIndex(n);
                        
                    old.forEach(highlighted -> {
                        removeEvents.forEach(handle -> {
                            try {
                                highlighted.removeEventHandler(MouseEvent.MOUSE_CLICKED, handle);
                            }
                            catch (Exception e) {}               
                        });
                    });
                    for (int i=0;i<old.size();i++){
                        old.get(i).setFill(Color.BLACK);
                    }
                    if (grid.getChildren().indexOf(n) > 63)
                    {   
                        old.clear();
                        for (Gamepiece[] board1 : board.board) {
                            for (int c = 0; c < board.board[0].length; c++) {
                                if (board1[c] != null && board1[c].isJumped() == true) {
                                    board1[c].removeJumped();
                                }
                            }
                        }
                        System.out.println("# of Black Pieces: " + board.getBlack());
                        System.out.println("# of Red Pieces: " + board.getRed());
                        
                        if (row % 2 == 1){
                            moves = board.getMoves(row, col/2);                           
                        }
                        else {
                            moves = board.getMoves(row, col/2);
                        }
                        
                        
                        moves.forEach(i -> {
                            if (i.getRow() % 2 == 1){
                                squares.get(8*i.getRow()+i.getCol()*2).setFill(Color.YELLOW);
                                old.add(squares.get(8*i.getRow()+i.getCol()*2));
                            }
                            else {
                                squares.get(8*i.getRow()+i.getCol()*2+1).setFill(Color.YELLOW);
                                old.add(squares.get(8*i.getRow()+i.getCol()*2+1));
                            }
                        });
                        moves.clear();
                        //when a highlighted space is clicked
                        //A Problem: When I jump a piece to reach a highlighted space, I get a nullPointerException
                        old.forEach(square -> {
                            //
                            EventHandler highlight = new EventHandler<MouseEvent> () {
                                public void handle(MouseEvent event) {
                                    //If you click on a highlighted piece, you remove all game pieces that were jumped as a result.
                                    ArrayList<Node> jumped = board.getJumped();
                                    for (Gamepiece[] board1 : board.board) {
                                        for (int c = 0; c < board.board[0].length; c++) {
                                            if (board1[c] != null && board1[c].isJumped() == true) {
                                                if (board1[c].getTeam() == 1) {
                                                    board.setRed();
                                                }
                                                else {
                                                    board.setBlack();
                                                }
                                                board1[c] = null;
                                            }
                                        }
                                    }
                                    jumped.forEach(n -> {
                                        grid.getChildren().remove(n);
                                    });
                                    int startRow = GridPane.getRowIndex(n);
                                    //position of the square we clicked in the list of squares that
                                    //represent the 8x8 grid.
                                    int index = squares.indexOf(square);
                                    int startCol = GridPane.getColumnIndex(n)/2;
                                    int desRow = index/8;
                                    int desCol = (index-(desRow*8))/2;

                                    if (board.board[startRow][startCol].getTeam() == 1){
                                        turnText.setText("R's turn.");
                                    }
                                    else {
                                        turnText.setText("B's turn.");
                                    }
                                    board.makeMove(startRow, startCol, desRow, desCol);
                                    if (desRow % 2 == 1){
                                        if (startRow % 2 == 1) {
                                            Node start = getNodeFromGridPane(grid, startCol*2, startRow);
                                            grid.getChildren().remove(start);
                                            grid.add(start, desCol*2, desRow);
                                        }
                                        else {
                                            Node start = getNodeFromGridPane(grid, startCol*2+1, startRow);
                                            grid.getChildren().remove(start);
                                            grid.add(start, desCol*2, desRow);
                                            
                                        }
                                    }
                                    //KEY NOTE: It matters if you move from even row to and even row (jump) or an odd row to an even row (regular)
                                    else {
                                        if (startRow % 2 == 1) {
                                            Node start = getNodeFromGridPane(grid, startCol*2, startRow);
                                            grid.getChildren().remove(start);
                                            grid.add(start, desCol*2+1, desRow);
                                        }
                                        else {
                                            Node start = getNodeFromGridPane(grid, startCol*2+1, startRow);
                                            grid.getChildren().remove(start);
                                            grid.add(start, desCol*2+1, desRow);
                                        }                                        
                                    }
                                    
                                    if (board.board[desRow][desCol].getCrown()){
                                        if (board.board[desRow][desCol].getTeam() == 0){
                                            int changeColor_index = board.getRedTeam().indexOf(n);
                                            board.getRedTeamCircles().get(changeColor_index).setFill(Color.PINK);
                                        }
                                        else {
                                            int changeColor_index = board.getBlackTeam().indexOf(n);
                                            board.getBlackTeamCircles().get(changeColor_index).setFill(Color.DIMGRAY);
                                        }
                                    }
                                    
                                    if (board.GameState() == 2){
                                        if (board.getBlack() == 0){
                                            System.out.println("The game has been won. \nRed team won.");
                                            Popup win = new Popup();
                                            Text winText = new Text("You won.");
                                            win.setX(250.0);
                                            win.setY(250.0);
                                            win.getContent().add(winText);
                                            
                                        }
                                        else {
                                            System.out.println("The game has been won. \nBlack Team won.");
                                        }
                                        
                                    }
                                }
                                
                            };
                            removeEvents.add(highlight);
                            square.addEventHandler(MouseEvent.MOUSE_CLICKED, highlight);
                        });       
                    }
                }
            });
        });
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.show();
    }
    
    public static GridPane getGrid(){
        return grid;
    }
    
    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
        int nodeColumn = GridPane.getColumnIndex(node);
        int nodeRow = GridPane.getRowIndex(node);
        if (grid.getChildren().indexOf(node) > 63){
            if (nodeColumn == col && nodeRow == row) {
                return node;
            }
        }
    }
    return null;
    }
    
    public static void main(String[] args) {
        launch();
    }

}
