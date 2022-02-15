package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class DeleteBundleController {

	@FXML
	private Button confirmButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Text promptText;

	private final String prompt = "Are you sure you want to delete ";
	private String bundleName;

	// reference to the main bundle controller
	private ViewBundlesController mainBundleController;

	/**
	 * Event Listener for "confirm delete bundle" button. The selected bundle will
	 * be deleted and the UI to delete a bundle will be closed.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onConfirmDelete(ActionEvent event) {
		try {
			ClimbSafeFeatureSet6Controller.deleteEquipmentBundle(bundleName);
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage());
		}
		// close UI
		mainBundleController.onCloseDeletePane();
		// refresh view
		mainBundleController.refreshTable();
	}

	/**
	 * Event Listener for "cancel delete bundle" button. The UI to delete a bundle
	 * will be closed.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onCancelDelete(ActionEvent event) {
		mainBundleController.onCloseDeletePane();
	}

	/**
	 * This method is used to set the name of the bundle to be deleted. The name of
	 * the bundle displayed in the UI will be updated.
	 * 
	 * @author FrederikMartin
	 */
	public void setBundleName(String name) {
		bundleName = name;
		promptText.setText(prompt + bundleName + '?');
	}

	/**
	 * This method needs to be called by the main bundle view controller to ensure
	 * this controller has a reference to it.
	 * 
	 * @author FrederikMartin
	 */
	public void setMainBundleController(ViewBundlesController mainBundleController) {
		this.mainBundleController = mainBundleController;
	}

}
