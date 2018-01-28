package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.testmydata.util.CommonFunctions;
import com.testmydata.util.StaticImages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConfirmDialogController implements Initializable {
	@FXML
	private Label detailsLabel;
	@FXML
	private Button yes, cancel;
	@FXML
	private ImageView appicon;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		detailsLabel.setText(CommonFunctions.message);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

		appicon.setImage(StaticImages.appicon.getImage());

		yes.setEffect(ds);
		cancel.setEffect(ds);
	}

	@FXML
	private boolean yes(ActionEvent event) {
		CommonFunctions.selectionstatus = "yes";

		Node source = (Node) event.getSource();
		Stage myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		return false;
	}

	@FXML
	private boolean cancel(ActionEvent event) {
		CommonFunctions.selectionstatus = "cancel";

		Node source = (Node) event.getSource();
		Stage myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		return false;
	}
}
