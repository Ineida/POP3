package Serveur.POP3S_serveur;

import Serveur.POP3S_serveur.Communication;


import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.net.ssl.KeyManagerFactory.*;

public class Main {

    private static final int PORT = 1025;
    private static SSLServerSocket ss;
    private static SSLSocket connexion;

    public static void main(String[] args) throws IOException {
        try {
            char[] passphrase = "password".toCharArray();
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(null, passphrase);
            FileOutputStream file = new FileOutputStream ("keyStoreServer.jks");
            keystore.store(file , passphrase);
            KeyManagerFactory kmf = getInstance("SunX509");
            kmf.init(keystore, passphrase);
            SSLContext context = SSLContext.getInstance("TLS");
            KeyManager[] keyManagers = kmf.getKeyManagers();


            context.init(keyManagers, null, null);
            ServerSocketFactory sf = context.getServerSocketFactory();

            ss = (SSLServerSocket) sf.createServerSocket(PORT);
            System.out.println(ss);
            String [] cipherSuite = ss.getSupportedCipherSuites();
            String [] allowedCipherSuite = new String[8];
            int j=0;
            for (int i = 0; i < ss.getSupportedCipherSuites().length; i++) {
                if(ss.getSupportedCipherSuites()[i].contains("anon")){
                    allowedCipherSuite[j] = ss.getSupportedCipherSuites()[i];
                    j++;
                }

            }


            ss.setEnabledCipherSuites(allowedCipherSuite);
            String [] cypher = ss.getEnabledCipherSuites();
            for (int i=0;i<cypher.length;i++){
                System.out.println(cypher[i]);
            }

            while(true){
                connexion = (SSLSocket) ss.accept();
                System.out.println(connexion);
                ss.setNeedClientAuth(false);

                new Thread(new Communication(connexion,ss.getInetAddress().getHostName())).start();
            }
        }catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try{
                if(connexion != null){
                    connexion.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
