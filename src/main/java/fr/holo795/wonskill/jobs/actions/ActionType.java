package fr.holo795.wonskill.jobs.actions;

public enum ActionType {

    BREAK(0),
    PLACE(1),
    INTERACT(2),
    WALK(3),
    CRAFT(4),
    SMELT(5),
    ENCHANT(6),
    FISH(7),
    KILL(8),
    TAME(9),
    HARVEST(10),
    EAT(11),
    SHEAR(12);
    public final int id;

    ActionType(int id) {
        this.id = id;
    }

}
