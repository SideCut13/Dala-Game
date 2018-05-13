package pl.kurcaba;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {


    private double mouseX,mouseY = 0;
    private int oldX, oldY = 0;

    private PieceType pieceType;

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public Piece (PieceType pieceType, int x, int y)
    {
        this.pieceType = pieceType;

        move(x,y);
        Ellipse background = new Ellipse(gameWindow.SQUARE_SIZE * 0.3125 , gameWindow.SQUARE_SIZE * 0.26);
        background.setFill(pieceType == pieceType.WHITE ? Color.BLACK : Color.valueOf("#c40003"));
        background.setStroke(Color.BLACK);
        background.setTranslateX((gameWindow.SQUARE_SIZE - gameWindow.SQUARE_SIZE  * 0.3125 * 2) / 2);
        background.setTranslateY((gameWindow.SQUARE_SIZE  - gameWindow.SQUARE_SIZE  * 0.26 * 2) / 2 + gameWindow.SQUARE_SIZE  * 0.07);

        Ellipse ellipse = new Ellipse(gameWindow.SQUARE_SIZE * 0.3125, gameWindow.SQUARE_SIZE * 0.26);

        ellipse.setFill(pieceType == PieceType.WHITE
                ? Color.WHITE : Color.BLACK);

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(gameWindow.SQUARE_SIZE * 0.03);

        ellipse.setTranslateX((gameWindow.SQUARE_SIZE - gameWindow.SQUARE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((gameWindow.SQUARE_SIZE - gameWindow.SQUARE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(background,ellipse);


        setOnMousePressed(event -> {

            mouseX = event.getSceneX() - gameWindow.BOARD_BORDER;
            mouseY = event.getSceneY() - gameWindow.BOARD_BORDER;

        });

        setOnMouseDragged(event -> {


            double mousePositionX = event.getSceneX() - gameWindow.BOARD_BORDER;
            double mousePositionY = event.getSceneY() - gameWindow.BOARD_BORDER;
            double mouseDifferenceX = mouseX - oldX * gameWindow.SQUARE_SIZE;
            double mouseDifferenceY = mouseY - oldY * gameWindow.SQUARE_SIZE;
            relocate(mousePositionX - mouseDifferenceX  ,mousePositionY - mouseDifferenceY );

        });
    }

    public Position getOldPiecePosition()
    {
        int xFieldPosition = (int) oldX ;
        int yFieldPosition = (int) oldY ;

        return new Position(xFieldPosition,yFieldPosition);
    }
    public void backToOldPosition()
    {
        move(oldX,oldY);
    }

    public void move(int x, int y)
    {
        this.oldX = x;
        this.oldY = y;

        relocate(oldX*gameWindow.SQUARE_SIZE,oldY*gameWindow.SQUARE_SIZE);
    }




}
