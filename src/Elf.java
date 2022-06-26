public class Elf extends Calliance{

    public Elf(String name) {
        super(name);
        this.AP = Constants.elfAP;
        this.HP = Constants.elfHP;
        this.maxHP = Constants.elfHP;
        this.moveNumber = Constants.elfMaxMove;
        this.specialP = Constants.elfRangedAP;
    }

    /*
    As it performs its special ranged attack at the end, it calls Special() method before Main.PrintBoard() method.
     */
    @Override
    public void Final() {
        Special();
        super.Final();
    }

    /*
    Elfs ranged attack is applied to a 5x5 area, but as the AP of elf and AP of its special is the same,
    this method only handles the rest of the squares outside of 3x3 area.
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

        for (int i = -2; i<3; i++){
            for (int j = -2; j<3; j++) {

                if(i == -2 || i == 2 || j == -2 || j == 2) {
                    if ((Main.board.length > (piecey + i) &&
                            (piecey + i) > -1) &&
                            (Main.board.length > (piecex + j) &&
                                    (piecex + j) > -1)) {
                        if (Main.board[piecey + i][piecex + j] == null) {
                            continue;
                        }
                        else if (!Main.board[piecey + i][piecex + j].faction.equals(this.faction)) {
                            Main.board[piecey + i][piecex + j].HP -= this.specialP;
                            if (Main.board[piecey + i][piecex + j].HP <= 0) {
                                Main.board[piecey + i][piecex + j] = null;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }
}
