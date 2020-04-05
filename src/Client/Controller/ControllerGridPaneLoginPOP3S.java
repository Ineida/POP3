package Client.Controller;

import Client.metier.Message;
import Client.pop3.POP3;
import Client.pop3s.ClientPOP3S;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGridPaneLoginPOP3S extends ControllerGridPaneLoginPOP3Abstract implements Initializable {
    private ClientPOP3S client ;

    public ControllerGridPaneLoginPOP3S(ClientPOP3S client) {
        this.client = client;
    }
    public ControllerGridPaneLoginPOP3S() {
        super();
    }

    public void setClient(ClientPOP3S client) {
        this.client = client;
    }

    public ClientPOP3S getClient() {
        return client;
    }

    public void demandeConnexion(ActionEvent event) throws IOException, InterruptedException {
        boolean connect = false;
        this.setError("");
        if (this.getInputPassword().length() == 0 || this.getInputUser().length() == 0) {
            this.setError("Le login ou le mot de passe vide");
        } else if(nombre_tentative < NOMBRE_MAX_CONNEXION) {
                //connexion etat connexion
                connect = client.connexion(getInputUser(), getInputPassword());
                if (connect) {
                    //etat
                    ArrayList<Message> messages = new ArrayList<Message>();
                    messages = client.getAllMessage();
                    this.setMessages(messages);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFile/GridPaneMessagerie.fxml"));
                    ControllerGridPaneMessagerieSecure controller = new ControllerGridPaneMessagerieSecure();
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
            } else {
            try {
                client.closeConnexion();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader(getClass().
                    getResource("../fxmlFile/GridPaneServeurConnexion.fxml"));
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

