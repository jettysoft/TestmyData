package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.testmydata.fxutil.UndecoratorController;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.util.StaticImages;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShowSqlScriptController implements Initializable {
	private static ShowSqlScriptController userHome = null;
	static String sqlscript = null;
	@FXML
	private Text scripttext;
	@FXML
	private ImageView closeicon;
	Stage myStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeicon.setImage(StaticImages.wrong_tick);
		scripttext.setText(sqlscript);

		closeicon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@SuppressWarnings("static-access")
			@Override
			public void handle(MouseEvent event) {
				Cleanup scu = new Cleanup();
				ShowSqlScriptController nc = new ShowSqlScriptController();
				scu.nullifyStrings(nc);

				Node source = (Node) event.getSource();
				myStage = (Stage) source.getScene().getWindow();
				myStage.close();
				UndecoratorController.getInstance(null);
			}
		});
	}

	public static ShowSqlScriptController getInstance(String script) {
		sqlscript = script;
		return userHome;
	}
}
