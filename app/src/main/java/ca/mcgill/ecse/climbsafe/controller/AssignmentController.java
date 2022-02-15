package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.Assignment;
import ca.mcgill.ecse.climbsafe.model.Assignment.Status;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.model.User;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class AssignmentController {

	private static ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();

	/**
	 * @author KarimAtoui
	 * @throws InvalidInputException This method iterates through all guides and
	 *                               creates an assignment with a member if
	 *                               appropriate, else the member is given an
	 *                               appropriate assignment
	 */
	public static void assign() throws InvalidInputException {

		if (climbSafe.getGuides().size() == 0) {
			for (Member member : climbSafe.getMembers()) {
				if (!member.hasAssignment() && !member.getGuideRequired()) {
					new Assignment(1, member.getNrWeeks(), member, climbSafe);
				}
			}
		}

		else // guide surfing
			for (Guide guide : climbSafe.getGuides()) {
				assignHelper(guide);
			}

		// check if someone didn't get assigned
		for (Member member : climbSafe.getMembers()) {
			if (!member.hasAssignment())
				throw new InvalidInputException("Assignments could not be completed for all members");
		}

		try {
			ClimbSafePersistence.save(); // Update the persistence layer
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * @author KarimAtoui
	 * @param guide
	 * @throws InvalidInputException Helper method to simplify assign method
	 */
	private static void assignHelper(Guide guide) throws InvalidInputException {
		// member surfing
		for (Member member : climbSafe.getMembers()) {
			// skip iteration if unnecessary
			if (member.hasAssignment()) {
				continue;
			}

			// variable
			int demandWeeks = member.getNrWeeks();
			if (member.getGuideRequired()) {
				// to calculate startweek and endweek of assignment
				int temp = guide.getRemainingWeeks();
				// create an assignment object
				Assignment assignment = new Assignment(0, 0, member, climbSafe);

				// guide assigning if appropriate
				if (guide.assign(assignment, demandWeeks)) {
					assignment.setStartWeek(1 + (climbSafe.getNrWeeks() - temp));
					assignment.setEndWeek(demandWeeks + (climbSafe.getNrWeeks() - temp));
				} else {
					assignment.delete(); // assignment object is deleted if unused
				}
			} else { // i.e. if member doesnt require guide
				new Assignment(1, demandWeeks, member, climbSafe);
			}

			try {
				ClimbSafePersistence.save(); // Update the persistence layer
			} catch (RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
		}
	}


	/**
	 * This method will let the member to pay using the authorization code and then
	 * set the status of the member, and catch the exception
	 * 
	 * @author yujieqin
	 * @param memberEmail
	 * @param code
	 * @throws InvalidInputException
	 */

	public static void payTrip(String memberEmail, String code) throws InvalidInputException {

		User u = Member.getWithEmail(memberEmail);

		if (!(u instanceof Member m)) {
			throw new InvalidInputException("Member with email address " + memberEmail + " does not exist");
		}
		switch (m.getAssignment().getStatus()) {
		case Assigned:
			if (!m.getAssignment().pay(code))
				throw new InvalidInputException("Invalid authorization code");
			try {
				ClimbSafePersistence.save(); // Update the persistence layer
			} catch (RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
			break;
		case Started:
		case Paid:
			throw new InvalidInputException("Trip has already been paid for");
		case Banned:
			throw new InvalidInputException("Cannot pay for the trip due to a ban");
		case Cancelled:
			throw new InvalidInputException("Cannot pay for a trip which has been cancelled");
		case Finished:
			throw new InvalidInputException("Cannot pay for a trip which has finished");
		default:
			break;
		}
	}

	/**
	 * This method will change the status of the assignment of a certain member to
	 * Finished.
	 * 
	 * @author FrederikMartin
	 * @param memberEmail
	 * @throws InvalidInputException
	 */
	public static void finishTrip(String memberEmail) throws InvalidInputException {

		User u = Member.getWithEmail(memberEmail);

		if (!(u instanceof Member m)) {
			throw new InvalidInputException("Member with email address " + memberEmail + " does not exist");
		}
		var assignment = m.getAssignment();

		switch (assignment.getStatus()) {
		case Assigned:
		case Paid:
			throw new InvalidInputException("Cannot finish a trip which has not started");
		case Banned:
			throw new InvalidInputException("Cannot finish the trip due to a ban");
		case Cancelled:
			throw new InvalidInputException("Cannot finish a trip which has been cancelled");
		case Started:
			assignment.finish();
			try {
				ClimbSafePersistence.save(); // Update the persistence layer
			} catch (RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
			break;
		case Finished:
		default:
			break;
		}
	}

	/**
	 * This method goes through all assignments and start the trip. Any Exception
	 * will handled after starting all assignments.
	 * 
	 *
	 * @author: Dawei Zhou
	 * @@param: int week
	 * @output: None
	 * 
	 * 
	 * @throws Exception
	 */
	public static void startTrip(int week) throws InvalidInputException {

		String error = null;

		for (Assignment assignment : climbSafe.getAssignments()) {

			if (assignment.getStartWeek() == week) {
				switch (assignment.getStatus()) {
				case Assigned:
				case Paid:
					assignment.startTrip();
					try {
						ClimbSafePersistence.save(); // Update the persistence layer
					} catch (RuntimeException e) {
						throw new InvalidInputException(e.getMessage());
					}
					break;
				case Banned:
					if (error == null)
						error = "Cannot start the trip due to a ban";
				case Cancelled:
					if (error == null)
						error = "Cannot start a trip which has been cancelled";
				case Finished:
					if (error == null)
						error = "Cannot start a trip which has finished";
				case Started:
				default:
					break;
				}

			}
		}
		if (error != null)
			throw new InvalidInputException(error);
	}

	/**
	 * This method will cancel trip for member. Different cases are NotPaid, Paid,
	 * Started, Banned, and Finished
	 * 
	 * @author rambodazimi
	 * @param email
	 * @throws InvalidInputException
	 */
	public static void cancelMemberTrip(String email) throws InvalidInputException {

		// find a user with the same email address
		User user = Member.getWithEmail(email);

		// check whether the user with that email is member or not
		if ((!(user instanceof Member member)) || user == null) { // if not member
			throw new InvalidInputException("Member with email address " + email + " does not exist");
		}

		var assignment = member.getAssignment();

		Status assignmentStatus = assignment.getStatus();

		if (assignmentStatus == Status.Assigned) { // has not paid yet
			assignment.cancel(); // cancel with full refund
		}
		if (assignmentStatus == Status.Paid) { // has already paid
			assignment.cancel(); // with refund of 50%
		}
		if (assignmentStatus == Status.Started) { // has already started
			assignment.cancel(); // with refund of 10%
		}
		if (assignmentStatus == Status.Banned) { // if member is banned
			throw new InvalidInputException("Cannot cancel the trip due to a ban");
		}
		if (assignmentStatus == Status.Finished) { // if the trip is finished
			throw new InvalidInputException("Cannot cancel a trip which has finished");
		}
		try {
			ClimbSafePersistence.save(); // Update the persistence layer
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * @author AbdelrahmanAli
	 * @param memberEmail
	 * @return true if the member exist and has assignment
	 */
	public static boolean hasAssignment(String memberEmail) {
		return Member.getWithEmail(memberEmail) != null ? ((Member) Member.getWithEmail(memberEmail)).hasAssignment()
				: false;
	}
}
