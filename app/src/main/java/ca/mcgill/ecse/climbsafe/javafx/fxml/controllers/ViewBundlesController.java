package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet5Controller;
import ca.mcgill.ecse.climbsafe.controller.TOBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class ViewBundlesController {

	@FXML
	private TableView<TOBundle> bundleTable;

	// references to columns in the table
	@FXML
	private TableColumn<TOBundle, String> nameColumn;
	@FXML
	private TableColumn<TOBundle, String> discountColumn;
	@FXML
	private TableColumn<TOBundle, String> priceColumn;

	@FXML
	private GridPane createPane;// root UI object for adding a new bundle
	@FXML
	private GridPane editPane;// root UI object for editing a bundle
	@FXML
	private GridPane deletePane;// root UI object for deleting a bundle

	// references to buttons
	@FXML
	private Button editBundleButton;
	@FXML
	private Button deleteBundleButton;

	// Popup panes controller references
	@FXML
	private AddBundleController addPaneController;// create bundle UI
	@FXML
	private EditBundleController changePaneController;// edit bundle UI
	@FXML
	private DeleteBundleController removePaneController;// delete bundle UI

	/**
	 * This method initializes all UI elements.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void initialize() {

		// update controller references
		addPaneController.setMainBundleController(this);
		changePaneController.setMainBundleController(this);
		removePaneController.setMainBundleController(this);

		createPane.setVisible(false);
		editPane.setVisible(false);
		deletePane.setVisible(false);

		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setResizable(false);
		discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
		discountColumn.setResizable(false);
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPricePerWeek"));
		priceColumn.setResizable(false);

		// disable edit and delete buttons when nothing is selected
		editBundleButton.setDisable(true);
		deleteBundleButton.setDisable(true);
		bundleTable.getSelectionModel().selectedItemProperty()
				.addListener((value, previousSelection, currentSelection) -> onSelectRow(currentSelection));

		//allow editing by double clicking a bundle
		bundleTable.setOnMouseClicked(event -> 
			{ if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() >= 2)
				onEditBundle(null);});
		
		// fill table
		bundleTable.getItems().addAll(ClimbSafeFeatureSet5Controller.getEquipmentBundles());
	}

	/**
	 * Event Listener for selecting a row. The edit and delete button will only enabled if a row is selected.
	 * 
	 * @author FrederikMartin
	 */
	private void onSelectRow(TOBundle bundle) {
		editBundleButton.setDisable(bundle == null);
		deleteBundleButton.setDisable(bundle == null);
	}

	/**
	 * Event Listener for "add bundle" button. The UI for creating a bundle will be
	 * shown.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onAddBundle(ActionEvent event) {
		createPane.setVisible(true);
	}

	/**
	 * Event Listener for "edit bundle" button. The UI for editing a bundle will be
	 * shown.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onEditBundle(ActionEvent event) {
		TOBundle bundle = bundleTable.getSelectionModel().getSelectedItem();
		if (bundle == null)
			return;// cant edit if nothing is selected
		changePaneController.setEditObject(bundle);
		editPane.setVisible(true);
	}

	/**
	 * Event Listener for "delete bundle" button. The UI for deleting a bundle will
	 * be shown.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onDeleteBundle(ActionEvent event) {
		TOBundle bundle = bundleTable.getSelectionModel().getSelectedItem();
		if (bundle == null)
			return;// cant delete if nothing is selected
		removePaneController.setBundleName(bundle.getName());
		deletePane.setVisible(true);
	}

	/**
	 * Calling this methods will close the UI for adding a new bundle. All fields
	 * will be reset, and the UI will be hidden.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onCloseCreatePane() {
		addPaneController.resetFields();
		createPane.setVisible(false);
	}

	/**
	 * This method clears any previous value and calls the controller to load all
	 * equipment bundles in the table.
	 * 
	 * @author FrederikMartin
	 */
	public void refreshTable() {
		bundleTable.getItems().clear();
		bundleTable.getItems().addAll(ClimbSafeFeatureSet5Controller.getEquipmentBundles());
	}

	/**
	 * Calling this methods will close the UI for editing a bundle. All fields will
	 * be reset, and the UI will be hidden.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onCloseEditPane() {
		changePaneController.resetFields();
		editPane.setVisible(false);
	}

	/**
	 * Calling this methods will close the UI for deleting a bundle.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onCloseDeletePane() {
		deletePane.setVisible(false);
	}
	
	/**
	 * This method is called when clicking on a tab.
	 * It will refresh all the tab's content with the current data.
	 * 
	 * @author Frederik Martin
	 */
	public void refreshTab() {
		addPaneController.refreshEquipmentCombobox();
		changePaneController.refreshEquipmentCombobox();
	}

}
