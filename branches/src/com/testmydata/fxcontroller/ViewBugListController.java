package com.testmydata.fxcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.testmydata.dao.DAO;
import com.testmydata.memorycleanup.Cleanup;
import com.testmydata.tfs.jira.binarybeans.BugFieldsBeans;
import com.testmydata.util.CommonFunctions;
import com.testmydata.util.DefaultBugServerDetails;
import com.testmydata.util.Loggedinuserdetails;
import com.testmydata.util.StaticImages;
import com.testmydata.util.TFSSynchronizeService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class ViewBugListController extends DashBoardController implements Initializable {
	@FXML
	private ImageView refreshicon, closeicon, processingicon;
	@FXML
	private JFXTextField searchtext, newcounttext, activecounttext, resolvedcounttext, closedcounttext;
	@FXML
	private Label refreshlbl;
	@FXML
	private AnchorPane actionanchor1;
	@FXML
	private TableView<BugFieldsBeans> bugtable;
	@FXML
	private TableColumn<BugFieldsBeans, String> id, servertype, bugid, title, tcid, ruleid, state, reason, assignedto,
			createdby;
	@FXML
	private TableColumn<BugFieldsBeans, Boolean> modifybutton = new TableColumn<BugFieldsBeans, Boolean>("Modify");
	static ArrayList<BugFieldsBeans> bugslist = new ArrayList<BugFieldsBeans>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InvoiceStaticHelper.setvblc(this);
		refreshicon.setImage(StaticImages.refresh);
		closeicon.setImage(StaticImages.closeicon);
		processingicon.setImage(StaticImages.source_run);

		populateBugslist(); // LoadBugList

		refreshlbl.setStyle(StaticImages.lblStyle);

		refreshicon.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refreshlbl.setVisible(true);
			}
		});
		refreshicon.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refreshlbl.setVisible(false);
			}
		});
		refreshicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				refresh();
			}
		});

		modifybutton.setGraphic(StaticImages.getmodifybutton());
		modifybutton.setText("");
		modifybutton.setSortable(false);
		modifybutton.setCellValueFactory(new PropertyValueFactory<>("buttons"));
		modifybutton.setPrefWidth(30);
		modifybutton.setResizable(false);
		modifybutton.setCellFactory(
				new Callback<TableColumn<BugFieldsBeans, Boolean>, TableCell<BugFieldsBeans, Boolean>>() {
					@Override
					public TableCell<BugFieldsBeans, Boolean> call(TableColumn<BugFieldsBeans, Boolean> p) {
						return new ModifyButtonCell();
					}
				});
		bugtable.getColumns().add(modifybutton);

		// id, servertype, bugid, title, tcid, ruleid, state, reason,
		// assignedto,createdby
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		servertype.setCellValueFactory(new PropertyValueFactory<>("servertype"));
		bugid.setCellValueFactory(new PropertyValueFactory<>("bugid"));
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		tcid.setCellValueFactory(new PropertyValueFactory<>("tcid"));
		ruleid.setCellValueFactory(new PropertyValueFactory<>("ruleid"));
		state.setCellValueFactory(new PropertyValueFactory<>("state"));
		reason.setCellValueFactory(new PropertyValueFactory<>("reason"));
		assignedto.setCellValueFactory(new PropertyValueFactory<>("assignedto"));
		createdby.setCellValueFactory(new PropertyValueFactory<>("createdby"));

		id.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
		servertype.setStyle("-fx-text-fill: #162a4c; -fx-font-weight:bold;");
		bugid.setStyle("-fx-text-fill: red; -fx-font-weight:bold;");
		title.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		tcid.setStyle("-fx-text-fill: #162a4c; -fx-font-weight:bold;");
		ruleid.setStyle("-fx-text-fill: #162a4c; -fx-font-weight:bold;");

		reason.setStyle("-fx-text-fill: #162a4c; -fx-font-weight:bold;");
		assignedto.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");
		createdby.setStyle("-fx-text-fill: black; -fx-font-weight:bold;");

		state.setCellFactory(new Callback<TableColumn<BugFieldsBeans, String>, TableCell<BugFieldsBeans, String>>() {

			@Override
			public TableCell<BugFieldsBeans, String> call(TableColumn<BugFieldsBeans, String> param) {
				final TableCell<BugFieldsBeans, String> cell = new TableCell<BugFieldsBeans, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (getIndex() > -1 && getIndex() < getTableView().getItems().size()) {
							BugFieldsBeans bfg = getTableView().getItems().get(getIndex());
							setText(item.toString());

							if (bfg.getState().trim().contains("New")) {
								setStyle("-fx-text-fill: #162a4c; -fx-font-weight:bold; -fx-alignment: center;");
							}
							if (bfg.getState().trim().contains("Active")) {
								setStyle("-fx-text-fill: #007ACC; -fx-font-weight:bold; -fx-alignment: center;");
							}
							if (bfg.getState().trim().contains("Resolved")) {
								setStyle("-fx-text-fill: #FF9D00; -fx-font-weight:bold; -fx-alignment: center;");
							}

							if (bfg.getState().trim().contains("Closed")) {
								setStyle("-fx-text-fill: #339933; -fx-font-weight:bold; -fx-alignment: center;");
							}

						}
					}
				};
				return cell;
			}
		});

		searchtext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String enteredString = searchtext.getText().toString();
				if (enteredString != null) {
					if (enteredString.length() >= 1) {
						@SuppressWarnings("unchecked")
						ArrayList<BugFieldsBeans> filteredData = filterByBugs(bugslist, enteredString);
						populateTable(filteredData);
					} else if (!(enteredString.length() >= 1)) {
						populateTable(bugslist);
					}
				}
			}
		});

		closeicon.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				AnchorPane pane = (AnchorPane) ((ImageView) t.getSource()).getParent().getParent().getParent();
				pane.getChildren().remove(pane.getChildren().size() - 1);

				ViewBugListController nc = new ViewBugListController();
				Cleanup.nullifyStrings(nc);
			}
		});
	}

	public void refresh() {
		refreshicon.setVisible(false);
		processingicon.setVisible(true);

		if (DefaultBugServerDetails.servertype.equals("TFS")) {
			TFSSynchronizeService.source = "buglist";
			TFSSynchronizeService.startsync(); // TFS Sync
		} else if (DefaultBugServerDetails.servertype.equals("JIRA")) {

		} else if (DefaultBugServerDetails.servertype.equals("TMD")) { // TestMyDATA
			refreshpostactions();
		}
	}

	public void refreshpostactions() {
		populateBugslist();

		processingicon.setVisible(false);
		refreshicon.setVisible(true);
	}

	private void populateBugslist() {
		try {
			bugslist = new DAO().getBuglist(null, null, DefaultBugServerDetails.projectname,
					DefaultBugServerDetails.serverid, Loggedinuserdetails.defaultproject);
			if (bugslist == null || bugslist.size() == 0) {
				searchtext.setDisable(true);
				removebugsfromtable();
			} else {
				searchtext.setDisable(false);
				populateTable(bugslist);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateTable(ArrayList<BugFieldsBeans> arrayListData) {
		if (arrayListData != null && arrayListData.size() > 0) {
			ObservableList<BugFieldsBeans> data = FXCollections.observableArrayList();
			int newcount = 0, active = 0, resolved = 0, closed = 0;

			for (int i = 0; i < arrayListData.size(); i++) {
				BugFieldsBeans bfb = arrayListData.get(i);
				bfb.setId(arrayListData.get(i).getId());
				bfb.setServertype(arrayListData.get(i).getServertype());
				bfb.setServerid(arrayListData.get(i).getServerid());
				bfb.setBugid(arrayListData.get(i).getBugid());
				bfb.setTitle(arrayListData.get(i).getTitle());
				bfb.setTcid(arrayListData.get(i).getTcid());
				bfb.setRuleid(arrayListData.get(i).getRuleid());
				bfb.setState(arrayListData.get(i).getState());
				bfb.setReason(arrayListData.get(i).getReason());
				bfb.setAssignedto(arrayListData.get(i).getAssignedto());
				bfb.setCreatedby(arrayListData.get(i).getCreatedby());
				bfb.setArea(arrayListData.get(i).getArea());
				bfb.setIterationpath(arrayListData.get(i).getIterationpath());
				bfb.setReprosteps(arrayListData.get(i).getReprosteps());
				bfb.setLocalprojectid(arrayListData.get(i).getLocalprojectid());

				data.add(bfb);
				bugtable.setItems(data);
				bugtable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

				if (arrayListData.get(i).getState().equals("New")) {
					newcount++;
				} else if (arrayListData.get(i).getState().equals("Active")) {
					active++;
				} else if (arrayListData.get(i).getState().equals("Resolved")) {
					resolved++;
				} else if (arrayListData.get(i).getState().equals("Closed")) {
					closed++;
				}
			}
			bugtable.refresh();
			newcounttext.setText(Integer.toString(newcount));
			activecounttext.setText(Integer.toString(active));
			resolvedcounttext.setText(Integer.toString(resolved));
			closedcounttext.setText(Integer.toString(closed));

		} else {
			removebugsfromtable();
			newcounttext.setText(Integer.toString(0));
			activecounttext.setText(Integer.toString(0));
			resolvedcounttext.setText(Integer.toString(0));
			closedcounttext.setText(Integer.toString(0));

			runmessage("No Bugs Found...");
		}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList filterByBugs(ArrayList<BugFieldsBeans> unFiltered, String str) {

		ArrayList<BugFieldsBeans> bgs = new ArrayList<BugFieldsBeans>();
		for (BugFieldsBeans bean : unFiltered) {
			if (bean.getServertype() != null && bean.getServertype().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getBugid() != null && bean.getBugid().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getId() != null && bean.getId().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getTitle() != null && bean.getTitle().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getState() != null && bean.getState().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getReason() != null && bean.getReason().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getAssignedto() != null && bean.getAssignedto().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			} else if (bean.getCreatedby() != null && bean.getCreatedby().toLowerCase().contains(str.toLowerCase())) {
				bgs.add(bean);
			}
		}
		return bgs;
	}

	private void removebugsfromtable() {
		if (bugtable != null) {
			for (int i = bugtable.getItems().size() - 1; i >= 0; i--) {
				bugtable.getItems().remove(i);
			}
		}
	}

	@Override
	public void runmessage(String message) {
		CommonFunctions.message = message;
		CommonFunctions.invokeAlertBox(getClass());
	}

	public class ModifyButtonCell extends TableCell<BugFieldsBeans, Boolean> {
		final Button cellButton = new Button();
		final ImageView modif_icon = new ImageView();
		StackPane pane = new StackPane();
		Tooltip tp = new Tooltip("Modify");

		ModifyButtonCell() {
			pane.setAlignment(Pos.CENTER);
			tp.setStyle(StaticImages.lblStyle);
			modif_icon.setImage(StaticImages.modify);
			modif_icon.setFitHeight(20.0);
			modif_icon.setFitWidth(20.0);
			cellButton.setMinWidth(20.0);
			cellButton.setMinHeight(20.0);
			cellButton.setStyle("-fx-background-color: transparent");
			Tooltip.install(cellButton, tp);
			pane.getChildren().addAll(modif_icon, cellButton);
			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {

					BugFieldsBeans bfb = bugtable.getItems().get(getIndex());

					if (Loggedinuserdetails.newbug == 1) {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								// fxmlLoader.setController(this);

								InvoiceStaticHelper.dash.runnewbug(true, bfb.getId(), bfb.getBugid(), bfb.getTitle(),
										bfb.getAssignedto(), bfb.getState(), bfb.getReason(), bfb.getArea(),
										bfb.getIterationpath(), bfb.getTcid(), bfb.getRuleid(), bfb.getServertype(),
										bfb.getServerid(), bfb.getReprosteps(), "update");

							}
						});

					} else {
						runmessage("Access Denied. Contact your Manager...");
					}
				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(pane);
			}
		}
	}
}
