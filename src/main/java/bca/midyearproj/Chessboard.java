package bca.midyearproj;

import javafx.event.EventHandler;
import javafx.event.EventTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import bca.midyearproj.Pieces.*;
import bca.midyearproj.Skills.Attack;
import bca.midyearproj.Skills.LastResort;
import bca.midyearproj.Skills.Spell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Chessboard extends VBox {
    
    // Constants
    private Color BORDER_GREEN = Color.rgb(112, 144, 51);
    private Color SKIP_TEXT = Color.rgb(68, 92, 21);
    private Color SKIP_BACKGROUND = Color.rgb(89, 120, 28);
    
    // Internal representation
    private Square[][] internalBoard;
    private Piece selectedPiece;
    private boolean player1Turn;
    private boolean moveTurn;
    private boolean game;

    // Visual board
    private GridPane board;

    // Bottom bar
    private HBox bar;

    // Side UI access
    private SideUI sideui;

    public Chessboard(File boardPattern) {

        internalBoard = new Square[8][8];
        selectedPiece = null;
        player1Turn = true;
        moveTurn = true;
        game = true;

        board = new GridPane();

        bar = new HBox();

        // Visual construction, internal board setup
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = new Square(row, col);
                board.add(square, col, row);
                internalBoard[row][col] = square;
            }
        }

        // Populating board
        setPieces(boardPattern);

        // Outline
        board.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(Square.TILE_SIZE / 2, Square.TILE_SIZE / 2, 0, Square.TILE_SIZE / 2))));

        // Bar setup
        bar.setPrefHeight(Square.TILE_SIZE / 2);
        bar.setBackground(new Background(new BackgroundFill(BORDER_GREEN, null, null)));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 0, Square.TILE_SIZE / 2));

        // Adding board and bar to game
        getChildren().addAll(board, bar);

        // Adding handler for click events on chessboard
        setOnMouseClicked(clickEvent);
    }

    /**
     * Helper method to associate the instance variable sideUI to the sideUI in game. This is run at the initialization of the Game class,
     * which allows this class to access and modify the sideUI from a click handler defined in this class instead of the Game class.
     * @param sideui
     */
    public void linkSideUi(SideUI sideui) {
        this.sideui = sideui;
    }

    /**
     * Helper function to translate board setup .txt to board pattern
     * @param boardPattern
     */
    public void setPieces(File boardPattern) {
        try {
            Scanner scannedFile = new Scanner(boardPattern);
            int rowCount = 0;
            while (scannedFile.hasNextLine()) {
                String patternRow = scannedFile.nextLine();
                for (int colCount = 0; colCount < patternRow.length(); colCount++) {
                    Square square = internalBoard[rowCount][colCount];
                    switch (patternRow.charAt(colCount)) {
                        case 'r': 
                            square.holdPiece(new Rook(false));
                            break;
                        case 'n':
                            square.holdPiece(new Knight(false));
                            break;
                        case 'b':
                            square.holdPiece(new Bishop(false));
                            break;
                        case 'q':
                            square.holdPiece(new Queen(false));
                            break;
                        case 'k':
                            square.holdPiece(new King(false));
                            break;
                        case 'p':
                            square.holdPiece(new Pawn(false));
                            break;
                        case 'R': 
                            square.holdPiece(new Rook(true));
                            break;
                        case 'N':
                            square.holdPiece(new Knight(true));
                            break;  
                        case 'B':
                            square.holdPiece(new Bishop(true));
                            break;
                        case 'Q':
                            square.holdPiece(new Queen(true));
                            break;
                        case 'K':
                            square.holdPiece(new King(true));
                            break;
                        case 'P':
                            square.holdPiece(new Pawn(true));
                            break;
                        case 'e':
                            break;
                    }
                }
                rowCount++;
            }
            scannedFile.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File was not found. Try checking the filename in the code.");
        }
        
    }

    public void showSkipButton() {
        Label text = new Label(" Pass Turn ");
        text.setFont(Font.font("Leelawdee UI Semilight", FontWeight.BOLD, 15));
        text.setTextFill(SKIP_TEXT);
        text.setBackground(new Background(new BackgroundFill(SKIP_BACKGROUND, null, null)));

        text.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("TURN SKIPPED");
                deselectAll();
                switchPlayer();
                switchTurn();
                removeSkipButton();
                sideui.getMenu().revertMenu();
            }
            
        });

        bar.getChildren().addAll(text);
    }

    public void removeSkipButton() {
        bar.getChildren().clear();
    }

    public void switchPlayer() {
        player1Turn = !player1Turn;
        if (player1Turn) sideui.getFeed().printWhiteTurn();
        else sideui.getFeed().printBlackTurn();
        sideui.getMenu().clearSelectedSkill();
    }

    public boolean playerTurn() {
        return player1Turn;
    }

    public void switchTurn() {
        moveTurn = !moveTurn;
        System.out.println("TURN SWITCH");
    }

    public boolean moveTurn() {
        return moveTurn;
    }

    public void deselectAll() {
        selectedPiece = null;
        for (Square[] row : internalBoard) {
            for (Square square : row) {
                square.deselectColorChange();
            }
        }
    }

    public void killPiece(Piece piece) {
        Square tile = (Square) piece.getParent();
        tile.releasePiece();
        if (piece instanceof King) {
            setOnMouseClicked(null);
            King king = (King) piece;
            String player = king.isLight() ? "White" : "Black";
            sideui.getFeed().printAction("The " + king.toString() + " has been slain. " + player + " wins!", player1Turn);
            game = false;
        }
    }
    
    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
    }

    public Square[][] getInternalBoard() {
        return internalBoard;
    }

    /**
     * Handler for when a click is registered
     */
    EventHandler<MouseEvent> clickEvent = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            EventTarget target = event.getTarget();

            if (game) {
                // MOVE TURN
                if (moveTurn()) {

                    System.out.println("m1");
                    
                    // Handle selecting a new piece
                    if (getSelectedPiece() == null) {

                        // If the click is on a square
                        if (target instanceof Square) {
                            Square square = (Square) target;

                            // Run select process only if the square has a piece and piece is player color
                            if (square.hasPiece() && (square.getPiece().isLight() == playerTurn())) {
                                Piece piece = square.getPiece();
                                setSelectedPiece(piece);
                                sideui.getMenu().updateMenu(piece);
                                for (Square moveSpace : piece.getPossibleMoves(getInternalBoard())) moveSpace.selectColorChange();
                                for (Square attackSpace : piece.getAmbushMoves(getInternalBoard())) attackSpace.ambushColorChange();
                            }
                        }

                        // If the click is on a piece
                        else if (target instanceof Piece) {
                            Piece piece = (Piece) target;
                            // Run select process only if the piece is player color
                            if (piece.isLight() == playerTurn()) {
                                setSelectedPiece(piece);
                                sideui.getMenu().updateMenu(piece);
                                for (Square moveSpace : piece.getPossibleMoves(getInternalBoard())) moveSpace.selectColorChange();
                                for (Square attackSpace : piece.getAmbushMoves(getInternalBoard())) attackSpace.ambushColorChange();
                            }
                        }


                    }

                    // Handle second action after selecting a piece (selectedPiece is not null)
                    else {

                        System.out.println("m2");

                        // If square is clicked
                        if (target instanceof Square) {
                            Square destination = (Square) target;
                            Square origin = getSelectedPiece().getSpace();
                            // If the same space is picked, deselect all
                            if (destination == origin) {
                                deselectAll();
                                sideui.getMenu().revertMenu();
                            }

                            else if (origin.getPiece().getAmbushMoves(internalBoard).contains(destination)) {
                                destination.getPiece().takeDamage(5);
                                sideui.getFeed().printAction(getSelectedPiece().toString() + " ambushes " + destination.getPiece().toString() + ", dealing 5 damage.", player1Turn);

                                deselectAll();
                                switchTurn();
                                showSkipButton();
                                sideui.getMenu().revertMenu();
                            }

                            // Only run routine if the destination is part of the possible moves, and doesn't have a piece on it
                            else if (!destination.hasPiece() && origin.getPiece().getPossibleMoves(internalBoard).contains(destination)) {
                                origin.releasePiece();
                                destination.holdPiece(getSelectedPiece());
                                sideui.getFeed().printAction(getSelectedPiece().toString() + " moves to " + destination.toString() + ".", player1Turn);

                                // If placed piece was a pawn, prevent two space movements after this
                                if (getSelectedPiece() instanceof Pawn) {
                                    ((Pawn) getSelectedPiece()).firstMove();
                                }

                                deselectAll();
                                switchTurn();
                                showSkipButton();
                                sideui.getMenu().revertMenu();
                            }
                        }

                        // If piece is clicked
                        else if (target instanceof Piece) {
                            Piece targetPiece = (Piece) target;
                            Square origin = getSelectedPiece().getSpace();
                            Square destination = (Square) targetPiece.getParent();
                            // Run deselect routine if the piece is the same piece as the selected piece
                            if (targetPiece == getSelectedPiece()) {
                                deselectAll();
                                sideui.getMenu().revertMenu();
                            }
                            
                            // If targetspace is part of the ambush spaces, deal ambush damage
                            else if (origin.getPiece().getAmbushMoves(internalBoard).contains(destination)) {
                                targetPiece.takeDamage(5);
                                sideui.getFeed().printAction(getSelectedPiece().toString() + " ambushes " + targetPiece.toString() + ", dealing 5 damage.", player1Turn);

                                deselectAll();
                                switchTurn();
                                showSkipButton();
                                sideui.getMenu().revertMenu();
                            }
                        }
                    }

                }

                // ATTACK TURN
                else {

                    System.out.println("a1");

                    // Handle selecting a new piece
                    if (getSelectedPiece() == null) {

                        System.out.println("null piece");
                        
                        // If the click is on a square
                        if (target instanceof Square) {
                            Square square = (Square) target;

                            // Only run select process if the square has a piece of the current player's color
                            if (square.hasPiece() && (square.getPiece().isLight() == playerTurn())) {
                                Piece piece = square.getPiece();
                                setSelectedPiece(piece);
                                sideui.getMenu().updateMenu(piece);
                                square.selectColorChange();
                            }
                        }

                        // If the click is on a piece
                        else if (target instanceof Piece) {
                            Piece piece = (Piece) target;
                            Square square = piece.getSpace();

                            // Only run select process if the piece is the player turn color
                            if (piece.isLight() == playerTurn()) {
                                setSelectedPiece(piece);
                                sideui.getMenu().updateMenu(piece);
                                square.selectColorChange();
                            }
                        }
                    }

                    // Handle second action after selecting piece
                    else {

                        System.out.println("a2");
                        
                        // If square is clicked
                        if (target instanceof Square) {

                            System.out.println("s0");

                            Square destination = (Square) target;
                            Square origin = getSelectedPiece().getSpace();
                            // If the same space is picked, deselect all
                            if (destination == origin) {
                                deselectAll();
                                sideui.getMenu().revertMenu();
                            }

                            // Attack handler
                            if (sideui.getMenu().getSelectedSkill() instanceof Attack) {
                                Attack attack = (Attack) sideui.getMenu().getSelectedSkill();
                                // Aoe attack handler
                                if (attack.aoe()) {

                                    // Check if it can be used (only activate if there's an enemy in the area)
                                    boolean canActivate = false;
                                    for (Square space : attack.runAlgorithm(origin, internalBoard)) {
                                        if (space.hasPiece() && (space.getPiece().isLight() != playerTurn())) canActivate = true;
                                    }

                                    if (canActivate) {
                                        // If a highlighted space is clicked
                                        if (attack.runAlgorithm(origin, internalBoard).contains(destination)) {
                                            sideui.getFeed().printAction(getSelectedPiece().toString() + " uses " + attack.toString() + ", dealing " + attack.getVal() + " to every enemy in the area!", player1Turn);
                                            // Iterate through every square in the attack algorithm
                                            for (Square space : attack.runAlgorithm(origin, internalBoard)) {
                                                if (space.hasPiece() && (space.getPiece().isLight() != playerTurn())) {
                                                    Piece targetPiece = space.getPiece(); 
                                                    targetPiece.takeDamage(attack.getVal());
                                                    if (targetPiece.getCurrentHP() <= 0) {
                                                        killPiece(targetPiece);
                                                        sideui.getFeed().printAction(targetPiece.toString() + " has been slain.", player1Turn);
                                                    }
                                                }
                                            }

                                            deselectAll();
                                            switchPlayer();
                                            switchTurn();
                                            removeSkipButton();
                                            sideui.getMenu().revertMenu(); 
                                        }
                                    }
                                    
                                }
                                // Regular attack handler
                                else {
                                    // If the destination has a piece, the piece is a different color, and the space is highlighted
                                    if (destination.hasPiece() && (destination.getPiece().isLight() != playerTurn()) && attack.runAlgorithm(origin, internalBoard).contains(destination)) {
                                        Piece targetPiece = destination.getPiece();
                                        sideui.getFeed().printAction(getSelectedPiece().toString() + " uses " + attack.toString() + " on " + targetPiece.toString() + ", dealing " + attack.getVal() + " damage.", player1Turn);
                                        targetPiece.takeDamage(sideui.getMenu().getSelectedSkill().getVal());
                                        if (targetPiece.getCurrentHP() <= 0) {
                                            killPiece(targetPiece);
                                            sideui.getFeed().printAction(targetPiece.toString() + " has been slain.", player1Turn);
                                        }

            
                                        deselectAll();
                                        switchPlayer();
                                        switchTurn();
                                        removeSkipButton();
                                        sideui.getMenu().revertMenu();
                                    }
                                }
                            }

                            // Spell handler
                            else if (sideui.getMenu().getSelectedSkill() instanceof Spell) {
                                Spell spell = (Spell) sideui.getMenu().getSelectedSkill();

                                // Spell algorithms should specify only spaces with allies in their algorithms, so no need for rigorous checking here
                                if (spell.runAlgorithm(origin, internalBoard).contains(destination)) {
                                    Piece targetPiece = destination.getPiece();
                                    // Only heal if its possible
                                    if (targetPiece.getCurrentHP() != targetPiece.getMaxHP()) {
                                        targetPiece.heal(spell.getVal());
                                        sideui.getFeed().printAction(getSelectedPiece() + " heals " + targetPiece.toString() + " for " + spell.getVal() + " health. " + targetPiece.toString() + " is now at " + targetPiece.getCurrentHP() + " HP.", player1Turn);

                                        deselectAll();
                                        switchPlayer();
                                        switchTurn();
                                        removeSkipButton();
                                        sideui.getMenu().revertMenu();
                                    }
                                }
                            }

                            // Last resort handler
                            else if (sideui.getMenu().getSelectedSkill() instanceof LastResort) {
                                LastResort lastResort = (LastResort) sideui.getMenu().getSelectedSkill();

                                if (lastResort.runAlgorithm(origin, internalBoard).contains(destination)) {

                                    // Finding the king that casted the spell
                                    King king = null;
                                    for (Square[] row : internalBoard) {
                                        for (Square space : row) {
                                            if (space.hasPiece() && (space.getPiece() instanceof King) && (space.getPiece().isLight() == playerTurn())) {
                                                king = (King) space.getPiece();
                                            }
                                        }
                                    }

                                    // Only run command if the king can heal any HP
                                    if (king.getCurrentHP() != king.getMaxHP()) {
                                        Piece sacrifice = destination.getPiece();
                                        int HP = sacrifice.getCurrentHP();
                                        killPiece(sacrifice);
                                        king.heal(HP);
                                        sideui.getFeed().printAction(king.toString() + " uses the Last Resort, and sacrifices " + sacrifice.toString() + "! " + king.toString() + " heals to " + king.getCurrentHP() + " HP.", player1Turn);
                                            
                                        deselectAll();
                                        switchPlayer();
                                        switchTurn();
                                        removeSkipButton();
                                        sideui.getMenu().revertMenu();
                                    }

                                }
                            }
                            
                        }

                        // If piece is clicked
                        else if (target instanceof Piece) {
                            Piece targetPiece = (Piece) target;
                            Square destination = (Square) targetPiece.getParent();
                            Square origin = getSelectedPiece().getSpace();
                            // Deselect if same piece is clicked
                            if (targetPiece == getSelectedPiece()) {
                                deselectAll();
                                sideui.getMenu().revertMenu();
                            }

                            // Attack handler
                            if (sideui.getMenu().getSelectedSkill() instanceof Attack) {
                                Attack attack = (Attack) sideui.getMenu().getSelectedSkill();
                                // Aoe attack handler
                                if (attack.aoe()) {

                                    // Check if it can be used (only activate if there's an enemy in the area)
                                    boolean canActivate = false;
                                    for (Square space : attack.runAlgorithm(origin, internalBoard)) {
                                        if (space.hasPiece() && space.getPiece().isLight() != playerTurn()) canActivate = true;
                                    }

                                    if (canActivate) {
                                        // If a highlighted space is clicked
                                        if (attack.runAlgorithm(origin, internalBoard).contains(destination)) {
                                            sideui.getFeed().printAction(getSelectedPiece().toString() + " uses " + attack.toString() + ", dealing " + attack.getVal() + " to every enemy in the area!", player1Turn);
                                            // Iterate through every square in the attack algorithm
                                            for (Square space : attack.runAlgorithm(origin, internalBoard)) {
                                                if (space.hasPiece() && (space.getPiece().isLight() != playerTurn())) {
                                                    Piece aoeTargetPiece = space.getPiece(); 
                                                    aoeTargetPiece.takeDamage(attack.getVal());
                                                    if (aoeTargetPiece.getCurrentHP() <= 0) {
                                                        killPiece(aoeTargetPiece);
                                                        sideui.getFeed().printAction(targetPiece.toString() + " has been slain.", player1Turn);
                                                    }
                                                }
                                            } 
                                                                                                                
                                            deselectAll();
                                            switchPlayer();
                                            switchTurn();
                                            removeSkipButton();
                                            sideui.getMenu().revertMenu();
                                        }
                                    }

                                }
                                // Regular attack handler
                                else {
                                    // If the destination has a piece, the piece is a different color, and the space is highlighted
                                    if (destination.hasPiece() && (destination.getPiece().isLight() != playerTurn()) && attack.runAlgorithm(origin, internalBoard).contains(destination)) {
                                        sideui.getFeed().printAction(getSelectedPiece().toString() + " uses " + attack.toString() + " on " + targetPiece.toString() + ", dealing " + attack.getVal() + " damage.", player1Turn);
                                        targetPiece.takeDamage(sideui.getMenu().getSelectedSkill().getVal());
                                        if (targetPiece.getCurrentHP() <= 0) {
                                            killPiece(targetPiece);
                                            sideui.getFeed().printAction(targetPiece.toString() + " has been slain.", player1Turn);
                                        }
                                        
                                        deselectAll();
                                        switchPlayer();
                                        switchTurn();
                                        removeSkipButton();
                                        sideui.getMenu().revertMenu();
                                    }
                                }

                            }

                            // Spell handler
                            else if (sideui.getMenu().getSelectedSkill() instanceof Spell) {
                                Spell spell = (Spell) sideui.getMenu().getSelectedSkill();

                                // Spell algorithms should specify only spaces with allies in their algorithms, so no need for rigorous checking here
                                if (spell.runAlgorithm(origin, internalBoard).contains(destination)) {
                                    // Only heal if its possible
                                    if (targetPiece.getCurrentHP() != targetPiece.getMaxHP()) {
                                        sideui.getFeed().printAction(getSelectedPiece() + " heals " + targetPiece.toString() + " for " + spell.getVal() + " health. " + targetPiece.toString() + " is now at " + targetPiece.getCurrentHP() + " HP.", player1Turn);
                                        targetPiece.heal(spell.getVal());

                                        deselectAll();
                                        switchPlayer();
                                        switchTurn();
                                        removeSkipButton();
                                        sideui.getMenu().revertMenu();
                                    }
                                }
                            }

                            // Last resort handler
                            else if (sideui.getMenu().getSelectedSkill() instanceof LastResort) {
                                LastResort lastResort = (LastResort) sideui.getMenu().getSelectedSkill();

                                if (lastResort.runAlgorithm(origin, internalBoard).contains(destination)) {

                                    // Finding the king that casted the spell
                                    King king = null;
                                    for (Square[] row : internalBoard) {
                                        for (Square space : row) {
                                            if (space.hasPiece() && (space.getPiece() instanceof King) && (space.getPiece().isLight() == playerTurn())) {
                                                king = (King) space.getPiece();
                                            }
                                        }
                                    }

                                    // Only run command if the king can heal any HP
                                    if (king.getCurrentHP() != king.getMaxHP()) {
                                        Piece sacrifice = destination.getPiece();
                                        int HP = sacrifice.getCurrentHP();
                                        killPiece(sacrifice);
                                        king.heal(HP);
                                        sideui.getFeed().printAction(king.toString() + " uses the Last Resort, and sacrifices " + sacrifice.toString() + "! " + king.toString() + " heals to " + king.getCurrentHP() + " HP.", player1Turn);
                                            
                                        deselectAll();
                                        switchPlayer();
                                        switchTurn();
                                        removeSkipButton();
                                        sideui.getMenu().revertMenu();
                                    }

                                }
                            }
                            
                            
                        }

                    }

                }
            }

            

        }
    };


}
