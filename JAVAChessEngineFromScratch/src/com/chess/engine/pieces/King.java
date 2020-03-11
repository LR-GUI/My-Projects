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

import static com.chess.engine.board.Move.MajorMove;

public class King extends Piece {
    // king is a type of piece
    // this class is concerned with the properties of the king
    // properties of pieces consist only of how they move and attack

    // following the Tile coordinate convention, these are the relative coordinates to the king's position
    // that are its standard legal move destinations, the king's unitary queen move
    // these are all destinations, each unitary direction and diagonal
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8,-1,1,8,-9,-7,7,9};

    // convenience constructor for isFirstMove
    public King(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KING, pieceAlliance, piecePosition, true); // class instantiation taking alliance and position of the king

    }

    public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KING, pieceAlliance, piecePosition, isFirstMove); // class instantiation taking alliance and position of the king

    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    // given a board, this returns a list of possible move destination tile coordinates, from every initial position

        final List<Move> legalMoves = new ArrayList<>();

        // this will iterate for each candidate direction
        for (final int selectedCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            // the candidate initializes as the current position
            final int candidateDestinationCoordinate = this.piecePosition + selectedCandidateOffset;

            // this excludes candidates invalidated by edge cases, calculated below
            if (isFirstColumnExclusion(this.piecePosition, selectedCandidateOffset) ||
                    isEightColumnExclusion(this.piecePosition, selectedCandidateOffset)) {
                continue;
            }


            // for still valid coordinates, this checks if the tile is in range
            // this removes move candidates from bottom and top rows that fall out of the board
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                // then this checks if the tile is occupied. if so, a PeaceMove is executed, no one is attacked
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
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
    public King movePiece(final Move move) {
        return new King(move.getPieceMoved().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString () {
        return PieceType.KING.toString();
    }

    // Edge cases for the king, used above
    // for the first and last columns, some moves break and have to be discarded
    private static boolean isFirstColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset==7 || candidateOffset==-9 ||
                candidateOffset==-1);
    }
    private static boolean isEightColumnExclusion (final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
    }
}
