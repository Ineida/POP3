package Client.Controller;

import Client.metier.Message;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class ControllerGridPaneMessagerieAbstract {
    @FXML
    protected Label bienvenue;

    @FXML
    protected Label error;

   @FXML
   protected SplitPane splitPane;

   @FXML
   protected AnchorPane anchorPaneLeft;

    @FXML
    protected Button synchronisation;
    @FXML
    protected Button deconnexion;

    protected List<Message> messages = new ArrayList<Message>();
    protected String ETAT = "INITIALISATION";


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

    public abstract void synchronisation();
}
