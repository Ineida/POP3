package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.ClientPOP3;
import metier.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGridPaneLoginPOP3 implements Initializable {

    @FXML
    protected TextField login;

    @FXML
    protected TextField password;

    @FXML
    private Label error;

    @FXML
    private Button validate;

    private ClientPOP3 client ;
    private List<Message> messages = new ArrayList<Message>();

    private boolean connect;
    private final int NOMBRE_MAX_CONNEXION =3;
    private int nombre_tentative = 0;
    private String ETAT = "INITIALISATION";

    public ControllerGridPaneLoginPOP3(ClientPOP3 client) {
        this.client = client;
    }
    public ControllerGridPaneLoginPOP3() {
        super();
    }

    public String getInputUser() {
        String user = login.getText() == null ? "" : login.getText();
        return user;
    }

    public String getInputPassword() {
        String pass = password.getText() == null ? "" : password.getText();
        return pass;
    }

    public void setError(String error) {
        this.error.setText(error);
        this.error.setWrapText(true);
    }


    public void setClient(ClientPOP3 client) {
        this.client = client;
    }

    public ClientPOP3 getClient() {
        return client;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setElementToMessages(Message message) {
        this.messages.add(message);
    }

    public String getETAT() {
        return ETAT;
    }

    public void setETAT(String ETAT) {
        this.ETAT = ETAT;
    }

    public void demandeConnexion(ActionEvent event) throws IOException, InterruptedException {
        boolean connect = false;
        setError("");
        if(nombre_tentative < NOMBRE_MAX_CONNEXION) {
            if (this.getInputPassword() == "" || this.getInputUser() == "") {
                setError("Le login ou le mot de passe vide");
            } else {
                //connexion etat connexion
                connect = client.connexion(getInputUser(), getInputPassword());
                if (connect) {
                    //etat
                    ArrayList<Message> messages = new ArrayList<Message>();
                    messages = client.getAllMessage();
                    this.setMessages(messages);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFile/GridPaneMessagerie.fxml"));
                    ControllerGridPaneMessagerie controller = new ControllerGridPaneMessagerie();
                    controller.setClient(client);
                    controller.setMessages(messages);
                    loader.setController(controller);
                    Parent messagerie = loader.load();

                    Scene loginScene = new Scene(messagerie);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(loginScene);
                    window.setTitle("Connexion messagerie");
                }else{
                    this.setError("Mot de passe ou login erroné");
                    nombre_tentative++;
                }
            }

        }
        else {
            try {
                client.closeConnexion();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFile/GridPaneServeurConnexion.fxml"));
            Parent serveur = null;
            try {
                serveur = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene loginScene = new Scene(serveur);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.setTitle("Connexion serveur");
        }

        password.setText("");
        login.setText("");
        this.connect = connect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.validate.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                try {
                    demandeConnexion(event);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

