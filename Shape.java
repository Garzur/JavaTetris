
package tetris;

import java.awt.Point;
import static java.lang.Math.abs;
import java.util.Random;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import static tetris.TetrisBoard.X_DIM_SQUARES;
import static tetris.TetrisBoard.Y_DIM_SQUARES;



public class Shape {
    /*
        enum of shapes
    */
    enum tetrisBlocks {Z, T, sqr, line, L, S, J};
    
    private Random rand = new Random();
    private Color color;
    private tetrisBlocks shapes;
    private tetrisBlocks[] index = tetrisBlocks.values();
    private int[][] coords;
    private int[][][] blockCoords;
    private int COORDSETS = 4;
    private int COORDVAL = 2;
    private TetrisBoard board;
    private TetrisSquare[] squares = new TetrisSquare[4];
    public int originX;
    public int originY;
    private int[] currentX = new int[COORDSETS];
    private int[] currentY = new int[COORDSETS];


    /*
        constructor for Shape class
    */
    public Shape(TetrisBoard board){
        /*
            sets origins to default values where tetris shapes will show up first
        */
        originX = board.X_DIM_SQUARES / 2;
        originY = 0;
        this.board = board;
        /*
            initializes coords multiarray to hold 4 sets of 2 coords
        */
        coords = new int[COORDSETS][COORDVAL];
        /*
            sets a random shape
        */
        setNewShape();

        /*
            creates squares on board and moves them to default location
            then makes that starting position the current X,Y
        */
        color = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        for(int i = 0; i < COORDSETS; i++){
            squares[i] = new TetrisSquare(board);
            squares[i].moveToTetrisLocation(originX + coords[i][0], originY + coords[i][1]);
            squares[i].setColor(color);
        }
        for(int i = 0; i < COORDSETS; i++){
            currentX[i] = squares[i].getX();
            currentY[i] = squares[i].getY();
        }
        
    }
    /*
        method to set sets of coords to an enum shape
        in turn the squares are built at those coords for each enum shape
    */
    public void setShape(tetrisBlocks block){
        System.out.println(block);
        blockCoords = new int[][][]{
            { {0, 0}, {0, 1}, {-1, 1}, {1, 0} },
            { {0, 0}, {-1, 0}, {1, 0}, {0, 1} },
            { {0, 0}, {0, -1}, {1, -1}, {1, 0} },
            { {0, 0}, {0, -1}, {0, 1}, {0, 2} },
            { {0, 0}, {0, 1}, {0, -1}, {1, -1} },
            { {0, 0}, {-1, 0}, {0, 1}, {1, 1} },
            { {0, 0}, {0, -1}, {-1, -1}, {0, 1} }
        };
        /*
            for loop to go through blockCoords array and match them to an enum shape
            then assign those coords to coords array to hold the values
        */
        for(int i = 0; i < COORDSETS; i++){
            for(int j = 0; j < COORDVAL; j++){
                coords[i][j] = blockCoords[block.ordinal()][i][j];
            }
        }
        shapes = block;
    }
    
    /*
        the aim of the method is to set a random shape
        set new shape calls set shape which picks an enum by index
    */
    public void setNewShape(){
        setShape(index[rand.nextInt(7)]);
    }
    
    /*
        returns enum shape
    */
    public tetrisBlocks getShape(){
        return shapes;
    }
    /*
        setter methods for relative coords for squares
    */
    private void setX(int num, int xCoord){ 
        coords[num][0] = xCoord;
    }
    private void setY(int num, int yCoord){ 
        coords[num][1] = yCoord;
    }
    /*
        get methods for the relative coords of squares
    */
    public int getXCoords(int num){
        return coords[num][0];
    }
    public int getYCoords(int num){ 
        return coords[num][1];
    }
    public int getCurrentXCoords(int num){
        return currentX[num];
    }
    public int getCurrentYCoords(int num){ 
        return currentY[num];
    }
    public TetrisSquare getSquares(int index){
        return squares[index];
    }
    public void setSquares(int[] x, int[] y){
        for(int i = 0; i < 4; i++){
            squares[i].moveToTetrisLocation(x[i], y[i]);
        }
    }
    
    private int[][] checkSquares(int[] newXVal, int[] newYVal){
        int[][] newCoords = new int[COORDSETS][COORDVAL];
        boolean newSquares = true;
        for(int i = 0; i < COORDSETS; i++){
            if(newXVal[i] < 0 || newXVal[i] >= X_DIM_SQUARES || newYVal[i] >= Y_DIM_SQUARES){
                for(int j = 0; j < COORDSETS; j++){
                    newCoords[j][0] = currentX[j];
                    newCoords[j][1] = currentY[j];
                }
                newSquares = false;
                break;
            }
            else{
                newCoords[i][0] = newXVal[i];
                newCoords[i][1] = newYVal[i];
            }
        }
        return newCoords;
    }
    
    private void commitSquares(int[][] newCoords){
        for(int i = 0; i < COORDSETS; i++){
            squares[i].moveToTetrisLocation(newCoords[i][0], newCoords[i][1]);
            currentX[i] = squares[i].getX();
            currentY[i] = squares[i].getY();
        }
        originX = squares[0].getX();
        originY = squares[0].getY();
        
    }
    
    
    public void rotateRight(){
        int[] oldXCoords = new int[COORDSETS];
        int[] oldYCoords = new int[COORDSETS];
        int[] newXCoords = new int[COORDSETS];
        int[] newYCoords = new int[COORDSETS];
        /*
            creates old coords for X and Y by taking current coords
        */        
        for(int i = 0; i < COORDSETS; i++){
            oldXCoords[i] = getXCoords(i);
            oldYCoords[i] = getYCoords(i);
        }
        /*
            sets new relative coords by using old relative coords
            adds old relative coords to origins
        */
        for(int i = 0; i < COORDSETS; i++){
            setX(i, -oldYCoords[i]);
            setY(i, oldXCoords[i]);
        }
        for(int i = 0; i < COORDSETS; i++){
            newYCoords[i] = originY + getYCoords(i);
            newXCoords[i] = originX + getXCoords(i);
        }
        /*
            moves squares to new current coords 
            and then updates origins
        */
        commitSquares(checkSquares(newXCoords, newYCoords));
    }
    
    
    public void rotateLeft(){
        int[] oldXCoords = new int[COORDSETS];
        int[] oldYCoords = new int[COORDSETS];
        int[] newXCoords = new int[COORDSETS];
        int[] newYCoords = new int[COORDSETS];
        /*
            Creates old coords for X and Y by taking current coords
        */        
        for(int i = 0; i < COORDSETS; i++){
            oldXCoords[i] = getXCoords(i);
            oldYCoords[i] = getYCoords(i);
        }
        /*
            loops through and sets new X and Y coords by using old coords
        */
        for(int i = 0; i < COORDSETS; i++){
            setX(i, oldYCoords[i]);
            setY(i, -oldXCoords[i]);
        }
        /*
            loops through and sets new current coords by adding relative coords to origin coords
        */
        for(int i = 0; i < COORDSETS; i++){
            newYCoords[i] = originY + getYCoords(i);
            newXCoords[i] = originX + getXCoords(i);
        }
        /*
            moves the squares to current coords
            
        */
        commitSquares(checkSquares(newXCoords, newYCoords));
    }
    
    /*
        moves squares right by adding +1 to the x-vals of each square and updates their position
        then updates the origins by getting current X and Y positions of center square
    */
    public void moveRight(){
        int[] newXCoords = new int[COORDSETS];
        int[] newYCoords = new int[COORDSETS];
        for(int i = 0; i < COORDSETS; i++){
            newXCoords[i] = squares[i].getX() + 1;
            newYCoords[i] = squares[i].getY();
        }
        commitSquares(checkSquares(newXCoords, newYCoords));
    }

    /*
        moves squares by subtracting -1 to the x-vals of each square and updates their position
        then updates the origins by getting current X and Y positions of center square
    */
    public void moveLeft(){
        int[] newXCoords = new int[COORDSETS];
        int[] newYCoords = new int[COORDSETS];
        for(int i = 0; i < COORDSETS; i++){
            newXCoords[i] = squares[i].getX() - 1;
            newYCoords[i] = squares[i].getY();
        }
        commitSquares(checkSquares(newXCoords, newYCoords));
    }

    public void tryMove(){
        int[] newXCoords = new int[COORDSETS];
        int[] newYCoords = new int[COORDSETS];
        for(int i = 0; i < COORDSETS; i++){
            newXCoords[i] = squares[i].getX();
            newYCoords[i] = squares[i].getY() + 1;
        }
        commitSquares(checkSquares(newXCoords, newYCoords));
        
    }
    
    
}
