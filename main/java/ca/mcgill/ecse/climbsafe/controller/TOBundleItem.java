/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.controller;

/**
 * author FrederikMartin
 */
// line 29 "../../../../../ClimbSafeTransferObjects.ump"
public class TOBundleItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBundleItem Attributes
  private String equipmentName;
  private int weight;
  private int pricePerWeek;
  private int quantity;

  //TOBundleItem Associations
  private TOBundle tOBundle;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBundleItem(String aEquipmentName, int aWeight, int aPricePerWeek, int aQuantity)
  {
    equipmentName = aEquipmentName;
    weight = aWeight;
    pricePerWeek = aPricePerWeek;
    quantity = aQuantity;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEquipmentName(String aEquipmentName)
  {
    boolean wasSet = false;
    equipmentName = aEquipmentName;
    wasSet = true;
    return wasSet;
  }

  public boolean setWeight(int aWeight)
  {
    boolean wasSet = false;
    weight = aWeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setPricePerWeek(int aPricePerWeek)
  {
    boolean wasSet = false;
    pricePerWeek = aPricePerWeek;
    wasSet = true;
    return wasSet;
  }

  public boolean setQuantity(int aQuantity)
  {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public String getEquipmentName()
  {
    return equipmentName;
  }

  public int getWeight()
  {
    return weight;
  }

  public int getPricePerWeek()
  {
    return pricePerWeek;
  }

  public int getQuantity()
  {
    return quantity;
  }
  /* Code from template association_GetOne */
  public TOBundle getTOBundle()
  {
    return tOBundle;
  }

  public boolean hasTOBundle()
  {
    boolean has = tOBundle != null;
    return has;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setTOBundle(TOBundle aTOBundle)
  {
    boolean wasSet = false;
    TOBundle existingTOBundle = tOBundle;
    tOBundle = aTOBundle;
    if (existingTOBundle != null && !existingTOBundle.equals(aTOBundle))
    {
      existingTOBundle.removeEquipment(this);
    }
    if (aTOBundle != null)
    {
      aTOBundle.addEquipment(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (tOBundle != null)
    {
      TOBundle placeholderTOBundle = tOBundle;
      this.tOBundle = null;
      placeholderTOBundle.removeEquipment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "equipmentName" + ":" + getEquipmentName()+ "," +
            "weight" + ":" + getWeight()+ "," +
            "pricePerWeek" + ":" + getPricePerWeek()+ "," +
            "quantity" + ":" + getQuantity()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "tOBundle = "+(getTOBundle()!=null?Integer.toHexString(System.identityHashCode(getTOBundle())):"null");
  }
}