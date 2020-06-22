/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diccionarioservidor;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author anton
 */

public class Protocolo{
    ArrayList<String> dividido = new ArrayList<String>();
    ArrayList<String> diccionario = new ArrayList<String>();
    String theOutput = "";
    String nombre = null;
    boolean existe = false;

    public void importarDiccionario() throws IOException{
        File fichero = new File("Diccionario.dat");
        if(fichero.exists()){
            try (ObjectInputStream dataIS = new ObjectInputStream(new FileInputStream(fichero))) {
                int i = 1;
                try {
                    try {
                        diccionario = (ArrayList<String>) dataIS.readObject();
                    } catch (ClassNotFoundException ex) {
                         System.out.println(ex + "No se encuentra la clase");
                    }
                } catch (EOFException | StreamCorruptedException eo) {
                        System.out.println("FIN DE LECTURA.");
                }
            }
        }
    }
   
     public String processInput(String theInput) throws IOException {

        importarDiccionario();
        boolean sesionIniciada = false;

        int contador = 0;
        for(int i = 0; i < theInput.length() -1; i++){
            if(theInput.charAt(i) == '#'){
                contador++;
                }
        }   
        String[] cadenaSeparada = theInput.split("#");
        
        for(int i = 0; i <= contador; i++) {   
            dividido.add(cadenaSeparada[i]);
        }

            if(theInput.contains("PROTOCOL_PSP_JUNIO")){
                if(theInput.contains("HELLO_ITS_ME")){
                    nombre = dividido.get(dividido.size()-1);
                    System.out.println("SESION INICIADA por " + nombre);
                    theOutput = "PROTOCOL_PSP_JUNIO#WELCOME#" + nombre;
                }
                dividido.clear();

                for(int i = 0; i <= contador; i++) {   
                    dividido.add(cadenaSeparada[i]);
                }
            
            if(theInput.contains("ASK_FOR")){
                System.out.println(theInput);      
                String busqueda = dividido.get(2);
                HebraBuscayGuarda buscayguarda = new HebraBuscayGuarda(busqueda, diccionario);
                try {
                    buscayguarda.buscayguarda.start();
                    buscayguarda.buscayguarda.join();
                    theOutput = buscayguarda.salida;
                } catch (InterruptedException ex) {
                    Logger.getLogger(HebraBuscayGuarda.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            }  else if(dividido.contains("GOODBYE_MY_LOVE")){
                theOutput = "PROTOCOL_PSP_JUNIO#OK#"+nombre+"#IWILLALWAISLOVEYOU";

        } return theOutput;
    }
}




