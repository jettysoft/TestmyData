package com.testmydata.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StaticImages {
	public final static ImageView homeicon = new ImageView("/com/testmydata/fximages/Home_Icon.png");
	public final static ImageView pdficon = new ImageView("/com/testmydata/fximages/pdf.png");
	public final static ImageView excelicon = new ImageView("/com/testmydata/fximages/excel.png");
	public final static ImageView sqleditor = new ImageView("/com/testmydata/fximages/sqleditor.png");
	public final static ImageView sqlcolumneditor = new ImageView("/com/testmydata/fximages/sqleditor_column.png");
	public final static ImageView closeicon = new ImageView("/com/testmydata/fximages/Wrong_tick.png");
	public final static ImageView source_execute = new ImageView("/com/testmydata/fximages/runtest.png");
	public final static ImageView source_run = new ImageView("/com/testmydata/fximages/please_wait.gif");
	public final static ImageView wrong_tick = new ImageView("/com/testmydata/fximages/Wrong_tick.png");
	public final static ImageView green_tick = new ImageView("/com/testmydata/fximages/tick_green.png");
	// public final static ImageView exit_confirm = new
	// ImageView("/com/testmydata/fximages/Exit_Confirm.png");
	public final static ImageView searchicon = new ImageView("/com/testmydata/fximages/search.png");
	public final static ImageView appicon = new ImageView("/com/testmydata/fximages/crown.png");
	public final static ImageView testcases = new ImageView("/com/testmydata/fximages/testcases.png");
	public final static ImageView controlreporticon = new ImageView("/com/testmydata/fximages/controlreport.png");
	public final static ImageView fieldicon = new ImageView("/com/testmydata/fximages/fieldtofield.png");
	public final static ImageView testsuites = new ImageView("/com/testmydata/fximages/testsuites.png");
	public final static ImageView newtestsuite = new ImageView("/com/testmydata/fximages/newtestsuite.png");
	public final static ImageView testexecution = new ImageView("/com/testmydata/fximages/testexecution.png");
	public final static ImageView report = new ImageView("/com/testmydata/fximages/report.png");
	public final static ImageView testreport = new ImageView("/com/testmydata/fximages/testreports.png");
	public final static ImageView settings = new ImageView("/com/testmydata/fximages/settings.png");
	public final static ImageView adduser = new ImageView("/com/testmydata/fximages/adduser.png");
	public final static ImageView changepassword = new ImageView("/com/testmydata/fximages/changepassword.png");
	public final static ImageView emailsettings = new ImageView("/com/testmydata/fximages/emailsettings.png");
	public final static ImageView qaserver = new ImageView("/com/testmydata/fximages/qaserver.png");
	public final static ImageView logout = new ImageView("/com/testmydata/fximages/logout.png");
	public final static ImageView save = new ImageView("/com/testmydata/fximages/save.png");
	public final static ImageView view = new ImageView("/com/testmydata/fximages/binocular.png");
	public final static ImageView refresh = new ImageView("/com/testmydata/fximages/refresh.png");
	public final static ImageView closeproject = new ImageView("/com/testmydata/fximages/closeproject.png");
	public final static ImageView modify = new ImageView("/com/testmydata/fximages/modify.png");
	public final static ImageView delete = new ImageView("/com/testmydata/fximages/delete.png");
	public final static ImageView add = new ImageView("/com/testmydata/fximages/add.png");
	public final static ImageView clear = new ImageView("/com/testmydata/fximages/clear.png");

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
		modif_icon.setImage(StaticImages.modify.getImage());
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
		delet_icon.setImage(StaticImages.delete.getImage());
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
		modif_icon.setImage(StaticImages.modify.getImage());
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
		delet_icon.setImage(StaticImages.delete.getImage());
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
