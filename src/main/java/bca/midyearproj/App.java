package bca.midyearproj;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception { 
        Game game = new Game(new File("src/main/resources/boards/default.txt"));
        Scene scene = new Scene(game);
        primaryStage.getIcons().add(new Image(new File("src/main/resources/images/icon.png").toURI().toString()));
        primaryStage.setTitle("RPChess");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}