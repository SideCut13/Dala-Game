package pl.kurcaba;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * This class check whether move was compatible with game logic
 */
public class GameLogic implements BoardEvents {

    private final int ROUND_NEEDED_TO_PUT_ALL_PIECES = 11;
    private int gamePhase = 1;
    private int moveCounter = 0;
    private boolean playerHaveToHit = false;






    /**
     *
     * @param pieceOnSquare piece which was on clicked square
     * @param squarePosition clicked field position
     *
     *  this function check what to do, when player clicked field
     */
    @Override
    public void squareClicked(Piece pieceOnSquare,PositionOnBoard squarePosition) {



        boolean fieldIsEmpty = pieceOnSquare == null;

        if (fieldIsEmpty)
        {
            boolean pieceHaveToBeInCenter = moveCounter <2;

            if(pieceHaveToBeInCenter) {
                
                boolean fieldIsInCenter = (squarePosition.coordX == 2 || squarePosition.coordX == 3) && (squarePosition.coordY == 2 || squarePosition.coordY == 3);
                if (fieldIsInCenter) {
                    if (gamePhase == 1 && !playerHaveToHit) GameWindow.createPlayersPiece(squarePosition);
                    moveCounter += 1;
                    if (moveCounter > ROUND_NEEDED_TO_PUT_ALL_PIECES) {
                        gamePhase = 2;
                        GameWindow.changeGamePhase(true);

                    }
                }
            }

            else
            {
                if (gamePhase == 1 && !playerHaveToHit) GameWindow.createPlayersPiece(squarePosition);
                moveCounter += 1;
                if (moveCounter > ROUND_NEEDED_TO_PUT_ALL_PIECES) {
                    gamePhase = 2;
                    GameWindow.changeGamePhase(true);

                }
            }
        }
        if (pieceOnSquare != null && pieceOnSquare.getPieceType() == PieceType.BLACK)
        {
            if(playerHaveToHit){
                GameWindow.deletePiece(squarePosition);
                playerHaveToHit = false;


            }
        }
        playerHaveToHit = checkWhetherPlayerHaveToHit(squarePosition);
        changeReqiuredActionLabel();

    }

    /**
     *
     * @return true - if move was compatible with logic, false if wasn't
     */
    @Override
    public boolean pieceCouldBeMoved(PositionOnBoard newPiecePosition) {

        if(gamePhase == 1) return false;
        else
        {
            if(!playerHaveToHit)
            {
                playerHaveToHit = checkWhetherPlayerHaveToHit(newPiecePosition);
                changeReqiuredActionLabel();
                return true;

            }
            else return false;
        }

    }

    /**
     *
     * @return return true or false, if player have to delete enemy piece, or false if he doesn't have to.
     * This method checks whether player have to hit enemy piece
     */
    private boolean checkWhetherPlayerHaveToHit(PositionOnBoard newPiecePosition)
    {
        PieceType[][] piecesOnBoard = GameWindow.getBoard();

                boolean HaveThreeHorizontalNeighbour;
                boolean HaveThreeVerticalNeighbour;

                boolean isBoardCorner = ((newPiecePosition.coordX ==0 && (newPiecePosition.coordY==0 || newPiecePosition.coordY==GameWindow.BOARD_HEIGHT -1)) ||
                        (newPiecePosition.coordX== GameWindow.BOARD_WIDTH -1) && (newPiecePosition.coordY==0 || newPiecePosition.coordY==GameWindow.BOARD_HEIGHT -1 ));
                if(!isBoardCorner) {

                    HaveThreeHorizontalNeighbour = (newPiecePosition.coordX != 0 && newPiecePosition.coordX != GameWindow.BOARD_HEIGHT - 1);
                    HaveThreeVerticalNeighbour = (newPiecePosition.coordY != 0 && newPiecePosition.coordY != GameWindow.BOARD_HEIGHT - 1);

                }
                else return false;

                if(HaveThreeHorizontalNeighbour)
                {
                    boolean playerHaveToHit = checkSquareHorizontal(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX,newPiecePosition.coordY));
                    if(playerHaveToHit) return true;
                }
                if(HaveThreeVerticalNeighbour)
                {
                    boolean playerHaveToHit = checkSquareVertical(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX,newPiecePosition.coordY));
                    if(playerHaveToHit) return true;
                }


                return false;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece have two horizontal neighbours
     */
    private boolean checkSquareHorizontal(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {
        boolean isLeftPieceWhite = piecesOnBoard[piecePosition.coordX-1][piecePosition.coordY] == PieceType.WHITE;
        if(!isLeftPieceWhite) return false;

        boolean isRightPieceWhite = piecesOnBoard[piecePosition.coordX+1][piecePosition.coordY] == PieceType.WHITE;
        if(!isRightPieceWhite) return false;

        return true;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece have two vertical neighbours
     */
    private boolean checkSquareVertical(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {
        boolean isUpPieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY-1] == PieceType.WHITE;
        if(!isUpPieceWhite) return false;

        boolean isDownPieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY+1] == PieceType.WHITE;
        if(!isDownPieceWhite) return false;


        return true;

    }



    /**
     *
     * This method will launch method which changes required action text in GUI
     *
     */
    private void changeReqiuredActionLabel()
    {
        if(playerHaveToHit)
        {
            GameWindow.changeRequiredAction(RequiredAction.HIT);
        }else
        {
            if(gamePhase == 1) GameWindow.changeRequiredAction(RequiredAction.DROP);
            else GameWindow.changeRequiredAction(RequiredAction.MOVE);
        }
    }


}
