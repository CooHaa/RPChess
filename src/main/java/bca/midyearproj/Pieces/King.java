package bca.midyearproj.Pieces;

import java.util.List;

import bca.midyearproj.Square;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.LastResort;
import bca.midyearproj.Skills.Skill;

/**
 * The king piece, defined as only being able to move one space in all 8 directions.
 * If the king dies, the player loses.
 */
public class King extends Piece {

    public King(boolean light) {
        super(light, "King", 80);

        Skill deadlyThrone = new Attack("Deadly Throne", adjacentSpaceFinder, 
        "A deadly close-range attack that deals 20 damage to all nearby pieces.", 20, true);
        Skill lastResort = new LastResort("Last Resort", allAlliesSpaceFinder, 
        "A last resort spell that sacrifices a piece for the King to attain its equivalent health.");

        skillList.add(deadlyThrone);
        skillList.add(lastResort);
    }

    @Override
    public String getDesc() {
        return "The king himself - the leader of the battalion. His throne on the battlefield is not just for show, for it is a deadly weapon in it of itself. I don't think I need to tell you this, but...don't let him fall.";
    }

    @Override
    public List<Square> getPossibleMoves(Square[][] boardState) {
        possibleMoves.clear();
        attackMoves.clear();
        Square currentPos = (Square) this.getParent();
        possibleMoves.add(currentPos);
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getCol();

        // Checking 8 tiles around piece
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                // Only allow if the move is within the board
                if ((currentRow + dRow < 8) && (currentRow + dRow >= 0) && (currentCol + dCol < 8) && (currentCol + dCol >= 0)) {
                    Square moveSpace = boardState[currentRow + dRow][currentCol + dCol];
                    if (moveSpace.hasPiece()) {
                        if (moveSpace.getPiece().isLight() != this.isLight()) attackMoves.add(moveSpace);
                    }
                    else possibleMoves.add(moveSpace);
                }
            }
        }

        return possibleMoves;
    }

    
}