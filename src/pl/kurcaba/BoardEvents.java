package pl.kurcaba;

public interface BoardEvents {

    void squareClicked(Piece pieceOnSquare,Position squarePosition);
    boolean pieceMoved();

}

