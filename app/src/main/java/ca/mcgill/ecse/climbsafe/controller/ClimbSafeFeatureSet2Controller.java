package ca.mcgill.ecse.climbsafe.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.BookableItem;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet2Controller {

	private static ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();

	/**
	 * This method will register a new member with the passed parameters
	 * 
	 * @author yujieqin
	 * @param email
	 * @param password
	 * @param name
	 * @param emergencyContact
	 * @param nrWeeks
	 * @param guideRequired
	 * @param hotelRequired
	 * @param itemNames
	 * @param itemQuantities
	 * @throws InvalidInputException
	 */

	public static void registerMember(String email, String password, String name, String emergencyContact, int nrWeeks,
			boolean guideRequired, boolean hotelRequired, List<String> itemNames, List<Integer> itemQuantities)
			throws InvalidInputException {

		// check if the email contain any spaces

		if (email.contains(" ")) {
			throw new InvalidInputException("The email must not contain any spaces");
		}

		// check if the email is valid (multiple cases), try to cover all cases

		if (!email.contains(".") || !email.contains("@") || email.contains("@.")) {
			throw new InvalidInputException("Invalid email");
		}

		// email address cannot starts with @ and end with .
		if (email.startsWith("@") || email.endsWith(".")) {
			throw new InvalidInputException("Invalid email");
		}

		// email address like user.mail@ca is invalid
		if (email.indexOf("@") > email.lastIndexOf(".")) {
			throw new InvalidInputException("Invalid email");
		}

		// email address cannot have 2 @ symbols
		if (email.indexOf("@") != email.lastIndexOf("@")) {
			throw new InvalidInputException("Invalid email");
		}

		// check if the email entered is allowed

		if (email.equals("admin@nmc.nt")) {
			throw new InvalidInputException("The email entered is not allowed for members");
		}

		// email address cannot be empty
		if (email.isEmpty()) {
			throw new InvalidInputException("The email cannot be empty");
		}

		// check if the password is empty

		if (password.isEmpty()) {
			throw new InvalidInputException("The password cannot be empty");
		}

		// check if the name is empty

		if (name.isEmpty()) {
			throw new InvalidInputException("The name cannot be empty");
		}

		// check if the emergency contact is empty

		if (emergencyContact.isEmpty()) {
			throw new InvalidInputException("The emergency contact cannot be empty");
		}

		// check if the number of weeks are greater than zero and less than or equal to
		// the number of climbing weeks in the climbing season

		if (nrWeeks < 1 || nrWeeks > climbSafe.getNrWeeks()) {
			throw new InvalidInputException(
					"The number of weeks must be greater than zero and less than or equal to the number of climbing weeks in the climbing season");
		}

		// check if the email already linked with another member
		List<Member> members = climbSafe.getMembers();

		for (Member mem : members) {
			if (mem.getEmail().equals(email)) {

				throw new InvalidInputException("A member with this email already exists");

			}
		}

		// check if the email is linked with a guide already

		List<Guide> guides = climbSafe.getGuides();

		for (Guide gui : guides) {
			if (gui.getEmail().equals(email)) {
				throw new InvalidInputException("A guide with this email already exists");
			}
		}

		// check if the requested item is available
		for (String itemWant : itemNames) {
			if (!BookableItem.hasWithName(itemWant)) {
				throw new InvalidInputException("Requested item not found");
			}
		}

		// register the new member if no errors

		Member newMember = new Member(email, password, name, emergencyContact, nrWeeks, guideRequired, hotelRequired,
				climbSafe);

		for (int j = 0; j < itemNames.size(); j++) {
		/*	if (itemQuantities.get(j) < 1)
				throw new InvalidInputException("Item quantity should be bigger than 0"); */
			BookableItem.getWithName(itemNames.get(j)).addBookedItem(itemQuantities.get(j), climbSafe, newMember);

		}

		// this line should be deleted!!!
		climbSafe.addMember(newMember);
		try {
			ClimbSafePersistence.save(); // Update the persistence layer
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	/**
	 * This method is to update existing member information with the passed
	 * parameters
	 * 
	 * @author yujieqin
	 * @param email
	 * @param newPassword
	 * @param newName
	 * @param newEmergencyContact
	 * @param newNrWeeks
	 * @param newGuideRequired
	 * @param newHotelRequired
	 * @param newItemNames
	 * @param newItemQuantities
	 * @throws InvalidInputException
	 */
	public static void updateMember(String email, String newPassword, String newName, String newEmergencyContact,
			int newNrWeeks, boolean newGuideRequired, boolean newHotelRequired, List<String> newItemNames,
			List<Integer> newItemQuantities) throws InvalidInputException {

		// check if the password is empty

		if (newPassword.isEmpty()) {
			throw new InvalidInputException("The password cannot be empty");
		}

		// check if the name if empty

		if (newName.isEmpty()) {
			throw new InvalidInputException("The name cannot be empty");
		}

		// check if the emergency contact is empty

		if (newEmergencyContact.isEmpty()) {

			throw new InvalidInputException("The emergency contact cannot be empty");
		}

		// check if the number of weeks are greater than zero and less than or equal to
		// the number of climbing weeks in the climbing season

		if ((newNrWeeks < 1)) {
			throw new InvalidInputException(
					"The number of weeks must be greater than zero and less than or equal to the number of climbing weeks in the climbing season");
		}

		if (newNrWeeks > climbSafe.getNrWeeks()) {
			throw new InvalidInputException(
					"The number of weeks must be greater than zero and less than or equal to the number of climbing weeks in the climbing season");
		}

		// check if the request item is found

		for (String itemWant : newItemNames) {
			if (!BookableItem.hasWithName(itemWant)) {
				throw new InvalidInputException("Requested item not found");
			}
		}

		// check if the member is existing

		List<Member> members = climbSafe.getMembers();

		boolean isFound = false;

		for (Member member : members) {

			// update the information if it's exist
			if (member.getEmail().equals(email)) {
				isFound = true;

				member.setPassword(newPassword);
				member.setName(newName);
				member.setEmergencyContact(newEmergencyContact);
				member.setNrWeeks(newNrWeeks);
				member.setGuideRequired(newGuideRequired);
				member.setHotelRequired(newHotelRequired);

				// clean the old bookedItem
			
				while (member.numberOfBookedItems() > 0) {
					member.getBookedItem(0).delete();
				}
				// add the new items and quantities
				for (int j = 0; j < newItemNames.size(); j++) {
				/*	if (newItemQuantities.get(j) < 1)
						throw new InvalidInputException("Item quantity should be bigger than 0"); */
					BookableItem.getWithName(newItemNames.get(j)).addBookedItem(newItemQuantities.get(j), climbSafe,
							member);
				}

				break;
			}

		}

		if (!isFound) {
			throw new InvalidInputException("Member not found");
		}
		try {
			ClimbSafePersistence.save(); // Update the persistence layer
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
   * @author AbdelrahmanAli
   * @return email of all members
   */
public static List<TOMember> getMembers() {
	  List<TOMember> toMembers = new ArrayList<>();
	  List<Member> members = climbSafe.getMembers();
	  for (Member member : members) {
		  var newMember = new TOMember(member.getEmail(),member.getName(), 
				  member.getEmergencyContact(), member.getPassword(),member.getNrWeeks(), member.getGuideRequired(), member.getHotelRequired());
		  		  
		 for(var item:member.getBookedItems()) {
			 newMember.addItem(new TOGenericItem(item.getQuantity(), item.getItem().getName()));
		 }
		 
		 toMembers.add(newMember);
	  }
	  return toMembers;
  }


}