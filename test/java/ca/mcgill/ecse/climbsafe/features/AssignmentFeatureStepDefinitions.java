package ca.mcgill.ecse.climbsafe.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.controller.AssignmentController;
import ca.mcgill.ecse.climbsafe.controller.InvalidInputException;
import ca.mcgill.ecse.climbsafe.model.Assignment;
import ca.mcgill.ecse.climbsafe.model.BookableItem;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Equipment;
import ca.mcgill.ecse.climbsafe.model.EquipmentBundle;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AssignmentFeatureStepDefinitions {

	private ClimbSafe climbSafe;
	private String error;

	/*
	 * this key variables is used to fetch the data from the map written by
	 * AbdelrahmanAli
	 */
	private static final String hotelRequired = "hotelRequired";
	private static final String guideRequired = "guideRequired";
	private static final String emergencyContact = "emergencyContact";
	private static final String password = "password";
	private static final String email = "email";
	private static final String name = "name";
	private static final String startDate = "startDate";
	private static final String nrWeeks = "nrWeeks";
	private static final String weight = "weight";
	private static final String discount = "discount";
	private static final String items = "items";
	private static final String quantity = "quantity";
	private static final String priceOfGuidePerWeek = "priceOfGuidePerWeek";
	private static final String pricePerWeek = "pricePerWeek";
	private static final String startWeek = "startWeek";
	private static final String endWeek = "endWeek";
	private static final String guideEmail = "guideEmail";
	private static final String memberEmail = "memberEmail";
	
	/**
	 * @author KarimAtoui
	 * @param dataTable
	 * This method sets the climbsafe system
	 */
	@Given("the following ClimbSafe system exists:")
	public void the_following_climb_safe_system_exists(io.cucumber.datatable.DataTable dataTable) {
		climbSafe = ClimbSafeApplication.getClimbSafe();
		error = "";

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		climbSafe.setStartDate(Date.valueOf(rows.get(0).get(startDate))); // Fetch and set startDate
		climbSafe.setNrWeeks(Integer.valueOf(rows.get(0).get(nrWeeks))); // Fetch and set number of
																			// weeks
		climbSafe.setPriceOfGuidePerWeek(Integer.valueOf(rows.get(0).get(priceOfGuidePerWeek))); // Fetch
																									// and
																									// parse
																									// price
																									// of
																									// guide
																									// per
																									// week
	}

	/**
	 * @author Abdul
	 * @param dataTable
	 * This method adds equipment to the climbsafe system
	 */
	@Given("the following pieces of equipment exist in the system:")
	public void the_following_pieces_of_equipment_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < rows.size(); i++) {
			Map<String, String> columns = rows.get(i); // working on a single row a time
			climbSafe.addEquipment(columns.get(name) // add new equipment
					, Integer.parseInt(columns.get(weight)), Integer.parseInt(columns.get(pricePerWeek)));
		}
	}

	/**
	 * @author Abdul
	 * @param dataTable
	 * This method adds equipment bundles to the climbsafe system
	 */
	@Given("the following equipment bundles exist in the system:")
	public void the_following_equipment_bundles_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < rows.size(); i++) {

			Map<String, String> columns = rows.get(i); // working on a single row a time

			// initialize the new EquipmentBundle
			EquipmentBundle newEquipmentBundle = new EquipmentBundle(columns.get(name),
					Integer.parseInt(columns.get(discount)), climbSafe);

			// Fetch the names of the equipments to be added to the bundle
			List<String> equipmentNamesInNewBundle = Arrays.asList(columns.get(items).split(","));
			// Fetch the quantity of each equipment
			List<String> equipmentsQuantity = Arrays.asList(columns.get(quantity).split(","));
			// get all equipments in the system
			List<Equipment> existingEquipments = climbSafe.getEquipment();

			for (int j = 0; j < equipmentNamesInNewBundle.size(); j++) {
				String equipmentName = equipmentNamesInNewBundle.get(j); // Fetch the equipment's name to be
																			// added to the bundle

				// start looking for the equipment by its name
				Equipment equipment = null;
				for (Equipment temp : existingEquipments) {
					if (temp.getName().equals(equipmentName)) {
						equipment = temp;
						break; // break once found
					}
				}

				// add the equipment to the bundle
				newEquipmentBundle.addBundleItem(Integer.parseInt(equipmentsQuantity.get(j)), climbSafe, equipment);
			}
		}
	}

	/**
	 * @author KarimAtoui
	 * @param dataTable
	 * This method adds guides to the climbsafe system
	 */
	@Given("the following guides exist in the system:")
	public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

		for (int i = 0; i < rows.size(); i++) {

			Map<String, String> columns = rows.get(i);

			new Guide(columns.get(email), columns.get(password), columns.get(name), columns.get(emergencyContact),
					climbSafe);
		}

	}

	/**
	 * @author Abdul
	 * @param dataTable
	 * This method adds members to the climbsafe system
	 */
	@Given("the following members exist in the system:")
	public void the_following_members_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

		for (int i = 0; i < rows.size(); i++) {

			Map<String, String> columns = rows.get(i);

			Member newMember = new Member(columns.get(email), columns.get(password), columns.get(name),
					columns.get(emergencyContact), Integer.parseInt(columns.get(nrWeeks)),
					Boolean.parseBoolean(columns.get(guideRequired)), Boolean.parseBoolean(columns.get(hotelRequired)),
					climbSafe);

			List<String> bookedItem = Arrays.asList(columns.get("bookedItems").split(","));
			List<String> itemQuality = Arrays.asList(columns.get("bookedItemQuantities").split(","));

			for (int j = 0; j < bookedItem.size(); j++) {
				BookableItem.getWithName(bookedItem.get(j)).addBookedItem(Integer.parseInt(itemQuality.get(j)),
						climbSafe, newMember);

			}

		}
	}

	/**
	 * @author KarimAtoui
	 * This method does the controller call for the assignment process
	 */
	@When("the administrator attempts to initiate the assignment process")
	public void the_administrator_attempts_to_initiate_the_assignment_process() {

		try {
			AssignmentController.assign();
		} catch (Exception e) {
			error += e.getMessage();
		}
	}
	
	/**
	 * @author KarimAtoui
	 * @param dataTable
	 * This method verifies that assignments are correctly created
	 */
	@Then("the following assignments shall exist in the system:")
	public void the_following_assignments_shall_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

		for (int i = 0; i < rows.size(); i++) {
			Map<String, String> columns = rows.get(i);

			Assignment assignment = null;
			for (Assignment aAssignment : climbSafe.getAssignments()) {
				if (aAssignment.getMember().getEmail().equals(columns.get(memberEmail))) {
					assignment = aAssignment;
					break;
				}
			}

			// check if assignment exists
			assertNotNull(assignment);

			// check assignment startweek
			assertEquals(assignment.getStartWeek(), Integer.parseInt(columns.get(startWeek)));

			// check assignment endweek
			assertEquals(assignment.getEndWeek(), Integer.parseInt(columns.get(endWeek)));

			if (columns.get(guideEmail) == null) {
				assertNull(assignment.getGuide());
			} else {
				assertEquals(assignment.getGuide().getEmail(), columns.get(guideEmail));
			}
		}
	}

	/**
	 * @author KarimAtoui
	 * @param email
	 * @param state
	 * This method verifies if a member is in the correct state
	 */
	@Then("the assignment for {string} shall be marked as {string}")
	public void the_assignment_for_shall_be_marked_as(String email, String state) {

		Member member = null;
		for (Member aMember : climbSafe.getMembers()) {
			if (aMember.getEmail().equals(email)) {
				member = aMember;
				break;
			}
		}

		assertNotNull(member);

		assertEquals(member.getAssignment().getStatusFullName(), state);
	}

	/**
	 * @author KarimAtoui
	 * @param assignmentNumber
	 * This method verifies if the number of assignment in the climbsafe system is the correct one
	 */
	@Then("the number of assignments in the system shall be {string}")
	public void the_number_of_assignments_in_the_system_shall_be(String assignmentNumber) {

		assertEquals(climbSafe.getAssignments().size(), Integer.parseInt(assignmentNumber));
	}
	
	/**
	 * @author KarimAtoui
	 * @param errorMessage
	 * This method verifies if the error thrown by the controller is the correct one
	 */
	@Then("the system shall raise the error {string}")
	public void the_system_shall_raise_the_error(String errorMessage) {
		assertTrue(error.contains(errorMessage));
	}

	/**
	 * @author yujieqin
	 * @param dataTable
	 */
	@Given("the following assignments exist in the system:")
	public void the_following_assignments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> cucumberData = dataTable.asMaps();

		for (Map<String, String> assignmentData : cucumberData) {
			var assignmentMember = (Member) User.getWithEmail(assignmentData.get("memberEmail"));
			var assignmentGuide = (Guide) User.getWithEmail(assignmentData.get("guideEmail"));
			int startWeek = Integer.valueOf(assignmentData.get("startWeek"));
			int endWeek = Integer.valueOf(assignmentData.get("endWeek"));

			Assignment newAssignment = climbSafe.addAssignment(startWeek, endWeek, assignmentMember);
			newAssignment.setGuide(assignmentGuide);
		}

	}

	/**
	 * @author yujieqin
	 * @param string
	 * @param string2
	 */
	@When("the administrator attempts to confirm payment for {string} using authorization code {string}")
	public void the_administrator_attempts_to_confirm_payment_for_using_authorization_code(String string,
			String string2) {
		// Write code here that turns the phrase above into concrete actions

		try {
			AssignmentController.payTrip(string, string2);
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}

	}

	/**
	 * @author yujieqin
	 * @param string
	 * @param string2
	 */
	@Then("the assignment for {string} shall record the authorization code {string}")
	public void the_assignment_for_shall_record_the_authorization_code(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		var assignment = ((Member) Member.getWithEmail(string)).getAssignment();
		assertEquals(string2, assignment.getPaymentCode());

	}

	/**
	 * @author yujieqin
	 * @param string
	 */
	@Then("the member account with the email {string} does not exist")
	public void the_member_account_with_the_email_does_not_exist(String string) {
		// Write code here that turns the phrase above into concrete actions

		Assert.assertNull(findMemberFromEmail(string));

	}

	/**
	 * @author Dawei Zhou
	 * @param count
	 */
	@Then("there are {string} members in the system")
	public void there_are_members_in_the_system(String count) {
		assertEquals(Integer.parseInt(count), climbSafe.getMembers().size());
	}

	/**
	 * @author Dawei Zhou
	 * @param message
	 */
	@Then("the error {string} shall be raised")
	public void the_error_shall_be_raised(String message) {
		System.out.println(error);
		assertTrue(error.contains(message));
	}

	/**
	 * @author Dawei Zhou
	 * @param email
	 */
	@When("the administrator attempts to cancel the trip for {string}")
	public void the_administrator_attempts_to_cancel_the_trip_for(String email) {
		try {
			AssignmentController.cancelMemberTrip(email);
		} catch (Exception e) {
			error += e.getMessage();
		}
	}

	/**
	 * @author Dawei Zhou
	 * @param email
	 */
	@Given("the member with {string} has paid for their trip")
	public void the_member_with_has_paid_for_their_trip(String email) {
		Member foundMember = null;
		for (var member : climbSafe.getMembers()) {
			if (member.getEmail().equals(email)) {
				foundMember = member;
				break;
			}
		}
		foundMember.getAssignment().pay("TheRightCode");

	}

	/**
	 * @author FrederikMartin
	 * @param email
	 * @param percentRefund
	 */
	@Then("the member with email address {string} shall receive a refund of {string} percent")
	public void the_member_with_email_address_shall_receive_a_refund_of_percent(String email, String percentRefund) {
		assertEquals(((Member) Member.getWithEmail(email)).getAssignment().getRefund(),
				Integer.parseInt(percentRefund));
	}

	/**
	 * @author FrederikMartin
	 * @param email
	 */
	@Given("the member with {string} has started their trip")
	public void the_member_with_has_started_their_trip(String email) {
		var assignment = ((Member) Member.getWithEmail(email)).getAssignment();
		assignment.pay("asdf");// give random valid code
		assignment.startTrip();// start trip
	}

	/**
	 * @author FrederikMartin
	 * @param email
	 */
	@When("the administrator attempts to finish the trip for the member with email {string}")
	public void the_administrator_attempts_to_finish_the_trip_for_the_member_with_email(String email) {
		try {
			AssignmentController.finishTrip(email);
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}
	}

	/**
	 * @author FrederikMartin
	 * @param email
	 */
	@Given("the member with {string} is banned")
	public void the_member_with_is_banned(String email) {
		var assignment = ((Member) Member.getWithEmail(email)).getAssignment();
		assignment.startTrip();// start trip without paying moves to banned
	}

	/**
	 * @author rambodazimi
	 * @param string
	 * @param string2
	 */
	@Then("the member with email {string} shall be {string}")
	public void the_member_with_email_shall_be(String email, String state) {
		assertEquals(((Member) (Member.getWithEmail(email))).getAssignment().getStatusFullName(), state);
	}

	/**
	 * @author rambodazimi
	 * @param string
	 */
	@When("the administrator attempts to start the trips for week {string}")
	public void the_administrator_attempts_to_start_the_trips_for_week(String string) {
		try {
			AssignmentController.startTrip(Integer.parseInt(string));
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}

	}

	/**
	 * @author rambodazimi
	 * @param string
	 */
	@Given("the member with {string} has cancelled their trip")
	public void the_member_with_has_cancelled_their_trip(String string) {
		// find a user with the same email address
		Member member = (Member) Member.getWithEmail(string);
		var assignment = member.getAssignment();
		assignment.cancel(); // cancel the trip
	}

	/**
	 * @author rambodazimi
	 * @param string
	 */
	@Given("the member with {string} has finished their trip")
	public void the_member_with_has_finished_their_trip(String string) {
		// find a user with the same email address
		Member member = (Member) Member.getWithEmail(string);
		var assignment = member.getAssignment();
		assignment.pay("asdf");
		assignment.startTrip();
		assignment.finish(); // finish the trip
	}

	/**
	 * Helper method used to find a member of the climbsafe system from its email
	 * address
	 * 
	 * @author Yujie
	 * @param email Email of the member to find in the climbsafe system instance
	 * @return
	 */
	private Member findMemberFromEmail(String email) {
		List<Member> memberList = ClimbSafeApplication.getClimbSafe().getMembers();
		for (Member member : memberList) {
			if (member.getEmail().equals(email)) {
				return member;
			}
		}
		return null;
	}
}
