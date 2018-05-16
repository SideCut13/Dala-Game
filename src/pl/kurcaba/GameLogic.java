package pl.kurcaba;

/**
 * This class check whether move was compatible with game logic
 */
public class GameLogic implements BoardEvents {

    private final int ROUND_NEEDED_TO_PUT_ALL_PIECES = 11;
    private int gameStage = 1;
    private int moveCounter = 0;
    private boolean userHaveToHit = false;


    /**
     *
     * @param pieceOnSquare piece which was on clicked square
     * @param squarePosition clicked field position
     *
     *  this function check what to do, when player clicked field
     */
    @Override
    public void squareClicked(Piece pieceOnSquare,PositionOnBoard squarePosition) {

        if (moveCounter > ROUND_NEEDED_TO_PUT_ALL_PIECES) gameStage = 2;

        boolean fieldIsEmpty = pieceOnSquare == null;

        if (fieldIsEmpty)
        {
            boolean pieceHaveToBeInCenter = moveCounter <2;

            if(pieceHaveToBeInCenter) {
                
                boolean fieldIsInCenter = (squarePosition.coordX == 2 || squarePosition.coordX == 3) && (squarePosition.coordY == 2 || squarePosition.coordY == 3);
                if (fieldIsInCenter) {
                    if (gameStage == 1 && !userHaveToHit) GameWindow.createPlayersPiece(squarePosition);
                    moveCounter += 1;
                }
            }

            else
            {
                if (gameStage == 1 && !userHaveToHit) GameWindow.createPlayersPiece(squarePosition);
                moveCounter += 1;
            }
        }
        if (pieceOnSquare != null && pieceOnSquare.getPieceType() == PieceType.BLACK)
        {
            if(userHaveToHit){
                GameWindow.deletePiece(squarePosition);
                userHaveToHit = false;
            }
        }

    }

    /**
     *
     * @return true - if move was compatible with logic, false if wasn't
     */
    @Override
    public boolean pieceCouldBeMoved() {

        if(gameStage == 1) return false;
        else
        {
            if(!userHaveToHit) return true;
            else return false;
        }

    }
}
