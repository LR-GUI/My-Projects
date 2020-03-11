package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece {
    // pawn is a type of piece
    // this class is concerned with the properties of the pawn
    // properties of pieces consist only of how they move and attack

    // following the Tile coordinate convention, these are the relative coordinates to the pawn's position
    // that are its standard legal move destinations, the pawn's forward move and diagonal attack
    // 8 is the simple forward move, 16 is the double initial move, 7 and 9 are the attack diagonal moves
    private static final int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};


    // convenience constructor for isFirstMove
    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, true); // class instantiation taking alliance and position of the pawn

    }

    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, isFirstMove); // class instantiation taking alliance and position of the pawn

    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
    // given a board, this returns a list of possible move destination tile coordinates, from every initial position

        final List<Move> legalMoves = new ArrayList<>();

        // this will iterate for each candidate diagonal
        for (final int selectedCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            // take the actual destination, based on initial position and the relative position from the two
            // this depends on pawn's alliance, getDirection gives 1 for white and -1 for black
            final int candidateDestinationCoordinate =
                    this.piecePosition + this.pieceAlliance.getDirection() * selectedCandidateOffset;

            // this removes move candidates from bottom and top rows that fall out of the board
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            // then this checks if the tile is occupied. if so, a peaceMove is executed, no one is attacked
            if (selectedCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));

                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            // this checks double initial moves
             // for the pawn, the edge cases are calculated along the process
            } else if (selectedCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                // this checks if the double move is blocked by pieces; if not, the move is executed
                final int behindCandidateDestination = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                if (!board.getTile(behindCandidateDestination).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }

             // this is for one direction of attack moves
            } else if (selectedCandidateOffset == 7 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                            (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }
                    }
                } else if(board.getEnPassantPawn() != null) {
                    // if this piece is next to enPassantPawn
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }

             // the other direction of attack moves
            } else if (selectedCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                    if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                        legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination)));
                    } else {
                        legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }

                } else if(board.getEnPassantPawn() != null) {
                    // if this piece is next to enPassantPawn
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }

            }

        }
        // immutableList from google's external package guava
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getPieceMoved().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString () {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() {
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }
}