package Client.Controller;

import Client.pop3.POP3;
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
import Client.pop3.ClientPOP3;
import Client.metier.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGridPaneLoginPOP3 extends
        ControllerGridPaneLoginPOP3Abstract implements Initializable {
    private ClientPOP3 client ;

    public ControllerGridPaneLoginPOP3(ClientPOP3 client) {
        this.client = client;
    }
    public ControllerGridPaneLoginPOP3() {
        super();
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
            } else {
            try {
                client.closeConnexion();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().
                    getResource("../fxmlFile/GridPaneServeurConnexion.fxml"));
            Parent serveur = null;
            try {
                ControllerGridPaneServeurConnexion controllerGridPaneServeurConnexion
                        = new ControllerGridPaneServeurConnexion();

                controllerGridPaneServeurConnexion.setMessageError("Vous avez effectué plus de 3 tentative de connection au " +
                        "serveur POP3. Veuillez reessayer");
                loader.setController(controllerGridPaneServeurConnexion);
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
        password.setPromptText("");
        login.setText("");
        login.setPromptText("");;
        this.connect = connect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.client.readServerResponse();
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

