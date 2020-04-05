package Client.pop3s;

import Client.TCP.ClientSecure;
import Client.metier.Message;
import Client.pop3.ClientPOP3;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientPOP3S  extends ClientPOP3 {
    private ClientSecure client;

    public ClientPOP3S(String ip, int port) throws IOException, NoSuchAlgorithmException,
            CertificateException, KeyStoreException, KeyManagementException
    {
        this(ip, port,"MD5");
    }

    public ClientPOP3S(String ip, int port, String method) throws IOException, NoSuchAlgorithmException,
            CertificateException, KeyStoreException, KeyManagementException
    {
        super();
        this.client = new ClientSecure(ip, port);
        this.client.connexion();
        this.method = method;
        this.messageDigest = MessageDigest.getInstance(this.method);
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void commandeAPOP(String user, String password) {
        String message = APOP + " " + user + " ";
        String  response = (String) this.readServerResponse().get("firstLine");
        String[] splitedResponse = response.split(" ");
        byte[] secret = (splitedResponse[splitedResponse.length -1]+password).getBytes();
        this.messageDigest.update(secret);
        secret = this.messageDigest.digest();
        message += secret;
        try {
            this.client.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void commandeSTAT() {
        try {
            this.client.sendMessage(STAT);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public void commandeQUIT() {
        try {
            this.client.sendMessage(QUIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commandeRETR(int nb_message) {
        String requete = RETR + " " +nb_message;
        try {
            this.client.sendMessage(requete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnexion() throws IOException {
        this.commandeQUIT();
        HashMap response = this.readServerResponse();
        if((boolean)response.get("succes")){
            this.client.closeConnexion();
            File file = new File(this.getUserResourcesName());
            this.deleteUserFiles(file);
        }
    }

    /**
     *
     * @return HashMap reponse du serveur
     * : example reponse d'une Requeste STAT
     * {
     *     succes : true,
     *     firtLine : "+OK 2 320"
     *     message: ""
     * }
     */
    public HashMap readServerResponse() {

        String messageRead = "";
        String firstline = "";
        boolean first = true;
        int charactere ;
        int charactereBefore = 0;
        HashMap response = new HashMap();
        try {
            while (!((charactere = this.client.intRead()) == 3 && charactereBefore ==10 )) {

                if(charactere == 13 ){ first = false; }
                if (first) {
                    firstline += (char) charactere;
                } else {
                    messageRead += (char) charactere;
                }
                charactereBefore = charactere;
            }
            if (firstline.contains("+OK")) {
                response.put("succes", true);
                response.put("firstLine", firstline);
            } else {
                response.put("succes", false);
            }
            response.put("message", messageRead);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int getNbMessages() {
        this.commandeSTAT();
        HashMap reponse = this.readServerResponse();
        String firtLine = (String)reponse.get("firstLine");
        String[] splitReponse = firtLine.split(" ");
        int nbMessage = Integer.parseInt(splitReponse[1]);
        return  nbMessage;
    }

    public Message getMessage(int nbMessage) {
        this.commandeRETR(nbMessage);
        HashMap reponse = this.readServerResponse();
        boolean succes = (boolean)reponse.get("succes");
        if (succes) {
            String messageString = (String)reponse.get("message");
            Message message = new Message(messageString);
            message.saveMessage(this.getUserResourcesName() + nbMessage);
            return message;
        }
        return null;

    }

    public ArrayList<Message> getAllMessage() {
        int nbMessages = this.getNbMessages();
        ArrayList<Message> myMessages = new ArrayList<Message>();
        for(int i = 1; i <= nbMessages; i++ ) {
            myMessages.add(this.getMessage(i));
        }
        return  myMessages;
    }

    public ArrayList<Message> getAllMessage(int nbMessages) {
        ArrayList<Message> myMessages = new ArrayList<Message>();
        for(int i = 1; i <= nbMessages; i++ ) {
            myMessages.add(this.getMessage(i));
        }
        return  myMessages;
    }

    /**
     *
     * @param user
     * @param password
     * @return connect : boolean a true si on est connectée
     * si connectée il sauvegarde le user et password du client
     */
    public boolean connexion(String user, String password){
        this.readServerResponse();
        boolean connect = false;
        this.commandeAPOP(user, password);
        HashMap response = this.readServerResponse();
        connect = (boolean)response.get("succes");

        if (connect){
            this.setUser(user);
            this.setPassword(password);
        }

        return connect;
    }


    public static void main(String[] args) throws IOException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ClientPOP3S clientPOP3S = new ClientPOP3S( "192.168.0.22", 1025);
        clientPOP3S.connexion("vascis", "mdp");

    }

}
