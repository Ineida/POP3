package fxmlFile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import metier.Etat;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String ETAT = Etat.AUTORISATION.toString();
        Parent gridPaneServeurConnexion = FXMLLoader.load(getClass().getResource("gridPaneServeurConnexion.fxml"));
        primaryStage.setTitle("Connexion serveur");
        primaryStage.setScene(new Scene(gridPaneServeurConnexion, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
