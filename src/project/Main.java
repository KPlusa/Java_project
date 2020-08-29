package project;
/**@author Kacper Plusa, Rafal Robak, Mateusz Bzymek
 * @version 1.0*/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**Klasa main, uruchamia klienta aplikacji*/
public class Main extends Application {
    private double x, y;
/**Metoda odpowiada za inicjalizacje pierwszej formatki*/
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/menu.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

