package ca.mcgill.ecse.climbsafe.javafx.fxml.controllers;

import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.javafx.fxml.ClimbSafeFxmlView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ViewUtils {
	  /** Calls the controller and shows an error, if applicable. */
	  public static boolean callController(Executable executable) {
	    try {
	      executable.execute();
	      ClimbSafeFxmlView.getInstance().refresh();
	      return true;
	    } catch (InvalidInputException e) {
	      showError(e.getMessage());
	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
	    return false;
	  }
	  
	  public static boolean successful(Executable controllerCall) {
		    return callController(controllerCall);
		  }
	  
	  public static void showError(String message) {
		    makePopupWindow("Error", message);
		  }
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
	  @FunctionalInterface
	  	interface Executable {
		  public void execute() throws Throwable;
	  	}
}
