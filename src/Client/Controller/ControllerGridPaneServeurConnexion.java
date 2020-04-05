package Client.Controller;

import Client.pop3.POP3;
import Client.pop3s.ClientPOP3S;
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

    private POP3 client;
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
    private Boolean demandeConnexion(ActionEvent event) {
        boolean connexion = false;
        String serveur = null;
        int port;
        try {
            serveur = getInputIp_address();

            port = getInputPort();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            String title;
            ControllerGridPaneLoginPOP3Abstract controllerGridPaneLoginPOP3;
            if (serveur != null && port != 0) {
                if (window.getTitle().contains("secure")) {
                    ClientPOP3S client = new ClientPOP3S(serveur, port);
                    title = "Connexion messagerie secure";
                    controllerGridPaneLoginPOP3 = new ControllerGridPaneLoginPOP3S(client);
                    this.client = client;

                } else {
                    ClientPOP3 client = new ClientPOP3(serveur, port);
                    title = "Connexion messagerie";
                    controllerGridPaneLoginPOP3 = new ControllerGridPaneLoginPOP3(client);
                    this.client = client;
                }
                connexion = true;
                setETAT(Etat.CONNEXION_TCP_REUSSI.toString());

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../fxmlFile/gridPaneLogin.fxml"));
                loader.setController(controllerGridPaneLoginPOP3);

                Parent gridPaneLogin = loader.load();
                Scene loginScene = new Scene(gridPaneLogin);
                window.setScene(loginScene);
                window.setTitle(title);
            }

        } catch(IOException e){
            this.setError("une erreur s'est produit:" + e.getMessage());
            connexion = false;
        } catch(CertificateException e1){
            this.setError("une erreur s'est produit:" + e1.getMessage());
            connexion = false;
        } catch(NoSuchAlgorithmException e){
            this.setError("une erreur s'est produit:" + e.getMessage());
            connexion = false;
        } catch(KeyStoreException e){
            this.setError("une erreur s'est produit:" + e.getMessage());
            connexion = false;
        } catch(KeyManagementException e){
            this.setError("une erreur s'est produit:" + e.getMessage());
            connexion = false;
        } finally{
            this.port.setText("");
            ip_address.setText("");
            demandeConnection = connexion;
        }

        return connexion;
    }
}
