package bca.midyearproj.Pieces;

import java.util.ArrayList;
import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.Skill;
import bca.midyearproj.Skills.SpaceFinder;
import bca.midyearproj.Skills.Spell;

/**
 * The bishop piece, defined as being able to move infinitely diagonally.
 */
public class Bishop extends Piece {

    public Bishop(boolean light) {
        super(light, "Bishop", 40);

        Skill magicMissile = new Attack("Magic Missile", Piece.combineSpaceFinders(diagonalSpaceFinder, adjacentSpaceFinder), 
        "A blast of arcane energy that can hit close and far enemies for 15.", 15, false);
        Skill healingLight = new Spell("Healing Light", healingLightSpaceFinder, 
        "A ray of heavenly light from the heavens to bless any ally on the field for 15 health.", 15);

        skillList.add(magicMissile);
        skillList.add(healingLight);
    }

    @Override
    public String getDesc() {
        return "A divine priest for the kingdom. Blessed by the gods of chess, they use their spells to keep allies in good condition. Your protection of them is the key to victory.";
    }

    @Override
    public List<Square> getPossibleMoves(Square[][] boardState) {
        possibleMoves.clear();
        attackMoves.clear();
        Square currentPos = (Square) this.getParent();
        possibleMoves.add(currentPos);
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getCol();

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

    SpaceFinder healingLightSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            List<Square> allies = Piece.allAlliesSpaceFinder.getViableSpaces(square, boardState);
            List<Square> spaces = new ArrayList<>();
            for (Square space : allies) {
                Piece ally = space.getPiece();
                if (ally.getCurrentHP() != ally.getMaxHP()) spaces.add(ally.getSpace());
            }
            return spaces;
        }
        
    };
    
}
