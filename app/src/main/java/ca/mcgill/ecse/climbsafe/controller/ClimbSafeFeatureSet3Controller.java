package ca.mcgill.ecse.climbsafe.controller;

import java.util.ArrayList;
import java.util.List;

//We only import those classes from the model that we need here
import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;


public class ClimbSafeFeatureSet3Controller {
	
	private static ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();	
	
	
	/**
	 * This method will register a person as a guide in the system with the following parameters
	 *
	 * @author Rambod Azimi
	 * @param email
	 * @param password
	 * @param name
	 * @param emergencyContact
	 * @throws InvalidInputException
	 */
	
  public static void registerGuide(String email, String password, String name, String emergencyContact) throws InvalidInputException {
	  	  
	  //save all guides that are in the system in a list called guideList
	  List<Guide> guideList = climbSafe.getGuides();
	  
	  //save all members that are in the system in a list called memberList
	  List<Member> memberList = climbSafe.getMembers();

	  //Email address cannot be empty
	  if(email.isEmpty()) {
		  throw new InvalidInputException("Email cannot be empty");
	  }
	  
	  //Email address cannot have any spaces
	  if(email.contains(" ")) {
		  throw new InvalidInputException("Email must not contain any spaces");
	  }
	  
	  //Email address cannot be similar to admin's email address
	  if(email.equals("admin@nmc.nt")) {
		  throw new InvalidInputException("Email cannot be admin@nmc.nt");
	  }
	  
	  //email address is invalid when it does not have @ or dot, or it has @.
	  if(!email.contains("@") || !email.contains(".") || email.contains("@.")) {
		  throw new InvalidInputException("Invalid email");
	  }
	  
	  //email address cannot end with dot or start with @
	  if(email.endsWith(".") || email.startsWith("@")) {
		  throw new InvalidInputException("Invalid email");
	  }
	  
	  //email address cannot have 2 @ symbols
	  if(email.indexOf("@") != email.lastIndexOf("@")) {
		  throw new InvalidInputException("Invalid email");
	  }
	  
	  //email address like greg.email@com is invalid
	  if(email.indexOf("@") > email.lastIndexOf(".")) {
		  throw new InvalidInputException("Invalid email");
	  }
	  
	  //true if found any same email address in the system
	  boolean isFound = false;
	  
	  //iterate through each GUIDE in the system to check their email addresses
	  for(Guide guide : guideList) {
		  if(guide.getEmail().equals(email)) {
			  isFound = true;
			  break;	//we found similar email!
		  }
	  }
	  //Email already exists in the system
	  if(isFound) {
		  throw new InvalidInputException("Email already linked to a guide account");
	  }else {	//check similarity in member's email addresses
		  
		  //iterate through each MEMBER in the system to check their email addresses
		  for(Member member : memberList) {
			  if(member.getEmail().equals(email)) {
				  isFound = true;
				  break;	//we found similar email!
			  }
		  }
		  //Email already exists in the system
		  if(isFound) {
			  throw new InvalidInputException("Email already linked to a member account");
		  }
	  }
	  
	  //if no password is typed
	  if(password.isEmpty()) {
		  throw new InvalidInputException("Password cannot be empty");
	  }
	  
	  //if no emergency contact is typed
	  if(emergencyContact.isEmpty()) {
		  throw new InvalidInputException("Emergency contact cannot be empty");
	  }
	  
	  //if no name is typed
	  if(name.isEmpty()) {
		  throw new InvalidInputException("Name cannot be empty");
	  }
	  
	  
	  //Calling Guide's constructor to constructor a new instance from Guide class
	  Guide aGuide = new Guide(email, password, name, emergencyContact, climbSafe);
	  
	  //Adding the guide to the system using addGuide method
	  climbSafe.addGuide(aGuide); 
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        }
	  
  }
  
  /**
   * This method will update the information of a guide in the system with the following parameters
   *
   * @author Rambod Azimi
   * @param email
   * @param newPassword
   * @param newName
   * @param newEmergencyContact
   * @throws InvalidInputException
   */

  public static void updateGuide(String email, String newPassword, String newName, String newEmergencyContact) throws InvalidInputException {
	  
	  //if no password is entered as a new password
	  if(newPassword.isEmpty()) {
		  throw new InvalidInputException("Password cannot be empty");
	  }
	  
	  //if no name is entered as a new name
	  if(newName.isEmpty()) {
		  throw new InvalidInputException("Name cannot be empty");
	  }
	  
	  //if no emergency contact is entered as a new emergency contact
	  if(newEmergencyContact.isEmpty()) {
		  throw new InvalidInputException("Emergency contact cannot be empty");
	  }
	  
	  //save all guides that are in the system in a list called guideList
	  List<Guide> guideList = climbSafe.getGuides();
	  
	  //A boolean variable to check whether the guide is found or not
	  boolean isFound = false;
	  
	  //iterate through each guide in the system to find the right guide (if it exists!)
	  for(Guide guide : guideList) {
		  if(guide.getEmail().equals(email)) {
			  isFound = true;
			  guide.setName(newName);
			  guide.setPassword(newPassword);
			  guide.setEmergencyContact(newEmergencyContact);			 
			  break;	//we are done!
		  }
	  }
	  
	  //if no guide is found in the system
	  if(!isFound) {
		  throw new InvalidInputException("Guide not found");
	  }
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        }
  }
  
  /**
   * This method will return a list of all guides in the system as transfer objects
   * @author rambodazimi
   * @return a list
   */
  public static ArrayList<TOGuide> getGuides(){
	  
	  ArrayList<TOGuide> guideList = new ArrayList<TOGuide>();
	  	  
	  for(Guide g : climbSafe.getGuides()) {
		  var toGuide = new TOGuide(g.getName(), g.getEmergencyContact(), g.getEmail(), g.getPassword());
		  guideList.add(toGuide);
	  }
	  return guideList;
  }
	
}
