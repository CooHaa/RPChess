package bca.midyearproj.Skills;

public class Attack extends Skill {

    private int damage;
    private boolean aoe;

    public Attack(String skillName, SpaceFinder algorithm, String skillDesc, int damage, boolean aoe) {
        super(skillName, algorithm, skillDesc);
        this.damage = damage;
        this.aoe = aoe;
    }

    @Override
    public int getVal() {
        return damage;
    }

    public boolean aoe() {
        return aoe;
    }
    
}
