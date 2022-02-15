package ca.mcgill.ecse.climbsafe.controller;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.Assignment;
import ca.mcgill.ecse.climbsafe.model.BookableItem;
import ca.mcgill.ecse.climbsafe.model.BookedItem;
import ca.mcgill.ecse.climbsafe.model.BundleItem;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Equipment;
import ca.mcgill.ecse.climbsafe.model.EquipmentBundle;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet6Controller {

	private static ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
	private static final String equipmentinBundleMsg = "The piece of equipment is in a bundle and cannot be deleted";
	
  /**
   * This method will delete the equipment with the
   * passed name if any. If there is no such equipment 
   * with the passed name, nothing happen. If the equipment 
   * is exist in a bundle, Exception will be thrown
   * 
   * @author AbdelrahmanAli
   * @param name Equipment name
   * @throws InvalidInputException
   */
public static void deleteEquipment(String name) throws InvalidInputException {
	  
	  // Check if equipment is exist with the provided name, if not create null equipment
	   Equipment equipment =  (BookableItem.getWithName(name) instanceof Equipment ) ? 
				(Equipment)BookableItem.getWithName(name) : null;
		
	  if (equipment == null) return;							// If no equipment with this name, nothing to do.
	  
	  List<EquipmentBundle> eBs = climbSafe.getBundles();		// Before delete any equipment, make sure its not a part of a bundle
	  for (EquipmentBundle eB : eBs) {
		  for (BundleItem bI : eB.getBundleItems()) {
			  if (bI.getEquipment().getName().equals(equipment.getName())) {	// if found any, throw an exception to be handled
				  throw new InvalidInputException(equipmentinBundleMsg);
			  }
		  }
	  }
	  if (equipment != null) {									// check if we found any
		  equipment.delete();									// delete if we found any and not exist in a bundle
	  }
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        }
  }

  /**
   * This method will delete the bundle with the
   * passed name if any. If there is no such one
   * with the passed name, nothing happen.
   * 
   * @author AbdelrahmanAli
   * @param name Equipment name
 * @throws InvalidInputException 
   */
public static void deleteEquipmentBundle(String name) throws InvalidInputException {  
	  
	  List<EquipmentBundle> bundles = climbSafe.getBundles();	// get all the equipments in ClimbSafe.
	  EquipmentBundle equipmentBundle = null;					// set the desired bundle as null
	  for (EquipmentBundle bd : bundles) {
		  if (bd.getName().equals(name)) {						// check if this is the name we are looking for
			  equipmentBundle = bd;								// if yes, break
			  break;
		  }	 
	  }
	  if (equipmentBundle != null) {							// check if we found any
		  equipmentBundle.delete();								// delete if we found any
	  }
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        }
  }
	  
 

  /**
   * when this method is called will return all the assignments in the
   * ClimbSafe Systems. This method does not check if 
   * the caller is administrator nor if its a user
   * 
   * @author AbdelrahmanAli
   * @return a list of all assignments 
   */
public static List<TOAssignment> getAssignments() {
	  
	  List<Assignment> assignments = climbSafe.getAssignments();		//First get all assignments from the system
	  List<TOAssignment> toAssignments = new ArrayList<TOAssignment>();	// Initialize a TO Object
	 
	  for (Assignment ass : assignments) {
		  TOAssignment toAssignment = new TOAssignment(ass.getMember().getEmail(),
				  ass.getMember().getName(),
				  null,							// Guide maybe null, will check later
				  null,							// Guide maybe null, will check later
				  null,							// set the hotel null for now, will update it later if any
				  ass.getStartWeek(),
				  ass.getEndWeek(),
				  0,							// set cost for guide 0 for now
				  0,							// set cost for rentals 0 for now	
				  ass.getStatus().toString(),
				  ass.getPaymentCode(),
				  ass.getRefund());							
		  
		  int numOfWeeks = ass.getEndWeek() - ass.getStartWeek() + 1;
		  
		  // add guide info if guide is not empty
		  if (ass.getGuide() != null) {
			  toAssignment.setGuideEmail(ass.getGuide().getEmail());
			  toAssignment.setGuideName(ass.getGuide().getName());
			  toAssignment.setTotalCostForGuide(climbSafe.getPriceOfGuidePerWeek() * numOfWeeks);
		  }
		  
		  // set the hotel but check first if it is empty
		  toAssignment.setHotelName(ass.getHotel() == null ? null : ass.getHotel().getName());
		  
		  // cost will be rounded to Integers later since Neptan currency of shillings does not have any cents
		  float cost = 0f;
		  List<BookedItem> items = ass.getMember().getBookedItems();	// get all the booked items by the member		  
		  for (BookedItem bookedItem : items) {
			  
			   BookableItem bI =  bookedItem.getItem();
			   if (bI instanceof Equipment) {
				   // total cost per week for the quantity of this equipment
				   cost += (((Equipment) bI).getPricePerWeek()  * bookedItem.getQuantity());
				   
			   } else if (bI instanceof EquipmentBundle) {
				   
				   int bundleCost = bundleCost((EquipmentBundle) bI);
				   // (100 - discount)/100
				   float priceDiscount = (float) (100 - ((EquipmentBundle) bI).getDiscount())/100;
				   // Discount only apply if a guide is hired, we set the price as 100% if no guide
				   priceDiscount = ass.getMember().getGuideRequired() ? priceDiscount : 1;
				   cost += (bundleCost * priceDiscount * (bookedItem.getQuantity()));
			   }
			   
		  }
		  toAssignment.setTotalCostForEquipment((int) cost * numOfWeeks);
		  toAssignments.add(toAssignment);
	  }
	  return toAssignments;
  }

/**
 * This is a helper method to calculate the total cost
 * of a bundle excluding any discounts
 * 
 * @author AbdelrahmanAli
 * @param bundle
 * @return total cost of a bundle
 */
private static int bundleCost(EquipmentBundle bundle) {
	  
	   List<BundleItem> list = bundle.getBundleItems();		// get all items in the bundle
	   int subCost = 0;										// initiate the cost as 0
	   for (BundleItem bundleItem : list) {
		   Equipment qq = bundleItem.getEquipment();
		  subCost += (qq.getPricePerWeek() * bundleItem.getQuantity());		// Accumulate on the cost
	   }
	return subCost;
}
  
}

