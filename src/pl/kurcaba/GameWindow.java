package pl.kurcaba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class support Game Window and Board Pane, create and display 36 squares. Class create, delete and display pieces
 * Check whether the move was compatible with rules (but not with game logic)
 */
public class GameWindow extends Application {

    public static final int SQUARE_SIZE = 100;
    public static final int BOARD_WIDTH = 6;
    public static final int BOARD_HEIGHT = 6;
    public static final int BOARD_BORDER = 150;

    private static Parent mainWindowLayout;
    private static Square[][] squaresOnBoard = new Square[6][6];

    private static Group squareGroup = new Group();
    private static Group pieceGroup = new Group();

    private static final BoardEvents gameLogic = new GameLogic();

    /**
     *
     * This method load layout and create main Scene, after all launch createBoard();
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        mainWindowLayout = FXMLLoader.load(getClass().getResource("layouts/gameWindow.fxml"));
        primaryStage.setTitle("Dala Game");
        primaryStage.setScene(new Scene(mainWindowLayout, 900, 900));
        primaryStage.setResizable(false);
        primaryStage.show();
        createBoard();
    }

    /**
     * This method create game board, create squares in two different colors. Add squares to squares table and display them on Game Pane
     */
    private void createBoard() {
        Pane gameBoardPane = (Pane) mainWindowLayout.lookup("#gamePane");

        for(int coordX = 0; coordX < BOARD_WIDTH ; coordX+= 1)
        {
            for(int coordY = 0; coordY < BOARD_HEIGHT; coordY+= 1)
            {
                boolean squareColor = (coordX+coordY) % 2 == 0;
                Square square = new Square(squareColor,coordX,coordY);
                square.setOnMouseClicked(event -> {
                    gameLogic.squareClicked(square.getPiece(),new PositionOnBoard(square.coordX,square.coordY));
                });

                squaresOnBoard[coordX][coordY] = square;
                squareGroup.getChildren().add(square);
            }
        }
        gameBoardPane.getChildren().addAll(squareGroup,pieceGroup);
    }

    /**
     *
     * @param newPiecePosition place when new piece is created
     *
     * This method create players pieces ( White ), add them to the group and display on field
     */

    protected static void createPlayersPiece(PositionOnBoard newPiecePosition)
    {
        Piece piece = new Piece(PieceType.WHITE,newPiecePosition.coordX,newPiecePosition.coordY);

        piece.setOnMouseReleased(event -> {
            pieceMovedByPlayer(event,piece);
        });
        squaresOnBoard[newPiecePosition.coordX][newPiecePosition.coordY].setPiece(piece);
        pieceGroup.getChildren().add(piece);

    }

    /**
     *
     *
     * @param event this is place where mouse was released
     * @param piece is piece which was moved
     *
     * This method check whether move was compatible with rules, and game logic.
     * If move was correct create new piece on new position, and delete piece on old position
     */
    protected static void pieceMovedByPlayer(MouseEvent event, Piece piece) {
        int newCoordX = ((int) event.getSceneX() - BOARD_BORDER) / SQUARE_SIZE;
        int newCoordY = (int) (event.getSceneY() - BOARD_BORDER) / SQUARE_SIZE;

        piece.backToOldPosition();
        boolean newFieldIsEmpty = squaresOnBoard[newCoordX][newCoordY].getPiece() == null;

        if (newFieldIsEmpty) {

            PositionOnBoard oldPosition = piece.getOldPiecePosition();
            int differenceX = oldPosition.coordX - newCoordX;
            int differenceY = oldPosition.coordY - newCoordY;

            boolean moveWasDiagonal = differenceX != 0 && differenceY != 0;
            if (moveWasDiagonal) return;

            boolean moveWasTooFar = (differenceX > 1 || differenceX < -1) || (differenceY > 1 || differenceY < -1);
            if (moveWasTooFar) return;


            if (gameLogic.pieceCouldBeMoved()) {

                deletePiece(oldPosition);
                createPlayersPiece(new PositionOnBoard(newCoordX, newCoordY));

            }
        }
    }

    /**
     *  this method delete piece from field, and from display
     */

    protected static void deletePiece(PositionOnBoard piecePosition)
    {
        Piece pieceToRemove = squaresOnBoard[piecePosition.coordX][piecePosition.coordY].getPiece();
        pieceGroup.getChildren().remove(pieceToRemove);
        squaresOnBoard[piecePosition.coordX][piecePosition.coordY].setPiece(null);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
