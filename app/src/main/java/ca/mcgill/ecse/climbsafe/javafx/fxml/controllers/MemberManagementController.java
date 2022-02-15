package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet1Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet2Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet4Controller;
import ca.mcgill.ecse.climbsafe.controller.ClimbSafeFeatureSet5Controller;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.controller.TOBundle;
import ca.mcgill.ecse.climbsafe.controller.TOEquipment;
import ca.mcgill.ecse.climbsafe.controller.TOGenericItem;
import ca.mcgill.ecse.climbsafe.controller.TOMember;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

public class MemberManagementController {
  
    
  @FXML
  private Text errorMessage;
  
  @FXML
  private HBox EquipmentPane;//root UI object for adding a new bundle
  @FXML
  private HBox BundlePane;
  
  
  // button
  @FXML
  private Button deleteMemberButton;
  
  @FXML
  private Button updateMemberButton;
  
  @FXML
  private Button newMemberButton;
  
  // promptField
  
  @FXML
  private Label addPromptLabel;

  
  
  
  // textfield
  
  @FXML
  private TextField emailTextField;
  
  @FXML
  private TextField fullNameTextField;
  
  @FXML
  private TextField weeksToClimbTextField;
  
  @FXML
  private PasswordField passwordTextField;
  
  @FXML
  private TextField emergencyContactTextField;
  
  @FXML
  private CheckBox needAGuideCheckBox;
  
  @FXML
  private CheckBox needHotelCheckBox;
  
  
  
  // Bundle table
  
  @FXML
  private TableView<TOBundle> SelectBundlesTable;
  
  //references to columns in the table
  @FXML
  private TableColumn<TOBundle, String> BundleNameColumn;
  @FXML
  private TableColumn<TOBundle, String> BundleDiscountColumn;
  @FXML
  private TableColumn<TOBundle, String> BundlePriceColumn;  
  
  @FXML
  private TableView<TOGenericItem> SelectedItemsTable;
  
  @FXML
  private TableColumn<TOGenericItem, String> SelectedItemsName;
  @FXML
  private TableColumn<TOGenericItem, Integer> SelectedItemsQty;
  
  // Equipment table
  
  @FXML
  private TableView<TOEquipment> SelectEquipmentsTable;
  
 

  @FXML
  private TableColumn<TOEquipment, String> EquipmentNameColumn;
  @FXML
  private TableColumn<TOEquipment, Integer> EquipmentWeightColumn;
  @FXML
  private TableColumn<TOEquipment, Integer> EquipmentPriceColumn;

  
  @FXML
  private ComboBox<String> listOfMembersComboBox;
  
  private TOMember TOMember;
  
  /**
   * List the member in the combo box
   * @author yujieqin
   * @param event
   */
  
  @FXML
  public void onListOfMembersComboBox(Event event) {
	  needAGuideCheckBox.setDisable(false);
		needHotelCheckBox.setDisable(false);
    for (TOMember aTOMember : ClimbSafeFeatureSet2Controller.getMembers()) {
        if (aTOMember.getEmail().equals(listOfMembersComboBox.getValue())) {
            this.TOMember = aTOMember;
            break;
        }
    }
    
    //Added by Abdul
    if (listOfMembersComboBox.getValue() != null) {
    	String email = listOfMembersComboBox.getValue();
    	TOMember member = null;
    	for (TOMember m :ClimbSafeFeatureSet2Controller.getMembers())
    		if (m.getEmail().equals(email)) {
    			member = m;
    			break;
    		}
    	emailTextField.setText(email);
    	fullNameTextField.setText(member.getName());
    	weeksToClimbTextField.setText(member.getWeeks()+"");
    	emergencyContactTextField.setText(member.getEmergancyContact());
    	needAGuideCheckBox.setSelected(member.isNeedGuide());
    	needHotelCheckBox.setSelected(member.getNeedHotel());
    	passwordTextField.setText(member.getPassword());
    	
    	//added by fred
    	SelectedItemsTable.getItems().clear();
    	SelectedItemsTable.getItems().addAll(member.getItems());
    	if (AssignmentController.hasAssignment(email)) {
    		needAGuideCheckBox.setDisable(true);
    		needHotelCheckBox.setDisable(true);
    	}
    }
   
}
  
  
  
  /**
   * Initialize the page
   * @author yujieqin
   * 
   */
  
  // initializaiton 
  @FXML
  public void initialize(){
    
	  needAGuideCheckBox.setDisable(false);
		needHotelCheckBox.setDisable(false);
    ObservableList<String> membersInList = FXCollections.observableArrayList();
    for (TOMember aTOMember : ClimbSafeFeatureSet2Controller.getMembers()) {
      membersInList.add(aTOMember.getEmail());
    }
    
    this.listOfMembersComboBox.getItems().addAll(membersInList);
    if (ClimbSafeFeatureSet2Controller.getMembers().size() < 1)
		 this.listOfMembersComboBox.setValue("No members");
		 else this.listOfMembersComboBox.setValue("Pick member");
	 
    
    
    addPromptLabel.setVisible(false);
    
    // for bundle table
    
    BundleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    BundleDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
    BundlePriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPricePerWeek"));
    
    SelectBundlesTable.getItems().addAll(ClimbSafeFeatureSet5Controller.getEquipmentBundles());
    
    
    
    // for equipment table
    
    EquipmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    EquipmentWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
    EquipmentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerWeek"));
    
    SelectEquipmentsTable.getItems().addAll(ClimbSafeFeatureSet4Controller.getEquipments());
    
    
    // for selected items
    SelectedItemsName.setCellValueFactory(new PropertyValueFactory<>("name"));
    
    SelectedItemsQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    SelectedItemsQty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));//make column editable with text field
    SelectedItemsQty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TOGenericItem, Integer>>() {
		@Override
		public void handle(CellEditEvent<TOGenericItem, Integer> event) {
			TOGenericItem item = event.getRowValue();// get row object
			item.setQuantity(event.getNewValue());// save new name to model
		}
	});
    	SelectedItemsTable.setPlaceholder(new Label("Oops.. You did not select items :("));
  }
  
  /**
   * the equipment will be added once clicked
   * @author yujieqin
   * @param event
   */
  @FXML
  public void onAddEquipmentItem(ActionEvent event) {
	  var selected = SelectEquipmentsTable.getSelectionModel().getSelectedItem();
	  if (selected!=null) {
		  var items =  SelectedItemsTable.getItems();
		  for (var item :items ) {
			  if(item.getName().equals(selected.getName())) {
				  var quantity = item.getQuantity();
				  if (quantity == Integer.MAX_VALUE)
					  return;
				  items.remove(item);
				  SelectedItemsTable.getItems().add(new TOGenericItem(quantity +1, selected.getName()));
				 return;
			  } 	 
		  }
		  SelectedItemsTable.getItems().add(new TOGenericItem(1, selected.getName()));
	  }

  }
  
  /**
   * the bundle will be added once clicked
   * @author yujieqin
   * @param event
   */
  @FXML
  public void onAddBundleItem(ActionEvent event) {
	  var selected = SelectBundlesTable.getSelectionModel().getSelectedItem();
	  if (selected!=null) {
		  var items =  SelectedItemsTable.getItems();
		  for (var item :items ) {
			  if(item.getName().equals(selected.getName())) {
				  var quantity = item.getQuantity();
				  if (quantity == Integer.MAX_VALUE)
					  return;
				  items.remove(item);
				  SelectedItemsTable.getItems().add(new TOGenericItem(quantity +1, selected.getName()));
				 return;
			  } 	 
		  }
		  SelectedItemsTable.getItems().add(new TOGenericItem(1,selected.getName()));

	  }
  }

  
  PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));  
  
  // event listener on button [Button].onAction
  /**
   * new member created once clicked
   * @author yujieqin
   * @param event
   */
  
  @FXML
  public void onNewMember(ActionEvent event)
  {	  
    String email = emailTextField.getText();
    String fullName = fullNameTextField.getText();
    String weeksToClimbString = weeksToClimbTextField.getText();
    String password = passwordTextField.getText();
    String emergencyContact = emergencyContactTextField.getText();
    boolean needAGuide = needAGuideCheckBox.isSelected();
    boolean needHotel = needHotelCheckBox.isSelected();
    
    int weeksToClimb=0;
	try {
		weeksToClimb = Integer.parseInt(weeksToClimbString);
	} catch (NumberFormatException e1) {
	
	      addPromptLabel.setText("Invalid number of weeks, need to be a number.");
	      addPromptLabel.setStyle("-fx-text-fill: red;");
	      addPromptLabel.setMinHeight(70);
	      addPromptLabel.setVisible(true);
	      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
	      visiblePause.play();
		return;
	}
    
    List<String> itemNames = new ArrayList<String>();
    List<Integer> itemQty = new ArrayList<Integer>();
    for (var i: SelectedItemsTable.getItems()) {
    	itemNames.add(i.getName());
    	itemQty.add(i.getQuantity());
    }
    
    
    
    try 
    {
      ClimbSafeFeatureSet2Controller.registerMember(email, password, fullName, emergencyContact, weeksToClimb, needAGuide, needHotel, itemNames, itemQty);  
     
      emailTextField.setText("");
      fullNameTextField.setText("");
      weeksToClimbTextField.setText("");
      passwordTextField.setText("");
      emergencyContactTextField.setText("");
      needAGuideCheckBox.setSelected(false);
      needHotelCheckBox.setSelected(false);
      SelectedItemsTable.getItems().clear();
      
                 
      listOfMembersComboBox.getSelectionModel().clearSelection();
      this.listOfMembersComboBox.getItems().addAll(email);
      
      addPromptLabel.setText("added!");
      addPromptLabel.setStyle("-fx-text-fill: green;");
      addPromptLabel.setVisible(true);
      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
      visiblePause.play();
     
      ClimbSafeFxmlView.getInstance().refresh();
      
    } catch (InvalidInputException e) {
      addPromptLabel.setText(e.getMessage());
      addPromptLabel.setStyle("-fx-text-fill: red;");
      addPromptLabel.setMinHeight(70);
      addPromptLabel.setVisible(true);
      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
      visiblePause.play();
    }
  }
  
  /**
   * member is updated once clicked
   * @author yujieqin
   * @param event
   */
  @FXML
  public void onUpdateMember(ActionEvent event)
  {
    String email = emailTextField.getText();
    String fullName = fullNameTextField.getText();
    String weeksToClimbString = weeksToClimbTextField.getText();
    String password = passwordTextField.getText();
    String emergencyContact = emergencyContactTextField.getText();
    boolean needAGuide = needAGuideCheckBox.isSelected();
    boolean needHotel = needHotelCheckBox.isSelected();
    
    int weeksToClimb=0;
	try {
		weeksToClimb = Integer.parseInt(weeksToClimbString);
	} catch (NumberFormatException e1) {
	  
      addPromptLabel.setText("error");
      addPromptLabel.setStyle("-fx-text-fill: red;");
      addPromptLabel.setMinHeight(70);
      addPromptLabel.setVisible(true);
      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
      visiblePause.play();	
		return;
	}
    
    List<String> itemNames = new ArrayList<String>();
    List<Integer> itemQty = new ArrayList<Integer>();
    for (var i: SelectedItemsTable.getItems()) {
    	itemNames.add(i.getName());
    	itemQty.add(i.getQuantity());
    }
          
    
    try 
    {
      ClimbSafeFeatureSet2Controller.updateMember(email, password, fullName, emergencyContact, weeksToClimb, needAGuide, needHotel, itemNames, itemQty);
      
      emailTextField.setText("");
      fullNameTextField.setText("");
      weeksToClimbTextField.setText("");
      passwordTextField.setText("");
      emergencyContactTextField.setText("");
      needAGuideCheckBox.setSelected(false);
      needHotelCheckBox.setSelected(false);
      SelectedItemsTable.getItems().clear();
      listOfMembersComboBox.getSelectionModel().clearSelection();
      
      addPromptLabel.setText("updated!");
      addPromptLabel.setStyle("-fx-text-fill: green;");
      addPromptLabel.setVisible(true);
      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
      visiblePause.play();
      
      ClimbSafeFxmlView.getInstance().refresh();
      
    } catch (InvalidInputException e) {
      addPromptLabel.setText(e.getMessage());
      addPromptLabel.setStyle("-fx-text-fill: red;");
      addPromptLabel.setMinHeight(70);
      addPromptLabel.setVisible(true);
      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
      visiblePause.play();
    }
    
  }
  
  
  /**
   * member is deleted once clicked
   * @author yujieqin
   * @param event
   */
  @FXML
  public void onDeleteMember(ActionEvent event)
  {
   try {
      ClimbSafeFeatureSet1Controller.deleteMember(listOfMembersComboBox.getValue());
      emailTextField.setText("");
      fullNameTextField.setText("");
      weeksToClimbTextField.setText("");
      passwordTextField.setText("");
      emergencyContactTextField.setText("");
      
      needAGuideCheckBox.setDisable(false);
      needHotelCheckBox.setDisable(false);
      needAGuideCheckBox.setSelected(false);
      needHotelCheckBox.setSelected(false);
      
      SelectedItemsTable.getItems().clear();
      
      addPromptLabel.setText("deleted!");
      addPromptLabel.setStyle("-fx-text-fill: green;");
      addPromptLabel.setVisible(true);
      visiblePause.setOnFinished(EventHandler -> addPromptLabel.setVisible(false));
      visiblePause.play();
      
      
      
    } catch (InvalidInputException e) {
      errorMessage.setText(e.getMessage());
    }
    
    ObservableList<String> membersInList = FXCollections.observableArrayList();
    
    for (TOMember aTOMember : ClimbSafeFeatureSet2Controller.getMembers()) {
      membersInList.add(aTOMember.getEmail());
    }
    listOfMembersComboBox.getItems().clear();
    this.listOfMembersComboBox.getItems().addAll(membersInList);
    
    
    

    
  }
  

  public void refreshTab()
  {
    emailTextField.setText("");
    fullNameTextField.setText("");
    weeksToClimbTextField.setText("");
    passwordTextField.setText("");
    emergencyContactTextField.setText("");
    
    needAGuideCheckBox.setDisable(false);
	needHotelCheckBox.setDisable(false);
    needAGuideCheckBox.setSelected(false);
    needHotelCheckBox.setSelected(false);
    
    addPromptLabel.setVisible(false);
    
    SelectedItemsTable.getItems().clear();
    SelectBundlesTable.getItems().clear();
    SelectEquipmentsTable.getItems().clear();
    
    SelectBundlesTable.getItems().addAll(ClimbSafeFeatureSet5Controller.getEquipmentBundles());
    SelectEquipmentsTable.getItems().addAll(ClimbSafeFeatureSet4Controller.getEquipments());
    
    ObservableList<String> membersInList = FXCollections.observableArrayList();
    
    for (TOMember aTOMember : ClimbSafeFeatureSet2Controller.getMembers()) {
      membersInList.add(aTOMember.getEmail());
    }
    listOfMembersComboBox.getItems().clear();
    this.listOfMembersComboBox.getItems().addAll(membersInList);
    
    ClimbSafeFxmlView.getInstance().refresh();
  }
}
