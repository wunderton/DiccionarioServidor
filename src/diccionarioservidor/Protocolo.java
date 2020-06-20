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
/**
 *
 * @author anton
 */

public class Protocolo {
    ArrayList<String> dividido = new ArrayList<String>();
    ArrayList<String> diccionario = new ArrayList<String>();;
    String theOutput = "Conectado";
    String nombre = null;
    boolean existe = false;

    public void borrarFichero(String nombreFichero) {

        File fichero = new File(nombreFichero);
        fichero.delete();
    }

    private void importarDiccionario() throws IOException{
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

    private void guardarDiccionario(ArrayList<String> diccionario) throws FileNotFoundException, IOException {
        String nombreFichero = "Diccionario.dat";
        borrarFichero(nombreFichero);
        File fichero = new File(nombreFichero);//declara el fichero
        
        FileOutputStream fileout = new FileOutputStream(fichero,true);
        try(ObjectOutputStream dataOS = new ObjectOutputStream(fileout)) {
            try{
                dataOS.writeObject(diccionario);
                dataOS.flush();
                dataOS.close();
                
            } catch (FileNotFoundException f) {
                System.out.println(f + "Error: El fichero no existe. ");
            } catch (IOException ioe) {
                System.out.println(ioe + "Error: Fallo en la escritura en el fichero. ");
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
                
                for(int i = 0; i < diccionario.size() && existe == false; i++){
                    if(busqueda == diccionario.get(i)){
                        existe = true;
                    }
                }
                if (diccionario.contains(busqueda)) {
                    theOutput = "PROTOCOL_PSP_JUNIO#ANSWER_TO#"+busqueda+"#IS#true";
                    System.out.println("\nEl elemento SÍ existe en la lista");
                } 
                else  {
                    theOutput = "PROTOCOL_PSP_JUNIO#ANSWER_TO#"+busqueda+"#IS#false";
                    System.out.println("\nEl elemento no existe, pero se ha añadido");
                    diccionario.add(busqueda);
                    guardarDiccionario(diccionario);
                }
            }
            else if(dividido.contains("GOODBYE_MY_LOVE")){
                theOutput = "PROTOCOL_PSP_JUNIO#OK#"+nombre+"#IWILLALWAISLOVEYOU";
            }

            }  else theOutput = "PROTOCOL_PSP_JUNIO#BAD_MESSAGE";

    return theOutput;
    } 
}




