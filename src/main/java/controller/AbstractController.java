package controller;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import util.Controllers;

/**
 * This is the abstract controller that every other controller should extend
 * In this system, every controller controls a scene, determining each scene's
 * action and behaviour.
 */
public abstract class AbstractController implements Initializable {
    /**
     * The scene
     */
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
