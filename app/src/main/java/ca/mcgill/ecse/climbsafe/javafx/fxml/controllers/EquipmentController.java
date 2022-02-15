package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet4Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet6Controller;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.controller.TOEquipment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EquipmentController {
	
	public static boolean confirmation = false;

	    @FXML
	    private Button addButton;
	    @FXML
	    private TextField addName;
	    @FXML
	    private TextField addPricePerWeek;
	    @FXML
	    private TextField addWeight;
	    @FXML
	    private Button deleteButton;
	    @FXML
	    private TextField deleteName;
	    @FXML
	    private Button updateButton;
	    @FXML
	    private TextField updateNewName;
	    @FXML
	    private TextField updateNewPricePerWeek;
	    @FXML
	    private TextField updateNewWeight;
	    @FXML
	    private TextField updateOldName;
	    
	    @FXML
	    private Label addMessage;
	    @FXML
	    private Label updateMessage;
	    @FXML
	    private Label deleteMessage;
	    @FXML
	    private Label addSuccessful;
	    @FXML
	    private Label updateSuccessful;
	    @FXML
	    private Label deleteSuccessful;
	    
	    @FXML
	    private TableView<TOEquipment> equipmentTable;
	    @FXML
	    private TableColumn<TOEquipment, String> nameColumn;
	    @FXML
	    private TableColumn<TOEquipment, Integer> weightColumn;
	    @FXML
	    private TableColumn<TOEquipment, Integer> pricePerWeekColumn;
	    
	    @FXML
	    private GridPane equipmentGrid;
	    
	    /**
	     * Initialize the page
	     * @author rambodazimi
	     */
	    public void initialize() {
	    	addMessage.setVisible(false);
	    	updateMessage.setVisible(false);
	    	deleteMessage.setVisible(false);
	    	addSuccessful.setVisible(false);
	    	updateSuccessful.setVisible(false);
	    	deleteSuccessful.setVisible(false);
	    	equipmentTable.setOpacity(0.85);
	    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
			weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
			pricePerWeekColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerWeek"));
			equipmentTable.getItems().addAll(ClimbSafeFeatureSet4Controller.getEquipments());
			equipmentTable.setPlaceholder(new Label("Oops.. No equipment available"));
	    }
	     
	    /**
	     * This method will refresh the table every time the admin wants to add/delete/update any equipment
	     * @author FredMartin
	     */
		public void refreshTable() {
			equipmentTable.getItems().clear();
			equipmentTable.getItems().addAll(ClimbSafeFeatureSet4Controller.getEquipments());
		}   
		
		/**
		 * Auto fill the fields for update/delete an equipment when a row in the table is selected
		 * @author rambodazimi
		 * @param event
		 */
	    @FXML
	    public void rowClicked(MouseEvent event) {
	    	refreshTab();
	    	if (equipmentTable.getSelectionModel().getSelectedItem() != null) {
	    	updateOldName.setText(equipmentTable.getSelectionModel().getSelectedItem().getName());
	    	updateNewName.setText(equipmentTable.getSelectionModel().getSelectedItem().getName());
	    	Integer w = equipmentTable.getSelectionModel().getSelectedItem().getWeight();
	    	String ww = w.toString();
	    	updateNewWeight.setText(ww);
	    	Integer p = equipmentTable.getSelectionModel().getSelectedItem().getPricePerWeek();
	    	String pp = p.toString();
	    	updateNewPricePerWeek.setText(pp);
	    	deleteName.setText(equipmentTable.getSelectionModel().getSelectedItem().getName());
	    	}
	    }
		
		/**
		   * Creates a popup window.
		   *
		   * @param title: title of the popup window
		   * @param message: message to display
		   */
		  public static void makePopupWindow(String title, String message) {
		    Stage dialog = new Stage();
		    dialog.initModality(Modality.APPLICATION_MODAL);
		    VBox dialogPane = new VBox();
		    // create UI elements
		    Text text = new Text(message);
		    Button okButton = new Button("OK");
		    okButton.setOnAction(a -> dialog.close());
		    // display the popup window
		    int innerPadding = 10; // inner padding/spacing
		    int outerPadding = 100; // outer padding
		    dialogPane.setSpacing(innerPadding);
		    dialogPane.setAlignment(Pos.CENTER);
		    dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
		    dialogPane.getChildren().addAll(text, okButton);
		    Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
		    dialog.setScene(dialogScene);
		    dialog.setTitle(title);
		    dialog.show();
		  }
		
		  /**
		   * very similar to makePopupWindow, but it's for confirmation message when admin wants to delete a guide
		   * @author rambodazimi
		   * @param title
		   * @param message
		   */
		  public static void confirmationPopupWindow(String title, String message) {
			  confirmation = false;
			    Stage dialog = new Stage();
			    dialog.initModality(Modality.APPLICATION_MODAL);
			    VBox dialogPane = new VBox();
			    // create UI elements
			    Text text = new Text(message);
			    Button okButton = new Button("Yes");
			    okButton.setOnAction(new EventHandler<ActionEvent>(){
			        @Override public void handle( ActionEvent e ) {
			        	confirmation = true;
			            dialog.close();
			        }
			        });
			    // display the popup window
			    int innerPadding = 10; // inner padding/spacing
			    int outerPadding = 100; // outer padding
			    dialogPane.setSpacing(innerPadding);
			    dialogPane.setAlignment(Pos.CENTER);
			    dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
			    dialogPane.getChildren().addAll(text, okButton);
			    Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
			    dialog.setScene(dialogScene);
			    dialog.setTitle(title);
			    dialog.showAndWait();
			  }

		/**
		 * When add button is clicked, it will first check each field to be in a correct form. Then, it will call addEquipment method in the controller
		 * @author rambodazimi
		 * @param event
		 */
	    @FXML
	    public void addButtonClick(ActionEvent event) {
	    	refreshMessages();
	    	 String name = addName.getText();
			 String weight = addWeight.getText();
			 String pricePerWeek = addPricePerWeek.getText();
			    
			    if (name == null || name.trim().isEmpty()) {
			    	addMessage.setText("Name cannot be empty!");
			    	addSuccessful.setVisible(false);
			    	addMessage.setVisible(true);
			    	return;
			    }
			    if (weight == null || weight.trim().isEmpty()) {
			    	addMessage.setText("Weight cannot be empty!");
			    	addSuccessful.setVisible(false);
			    	addMessage.setVisible(true);
			    	return;
			    }
			    if (pricePerWeek == null || pricePerWeek.trim().isEmpty()) {
			    	addMessage.setText("Price per week cannot be empty!");
			    	addSuccessful.setVisible(false);
			    	addMessage.setVisible(true);
			    	return;
			    }
			        
			    //check the correctness of weight and price per week
			    Integer w = tryParse(weight);
			    Integer ppw = tryParse(pricePerWeek);
			    
			    	try {
			    		if(w == null || ppw == null) {
					    	addMessage.setText("Only put integer in weight and price.");
					    	addSuccessful.setVisible(false);
					    	addMessage.setVisible(true);
					    	return;
			    		}
						ClimbSafeFeatureSet4Controller.addEquipment(name, Integer.parseInt(weight), Integer.parseInt(pricePerWeek));
			    		refreshTable();	//add the equipment in the table
						addName.setText("");
						addWeight.setText("");
						addPricePerWeek.setText("");
				    	addSuccessful.setText("Successful!");
				    	addMessage.setVisible(false);
				    	addSuccessful.setVisible(true);
					} catch (InvalidInputException e) {
				    	addMessage.setText(e.getMessage());
				    	addSuccessful.setVisible(false);
				    	addMessage.setVisible(true);
				    }   
	    }

	    @FXML
	    void updateButtonClicked(ActionEvent event) {
	    	 refreshMessages();
	    	 String oldName = updateOldName.getText();
			 String newName = updateNewName.getText();
			 String newWeight = updateNewWeight.getText();
			 String newPricePerWeek = updateNewPricePerWeek.getText();

			    
			    if (oldName == null || oldName.trim().isEmpty()) {
			    	updateMessage.setText("Old name cannot be empty!");
			    	updateSuccessful.setVisible(false);
			    	updateMessage.setVisible(true);			    	
			    	return;
			    }
			    if (newName == null || newName.trim().isEmpty()) {
			    	updateMessage.setText("New name cannot be empty!");
			    	updateSuccessful.setVisible(false);
			    	updateMessage.setVisible(true);
			    	return;
			    }
			    if (newWeight == null || newWeight.trim().isEmpty()) {
			    	updateMessage.setText("New weight cannot be empty!");
			    	updateSuccessful.setVisible(false);
			    	updateMessage.setVisible(true);
			    	return;
			    }
			    if (newPricePerWeek == null || newPricePerWeek.trim().isEmpty()) {
			    	updateMessage.setText("New price per week cannot be empty!");
			    	updateSuccessful.setVisible(false);
			    	updateMessage.setVisible(true);
			    	return;
			    }
			        
			    //check the correctness of new weight and new price per week
			    Integer nw = tryParse(newWeight);
			    Integer nppw = tryParse(newPricePerWeek);
			    
			    	try {
			    		if(nw == null || nppw == null) {
					    	updateMessage.setText("Only put integer in weight and price");
					    	updateSuccessful.setVisible(false);
					    	updateMessage.setVisible(true);
					    	return;
			    		}
						ClimbSafeFeatureSet4Controller.updateEquipment(oldName, newName, Integer.parseInt(newWeight), Integer.parseInt(newPricePerWeek));
			    		refreshTable(); //update the equipment in the table
						updateOldName.setText("");
						updateNewWeight.setText("");
						updateNewName.setText("");
						updateNewPricePerWeek.setText("");
				    	updateSuccessful.setText("Successful!");
				    	updateMessage.setVisible(false);
				    	updateSuccessful.setVisible(true);
					} catch (InvalidInputException e) {
				    	updateMessage.setText(e.getMessage());
				    	updateSuccessful.setVisible(false);
				    	updateMessage.setVisible(true);
					}
			    	deleteName.setText("");
	    }
	    
	    @FXML
	    void deleteButtonClicked(ActionEvent event) {
	    	refreshMessages();
	    	String name = deleteName.getText();
	    	if(name == null || name.trim().isEmpty()) {
		    	deleteMessage.setText("Name cannot be empty!");
		    	deleteSuccessful.setVisible(false);
		    	deleteMessage.setVisible(true);
		    	return;
	    	}
	    	
	    	try {
	    		confirmationPopupWindow("Warning" , "Are you sure you want to delete the equipment?");
	    		if(confirmation == true) {
		    		ClimbSafeFeatureSet6Controller.deleteEquipment(name);
					deleteName.setText("");
					refreshTable();	//delete the equipment from the table
			    	deleteSuccessful.setText("If the equipment exists it's deleted!");
			    	deleteMessage.setVisible(false);
			    	deleteSuccessful.setVisible(true);
	    		}
	    	}  catch (InvalidInputException e) {
		    	deleteMessage.setText(e.getMessage());
		    	deleteSuccessful.setVisible(false);
		    	deleteMessage.setVisible(true);
		    }
	    	updateOldName.setText("");
	    	updateNewName.setText("");
	    	updateNewWeight.setText("");
	    	updateNewPricePerWeek.setText("");
	    }
  
	    /**
	     * This helper method checks the correctness of format of the number. If not, return null
	     * @author rambodazimi
	     * @param str
	     * @return
	     */
	    public static Integer tryParse(String str) {
	    	  try {
	    	    return Integer.parseInt(str);
	    	  } catch (NumberFormatException e) {
	    	    return null;
	    	  }
	    }
	    
	    
	    public void refreshMessages() {
		    addMessage.setText("");
		    updateMessage.setText("");
		    deleteMessage.setText("");
		    addSuccessful.setText("");
		    updateSuccessful.setText("");
		    deleteSuccessful.setText("");
	    }

		public void refreshTab() {
		    addName.setText("");
		    addPricePerWeek.setText("");
		    addWeight.setText("");
		    deleteName.setText("");
		    updateNewName.setText("");
		    updateNewPricePerWeek.setText("");
		    updateNewWeight.setText("");
		    updateOldName.setText("");
		    addMessage.setText("");
		    updateMessage.setText("");
		    deleteMessage.setText("");
		    addSuccessful.setText("");
		    updateSuccessful.setText("");
		    deleteSuccessful.setText("");
		}
}
