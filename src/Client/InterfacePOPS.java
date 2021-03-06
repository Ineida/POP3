package Client;

import Client.Controller.ControllerGridPaneServeurConnexion;
import Client.TCP.Client;
import Client.metier.Etat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class InterfacePOPS extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String ETAT = Etat.AUTORISATION.toString();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                getClass().getResource("./fxmlFile/gridPaneServeurConnexion.fxml"));
        ControllerGridPaneServeurConnexion controller = new ControllerGridPaneServeurConnexion();
        loader.setController(controller);
        Parent gridPaneServeurConnexion = loader.load();

        primaryStage.setTitle("Connexion serveur secure");
        primaryStage.setScene(new Scene(gridPaneServeurConnexion));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
