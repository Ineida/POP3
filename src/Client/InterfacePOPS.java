package Client;

import Client.TCP.Client;
import Client.metier.Etat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InterfacePOPS extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String ETAT = Etat.AUTORISATION.toString();
        Parent gridPaneServeurConnexion = FXMLLoader.load(
                getClass().getResource("./fxmlFile/gridPaneServeurConnexion.fxml"));
        primaryStage.setTitle("Connexion serveur secure");
        primaryStage.setScene(new Scene(gridPaneServeurConnexion));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
