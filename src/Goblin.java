public class Goblin extends Zorde{

    public Goblin(String name) {
        super(name);
        this.AP = Constants.goblinAP;
        this.HP = Constants.goblinHP;
        this.maxHP = Constants.goblinHP;
        this.moveNumber = Constants.goblinMaxMove;
    }
}
