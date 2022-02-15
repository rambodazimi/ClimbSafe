package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import javafx.fxml.FXML;
import java.util.*;

import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.TOAssignment;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ViewAssignmentController {

	ObservableList<String> sortList = FXCollections.observableArrayList();

	ObservableList<String> filterList = FXCollections.observableArrayList();

	@FXML
	private ChoiceBox<String> sortBy;

	@FXML
	private ChoiceBox<String> filterBy;

	@FXML
	private TableView<TOAssignment> overviewTable;

	@FXML
	private Label errorLbl;

	@FXML
	private Button initiateAssignmentBtn;

	@FXML
	private TextField searchAssignment;
	
	@FXML
	private ManageTripsController manageTripInstanceController;

	/**
	 * init UI elements
	 *
	 * @author AbdelrahmanAli
	 */
	@FXML
	public void initialize() {
		
		manageTripInstanceController.setViewAssignmentInstance(this);
		
		loadChoiceBoxes();
		// initialize the overview table by adding new columns
		overviewTable.getColumns().add(createTableColumn("Member", "memberName"));
		overviewTable.getColumns().add(createTableColumn("Member Email", "memberEmail"));
		overviewTable.getColumns().add(createTableColumn("Guide", "guideName"));
		overviewTable.getColumns().add(createTableColumn("Guide Email", "guideEmail"));
		overviewTable.getColumns().add(createTableColumn("start week", "startWeek"));
		overviewTable.getColumns().add(createTableColumn("end week", "endWeek"));
		overviewTable.getColumns().add(createTableColumn("Guide $", "totalCostForGuide"));
		overviewTable.getColumns().add(createTableColumn("Equipments $", "totalCostForEquipment"));
		overviewTable.getColumns().add(createTableColumn("Authorization code", "authorizationCode"));
		overviewTable.setPlaceholder(new Label("Oops.. No Assignment available"));

		var refundColumn = new TableColumn<TOAssignment, Integer>("Refund");
		refundColumn.setCellValueFactory(new PropertyValueFactory<>("refundedPercentageAmount"));
		refundColumn.setCellFactory(col -> new TableCell<>() {
			@Override
			public void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null && item > 0)
					setText(item + "%");
				else 
					setText("");
			}
		});
		overviewTable.getColumns().add(refundColumn);

		var statusColumn = createTableColumn("Status", "status");
		overviewTable.getColumns().add(statusColumn);

		if (sortBy.getValue() == null || sortBy.getValue() == "")
			sortBy.setValue("starting week");

		if (filterBy.getValue() == null || filterBy.getValue() == "")
			filterBy.setValue("All");

		// overview table if a refreshable element
		overviewTable.addEventHandler(ClimbSafeFxmlView.REFRESH_EVENT,
				e -> overviewTable.setItems(getSortedAssignments()));

		// register refreshable nodes
		ClimbSafeFxmlView.getInstance().registerRefreshEvent(overviewTable);

		// set cell color depends on the status
		statusColumn.setCellFactory(col -> new TableCell<>() {

			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				var row = getTableRow();
				setText(item);
				if (row.getItem() != null && row.getItem().getStatus().equals("Banned")) {
					this.setStyle("-fx-background-color: red; -fx-text-fill: white;"); // Red if banned
				} else if (row.getItem() != null && row.getItem().getStatus().equals("Finished")) {
					this.setStyle("-fx-background-color: green; -fx-text-fill: white;");
				} else {
					this.setStyle("-fx-background-color: none; -fx-text-fill: black;"); // None otherwise

				}
			}
		});

		searchAssignment.textProperty().addListener((obs, oldText, newText) -> {
			ClimbSafeFxmlView.getInstance().refresh();
		});

		overviewTable.setOpacity(0.85);
		
		overviewTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null && overviewTable.getSelectionModel().getSelectedItem() != null) {
		    	manageTripInstanceController.updateComboBox(
		    			overviewTable.getSelectionModel().getSelectedItem().getMemberEmail());
		    }
		});

	}

	/**
	 * @author AbdelrahmanAli
	 */
	private ObservableList<TOAssignment> getSortedAssignments() {
		List<TOAssignment> assignments = sortAssignments();
		return FXCollections.observableList(assignments);
	}

	/**
	 * @author AbdelrahmanAli
	 */
	@FXML
	public void initiateClicked(ActionEvent event) {
		errorLbl.setText("");
		try {
			AssignmentController.assign();
		} catch (Exception e) {
			e.printStackTrace();
			errorLbl.setText(e.getMessage());
		}
		ClimbSafeFxmlView.getInstance().refresh();
	}

	/**
	 * Event Listener on ChoiceBox[#sortBy].onAction
	 *
	 * @author AbdelrahmanAli
	 */
	@FXML
	public void sortByChanged(ActionEvent event) {
		ClimbSafeFxmlView.getInstance().refresh();
	}

	/**
	 * Event Listener on ChoiceBox[#sortBy].onAction
	 *
	 * @author AbdelrahmanAli
	 */
	@FXML
	public void filterByChanged(ActionEvent event) {
		ClimbSafeFxmlView.getInstance().refresh();
	}

	private List<TOAssignment> sortAssignments() {
		List<TOAssignment> assignments = ClimbSafeFeatureSet6Controller.getAssignments();
		List<TOAssignment> searchedList = new ArrayList<>();
		List<TOAssignment> filteredList = new ArrayList<>();

		if (searchAssignment == null || searchAssignment.getText().equals(""))
			searchedList = assignments;
		else
			for (TOAssignment assignment : assignments) {
				if (assignment.getMemberEmail().toLowerCase().contains(searchAssignment.getText())) {
					searchedList.add(assignment);
					continue;
				}
				if (assignment.getGuideEmail() != null && assignment.getGuideEmail().toLowerCase()
						.contains(searchAssignment.getText().toString().toLowerCase())) {
					searchedList.add(assignment);
					continue;
				}
				if (assignment.getMemberName().toLowerCase()
						.contains(searchAssignment.getText().toString().toLowerCase())) {
					searchedList.add(assignment);
					continue;
				}
				if (assignment.getGuideName() != null && assignment.getGuideName().toLowerCase()
						.contains(searchAssignment.getText().toString().toLowerCase())) {
					searchedList.add(assignment);
					continue;
				}
			}

		String filter = filterBy.getValue();
		if (filter.equals("All"))
			filteredList = searchedList;
		else
			for (TOAssignment assignment : searchedList)
				if (assignment.getStatus().equals(filter))
					filteredList.add(assignment);

		String compareRequest = sortBy.getValue();
		if (compareRequest.equals("Shorter Trips"))
			Collections.sort(filteredList, new ShortWeeks());
		else if (compareRequest.equals("Longer Trips"))
			Collections.sort(filteredList, new LongWeeks());
		else if (compareRequest.equals("Member A-Z"))
			Collections.sort(filteredList, new MemberNameCompare());
		else if (compareRequest.equals("Guide A-Z"))
			Collections.sort(filteredList, new GuideNameCompare());
		return filteredList;
	}

	/**
	 * Helper method
	 *
	 * @author AbdelrahmanAli
	 */
	private void loadChoiceBoxes() {
		sortList.removeAll(sortList);
		String sortByMemmberName = "Member A-Z";
		String sortByGuideName = "Guide A-Z";
		String sortByweeksAsc = "Shorter Trips";
		String sortByweeksDec = "Longer Trips";

		sortList.addAll(sortByMemmberName, sortByGuideName, sortByweeksAsc, sortByweeksDec);
		sortBy.getItems().addAll(sortList);

		filterList.removeAll(filterList);
		String all = "All";
		String statusAssigned = "Assigned";
		String statusBanned = "Banned";
		String statusPaid = "Paid";
		String statusStarted = "Started";
		String statusFinished = "Finished";
		String statusFinal = "Final";
		String statusCancelled = "Cancelled";
		filterList.addAll(all, statusAssigned, statusBanned, statusPaid, statusStarted, statusFinished, statusFinal,
				statusCancelled);
		filterBy.getItems().addAll(filterList);
	}

	/**
	 * This method will create table column
	 *
	 * @author AbdelrahmanAli
	 * @return column of requested TOAssignment
	 */
	public static TableColumn<TOAssignment, String> createTableColumn(String header, String propertyName) {
		TableColumn<TOAssignment, String> column = new TableColumn<TOAssignment, String>(header);
		column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
		return column;
	}

	// implement Comparator to compare Assignments by starting week
	private static class ShortWeeks implements Comparator<TOAssignment> {
		/**
		 * compare assignment based on number of weeks
		 *
		 * @author AbdelrahmanAli
		 */
		public int compare(TOAssignment assignment1, TOAssignment assignment2) {
			return ((assignment1.getEndWeek() - assignment1.getStartWeek())
					- (assignment2.getEndWeek() - assignment2.getStartWeek()));

		}
	}

	// implement Comparator to compare Assignments by starting week
	private static class LongWeeks implements Comparator<TOAssignment> {
		/**
		 * compare assignment based on number of weeks
		 *
		 * @author AbdelrahmanAli
		 */
		public int compare(TOAssignment assignment1, TOAssignment assignment2) {
			return ((assignment2.getEndWeek() - assignment2.getStartWeek())
					- (assignment1.getEndWeek() - assignment1.getStartWeek()));

		}
	}

	// implement Comparator to compare Assignments by Member's name
	private static class MemberNameCompare implements Comparator<TOAssignment> {
		/**
		 * compare assignment based on member name
		 *
		 * @author AbdelrahmanAli
		 */
		public int compare(TOAssignment assignment1, TOAssignment assignment2) {
			return (assignment1.getMemberName().compareTo(assignment2.getMemberName()));

		}
	}

	private static class GuideNameCompare implements Comparator<TOAssignment> {
		/**
		 * compare assignment based on guide name
		 *
		 * @author AbdelrahmanAli
		 */
		public int compare(TOAssignment assignment1, TOAssignment assignment2) {
			if (assignment1.getGuideName() == null || assignment1.getGuideName().equals(""))
				return -1;
			else if (assignment2.getGuideName() == null || assignment2.getGuideName().equals(""))
				return 1;

			return (assignment1.getGuideName().compareTo(assignment2.getGuideName()));

		}

	}

	/**
	 * @author AbdelrahmanAli
	 */
	public void refreshTab() {
		ClimbSafeFxmlView.getInstance().refresh();
		searchAssignment.setText("");
		errorLbl.setText("");
		manageTripInstanceController.refreshTab();
	}

	public void updateUI() {	
		searchAssignment.setText("");
		errorLbl.setText("");
		overviewTable.setItems(getSortedAssignments());
	}
}
