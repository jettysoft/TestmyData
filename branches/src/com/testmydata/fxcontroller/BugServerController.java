package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class BugServerController implements Initializable {
	@FXML
	private ImageView saveicon, updateicon, refreshicon1, closeicon;
	@FXML
	private JFXComboBox<String> tfsurlcombo;
	@FXML
	private JFXTextField tfsurltext, tfsprojectnametext, tfsusernametext, tfspassword;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
