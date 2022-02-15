/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.controller;
import java.sql.Date;

/**
 * @author AbdelrahmanAli
 */
// line 72 "../../../../../ClimbSafeTransferObjects.ump"
public class TOClimbSafe
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOClimbSafe Attributes
  private int guideCost;
  private int numOfWeeks;
  private Date date;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOClimbSafe(int aGuideCost, int aNumOfWeeks, Date aDate)
  {
    guideCost = aGuideCost;
    numOfWeeks = aNumOfWeeks;
    date = aDate;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setGuideCost(int aGuideCost)
  {
    boolean wasSet = false;
    guideCost = aGuideCost;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumOfWeeks(int aNumOfWeeks)
  {
    boolean wasSet = false;
    numOfWeeks = aNumOfWeeks;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public int getGuideCost()
  {
    return guideCost;
  }

  public int getNumOfWeeks()
  {
    return numOfWeeks;
  }

  public Date getDate()
  {
    return date;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "guideCost" + ":" + getGuideCost()+ "," +
            "numOfWeeks" + ":" + getNumOfWeeks()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null");
  }
}