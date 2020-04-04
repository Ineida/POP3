package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Client.pop3.ClientPOP3;
import Client.metier.Etat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class ControllerGridPaneServeurConnexion {
    @FXML
    private Button validate;

    @FXML
    private TextField ip_address;

    @FXML
    private TextField port;

    @FXML
    private Label error;

    private ClientPOP3 client;
    private boolean demandeConnection = false;
    private String ETAT = "INITIALISATION";

    public String getInputIp_address() throws UnknownHostException {
        return String.valueOf(ip_address.getText());
    }

    public int getInputPort() {
        return Integer.parseInt(port.getText());
    }

    public void setError(String error) {
        this.error.setText( error);
    }

    public String getETAT() {
        return ETAT;
    }

    public void setETAT(String ETAT) {
        this.ETAT = ETAT;
    }

    /**
     * initialisation d'une connexion Client.Controller.Controller.TCP
     * @return
     */
    @FXML
    private Boolean demandeConnexion(ActionEvent event) throws IOException {
        boolean connexion = false;
        String serveur = null;
        int port;
        try {
            serveur = getInputIp_address();
            port = getInputPort();
            if(serveur!= null && port != 0){
                this.client = new ClientPOP3(serveur, port);
                connexion = true;
                setETAT(Etat.CONNEXION_TCP_REUSSI.toString());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../fxmlFile/gridPaneLogin.fxml"));
                ControllerGridPaneLoginPOP3 controllerGridPaneLoginPOP3  = new ControllerGridPaneLoginPOP3(this.client);
                controllerGridPaneLoginPOP3.setETAT(ETAT);
                loader.setController(controllerGridPaneLoginPOP3);

                Parent gridPaneLogin = loader.load();
                Scene loginScene = new Scene(gridPaneLogin);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.setTitle("Connexion messagerie");
            }
        } catch (IOException e) {
            this.setError("une erreur s'est produit:"+ e.getMessage());
            connexion = false;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            this.port.setText("");
            ip_address.setText("");
            demandeConnection = connexion;
        }

        return connexion;
    }
}
