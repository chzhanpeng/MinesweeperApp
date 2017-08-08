import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;


public class MinesweeperApp extends Application {

    // The window of the application
    private Stage window;
    // Scenes of the application
    private Scene homeScene, gameScene, settingScene;
    // Size of window
    private int windowSizeX, windowSizeY;
    // Size of the mine
    private int tileSize;
    private int fieldSizeX, fieldSizeY;
    // pane for mine field
    private GridPane mineField;
    private MineSweeper game;

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Application starts runnning here
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        // Initialize default values;
        tileSize = 20;
        fieldSizeX = 20;
        fieldSizeY = 20;
        windowSizeX = findWindowSizeX();
        windowSizeY = findWindowSizeY();
        //window.setMinWidth(450);
        //window.setMinHeight(450);
        primaryStage.setTitle("MineSweeper");

        // Initilize home scene and set as default scene
        this.homeScene = new Scene(createHomeScene());
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private GridPane createMineField() {
        game = new MineSweeper(fieldSizeX, fieldSizeY, "easy");
        Generator grt = new Generator();
        grt.randomGenerate(game, 50);
        game.cheat();


        GridPane gamePane = new GridPane();
        gamePane.setHgap(1);
        gamePane.setVgap(1);

        // Create event filter that only allow tiles to detect mouse event
        EventHandler<MouseEvent> filter = new EventHandler<MouseEvent> (){
            @Override
            public void handle(MouseEvent e) {
                Node source = (Node) e.getSource();
                Integer row = GridPane.getRowIndex(source);
                Integer col = GridPane.getColumnIndex(source);
                //System.out.printf("%d %d\n", row, col);
                game.reveal(row, col);
            }
        };

        // Draw all tiles in the mine
        for(int i = 0; i < this.fieldSizeX; i++) {
            for(int j = 0; j < this.fieldSizeY; j++) {
                gamePane.add(game.getTile(i, j), j, i);
                game.getTile(i, j).addEventFilter(MouseEvent.MOUSE_PRESSED, filter);
            }
        }


        return gamePane;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Tile class for tiles in the game
    /*private class Tile extends Parent {

        public Tile() {
            Rectangle tile = new Rectangle(20, 20);
            tile.setFill(Color.BLUE);
            this.getChildren().add(tile);
            tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    tile.setFill(Color.RED);
                }
            });
        }
    }*/
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the home scene
    // MenuScene contains start, setting, and close buttons
    private Parent createHomeScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);
        VBox vbox = new VBox();
        root.getChildren().add(vbox);

        // Create buttons
        Button startButton = new Button("Start");
        Button closeButton = new Button("Close");
        Button settingButton = new Button("Setting");
        // Give each button its functionality
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

        vbox.getChildren().addAll(startButton, settingButton, closeButton);
        return root;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the main game scene
    // This scene includes the gameSceneMenu and the mineField
    private Parent createGameScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);

        //HBox menuPane = new HBox();
        //menuPane.getChildren().add(createGameSceneMenu());

        BorderPane bp = new BorderPane();
        mineField = createMineField();
        //bp.setTop(menuPane);
        bp.setCenter(mineField);
        root.getChildren().add(bp);

        ScrollPane sp = new ScrollPane();
        sp.setContent(root);
        sp.fitToWidthProperty();
        return sp;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create a menu pane for the game scene
    // This pane consists of few buttons of different functionality
    private HBox createGameSceneMenu() {
        HBox menu = new HBox();
        menu.setMinHeight(40);
        menu.setStyle("-fx-background-color: rgb(173, 175, 174)");
        // Create buttons
        Button homeButton = new Button("Home");
        Button newButton = new Button("New Game");
        Button settingButton = new Button("Setting");
        Button closeButton = new Button("Close");
        // Assign button functionalities
        homeButton.setOnAction(e -> {
            window.setScene(homeScene);
        });
        newButton.setOnAction(e -> {
            gameScene = new Scene(createGameScene());
        });
        settingButton.setOnAction(e ->{
            if(settingScene == null) {
                settingScene = new Scene(createSettingScene());
            }
            window.setScene(settingScene);
        });
        closeButton.setOnAction(e -> {
            window.close();
        });
        menu.getChildren().addAll(homeButton, newButton, settingButton, closeButton);
        return menu;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create setting scene that allows player to change game setting
    // This scene includes some dropdown menus that allow player to select
    // size of mine field and game difficulty; it also contains 2 buttons for
    // saving and discarding changes
    private Parent createSettingScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);
        VBox optionBox = new VBox();

        // Drop down option for mine field size
        HBox fieldSizeBox= new HBox();
        Label fieldSizeLabel = new Label("Field Size");
        Label xLabel = new Label("X");
        ChoiceBox<Integer> fieldSizeXChoiceBox = new ChoiceBox<Integer>();
        ChoiceBox<Integer> fieldSizeYChoiceBox = new ChoiceBox<Integer>();
        for(int i = 5; i <= 40; i+=5) {                 // Add all options
            fieldSizeXChoiceBox.getItems().add(i);
            fieldSizeYChoiceBox.getItems().add(i);
        }
        fieldSizeXChoiceBox.setValue(this.fieldSizeX);    // default values
        fieldSizeYChoiceBox.setValue(this.fieldSizeY);

        // Buttons for saving and discarding changes
        HBox saveBox = new HBox();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.setOnAction(e -> {
            this.fieldSizeX = fieldSizeXChoiceBox.getValue();
            this.fieldSizeY= fieldSizeYChoiceBox.getValue();
            this.windowSizeX = findWindowSizeX();
            this.windowSizeY = findWindowSizeY();
            this.window.setScene(this.homeScene);
            this.gameScene = new Scene(createGameScene());
        });
        cancelButton.setOnAction( e -> {
            this.window.setScene(this.homeScene);
        });

        fieldSizeBox.getChildren().addAll(fieldSizeLabel, fieldSizeXChoiceBox, xLabel, fieldSizeYChoiceBox);
        saveBox.getChildren().addAll(saveButton, cancelButton);
        optionBox.getChildren().addAll(fieldSizeBox, saveBox);
        root.getChildren().add(optionBox);
        return root;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return a proper value for the size of the window based on the size of
    // tiles and the size of mineField
    public int findWindowSizeX() {
        if(fieldSizeX < 10) {
            return 200;
        }else{
            return tileSize * (fieldSizeX + 1);
        }
    }
    public int findWindowSizeY() {
        if(fieldSizeY < 10) {
            return 200;
        }else{
            return tileSize * (fieldSizeY + 1);
        }
    }
}
