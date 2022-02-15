package ca.mcgill.ecse.climbsafe.controller;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.BookableItem;
import ca.mcgill.ecse.climbsafe.model.BundleItem;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Equipment;
import ca.mcgill.ecse.climbsafe.model.EquipmentBundle;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;
public class ClimbSafeFeatureSet5Controller {
	
	private static ClimbSafe climbSafe = ClimbSafeApplication.getClimbSafe();
	
	/**
	 * @author KarimAtoui
	 * 
	 * @param name name of the equipment bundle
	 * @param discount discount of the equipment bundle from 0 to 100
	 * @param equipmentNames list of equipment names for the equipment bundle
	 * @param equipmentQuantities list of equipment quantity for the equipment bundle in the same order as equipmentNames
	 * @throws InvalidInputException exception for a faulty parameter
	 * 
	 * This method creates and adds an EquipmentBundle object to the climbSafe system.
	 */
	
	public static void addEquipmentBundle(String name, int discount, List<String> equipmentNames,
      List<Integer> equipmentQuantities) throws InvalidInputException {
	  
	  checkParams(name, discount, equipmentNames, equipmentQuantities);
	  
	  //duplicate name in system
	  if (BookableItem.hasWithName(name)) {
		  throw new InvalidInputException("A bookable item called " + name + " already exists");
	  }

	  EquipmentBundle equipmentBundle = new EquipmentBundle(name, discount, climbSafe);

	  for (int i=0; i<equipmentNames.size(); i++) {
		  
		  Equipment equipment = null;
		  String equipmentName = equipmentNames.get(i);
		  int equipmentQuantity= equipmentQuantities.get(i).intValue();
		  
		  for (int j=0; j<climbSafe.getEquipment().size(); j++) {
			  if (equipmentName.equals(climbSafe.getEquipment(j).getName())) {
				  equipment= climbSafe.getEquipment(j);
				  break;
			  } 
		  }
		  
		  equipmentBundle.addBundleItem(new BundleItem(equipmentQuantity, climbSafe, equipmentBundle, equipment));
		  
	  }
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        } 
	}
	  
	
	/**
	 * @author KarimAtoui
	 * 
	 * @param oldName name of the equipment bundle to be modified
	 * @param newName new name for the equipment bundle
	 * @param newDiscount new discount for the equipment bundle
	 * @param newEquipmentNames new list of equipment names for the equipment bundle
	 * @param newEquipmentQuantities new list of equipment quantities for the equipment bundle
	 * @throws InvalidInputException exception for a faulty parameter
	 * 
	 * This method update an equipment bundle by finding it, emptying it, and adding the updates.
	 */
	public static void updateEquipmentBundle(String oldName, String newName, int newDiscount,
      List<String> newEquipmentNames, List<Integer> newEquipmentQuantities)
      throws InvalidInputException {
	  
	  EquipmentBundle equipmentBundle=null;
	  
	  for (EquipmentBundle eb : climbSafe.getBundles()) {
		  if (eb.getName().equals(oldName)) {
			  equipmentBundle=eb;
			  break;
		  }
	  }
	  
	  if (equipmentBundle==null) {
		  throw new InvalidInputException("Equipment bundle "+ oldName + " does not exist");
	  }
	  
	  if (!oldName.equals(newName)) {
		  if (BookableItem.hasWithName(newName)) {
			  throw new InvalidInputException("A bookable item called " + newName + " already exists");
		  }
	  }
	  
	  checkParams(newName, newDiscount, newEquipmentNames, newEquipmentQuantities);
	  
	  equipmentBundle.setName(newName);	  
	  equipmentBundle.setDiscount(newDiscount);
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        }
	  for (int i=equipmentBundle.getBundleItems().size()-1; i>=0; i--) {
		  equipmentBundle.getBundleItem(i).delete();
	  }
	  
	  
	  for (int i=0; i<newEquipmentNames.size(); i++) {
		  
		  Equipment equipment = null;
		  String equipmentName = newEquipmentNames.get(i);
		  int equipmentQuantity= newEquipmentQuantities.get(i).intValue();
		  
		  for (int j=0; j<climbSafe.getEquipment().size(); j++) {
			  if (equipmentName.equals(climbSafe.getEquipment(j).getName())) {
				  equipment= climbSafe.getEquipment(j);
				  break;
			  } 
		  }
		  
		  equipmentBundle.addBundleItem(new BundleItem(equipmentQuantity, climbSafe, equipmentBundle, equipment));
	  }
	  try {
	        ClimbSafePersistence.save();					// Update the persistence layer
	      } catch (RuntimeException e) {
	        throw new InvalidInputException(e.getMessage());
	        }
	}
	
	
	/**
	 * This method will return the information for all bundles as a list of transfer objects
	 * 
	 * @author FrederikMartin
	 * @return List of bundles as transfer objects
	 */
	public static List<TOBundle> getEquipmentBundles() {
		List<TOBundle> bundleList = new ArrayList<TOBundle>();
		
		for (EquipmentBundle eb : climbSafe.getBundles()) {
			
			int totalPrice = 0;
			for(var bi:eb.getBundleItems()) {
				totalPrice+=bi.getEquipment().getPricePerWeek();
			}
			
			var toBundle = new TOBundle(eb.getName(), eb.getDiscount(), totalPrice);
			
			for(var bItem:eb.getBundleItems()) {
				var equipment = bItem.getEquipment();
				toBundle.addEquipment(new TOBundleItem(equipment.getName(), equipment.getWeight(), equipment.getPricePerWeek(), bItem.getQuantity()));
			}						
			
			bundleList.add(toBundle);
		  }
		
		climbSafe.getBundles();		
		return bundleList;
	}
	
	
	
	
	
	/**
	 * @author KarimAtoui
	 * 
	 * @param name
	 * @param discount
	 * @param equipmentNames
	 * @param equipmentQuantities
	 * @throws InvalidInputException
	 * 
	 * Helper method that verifies if the parameters respect certain logical suppositions
	 */
	
	private static void checkParams(String name, int discount, List<String> equipmentNames, List<Integer> equipmentQuantities) throws InvalidInputException {
		
		if (name==null || name.isBlank()) {
			  throw new InvalidInputException("Equipment bundle name cannot be empty");
		  }
		  
		  if (discount>100) {
			  throw new InvalidInputException("Discount must be no more than 100");
		  }
		  
		  if (discount<0) {
			  throw new InvalidInputException("Discount must be at least 0");
		  }

		  if (equipmentNames.size()<2) {
			  throw new InvalidInputException("Equipment bundle must contain at least two distinct types of equipment");
		  }

		  for (Integer i : equipmentQuantities) {
			  if (i<=0) {
				  throw new InvalidInputException("Each bundle item must have quantity greater than or equal to 1");
			  }
		  }
		  

		  for (int i=0; i<equipmentNames.size()-1; i++) {

			  for (int j=i+1; j<equipmentNames.size(); j++) {
				  if (equipmentNames.get(i).equals(equipmentNames.get(j))) {
					  throw new InvalidInputException("Equipment bundle must contain at least two distinct types of equipment");
				  }
			  }
		  }
		  
		  for (int i=0; i<equipmentNames.size(); i++) {
			  
			  String equipmentName = equipmentNames.get(i);
			  
			  if (!Equipment.hasWithName(equipmentName)) {
				  throw new InvalidInputException("Equipment "+ equipmentName +" does not exist");
			  }
		  }
	}
}

