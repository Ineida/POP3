/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Serveur.tp1_ipc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

public class Communication implements Runnable {

    private final Socket connexion;
    //flux I/O
    private BufferedReader in;
    private PrintStream out;
    private final String chemin = "./src/";
    private String etat;
    private boolean clientDemandeQuit;
    private String login;
    private static final char END_OF_TEXT = (char)3;


    public Communication(Socket connexion) {
        this.connexion = connexion;
        try {
            in = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            out = new PrintStream(connexion.getOutputStream());
            etat = "Initialisation";
            clientDemandeQuit = false;
            login = "";
        } catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        out.println("+OK POP3 server ready");
        out.print(END_OF_TEXT);
        etat = "Autorisation";
        while (clientDemandeQuit == false) {
            String messageClient;
            try {
                messageClient = in.readLine();
                String messageClientSplit[] = messageClient.split(" ");
                String cmd = messageClientSplit[0];

                switch (cmd) {
                    case "APOP":
                        switch (etat) {
                            case "Autorisation":
                                login = messageClientSplit[1];
                                File file = new File(chemin + login);
                                boolean fileExist = file.exists();

                                if (fileExist) {
                                    File fileMdp = new File(chemin + login + "/mdp.txt");
                                    BufferedReader br;
                                    br = new BufferedReader(new FileReader(fileMdp));
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        if (line.equals(messageClientSplit[2])) {
                                            File listeFichier1[] = file.listFiles();
                                            int nbMessages = listeFichier1.length - 1;
                                            out.println("+OK " + nbMessages + " messages");
                                            etat = "Transaction";
                                        } else {
                                            out.println("-ERR mot de passe");
                                        }
                                    }
                                    br.close();
                                } else {
                                    out.println("-ERR login");
                                }
                                break;
                            case "Transaction":
                                out.println("-ERR commande non autorisée");
                                break;
                            default:
                                out.println("-ERR commande non autorisée");
                                break;
                        }
                        out.print(END_OF_TEXT);
                        break;
                    case "RETR":
                        switch (etat) {
                            case "Autorisation":
                                out.println("-ERR commande non autorisée" );
                                break;
                            case "Transaction":
                                String numeroMessage = messageClientSplit[1];
                                File fileMessage = new File(chemin + login + "/" + numeroMessage + ".txt");
                                if (fileMessage.exists()) {
                                    long octets = fileMessage.length();
                                    out.println("+OK " + octets + " octets" );
                                    BufferedReader bufferMessage = new BufferedReader(new FileReader(fileMessage));
                                    String lineMessage;
                                    boolean partieCorps = false;
                                    while ((lineMessage = bufferMessage.readLine()) != null) {
                                        int tailleLigne = lineMessage.length();
                                        if (lineMessage.equals("")) {
                                            partieCorps = true;
                                        }
                                        if (tailleLigne > 78 && partieCorps) {
                                            while (tailleLigne > 78) {
                                                out.println(lineMessage.substring(0, 78));
                                                lineMessage = lineMessage.substring(78);
                                                tailleLigne = lineMessage.length();
                                            }

                                            out.println(lineMessage);

                                        } else {
                                            out.println(lineMessage);
                                        }
                                    }
                                    bufferMessage.close();
                                } else {
                                    out.println("-ERR message non trouvé" );
                                }
                                break;
                            default:
                                out.println("-ERR commande non autorisée");
                                break;
                        }
                        out.print(END_OF_TEXT);
                        break;
                    case "STAT":
                        switch (etat) {
                            case "Autorisation":
                                out.println("-ERR commande non autorisée" );
                                break;
                            case "Transaction":
                                File file = new File(chemin + login);
                                File listeFichier[] = file.listFiles();
                                int nbMessage = listeFichier.length - 1;
                                long taille_depot = 0;
                                for(int i=0;i<nbMessage;i++){
                                    taille_depot+=listeFichier[i].length();
                                }
                                out.println("+OK " + nbMessage + " "+taille_depot );
                                break;
                            default:
                                out.println("-ERR commande non autorisée");
                                break;
                        }
                        out.print(END_OF_TEXT);
                        break;
                    case "QUIT":
                        switch (etat) {
                            case "Autorisation":
                                out.println("+OK POP3 server signing off");
                                out.print(END_OF_TEXT);
                                in.close();
                                out.close();
                                connexion.close();
                                clientDemandeQuit = true;
                                break;
                            case "Transaction":
                                out.println("+OK POP3 server signing off");
                                out.print(END_OF_TEXT);
                                in.close();
                                out.close();
                                connexion.close();
                                clientDemandeQuit = true;
                                break;
                            default:
                                out.println("-ERR commande non autorisée");
                                out.print(END_OF_TEXT);
                                break;
                        }
                        break;
                    default:
                        out.println("-ERR commande incorrect");
                        out.print(END_OF_TEXT);
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
                out.println("-ERR");
                out.print(END_OF_TEXT);
            }

        }
    }
}
