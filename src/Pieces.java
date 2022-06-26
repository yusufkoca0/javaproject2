import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Pieces {

    public String name;
    public String faction;
    public int HP;
    public int maxHP;
    public int AP;
    public int moveNumber;
    public int specialP;
    public String[] moves;
    public ArrayList<String[]> steps = new ArrayList<>();

    public Pieces(String name) {
        this.name = name;
    }

    /*
    Checks if the command has correct amount of steps. If it does not it prints the error message.
    If it does, It calls the Action() method.
     */
    public void CheckAction(String s) throws MoveNumberException{
        moves = s.split(";");
        if (moves.length != 2*this.moveNumber){
            throw new MoveNumberException("Error : Move sequence contains wrong number of move steps. Input line ignored.");
        }
        else {
            for (int i = 0; i < moves.length; i=i+2){
                String[] stepxy = new String[]{moves[i], moves[i+1]};
                steps.add(stepxy);
            }
        }
        Action();
    }

    /*
    Tries CheckWalk(String[] step) method and handles the 3 exceptions.
    If OutofBoardException is thrown before the first move happens, it only prints the error message.
    if it is thrown after the first move it firsts prints the board then prints the error message.
    It stops the execution of the rest of the command line in both cases.

    If other 2 exceptions are thrown it just stops the execution of the rest of the command line.

    If there is no exceptions it calls Final() method.

    After any of the stated 4 cases it clears the "steps" arraylist for future commands.
     */
    public void Action(){
        for (int i = 0; i < steps.size(); i++){

            try {
                CheckWalk(steps.get(i));
            }
            catch (OutofBoardException ex){
                try {

                    if (i != 0){
                        Main.PrintBoard();
                    }

                    BufferedWriter br = new BufferedWriter(new FileWriter(Main.output,true));
                    br.write(ex.getMessage() + "\n\n");
                    br.close();

                    this.steps.clear();
                    return;

                } catch (Exception e) {
                    return;
                }
            } catch (FriendlyPieceException ex2){
                Main.PrintBoard();
                this.steps.clear();
                return;
            }catch (FighttoDeathException ex3){
                Main.PrintBoard();
                this.steps.clear();
                return;
            }
        }
        Final();
        this.steps.clear();
    }

    /*
    Checks if a piece trying to move out of the board, if so it throws OutofBoardException

    If it doesn't, it calls WalkValid(int x, int y, String[] step) function and handles the 2 exceptions from there.

    It basically catches the exception and throws it to the Action() method.
     */
    public void CheckWalk(String[] step) throws OutofBoardException, FriendlyPieceException, FighttoDeathException{

        int piecex = -1;
        int piecey = -1;

        for (int i = 0; i < Main.board.length; i++){
            for (int j = 0; j < Main.board.length; j++){
                if (Main.board[i][j] == this){
                    piecey = i;
                    piecex = j;
                }
            }
        }

        if ((piecey + Integer.parseInt(step[1])) >= Main.board.length || (piecex + Integer.parseInt(step[0])) >= Main.board.length){
            throw new OutofBoardException("Error : Game board boundaries are exceeded. Input line ignored.");
        }

        else if ((piecey + Integer.parseInt(step[1])) <= -1 || (piecex + Integer.parseInt(step[0])) <= -1){
            throw new OutofBoardException("Error : Game board boundaries are exceeded. Input line ignored.");
        }

        else {
            try{
                WalkValid(piecex, piecey, step);
            }catch (FriendlyPieceException ex){
                throw new FriendlyPieceException();
            }catch (FighttoDeathException ex2){
                throw new FighttoDeathException();
            }

        }
    }

    /*
    Checks if it moves a square occupied by another piece or not.

    If it does not it calls Walk(int x, int y, String[] step) function.

    If it moves to friendly piece it throws FriendlyPieceException.

    If it moves to enemy piece it first calls FighttoDeath(int x, int y, String[] step) method,
    then catches FighttoDeathException and theows it to the CheckWalk(String[] step) method.
     */
    public void WalkValid(int x, int y, String[] step) throws FriendlyPieceException, FighttoDeathException{

        if (Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])] == null){
            Walk(x, y, step);
        }
        else if (Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].faction.equals(this.faction) &&
                 Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])] != this){
            throw new FriendlyPieceException();
        }
        else if (!Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].faction.equals(this.faction)){
            try {
                FighttoDeath(x, y, step);
            }catch (FighttoDeathException ex){
                throw new FighttoDeathException();
            }
        }
    }

    /*
    Performs the Walk operation then it calls Attack(int x, int y, String[] step) method.
     */
    public void Walk(int x, int y, String[] step){

        Main.board[y][x] = null;
        Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])] = this;
        Attack(x, y, step);
    }

    /*
    Handles the 3 possible situation of fight to death situation and throws FighttoDeathException afterwards.
     */
    public void FighttoDeath(int x, int y, String[] step) throws FighttoDeathException{

        Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP -= this.AP;
        if (Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP < 0){
            Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP = 0;
        }

        if (this.HP > Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP){
            this.HP -= Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP;
            Main.board[y][x] = null;
            Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP = 0;
            Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])] = this;
            throw new FighttoDeathException();
        }
        else if (this.HP < Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP){
            Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP -= this.HP;
            this.HP = 0;
            Main.board[y][x] = null;
            throw new FighttoDeathException();
        }
        else if(this.HP == Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP) {
            Main.board[y + Integer.parseInt( step[1])][x + Integer.parseInt(step[0])].HP = 0;
            this.HP = 0;
            Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])].HP = 0;
            Main.board[y][x] = null;
            Main.board[y + Integer.parseInt(step[1])][x + Integer.parseInt(step[0])] = null;
            throw new FighttoDeathException();
        }
    }

    /*
    Performs the Attack operation
     */
    public void Attack(int x, int y, String[] step){

        for (int i = -1; i<2; i++){
            for (int j = -1; j<2; j++) {

                if ((Main.board.length > (y + Integer.parseInt(step[1]) + i) &&
                     (y + Integer.parseInt(step[1]) + i) > -1) &&
                     (Main.board.length > (x + Integer.parseInt(step[0]) + j) &&
                     (x + Integer.parseInt(step[0]) + j) >-1)) {
                    if (Main.board[y + Integer.parseInt(step[1]) + i][x + Integer.parseInt(step[0]) + j] == null) {
                        continue;
                    } else if (!Main.board[y + Integer.parseInt(step[1]) + i][x + Integer.parseInt(step[0]) + j].faction.equals(this.faction)) {
                        Main.board[y + Integer.parseInt(step[1]) + i][x + Integer.parseInt(step[0]) + j].HP -= this.AP;
                        if (Main.board[y + Integer.parseInt(step[1]) + i][x + Integer.parseInt(step[0]) + j].HP <= 0) {
                            Main.board[y + Integer.parseInt(step[1]) + i][x + Integer.parseInt(step[0]) + j] = null;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    /*
    Empty in default.
     */
    public void Special(){}

    /*
    Calls the Main.PrintBoard() function in default.
     */
    public void Final(){
        Main.PrintBoard();
    }

}
