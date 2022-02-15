package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet4Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet5Controller;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.controller.TOBundleItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

public class AddBundleController {

	@FXML
	private TextField bundleNameField;
	@FXML
	private Slider discountSlider;
	@FXML
	private TextField sliderText;
	@FXML
	private Button createBundleButton;

	@FXML
	private Text errorMessage;

	@FXML
	private TableView<TOBundleItem> pickEquipmentTable;

	@FXML
	private TableColumn<TOBundleItem, String> equipmentNameColumn;
	@FXML
	private TableColumn<TOBundleItem, Integer> quantityColumn;

	// reference to the main bundle controller
	private ViewBundlesController mainBundleController;

	// this string is used as a placeHolder in the table
	private final String placeholderText = "Doubleclick to add equipment";

	// used to synchronize the slider and the text field for discount
	private int discountVal;

	/**
	 * This method initializes all UI elements.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void initialize() {
		pickEquipmentTable.setEditable(true);

		List<String> equipmentList = new ArrayList();
		equipmentList.add("");
		equipmentList.addAll(
				ClimbSafeFeatureSet4Controller.getEquipments().stream().map(equipment -> equipment.getName()).toList());

		equipmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
		equipmentNameColumn.setCellFactory(
				ComboBoxTableCell.forTableColumn(equipmentList.toArray(new String[equipmentList.size()])));
		equipmentNameColumn.setSortable(false);
		equipmentNameColumn.setResizable(false);
		equipmentNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TOBundleItem, String>>() {

			@Override
			public void handle(CellEditEvent<TOBundleItem, String> event) {
				TOBundleItem item = event.getRowValue();// get row object
				item.setEquipmentName(event.getNewValue());// save new name to model
				if (item.getEquipmentName() == "") {
					pickEquipmentTable.getItems().remove(item);
				}
				equipmentListChanged();// add new row if necessary
			}
		});

		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		quantityColumn.setSortable(false);
		quantityColumn.setResizable(false);
		quantityColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TOBundleItem, Integer>>() {

			@Override
			public void handle(CellEditEvent<TOBundleItem, Integer> event) {
				TOBundleItem item = event.getRowValue();// get row object
				item.setQuantity(event.getNewValue());// save new name to model
			}
		});

		pickEquipmentTable.getItems().add(new TOBundleItem(placeholderText, 0, 0, 0));
	}
	
	/**
	 * This method is used to refresh the content of the combobox used for chossing equipment.
	 * 
	 * @author FrederikMartin
	 */
	public void refreshEquipmentCombobox() {
		List<String> equipmentList = new ArrayList();
		equipmentList.add("");
		equipmentList.addAll(
				ClimbSafeFeatureSet4Controller.getEquipments().stream().map(equipment -> equipment.getName()).toList());
		
		equipmentNameColumn.setCellFactory(
				ComboBoxTableCell.forTableColumn(equipmentList.toArray(new String[equipmentList.size()])));
	}

	/**
	 * Event Listener for "create bundle" button. This methods calls the controller
	 * to add a new bundle. Upon error, a message will be shown to the user. If
	 * creating the bundle is successful, the UI for creating a bundle is closed and
	 * the main bundle UI is refreshed.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onCreateBundle(ActionEvent event) {
		try {
			// Last item is removed because it is a placeholder item
			List<String> eqNames = pickEquipmentTable.getItems().stream().map(toBundle -> toBundle.getEquipmentName())
					.collect(Collectors.toList());
			eqNames.remove(eqNames.size() - 1);// remove last item

			List<Integer> eqQty = pickEquipmentTable.getItems().stream().map(toBundle -> toBundle.getQuantity())
					.collect(Collectors.toList());
			eqQty.remove(eqQty.size() - 1);// remove last item

			ClimbSafeFeatureSet5Controller.addEquipmentBundle(bundleNameField.getText(), discountVal, eqNames, eqQty);
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			errorMessage.setText(e.getMessage());
			return;// close UI only if adding bundle was successful
		}
		// close UI
		mainBundleController.onCloseCreatePane();
		// refresh view
		mainBundleController.refreshTable();
	}

	/**
	 * This method updates the discount value in the text field to reflect the one
	 * in the slider
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onSliderValueChanged() {
		discountVal = (int) discountSlider.getValue();
		sliderText.setText(Integer.toString(discountVal));
	}

	/**
	 * This method updates the discount value in the slider to reflect the on in the
	 * text field. In the case where a character other than numbers is in the text
	 * field, the text field value is reset to the previous valid discount value.
	 * 
	 * @author FrederikMartin
	 */
	@FXML
	public void onDiscountTextChanged() {
		int newVal = 0;
		try {

			newVal = Integer.parseInt(sliderText.getText());
		} catch (NumberFormatException e) {
			sliderText.setText(Integer.toString(discountVal));
			discountSlider.setValue(discountVal);
			return;
		}

		discountVal = newVal;
		discountSlider.setValue(discountVal);
	}

	/**
	 * Resets all the field of the add UI.
	 * 
	 * @author FrederikMartin
	 */
	public void resetFields() {
		bundleNameField.setText("");
		discountSlider.setValue(0);
		sliderText.setText("0");
		pickEquipmentTable.getItems().clear();
		pickEquipmentTable.getItems().add(new TOBundleItem(placeholderText, 0, 0, 0));
		errorMessage.setText("");
	}

	/**
	 * This method needs to be called when the equipment table is changed to ensure
	 * it is always possible to add new equipment in the bundle.
	 * 
	 * @author FrederikMartin
	 */
	private void equipmentListChanged() {

		var items = pickEquipmentTable.getItems();
		
		//if there are no rows, we need to add a placeholder
		if (items.size() == 0) {
			items.add(new TOBundleItem(placeholderText, 0, 0, 0));
			return;
		}

		var lastItem = items.get(items.size() - 1);

		//if the last row isnt a placeholder, we need to add one
		if (lastItem.getEquipmentName() == placeholderText)
			return;
		else
			items.add(new TOBundleItem(placeholderText, 0, 0, 0));

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
