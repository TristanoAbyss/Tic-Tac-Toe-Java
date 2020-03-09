import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TicTacToeServer {
    public static final int PORT = 22222;
    private final ServerSocket serverSocket;
    private boolean isQuit = false;
    ArrayList<Connection> players = new ArrayList<>();
    private TicTacToeGame game;

    public static void main(String[] args) throws IOException{
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            new TicTacToeServer(serverSocket).startServer();
        }
    }

    public TicTacToeServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    /**
     * begins taking requests from its clients and where most of the logic occurs whether it by move or quit
     *
     * @throws IOException
     */

    public void startServer() throws IOException {
        try(
            Socket p1 = serverSocket.accept();
            Socket p2 = serverSocket.accept();
        ){
            this.players.add(new Connection(p1));
            this.players.add(new Connection(p2));
            this.players.get(0).writeInt(TicTacToeProtocol.PLAYER);
            this.players.get(0).writeInt(1);
            this.players.get(1).writeInt(TicTacToeProtocol.PLAYER);
            this.players.get(1).writeInt(2);
            this.initializeGame();
            while(!isQuit){
                int currentPlayer = game.getNextPlayer()-1;
                sendTurn(currentPlayer);
                int command = this.players.get(currentPlayer).readInt();
                switch (command) {
                    case TicTacToeProtocol.MOVE:
                        this.handleMove(currentPlayer);
                        break;
                    case TicTacToeProtocol.QUIT:
                        this.handleQuit(currentPlayer);
                }
            }
        }finally {
            this.closeAllConnections();
        }
    }

    /**
     * if the game is exited the game will close its sockets and exit itself
     */

    private void closeAllConnections() {
        for (Connection c : this.players) {
            c.close();
        }
    }

    /**
     * handles the move of the player, after each move the win condition is checked to see if a winner or a tie has been accomplished. If either condition has been found
     * a new game is created and the player that did not win will have first move, where as if a tie has been found the player that last moved will be given first move.
     *
     * @param player player who is currently moving piece
     * @throws IOException
     */

    public void handleMove(int player) throws IOException {
        RowCol move = new RowCol(this.players.get(player).readInt(), this.players.get(player).readInt());
        try {
            this.game.makeNextPlay(player, move.getRow(), move.getCol());
            printBoard();

            if (this.game.hasWinner() != 0 || this.game.isTie()) {
                int winner = this.game.hasWinner();
                sendGameend(winner);
                if (1 == winner){
                    initializeGame();
                    game.flipPlayer();
                } else if(this.game.isTie()){
                    if(this.game.getNextPlayer() == 1){
                        initializeGame();
                    } else {
                        initializeGame();
                        this.game.flipPlayer();
                    }
                } else if ( 2 == winner)
                    initializeGame();
            } else {
                this.game.flipPlayer();
            }
        } catch (InvalidMoveException e){
        }
    }

    /**
     * prints the board on the server
     */

    public void printBoard() {
        System.out.println("Board: ");
        this.game.printBoard();
    }

    /**
     * Sends the turn to the player who currently has their turn
     *
     * @param currentPlayer the current player who's turn it is
     * @throws IOException
     */

    private void sendTurn(int currentPlayer) throws IOException {
        Connection p = this.players.get(currentPlayer);
        p.writeInt(TicTacToeProtocol.TURN);
        p.writeInt(currentPlayer+1);
        p.writeUTF(game.getBoardMap());
    }

    /**
     * initializes a new game board
     *
     */

    private void initializeGame() {
        this.game = new TicTacToeGame();
    }

    /**
     * If a win condition is found or a tie is met, the game will send to both clients that the game is over
     *
     * @param winner the winner / tie
     * @throws IOException
     */

    private void sendGameend(int winner) throws IOException {
        for (Connection p : this.players) {
            p.writeInt(TicTacToeProtocol.GAMEEND);
            p.writeInt(winner);
        }
    }

    /**
     * changes the gamestate to quit so that the server knows to close itself, as well as closes both sockets to the clients
     *
     * @param player
     * @throws IOException
     */

    public void handleQuit(int player) throws IOException {
        sendQuit(players.get(other(player)));
        this.isQuit = true;
    }

    /**
     * sends the quit command to the player that had not yet entered quit
     *
     * @param player the player that indicated to quit the game
     * @return
     */

    private int other(int player) {
        return player == 0 ? 1 : 0;
    }

    /**
     * Sends the quit protocol to the other player
     *
     * @param connection the communication given to the client
     * @throws IOException
     */

    private void sendQuit(Connection connection) throws IOException {
        connection.writeInt(TicTacToeProtocol.QUIT);
    }
}
