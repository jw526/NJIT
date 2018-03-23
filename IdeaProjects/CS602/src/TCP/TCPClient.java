package TCP;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws Exception{
        String sentence;
        String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", 6789);
        //outputStream bind to the socket
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        //read from server through socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + '\n');
        modifiedSentence = inFromServer.readLine();
        System.out.println("From Server:" + modifiedSentence);
        clientSocket.close();

    }
}
