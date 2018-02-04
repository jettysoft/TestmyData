package com.testmydata.fxhelpers;

import java.io.IOException;

import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.util.StaticImages;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginFXHelper {
	public static Stage stage;
	double xOffset = 0;
	double yOffset = 0;

	public void initAndShowGUI(String screenname) {
		// This method is invoked on the EDT thread
		// JFrame frame = new JFrame("Swing and JavaFX");
		final JFXPanel fxPanel = new JFXPanel();

		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

		if (sname.equalsIgnoreCase("splash")) {
			Screenpath = "/com/testmydata/fxml/splash.fxml";
		} else if (sname.equalsIgnoreCase("loginScreen")) {
			Screenpath = "/com/testmydata/fxml/login.fxml";
		} else if (sname.equalsIgnoreCase("companaynameadding")) {
			Screenpath = "/com/testmydata/fxml/CompanyNameAdding.fxml";
		} else if (sname.equalsIgnoreCase("forgotpassword")) {
			Screenpath = "/com/testmydata/fxml/ForgotPassword.fxml";
		} else if (sname.equalsIgnoreCase("welcomepage")) {
			Screenpath = "/com/testmydata/fxml/WelcomePage.fxml";
		}

		Parent root = FXMLLoader.load(getClass().getResource(Screenpath));
		stage = new Stage();
		// stage.setTitle("Login");
		UndecoratorController.getInstance("close");
		stage.initStyle(StageStyle.UNDECORATED);

		stage.getIcons().addAll(StaticImages.appicon);

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
		Scene scene = new Scene(root);
		stage.setScene(scene);
		Platform.setImplicitExit(false);
		stage.show();
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