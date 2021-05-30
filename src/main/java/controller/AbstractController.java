package controller;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import util.Controllers;

public abstract class AbstractController implements Initializable {
    private Parent scene;

    public Parent getScene() {
        return scene;
    }

    public void setScene(Parent scene) {
        this.scene = scene;
    }
    
    public void scene(){
        Controllers.setCenter(scene);
    }

    @Override
    public String toString() {
        return "AbstractController{" +
                "scene=" + scene +
                '}';
    }
}
