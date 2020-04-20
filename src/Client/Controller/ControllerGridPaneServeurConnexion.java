package Client.Controller;

import Client.pop3.POP3;
import Client.pop3s.ClientPOP3S;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Client.pop3.ClientPOP3;
import Client.metier.Etat;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.security.sasl.SaslException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ResourceBundle;

public class ControllerGridPaneServeurConnexion implements Initializable {
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
    private String messageError;

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getInputIp_address() throws UnknownHostException {
        return String.valueOf(ip_address.getText());
    }

    public int getInputPort() {
        System.out.println(port.getText());
        return port.getText().isEmpty() ? 0: Integer.parseInt(port.getText());
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
            }else {
                setError("Port ou Adresse IP vide");
            }

        } catch (NumberFormatException ignored) {
            ignored.printStackTrace();
            this.setError("Veulliez saisir pour le port un nombre entier");
        }catch (ConnectException e0){
            if (e0.getMessage().contains("Connection refused: connect"))
                this.setError("Veulliez verifier si le serveur est en marche et si le port est correct");
            else if(e0.getMessage().contains("Connection timed out: connect"))
                this.setError("Veulliez verifier si l'adresse IP du serveur est correct");
            else  this.setError("Veulliez verifier si le serveur est en marche et si le port  et l'adresse IP sont correct");
        } catch (UnknownHostException e){
            this.setError("Le serveur n'est pas connu");
        }catch (SocketException s){
            this.setError("Probleme de connection au réseau");
        }catch(IOException e){
            this.setError("une erreur s'est produit");
            e.printStackTrace();
            connexion = false;
        } catch(CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e1){
            this.setError("une erreur s'est produit");
            connexion = false;
        } finally{
            this.port.setText("");
            ip_address.setText("");
            demandeConnection = connexion;
        }

        return connexion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.error.setAlignment(Pos.CENTER);
        this.error.setWrapText(true);
        this.setError(messageError);
    }
}
