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
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;


// Tiles to place on game board
public class Tile extends StackPane {
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Reveal this tile, display numSurroundingMines if it's a safe tile,
	// display bomb image if is a mine
	public void reveal() {
		visible = true;
		txt.setText(getText());
		txt.setVisible(true);
		if(isMine()) {
			Image img = new Image(getClass().getResourceAsStream("images/bomb.png"), 18, 18, false, false);
			rect.setFill(new ImagePattern(img));
		} else {
			rect.setFill(Color.WHITE);
		}
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Return number of surrounding mine as Text
	private String getText() {
		if(this.mine == true) {
			return "";
		} else {
			return numSurroundingMines == 0 ? "" : String.format("%d", numSurroundingMines);
		}
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Return true if this tile is a mine
	public boolean isMine() {
		return mine;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Return number of surrounding mines
	public int getNumSurroundingMines() {
		return numSurroundingMines;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Return true if this tile is revealable
	public boolean isRevealable() {
		return !visible;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Flag this tile
	public void flag() {
		flag = true;
		Image img = new Image(getClass().getResourceAsStream("images/flag.png"), 18, 18, false, false);
		rect.setFill(new ImagePattern(img));
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Remove flag
	public void deflag() {
		flag = false;
		rect.setFill(Color.GRAY);
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Return true if this tile is flagged
	public boolean isFlagged() {
		return flag;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Return true is check is needed for this tile
	public boolean needCheck() {
		return check;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Skip this tile on next check
	public void skip() {
		check = false;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
