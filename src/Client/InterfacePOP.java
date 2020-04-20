package Client;

import Client.Controller.ControllerGridPaneServeurConnexion;
import Client.metier.Etat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InterfacePOP extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String ETAT = Etat.AUTORISATION.toString();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                getClass().getResource("./fxmlFile/gridPaneServeurConnexion.fxml"));
        ControllerGridPaneServeurConnexion  controller = new ControllerGridPaneServeurConnexion();
        loader.setController(controller);
        Parent gridPaneServeurConnexion = loader.load();

        primaryStage.setTitle("Connexion serveur");
        primaryStage.setScene(new Scene(gridPaneServeurConnexion));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
