import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
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
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MinesweeperApp extends Application {
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // The window of the application
    private Stage window;
    // Size of window
    private int windowSizeX, windowSizeY;
    // Size of the mine
    private int tileSize;
    // Size of the mine field
    private int numRows, numCols;
    // pane for mine field
    private GridPane mineField;
    // Stores all information about the game
    private Minesweeper game;
    // Game difficulty
    private String difficulty;
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Application starts runnning here
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        // Initialize default values;
        tileSize = 20;
        numRows = 12;
        numCols = 12;
        difficulty = "easy";
        windowSizeX = findWindowSizeX();
        windowSizeY = findWindowSizeY();
        primaryStage.setTitle("Minesweeper");
        // Initilize home scene and set as default scene
        primaryStage.setScene(createHomeScene());
        primaryStage.show();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create mineFiled and store in mineField
    // This method creates all the tiles and event handler for each tile
    private void createMineField() {
        game = new Minesweeper(numRows, numCols, difficulty);
        mineField = new GridPane();
        mineField.setHgap(1);
        mineField.setVgap(1);

        EventHandler<MouseEvent> filter = createTileEH();
        // Draw all tiles in the mine
        for(int i = 0; i < this.numRows; i++) {
            for(int j = 0; j < this.numCols; j++) {
                mineField.add(game.getTile(i, j), j, i);
                game.getTile(i, j).addEventFilter(MouseEvent.MOUSE_PRESSED, filter);
            }
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create event filter that only allow tiles to detect mouse event
    // This event handler tracks the source of the click and make action to
    // the game.
    private EventHandler<MouseEvent> createTileEH(){
        EventHandler<MouseEvent> filter = new EventHandler<MouseEvent> (){
            @Override
            public void handle(MouseEvent e) {
                Node source = (Node) e.getSource();
                Integer row = GridPane.getRowIndex(source);
                Integer col = GridPane.getColumnIndex(source);

                // Handle left click
                if(e.getButton() == MouseButton.PRIMARY &&
                !game.isFlagged(row, col)) {
                    game.reveal(row, col);
                    if(game.getTile(row, col).isMine()) {
                        window.setScene(createEndingScene("You Lose"));
                    }
                    // Handle right click
                } else if(e.getButton() == MouseButton.SECONDARY) {
                    if(game.isFlagged(row, col)) {
                        game.deflag(row, col);
                    } else if(!game.isVisible(row, col)){
                        game.flag(row, col);
                    }
                }
                if(game.win()) {
                    window.setScene(createEndingScene("You Win"));
                }
            }
        };
        return filter;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the home scene
    // MenuScene contains start, setting, and close buttons
    private Scene createHomeScene() {
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(windowSizeX, windowSizeY);
        VBox optionBox = new VBox();
        optionBox.setAlignment(Pos.CENTER);
        root.getChildren().add(optionBox);

        // Create buttons
        Button startButton = new Button("Start");
        Button closeButton = new Button("Close");
        Button settingButton = new Button("Setting");

        // Resize buttons
        startButton.setPrefWidth(70);
        closeButton.setPrefWidth(70);
        settingButton.setPrefWidth(70);

        // Give each button its functionality
        startButton.setOnAction(e -> {
            window.setScene(createGameScene());
        });
        settingButton.setOnAction(e -> {
            window.setScene(createSettingScene());
        });
        closeButton.setOnAction(e -> {
            window.close();
        });

        // Gather all components and return scene
        optionBox.getChildren().addAll(startButton, settingButton, closeButton);
        return new Scene(root);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // This method creates the main game scene
    // This scene includes the gameSceneMenu and the mineField
    private Scene createGameScene() {
        Pane root = new Pane();
        root.setPrefSize(windowSizeX, windowSizeY);
        BorderPane bp = new BorderPane();

        // Create components: game field and menu
        createMineField();
        HBox menu = createGameSceneMenu();

        // Gather all components and return scene
        bp.setTop(menu);
        bp.setCenter(mineField);
        root.getChildren().add(bp);
        ScrollPane sp = new ScrollPane();
        sp.setStyle("-fx-background-color: rgba(142, 162, 159, 0.23)");
        sp.setContent(root);
        return new Scene(sp);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create a menu pane for the game scene
    // This pane consists of few buttons of different functionality
    private HBox createGameSceneMenu() {
        HBox menu = new HBox();
        menu.setSpacing(1);

        // Load images for buttons
        Image imgHome = new Image(getClass().getResourceAsStream("images/home_button.png"), 18, 18, false, false);
        Image imgSetting = new Image(getClass().getResourceAsStream("images/setting_button.png"), 18, 18, false, false);
        Image imgClose = new Image(getClass().getResourceAsStream("images/close_button.png"), 18, 18, false, false);
        Image imgNew = new Image(getClass().getResourceAsStream("images/new_button.png"), 18, 18, false, false);
        Image imgHint = new Image(getClass().getResourceAsStream("images/hint_button.png"), 18, 18, false, false);

        // Create buttons
        Button homeButton = new Button(null, new ImageView(imgHome));
        Button newButton = new Button(null, new ImageView(imgNew));
        Button settingButton = new Button(null, new ImageView(imgSetting));
        Button closeButton = new Button(null, new ImageView(imgClose));
        Button hintButton = new Button(null, new ImageView(imgHint));

        // Resize the buttons
        homeButton.setMinWidth(20);
        homeButton.setMinHeight(20);
        newButton.setMinWidth(20);
        newButton.setMinHeight(20);
        settingButton.setMinWidth(20);
        settingButton.setMinHeight(20);
        closeButton.setMinWidth(20);
        closeButton.setMinHeight(20);
        hintButton.setMinWidth(20);
        hintButton.setMinHeight(20);

        homeButton.setPrefSize(20, 20);
        newButton.setPrefSize(20, 20);
        settingButton.setPrefSize(20, 20);
        closeButton.setPrefSize(20, 20);
        hintButton.setPrefSize(20, 20);

        // Assign button functionalities
        homeButton.setOnAction(e -> {
            window.setScene(createHomeScene());
        });
        newButton.setOnAction(e -> {
            window.setScene(createGameScene());
        });
        settingButton.setOnAction(e ->{
            window.setScene(createSettingScene());
        });
        closeButton.setOnAction(e -> {
            window.close();
        });
        hintButton.setOnAction(e -> {
            Solver.search(game);
            if(game.win()) {
                window.setScene(createEndingScene("You Win"));
            }
        });

        // Gather all components and return menu box
        menu.getChildren().addAll(homeButton, newButton, settingButton, closeButton, hintButton);
        return menu;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return the ending scene
    // This scene uses the game scene as background and 3 buttons
    private Scene createEndingScene(String message) {
        StackPane root = new StackPane();
        VBox optionBox = new VBox();
        optionBox.setAlignment(Pos.CENTER);
        Label endingMsg = new Label(message);
        endingMsg.setTextFill(Color.GREEN);
        endingMsg.setFont(new Font(windowSizeX * .2));

        // Create ending scene buttons
        Button newButton = new Button("New Game");
        Button settingButton = new Button("Setting");
        Button exitButton = new Button("Exit");
        newButton.setPrefWidth(90);
        settingButton.setPrefWidth(90);
        exitButton.setPrefWidth(90);

        // Add event handler for the buttons
        newButton.setOnAction(e -> {
            mineField = null;
            createMineField();
            window.setScene(createGameScene());
        });
        settingButton.setOnAction(e -> {
            mineField = null;
            window.setScene(createSettingScene());
        });
        exitButton.setOnAction(e -> {
            window.close();
        });

        // Gather all components and return scene
        optionBox.getChildren().addAll(endingMsg, newButton, settingButton, exitButton);
        root.getChildren().addAll(mineField, optionBox);
        return new Scene(root);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Create setting scene that allows player to change game setting
    // This scene includes some dropdown menus that allow player to select
    // size of mine field and game difficulty; it also contains 2 buttons for
    // saving and discarding changes
    private Scene createSettingScene() {
        StackPane root = new StackPane();
        root.setPrefSize(windowSizeX, windowSizeY);
        VBox optionBox = new VBox();
        optionBox.setAlignment(Pos.CENTER);

        // ChoiceBox for game size
        HBox numRowsBox= new HBox();
        HBox numColsBox = new HBox();
        numRowsBox.setAlignment(Pos.CENTER);
        numColsBox.setAlignment(Pos.CENTER);
        Label rowsLabel = new Label("Rows");
        Label colsLabel = new Label("Cols");
        ChoiceBox<Integer> numRowsCB = new ChoiceBox<Integer>();
        ChoiceBox<Integer> numColsCB = new ChoiceBox<Integer>();
        for(int i = 6; i <= 66; i+=6) {
            numRowsCB.getItems().add(i);
            numColsCB.getItems().add(i);
        }
        numRowsCB.setValue(numRows);
        numColsCB.setValue(numCols);
        numRowsBox.getChildren().addAll(rowsLabel, numRowsCB);
        numColsBox.getChildren().addAll(colsLabel, numColsCB);

        // ChoiceBox for game diffculty
        HBox difficultyBox = new HBox();
        difficultyBox.setAlignment(Pos.CENTER);
        Label difficultyLabel = new Label("Difficulty");
        ChoiceBox<String> difficultyCB = new ChoiceBox<String>();
        difficultyCB.getItems().addAll("easy", "medium", "hard");
        difficultyCB.setValue("easy");
        difficultyBox.getChildren().addAll(difficultyLabel, difficultyCB);

        // Buttons for saving and discarding changes
        HBox saveBox = new HBox();
        saveBox.setAlignment(Pos.CENTER);
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.setOnAction(e -> {
            numRows = numRowsCB.getValue();
            numCols= numColsCB.getValue();
            difficulty = difficultyCB.getValue();
            windowSizeX = findWindowSizeX();
            windowSizeY = findWindowSizeY();
            window.setScene(createHomeScene());
        });
        cancelButton.setOnAction( e -> {
            this.window.setScene(createHomeScene());
        });

        // Resize choice boxes and buttons
        numRowsCB.setPrefWidth(70);
        numColsCB.setPrefWidth(70);
        difficultyCB.setPrefWidth(70);
        saveButton.setPrefWidth(70);
        cancelButton.setPrefWidth(70);

        // Gather all components and return scene
        saveBox.getChildren().addAll(saveButton, cancelButton);
        optionBox.getChildren().addAll(numRowsBox, numColsBox, difficultyBox, saveBox);
        root.getChildren().add(optionBox);
        return new Scene(root);
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Return a proper value for the size of the window based on the size of
    // tiles and the size of mineField
    public int findWindowSizeX() {
        return (tileSize+1) * numCols;
    }
    public int findWindowSizeY() {
        return (tileSize+1) * (numRows + 1); // 1 extra row for menu
    }
}
