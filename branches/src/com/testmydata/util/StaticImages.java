package com.testmydata.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StaticImages {
	public final static Image appicon = new Image("/com/testmydata/fximages/crown.png", 60, 60, false, false);
	public final static Image source_execute = new Image("/com/testmydata/fximages/runtest.png", 25, 25, false, false);
	public final static Image source_run = new Image("/com/testmydata/fximages/please_wait.gif", 25, 25, false, false);
	public final static Image pdficon = new Image("/com/testmydata/fximages/pdf.png", 25, 25, false, false);
	public final static Image excelicon = new Image("/com/testmydata/fximages/excel.png", 25, 25, false, false);
	public final static Image homeicon = new Image("/com/testmydata/fximages/Home_Icon.png", 40, 40, false, false);
	public final static Image sqleditor = new Image("/com/testmydata/fximages/sqleditor.png", 20, 20, false, false);
	public final static Image sqlcolumneditor = new Image("/com/testmydata/fximages/sqleditor_column.png", 20, 20,
			false, false);
	public final static Image wrong_tick = new Image("/com/testmydata/fximages/Wrong_tick.png", 20, 20, false, false);
	public final static Image green_tick = new Image("/com/testmydata/fximages/tick_green.png", 20, 20, false, false);
	// public final static ImageView exit_confirm = new
	// ImageView("/com/testmydata/fximages/Exit_Confirm.png");
	public final static Image closeicon = new Image("/com/testmydata/fximages/Wrong_tick.png", 20, 20, false, false);
	public final static Image searchicon = new Image("/com/testmydata/fximages/search.png", 20, 20, false, false);
	public final static Image testcases = new Image("/com/testmydata/fximages/testcases.png", 20, 20, false, false);
	public final static Image controlreporticon = new Image("/com/testmydata/fximages/controlreport.png", 20, 20, false,
			false);
	public final static Image fieldicon = new Image("/com/testmydata/fximages/fieldtofield.png", 20, 20, false, false);
	public final static Image testsuites = new Image("/com/testmydata/fximages/testsuites.png", 20, 20, false, false);
	public final static Image newtestsuite = new Image("/com/testmydata/fximages/newtestsuite.png", 20, 20, false,
			false);
	public final static Image testexecution = new Image("/com/testmydata/fximages/testexecution.png", 20, 20, false,
			false);
	public final static Image report = new Image("/com/testmydata/fximages/report.png", 20, 20, false, false);
	public final static Image testreport = new Image("/com/testmydata/fximages/testreports.png", 20, 20, false, false);
	public final static Image settings = new Image("/com/testmydata/fximages/settings.png", 20, 20, false, false);
	public final static Image adduser = new Image("/com/testmydata/fximages/adduser.png", 20, 20, false, false);
	public final static Image changepassword = new Image("/com/testmydata/fximages/changepassword.png", 20, 20, false,
			false);
	public final static Image emailsettings = new Image("/com/testmydata/fximages/emailsettings.png", 20, 20, false,
			false);
	public final static Image qaserver = new Image("/com/testmydata/fximages/qaserver.png", 20, 20, false, false);
	public final static Image logout = new Image("/com/testmydata/fximages/logout.png", 20, 20, false, false);
	public final static Image save = new Image("/com/testmydata/fximages/save.png", 20, 20, false, false);
	public final static Image view = new Image("/com/testmydata/fximages/binocular.png", 20, 20, false, false);
	public final static Image refresh = new Image("/com/testmydata/fximages/refresh.png", 20, 20, false, false);
	public final static Image closeproject = new Image("/com/testmydata/fximages/closeproject.png", 20, 20, false,
			false);
	public final static Image modify = new Image("/com/testmydata/fximages/modify.png", 20, 20, false, false);
	public final static Image delete = new Image("/com/testmydata/fximages/delete.png", 20, 20, false, false);
	public final static Image add = new Image("/com/testmydata/fximages/add.png", 20, 20, false, false);
	public final static Image clear = new Image("/com/testmydata/fximages/clear.png", 20, 20, false, false);
	public final static Image bugicon = new Image("/com/testmydata/fximages/bug.png", 20, 20, false, false);
	public final static Image infoicon = new Image("/com/testmydata/fximages/help.png", 20, 20, false, false);

	public final static Image im_loading = new Image("/com/testmydata/fxml/302.gif", 25, 25, false, false);

	public static String lblStyleold = "-fx-background-color: linear-gradient(#f7f79e,#f2f271);  -fx-text-alignment :center; -fx-background-radius: 2; "
			+ " -fx-background-insets: 0; -fx-text-fill: #162a4c; -fx-font-weight: normal; -fx-border-clor: #162a4c;";

	public static String lblStyle = "-fx-background-color: #000000; -fx-opacity: 0.8;  -fx-text-alignment :center; -fx-background-radius: 2; "
			+ " -fx-background-insets: 0; -fx-text-fill: #ffffff; -fx-font-weight: normal; -fx-border-clor: #ffffff;";

	public static StackPane getmodifybutton() {
		final Button mod_Button = new Button();
		final ImageView modif_icon = new ImageView();
		StackPane mod_pane = new StackPane();
		Tooltip tp = new Tooltip(" Modify ");
		mod_pane.setAlignment(Pos.CENTER);
		tp.setStyle(StaticImages.lblStyle);
		modif_icon.setImage(StaticImages.modify);
		modif_icon.setFitHeight(20.0);
		modif_icon.setFitWidth(20.0);
		mod_Button.setMinWidth(20.0);
		mod_Button.setMinHeight(20.0);
		mod_Button.setStyle("-fx-background-color: transparent");
		Tooltip.install(mod_Button, tp);
		mod_pane.getChildren().addAll(modif_icon, mod_Button);

		return mod_pane;
	}

	public static StackPane getdeletebutton() {
		StackPane pane = new StackPane();
		final ImageView delet_icon = new ImageView();
		Tooltip del_tp = new Tooltip(" Delete ");
		final Button cellButton = new Button();
		pane.setAlignment(Pos.CENTER);
		del_tp.setStyle(StaticImages.lblStyle);
		delet_icon.setImage(StaticImages.delete);
		delet_icon.setFitHeight(20);
		delet_icon.setFitWidth(20);
		cellButton.setMinWidth(20.0);
		cellButton.setMinHeight(20.0);
		cellButton.setStyle("-fx-background-color: transparent");
		Tooltip.install(cellButton, del_tp);
		pane.getChildren().addAll(delet_icon, cellButton);

		return pane;
	}

	public static StackPane getmodifybuttonintable(final Button cellButton) {
		final ImageView modif_icon = new ImageView();
		StackPane mod_pane = new StackPane();
		Tooltip tp = new Tooltip(" Modify ");
		mod_pane.setAlignment(Pos.CENTER);
		tp.setStyle(StaticImages.lblStyle);
		modif_icon.setImage(StaticImages.modify);
		modif_icon.setFitHeight(20.0);
		modif_icon.setFitWidth(20.0);
		cellButton.setMinWidth(20.0);
		cellButton.setMinHeight(20.0);
		cellButton.setStyle("-fx-background-color: transparent");
		Tooltip.install(cellButton, tp);
		mod_pane.getChildren().addAll(modif_icon, cellButton);

		return mod_pane;
	}

	public static StackPane getdeletebuttonintable(final Button cellButton) {
		StackPane pane = new StackPane();
		final ImageView delet_icon = new ImageView();
		Tooltip del_tp = new Tooltip(" Delete ");
		pane.setAlignment(Pos.CENTER);
		del_tp.setStyle(StaticImages.lblStyle);
		delet_icon.setImage(StaticImages.delete);
		delet_icon.setFitHeight(20);
		delet_icon.setFitWidth(20);
		cellButton.setMinWidth(20.0);
		cellButton.setMinHeight(20.0);
		cellButton.setStyle("-fx-background-color: transparent");
		Tooltip.install(cellButton, del_tp);
		pane.getChildren().addAll(delet_icon, cellButton);

		return pane;
	}
}
