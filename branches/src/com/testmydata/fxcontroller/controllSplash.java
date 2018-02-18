
package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.testmydata.animation.FadeInLeftTransition;
import com.testmydata.animation.FadeInRightTransition;
import com.testmydata.animation.FadeInTransition;
import com.testmydata.binarybeans.InvoiceHeaderBeanBinaryTrade;
import com.testmydata.binarybeans.UsersDetailsBeanBinaryTrade;
import com.testmydata.fxhelpers.LoginFXHelper;
import com.testmydata.main.WelcomePage;
import com.testmydata.util.StaticImages;
import com.testmydata.vpn.VpnConnectionThread;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class controllSplash implements Initializable {
	static controllSplash userHome;
	@FXML
	private Text lblWelcome;
	@FXML
	private Text lblRudy, lblsecure;
	@FXML
	private VBox vboxBottom;
	@FXML
	private Label lblClose;
	static String source, userlevel;
	static UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree;
	Stage stage;
	@FXML
	private ImageView imgLoading;
	int countvpnlaunch = 0;
	static ArrayList<InvoiceHeaderBeanBinaryTrade> headerlist;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if (source.equals("welcome")) {
			lblWelcome.setText("Welcome to");
			lblsecure.setText("Securing....");
		} else if (source.equals("login")) {
			lblWelcome.setText("Loading");
			lblsecure.setText("Secured....");
		}
		imgLoading.setImage(StaticImages.im_loading);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

		lblWelcome.setEffect(ds);
		lblRudy.setEffect(ds);
		lblsecure.setEffect(ds);

		longStart();
		lblClose.setOnMouseClicked((MouseEvent event) -> {
			VpnConnectionThread.shutdown();
			Platform.exit();
			System.exit(0);
		});
	}

	public static controllSplash getInstance(String source1, String userlevel1,
			UsersDetailsBeanBinaryTrade loggedInUsersDetailsBeanBinaryTree1,
			ArrayList<InvoiceHeaderBeanBinaryTrade> headerlist1) {
		source = source1;
		userlevel = userlevel1;
		loggedInUsersDetailsBeanBinaryTree = loggedInUsersDetailsBeanBinaryTree1;
		headerlist = headerlist1;
		return userHome;
	}

	private void longStart() {
		Service<String> service = new Service<String>() {
			@Override
			protected Task<String> createTask() {
				return new Task<String>() {
					@Override
					protected String call() throws Exception {
						int max = 0;
						if (source.equals("welcome")) {
							max = 100;
						} else if (source.equals("login")) {
							max = 100;
						}
						updateProgress(0, max);
						for (int k = 0; k < max; k++) {
							Thread.sleep(60);
							updateProgress(k + 1, max);
						}
						return "true";
					}
				};
			}
		};
		service.start();
		service.setOnRunning((WorkerStateEvent event) -> {

			new FadeInLeftTransition(lblWelcome).play();
			new FadeInRightTransition(lblRudy).play();
			new FadeInLeftTransition(lblsecure).play();
			new FadeInTransition(vboxBottom).play();

		});
		service.setOnSucceeded((WorkerStateEvent event) -> {
			// code to call loginfxml after splash screen
			// "Sample Apps", true, StageStyle.UNDECORATED, false);
			if (source.equals("welcome")) {
				if (VpnConnectionThread.isVpnConnected()) {
					LoginFXHelper.stage.close();
					WelcomePage wp = new WelcomePage();
					wp.run();
				} else if (!VpnConnectionThread.isVpnConnected() && !VpnConnectionThread.isVpnError()) {
					longStart();
				} else if (!VpnConnectionThread.isVpnConnected() && VpnConnectionThread.isVpnError()) {
					longStart();
					VpnConnectionThread.shutdown();
					VpnConnectionThread.launch();
				}

			} else if (source.equals("login")) {
				LoginFXHelper.stage.close();
				ControllLogin cl = new ControllLogin();
				cl.openDashBoard(userlevel, loggedInUsersDetailsBeanBinaryTree);
			}
		});
	}
}
