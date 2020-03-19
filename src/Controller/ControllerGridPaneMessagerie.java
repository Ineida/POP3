package Controller;

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
import metier.ClientPOP3;
import metier.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGridPaneMessagerie implements Initializable {
    @FXML
    private Label bienvenue;

    @FXML
    private Label error;

   @FXML
   private SplitPane splitPane;

   @FXML
   private AnchorPane anchorPaneLeft;

    @FXML
    private Button synchronisation;
    @FXML
    private Button deconnexion;

    private ClientPOP3 client;
    private List<Message> messages = new ArrayList<Message>();
    private String ETAT = "INITIALISATION";

    public ClientPOP3 getClient() {
        return client;
    }

    public void setClient(ClientPOP3 client) {
        this.client = client;
    }

    public void setAnchorPaneLeftMessages(ListView<HBox> listView) {
        this.anchorPaneLeft.getChildren().addAll(listView);
    }

    public void setBienvenue(String bienvenue) {
        this.bienvenue.setText(bienvenue);
    }

    public void setError(String error) {
        this.error.setText(error);
    }

    public void setSplitPaneMessageReader(GridPane anchoirPane) {
        this.splitPane.getItems().set(0,anchoirPane);
    }

    public void SplitPaneMessageDelete() {
        this.splitPane.getItems().set(0,anchorPaneLeft);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxmlFile/GridPaneServeurConnexion.fxml"));
                        Parent serveur = null;
                        try {
                            serveur = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Scene loginScene = new Scene(serveur);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(loginScene);
                        window.setTitle("Connexion serveur");
                    }
                });
        this.synchronisation.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                   @Override
                                                   public void handle(MouseEvent event) {
                                                     synchronisation();
                                                   }
                                               });
    };

    public HBox item(String from, String date, String suject, String messageId) {
        Label fromLabel = new Label(from);
        fromLabel.setPrefWidth(270);

        Label dateLabel = new Label(date);
        dateLabel.setPrefWidth(200);

        Label sujectLabel = new Label(suject);
        sujectLabel.setPrefWidth(270);

        Label messageIdLabel = new Label(messageId);
        messageIdLabel.setPrefWidth(100);


        HBox title = new HBox(fromLabel, dateLabel, sujectLabel, messageIdLabel);
        title.setPrefWidth(840.0);
        return title;
    }

    public void setAnchorPaneLeftMessages(List<Message> messages) {
        ListView<HBox> items = new ListView<HBox>();
        HBox item = new HBox();

        ObservableList<HBox> hboxOservable = FXCollections.observableArrayList();
        item = this.item("From","Date", "Suject","Message-ID");
        item.setStyle("-fx-background-color: gray");
        hboxOservable.add(0,item);
        items.setItems(hboxOservable);

        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            item = this.item(message.getFrom(), message.getDate(), message.getSubject(), message.getMessageID());
            int index = i;
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                       @Override
                                       public void handle(MouseEvent event) {
                                               Message message = messages.get(index);
                                               GridPane anchorPane = new GridPane();
                                               Button fermer = new Button("fermer");

                                           fermer.setOnMouseClicked(new EventHandler<MouseEvent>(){
                                                   @Override
                                                   public void handle(MouseEvent event) {
                                                       SplitPaneMessageDelete();
                                                   }
                                               });
                                           anchorPane.addRow(0, fermer);
                                           setSplitPaneMessageReader(anchorPane);
                                           Text text = new Text(message.toString());
                                           text.setWrappingWidth(835);

                                           anchorPane.addRow(1,text);
                                           anchorPane.autosize();
                                           anchorPane.maxWidth(840);
                                       }
                                       });
            if (i % 2 == 1){ item.setStyle("-fx-background-color: #FBFBFB"); }
            hboxOservable.add(i+1,item);
        }
        items.setItems(hboxOservable);
        items.setMinWidth(840);

        this.setAnchorPaneLeftMessages(items);
    }

    public void synchronisation() {
        setAnchorPaneLeftMessages(this.getClient().getAllMessage());
    }
}
