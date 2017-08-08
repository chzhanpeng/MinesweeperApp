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
	// Rectangle representing the tile
	protected Rectangle rect;
	// Text for  number of surrouding mine
	protected Text txt;

	// Tile class constructor, initialize mine field,
	public Tile(int row, int col, boolean mine) {
		this.row = row;
		this.col = col;
		this.mine = mine;
		this.check = true;
		this.flag = false;
		this.visible = false;
		this.numSurroundingMines = 0;
		this.rect = new Rectangle(20, 20);
		rect.setFill(Color.GRAY);
		this.txt = new Text();
		this.txt.setFont(new Font(15));
		this.txt.setTextAlignment(TextAlignment.CENTER);
		this.txt.setFill(Color.BLACK);
		this.txt.setVisible(false);
		this.getChildren().addAll(rect, txt);
		/*Text txt = new Text();
		this.getChildren().addAll(rect, txt);
		txt.setVisible(false);
		txt.setFill(Color.BLACK);
		txt.setFont(new Font(15));
		txt.setTextAlignment(TextAlignment.CENTER);
		/*tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				tile.setFill(getColor());
				txt.setText(getText());
				txt.setVisible(true);
			}
		});*/
	}

	// Reveal this tile
	public void reveal() {
		txt.setText(getText());
		rect.setFill(getColor());
		txt.setVisible(true);
		visible = true;
	}

	// Return appropriate color for this tile
	private Color getColor() {
		return this.mine == true ? Color.RED : Color.WHITE;
	}

	// Return number of surrounding mine as Text
	private String getText() {
		if(this.mine == true) {
			return "";
		} else {
			return numSurroundingMines == 0 ? "" : String.format("%d", numSurroundingMines);
		}
	}

	public boolean isMine() {
		return mine;
	}
	public int getNumSurroundingMines() {
		return numSurroundingMines;
	}
	public boolean isRevealable() {
		return !visible;
	}
}
