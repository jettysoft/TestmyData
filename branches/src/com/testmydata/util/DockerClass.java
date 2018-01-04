package com.testmydata.util;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DockerClass {
	static String startDate_Default = null, endDate_Default = null;

	private final List<ImageView> arrImgView = new ArrayList<>();
	private final List<Label> lblList = new ArrayList<>();
	private final String themeUrl, lblStyle;

	public DockerClass() {
		lblStyle = "-fx-background-color: linear-gradient(#277CD2, #0C23EA);  -fx-text-alignment :center; -fx-background-radius: 25; -fx-background-insets: 0; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-clor: white;";
		themeUrl = getClass().getResource("/com/testmydata/fximages/dock_background.png").toExternalForm();
	}

	public void add(ImageView imgView) {
		Label lbl = new Label(imgView.getId().replaceAll("_", " "));
		lbl.setStyle(lblStyle);
		lbl.setMaxWidth(70);
		lbl.setVisible(false);
		GridPane.setValignment(lbl, VPos.BOTTOM);
		GridPane.setHalignment(lbl, HPos.CENTER);
		GridPane.setMargin(lbl, new Insets(-20, 0, 0, 0));

		lblList.add(lbl);
		arrImgView.add(imgView);
	}

	public ImageView get(int idx) {
		return arrImgView.get(idx);
	}

	public boolean remove(ImageView imgView) {
		return arrImgView.remove(imgView);
	}

	public ImageView remove(int idx) {
		return arrImgView.remove(idx);
	}

	public SubScene redrawDock() {
		if (arrImgView.isEmpty())
			return null;

		arrImgView.forEach(each -> {
			each.setFitHeight(60);
			each.setFitWidth(60);
			each.setPreserveRatio(true);

			Effect defaultEffect = new Reflection(-20, 0.75, 0.45, 0); // new
																		// DropShadow(30,
																		// 2, 1,
																		// Color.DARKGRAY);
			each.setEffect(defaultEffect);

			each.setOnMouseMoved((e) -> {
				((ImageView) e.getSource()).setEffect(new DropShadow(4, 4, 4, Color.DARKGRAY));
				lblList.get(arrImgView.indexOf(each)).setVisible(true);
				each.setFitHeight(82);
				each.setFitWidth(82);
				each.setPreserveRatio(true);
			});

			each.setOnMouseExited((e) -> {
				((ImageView) e.getSource()).setEffect(defaultEffect);
				lblList.get(arrImgView.indexOf(each)).setVisible(false);
				each.setFitHeight(60);
				each.setFitWidth(60);
				each.setPreserveRatio(true);
			});
			each.setOnMouseClicked((e) -> {
				((ImageView) e.getSource()).setEffect(new DropShadow(4, 4, 4, Color.DARKGRAY));
				each.setFitHeight(82);
				each.setFitWidth(82);
				each.setPreserveRatio(true);
				openScenenow(((ImageView) e.getSource()).getId());
			});
		});

		GridPane gPane = new GridPane();
		gPane.setHgap(5);
		// gPane.setVgap(10);
		// gPane.setPadding(new Insets(0, 10, 20, 20));
		gPane.setPadding(new Insets(0, 10, -4, 20));
		gPane.setCache(true);
		gPane.alignmentProperty().setValue(Pos.CENTER);

		ImageView[] getArrImgView = new ImageView[arrImgView.size()];
		arrImgView.toArray(getArrImgView);
		gPane.addRow(1, getArrImgView);

		for (int idx = 0; idx < getArrImgView.length; idx++) {
			gPane.add(lblList.get(idx), idx, 1);
		}

		Pane pane = new Pane();

		ImageView theme = new ImageView(themeUrl);
		theme.setFitHeight(80);
		theme.setFitWidth(arrImgView.size() * 74);

		pane.getChildren().add(theme);
		pane.setLayoutY(0);

		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: transparent; " + "-fx-background-radius: 16;");

		root.getChildren().add(pane);
		root.getChildren().add(gPane);

		SubScene childScene = new SubScene(root, arrImgView.size() * 75, 82);
		childScene.setFill(Color.TRANSPARENT);

		return childScene;
	}

	public void openScenenow(String id) {
		try {

		} catch (Exception e) {
		}
	}
}
