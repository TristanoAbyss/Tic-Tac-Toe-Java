import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final DataOutputStream output;
    private final DataInputStream input;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    public int readInt() throws IOException {
        return this.input.readInt();
    }

    public void writeInt(int i) throws IOException {
        this.output.writeInt(i);
        this.output.flush();
    }

    public void writeUTF(String i) throws IOException {
        this.output.writeUTF(i);
        this.output.flush();
    }

    public String readUTF() throws IOException {
        return this.input.readUTF();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
        }
    }
}