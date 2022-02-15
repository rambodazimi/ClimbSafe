/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.controller;
import java.util.*;

/**
 * author FrederikMartin
 */
// line 21 "../../../../../ClimbSafeTransferObjects.ump"
public class TOBundle
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBundle Attributes
  private String name;
  private int discount;
  private int totalPricePerWeek;

  //TOBundle Associations
  private List<TOBundleItem> equipments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBundle(String aName, int aDiscount, int aTotalPricePerWeek)
  {
    name = aName;
    discount = aDiscount;
    totalPricePerWeek = aTotalPricePerWeek;
    equipments = new ArrayList<TOBundleItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDiscount(int aDiscount)
  {
    boolean wasSet = false;
    discount = aDiscount;
    wasSet = true;
    return wasSet;
  }

  public boolean setTotalPricePerWeek(int aTotalPricePerWeek)
  {
    boolean wasSet = false;
    totalPricePerWeek = aTotalPricePerWeek;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public int getDiscount()
  {
    return discount;
  }

  public int getTotalPricePerWeek()
  {
    return totalPricePerWeek;
  }
  /* Code from template association_GetMany */
  public TOBundleItem getEquipment(int index)
  {
    TOBundleItem aEquipment = equipments.get(index);
    return aEquipment;
  }

  public List<TOBundleItem> getEquipments()
  {
    List<TOBundleItem> newEquipments = Collections.unmodifiableList(equipments);
    return newEquipments;
  }

  public int numberOfEquipments()
  {
    int number = equipments.size();
    return number;
  }

  public boolean hasEquipments()
  {
    boolean has = equipments.size() > 0;
    return has;
  }

  public int indexOfEquipment(TOBundleItem aEquipment)
  {
    int index = equipments.indexOf(aEquipment);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEquipments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addEquipment(TOBundleItem aEquipment)
  {
    boolean wasAdded = false;
    if (equipments.contains(aEquipment)) { return false; }
    TOBundle existingTOBundle = aEquipment.getTOBundle();
    if (existingTOBundle == null)
    {
      aEquipment.setTOBundle(this);
    }
    else if (!this.equals(existingTOBundle))
    {
      existingTOBundle.removeEquipment(aEquipment);
      addEquipment(aEquipment);
    }
    else
    {
      equipments.add(aEquipment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEquipment(TOBundleItem aEquipment)
  {
    boolean wasRemoved = false;
    if (equipments.contains(aEquipment))
    {
      equipments.remove(aEquipment);
      aEquipment.setTOBundle(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEquipmentAt(TOBundleItem aEquipment, int index)
  {  
    boolean wasAdded = false;
    if(addEquipment(aEquipment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEquipments()) { index = numberOfEquipments() - 1; }
      equipments.remove(aEquipment);
      equipments.add(index, aEquipment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEquipmentAt(TOBundleItem aEquipment, int index)
  {
    boolean wasAdded = false;
    if(equipments.contains(aEquipment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEquipments()) { index = numberOfEquipments() - 1; }
      equipments.remove(aEquipment);
      equipments.add(index, aEquipment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEquipmentAt(aEquipment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while( !equipments.isEmpty() )
    {
      equipments.get(0).setTOBundle(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "discount" + ":" + getDiscount()+ "," +
            "totalPricePerWeek" + ":" + getTotalPricePerWeek()+ "]";
  }
}