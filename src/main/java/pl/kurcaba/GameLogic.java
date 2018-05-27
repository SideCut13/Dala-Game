package pl.kurcaba;


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
        if(!playerHaveToHit) {
            playerHaveToHit = checkWhetherPlayerHaveToHit(squarePosition);
            changeReqiuredActionLabel();
        }

    }

    /**
     *
     * if move was correct, method deletes old piece, and create new
     */
    @Override
    public void pieceWasMoved(PositionOnBoard oldPiecePosition,PositionOnBoard newPiecePosition) {

        if(gamePhase == 1) return;
        else
        {
            if(!playerHaveToHit)
            {
                GameWindow.deletePiece(oldPiecePosition);
                GameWindow.createPlayersPiece(newPiecePosition);
                playerHaveToHit = checkWhetherPlayerHaveToHit(newPiecePosition);
                changeReqiuredActionLabel();
            }
        }
    }


    /**
     *
     * @return return true or false, if player have to delete enemy piece, or false if he doesn't have to.
     * This method checks whether player have to removes opponent's piece
     */
    private boolean checkWhetherPlayerHaveToHit(PositionOnBoard newPiecePosition)
    {

        // here we check whether piece is between two white pieces
        PieceType[][] piecesOnBoard = GameWindow.getBoard();

        boolean playerHaveToHit;
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
            playerHaveToHit = checkSquareHorizontal(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX,newPiecePosition.coordY));
            if(playerHaveToHit) return true;
        }
        if(HaveThreeVerticalNeighbour)
        {
            playerHaveToHit = checkSquareVertical(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX,newPiecePosition.coordY));
            if(playerHaveToHit) return true;
        }

        //here we check whether piece is next to two white pieces

        boolean canCheckUpNeighbour = (newPiecePosition.coordY > 1);
        if(canCheckUpNeighbour)
        {
            playerHaveToHit = checkSquareVertical(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX,newPiecePosition.coordY-1));
            if(playerHaveToHit) return true;
        }
        boolean canCheckDownNeighbour = (newPiecePosition.coordY < GameWindow.BOARD_HEIGHT -2);
        if(canCheckDownNeighbour)
        {
            playerHaveToHit = checkSquareVertical(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX,newPiecePosition.coordY+1));
            if(playerHaveToHit) return true;
        }
        boolean canCheckLeftNeighbour = (newPiecePosition.coordX > 1);
        if(canCheckLeftNeighbour)
        {
            playerHaveToHit = checkSquareHorizontal(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX-1,newPiecePosition.coordY));
            if(playerHaveToHit) return true;
        }
        boolean canCheckRightNeighbour = (newPiecePosition.coordX < GameWindow.BOARD_WIDTH -2);
        if(canCheckRightNeighbour)
        {
            playerHaveToHit = checkSquareHorizontal(piecesOnBoard,new PositionOnBoard(newPiecePosition.coordX+1,newPiecePosition.coordY));
            if(playerHaveToHit) return true;
        }

        return false;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece is white and if have two horizontal white neighbours
     */
    private boolean checkSquareHorizontal(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {
        boolean isMiddlePieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY] == PieceType.WHITE;
        if(!isMiddlePieceWhite) return false;

        boolean isLeftPieceWhite = piecesOnBoard[piecePosition.coordX-1][piecePosition.coordY] == PieceType.WHITE;
        if(!isLeftPieceWhite) return false;

        boolean isRightPieceWhite = piecesOnBoard[piecePosition.coordX+1][piecePosition.coordY] == PieceType.WHITE;
        if(!isRightPieceWhite) return false;

        return true;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece is white and if have two vertical white neighbours
     */
    private boolean checkSquareVertical(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {

        boolean isMiddlePieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY] == PieceType.WHITE;
        if(!isMiddlePieceWhite) return false;

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
