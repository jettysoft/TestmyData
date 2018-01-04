
package com.testmydata.fxutil;

import java.awt.Desktop;
import java.net.URI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UndecoratorSceneDemoTouch extends Application {

	Stage primaryStage;
	@FXML
	Slider sliderOpacity;
	@FXML
	TabPane tabPane;
	@FXML
	Hyperlink hyperlink;

	@Override
	public void start(final Stage stage) throws Exception {
		primaryStage = stage;
		primaryStage.setTitle("Undecorator Scene");

		// The UI (Client Area) to display
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/testmydata/fxml/Payroll_Payroll.fxml"));
		fxmlLoader.setController(this);
		Region root = (Region) fxmlLoader.load();

		// The Undecorator as a Scene
		final UndecoratorScene undecoratorScene = new UndecoratorScene(primaryStage, root);
		// Overrides defaults
		undecoratorScene.addStylesheet("/com/testmydata/css/decoration.css");
		// Enable fade transition
		undecoratorScene.setFadeInTransition();

		// Optional: Enable this node to drag the stage
		// By default the root argument of Undecorator is set as draggable
		// Node node = root.lookup("#draggableNode"); // Enable TabPane to drag
		// the stage
		// undecoratorScene.setAsStageDraggable(stage, tabPane);

		/*
		 * Fade out transition on window closing request
		 */
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				we.consume(); // Do not hide yet
				undecoratorScene.setFadeOutTransition();
			}
		});

		// initUI();

		primaryStage.setScene(undecoratorScene);
		primaryStage.toFront();
		primaryStage.show();
	}

	void initUI() {
		tabPane.opacityProperty().bind(sliderOpacity.valueProperty());
		hyperlink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Desktop.getDesktop()
							.browse(new URI("https://arnaudnouard.wordpress.com/2014/12/20/undecorator-bis/"));
				} catch (Exception ex) {
				}
			}
		});
	}

	/**
	 * Show a non resizable Stage
	 *
	 * @param event
	 */
	@FXML
	private void handleShowNonResizableStage(ActionEvent event) throws Exception {
		UndecoratorSceneDemoTouch undecoratorSceneDemo = new UndecoratorSceneDemoTouch();
		Stage stage = new Stage();
		stage.setTitle("Not resizable stage");
		stage.setResizable(false);
		stage.setWidth(600);
		stage.setMinHeight(400);
		undecoratorSceneDemo.start(stage);
	}

	/**
	 * Handles Utility stage buttons
	 *
	 * @param event
	 */
	public void handleUtilityAction(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
