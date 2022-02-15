package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet1Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet2Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet3Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.TOAssignment;
import ca.mcgill.ecse.climbsafe.controller.TOClimbSafe;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * 
 * @author Dawei Zhou
 *
 */
public class ClimbSafeSetupController {

	@FXML
	private TextField costTextField;

	@FXML
	private TextField numWeeksTextField;

	@FXML
	private Button saveButton;

	@FXML
	private Button clearButton;

	@FXML
	private Label savePromptLabel;

	@FXML
	private DatePicker startDatePicker;

	@FXML
	private Label guideCntLabel;

	@FXML
	private Label memberCntLabel;

	@FXML
	private Label bannedCntLabel;

	/**
	 * 
	 * @author Dawei Zhou
	 * @param null This method set the prompt text and disable the textfield of
	 *             datepicker. Set the size of Datepicker
	 */
	@FXML
	public void initialize() {

		guideCntLabel.setText(ClimbSafeFeatureSet3Controller.getGuides().size() + "");

		memberCntLabel.setText(ClimbSafeFeatureSet2Controller.getMembers().size() +"");

		int banned = 0;

		for (TOAssignment m : ClimbSafeFeatureSet6Controller.getAssignments())
			if (m.getStatus().equals("Banned")) 
				banned++;
		

		bannedCntLabel.setText(Integer.toString(banned));

		TOClimbSafe climbSafe = ClimbSafeFeatureSet1Controller.getClimbSafe();
		

		int cost = climbSafe.getGuideCost();
		Date begDate = climbSafe.getDate();
		int weeks = climbSafe.getNumOfWeeks();
		if (!(cost == 0 && weeks == 0 && begDate.compareTo(Date.valueOf(LocalDate.MIN)) == 0)) { // don't set the prompt
																									// if it's the
																									// initial setup

			costTextField.setPromptText(String.format("current cost: %d $", cost));

			numWeeksTextField.setPromptText(String.format("current number of weeks: %d", weeks));

			startDatePicker.setPromptText(String.format("current start date: %s", begDate));
		}
	}

	/**
	 * 
	 * @author Dawei Zhou
	 * @param event When the user click the save button, this method get input from
	 *              the user and update the program setting if the input is valid,
	 *              pop error message if the input is invalid.
	 */
	@FXML
	void saveClicked(ActionEvent event) {

		TOClimbSafe climbSafe = ClimbSafeFeatureSet1Controller.getClimbSafe();
		

		int cost;
		Date date;
		int nWeeks;
		try {
			// the user can choose to leave the box blank
			// if he/she does not need to change it

			if (costTextField.getText().isBlank())
				cost = climbSafe.getGuideCost();
			else
				cost = Integer.parseInt(costTextField.getText());

			if (numWeeksTextField.getText().isBlank())
				nWeeks = climbSafe.getNumOfWeeks();
			else
				nWeeks = Integer.parseInt(numWeeksTextField.getText());

			if (startDatePicker.getValue() == null)
				date = climbSafe.getDate();
			else
				date = Date.valueOf(startDatePicker.getValue());
			try {
				ClimbSafeFeatureSet1Controller.setup(date, nWeeks, cost);

				costTextField.setText("");

				numWeeksTextField.setText("");

				startDatePicker.setValue(null);

				savePromptLabel.setText("Successfully saved!");

				savePromptLabel.setStyle("-fx-text-fill: green;");

				savePromptLabel.setVisible(true);

				initialize();
			} catch (Exception e) {
				savePromptLabel.setText(e.getMessage());

				savePromptLabel.setStyle("-fx-text-fill: red;");

				savePromptLabel.setVisible(true);
			}

		} catch (Exception e) { // only get here when the input is not integer

			savePromptLabel.setText("Please enter integer value for guide cost and number of weeks.");

			savePromptLabel.setStyle("-fx-text-fill: red;");

			savePromptLabel.setVisible(true);
		}

		ClimbSafeFxmlView.getInstance().refresh();

	}

	@FXML
	void clearClicked(ActionEvent event) {

		refresh();

	}

	@FXML
	void refreshClicked(ActionEvent event) {
		
		TOClimbSafe climbSafe = ClimbSafeFeatureSet1Controller.getClimbSafe();
		

		int cost = climbSafe.getGuideCost();
		Date begDate = climbSafe.getDate();
		int weeks = climbSafe.getNumOfWeeks();

		guideCntLabel.setText(ClimbSafeFeatureSet3Controller.getGuides().size() + "");

		memberCntLabel.setText(ClimbSafeFeatureSet2Controller.getMembers().size() + "");


		int banned = 0;

		for (TOAssignment m : ClimbSafeFeatureSet6Controller.getAssignments())
			if (m.getStatus().equals("Banned")) 
				banned++;
		bannedCntLabel.setText(Integer.toString(banned));
	}

	/*
	 * @author Dawei Zhou 
	 * This method do a refresh on the setup panel of this page.
	 */
	private void refresh() {
		costTextField.setText("");

		numWeeksTextField.setText("");

		startDatePicker.setValue(null);

		savePromptLabel.setVisible(false);
	}

	public void refreshTab() {
		initialize();

		refresh();

	}

}
