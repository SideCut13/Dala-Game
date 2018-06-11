package pl.kurcaba;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * It is one square on the board
 */
public class Square extends Rectangle {

    public final int coordX;
    public final int coordY;


    private Piece piece = null;

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square(boolean color, int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;

        setWidth(GameWindow.SQUARE_SIZE);
        setHeight(GameWindow.SQUARE_SIZE);

        relocate(coordX * GameWindow.SQUARE_SIZE, coordY * GameWindow.SQUARE_SIZE);
        setFill(color ? Color.valueOf("#C89473") : Color.valueOf("#4E2C15"));
    }
}
