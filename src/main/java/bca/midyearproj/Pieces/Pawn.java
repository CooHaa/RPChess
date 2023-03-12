package bca.midyearproj.Pieces;

import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.Skill;

/**
 * The pawn piece, defined as being able to move one or two spaces forward from their original position, and only one space forward
 * on any other turn.
 */
public class Pawn extends Piece {

    // Tracks if the piece has been moved, used to restrict moving two spaces after first move
    private boolean hasMoved = false;

    public Pawn(boolean light) {
        super(light, "Pawn", 15);

        Skill shoulderBash = new Attack("Shoulder Bash", adjacentSpaceFinder, 
        "A basic melee attack that can hit any piece directly next to the pawn for 5 damage.", 5, false);

        skillList.add(shoulderBash);
    }

    public String getDesc() {
        return "A brave warrior whose spirit doesn't quite match their strength. They try their best though!";
    }

    @Override
    public List<Square> getPossibleMoves(Square[][] boardState) {
        possibleMoves.clear();
        Square currentPos = (Square) this.getParent();
        possibleMoves.add(currentPos);
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getCol();

        // Adds extra space if the piece hasn't moved
        int extraSpace = hasMoved ? 0 : 1;
        
        for (int row = 1; row <= (1 + extraSpace); row++) {

            // Accounts for diff. direction of movement for diff. colors
            int colorDiff = this.isLight() ? (row * -1) : row;
            // Only allow move if its within the chessboard
            if ((currentRow + colorDiff) >= 0 && (currentRow + colorDiff) < 8) {
                Square moveSpace = boardState[currentRow + colorDiff][currentCol];
                if (!moveSpace.hasPiece() || row == 0) possibleMoves.add(moveSpace);
            }
        }

        return possibleMoves;
    }

    @Override
    public List<Square> getAmbushMoves(Square[][] boardState) {
        attackMoves.clear();
        Square currentPos = (Square) this.getParent();
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getCol();

        // Accounts for diff. direction of attack for diff. colors
        int colorDiff = this.isLight() ? -1 : 1;

        // Check if row is in bounds
        if ((currentRow + colorDiff) >= 0 && (currentRow + colorDiff) < 8) {
            // Check if left attack is in bounds
            if ((currentCol - 1) >= 0) {
                Square leftAttack = boardState[currentRow + colorDiff][currentCol - 1];
                // Only an attack space if there's a piece of the other color
                if (leftAttack.hasPiece() && (leftAttack.getPiece().isLight() != this.isLight())) attackMoves.add(leftAttack);
            }
            // Check if right attack is in bounds
            if ((currentCol + 1) < 8) {
                Square rightAttack = boardState[currentRow + colorDiff][currentCol + 1];
                // Only an attack space if there's a piece of the other color
                if (rightAttack.hasPiece() && (rightAttack.getPiece().isLight() != this.isLight())) attackMoves.add(rightAttack);
            }
        }

        return attackMoves;
    }
    
    public void firstMove() {
        hasMoved = true;
    }
    
}
