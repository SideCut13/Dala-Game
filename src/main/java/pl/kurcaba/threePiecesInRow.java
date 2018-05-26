package pl.kurcaba;

public class threePiecesInRow {

    private PositionOnBoard middlePiece;
    private PositionOnBoard leftPiece;
    private PositionOnBoard rightPiece;

    public threePiecesInRow(PositionOnBoard middlePiece, PositionOnBoard leftPiece, PositionOnBoard rightPiece) {
        this.middlePiece = middlePiece;
        this.leftPiece = leftPiece;
        this.rightPiece = rightPiece;
    }

    public PositionOnBoard getMiddlePiece() {
        return middlePiece;
    }

    public PositionOnBoard getLeftPiece() {
        return leftPiece;
    }

    public PositionOnBoard getRightPiece() {
        return rightPiece;
    }
}
