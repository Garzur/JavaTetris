/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import javafx.scene.paint.Color;
import tetris.Shape.tetrisBlocks;
import static tetris.TetrisBoard.X_DIM_SQUARES;
import static tetris.TetrisBoard.Y_DIM_SQUARES;


/**
 * This should be implemented to include your game control.
 * @author pipWolfe
 */
public class TetrisGame {
    private final Tetris tetrisApp;
    private Shape currentShape;
    private int boardWidth;
    private int boardHeight;
    private int currentX;
    private int currentY;
    private TetrisSquare[][] deadSquares = new TetrisSquare[TetrisBoard.Y_DIM_SQUARES][TetrisBoard.X_DIM_SQUARES];
    private final TetrisBoard board;
    private int fullLinesScore = 0 * 100;
    private boolean[][] trueColor = new boolean[TetrisBoard.Y_DIM_SQUARES][TetrisBoard.X_DIM_SQUARES];
    
    /**
     * Initialize the game. Remove the example code and replace with code
     * that creates a random piece.
     * @param tetrisApp A reference to the application (use to set messages).
     * @param board A reference to the board on which Squares are drawn
     */
    public TetrisGame(Tetris tetrisApp, TetrisBoard board) {
        this.board = board;
        /*
        creates a new shape on board
        */
        newShape();
        
        // Some sample code that places two squares on the board.
        // Take this out and construct your random piece here.
        
        
        
        this.tetrisApp = tetrisApp;
        // You can use this to show the score, etc.
        tetrisApp.setMessage("Game has started!");
    }

    private void corruption(Shape oldShape){
        boolean touched = false;
        int[] squaresY = new int[4];
        int[] squaresX = new int[4];
        for(int i = 0; i < 4; i++){
            if(oldShape.getSquares(i).getY() >= Y_DIM_SQUARES - 1){
                touched = true;
                for(int j = 0; j < 4; j++){
                    squaresY[j] = oldShape.getSquares(j).getY();
                    squaresX[j] = oldShape.getSquares(j).getX();
                    deadSquares[squaresY[j]][squaresX[j]] = oldShape.getSquares(j);
                    deadSquares[squaresY[j]][squaresX[j]].setColor(Color.BLACK);
                }
                removeFullLines();
                newShape();
                break;
            }
            else if (!touched){
                squaresY[i] = oldShape.getSquares(i).getY() + 1;
                squaresX[i] = oldShape.getSquares(i).getX();
                if(deadSquares[squaresY[i]][squaresX[i]] != null){
                    touched = true;
                    for(int j = 0; j < 4; j++){
                        squaresY[j] = oldShape.getSquares(j).getY();
                        squaresX[j] = oldShape.getSquares(j).getX();
                        deadSquares[squaresY[j]][squaresX[j]] = oldShape.getSquares(j);
                        deadSquares[squaresY[j]][squaresX[j]].setColor(Color.BLACK);
                    }
                    removeFullLines();
                    newShape();
                    break;
                }
            }
            else if(!touched){
                break;
            }
        
        }
    }
    private void removeFullLines(){
        boolean canRemove;
        int row = 0;
        while(row < Y_DIM_SQUARES){
            canRemove = true;
            for (int j = 0; j < X_DIM_SQUARES; j++) {
                if(deadSquares[row][j] == null){
                    canRemove = false;
                    break;
                }
            }
            if(canRemove) {
                fullLinesScore++;
                for(int k = 0; k < X_DIM_SQUARES; k++){
                    deadSquares[row][k].removeFromDrawing();
                    deadSquares[row][k] = null;
                }
                moveDeadSquares();
                row = 0;
            }
            row++;
        }
        tetrisApp.setMessage("Score: " + fullLinesScore);
    }
    
    private void moveDeadSquares() {
        for(int i = Y_DIM_SQUARES - 2; i > 0; i--){
            for(int j = 0; j < X_DIM_SQUARES; j++){
                if(deadSquares[i][j] != null){
                    for(int k = i; k <= Y_DIM_SQUARES - 2; k++){
                        if(deadSquares[k + 1][j] == null && deadSquares[k][j] != null){
                            deadSquares[k][j].moveToTetrisLocation(j, k + 1);
                            deadSquares[k + 1][j] = deadSquares[k][j];
                            deadSquares[k][j] = null;
                        }
                    }
                }
            }
        }
    }
    
    private void newShape(){
        currentShape = new Shape(board);
    }
    private void gameOver(){
        for(int i = 0; i < Y_DIM_SQUARES; i++){
            for (int j = 0; j < X_DIM_SQUARES; j++) {
                if (deadSquares[i][j] != null && deadSquares[i][j].getY() < 3) {
                    tetrisApp.setMessage("GAME OVER  " + "  Score: " + fullLinesScore);
                    tetrisApp.pause();
                }
            }
        }
    }
    
    /**
     * Animate the game, by moving the current tetris piece down.
     */
    void update() {
        //System.out.println("updating");
        currentShape.tryMove();
        corruption(currentShape);
        gameOver();
    }
    
    /**
     * Move the current tetris piece left.
     */
    void left() {
        //System.out.println("left key was pressed!");
        /*
        calls move left method on current shape
        */
        currentShape.moveLeft();
        
    }

    /**
     * Move the current tetris piece right.
     */
    void right() {
        //System.out.println("right key was pressed!");
        /*
            calls move right method on current shape
        */
        currentShape.moveRight();
        
    }

    /**
     * Drop the current tetris piece.
     */
    void drop() {
        //System.out.println("drop key was pressed!");
        currentShape.tryMove();
        
    }

    /**
     * Rotate the current piece counter-clockwise.
     */
    void rotateLeft() {
        //System.out.println("rotate left key was pressed!");
        /*
            calls rotate left method on current shape
        */
        currentShape.rotateLeft();
    }
    
    /**
     * Rotate the current piece clockwise.
     */
    void rotateRight() {
        //System.out.println("rotate right key was pressed!");
        /*
            calls rotate right method on current shape
        */
        currentShape.rotateRight();
    }
    
    
}
