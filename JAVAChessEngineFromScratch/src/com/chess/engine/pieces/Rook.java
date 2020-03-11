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

import static com.chess.engine.board.Move.AttackMove;
import static com.chess.engine.board.Move.MajorMove;

public class Rook extends Piece {
    // rook is a type of piece
    // this class is concerned with the properties of the rook
    // properties of pieces consist only of how they move and attack

    // following the Tile coordinate convention, these are the relative coordinates to the rook's position
    // that are its standard legal move destinations, the rook's straight move
    // all destinations are multiples of these four numbers, representing each direction
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8,-1,1,8};

    // convenience constructor for isFirstMove
    public Rook(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.ROOK, pieceAlliance, piecePosition, true); // class instantiation taking alliance and position of the rook

    }

    public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.ROOK, pieceAlliance, piecePosition, isFirstMove); // class instantiation taking alliance and position of the rook

    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    // given a board, this returns a list of possible move destination tile coordinates, from every initial position

        final List<Move> legalMoves = new ArrayList<>();

        // this will iterate for each candidate direction
        for (final int selectedCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            // the candidate initializes as the current position
            int candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                // this goes until the destination is off the board

                // this excludes candidates invalidated by edge cases, calculated below
                if(isFirstColumnExclusion(candidateDestinationCoordinate,selectedCandidateOffset) ||
                        isEightColumnExclusion(candidateDestinationCoordinate,selectedCandidateOffset)) {
                    break;
                }

                // update the destination to the next tile of the selected direction
                candidateDestinationCoordinate += selectedCandidateOffset;

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
                        break; // if the destination is occupied, this break removes the rest of the diagonal
                               // that is blocked by this occupying piece
                    }
                }
            }
        }
        // immutableList from google's external package guava
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getPieceMoved().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString () {
        return PieceType.ROOK.toString();
    }

    // Edge cases for the rook, used above
    // for the first and last columns, one direction breaks and has to be discarded
    private boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset==-1);
    }

    private boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHT_COLUMN[currentPosition] && (candidateOffset==1);
    }
}
