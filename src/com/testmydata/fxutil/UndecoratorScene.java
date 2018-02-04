
package com.testmydata.fxutil;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UndecoratorScene extends Scene {

	static public final String DEFAULT_STYLESHEET = "/com/testmydata/css/skin/undecorator.css";
	static public final String DEFAULT_STYLESHEET_UTILITY = "/com/testmydata/css/skin/undecoratorUtilityStage.css";
	static public final String DEFAULT_STAGEDECORATION = "/com/testmydata/fxml/stagedecoration.fxml";
	static public final String DEFAULT_STAGEDECORATION_UTILITY = "/com/testmydata/fxml/stageUtilityDecoration.fxml";

	static public final String DEFAULT_STYLESHEET_TOUCH = "/com/testmydata/css/skin/Touch/undecorator.css";
	static public final String DEFAULT_STYLESHEET_UTILITY_TOUCH = "/com/testmydata/css/skin/Touch/undecoratorUtilityStage.css";
	static public final String DEFAULT_STAGEDECORATION_TOUCH = "/com/testmydata/fxml/stagedecorationTouch.fxml";
	static public final String DEFAULT_STAGEDECORATION_UTILITY_TOUCH = "/com/testmydata/fxml/stageUtilityDecorationTouch.fxml";

	static public String STYLESHEET = DEFAULT_STYLESHEET_TOUCH;
	static public String STYLESHEET_UTILITY = DEFAULT_STYLESHEET_UTILITY_TOUCH;
	static public String STAGEDECORATION = DEFAULT_STAGEDECORATION_TOUCH;
	static public String STAGEDECORATION_UTILITY = DEFAULT_STAGEDECORATION_UTILITY_TOUCH;

	Undecorator undecorator;

	static public void setClassicDecoration() {
		UndecoratorScene.STAGEDECORATION = UndecoratorScene.DEFAULT_STAGEDECORATION;
		UndecoratorScene.STAGEDECORATION_UTILITY = UndecoratorScene.DEFAULT_STAGEDECORATION_UTILITY;
		UndecoratorScene.STYLESHEET = UndecoratorScene.DEFAULT_STYLESHEET;
		UndecoratorScene.STYLESHEET_UTILITY = UndecoratorScene.DEFAULT_STYLESHEET_UTILITY;
	}

	/**
	 * Basic constructor with built-in behavior
	 *
	 * @param stage
	 *            The main stage
	 * @param root
	 *            your UI to be displayed in the Stage
	 */
	public UndecoratorScene(Stage stage, Region root) {
		this(stage, StageStyle.TRANSPARENT, root, STAGEDECORATION);
	}

	/**
	 * UndecoratorScene constructor
	 *
	 * @param stage
	 *            The main stage
	 * @param stageStyle
	 *            could be StageStyle.UTILITY or StageStyle.TRANSPARENT
	 * @param root
	 *            your UI to be displayed in the Stage
	 * @param stageDecorationfxml
	 *            Your own Stage decoration or null to use the built-in one
	 */
	public UndecoratorScene(Stage stage, StageStyle stageStyle, Region root, String stageDecorationfxml) {

		super(root);

		/*
		 * fxml
		 */
		if (stageDecorationfxml == null) {
			if (stageStyle == StageStyle.UTILITY) {
				stageDecorationfxml = STAGEDECORATION_UTILITY;
			} else {
				stageDecorationfxml = STAGEDECORATION;
			}
		}
		undecorator = new Undecorator(stage, root, stageDecorationfxml, stageStyle);
		super.setRoot(undecorator);

		// Customize it by CSS if needed:
		if (stageStyle == StageStyle.UTILITY) {
			undecorator.getStylesheets().add(STYLESHEET_UTILITY);
		} else {
			undecorator.getStylesheets().add(STYLESHEET);
		}

		// Transparent scene and stage
		if (stage.getStyle() != StageStyle.TRANSPARENT) {
			stage.initStyle(StageStyle.TRANSPARENT);
		}
		super.setFill(Color.TRANSPARENT);

		// Default Accelerators
		undecorator.installAccelerators(this);

		// Forward pref and max size to main stage
		stage.setMinWidth(undecorator.getMinWidth());
		stage.setMinHeight(undecorator.getMinHeight());
		stage.setWidth(undecorator.getPrefWidth());
		stage.setHeight(undecorator.getPrefHeight());
	}

	public void removeDefaultStylesheet() {
		undecorator.getStylesheets().remove(STYLESHEET);
		undecorator.getStylesheets().remove(STYLESHEET_UTILITY);
	}

	public void addStylesheet(String css) {
		undecorator.getStylesheets().add(css);
	}
/*
	public void setAsStageDraggable(Stage stage, Node node) {
		undecorator.setAsStageDraggable(stage, node);
	}
*/
	public void setBackgroundStyle(String style) {
		undecorator.getShadowNode().setStyle(style);
	}

	public void setBackgroundOpacity(double opacity) {
		undecorator.getShadowNode().setOpacity(opacity);
	}

	public void setBackgroundPaint(Paint paint) {
		undecorator.removeDefaultBackgroundStyleClass();
		undecorator.getShadowNode().setFill(paint);
	}

	public Undecorator getUndecorator() {
		return undecorator;
	}

	public void setFadeInTransition() {
		undecorator.setFadeInTransition();
	}

	public void setFadeOutTransition() {
		undecorator.setFadeOutTransition();
	}

}
