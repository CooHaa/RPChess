package bca.midyearproj;

import bca.midyearproj.Pieces.Piece;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Square extends StackPane {

    public static final int TILE_SIZE = 64;
    public static final Color TILE_GREEN_LIGHT = Color.rgb(210, 244, 173);
    public static final Color TILE_GREEN_DARK = Color.rgb(184, 212, 132);
    public static final Color SELECTED_LIGHT = Color.rgb(192, 209, 209);
    public static final Color SELECTED_DARK = Color.rgb(158, 172, 172);
    public static final Color ATTACK_LIGHT = Color.rgb(245, 194, 174);
    public static final Color ATTACK_DARK = Color.rgb(212, 143, 131);
    public static final Color AMBUSH_LIGHT = Color.rgb(235, 245, 174);
    public static final Color AMBUSH_DARK = Color.rgb(211, 212, 131);
    public static final Color AOE_LIGHT = Color.rgb(235, 134, 94);
    public static final Color AOE_DARK = Color.rgb(193, 93, 75);
    public static final Color UNAVAILABLE_LIGHT = Color.rgb(209, 209, 209);
    public static final Color UNAVAILABLE_DARK = Color.rgb(171, 171, 171);
    public static final Color SPELL_LIGHT = Color.rgb(255, 164, 206);
    public static final Color SPELL_DARK = Color.rgb(255, 188, 214);

    private boolean hasPiece;
    private Piece piece;
    private int row;
    private int col;
    private Color color;


    public Square(int row, int col) {
        hasPiece = false;
        piece = null;
        this.row = row;
        this.col = col;
        if ((row + col) % 2 == 0) {
            this.setBackground(new Background(new BackgroundFill(TILE_GREEN_DARK, null, null)));
            color = TILE_GREEN_DARK;
        }
        else {
            this.setBackground(new Background(new BackgroundFill(TILE_GREEN_LIGHT, null, null)));
            color = TILE_GREEN_LIGHT;
        }
        this.setPrefSize(TILE_SIZE, TILE_SIZE);
    }

    public boolean hasPiece() {
        return hasPiece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void holdPiece(Piece piece) {
        this.piece = piece;
        hasPiece = true;
        this.getChildren().add(piece);
    }

    public Piece releasePiece() {
        this.getChildren().remove(piece);
        Piece returnPiece = this.piece;
        this.piece = null;
        hasPiece = false;
        return returnPiece;
    }
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public void selectColorChange() {
        if (color.equals(TILE_GREEN_LIGHT)) setColor(SELECTED_LIGHT);
        else if (color.equals(TILE_GREEN_DARK)) setColor(SELECTED_DARK);
    }

    public void deselectColorChange() {
        if (color.equals(SELECTED_LIGHT) || color.equals(ATTACK_LIGHT) || color.equals(AMBUSH_LIGHT) || color.equals(AOE_LIGHT) || color.equals(UNAVAILABLE_LIGHT) || color.equals(SPELL_LIGHT)) setColor(TILE_GREEN_LIGHT);
        else if (color.equals(SELECTED_DARK) || color.equals(ATTACK_DARK) || color.equals(AMBUSH_DARK) || color.equals(AOE_DARK) || color.equals(UNAVAILABLE_DARK) || color.equals(SPELL_DARK)) setColor(TILE_GREEN_DARK);
    }

    public void attackColorChange() {
        if (color.equals(TILE_GREEN_LIGHT)) setColor(ATTACK_LIGHT);
        else if (color.equals(TILE_GREEN_DARK)) setColor(ATTACK_DARK);
    }

    public void aoeColorChange() {
        if (color.equals(TILE_GREEN_LIGHT)) setColor(AOE_LIGHT);
        else if (color.equals(TILE_GREEN_DARK)) setColor(AOE_DARK);
    }

    public void ambushColorChange() {
        if (color.equals(TILE_GREEN_LIGHT)) setColor(AMBUSH_LIGHT);
        else if (color.equals(TILE_GREEN_DARK)) setColor(AMBUSH_DARK);
    }

    public void unavailableColorChange() {
        if (color.equals(TILE_GREEN_LIGHT)) setColor(UNAVAILABLE_LIGHT);
        else if (color.equals(TILE_GREEN_DARK)) setColor(UNAVAILABLE_DARK);
    }

    public void spellColorChange() {
        if (color.equals(TILE_GREEN_LIGHT)) setColor(SPELL_LIGHT);
        else if (color.equals(TILE_GREEN_DARK)) setColor(SPELL_DARK);
    }

    @Override
    public String toString() {
        String[] arr = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String file = arr[col];
        int rank = 8 - row;
        return file + rank + "";
    }

    
}
