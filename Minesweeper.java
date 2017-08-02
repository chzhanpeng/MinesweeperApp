import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;



public class Minesweeper extends Application {


    // The window of the application
    private Stage window;
    // Scenes of the application
    private Scene menuScene, gameScene, settingScene;
    // Size of window
    private int windowSizeX, windowSizeY;
    // Size of the mine
    private int tileSize;
    private int width, height;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Application starts runnning here
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        // Initialize default values;
        windowSizeX = 400;
        windowSizeY = 400;
        tileSize = 20;
        width = 20;
        height = 20;

        primaryStage.setTitle("MineSweeper");
        // Initilize menu scene and set as default scene
        this.menuScene = new Scene(createMenuScene());
        primaryStage.setScene(menuScene);
        primaryStage.show();
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
    // This method creates the intro menu scene
    // MenuScene contains start, setting, and close buttons
    private Parent createMenuScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);

        VBox box = new VBox();
        root.getChildren().add(box);
        Button startButton = new Button("Start");
        Button closeButton = new Button("Close");
        Button settingButton = new Button("Setting");
        // Give each button its functionaliyy
        startButton.setOnAction(e -> {
            this.gameScene = new Scene(createGameScene());
            window.setScene(this.gameScene);
        });
        settingButton.setOnAction(e -> {
            this.settingScene = new Scene(createSettingScene());
            window.setScene(this.settingScene);
        });
        closeButton.setOnAction(e -> {
            window.close();
        });

        box.getChildren().add(startButton);
        box.getChildren().add(settingButton);
        box.getChildren().add(closeButton);

        return root;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the game scene where the game is played
    private Parent createGameScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);

        // Draw all tiles in the mine
        for(int i = 0; i < this.width; i++) {
            for(int j = 0; j < this.height; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(i * this.tileSize);
                tile.setTranslateY(j * this.tileSize);
                root.getChildren().add(tile);
            }
        }

        return root;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create setting scene that allows player to change game setting
    // This scene includes some dropdown menus that allow player to select
    // size of mine and game difficulty;
    private Parent createSettingScene() {
        Pane root = new Pane();
        root.setPrefSize(200,200);
        VBox optionBox = new VBox();

        HBox widthBox = new HBox();
        HBox heightBox = new HBox();
        HBox saveBox = new HBox();
        // Drop down option for game width and height
        Label widthLabel = new Label("Width");
        Label heightLabel = new Label("Height");
        ChoiceBox<Integer> widthChoiceBox = new ChoiceBox<Integer>();
        ChoiceBox<Integer> heightChoiceBox = new ChoiceBox<Integer>();
        for(int i = 5; i <= 100; i+=5) {            // Add all options
          widthChoiceBox.getItems().add(i);
          heightChoiceBox.getItems().add(i);
        }
        widthChoiceBox.setValue(this.width);
        heightChoiceBox.setValue(this.height);
        // Buttons for saving and discarding changes
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.setOnAction(e -> {
            this.width = widthChoiceBox.getValue();
            this.height = heightChoiceBox.getValue();
            this.window.setScene(this.menuScene);
            System.out.println(this.width);
            System.out.println(this.height);
        });
        cancelButton.setOnAction( e -> {
            this.window.setScene(this.menuScene);
            System.out.println(this.width);
            System.out.println(this.height);
        });

        widthBox.getChildren().addAll(widthLabel, widthChoiceBox);
        heightBox.getChildren().addAll(heightLabel, heightChoiceBox);
        saveBox.getChildren().addAll(saveButton, cancelButton);

        optionBox.getChildren().addAll(widthBox, heightBox, saveBox);
        root.getChildren().add(optionBox);
        return root;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
