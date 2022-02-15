/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.controller;

/**
 * author FrederikMartin
 */
// line 65 "../../../../../ClimbSafeTransferObjects.ump"
public class TOGenericItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOGenericItem Attributes
  private int quantity;
  private String name;

  //TOGenericItem Associations
  private TOMember tOMember;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOGenericItem(int aQuantity, String aName)
  {
    quantity = aQuantity;
    name = aName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setQuantity(int aQuantity)
  {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public String getName()
  {
    return name;
  }
  /* Code from template association_GetOne */
  public TOMember getTOMember()
  {
    return tOMember;
  }

  public boolean hasTOMember()
  {
    boolean has = tOMember != null;
    return has;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setTOMember(TOMember aTOMember)
  {
    boolean wasSet = false;
    TOMember existingTOMember = tOMember;
    tOMember = aTOMember;
    if (existingTOMember != null && !existingTOMember.equals(aTOMember))
    {
      existingTOMember.removeItem(this);
    }
    if (aTOMember != null)
    {
      aTOMember.addItem(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (tOMember != null)
    {
      TOMember placeholderTOMember = tOMember;
      this.tOMember = null;
      placeholderTOMember.removeItem(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "quantity" + ":" + getQuantity()+ "," +
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "tOMember = "+(getTOMember()!=null?Integer.toHexString(System.identityHashCode(getTOMember())):"null");
  }
}