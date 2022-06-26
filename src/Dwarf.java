public class Dwarf extends Calliance{

    public Dwarf(String name) {
        super(name);
        this.AP = Constants.dwarfAP;
        this.HP = Constants.dwarfHP;
        this.maxHP = Constants.dwarfHP;
        this.moveNumber = Constants.dwarfMaxMove;
    }
}
