package bca.midyearproj.Pieces;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Skill;
import bca.midyearproj.Skills.SpaceFinder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Abstract parent class representing a piece on the board.
 * <p>
 * All child piece classes inherit the constructor of this class, but each piece must define its own moves.
 */
public abstract class Piece extends ImageView {

    protected boolean light;
    protected String piece;
    protected int maxHP;
    protected int currentHP;
    protected List<Square> possibleMoves;
    protected List<Square> attackMoves;
    protected List<Skill> skillList;

    public Piece(boolean light, String piece, int maxHP) {
        this.light = light;
        this.piece = piece;
        this.maxHP = maxHP;
        currentHP = maxHP;
        possibleMoves = new ArrayList<>();
        attackMoves = new ArrayList<>();
        skillList = new ArrayList<>();
        if (light) setImage(new Image(new File("src/main/resources/images/Light" + piece + ".png").toURI().toString()));
        else setImage(new Image(new File("src/main/resources/images/Dark" + piece + ".png").toURI().toString()));
        setFitWidth(50);
        setPreserveRatio(true);
        setSmooth(true);
    }

    public boolean isLight() {
        return light;
    }

    // Description varies based on piece
    public abstract String getDesc();

    // Depends on the piece
    public abstract List<Square> getPossibleMoves(Square[][] boardState);

    // The search for ambush spaces for most pieces is connected to the search for move spaces, but this is overriden only for the pawn which has a different attack pattern
    public List<Square> getAmbushMoves(Square[][] boardState) {
        return attackMoves;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public String toString() {
        String color = isLight() ? "White" : "Black";
        return color + " " + piece;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public String displayHP() {
        return "HP: " + currentHP + "/" + maxHP;
    }

    public Square getSpace() {
        return (Square) getParent();
    }

    public void takeDamage(int dmg) {
        currentHP -= dmg;
    }

    public void heal(int heal) {
        if (currentHP + heal <= maxHP) currentHP += heal;
        else currentHP = maxHP;
    }


    // Defining common space finding algorithms

    /*
     * Finds the eight spaces around the piece
     */
    public static SpaceFinder adjacentSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (((row + dr < 8) && (row + dr >= 0) && (col + dc < 8) && (col + dc >= 0)) && !((dr == 0) && (dc == 0))) {
                        spaces.add(boardState[row + dr][col + dc]);
                    }
                }
            }
            return spaces;

        }

    };

    /*
     * Finds spaces in the row, stops at first found piece (inclusive)
     */
    public static SpaceFinder rowSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();

            // Left check
            for (int dc = col - 1; dc >= 0; dc--) {
                Square leftMove = boardState[row][dc];
                if (leftMove.hasPiece()) {
                    spaces.add(leftMove);
                    break;
                }
                spaces.add(leftMove);
            }

            // Right check
            for (int dc = col + 1; dc < 8; dc++) {
                Square rightMove = boardState[row][dc];
                if (rightMove.hasPiece()) {
                    spaces.add(rightMove);
                    break;
                }
                spaces.add(rightMove);
            }

            return spaces;
        }

    };

    /*
     * Finds spaces in the column, stops at first found piece (inclusive)
     */
    public static SpaceFinder colSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();

            // Up check
            for (int dr = row - 1; dr >= 0; dr--) {
                Square upMove = boardState[dr][col];
                if (upMove.hasPiece()) {
                    spaces.add(upMove);
                    break;
                }
                spaces.add(upMove);
            }

            // Down check
            for (int dr = row + 1; dr < 8; dr++) {
                Square downMove = boardState[dr][col];
                if (downMove.hasPiece()) {
                    spaces.add(downMove);
                    break;
                }
                spaces.add(downMove);
            }

            return spaces;
        }

    };

    /*
     * Combines the row and column space finders
     */
    public static SpaceFinder rowColSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            return combineSpaceFinders(rowSpaceFinder, colSpaceFinder).getViableSpaces(square, boardState);
        }

    };

    /*
     * Finds spaces in the row, does not stop at any piece
     */
    public static SpaceFinder rowPierceSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dc = 0; dc < 8; dc++) {
                if (dc != col) spaces.add(boardState[row][dc]);
            }
            return spaces;
        }

    };

    /*
     * Finds spaces in the column, does not stop at any piece
     */
    public static SpaceFinder colPierceSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = 0; dr < 8; dr++) {
                if (dr != row) spaces.add(boardState[dr][col]);
            }
            return spaces;
        }

    };

    /*
     * Combines the piercing row and column space finders
     */
    public static SpaceFinder rowColPierceSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            return combineSpaceFinders(rowPierceSpaceFinder, colPierceSpaceFinder).getViableSpaces(square, boardState);
        }
        
    };

    /*
     * Finds spaces in the diagonal going down-right from the upper left corner
     */
    public static SpaceFinder drDiagonalSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int d = 1; (row - d >= 0) && (col - d >= 0); d++) {
                Square space = boardState[row - d][col - d];
                if (space.hasPiece()) {
                    spaces.add(space);
                    break;
                }
                spaces.add(space);
            }
            for (int d = 1; (row + d < 8) && (col + d < 8); d++) {
                Square space = boardState[row + d][col + d];
                if (space.hasPiece()) {
                    spaces.add(space);
                    break;
                }
                spaces.add(space);
            }
            return spaces;
        }
        
    };

    /*
     * Finds spaces in the diagonal going up-right from the bottom left corner
     */
    public static SpaceFinder urDiagonalSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int d = 1; (row - d >= 0) && (col + d < 8); d++) {
                Square space = boardState[row - d][col + d];
                if (space.hasPiece()) {
                    spaces.add(space);
                    break;
                }
                spaces.add(space);
            }
            for (int d = 1; (row + d < 8) && (col - d >= 0); d++) {
                Square space = boardState[row + d][col - d];
                if (space.hasPiece()) {
                    spaces.add(space);
                    break;
                }
                spaces.add(space);
            }
            return spaces;
        }
        
    };

    /*
     * Finds spaces across both diagonals, stopping at found piece
     */
    public static SpaceFinder diagonalSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            return combineSpaceFinders(drDiagonalSpaceFinder, urDiagonalSpaceFinder).getViableSpaces(square, boardState);
        }
        
    };

    /*
     * Finds all ally pieces
     */
    public static SpaceFinder allAlliesSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            List<Square> spaces = new ArrayList<>();
            for (Square[] row : boardState) {
                for (Square space : row) {
                    if (space.hasPiece() && (square != space) && (space.getPiece().isLight() == square.getPiece().isLight())) spaces.add(space);
                }
            }
            return spaces;
        }
    };

    /**
     * Helper method to combine the space finding of two different spacefinders
     * @param sf1
     * @param sf2
     * @return
     */
    public static SpaceFinder combineSpaceFinders(SpaceFinder sf1, SpaceFinder sf2) {
        SpaceFinder sf = new SpaceFinder() {

            @Override
            public List<Square> getViableSpaces(Square square, Square[][] boardState) {
                List<Square> spaces1 = sf1.getViableSpaces(square, boardState);
                List<Square> spaces2 = sf2.getViableSpaces(square, boardState);
                spaces1.removeAll(spaces2);
                spaces1.addAll(spaces2);
                return spaces1;
            }
            
        };

        return sf;
    }
    
    
}
