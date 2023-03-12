package bca.midyearproj;

import java.io.File;

import javafx.scene.layout.HBox;

/**
 * The class that represents the entire game as a whole. More than being anything functional on its own, it serves
 * as a connector between the various components of the game, so that they can interact with each other. (i.e, updating the UI when
 * a piece is selected on the chessboard, highlighting the viable spaces of a move when a skill is clicked in the menu, etc.)
 */
public class Game extends HBox {

    private Chessboard chessboard;
    private SideUI sideui;

    public Game(File boardPattern) {
        chessboard = new Chessboard(boardPattern);
        sideui = new SideUI();
        getChildren().addAll(chessboard, sideui);
        chessboard.linkSideUi(sideui);
        sideui.linkChessboard(chessboard);
    }

        
}
