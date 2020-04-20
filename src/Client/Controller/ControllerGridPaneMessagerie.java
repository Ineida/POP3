package Client.Controller;

import Client.pop3.ClientPOP3;
import Client.pop3.POP3;
import Client.pop3s.ClientPOP3S;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Client.metier.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGridPaneMessagerie  extends
        ControllerGridPaneMessagerieAbstract implements Initializable{

    private ClientPOP3 client;

    public ClientPOP3 getClient() {
        return client;
    }

    public void setClient(ClientPOP3 client) {
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAnchorPaneLeftMessages(messages);
        setBienvenue("Bienvenue dans votre compte " + client.getUser());
        this.deconnexion.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            client.closeConnexion();
                        } catch (IOException ignored) {

                        }
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFile/GridPaneServeurConnexion.fxml"));
                        Parent serveur = null;
                        try {
                            loader.setController(new ControllerGridPaneServeurConnexion());
                            serveur = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Scene loginScene = new Scene(serveur);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(loginScene);
                        String title = "Connexion serveur";
                        window.setTitle(title);
                    }
                });
        this.synchronisation.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                   @Override
                                                   public void handle(MouseEvent event) {
                                                     synchronisation();
                                                   }
                                               });
    };

    public void synchronisation() {
        setAnchorPaneLeftMessages(this.getClient().getAllMessage());
    }
}
