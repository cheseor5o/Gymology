import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Controllers;
import util.Databases;
import util.InstanceDataBase;
import util.OrderDatabase;

import java.io.IOException;

public class App extends Application{
    
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        Parent parent = loader.load();
        MainController mainController = loader.getController();
        Controllers.put(mainController);
        Controllers.setMainPane(mainController.getMainPane());
        stage.setTitle("Gymology");
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.show();
        mainController.homeScene();
        //持久化
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("See you next time!");
            try {
                InstanceDataBase.store();
                OrderDatabase.store();
                Databases.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
