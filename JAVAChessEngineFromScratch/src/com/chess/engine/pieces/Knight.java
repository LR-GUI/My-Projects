package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {
    // knight is a type of piece
    // this class is concerned with the properties of the knight
    // properties of pieces consist only of how they move and attack

    // following the Tile coordinate convention, these are the relative coordinates to the knight's position
    // that are its standard legal move destinations, the knight's L move
    private static final int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};

    // convenience constructor for isFirstMove
    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition, true); // class instantiation taking alliance and position of the knight
    }

    public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition, isFirstMove); // class instantiation taking alliance and position of the knight
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    // a collection is above a list and the return doesn't have to restrict to list, despite being a list here
    // given a board, this returns a list of possible move destination tile coordinates, from every initial position

        final List<Move> legalMoves = new ArrayList<>();

        // this will iterate for each candidate destination
        for (final int selectedCandidateOffset : CANDIDATE_MOVE_COORDINATES) {

            // take the actual destination, based on initial position and the relative position from the two
            final int candidateDestinationCoordinate = this.piecePosition + selectedCandidateOffset;

            // this excludes candidates invalidated by edge cases, calculated below
            if (isFirstColumnExclusion(this.piecePosition,selectedCandidateOffset) ||
                    isSecondColumnExclusion(this.piecePosition,selectedCandidateOffset) ||
                    isSeventhColumnExclusion(this.piecePosition,selectedCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition,selectedCandidateOffset))
                continue;

            // for still valid coordinates, this checks if the tile is in range
            // this removes move candidates from bottom and top rows that fall out of the board
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                // then this checks if the tile is occupied. if so, a PeaceMove is executed, no one is attacked
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    // else, an AttackMove is executed if the piece is enemy aligned
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }
            }
        }

        // immutableList from google's external package guava
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getPieceMoved().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString () {
        return PieceType.KNIGHT.toString();
    }

    // Edge cases for the knight, used above
    // for the first two and last two columns, some moves break and have to be discarded
    private static boolean isFirstColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset==-17 || candidateOffset==-10 ||
            candidateOffset==6 || candidateOffset==15);
    }
    private static boolean isSecondColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }
    private static boolean isSeventhColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }
    private static boolean isEightColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset==-6 || candidateOffset==-15 ||
                candidateOffset==10 || candidateOffset==17);
    }

}
