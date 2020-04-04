package Client.pop3;

import Client.TCP.Client;
import Client.metier.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ClientPOP3 {
    protected static final String APOP ="APOP";
    protected static final String RETR ="RETR";
    protected static final String STAT ="STAT";
    protected static final String QUIT ="QUIT";
    private Client client;
    protected String user;
    protected String password;
    protected String ressourcesName = "src/Client/ClientAccount/";

    public ClientPOP3(String ip, int port) throws IOException {
       this.client = new Client(ip, port);
       this.client.connexion();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //unique car chaque utilisateur est differents dans les serveur
    public String getUserResourcesName() {
        return this.ressourcesName;
    }

    public void commandeAPOP( String user, String password) {
        String message = APOP + " " + user + " " + password;
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

    public  void deleteUserFiles(File file){
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                f.delete();
            }
        }
    }
}