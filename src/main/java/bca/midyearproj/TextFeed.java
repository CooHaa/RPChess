package bca.midyearproj;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextFeed extends ScrollPane {

    private static Color FEED_BLACK = Color.rgb(15, 21, 30);

    private List<Label> messages = new ArrayList<>();
    private VBox chatbox = new VBox(5);
    
    public TextFeed() {
        setPrefSize(Square.TILE_SIZE * 6, Square.TILE_SIZE * 4);
        setContent(chatbox);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        getStyleClass().add("edge-to-edge");
        vvalueProperty().bind(chatbox.heightProperty());
        chatbox.setBackground(new Background(new BackgroundFill(FEED_BLACK, null, null)));
        chatbox.setMinSize(Square.TILE_SIZE * 6, Square.TILE_SIZE * 4);
        chatbox.setMaxWidth(Square.TILE_SIZE * 6);

        Label init = new Label("Let the game begin!");
        init.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 15));
        init.setTextFill(Color.WHITE);
        init.setPrefWidth(Square.TILE_SIZE * 6);
        init.setAlignment(Pos.CENTER);
        init.setPadding(new Insets(2));
        chatbox.getChildren().add(init);

        printWhiteTurn();

    }

    public void print(String msg, Pos pos, FontWeight weight, Color color, int size) {
        Label text = new Label(msg);
        text.setFont(Font.font("Leelawdee UI Semilight", weight, size));
        text.setTextFill(color);        
        text.setAlignment(pos);
        text.setWrapText(true);
        text.setPadding(new Insets(0, 2, 0, 2));
        text.setPrefWidth(Square.TILE_SIZE * 6);
        messages.add(text);

        chatbox.getChildren().add(text);
    }

    public void printWhiteTurn() {
        print("White's Turn:", Pos.CENTER_LEFT, FontWeight.BOLD, Square.TILE_GREEN_LIGHT, 15);
    }

    public void printWhiteAction(String action)  {
        print(action, Pos.CENTER_LEFT, FontWeight.THIN, Square.TILE_GREEN_LIGHT, 10);
    }

    public void printBlackTurn() {
        print("Black's Turn:", Pos.CENTER_RIGHT, FontWeight.BOLD, Square.TILE_GREEN_DARK, 15);
    }

    public void printBlackAction(String action)  {
        print(action, Pos.CENTER_RIGHT, FontWeight.THIN, Square.TILE_GREEN_DARK, 10);
    }

    public void printAction(String action, boolean white) {
        if (white) printWhiteAction(action);
        else printBlackAction(action);
    }
    
}
