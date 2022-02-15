package ca.mcgill.ecse.climbsafe.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Guide;
import ca.mcgill.ecse.climbsafe.model.Member;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet1Controller {
	
	private static ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
	
	/**
	 * This method takes a startDate, nrWeeks and a priceOfGuidePerWeek, validate
	 * the input then update the climbsafe system.
	 * @author Dawei Zhou
	 * @param startDate
	 * @param nrWeeks
	 * @param priceOfGuidePerWeek
	 * @throws InvalidInputException
	 */
  public static void setup(Date startDate, int nrWeeks, int priceOfGuidePerWeek)
      throws InvalidInputException {
	
	  var error = "";
	  // the startDate should not be earlier than today
	  long millis=System.currentTimeMillis();  
	  Date today=new java.sql.Date(millis); // a Date object of today
	  
	  try {
		  
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  LocalDate dt = LocalDate.parse(startDate.toString(), formatter);
	  }catch(Exception e) {
		  error = "Invalid date";
	  }
	  
	  if (nrWeeks<1) {
		  error = "The number of climbing weeks must be greater than or equal to zero.";
	  }
	  if (nrWeeks>52) {
		  error = "The climbing season cannot be longger than a year.";
	  }
	  if (priceOfGuidePerWeek<=0) {
		  error = "The price of guide per week must be greater than or equal to zero.";
	  }
	  if (!error.isEmpty()) {
	      throw new InvalidInputException(error.trim());
	  }
	  
	  // Setup the climbsafe program
	  try {
	      climbSafe.setStartDate(startDate);
	      climbSafe.setNrWeeks(nrWeeks);
	      climbSafe.setPriceOfGuidePerWeek(priceOfGuidePerWeek);
	      for (Guide g : climbSafe.getGuides()) {
	    	  g.setRemainingWeeks(nrWeeks);
	      }
	      ClimbSafePersistence.save();					// Update the persistence layer		      
	    } catch (RuntimeException e) {
	      throw new InvalidInputException(e.getMessage());
	    }
		  
  }
  
/** This method take a email of a member and delete him/her in the system. 
 * 
 * @author Dawei Zhou
 * @param email
 * @throws InvalidInputException 
 */
  public static void deleteMember(String email) throws InvalidInputException {
	  Member member=findMember(email);
	  if (member != null) {
		  member.delete();
		  try {
		        ClimbSafePersistence.save();					// Update the persistence layer
		      } catch (RuntimeException e) {
		        throw new InvalidInputException(e.getMessage());
		        }
	    }
  }
  
  
  /** This is a helper function. It takes a email and return the member object.
   * 
   * @author Dawei Zhou
   * @param email
   * @return Member
   */
  private static Member findMember(String email) {
	    Member foundMember = null;
	    for (var member : climbSafe.getMembers()) {
	      if (member.getEmail().equals(email) ) {
	    	  foundMember = member;
	        break;
	      }
	    }
	    return foundMember;
	  }
  /**This method takes a email address and then delete the Guide object from the system.
   * 
   * @author Dawei Zhou
   * @param email
 * @throws InvalidInputException 
   */
  public static void deleteGuide(String email) throws InvalidInputException {
	  Guide guide = findGuide(email);
	  
	  if (guide != null) {
		  guide.delete();
		  try {
		        ClimbSafePersistence.save();					// Update the persistence layer
		      } catch (RuntimeException e) {
		        throw new InvalidInputException(e.getMessage());
		        }
	    }
	  
  }
  
  /** This is a helper function. It takes a email and return a Guide object.
   * @author Dawei Zhou
   * @param email
   * @return
   */
  private static Guide findGuide(String email) {
	    Guide foundGuide = null;
	    for (var guide : climbSafe.getGuides()) {
	    	String thisEmail=guide.getEmail();
	    	
	      if (thisEmail.equals(email)) {
	    	  foundGuide = guide;
	    	  break;
	      }
	    }
	    
	    return foundGuide;
	  }
  
  // this method needs to be implemented only by teams with seven team members
  public static void deleteHotel(String name) {}

  public static TOClimbSafe getClimbSafe() {
	  return new TOClimbSafe(climbSafe.getPriceOfGuidePerWeek(), climbSafe.getNrWeeks(), climbSafe.getStartDate());
  }
}

