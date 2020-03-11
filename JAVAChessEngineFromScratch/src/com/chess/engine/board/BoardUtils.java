package com.chess.engine.board;

import java.util.*;

public class BoardUtils {

    private static boolean[] initColumn(int columnNumber) {
        // this method highlights as true tiles belonging to the given column
        // this returns an array of booleans covering every tile
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber]=true;
            columnNumber += NUM_TILES_PER_ROW;

        } while (columnNumber<NUM_TILES);

        return column;
    }

    // board constants
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;


    // arrays of booleans of size 64, one for each tile, marking true for given column's tiles
    // used in calculating edge cases for moves
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);

    // as above, this is for rows
    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(1);
    public static final boolean[] THIRD_ROW = initRow(2);
    public static final boolean[] FOURTH_ROW = initRow(3);
    public static final boolean[] FIFTH_ROW = initRow(4);
    public static final boolean[] SIXTH_ROW = initRow(5);
    public static final boolean[] SEVENTH_ROW = initRow(6);
    public static final boolean[] EIGHTH_ROW = initRow(7);

    public static final int START_TILE_INDEX = 0;

    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        }));
    }

    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i<NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static boolean[] initRow(int rowNumber) {
        // this method highlights as true tiles belonging to the given row
        // this returns an array of booleans covering every tile
        final boolean[] row = new boolean[NUM_TILES];
        int tile = rowNumber*8;
        do {
            row[tile]=true;
            tile++;
        } while (tile % NUM_TILES_PER_ROW != 0);

        return row;

    }

    private BoardUtils() {
        // this is a private constructor of a class that is used as a giver of board info, not to be instantiated
        throw new RuntimeException("You can't instantiate me!");
    }

    // test for coordinates in range, used to remove moves falling out of the board
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate>=0 && coordinate<NUM_TILES;
    }

    public int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);

    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
}
