package Client.metier;

public enum Etat {
    //Objets directement construits
    INITIALISATION ("INITIALISATION"),
    CONNEXION_TCP_REUSSI ("CONNEXION_TCP_REUSSI"),
    AUTORISATION ("AUTORISATION"),
    ATTENTE_RESPONSE_SERVEUR ("ATTENTE_RESPONSE_SERVEUR"),
    TRANSACTION ("TRANSACTION"),
    UPADATE ("UPADATE");

    private String name = "";

    //Constructeur
    Etat(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
