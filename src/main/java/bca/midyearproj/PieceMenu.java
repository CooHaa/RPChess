package bca.midyearproj;

import bca.midyearproj.Pieces.Piece;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.LastResort;
import bca.midyearproj.Skills.Skill;
import bca.midyearproj.Skills.Spell;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PieceMenu extends BorderPane {

    // Colors, backgrounds, displays
    private static final Color BACKGROUND = Color.rgb(17, 19, 23);
    private static final Background BACKGROUND_BACKGROUND = new Background(new BackgroundFill(BACKGROUND, null, null));
    private static final Color BODY = Color.rgb(9, 32, 58);
    private static final Background BODY_BACKGROUND = new Background(new BackgroundFill(BODY, null, null));
    private static final Color OUTLINE = Color.rgb(9, 74, 122);
    private static final Border OUTLINE_BORDER = new Border(new BorderStroke(OUTLINE, BorderStrokeStyle.SOLID, null, new BorderWidths(10)));
    private static final Color DISPLAY = Color.rgb(91, 171, 231);
    private static final Background DISPLAY_BACKGROUND = new Background(new BackgroundFill(DISPLAY, null, null));
    private static final Color TEXT = Color.rgb(206, 235, 249);
    private static final Background HIGHLIGHT_BACKGROUND = new Background(new BackgroundFill(TEXT, new CornerRadii(0.1), null));

    // Instances for header
    private HBox header;
    private StackPane pieceDisplay;

    // Instance for skill list
    private VBox skillList;

    // Instance for description box
    private VBox descBox;

    // Instance for status effect box
    private VBox statusEffects;

    // Tracks which piece is selected
    private Piece selectedPiece;

    // Tracks which skill is selected
    private Skill selectedSkill;
    private Pane selectedSkillLabel;
    

    public PieceMenu() {
        this.setBackground(BACKGROUND_BACKGROUND);
        this.setPrefSize(Square.TILE_SIZE * 6, Square.TILE_SIZE * 5);
        selectedPiece = null;
        selectedSkill = null;
        selectedSkillLabel = null;
        constructMenu();
    }


    /**
     * Initial construction of the piece menu. Should only be called once on creation
     */
    public void constructMenu() {
        constructHeader();
        constructSkillList();
        constructDescriptionBox();
        constructBorder();
    }

    /**
     * Updates the piece menu to show information about a certain piece.
     * @param piece
     */
    public void updateMenu(Piece piece) {
        selectedPiece = piece;
        updateHeader(piece);
        updateSkillList(piece);
        updateDescriptionBox(piece.getDesc());
    }

    /**
     * Reverts menu to its default state
     */
    public void revertMenu() {
        selectedPiece = null;
        revertHeader();
        revertSkillList();
        revertDescriptionBox();
    }

    /**
     * Initial creation of header, with no information on it
     */
    public void constructHeader() {
        
        // Header body
        header = new HBox(10);
        header.setPrefHeight(Square.TILE_SIZE + 10);
        header.setPadding(new Insets(10));
        header.setBackground(BODY_BACKGROUND);
        header.setBorder(new Border(new BorderStroke(OUTLINE, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 10, 0))));
        header.setAlignment(Pos.CENTER);

        // Piece graphic background
        pieceDisplay = new StackPane();
        pieceDisplay.setPrefSize(40, 40);
        pieceDisplay.setBackground(DISPLAY_BACKGROUND);
        
        // Filler text
        Text text = new Text("Select a piece...");
        text.setFont(Font.font("Leelawadee UI Semilight", FontWeight.EXTRA_BOLD, 30));
        text.setFill(TEXT);
        
        // Spacer
        Pane spacer = new Pane();

        header.getChildren().addAll(pieceDisplay, text, spacer);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        BorderPane.setMargin(header, new Insets(0, 0, 5, 0));
        setTop(header);

    }

    /**
     * Updates header with information regarding a specific piece
     * @param piece
     */
    public void updateHeader(Piece piece) {

        // Clear filler
        header.getChildren().clear();

        // Piece graphic background
        pieceDisplay = new StackPane();
        pieceDisplay.setPrefSize(40, 40);
        pieceDisplay.setBackground(DISPLAY_BACKGROUND);

        // Piece graphic
        ImageView pieceGraphic = new ImageView(piece.getImage());
        pieceGraphic.setFitWidth(35);
        pieceGraphic.setPreserveRatio(true);
        pieceGraphic.setSmooth(true);
        pieceDisplay.getChildren().add(pieceGraphic);

        // Piece name
        Text pieceName = new Text(piece.toString());
        pieceName.setFont(Font.font("Leelawadee UI Semilight", FontWeight.EXTRA_BOLD, 30));
        pieceName.setFill(TEXT);

        // Piece HP
        Text pieceHP = new Text(piece.displayHP());
        pieceHP.setFont(Font.font("Leelawdee UI Semilight", FontWeight.NORMAL, FontPosture.ITALIC, 20));
        pieceHP.setFill(DISPLAY);

        // Spacer
        Pane spacer = new Pane();

        header.getChildren().addAll(pieceDisplay, pieceName, spacer, pieceHP);
        HBox.setHgrow(spacer, Priority.ALWAYS);

    }

    /**
     * Restores header to default state
     */
    public void revertHeader() {

        // Remove piece name, spacer, HP
        header.getChildren().clear();

        // Piece graphic background
        pieceDisplay = new StackPane();
        pieceDisplay.setPrefSize(40, 40);
        pieceDisplay.setBackground(DISPLAY_BACKGROUND);

        // Filler text
        Text text = new Text("Select a piece...");
        text.setFont(Font.font("Leelawadee UI Semilight", FontWeight.EXTRA_BOLD, 30));
        text.setFill(TEXT);
        
        // Spacer
        Pane spacer = new Pane();

        header.getChildren().addAll(pieceDisplay, text, spacer);
        HBox.setHgrow(spacer, Priority.ALWAYS);



    }

    /**
     * Initial construction of skill list, with no information
     */
    public void constructSkillList() {

        // List body
        skillList = new VBox(10);
        skillList.setPrefSize(Square.TILE_SIZE * 2.2, Square.TILE_SIZE * 3);
        skillList.setPadding(new Insets(10));
        skillList.setBackground(BODY_BACKGROUND);
        skillList.setBorder(OUTLINE_BORDER);

        // Title text
        Text title = new Text("Skill List");
        title.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 20));
        title.setFill(TEXT);
        skillList.getChildren().add(title);

        BorderPane.setMargin(skillList, new Insets(5, 5, 10, 10));
        setLeft(skillList);

    }

    /**
     * Updates skill list with information about a specific piece
     * @param piece
     */
    public void updateSkillList(Piece piece) {

        // Skill names
        for (Skill skill : piece.getSkillList()) {

            Label skillName = new Label(skill.toString());
            skillName.setFont(Font.font("Leelawdee UI Semilight", FontWeight.NORMAL, 15));
            skillName.setTextFill(DISPLAY);
            skillName.setWrapText(true);

            Pane skillLabel = new Pane(skillName);
            skillLabel.setBackground(BODY_BACKGROUND);
            skillLabel.setPadding(new Insets(2));
            skillLabel.setPrefWidth(Square.TILE_SIZE * 2);
            skillLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {

                    // Only function if its the attack turn
                    if (!getChessboard().moveTurn()) { 

                        // If no skill is selected, select
                        if (selectedSkill == null) {
                            skillLabel.setBackground(HIGHLIGHT_BACKGROUND);
                            selectedSkillLabel = skillLabel;
                            selectedSkill = skill;
                            revertDescriptionBox();
                            updateDescriptionBox(skill.getDesc());
                            // Board highlighting preview algorithm
                            for (Square attackSpace : skill.runAlgorithm(piece.getSpace(), getChessboard().getInternalBoard())) {
                                // If the skill is an attack
                                if (skill instanceof Attack) {
                                    Attack attack = (Attack) skill;
                                    if (attack.aoe()) attackSpace.aoeColorChange();
                                    else {        
                                        if (attackSpace.hasPiece() && (attackSpace.getPiece().isLight() != getChessboard().playerTurn())) attackSpace.attackColorChange();
                                        else attackSpace.unavailableColorChange();
                                    }
                                }
                                // If the skill is a spell
                                else if (skill instanceof Spell) {
                                    attackSpace.spellColorChange();
                                }
                                // If the skill is Last Resort
                                else if (skill instanceof LastResort) {
                                    attackSpace.attackColorChange();
                                }
                            }
                        }
                        // If a skill is selected and the click is on this specific skill, deselect
                        else if ((selectedSkill != null) && (selectedSkillLabel == skillLabel)) {
                            skillLabel.setBackground(BODY_BACKGROUND);
                            selectedSkillLabel = null;
                            selectedSkill = null;
                            revertDescriptionBox();
                            updateDescriptionBox(piece.getDesc());
                            getChessboard().deselectAll();
                            getChessboard().setSelectedPiece(piece);
                            piece.getSpace().selectColorChange();
                            
                        }
                        // If a skill is selected and the click is on a different skill, select that one
                        else if ((selectedSkill != null) && (selectedSkillLabel != skillLabel)) {
                            selectedSkillLabel.setBackground(BODY_BACKGROUND);
                            skillLabel.setBackground(HIGHLIGHT_BACKGROUND);
                            selectedSkillLabel = skillLabel;
                            selectedSkill = skill;
                            revertDescriptionBox();
                            updateDescriptionBox(skill.getDesc());
                            getChessboard().deselectAll();
                            getChessboard().setSelectedPiece(piece);
                            piece.getSpace().selectColorChange();
                            // Board highlighting preview algorithm
                            for (Square attackSpace : skill.runAlgorithm(piece.getSpace(), getChessboard().getInternalBoard())) {
                                // If the skill is an attack
                                if (skill instanceof Attack) {
                                    Attack attack = (Attack) skill;
                                    if (attack.aoe()) attackSpace.aoeColorChange();
                                    else {
                                        if (attackSpace.hasPiece() && (attackSpace.getPiece().isLight() != getChessboard().playerTurn())) attackSpace.attackColorChange();
                                        else attackSpace.unavailableColorChange();
                                    }
                                }
                                // If the skill is a spell
                                else if (skill instanceof Spell) {
                                    attackSpace.spellColorChange();
                                }
                                // If the skill is Last Resort
                                else if (skill instanceof LastResort) {
                                    attackSpace.attackColorChange();
                                }
                            }

                        }

                    }
                    
                }

            });

            skillList.getChildren().add(skillLabel);
        }

    }

    /**
     * Reverts skill list to default state
     */
    public void revertSkillList() {

        // Clear all contents (easier than clearing all children except first, the title)
        skillList.getChildren().clear();

        // Title text
        Text title = new Text("Skill List");
        title.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 20));
        title.setFill(TEXT);
        skillList.getChildren().add(title);

    }

    /**
     * Initial construction of description box with no information
     */
    public void constructDescriptionBox() {

        // Textbox body
        descBox = new VBox(10);
        descBox.setPrefHeight(Square.TILE_SIZE * 3);
        descBox.setPadding(new Insets(10));
        descBox.setBackground(BODY_BACKGROUND);
        descBox.setBorder(OUTLINE_BORDER);

        // Title text
        Text title = new Text("Description");
        title.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 20));
        title.setFill(TEXT);

        descBox.getChildren().add(title);
        BorderPane.setMargin(descBox, new Insets(5, 10, 10, 5));

        setCenter(descBox);

    }

    /**
     * Updates description box with information
     * @param desc
     */
    public void updateDescriptionBox(String desc) {

        // Description text
        Label descText = new Label(desc);
        descText.setFont(Font.font("Leelawdee UI Semilight", FontWeight.NORMAL, 10));
        descText.setTextFill(DISPLAY);
        descText.setWrapText(true);

        descBox.getChildren().add(descText);

    }

    /**
     * Reverts description box to default state
     */
    public void revertDescriptionBox() {

        descBox.getChildren().clear();

        // Title text
        Text title = new Text("Description");
        title.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 20));
        title.setFill(TEXT);

        descBox.getChildren().add(title);

    }

    public void constructBorder() {
        Pane border = new Pane();
        border.setPrefHeight(10);
        border.setBackground(new Background(new BackgroundFill(OUTLINE, null, null)));

        setBottom(border);
    }

    /**
     * Constructs status effects box, features currently unimplemented
     */
    public void constructStatusEffects() {
        
        // Effects body
        statusEffects = new VBox(5);
        statusEffects.setPrefHeight(Square.TILE_SIZE + 10);
        statusEffects.setPadding(new Insets(10));
        statusEffects.setBackground(BODY_BACKGROUND);
        statusEffects.setBorder(new Border(new BorderStroke(OUTLINE, BorderStrokeStyle.SOLID, null, new BorderWidths(10, 0, 0 ,0))));
        statusEffects.setAlignment(Pos.CENTER_LEFT);

        // Title text
        Text title = new Text("Status Effects");
        title.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 15));
        title.setFill(TEXT);

        // Status text
        Text desc = new Text("This feature is currently unimplemented");
        desc.setFont(Font.font("Leelawdee UI Semilight", FontWeight.NORMAL, 10));
        desc.setFill(DISPLAY);

        statusEffects.getChildren().addAll(title, desc);

        setBottom(statusEffects);

    }

    public void updateStatusEffects(Piece piece) {

    }

    public void revertStatusEffects() {

    }

    public Chessboard getChessboard() {
        return ((SideUI) getParent()).getChessboard();
    }

    public Skill getSelectedSkill() {
        return selectedSkill;
    }

    public void clearSelectedSkill() {
        selectedSkill = null;
    }
    
}
