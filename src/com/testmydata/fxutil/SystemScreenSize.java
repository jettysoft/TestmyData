package com.testmydata.fxutil;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SystemScreenSize {

	public void start(Stage stage) {
		Group root = new Group();
		Scene scene = new Scene(root, 500, 200);
		stage.setScene(scene);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());

		stage.show();

	}
}
