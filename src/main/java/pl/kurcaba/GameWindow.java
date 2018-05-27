package pl.kurcaba;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
    public static final int BOARD_BORDER = 75;
    public static final int WINDOW_HEIGHT = 750;
    public static final int WINDOW_LENGHT = 1200;

    private static Parent mainWindowLayout;
    private static Square[][] squaresOnBoard = new Square[BOARD_HEIGHT][BOARD_WIDTH];

    private static Group squareGroup;
    private static Group pieceGroup;
    private Label gamePhase;

    private Button resetGameButton;
    private static BoardEvents gameLogic = new GameLogic();



    /**
     * This method create game board, create squares in two different colors. Add squares to squares table and display them on Game Pane
     */
    private void createBoard() {
        Pane gameBoardPane = (Pane) mainWindowLayout.lookup("#gamePane");

        squareGroup = new Group();
        pieceGroup = new Group();

        for(int coordX = 0; coordX < BOARD_HEIGHT ; coordX+= 1)
        {
            for(int coordY = 0; coordY < BOARD_WIDTH; coordY+= 1)
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

        int newCoordX = (((int) event.getSceneX() - BOARD_BORDER) / SQUARE_SIZE) ;
        int newCoordY = ((int) (event.getSceneY() - BOARD_BORDER) / SQUARE_SIZE) ;

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

            PositionOnBoard newPiecePosition = new PositionOnBoard(newCoordX, newCoordY);

            gameLogic.pieceWasMoved(oldPosition,newPiecePosition);

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

    /**
     *
     * @param isMovePhase true if is move phase, false if is drop phase
     *                    this method change gamePhase label
     */
    public static void changeGamePhase(boolean isMovePhase)
    {

        Text gamePhaseText = (Text) mainWindowLayout.lookup("#gamePhase");
        if(isMovePhase)
        {
            gamePhaseText.setText("Faza gry: ruch");
        }else
        {
            gamePhaseText.setText("Faza gry: rozstawianie");
        }

    }

    /**
     *
     * @param action is Enum which define needed action type
     *  This method change requiredAction label in game window
     */
    public static void changeRequiredAction(RequiredAction action) {

        Text requiredActionText = (Text) mainWindowLayout.lookup("#requiredAction");
        switch(action)
        {
            case DROP:
                requiredActionText.setText("Wymagana akcja: rozstawienie");
                break;
            case MOVE:
                requiredActionText.setText("Wymagana akcja: ruch");
                break;
            case HIT:
                requiredActionText.setText("Wymagana akcja: bicie");
                break;
        }
    }

    /**
     *
     *
     * @return table with size : BOARD_HEIGHT*BOARD_WIDTH. Table has information about piece type on all fields
     */
    public static PieceType[][] getBoard()
    {
        PieceType[][] board = new PieceType[BOARD_HEIGHT][BOARD_WIDTH];

        for(int i = 0;i<BOARD_HEIGHT;i++)
        {
            for(int j = 0;j<BOARD_WIDTH;j++)
            {
                if(squaresOnBoard[i][j].getPiece() == null) board[i][j] = null;
                else board[i][j] = squaresOnBoard[i][j].getPiece().getPieceType();

            }
        }
        return board;
    }

    /**
     *
     * this button resets whole game
     */
    private void resetGame()
    {
        createBoard();
        gameLogic = new GameLogic();
        changeGamePhase(false);
        changeRequiredAction(RequiredAction.DROP);
    }
    /**
     *
     * This method load layout and create main Scene, after all launch createBoard();
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        mainWindowLayout = FXMLLoader.load(getClass().getResource("/layouts/gameWindow.fxml"));
        primaryStage.setTitle("Dala Game");
        primaryStage.setScene(new Scene(mainWindowLayout, WINDOW_LENGHT, WINDOW_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
        createBoard();

        Button resetButton = (Button) mainWindowLayout.lookup("#resetButton");

        resetButton.setOnMouseClicked(event -> resetGame() );

    }
    public static void main(String[] args) {
        launch(args);
    }


}

