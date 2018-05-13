package pl.kurcaba;

public enum PieceType {
    WHITE(1),BLACK(2);

    final int color;

    PieceType(int color)
    {
        this.color = color;
    }

}
