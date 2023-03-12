package bca.midyearproj.Pieces;

import java.util.ArrayList;
import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.Skill;
import bca.midyearproj.Skills.SpaceFinder;
import bca.midyearproj.Skills.Spell;

/**
 * The queen piece, defined as being able to move in any of the eight directions infinitely.
 */
public class Queen extends Piece {

    public Queen(boolean light) {
        super(light, "Queen", 50);

        Skill royalFan = new Attack("Royal Fan", royalFanSpaceFinder, 
        "A wave of the Queen's royal fan, dealing 10 damage to all enemies nearby.", 10, true);
        Skill queensGrace = new Spell("Queen's Grace", queensGracesSpaceFinder, 
        "Her Majesty's grace, which heals a nearby piece up to max health.", 100);

        skillList.add(royalFan);
        skillList.add(queensGrace);
    }

    @Override
    public String getDesc() {
        return "The revered queen of the kingdom. Despite her status, she does not fear the battle, and utilizes her deadly fan to attack in swaths. Use her blessing well, for she only has one.";
    }

    @Override
    public List<Square> getPossibleMoves(Square[][] boardState) {
        possibleMoves.clear();
        attackMoves.clear();
        Square currentPos = (Square) this.getParent();
        possibleMoves.add(currentPos);
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getCol();

        // Checking left
        for (int col = currentCol - 1; col >= 0; col--) {
            Square leftMove = boardState[currentRow][col];
            if (leftMove.hasPiece()) {
                if (leftMove.getPiece().isLight() != this.isLight()) attackMoves.add(leftMove);
                break;
            }
            possibleMoves.add(leftMove);
        }
        // Checking right
        for (int col = currentCol + 1; col < 8; col++) {
            Square rightMove = boardState[currentRow][col];
            if (rightMove.hasPiece()) {
                if (rightMove.getPiece().isLight() != this.isLight()) attackMoves.add(rightMove);
                break;
            }
            possibleMoves.add(rightMove);
        }

        // Checking up
        for (int row = currentRow - 1; row >= 0; row--) {
            Square upMove = boardState[row][currentCol];
            if (upMove.hasPiece()) {
                if (upMove.getPiece().isLight() != this.isLight()) attackMoves.add(upMove);
                break;
            }
            possibleMoves.add(upMove);
        }
        // Checking down
        for (int row = currentRow + 1; row < 8; row++) {
            Square downMove = boardState[row][currentCol];
            if (downMove.hasPiece()) {
                if (downMove.getPiece().isLight() != this.isLight()) attackMoves.add(downMove);
                break;
            }
            possibleMoves.add(downMove);
        }

        // Checking upper left
        for (int offset = 1; (currentRow - offset >= 0) && (currentCol - offset >= 0); offset++) {
            Square upperLeftMove = boardState[currentRow - offset][currentCol - offset];
            if (upperLeftMove.hasPiece()) {
                if (upperLeftMove.getPiece().isLight() != this.isLight()) attackMoves.add(upperLeftMove);
                break;
            }
            possibleMoves.add(upperLeftMove);
        }

        // Checking upper right
        for (int offset = 1; (currentRow - offset >= 0) && (currentCol + offset < 8); offset++) {
            Square upperRightMove = boardState[currentRow - offset][currentCol + offset];
            if (upperRightMove.hasPiece()) {
                if (upperRightMove.getPiece().isLight() != this.isLight()) attackMoves.add(upperRightMove);
                break;
            }
            possibleMoves.add(upperRightMove);
        }

        // Checking lower left
        for (int offset = 1; (currentRow + offset < 8) && (currentCol - offset >= 0); offset++) {
            Square lowerLeftMove = boardState[currentRow + offset][currentCol - offset];
            if (lowerLeftMove.hasPiece()) {
                if (lowerLeftMove.getPiece().isLight() != this.isLight()) attackMoves.add(lowerLeftMove);
                break;
            }
            possibleMoves.add(lowerLeftMove);
        }

        // Checking lower right
        for (int offset = 1; (currentRow + offset < 8) && (currentCol + offset < 8); offset++) {
            Square lowerLeftMove = boardState[currentRow + offset][currentCol + offset];
            if (lowerLeftMove.hasPiece()) {
                if (lowerLeftMove.getPiece().isLight() != this.isLight()) attackMoves.add(lowerLeftMove);
                break;
            }
            possibleMoves.add(lowerLeftMove);
        }

        return possibleMoves;
        
    }

    SpaceFinder royalFanSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = 1; dr <= 2; dr++) {
                int colorChange = dr * (isLight() ? -1 : 1);
                for (int dc = -dr; dc <= dr; dc++) {
                    if ((row + colorChange < 8) && (row + colorChange >= 0) && (col + dc < 8) && (col + dc >= 0)) {
                        spaces.add(boardState[row + colorChange][col + dc]);
                    }
                } 
            }
            return spaces;
        }
        
    };

    SpaceFinder queensGracesSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = -2; dr <= 2; dr++) {
                for (int dc = -2; dc <= 2; dc++) {
                    if (((row + dr < 8) && (row + dr >= 0) && (col + dc < 8) && (col + dc >= 0)) && !((dr == 0) && (dc == 0))) {
                        Square space = boardState[row + dr][col + dc];
                        if (space.hasPiece() && (space.getPiece().isLight() == isLight()) && (space.getPiece().getMaxHP() != space.getPiece().getCurrentHP())) {
                            spaces.add(space);
                        }
                    }
                }
            }
            return spaces;
        }
        
    };
    
}
