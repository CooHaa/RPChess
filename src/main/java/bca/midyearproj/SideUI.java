package bca.midyearproj;

import javafx.scene.layout.VBox;

public class SideUI extends VBox {

    private PieceMenu menu;
    private TextFeed feed;

    private Chessboard chessboard;

    public SideUI() {
        menu = new PieceMenu();
        feed = new TextFeed();
        getChildren().addAll(menu, feed);
    }
    
    public void linkChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public PieceMenu getMenu() {
        return menu;
    }

    public TextFeed getFeed() {
        return feed;
    }

}
