public class Ork extends Zorde{

    public Ork(String name) {
        super(name);
        this.AP = Constants.orkAP;
        this.HP = Constants.orkHP;
        this.maxHP = Constants.orkHP;
        this.moveNumber = Constants.orkMaxMove;
        this.specialP = Constants.orkHealPoints;
    }

    /*
    If walking operation is a valid move normally this method gets called.

    In addition the default method, it firsts calls Special() method then performs everything as default.
     */
    @Override
    public void WalkValid(int x, int y, String[] step) throws FriendlyPieceException, FighttoDeathException {
        Special();
        super.WalkValid(x, y, step);
    }

    /*
    As it only attacks after final step it overrides this method and removes the call of Attack(int x, int y, String[] step)
    method from it.
    */
    @Override
    public void Walk(int x, int y, String[] step) {
        Main.board[y][x] = null;
        Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])] = this;
    }

    /*
    Instead it calls the Attack(int x, int y, String[] step) method here, before Main.PrintBoard() method.
     */
    @Override
    public void Final() {
        int piecex = -1;
        int piecey = -1;
        String[] step = new String[]{"0", "0"};

        for (int i = 0; i < Main.board.length; i++){
            for (int j = 0; j < Main.board.length; j++){
                if (Main.board[i][j] == this){
                    piecey = i;
                    piecex = j;
                }
            }
        }
        if(piecex != -1 && piecey !=-1) {
            Attack(piecex, piecey, step);
        }
        super.Final();
    }

    /*
    Performs the special power of ork (healing teammates) operation.
     */
    @Override
    public void Special() {

        int piecex = 0;
        int piecey = 0;

        for (int i = 0; i < Main.board.length; i++){
            for (int j = 0; j < Main.board.length; j++){
                if (Main.board[i][j] == this){
                    piecey = i;
                    piecex = j;
                }
            }
        }

        for (int i = -1; i<2; i++){
            for (int j = -1; j<2; j++) {
                if((Main.board.length > (piecey + i) &&
                        (piecey + i) > -1) &&
                        (Main.board.length > (piecex + j) &&
                        (piecex + j) > -1)) {
                    if (Main.board[piecey + i][piecex + j] == null) {
                        continue;
                    } else if (Main.board[piecey + i][piecex + j].faction.equals(this.faction)) {


                        Main.board[piecey + i][piecex + j].HP += this.specialP;
                        if (Main.board[piecey + i][piecex + j].HP > Main.board[piecey + i][piecex + j].maxHP) {
                            Main.board[piecey + i][piecex + j].HP = Main.board[piecey + i][piecex + j].maxHP;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
