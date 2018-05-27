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
     * if move was correct, method deletes old piece, and create new
     */
    void pieceWasMoved(PositionOnBoard oldPiecePosition,PositionOnBoard newPiecePosition);

}

