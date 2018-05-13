package pl.kurcaba;

public class gameLogic implements BoardEvents {


    int gameStage = 1;
    int moveCounter = 0;
    boolean userHit = false;


    @Override
    public void squareClicked(Piece pieceOnSquare,Position squarePosition) {

        if (moveCounter > 11) gameStage = 2;

        if (pieceOnSquare == null)
        {
            if(moveCounter <2) {
                if ((squarePosition.X == 2 || squarePosition.X == 3) && (squarePosition.Y == 2 || squarePosition.Y == 3)) {
                    if (gameStage == 1 && !userHit) gameWindow.createPiece(squarePosition, PieceType.WHITE);
                    moveCounter += 1;
                }
            }else
            {
                if (gameStage == 1 && !userHit) gameWindow.createPiece(squarePosition, PieceType.WHITE);
                moveCounter += 1;
            }
        }
        if (pieceOnSquare != null && pieceOnSquare.getPieceType() == PieceType.BLACK)
        {
            if(userHit){
                gameWindow.deletePiece(squarePosition);
                userHit = false;
            }
        }

    }

    @Override
    public boolean pieceMoved() {

        if(gameStage == 1) return true;
        else
        {
            if(!userHit) return true;
            else return false;
        }

    }
}
