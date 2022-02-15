package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet2Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.controller.TOAssignment;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ManageTripsController {

	@FXML
	private Button cancelTripButton;
	@FXML
	private Button finishButton;
	@FXML
	private ComboBox<String> memberComboBox;
	@FXML
	private TextField weekField;
	@FXML
	private Button startButton;
	@FXML
	private Button payButton;
	@FXML
	private TextField codeField;
	
	
	private ViewAssignmentController viewAssignmentInstance;

	private TOAssignment TOAssignment;

	private ObservableList<String> comboBoxList = FXCollections.observableArrayList();

	private int weekStarted = 0;
	
	@FXML
	private Label errorLabelForTrips;

	/**
	 * @author KarimAtoui
	 */
	@FXML
	public void initialize() {

		errorLabelForTrips.setTextFill(Color.RED);
		errorLabelForTrips.setVisible(false);
		// Added by Abdul
		if (ClimbSafeFeatureSet2Controller.getMembers().size() < 1)
			this.memberComboBox.setValue("No members");
		else
			this.memberComboBox.setValue("Pick member");

		ClimbSafeFxmlView.getInstance().registerRefreshEvent(errorLabelForTrips);
		ClimbSafeFxmlView.getInstance().registerRefreshEvent(weekField);
		ClimbSafeFxmlView.getInstance().registerRefreshEvent(codeField);

		errorLabelForTrips.setStyle("-fx-text-fill: red;");
		errorLabelForTrips.addEventHandler(ClimbSafeFxmlView.REFRESH_EVENT,
				e -> errorLabelForTrips.setText(""));


		weekField.addEventHandler(ClimbSafeFxmlView.REFRESH_EVENT,
				e -> weekField.setText(""));

		codeField.addEventHandler(ClimbSafeFxmlView.REFRESH_EVENT,
				e -> codeField.setText(""));

	}

	/**
	 * @author KarimAtoui
	 */
	// Event Listener on Button[#startButton].onMouseClicked
	@FXML
	public void startClick(MouseEvent event) {
		errorLabelForTrips.setVisible(true);
		if (!verifyString(this.weekField.getText())) {
			return;
		}
		try {
			this.weekStarted = Integer.parseInt(this.weekField.getText());
		} catch (Exception e) {
			errorLabelForTrips.setText("Invalid integer value");
			errorLabelForTrips.setStyle("-fx-text-fill: red;");
		}

		try {
			AssignmentController.startTrip(this.weekStarted);
		} catch (Exception e) {
		}
		errorLabelForTrips.setText("Successfully start week " + this.weekStarted);
		errorLabelForTrips.setStyle("-fx-text-fill: green;");
		update();
	}

	/**
	 * @author KarimAtoui
	 * when user chooses a member in the combobox
	 */
	// Event Listener on ComboBox[#memberComboBox].onAction
	@FXML
	public void memberChoiceDone(Event event) {

		for (TOAssignment aTOAssignment : ClimbSafeFeatureSet6Controller.getAssignments()) {
			if (aTOAssignment.getMemberEmail().equals(memberComboBox.getValue())) {
				this.TOAssignment = aTOAssignment;
				break;
			}
		}
		
		this.errorLabelForTrips.setText("");
		this.codeField.setText("");
	}

	/**
	 * @author KarimAtoui
	 * update the elements in the combobox when it is clicked
	 */
	@FXML
	public void comboBoxClicked(MouseEvent event) {
		this.comboBoxList.clear();
		for (TOAssignment aTOAssignment : ClimbSafeFeatureSet6Controller.getAssignments()) {
			this.comboBoxList.add(aTOAssignment.getMemberEmail());
		}
		this.memberComboBox.setItems(comboBoxList);

	}

	/**
	 * @author KarimAtoui
	 */
	// Event Listener on Button[#finishCancelButton].onMouseClicked
	@FXML
	public void cancelClicked(MouseEvent event) {
		errorLabelForTrips.setVisible(true);

		if (this.TOAssignment == null)
			return;
		try {
			AssignmentController.cancelMemberTrip(this.TOAssignment.getMemberEmail());
			errorLabelForTrips.setText("Succesfully cancelled trip");
			errorLabelForTrips.setStyle("-fx-text-fill: green;");
		} catch (InvalidInputException e) {
			errorLabelForTrips.setText(e.getMessage());
			errorLabelForTrips.setStyle("-fx-text-fill: red;");
		}
		update();
	}

	/**
	 * @author KarimAtoui
	 */
	// Event Listener on Button[#finishCancelButton].onMouseClicked
	@FXML
	public void finishClicked(MouseEvent event) {
		errorLabelForTrips.setVisible(true);
		if (this.TOAssignment == null)
			return;
		try {
			AssignmentController.finishTrip(this.TOAssignment.getMemberEmail());
			errorLabelForTrips.setText("Succesfully finished trip");
			errorLabelForTrips.setStyle("-fx-text-fill: green;");
		} catch (InvalidInputException e) {
			errorLabelForTrips.setText(e.getMessage());
			errorLabelForTrips.setStyle("-fx-text-fill: red;");
		}
		update();
	}

	/**
	 * @author KarimAtoui
	 */
	// Event Listener on Button[#payButton].onMouseClicked
	@FXML
	public void payMouseClicked(MouseEvent event) {
		errorLabelForTrips.setVisible(true);

		if (TOAssignment == null)
			return;

		String code = codeField.getText();

		try {
			AssignmentController.payTrip(this.TOAssignment.getMemberEmail(), code);
			errorLabelForTrips.setText("Paid successfully");
			errorLabelForTrips.setStyle("-fx-text-fill: green;");
		} catch (InvalidInputException e) {
			errorLabelForTrips.setText(e.getMessage());
			errorLabelForTrips.setStyle("-fx-text-fill: red;");
		}
		update();
	}

	/**
	 * @author KarimAtoui
	 * UPdates TOAssignment
	 */
	private void update() {

		for (TOAssignment aTOAssignment : ClimbSafeFeatureSet6Controller.getAssignments()) {
			if (this.TOAssignment != null
					&& aTOAssignment.getMemberEmail().equals(this.TOAssignment.getMemberEmail())) {
				this.TOAssignment = aTOAssignment;
			}
		}
		viewAssignmentInstance.updateUI();
	}

	/**
	 * @author KarimAtoui
	 * REfresh the page
	 */
	private void refresh() {
		ClimbSafeFxmlView.getInstance().refresh();
	}

	/**
	 * @author KarimAtoui
	 * verify if there is only integers in the string
	 */
	private boolean verifyString(String s) {
		if (s.isBlank())
			return false;

		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}
	
	public void setViewAssignmentInstance(ViewAssignmentController viewAssignmentInstance) {
		this.viewAssignmentInstance=viewAssignmentInstance;
	}

	/**
	 * @author KarimAtoui
	 * actions that are done when the tab is clicked
	 */
	public void refreshTab() {
		this.TOAssignment = null;
		errorLabelForTrips.setText("");
		memberComboBox.setValue("Pick member");
		refresh();
	}

	public void updateComboBox(String memberEmail) {
		if (memberEmail != null && !memberEmail.equals(""))
		memberComboBox.setValue(memberEmail);	
	}
}
