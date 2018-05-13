package pl.kurcaba;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class gameWindow extends Application {

    public static final int SQUARE_SIZE = 100;
    public static final int BOARD_WIDTH = 6;
    public static final int BOARD_HEIGHT = 6;
    public static final int BOARD_BORDER = 150;

    private static Parent mainWindowLayout;
    private static Square[][] squareOnBoard = new Square[6][6];

    private static Group squareGroup = new Group();
    private static Group pieceGroup = new Group();

    private static final BoardEvents gameLogic = new gameLogic();


    @Override
    public void start(Stage primaryStage) throws Exception{
        mainWindowLayout = FXMLLoader.load(getClass().getResource("layouts/gameWindow.fxml"));
        primaryStage.setTitle("Dala Game");
        primaryStage.setScene(new Scene(mainWindowLayout, 900, 900));
        primaryStage.setResizable(false);
        primaryStage.show();
        createBoard();
    }

    private void createBoard() {
        Pane gameBoardPane = (Pane) mainWindowLayout.lookup("#gamePane");

        for(int x = 0; x < BOARD_WIDTH ; x+= 1)
        {
            for(int y = 0; y < BOARD_HEIGHT; y+= 1)
            {
                boolean squareColor = (x+y) % 2 == 0;
                Square square = new Square(squareColor,x,y);
                square.setOnMouseClicked(event -> {

                    gameLogic.squareClicked(square.getPiece(),new Position(square.X,square.Y));

                });

                squareOnBoard[x][y] = square;
                squareGroup.getChildren().add(square);
            }
        }

        gameBoardPane.getChildren().addAll(squareGroup,pieceGroup);

    }

    protected static void createPiece(Position newPiecePosition, PieceType type)
    {
        Piece piece = new Piece(type,newPiecePosition.X,newPiecePosition.Y);

        piece.setOnMouseReleased(event -> {

            pieceMoved(event,piece);

        });
        squareOnBoard[newPiecePosition.X][newPiecePosition.Y].setPiece(piece);
        pieceGroup.getChildren().add(piece);

    }
    protected static void pieceMoved(MouseEvent event, Piece piece)
    {
        int xSquareNumber =  ((int)event.getSceneX() - BOARD_BORDER) / SQUARE_SIZE;
        int ySquareNumber = (int) (event.getSceneY()- BOARD_BORDER) / SQUARE_SIZE;

        piece.backToOldPosition();


        if(squareOnBoard[xSquareNumber][ySquareNumber].getPiece() == null) {

            Position movedPieceOldPosition = piece.getOldPiecePosition();
            int differenceX = movedPieceOldPosition.X - xSquareNumber;
            int differenceY = movedPieceOldPosition.Y - ySquareNumber;

            if  (differenceX != 0 && differenceY != 0 ) return;
            if ((differenceX > 1 || differenceX < -1) || (differenceY > 1 || differenceY < -1))  return;
            else{

                if (gameLogic.pieceMoved()) {


                    Piece oldPiece = squareOnBoard[movedPieceOldPosition.X][movedPieceOldPosition.Y].getPiece();
                    pieceGroup.getChildren().remove(oldPiece);
                    squareOnBoard[movedPieceOldPosition.X][movedPieceOldPosition.Y].setPiece(null);

                    createPiece(new Position(xSquareNumber, ySquareNumber), PieceType.WHITE);

                    gameLogic.pieceMoved();
                }
            }
        }

    }


    protected static void deletePiece(Position piecePosition)
    {
        pieceGroup.getChildren().remove(squareOnBoard[piecePosition.X][piecePosition.Y]);
        squareOnBoard[piecePosition.X][piecePosition.Y].setPiece(null);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
