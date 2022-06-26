import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {

    public static String output;
    public static int index;
    public static ArrayList<String> initialsList;
    public static ArrayList<String> commandsList;
    public static ArrayList<Pieces> piecesList;
    public static ArrayList<Pieces> piecesonBoard;
    public static String[] command;
    public static Pieces[][] board;


    public static void main(String[] args) {

        initialsList = new ArrayList<>();
        commandsList = new ArrayList<>();
        piecesList = new ArrayList<>();
        piecesonBoard = new ArrayList<>();
        output = args[2];
        index = 0;


        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(output));
            br.close();
        } catch (Exception ex) {
            return;
        }


        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String s;
            while ((s = br.readLine()) != null) {
                initialsList.add(s);
            }
            br.close();

        } catch (Exception ex) {
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            String s;
            while ((s = br.readLine()) != null) {
                commandsList.add(s);
            }
            br.close();

        } catch (Exception ex) {
            return;
        }

        ExecuteInitials();
        PrintBoard();
        ExecuteCommands();
    }

    /*
    Creates the objects and prepares the board.
     */
    public static void ExecuteInitials(){
        for (int i = 0; i < initialsList.size(); i++){
            if (initialsList.get(i).startsWith("BOARD")){
                String[] boardsize = initialsList.get(i+1).split("x");
                board = new Pieces[Integer.parseInt(boardsize[0])][Integer.parseInt(boardsize[0])];
            }
            else if (initialsList.get(i).startsWith("ELF")){
                String[] pieceArray = initialsList.get(i).split(" ");
                piecesList.add(new Elf(pieceArray[1]));
                board[Integer.parseInt(pieceArray[3])][Integer.parseInt(pieceArray[2])] = piecesList.get(index);
                index++;
            }
            else if (initialsList.get(i).startsWith("HUMAN")){
                String[] pieceArray = initialsList.get(i).split(" ");
                piecesList.add(new Human(pieceArray[1]));
                board[Integer.parseInt(pieceArray[3])][Integer.parseInt(pieceArray[2])] = piecesList.get(index);
                index++;
            }
            else if (initialsList.get(i).startsWith("DWARF")){
                String[] pieceArray = initialsList.get(i).split(" ");
                piecesList.add(new Dwarf(pieceArray[1]));
                board[Integer.parseInt(pieceArray[3])][Integer.parseInt(pieceArray[2])] = piecesList.get(index);
                index++;
            }
            else if (initialsList.get(i).startsWith("ORK")){
                String[] pieceArray = initialsList.get(i).split(" ");
                piecesList.add(new Ork(pieceArray[1]));
                board[Integer.parseInt(pieceArray[3])][Integer.parseInt(pieceArray[2])] = piecesList.get(index);
                index++;
            }
            else if (initialsList.get(i).startsWith("TROLL")){
                String[] pieceArray = initialsList.get(i).split(" ");
                piecesList.add(new Troll(pieceArray[1]));
                board[Integer.parseInt(pieceArray[3])][Integer.parseInt(pieceArray[2])] = piecesList.get(index);
                index++;
            }
            else if (initialsList.get(i).startsWith("GOBLIN")){
                String[] pieceArray = initialsList.get(i).split(" ");
                piecesList.add(new Goblin(pieceArray[1]));
                board[Integer.parseInt(pieceArray[3])][Integer.parseInt(pieceArray[2])] = piecesList.get(index);
                index++;
            }
        }
    }

    /*
    Executes the command
     */
    public static void ExecuteCommands(){
        for (String s: commandsList){
            command = s.split(" ");
            for (Pieces pieces: piecesList){
                if (pieces.name.equals(command[0])){
                    try {
                        if (pieces.HP > 0) {
                            pieces.CheckAction(command[1]);
                        }
                    }catch (MoveNumberException ex){
                        try {
                            BufferedWriter br = new BufferedWriter(new FileWriter(output,true));
                            br.write(ex.getMessage() + "\n\n");
                            br.close();
                        } catch (Exception e) {
                            return;
                        }
                    }
                }
            }
        }
    }

    /*
    Prints the board and some expected values, and the result of the match if it ends.
     */
    public static void PrintBoard(){
        String border = "";

        boolean calliance = false;
        boolean zorde = false;

        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(output, true));

            for (int i = 0; i < board.length+1; i++){
                border += "**";
            }

            br.write(border + "\n");
            for (int i = 0; i < board.length; i++){
                br.write("*");
                for (int j = 0; j < board.length; j++){
                    if (board[i][j] == null){
                        br.write("  ");
                    }
                    else {
                        br.write(board[i][j].name);
                    }
                }
                br.write("*\n");
            }
            br.write(border + "\n");

            for (int i = 0; i < board.length; i++){
                for (int j = 0; j < board.length; j++){
                    if (board[i][j] != null){
                        piecesonBoard.add(board[i][j]);
                    }
                }
            }

            piecesonBoard.sort(new Comparator<Pieces>() {
                @Override
                public int compare(Pieces o1, Pieces o2) {
                    return o1.name.compareTo((o2.name));
                }
                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            });

            br.write("\n");
            for (Pieces pieces: piecesonBoard){
                br.write(pieces.name + "\t" + pieces.HP + "\t" + "(" + pieces.maxHP + ")\n");
            }
            br.write("\n");

            for (Pieces pieces: piecesonBoard){
                if (pieces instanceof Calliance){
                    calliance = true;
                    break;
                }
            }

            for (Pieces pieces: piecesonBoard){
                if (pieces instanceof Zorde){
                    zorde = true;
                    break;
                }
            }

            if (!zorde && !calliance){
                br.write("\nGame Finished\nGame is a Draw");
                br.close();
                System.exit(0);
            }
            else if (!zorde && calliance){
                br.write("\nGame Finished\nCalliance Wins");
                br.close();
                System.exit(0);
            }
            else if (zorde && !calliance){
                br.write("\nGame Finished\nZorde Wins");
                br.close();
                System.exit(0);
            }
            piecesonBoard.clear();

            br.close();
        } catch (Exception ex) {
            return;
        }

    }

}
