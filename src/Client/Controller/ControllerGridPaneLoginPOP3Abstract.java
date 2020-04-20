package Client.Controller;

import Client.metier.Message;
import Client.pop3.POP3;
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

public abstract class ControllerGridPaneLoginPOP3Abstract implements Initializable {

    @FXML
    protected TextField login;

    @FXML
    protected TextField password;

    @FXML
    protected Label error;

    @FXML
    protected Button validate;

    protected List<Message> messages = new ArrayList<Message>();

    protected boolean connect;
    protected final int NOMBRE_MAX_CONNEXION =2; // 3 tentative car commence à 0
    protected int nombre_tentative = 0;
    protected String ETAT = "INITIALISATION";



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

}

