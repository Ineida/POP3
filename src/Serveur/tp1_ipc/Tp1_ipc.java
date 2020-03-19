/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveur.tp1_ipc;

/**
 *
 * @author Smaïline
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tp1_ipc {

    /**
     * @param args the command line arguments
     */

    private static final int PORT = 1025;
    private static  ServerSocket ss;
    private static Socket connexion;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
            while(true){
                connexion = ss.accept();
                new Thread(new Communication(connexion)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Tp1_ipc.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try{
                if(connexion != null){
                    connexion.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Tp1_ipc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
