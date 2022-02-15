package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet1Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet3Controller;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.controller.TOGuide;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class GuideController {
		  
	public static boolean confirmation = false;
	
	  @FXML
	  private TextField registerEmail;
	  @FXML
	  private TextField registerPhoneNumber;
	  @FXML
	  private TextField registerPassword;
	  @FXML
	  private TextField registerName;
	  @FXML
	  private TextField updateEmail;
	  @FXML
	  private TextField updateNewPassword;
	  @FXML
	  private TextField updateNewName;
	  @FXML
	  private TextField updateNewContact;
	  @FXML
	  private TextField deleteEmail;
	  @FXML
	  private Button registerButton;
	  @FXML
	  private Button updateButton;
	  @FXML
	  private Button deleteButton;
	  
	  @FXML
	  private Label registerMessage;
	  @FXML
	  private Label updateMessage;
	  @FXML
	  private Label deleteMessage;
	  @FXML
	  private Label registerSuccessful;
	  @FXML
	  private Label updateSuccessful;
	  @FXML
	  private Label deleteSuccessful;
	    
	  @FXML
	  private TableView<TOGuide> guideTable;
	  @FXML
	  private TableColumn<TOGuide, String> emailColumn;
	  @FXML
	  private TableColumn<TOGuide, String> contactColumn;
	  @FXML
	  private TableColumn<TOGuide, String> nameColumn;
	  @FXML
	  private TableColumn<TOGuide, String> passwordColumn;
	  
	  @FXML
	  private CheckBox showPasswords;
	  
	  @FXML
	  private GridPane guideGrid2;
	    
	  /**
	   * Initialize the guide page
	   * @author rambodazimi
	   */
	  public void initialize() {
		  passwordColumn.setVisible(false);
		  registerMessage.setVisible(false);
	      updateMessage.setVisible(false);
	      deleteMessage.setVisible(false);
	      registerSuccessful.setVisible(false);
	      updateSuccessful.setVisible(false);
	      deleteSuccessful.setVisible(false);
	      guideTable.setOpacity(0.85);
		  nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		  contactColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyContact"));
		  emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));		
		  passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		  guideTable.getItems().addAll(ClimbSafeFeatureSet3Controller.getGuides());
		  guideTable.setPlaceholder(new Label("Oops.. No guides available"));
	  }
	  
	    /**
	     * This method will refresh the table every time the admin wants to add/delete/update any guide
	     * @author FredMartin
	     */
		public void refreshTable() {
			guideTable.getItems().clear();
			guideTable.getItems().addAll(ClimbSafeFeatureSet3Controller.getGuides());
		}
		
		
		/**
		 * Auto fill the fields for update/delete a guide when a row in the table is selected
		 * @author rambodazimi
		 * @param event
		 */
	    @FXML
	    public void rowClicked(MouseEvent event) {
	    	refreshTab();
	    	if (guideTable.getSelectionModel().getSelectedItem() != null) {
	    	updateEmail.setText(guideTable.getSelectionModel().getSelectedItem().getEmail());
	    	deleteEmail.setText(guideTable.getSelectionModel().getSelectedItem().getEmail());
	    	updateNewPassword.setText(guideTable.getSelectionModel().getSelectedItem().getPassword());
	    	updateNewName.setText(guideTable.getSelectionModel().getSelectedItem().getName());
	    	updateNewContact.setText(guideTable.getSelectionModel().getSelectedItem().getEmergencyContact());
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
	   * When register button is clicked, it will first check each field to be in a correct form. Then, it will call registerGuide method in the controller
	   * @author rambodazimi
	   * @param event
	   */
	  @FXML
	  public void registerButtonClicked(ActionEvent event) {
		    refreshMessages();
		    String name = registerName.getText();
		    String phoneNumber = registerPhoneNumber.getText();
		    String password = registerPassword.getText();
		    String email = registerEmail.getText();
		    if (name == null || name.trim().isEmpty()) {
		    	registerMessage.setText("Name cannot be empty!");
		    	registerSuccessful.setVisible(false);
		    	registerMessage.setVisible(true);
		    	return;
		    }
		    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
		    	registerMessage.setText("Phone number cannot be empty!");
		    	registerSuccessful.setVisible(false);
		    	registerMessage.setVisible(true);
		    	return;
			}
		    if (email == null || email.trim().isEmpty()) {
		    	registerMessage.setText("Email cannot be empty!");
		    	registerSuccessful.setVisible(false);
		    	registerMessage.setVisible(true);
		    	return;
			}
		    if (password == null || password.trim().isEmpty()) {
		    	registerMessage.setText("Password cannot be empty!");
		    	registerSuccessful.setVisible(false);
		    	registerMessage.setVisible(true);
		    	return;
			} else {
		    	try {
					ClimbSafeFeatureSet3Controller.registerGuide(email, password, name, phoneNumber);
					refreshTable();	//add the guide in the table
					registerName.setText("");
					registerPassword.setText("");
					registerEmail.setText("");
					registerPhoneNumber.setText("");
			    	registerSuccessful.setText("Successful!");
			    	registerMessage.setVisible(false);
			    	registerSuccessful.setVisible(true);
			    } catch (InvalidInputException e) {
			    	registerMessage.setText(e.getMessage());
			    	registerSuccessful.setVisible(false);
			    	registerMessage.setVisible(true);
			    }
		    }
	  }

	  /**
	   * When update button is clicked, it will first check each field to be in a correct form. Then, it will call updateGuide method in the controller
	   * @author rambodazimi
	   * @param event
	   */
	  @FXML
	  public void updateButtonClicked(ActionEvent event) {
		  refreshMessages();
		  String name = updateNewName.getText();
		  String phoneNumber = updateNewContact.getText();
		  String password = updateNewPassword.getText();
		  String email = updateEmail.getText();
		  if (name == null || name.trim().isEmpty()) {
		    	updateMessage.setText("New name cannot be empty!");
		    	updateSuccessful.setVisible(false);
		    	updateMessage.setVisible(true);	
			  	return;
		  }
		  if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
		    	updateMessage.setText("New phone number cannot be empty!");
		    	updateSuccessful.setVisible(false);
		    	updateMessage.setVisible(true);	
			  	return;
		  }
		  if (email == null || email.trim().isEmpty()) {
		    	updateMessage.setText("Email cannot be empty!");
		    	updateSuccessful.setVisible(false);
		    	updateMessage.setVisible(true);	
		    	return;
		  }
		  if (password == null || password.trim().isEmpty()) {
		    	updateMessage.setText("New password cannot be empty!");
		    	updateSuccessful.setVisible(false);
		    	updateMessage.setVisible(true);	
		    	return;
		  } else {
		    	try {
					ClimbSafeFeatureSet3Controller.updateGuide(email, password, name, phoneNumber);
					refreshTable();	//update the guide in the table
					updateNewName.setText("");
					updateNewPassword.setText("");
					updateEmail.setText("");
					updateNewContact.setText("");
			    	updateSuccessful.setText("Successful!");
			    	updateMessage.setVisible(false);
			    	updateSuccessful.setVisible(true);
			    } catch (InvalidInputException e) {
			    	updateMessage.setText(e.getMessage());
			    	updateSuccessful.setVisible(false);
			    	updateMessage.setVisible(true);
			    }
		    }
		  deleteEmail.setText("");
	  }
  
	  /**
	   * When delete button is clicked, it will first check each field to be in a correct form. Then, it will call deleteGuide method in the controller
	   * @author rambodazimi
	   * @param event
	   */
	  @FXML
	  public void deleteButtonClicked(ActionEvent event) {
		  refreshMessages();
		  String email = deleteEmail.getText();
		  if (email == null || email.trim().isEmpty()) {
		    	deleteMessage.setText("Email cannot be empty!");
		    	deleteSuccessful.setVisible(false);
		    	deleteMessage.setVisible(true);
		    	return;
		  } else {
		    	try {
		    		confirmationPopupWindow("Warning" , "Are you sure you want to delete the guide?");
		    		if(confirmation == true) {
			    		ClimbSafeFeatureSet1Controller.deleteGuide(email);
						refreshTable();	//delete the guide from the table
						deleteEmail.setText("");
				    	deleteSuccessful.setText("If the guide exists it's deleted!");
				    	deleteMessage.setVisible(false);
				    	deleteSuccessful.setVisible(true);	
				    }
				} catch (InvalidInputException e) {
			    	deleteMessage.setText(e.getMessage());
			    	deleteSuccessful.setVisible(false);
			    	deleteMessage.setVisible(true);
			    }
		    }
		  updateEmail.setText("");
		  updateNewPassword.setText("");
		  updateNewName.setText("");
		  updateNewContact.setText("");
	  }
	  
	  static int number = 0;
	    @FXML
	    public void showPasswordsChecked(MouseEvent event) {
	    	if(number % 2 == 0) {
		    	if(showPasswords.selectedProperty() != null) {
		    		passwordColumn.setVisible(true);
			    	number++;
		    	}
	    	}else {
	    		passwordColumn.setVisible(false);
	    		number--;
	    	}
	    	
	    }
	    
	    public void refreshMessages() {
		    registerMessage.setText("");
		    updateMessage.setText("");
		    deleteMessage.setText("");
		    registerSuccessful.setText("");
		    updateSuccessful.setText("");
		    deleteSuccessful.setText("");
	    }

	public void refreshTab() {
		  registerMessage.setText("");
		  updateMessage.setText("");
		  deleteMessage.setText("");
		  registerSuccessful.setText("");
		  updateSuccessful.setText("");
		  deleteSuccessful.setText("");
		  registerEmail.setText("");
		  registerPhoneNumber.setText("");
		  registerPassword.setText("");
		  registerName.setText("");
		  updateEmail.setText("");
		  updateNewPassword.setText("");
		  updateNewName.setText("");
		  updateNewContact.setText("");
		  deleteEmail.setText("");
	}	
}
