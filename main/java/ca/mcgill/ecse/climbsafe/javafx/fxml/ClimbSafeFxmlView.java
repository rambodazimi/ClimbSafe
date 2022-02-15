package ca.mcgill.ecse.climbsafe.javafx.fxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClimbSafeFxmlView extends Application {

	public static final EventType<Event> REFRESH_EVENT = new EventType<Event>("REFRESH");
	  private static ClimbSafeFxmlView instance;
	  private List<Node> refreshableNodes = new ArrayList<Node>();

	  /**
	   * init ui elements and set main page
	   * 
	   *@author AbdelrahmanAli
	   */
	@Override
	  public void start(Stage primaryStage) {
	    instance = this;
	    try {
	      Pane root = (Pane) FXMLLoader.load(getClass().getResource("MainPage.fxml"));
	      Scene scene = new Scene(root);
	      scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Fuzzy+Bubbles");
	      scene.getStylesheets().add("/stylesheet.css");
	      primaryStage.setScene(scene);
	      primaryStage.setMinWidth(1200);
	      primaryStage.setMinHeight(670);
	      primaryStage.setTitle("Best Climb Safe");
	      primaryStage.setMaximized(true);
	      primaryStage.show();	      
	      refresh();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	 	/**
	 	 *  Register the node for receiving refresh events
	 	 *  
		 *@author AbdelrahmanAli
		 */
	 
	  public void registerRefreshEvent(Node node) {
	    refreshableNodes.add(node);
	  }

	  	/**
	  	 * Register multiple nodes for receiving refresh events
	  	 * 
		 *@author AbdelrahmanAli
		 */
	  public void registerRefreshEvent(Node... nodes) {
	    for (var node: nodes) {
	      refreshableNodes.add(node);
	    }
	  }

	  /**
	   * remove the node from receiving refresh events
	   * 
	   *@author AbdelrahmanAli
	   */
	  public void removeRefreshableNode(Node node) {
	    refreshableNodes.remove(node);
	  }

	  /**
	   * refresh all events through registered nodes
	   * 
	   *@author AbdelrahmanAli
	   */
	  public void refresh() {
	    for (Node node : refreshableNodes) {
	      node.fireEvent(new Event(REFRESH_EVENT));
	    }
	  }

	  /**
	   *@author AbdelrahmanAli
	   */
	  public static ClimbSafeFxmlView getInstance() {
	    return instance;
	  }

}
