package Controller;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class ControllerScreen {
    private HashMap<String, Parent> screenMap = new HashMap<>();
    private Scene actuelScene;

    public ControllerScreen(Scene main) {
        this.actuelScene = main;
    }

    protected void addScreen(String name, Parent pane){
        screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    protected void activate(String name){
        actuelScene.setRoot( screenMap.get(name) );
    }

    public Scene getActuelScene() {
        return actuelScene;
    }
}
