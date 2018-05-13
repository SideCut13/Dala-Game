package pl.kurcaba;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {

    public final int X;
    public final int Y;


    private Piece piece = null;

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square(boolean color, int x, int y)
    {
        this.X = x;
        this.Y = y;

        setWidth(gameWindow.SQUARE_SIZE);
        setHeight(gameWindow.SQUARE_SIZE);

        relocate(x * gameWindow.SQUARE_SIZE,y*gameWindow.SQUARE_SIZE);
        setFill(color ? Color.valueOf("#feb") : Color.valueOf("#582"));

        

    }






}
