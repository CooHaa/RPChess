package bca.midyearproj.Pieces;

import java.util.ArrayList;
import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.Skill;
import bca.midyearproj.Skills.SpaceFinder;

/**
 * The knight piece, defined as being able to move two spaces in any horizontal or vertical direction, and one space in the perpendicular direction.
 * Can move over pieces. 
 */
public class Knight extends Piece {

    public Knight(boolean light) {
        super(light, "Knight", 40);

        Skill bowShot = new Attack("Bow Shot", bowShotSpaceFinder, 
        "A shot from a bow and arrow that can deal 10 damage to faraway enemies.", 10, false);
        Skill arrowRain = new Attack("Arrow Rain", arrowRainSpaceFinder, 
        "A barrage of arrows that hits all enemies in a ring for 15 damage.", 15, true);

        skillList.add(bowShot);
        skillList.add(arrowRain);
    }

    @Override
    public String getDesc() {
        return "A valiant sharpshooter on horseback. Trained in the art of the bow and arrow, they never miss their shots even in the fray of battle. Be careful, however - they struggle to protect themselves at close range.";
    }

    @Override
    public List<Square> getPossibleMoves(Square[][] boardState) {
        possibleMoves.clear();
        attackMoves.clear();
        Square currentPos = (Square) this.getParent();
        possibleMoves.add(currentPos);
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getCol();

        // Checking upper moves
        if (currentRow - 2 >= 0) {
            // Checking left attack
            if (currentCol - 1 >= 0) {
                Square leftMove = boardState[currentRow - 2][currentCol - 1];
                if (leftMove.hasPiece()) {
                    if (leftMove.getPiece().isLight() != this.isLight()) attackMoves.add(leftMove);
                }
                else possibleMoves.add(leftMove);
            }

            // Checking right attack
            if (currentCol + 1 < 8) {
                Square rightMove = boardState[currentRow - 2][currentCol + 1];
                if (rightMove.hasPiece()) {
                    if (rightMove.getPiece().isLight() != this.isLight()) attackMoves.add(rightMove);
                }
                else possibleMoves.add(rightMove);
            }
        }

        // Checking lower moves
        if (currentRow + 2 < 8) {
            // Checking left attack
            if (currentCol - 1 >= 0) {
                Square leftMove = boardState[currentRow + 2][currentCol - 1];
                if (leftMove.hasPiece()) {
                    if (leftMove.getPiece().isLight() != this.isLight()) attackMoves.add(leftMove);
                }
                else possibleMoves.add(leftMove);
            }

            // Checking right attack
            if (currentCol + 1 < 8) {
                Square rightMove = boardState[currentRow + 2][currentCol + 1];
                if (rightMove.hasPiece()) {
                    if (rightMove.getPiece().isLight() != this.isLight()) attackMoves.add(rightMove);
                }
                else possibleMoves.add(rightMove);
            }
        }

        // Checking left moves
        if (currentCol - 2 >= 0) {
            // Checking left attack
            if (currentRow - 1 >= 0) {
                Square upMove = boardState[currentRow - 1][currentCol - 2];
                if (upMove.hasPiece()) {
                    if (upMove.getPiece().isLight() != this.isLight()) attackMoves.add(upMove);
                }
                else possibleMoves.add(upMove);
            }

            // Checking right attack
            if (currentRow + 1 < 8) {
                Square downMove = boardState[currentRow + 1][currentCol - 2];
                if (downMove.hasPiece()) {
                    if (downMove.getPiece().isLight() != this.isLight()) attackMoves.add(downMove);
                }
                else possibleMoves.add(downMove);
            }
        }

        // Checking right moves
        if (currentCol + 2 < 8) {
            // Checking left attack
            if (currentRow - 1 >= 0) {
                Square upMove = boardState[currentRow - 1][currentCol + 2];
                if (upMove.hasPiece()) {
                    if (upMove.getPiece().isLight() != this.isLight()) attackMoves.add(upMove);
                }
                else possibleMoves.add(upMove);
            }

            // Checking right attack
            if (currentRow + 1 < 8) {
                Square downMove = boardState[currentRow + 1][currentCol + 2];
                if (downMove.hasPiece()) {
                    if (downMove.getPiece().isLight() != this.isLight()) attackMoves.add(downMove);
                }
                else possibleMoves.add(downMove);
            }
        }

        return possibleMoves;
        
    }

    SpaceFinder bowShotSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = -3; dr <= 3; dr++) {
                for (int dc = -3; dc <= 3; dc++) {
                    if (((row + dr < 8) && (row + dr >= 0) && (col + dc < 8) && (col + dc >= 0)) && !adjacentSpaceFinder.getViableSpaces(square, boardState).contains(boardState[row + dr][col + dc])) {
                        spaces.add(boardState[row + dr][col + dc]);
                    }
                }
            }
            return spaces;
        }
        
    };

    SpaceFinder arrowRainSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = -2; dr <= 2; dr++) {
                for (int dc = -2; dc <= 2; dc++) {
                    if (((row + dr < 8) && (row + dr >= 0) && (col + dc < 8) && (col + dc >= 0)) && !adjacentSpaceFinder.getViableSpaces(square, boardState).contains(boardState[row + dr][col + dc]) && !((Math.abs(dr) == 2) && (Math.abs(dc) == 2))) {
                        spaces.add(boardState[row + dr][col + dc]);
                    }
                }
            }
            return spaces;
        }
        
    };
    
}
