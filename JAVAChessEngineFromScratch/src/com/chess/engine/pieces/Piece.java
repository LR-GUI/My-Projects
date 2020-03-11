package com.chess.engine.pieces; // The Piece class is going to the pieces subpackage

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    // Each different piece is going to be a subclass of Piece
    // Pieces have an alliance (black or white) and a position in the board

    protected final Alliance pieceAlliance;
    protected final int piecePosition;

    protected final PieceType pieceType;

    protected final boolean isFirstMove;

    private final int cachedHashCode;

    Piece (final PieceType pieceType, final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;

        this.pieceType = pieceType;

        this.isFirstMove = isFirstMove;

        this.cachedHashCode = computeHashCode();
    }


    @Override
    public boolean equals(final Object other) {
        if(this==other)
            return true;
        if(!(other instanceof Piece))
            return false;
        final Piece otherPiece = (Piece) other;
        return this.piecePosition==otherPiece.piecePosition && this.pieceType==otherPiece.pieceType &&
                this.pieceAlliance==otherPiece.pieceAlliance && this.isFirstMove==otherPiece.isFirstMove();
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    protected int computeHashCode() {
        int result = this.pieceType.hashCode();
        result = 31*result + this.pieceAlliance.hashCode();
        result = 31*result + this.piecePosition;
        result = 31*result + (this.isFirstMove ? 1:0);
        return result;
    }



    // This method returns true for the first move of pieces
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    // This method returns the information of piece alliance
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    // This method returns the position of the piece on the board, the coordinate
    public int getPiecePosition() { return this.piecePosition; }

    // Each different piece has their own legal moves to be calculated by this method overriding it
    // Legal moves for each piece depend only on the boardstate
    public abstract Collection<Move> calculateLegalMoves (final Board board);

    public abstract Piece movePiece(Move move);

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }


    public enum PieceType {

        PAWN("P", 100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString () {
            return this.pieceName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
