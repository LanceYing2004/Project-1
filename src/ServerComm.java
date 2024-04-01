import java.io.*;
import java.net.Socket;

/**
 * Server communication tool that used to connect with rest of the program.
 *
 * @author Daniel Mayer, Lab Section #3, Team #11
 * @version July 31, 2023
 */
public class ServerComm {
    public Object communicateWithServer(int command, Object inObj) throws IOException, ClassNotFoundException {

        Socket sock = new Socket("localhost", 11000);
        sock.setTcpNoDelay(true);
        // First, the output stream
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
        // Create the objects arr
        Object[] serverArr = new Object[2];
        serverArr[0] = (Integer) command;
        serverArr[1] = inObj;
        oos.writeObject(serverArr);
        oos.flush();
        // Next, the input stream
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));
        Object retObj = ois.readObject();
        return retObj;

    }
}
