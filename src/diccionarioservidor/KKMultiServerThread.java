/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diccionarioservidor;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * @author anton
 */



public class KKMultiServerThread extends Thread {
    private Socket socket = null;

    public KKMultiServerThread(Socket socket) {
        super("KKMultiServerThread");
        this.socket = socket;
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String buffer = in.readLine();
            String inputLine, outputLine;
            Protocolo protocol = new Protocolo();
            outputLine = protocol.processInput(buffer);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = protocol.processInput(inputLine);
                out.println(outputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}