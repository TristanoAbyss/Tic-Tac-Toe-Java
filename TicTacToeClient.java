import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TicTacToeClient {

    private boolean isQuit = false;
    private Connection server;
    private Scanner in;

    public static void main(String[] args) throws IOException {
        String HOST = "localhost";
        int PORT = TicTacToeServer.PORT;
        try (
            Socket socket = new Socket(HOST, PORT)
        ) {
            new TicTacToeClient(socket).startClient();
        }
    }

    public TicTacToeClient(Socket socket) throws IOException {
        this.server = new Connection(socket);
        this.in = new Scanner(System.in);
    }

    /**
     * begins taking inputs from the specified player
     *
     * @throws IOException
     */

    public void startClient() throws IOException {
        while (!isQuit) {
            int command = server.readInt();
            switch (command) {
                case TicTacToeProtocol.TURN:
                    this.handleTurn();
                    break;
                case TicTacToeProtocol.GAMEEND:
                    this.handleGameend();
                    break;
                case TicTacToeProtocol.QUIT:
                    this.handleQuit();
                    break;
            }
        }
    }

    /**
     * Indicates which player's turn it is and accepts inputs for directions
     *
     * @throws IOException
     */


    public void handleTurn() throws IOException {
        System.out.println("\n");
        System.out.print(("turn "+server.readInt())+"\n");
        printBoard(server.readUTF());
        String s = this.in.nextLine();
        if (s.trim().equals("quit")) {
            this.isQuit = true;
            sendQuit();
        } else {
            sendMove(s.split(" "));
        }
    }

    /**
     * Prints the current board
     *
     * @param board the number value string that will be used to print the board
     */

    private void printBoard(String board) {
        GameBoard game = new GameBoard(board);
        System.out.println(" "+board);
        game.printBoard();
    }

    /**
     * Sends the move to the server so that it may be placed and recorded in the board that the
     * server holds
     *
     * @param s the input from the player
     * @throws IOException IO exception
     */


    private void sendMove(String[] s) throws IOException {
        int dir1 = -1;
        int dir2 = -1;
        if (s[0].equals("move")) {
            dir1 = Integer.parseInt(s[1]);
            dir2 = Integer.parseInt(s[2]);
        } if((dir1 <= -1 || dir1 >= 3) || (dir2 <= -1 || dir2 >= 3) || (s.length < 3 || s.length > 3)){
            System.out.println("Input is out of bounds, try again");
            catchFlaw();
        } else {
            this.server.writeInt(TicTacToeProtocol.MOVE);
            this.server.writeInt(dir1);
            this.server.writeInt(dir2);
        }
    }

    /**
     * if an incorrect move is input, catchFlaw will catch the input and force the user to input
     * another move
     *
     * @throws IOException
     */

    private void catchFlaw() throws IOException {
        String s = this.in.nextLine();
        if (s.trim().equals("quit")) {
            this.isQuit = true;
            sendQuit();
        } else {
            sendMove(s.split(" "));
        }
    }

    /**
     * If either player wants to quit the game, entering quit will disconnect both players
     *
     * @throws IOException
     */

    private void sendQuit() throws IOException {
        this.server.writeInt(TicTacToeProtocol.QUIT);
    }

    /**
     * If the game has either a tie or a winner, the game will print who won and then restart the
     * game
     *
     * @throws IOException
     */

    public void handleGameend() throws IOException {
        int winner = server.readInt();
        System.out.println();
        if (winner > 0) {
            System.out.println("Player " + winner + " won!");
        } else
            System.out.println("The game has tied!");
    }

    /**
     * Alters the boolean isQuit to true
     *
     */

    public void handleQuit() {
        this.isQuit = true;
    }
}

