package bca.midyearproj.Pieces;

import java.util.ArrayList;
import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.Skill;
import bca.midyearproj.Skills.SpaceFinder;

/**
 * The rook piece, defined as being able to move infinitely in the horizontal and vertical directions.
 */
public class Rook extends Piece {

    public Rook(boolean light) {
        super(light, "Rook", 30);

        Skill smokeBomb = new Attack("Smoke Bomb", smokeBombSpaceFinder, 
        "A hand-crafted smoke bomb that deals 5 damage to all pieces in a large radius.", 5, true);
        Skill throwKnife = new Attack("Throwing Knife", Piece.combineSpaceFinders(rowColSpaceFinder, adjacentSpaceFinder), 
        "A concealed blade that can hit both close and far away for 10 damage.", 10, false);

        skillList.add(smokeBomb);
        skillList.add(throwKnife);
    }

    @Override
    public String getDesc() {
        return "A sly rogue that works under the covers. Their tactics may be cheap and underhanded, but one can't deny their effectiveness. Keep their fragility in mind as you sneak past defenses!";
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

        return possibleMoves;
    }

    SpaceFinder smokeBombSpaceFinder = new SpaceFinder() {

        @Override
        public List<Square> getViableSpaces(Square square, Square[][] boardState) {
            ArrayList<Square> spaces = new ArrayList<>();
            int row = square.getRow();
            int col = square.getCol();
            for (int dr = -2; dr <= 2; dr++) {
                for (int dc = -2; dc <= 2; dc++) {
                    if (((row + dr < 8) && (row + dr >= 0) && (col + dc < 8) && (col + dc >= 0)) && !((dr == 0) && (dc == 0))) {
                        spaces.add(boardState[row + dr][col + dc]);
                    }
                }
            }
            return spaces;
        }
        
    };

}
