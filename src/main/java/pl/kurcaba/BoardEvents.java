package pl.kurcaba;

public interface BoardEvents {

    /**
     *
     * @param pieceOnSquare piece on clicked square
     * @param squarePosition clicked square position
     * What to do, when square was clicked
     */
    void squareClicked(Piece pieceOnSquare,PositionOnBoard squarePosition);

    /**
     *
     * @return true if move was compatible with game logic, false if wasn't
     */
    boolean pieceCouldBeMoved(PositionOnBoard newPiecePosition);

}

