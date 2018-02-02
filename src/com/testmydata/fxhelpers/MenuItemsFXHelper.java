package com.testmydata.fxhelpers;

import java.io.IOException;

import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.fxutil.UndecoratorScene;
import com.testmydata.util.StaticImages;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuItemsFXHelper {
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
					e.printStackTrace();
				}
			}
		});
	}

	public void initFX(JFXPanel fxPanel, String sname) throws IOException {
		// This method is invoked on the JavaFX thread
		String Screenpath = "";

		if (sname.equalsIgnoreCase("changepassword")) {
			Screenpath = "/com/testmydata/fxml/ChangePassword.fxml";
		} else if (sname.equalsIgnoreCase("emailsettings")) {
			Screenpath = "/com/testmydata/fxml/EmailSettings.fxml";
		} else if (sname.equalsIgnoreCase("qaserversettings")) {
			Screenpath = "/com/testmydata/fxml/qaserver.fxml";
		} else if (sname.equalsIgnoreCase("newfieldtofield")) {
			Screenpath = "/com/testmydata/fxmlnew/newfieldtofield.fxml";
		} else if (sname.equalsIgnoreCase("executefieldtofield")) {
			Screenpath = "/com/testmydata/fxml/ExecuteFieldtoField.fxml";
		} else if (sname.equalsIgnoreCase("newtestsuite")) {
			Screenpath = "/com/testmydata/fxml/NewTestSuite.fxml";
		} else if (sname.equalsIgnoreCase("executetestsuite")) {
			Screenpath = "/com/testmydata/fxml/ExecuteTestSuite.fxml";
		} else if (sname.equalsIgnoreCase("newcontrolreport")) {
			Screenpath = "/com/testmydata/fxml/NewControlReportRule.fxml";
		} else if (sname.equalsIgnoreCase("executecontrolreport")) {
			Screenpath = "/com/testmydata/fxml/ExecuteControlReport.fxml";
		} else if (sname.equalsIgnoreCase("testreports")) {
			Screenpath = "/com/testmydata/fxml/testreports.fxml";
		} else if (sname.equalsIgnoreCase("adduser")) {
			Screenpath = "/com/testmydata/fxml/AddUser.fxml";
		} else if (sname.equalsIgnoreCase("projectsetup")) {
			Screenpath = "/com/testmydata/fxmlnew/projects.fxml";
		}

		// Parent root = FXMLLoader.load(getClass().getResource(Screenpath));
		// The UI (Client Area) to display
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Screenpath));
		// fxmlLoader.setController(this);
		Region root = (Region) fxmlLoader.load();

		stage = new Stage();

		// The Undecorator as a Scene
		UndecoratorController.getInstance("menuitem");
		final UndecoratorScene undecoratorScene = new UndecoratorScene(stage, root);
		// Overrides defaults
		undecoratorScene.addStylesheet("/com/testmydata/css/decoration.css");
		undecoratorScene.addStylesheet("/com/testmydata/css/TableTextColor.css");

		stage.setScene(undecoratorScene);
		stage.toFront();
		stage.getIcons().addAll(StaticImages.appicon);
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