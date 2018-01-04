package com.testmydata.fxhelpers;

import java.io.IOException;

import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.fxutil.UndecoratorScene;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomePageFXHelper {
	public static Stage stage;
	double xOffset = 0;
	double yOffset = 0;

	public void initAndShowGUI(String screenname) {

		final JFXPanel fxPanel = new JFXPanel();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					initFX(fxPanel, screenname);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void initFX(JFXPanel fxPanel, String sname) throws IOException {
		// This method is invoked on the JavaFX thread
		String Screenpath = "";

		if (sname.equalsIgnoreCase("homepage")) {
			Screenpath = "/com/testmydata/fxml/HomePage.fxml";
		} else if (sname.equalsIgnoreCase("registration")) {
			Screenpath = "/com/testmydata/fxml/Registration.fxml";
		}

		// Parent root = FXMLLoader.load(getClass().getResource(Screenpath));
		// The UI (Client Area) to display
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Screenpath));
		// fxmlLoader.setController(this);
		Region root = (Region) fxmlLoader.load();

		stage = new Stage();

		// The Undecorator as a Scene
		UndecoratorController.getInstance("close");
		final UndecoratorScene undecoratorScene = new UndecoratorScene(stage, root);
		// Overrides defaults
		undecoratorScene.addStylesheet("/com/testmydata/css/decoration.css");
		// Enable fade transition
		// undecoratorScene.setFadeInTransition();
		stage.setScene(undecoratorScene);
		stage.toFront();
		Image image = new Image("/com/testmydata/fximages/crown.png");
		stage.getIcons().addAll(image);
		stage.show();
		stage.setFullScreen(true);
		// stage.initStyle(StageStyle.UNDECORATED);
		// new SystemScreenSize().start(stage);

		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		// Scene scene = new Scene(root);
		// stage.setScene(scene);
		Platform.setImplicitExit(false);
		// stage.show();
	}

	@SuppressWarnings("unused")
	private static Scene createScene() {
		Group root = new Group();
		Scene scene = new Scene(root, Color.ALICEBLUE);
		Text text = new Text();

		text.setX(40);
		text.setY(100);
		text.setFont(new Font(25));
		text.setText("Welcome JavaFX!");

		root.getChildren().add(text);

		return (scene);
	}

}