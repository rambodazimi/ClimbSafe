/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.controller;
import java.util.*;

/**
 * @author AbdelrahmanAli
 */
// line 54 "../../../../../ClimbSafeTransferObjects.ump"
public class TOMember
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOMember Attributes
  private String email;
  private String name;
  private String emergancyContact;
  private String password;
  private int weeks;
  private boolean needGuide;
  private boolean needHotel;

  //TOMember Associations
  private List<TOGenericItem> items;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOMember(String aEmail, String aName, String aEmergancyContact, String aPassword, int aWeeks, boolean aNeedGuide, boolean aNeedHotel)
  {
    email = aEmail;
    name = aName;
    emergancyContact = aEmergancyContact;
    password = aPassword;
    weeks = aWeeks;
    needGuide = aNeedGuide;
    needHotel = aNeedHotel;
    items = new ArrayList<TOGenericItem>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
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

  public boolean setEmergancyContact(String aEmergancyContact)
  {
    boolean wasSet = false;
    emergancyContact = aEmergancyContact;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setWeeks(int aWeeks)
  {
    boolean wasSet = false;
    weeks = aWeeks;
    wasSet = true;
    return wasSet;
  }

  public boolean setNeedGuide(boolean aNeedGuide)
  {
    boolean wasSet = false;
    needGuide = aNeedGuide;
    wasSet = true;
    return wasSet;
  }

  public boolean setNeedHotel(boolean aNeedHotel)
  {
    boolean wasSet = false;
    needHotel = aNeedHotel;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getName()
  {
    return name;
  }

  public String getEmergancyContact()
  {
    return emergancyContact;
  }

  public String getPassword()
  {
    return password;
  }

  public int getWeeks()
  {
    return weeks;
  }

  public boolean getNeedGuide()
  {
    return needGuide;
  }

  public boolean getNeedHotel()
  {
    return needHotel;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isNeedGuide()
  {
    return needGuide;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isNeedHotel()
  {
    return needHotel;
  }
  /* Code from template association_GetMany */
  public TOGenericItem getItem(int index)
  {
    TOGenericItem aItem = items.get(index);
    return aItem;
  }

  public List<TOGenericItem> getItems()
  {
    List<TOGenericItem> newItems = Collections.unmodifiableList(items);
    return newItems;
  }

  public int numberOfItems()
  {
    int number = items.size();
    return number;
  }

  public boolean hasItems()
  {
    boolean has = items.size() > 0;
    return has;
  }

  public int indexOfItem(TOGenericItem aItem)
  {
    int index = items.indexOf(aItem);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addItem(TOGenericItem aItem)
  {
    boolean wasAdded = false;
    if (items.contains(aItem)) { return false; }
    TOMember existingTOMember = aItem.getTOMember();
    if (existingTOMember == null)
    {
      aItem.setTOMember(this);
    }
    else if (!this.equals(existingTOMember))
    {
      existingTOMember.removeItem(aItem);
      addItem(aItem);
    }
    else
    {
      items.add(aItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeItem(TOGenericItem aItem)
  {
    boolean wasRemoved = false;
    if (items.contains(aItem))
    {
      items.remove(aItem);
      aItem.setTOMember(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addItemAt(TOGenericItem aItem, int index)
  {  
    boolean wasAdded = false;
    if(addItem(aItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItems()) { index = numberOfItems() - 1; }
      items.remove(aItem);
      items.add(index, aItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveItemAt(TOGenericItem aItem, int index)
  {
    boolean wasAdded = false;
    if(items.contains(aItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItems()) { index = numberOfItems() - 1; }
      items.remove(aItem);
      items.add(index, aItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addItemAt(aItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while( !items.isEmpty() )
    {
      items.get(0).setTOMember(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "name" + ":" + getName()+ "," +
            "emergancyContact" + ":" + getEmergancyContact()+ "," +
            "password" + ":" + getPassword()+ "," +
            "weeks" + ":" + getWeeks()+ "," +
            "needGuide" + ":" + getNeedGuide()+ "," +
            "needHotel" + ":" + getNeedHotel()+ "]";
  }
}