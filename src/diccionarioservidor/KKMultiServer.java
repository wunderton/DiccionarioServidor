/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diccionarioservidor;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class KKMultiServer {
    public static void main(String[] args){

    int portNumber = 3478;
    boolean listening = true;
    ArrayList<KKMultiServerThread> conexiones = new ArrayList<>();
                 
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
               conexiones.add(new KKMultiServerThread(serverSocket.accept()));
               conexiones.get(conexiones.size()- 1).start(); 
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}
