package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.testmydata.util.CommonFunctions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DialogController implements Initializable {
	@FXML
	private Label detailsLabel, headerLabel;
	@FXML
	private Button okButton;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		detailsLabel.setText(CommonFunctions.message);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

		headerLabel.setEffect(ds);
		okButton.setEffect(ds);
	}

	@FXML
	private boolean onClick(ActionEvent event) {
		CommonFunctions.dialogokstatus = "ok";

		Node source = (Node) event.getSource();
		Stage myStage = (Stage) source.getScene().getWindow();
		myStage.close();
		return false;
	}
}
