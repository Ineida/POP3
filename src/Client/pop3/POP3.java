package Client.pop3;

import Client.TCP.Client;
import Client.metier.Message;
import Client.metier.StringServices;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class  POP3 {
    protected static final String APOP ="APOP";
    protected static final String RETR ="RETR";
    protected static final String STAT ="STAT";
    protected static final String QUIT ="QUIT";
    protected String user;
    protected String password;
    protected String ressourcesName = "src/Client/ClientAccount/";
    protected MessageDigest messageDigest;
    protected  String method;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public abstract void commandeAPOP(String user, String password);

    public abstract void commandeSTAT();

    public abstract void commandeQUIT();
    public abstract void commandeRETR(int nb_message);

    public abstract void closeConnexion() throws IOException;

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
    public abstract HashMap readServerResponse();

    public abstract int getNbMessages();

    public abstract Message getMessage(int nbMessage);

    public abstract ArrayList<Message> getAllMessage();

    public abstract ArrayList<Message> getAllMessage(int nbMessages);

    /**
     *
     * @param user
     * @param password
     * @return connect : boolean a true si on est connectée
     * si connectée il sauvegarde le user et password du client
     */
    public abstract boolean connexion(String user, String password);

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

