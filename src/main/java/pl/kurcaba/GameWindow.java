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
import java.util.ArrayList;
import java.util.List;

/**
 * This class support Game Window and Board Pane, create and display 36 squares. Class create, delete and display pieces
 * Check whether the move was compatible with rules (but not with game logic)
 */
public class GameWindow extends Application {

    public static final int SQUARE_SIZE = 75;
    public static final int BOARD_WIDTH = 6;
    public static final int BOARD_HEIGHT = 6;
    public static final int BOARD_BORDER = 75;
    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_LENGHT = 900;

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
     * @param newPiecePosition place when new piece is created
     *
     * This method create computer's pieces ( Black ), add them to the group and display on field
     */

    protected static void createComputerPiece(PositionOnBoard newPiecePosition)
    {
        if(squaresOnBoard[newPiecePosition.coordX][newPiecePosition.coordY].getPiece() == null) {
            Piece piece = new Piece(PieceType.BLACK, newPiecePosition.coordX, newPiecePosition.coordY);
            squaresOnBoard[newPiecePosition.coordX][newPiecePosition.coordY].setPiece(piece);
            pieceGroup.getChildren().add(piece);
        }

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

    public static void deletePiece(PositionOnBoard piecePosition)
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
                requiredActionText.setText("Akcja: rozstawienie");
                break;
            case MOVE:
                requiredActionText.setText("Akcja: ruch");
                break;
            case HIT:
                requiredActionText.setText("Akcja: bicie");
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
    public static void getPossibleMoves(PieceType[][] board, PositionOnBoard piecePosition,List<PositionOnBoard> possibleMovesList)
    {

        if(piecePosition.coordY-1 > 0) {
            if (board[piecePosition.coordX][piecePosition.coordY - 1] == null)
            {
                possibleMovesList.add(new PositionOnBoard(piecePosition.coordX,piecePosition.coordY-1));
            }
        }
        if(piecePosition.coordY+1 < 6) {
            if (board[piecePosition.coordX][piecePosition.coordY + 1] == null)
            {
                possibleMovesList.add(new PositionOnBoard(piecePosition.coordX,piecePosition.coordY-1));
            }
        }
        if(piecePosition.coordX-1 > 0) {
            if (board[piecePosition.coordX -1][piecePosition.coordY] == null)
            {
                possibleMovesList.add(new PositionOnBoard(piecePosition.coordX-1,piecePosition.coordY));
            }
        }
        if(piecePosition.coordX+1 < 6) {
            if (board[piecePosition.coordX+1][piecePosition.coordY] == null)
            {
                possibleMovesList.add(new PositionOnBoard(piecePosition.coordX+1,piecePosition.coordY));
            }
        }

    }
    static int log2(int n)
    {
        return (n==1)? 0 : 1 + log2(n/2);
    }

    public static int minimax(int depth, int nodeIndex, boolean  isMax,
                       int scores[], int h)
    {
        // Terminating condition. i.e leaf node is reached
        if (depth == h)
            return scores[nodeIndex];

        // If current move is maximizer, find the maximum attainable
        // value
        if (isMax)
            return Math.max(minimax(depth+1, nodeIndex*2, false, scores, h),
                    minimax(depth+1, nodeIndex*2 + 1, false, scores, h));

            // Else (If current move is Minimizer), find the minimum
            // attainable value
        else
            return Math.min(minimax(depth+1, nodeIndex*2, true, scores, h),
                    minimax(depth+1, nodeIndex*2 + 1, true, scores, h));
    }
    public static void getPossibleComputerMove() {
        PieceType[][] board = getBoard();
        GameLogic gameLogic = new GameLogic();
        int gamePhase = gameLogic.getGamePhase();
        RequiredAction action = null;
        List<PositionOnBoard> positionListWhite = new ArrayList();
        List<PositionOnBoard> positionListBlack = new ArrayList();

        switch (gamePhase) {
            case 1:
                if(board[3][2] == null){
                    createComputerPiece(new PositionOnBoard(3, 2));
                }
                else if(board[2][3] == null){
                    createComputerPiece(new PositionOnBoard(2, 3));
                }
                else if(board[3][3] == null){
                    createComputerPiece(new PositionOnBoard(3, 3));
                }
                else if(board[2][2] == null){
                    createComputerPiece(new PositionOnBoard(2, 2));
                }
                //createComputerPiece(new PositionOnBoard(2, 3));
                break;
            case 2:
                for (int i = 0; i < BOARD_HEIGHT; i++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        Piece piece = squaresOnBoard[i][j].getPiece();
                        if (piece != null && piece.getPieceType() == PieceType.WHITE) {
                            getPossibleMoves(board, new PositionOnBoard(i, j), positionListWhite);
                        }
                        if (piece != null && piece.getPieceType().equals(PieceType.BLACK)) {
                            getPossibleMoves(board, new PositionOnBoard(i, j), positionListBlack);
                        }
                    }
                }
                for (int i = 0; i < positionListBlack.size() ; i++) {
                    for (int j = 0; j < positionListWhite.size(); j++) {
                        if (positionListWhite.get(j).equals(positionListBlack.get(i))) {
                            int scores[] = {3, 5, 2, 9, 12, 5, 23, 23};
                            int n = scores.length;
                            int h = log2(n);
                            minimax(0, 0, true, scores, h);
                            createComputerPiece(positionListBlack.get(i));
                        }
                    }
                }
                break;
        }
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

