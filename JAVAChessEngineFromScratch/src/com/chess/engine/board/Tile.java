package com.chess.engine.board; // The Tile class is going to the board subpackage

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {
    // Represents 1 tile unit, the board has 64 of them
    // This class should be extended to become an empty tile or an occupied tile
    // It's better to use polymorphism than to fill the class with conditionals
    // This class is immutable (if can be, it's good to be), so an object of this class can't be changed once created

    // This is a property of the Tile set at instantiation by the constructor
    // It's protected, so can only be accessed from subclasses
    // It's final, so can't be modified after initialization by the constructor
    // These are because Tile is to be immutable
    protected final int tileCoordinate;


    // This variable is a map containing a board of empty tiles, it's the empty board
    // Private instead of protected means it can be accessed from other classes of the package
    // Declare as Map instead of HashMap adds flexibility of changing types, but they create the same objects
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    // This method, used above, returns a map containing empty tiles and their coordinates
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i=0; i<BoardUtils.NUM_TILES; i++) // BoardUtils is a class of utilities as the NUM_TILES = 64
            emptyTileMap.put(i, new EmptyTile(i)); // coordinates range from 0 to 63, from a8 to h1

        // here we use an external library, guava from google, to make the created map immutable
        // this is not necessary, it's a good practice to ensure perfect immutability of Tile objects
        // collections.unmodifiableMap() is almost the same and can be used here too (and it's internal)
        return ImmutableMap.copyOf(emptyTileMap);
    }

    // this method is the only one that can actually be used, it returns a new tile
    // if we pass a piece, the created tile is occupied. If not, it is taken from the empty board map
    // (?) the hashmap of empty pieces is understendable, we want effective access to addressed tiles (cache)
        // but why not make the occupied like this too? would it be undesirable to create all possible occupations?
    public static Tile createTile (final int tileCoordinate, final Piece piece) {
        return piece != null? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    Tile (final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;


    }

    // Abstract methods to be filled by the extensions
    public abstract boolean isTileOccupied(); // method that determines occupation
    public abstract Piece getPiece(); // returns the piece if occupied, null if not

    public int getTileCoordinate() {
        return this.tileCoordinate;
    }


    // Below are the two subclasses extending tile
    // They are final because they are designed to not be extended
    // The subclasses are static, so they can be instantiated through the outer class, Tile, as Tile.subclass
    // The other option is to put the subclasses in different files as non-nested, non-static extensions


    // the empty subclass
    public static final class EmptyTile extends Tile {

        EmptyTile(final int coordinate) {
            super(coordinate); // call to Tile constructor passing its coordinate
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    // the occupied subclass
    public static final class OccupiedTile extends Tile {

        private final Piece pieceOnTile; // This should be private final for Tile to be immutable

        OccupiedTile (final int coordinate, final Piece pieceOnTile) {
            super(coordinate); // call to Tile constructor passing its coordinate
            this.pieceOnTile = pieceOnTile; // saves the occupying piece to OccupiedTile
        }

        @Override // black pieces are made lowercase
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString();
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile; // returns the piece saved on the tile
                                     // this format prevents external access to the modifier method pieceOnTile
        }
    }

}
