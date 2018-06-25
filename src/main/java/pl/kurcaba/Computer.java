package pl.kurcaba;


public class Computer {


    public static boolean checkWhetherComputerCanHit(PositionOnBoard newPiecePosition,PieceType[][] piecesOnBoard)
    {

        // here we check whether piece is between two black pieces


        boolean playerHaveToHit;
        boolean HaveThreeHorizontalNeighbour = false;
        boolean HaveThreeVerticalNeighbour = false;

        boolean isBoardCorner = ((newPiecePosition.coordX ==0 && (newPiecePosition.coordY==0 || newPiecePosition.coordY==GameWindow.BOARD_HEIGHT -1)) ||
                (newPiecePosition.coordX== GameWindow.BOARD_WIDTH -1) && (newPiecePosition.coordY==0 || newPiecePosition.coordY==GameWindow.BOARD_HEIGHT -1 ));
        if(!isBoardCorner) {

            HaveThreeHorizontalNeighbour = (newPiecePosition.coordX != 0 && newPiecePosition.coordX != GameWindow.BOARD_HEIGHT - 1);
            HaveThreeVerticalNeighbour = (newPiecePosition.coordY != 0 && newPiecePosition.coordY != GameWindow.BOARD_HEIGHT - 1);

        }


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
    private static boolean checkSquareHorizontal(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {
        boolean isMiddlePieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY] == PieceType.BLACK;
        if(!isMiddlePieceWhite) return false;

        boolean isLeftPieceWhite = piecesOnBoard[piecePosition.coordX-1][piecePosition.coordY] == PieceType.BLACK;
        if(!isLeftPieceWhite) return false;

        boolean isRightPieceWhite = piecesOnBoard[piecePosition.coordX+1][piecePosition.coordY] == PieceType.BLACK;
        if(!isRightPieceWhite) return false;

        return true;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece is white and if have two vertical white neighbours
     */
    private static boolean checkSquareVertical(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {

        boolean isMiddlePieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY] == PieceType.BLACK;
        if(!isMiddlePieceWhite) return false;

        boolean isUpPieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY-1] == PieceType.BLACK;
        if(!isUpPieceWhite) return false;

        boolean isDownPieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY+1] == PieceType.BLACK;
        if(!isDownPieceWhite) return false;

        return true;

    }

    public static void getPossibleMoves( PositionOnBoard piecePosition)
    {

        if(piecePosition.coordY-1 > 0) {
            PieceType[][] board = GameWindow.getBoard();
            if (board[piecePosition.coordX][piecePosition.coordY - 1] == null)
            {

                board[piecePosition.coordX][piecePosition.coordY] = null; // after move old position will be empty
                board[piecePosition.coordX][piecePosition.coordY-1] = PieceType.BLACK;
                boolean isHit =Computer.checkWhetherComputerCanHit(new PositionOnBoard(piecePosition.coordX,piecePosition.coordY-1)
                ,board);
                // put your code here

            }
        }
        if(piecePosition.coordY+1 < 6) {
            PieceType[][] board = GameWindow.getBoard();
            if (board[piecePosition.coordX][piecePosition.coordY + 1] == null)
            {
                board[piecePosition.coordX][piecePosition.coordY] = null; // after move old position will be empty
                board[piecePosition.coordX][piecePosition.coordY+1] = PieceType.BLACK;
                boolean isHit = Computer.checkWhetherComputerCanHit(new PositionOnBoard(piecePosition.coordX,piecePosition.coordY+1)
                        ,board);
                // put your code here
            }
        }
        if(piecePosition.coordX-1 > 0) {
            PieceType[][] board = GameWindow.getBoard();
            if (board[piecePosition.coordX -1][piecePosition.coordY] == null)
            {
                board[piecePosition.coordX][piecePosition.coordY] = null; // after move old position will be empty
                board[piecePosition.coordX-1][piecePosition.coordY] = PieceType.BLACK;
                boolean isHit = Computer.checkWhetherComputerCanHit(new PositionOnBoard(piecePosition.coordX-1,piecePosition.coordY)
                        ,board);
            }
        }
        if(piecePosition.coordX+1 < 6) {
            PieceType[][] board = GameWindow.getBoard();
            if (board[piecePosition.coordX+1][piecePosition.coordY] == null)
            {
                board[piecePosition.coordX][piecePosition.coordY] = null; // after move old position will be empty
                board[piecePosition.coordX+1][piecePosition.coordY] = PieceType.BLACK;
                boolean isHit = Computer.checkWhetherComputerCanHit(new PositionOnBoard(piecePosition.coordX+1,piecePosition.coordY)
                        ,board);
                // put your code here
            }
        }

    }

}
