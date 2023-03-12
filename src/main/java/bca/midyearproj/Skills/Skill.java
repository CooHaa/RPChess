package bca.midyearproj.Skills;

import java.util.List;

import bca.midyearproj.Square;

public abstract class Skill {
    
    protected String skillName;
    protected String skillDesc;
    protected SpaceFinder algorithm;

    public Skill(String skillName, SpaceFinder algorithm, String skillDesc) {
        this.skillName = skillName;
        this.algorithm = algorithm;
        this.skillDesc = skillDesc;
    }

    public String toString() {
        return skillName;
    }

    public String getDesc() {
        return skillDesc;
    }

    public List<Square> runAlgorithm(Square square, Square[][] boardState) {
        return algorithm.getViableSpaces(square, boardState);
    }

    public abstract int getVal();

}
