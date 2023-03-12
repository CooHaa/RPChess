package bca.midyearproj.Skills;

import java.util.List;

import bca.midyearproj.Square;

public interface SpaceFinder {
    
    public List<Square> getViableSpaces(Square square, Square[][] boardState);

}
