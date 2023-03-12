package bca.midyearproj.Skills;

public class Spell extends Skill {

    private int heal;

    public Spell(String skillName, SpaceFinder algorithm, String skillDesc, int heal) {
        super(skillName, algorithm, skillDesc);
        this.heal = heal;
    }

    @Override
    public int getVal() {
        return heal;
    }
    
}
