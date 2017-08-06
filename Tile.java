import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;


// Tiles to place on game board
public class Tile extends StackPane {

	// Whether the tile is a mine
	protected boolean mine;
	// Number of surrounding mines in surrounding tile
	protected int numSurroundingMines;
	// Whether if the tile is revealed
	protected boolean visible;
	// Tile can be flagged as a mine
	protected boolean flag;
	// Coordinate of tile
	protected int row, col;
	// Whether check is need for this tile, false means no check need
	protected boolean check;

	// Tile class constructor, initialize mine field,
	public Tile(int row, int col, boolean mine) {
		this.row = row;
		this.col = col;
		this.mine = mine;
		this.check = true;
		this.flag = false;
		this.visible = false;
		this.numSurroundingMines = 0;
		Rectangle tile = new Rectangle(20, 20);
		tile.setFill(Color.GRAY);
		Text txt = new Text();
		this.getChildren().addAll(tile, txt);
		txt.setVisible(false);
		txt.setFill(Color.BLACK);
		txt.setFont(new Font(15));
		txt.setTextAlignment(TextAlignment.CENTER);
		tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tile.setFill(getColor());
				txt.setText(getText());
				txt.setVisible(true);
			}
		});
	}

	private Color getColor() {
		return this.mine == true ? Color.RED : Color.WHITE;
	}

	private String getText() {
		if(this.mine == true) {
			return "";
		} else {
			return numSurroundingMines == 0 ? "" : String.format("%d", numSurroundingMines);
		}
	}
}
