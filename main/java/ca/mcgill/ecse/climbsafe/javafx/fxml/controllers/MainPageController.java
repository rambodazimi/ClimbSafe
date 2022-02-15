package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainPageController {

	// references to all tab
	@FXML
	private Tab setupTabMain;
	@FXML
	private Tab memberTabMain;
	@FXML
	private Tab guideTabMain;
	@FXML
	private Tab equipmentTabMain;
	@FXML
	private Tab bundlesTabMain;
	@FXML
	private Tab manageTripsTabMain;
	@FXML
	private Tab assignmentTabMain;

	// references to all tab controllers
	@FXML
	private ClimbSafeSetupController setupTabController;
	@FXML
	private EquipmentController equipmentTabController;
	@FXML
	private GuideController guideTabController;
	@FXML
	private MemberManagementController memberTabController;
	@FXML
	private ManageTripsController manageTripsTabController;
	@FXML
	private ViewAssignmentController assignmentsTabController;
	@FXML
	private ViewBundlesController bundlesTabController;

	/**
	 * This is to make sure any tab will be refreshed it is selected.
	 * 
	 * @author Frederik Martin
	 */
	@FXML
	public void initialize() {

		setupTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (setupTabMain.selectedProperty().get()) {
					setupTabController.refreshTab();
				}
			}
		});

		memberTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (memberTabMain.selectedProperty().get()) {
					memberTabController.refreshTab();
				}
			}
		});

		guideTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (guideTabMain.selectedProperty().get()) {
					guideTabController.refreshTab();
				}
			}
		});

		equipmentTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (equipmentTabMain.selectedProperty().get()) {
					equipmentTabController.refreshTab();
				}
			}
		});

		bundlesTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (bundlesTabMain.selectedProperty().get()) {
					bundlesTabController.refreshTab();
				}
			}
		});

		/* manageTripsTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (manageTripsTabMain.selectedProperty().get()) {
					manageTripsTabController.refreshTab();
				}
			}
		}); */

		assignmentTabMain.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				// selection event is triggered both on entry and exit of selection
				// the condition allows only entry event to go through
				if (assignmentTabMain.selectedProperty().get()) {
					assignmentsTabController.refreshTab();
					
				}
			}
		});

	}

}
