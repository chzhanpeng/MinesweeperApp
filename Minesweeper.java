import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;



public class Minesweeper extends Application {


    // The window of the application
    private Stage window;
    // Scenes of the application
    private Scene menuScene, gameScene;
    // Size of window
    private int windowSizeX, windowSizeY;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Application starts runnning here
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        windowSizeX = 400;
        windowSizeY = 400;

        primaryStage.setTitle("MineSweeper");

        this.menuScene = new Scene(createMenuScene());
        this.gameScene = new Scene(createGameScene());

        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the intro menu scene
    private Parent createMenuScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);

        Button startButton = new Button("START");

        startButton.addEventHandler(ActionEvent.ACTION, e -> {
            window.setScene(this.gameScene);
        }); // Switch to gameScene when startButton is clicked
        root.getChildren().add(startButton);

        return root;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the game scene
    private Parent createGameScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);

        // Draw all tiles in the mine
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(i * 20);
                tile.setTranslateY(j * 20);

                root.getChildren().add(tile);
            }
        }

        return root;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Tile class for tiles in the game
    private class Tile extends StackPane {

        // Draw the tile
        public Tile() {
            Rectangle border = new Rectangle(20, 20);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border);
        }

    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
