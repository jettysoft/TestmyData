package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.StaticImages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TestResultsController implements Initializable {
	@FXML
	private Label queryresultlabel, teststatuslabel, messagelabel;
	@FXML
	private JFXButton okButton;
	@FXML
	private ImageView appicon;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		appicon.setImage(StaticImages.appicon);
		queryresultlabel.setText("Query Result : " + CommonFunctions.queryresult);
		teststatuslabel.setText("Test Status : " + CommonFunctions.teststatus);
		messagelabel.setText("Message : " + CommonFunctions.message);
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
