package com.lewis.brandon.conway;

import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {

	Boolean isPlaying = false;
	ConwaysGameOfLife gameOfLife = new ConwaysGameOfLife(8, 6);
	static final int CELL_SIZE = 30, GRID_OFFSET_Y = 100, GRID_OFFSET_X = 10, MIN_GRID_WIDTH = 4, MAX_GRID_WIDTH = 60, MIN_GRID_HEIGHT = 4, MAX_GRID_HEIGHT = 30;
	static final Color LIVING_CELL_COLOR = Color.MEDIUMSEAGREEN, DEAD_CELL_COLOR = Color.DIMGRAY;
	
	// SHARED JAVAFX NODES/CONTROLS
	Group group = new Group();
	GridPane gridPane = new GridPane();
	Slider heightSlider, widthSlider;
	Label instructions;
	TextField widthInput, heightInput;

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(group, 600, 450);
		scene.setFill(Color.LIGHTGRAY);

		gridPane.setAlignment(Pos.TOP_LEFT);
		gridPane.setHgap(10); // Manages spacing between grid columns
		gridPane.setVgap(10); // Manages spacing between grid rows
		gridPane.setPadding(new Insets(10, 10, 10, 10));

		buildBoardWidthControls();
		buildBoardHeightControls();
		buildGameControlButtons();
		buildGameLabels();

		group.getChildren().add(gridPane);

		stage.setTitle("Conway's Game of Life (Author: Brandon Lewis)");
		stage.setScene(scene);
		stage.show();

		// Set "Enter" key as trigger for calculating the next generation
		// while a game is in progress
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			if (isPlaying && key.getCode() == KeyCode.ENTER) {
				gameOfLife.calculateNextGeneration();
				updateGrid();
			}
		});

		updateGrid();	// Trigger first grid draw
	}

	private void buildBoardWidthControls() {
		Label mWidth = new Label("Board Width:");
		gridPane.add(mWidth, 0, 0);
		
		widthInput = new TextField();
		widthInput.setText("8");
		widthInput.setMaxWidth(35);
		widthInput.setDisable(true);
		widthInput.setAlignment(Pos.CENTER);
		gridPane.add(widthInput, 3, 0);

		widthSlider = new Slider(MIN_GRID_WIDTH, MAX_GRID_WIDTH, 8);
		widthSlider.setShowTickMarks(true);
		widthSlider.setMinorTickCount(2);
		widthSlider.setMajorTickUnit(10);
		widthSlider.setBlockIncrement(1.0);
		widthSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				widthInput.setText(String.valueOf(newValue.intValue()));
				int width = Integer.valueOf(widthInput.getText());
				int height = Integer.valueOf(heightInput.getText());
				gameOfLife = new ConwaysGameOfLife(width, height);
				updateGrid();
			}
		});
		gridPane.add(widthSlider, 1, 0, 2, 1);
	}

	private void buildBoardHeightControls() {
		Label mHeight = new Label("Board Height:");
		gridPane.add(mHeight, 0, 1);
		heightInput = new TextField();
		heightInput.setText("6");
		heightInput.setMaxWidth(35);
		heightInput.setDisable(true);
		heightInput.setAlignment(Pos.CENTER);
		gridPane.add(heightInput, 3, 1);

		heightSlider = new Slider(MIN_GRID_HEIGHT, MAX_GRID_HEIGHT, 8);
		heightSlider.setShowTickMarks(true);
		heightSlider.setMinorTickCount(2);
		heightSlider.setMajorTickUnit(10);
		heightSlider.setBlockIncrement(1.0);
		heightSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				heightInput.setText(String.valueOf(newValue.intValue()));
				int width = Integer.valueOf(widthInput.getText());
				int height = Integer.valueOf(heightInput.getText());
				gameOfLife = new ConwaysGameOfLife(width, height);
				updateGrid();
			}
		});
		gridPane.add(heightSlider, 1, 1, 2, 1);
	}
	
	private void buildGameControlButtons() {
		Button randomizeBtn = new Button("Randomize");
		gridPane.add(randomizeBtn, 5, 0);
		randomizeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameOfLife.randomizeFirstGeneration();
				updateGrid();
			}
		});

		Button clearBtn = new Button("Clear Grid");
		gridPane.add(clearBtn, 6, 0);
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameOfLife = new ConwaysGameOfLife(gameOfLife.getBoardWidth(), gameOfLife.getBoardHeight());
				updateGrid();
			}
		});

		Button startBtn = new Button("Start");
		gridPane.add(startBtn, 4, 0);
		startBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isPlaying) {
					instructions.setText("Click a cell to toggle its state.");
					startBtn.setText("Start");
					widthSlider.setDisable(false);
					heightSlider.setDisable(false);
					clearBtn.setDisable(false);
					randomizeBtn.setDisable(false);
					isPlaying = false;
				} else {
					startBtn.setText("Stop");
					instructions.setText("Press \"Enter\" to see the next generation of Life.");
					widthSlider.setDisable(true);
					heightSlider.setDisable(true);
					clearBtn.setDisable(true);
					randomizeBtn.setDisable(true);
					isPlaying = true;

					updateGrid();
				}
			}
		});
	}
	
	private void buildGameLabels() {
		instructions = new Label("Click a cell to toggle its state.");
		gridPane.add(instructions, 0, 2, 4, 1);

		Label livingCell = new Label("Living Cell");
		livingCell.setTextFill(LIVING_CELL_COLOR);
		livingCell.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
		gridPane.add(livingCell, 4, 1);

		Label deadCell = new Label("Dead Cell");
		deadCell.setTextFill(DEAD_CELL_COLOR);
		deadCell.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
		gridPane.add(deadCell, 5, 1);
	}

	private void clearPaintedNodes(Group rootGroup) {
		rootGroup.getChildren().removeAll(rootGroup.getChildren().stream()
				.filter(n -> n instanceof Rectangle)
				.collect(Collectors.toList()));
	}

	private void updateGrid() {
		clearPaintedNodes(group);	// Remove every drawn Rectangle for garbage collection before redrawing
		int xLoc = 0, yLoc = 0;
		for (boolean[] bArray : gameOfLife.getMatrix()) {
			for (boolean current : bArray) {
				Color c = current ? LIVING_CELL_COLOR : DEAD_CELL_COLOR;
				Rectangle r = new Rectangle(CELL_SIZE, CELL_SIZE, c);
				r.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
				r.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!isPlaying) {
							int clickedCellIndexX = (int) Math.floor((event.getX() - GRID_OFFSET_X) / CELL_SIZE);
							int clickedCellIndexY = (int) Math.floor((event.getY() - GRID_OFFSET_Y) / CELL_SIZE);
							gameOfLife.toggleCell(clickedCellIndexX, clickedCellIndexY);
							updateGrid();
						}
					}
				});
				r.setX(GRID_OFFSET_X + xLoc);
				r.setY(GRID_OFFSET_Y + yLoc);
				group.getChildren().add(r);

				yLoc += CELL_SIZE;
			}
			xLoc += CELL_SIZE;
			yLoc = 0;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Application.launch(args);
	}
}