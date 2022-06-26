public class Troll extends Zorde{

    public Troll(String name) {
        super(name);
        this.AP = Constants.trollAP;
        this.HP = Constants.trollHP;
        this.maxHP = Constants.trollHP;
        this.moveNumber = Constants.trollMaxMove;
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
}
