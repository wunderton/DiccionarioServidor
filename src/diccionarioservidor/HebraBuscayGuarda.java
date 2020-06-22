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
public class HebraBuscayGuarda implements Runnable {
    public Thread buscayguarda;
    String busqueda = null;
    String salida = null;
    ArrayList<String> diccionario;

    public HebraBuscayGuarda(String busqueda, ArrayList<String> diccionario) {
        this.busqueda = busqueda;
        this.diccionario = diccionario;
        buscayguarda = new Thread(this, "HebraBuscayGuarda");
    }

    public void borrarFichero(String nombreFichero) {

        File fichero = new File(nombreFichero);
        fichero.delete();
    }

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

    public void guardarDiccionario(ArrayList<String> diccionario) throws FileNotFoundException, IOException {
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

    public synchronized void buscaryguardarpalabra() throws FileNotFoundException, IOException {
        if (diccionario.contains(busqueda)) {
            salida = "PROTOCOL_PSP_JUNIO#ANSWER_TO#"+busqueda+"#IS#true";
            System.out.println("\nEl elemento SÍ existe en la lista");
        } 
        else  {
            salida = "PROTOCOL_PSP_JUNIO#ANSWER_TO#"+busqueda+"#IS#false";
            System.out.println("\nEl elemento no existe, pero se ha añadido");
            diccionario.add(busqueda);
            guardarDiccionario(diccionario);
        }

    } 

  @Override
    public void run() {
        try{
            buscaryguardarpalabra();
    } catch (FileNotFoundException f){
        System.out.println(f + " Fichero no encontrado");
    } catch (IOException io){
        System.out.println(io + " Error entrada o salida");
    } 
}
}
