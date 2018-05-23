package pl.kurcaba;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * its one piece on the board, ensure smooth relocate when player drag a piece
 * piece remember its old position, and can back when move was incorrect
 */
public class Piece extends StackPane {


    private double mouseCoordX,mouseCoordY = 0;
    private int oldCoordX, oldCoordY = 0;

    private PieceType pieceType;

    public PieceType getPieceType() {
        return pieceType;
    }


    public Piece (PieceType pieceType, int coordX, int coordY)
    {
        this.pieceType = pieceType;

        movePiece(coordX,coordY);
        Ellipse pieceBackground = createPieceBackground();
        Ellipse pieceTop = createPieceTop();


        getChildren().addAll(pieceBackground,pieceTop);

        setOnMousePressed(event -> {
            mouseCoordX = event.getSceneX() - GameWindow.BOARD_BORDER;
            mouseCoordY = event.getSceneY() - GameWindow.BOARD_BORDER;
        });

        setOnMouseDragged(mouseEvent -> {
            relocatePieceOnScreen(mouseEvent);
        });
    }

    public PositionOnBoard getOldPiecePosition()
    {
        int coordX = (int) oldCoordX ;
        int coordY = (int) oldCoordY ;

        return new PositionOnBoard(coordX,coordY);
    }
    public void backToOldPosition()
    {
        movePiece(oldCoordX,oldCoordY);
    }

    public void movePiece(int x, int y)
    {
        this.oldCoordX = x;
        this.oldCoordY = y;

        relocate(oldCoordX*GameWindow.SQUARE_SIZE,oldCoordY*GameWindow.SQUARE_SIZE);
    }

    private Ellipse createPieceBackground()
    {
        Ellipse background = new Ellipse(GameWindow.SQUARE_SIZE * 0.3125 , GameWindow.SQUARE_SIZE * 0.26);
        background.setFill(pieceType == pieceType.WHITE ? Color.BLACK : Color.valueOf("#c40003"));
        background.setStroke(Color.BLACK);
        background.setTranslateX((GameWindow.SQUARE_SIZE - GameWindow.SQUARE_SIZE  * 0.3125 * 2) / 2);
        background.setTranslateY((GameWindow.SQUARE_SIZE  - GameWindow.SQUARE_SIZE  * 0.26 * 2) / 2 + GameWindow.SQUARE_SIZE  * 0.07);
        return background;
    }
    private Ellipse createPieceTop()
    {
        Ellipse pieceTop = new Ellipse(GameWindow.SQUARE_SIZE * 0.3125, GameWindow.SQUARE_SIZE * 0.26);

        pieceTop.setFill(pieceType == PieceType.WHITE
                ? Color.WHITE : Color.BLACK);

        pieceTop.setStroke(Color.BLACK);
        pieceTop.setStrokeWidth(GameWindow.SQUARE_SIZE * 0.03);

        pieceTop.setTranslateX((GameWindow.SQUARE_SIZE - GameWindow.SQUARE_SIZE * 0.3125 * 2) / 2);
        pieceTop.setTranslateY((GameWindow.SQUARE_SIZE - GameWindow.SQUARE_SIZE * 0.26 * 2) / 2);
        return pieceTop;
    }
    private void relocatePieceOnScreen(MouseEvent event)
    {
        double mousePositionX = event.getSceneX() - GameWindow.BOARD_BORDER;
        double mousePositionY = event.getSceneY() - GameWindow.BOARD_BORDER;
        double mouseDifferenceX = mouseCoordX - oldCoordX * GameWindow.SQUARE_SIZE;
        double mouseDifferenceY = mouseCoordY - oldCoordY * GameWindow.SQUARE_SIZE;
        relocate(mousePositionX - mouseDifferenceX  ,mousePositionY - mouseDifferenceY );
    }
    
}
