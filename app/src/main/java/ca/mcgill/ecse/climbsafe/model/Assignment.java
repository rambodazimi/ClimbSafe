/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.model;
import java.io.Serializable;

// line 23 "../../../../../ClimbSafeStates.ump"
// line 129 "../../../../../ClimbSafePersistence.ump"
// line 97 "../../../../../ClimbSafe.ump"
public class Assignment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Assignment Attributes
  private int startWeek;
  private int endWeek;
  private int refundAmount;
  private String paymentCode;

  //Assignment State Machines
  public enum Status { Assigned, Banned, Paid, Started, Finished, Final, Cancelled }
  private Status status;

  //Assignment Associations
  private Member member;
  private Guide guide;
  private Hotel hotel;
  private ClimbSafe climbSafe;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Assignment(int aStartWeek, int aEndWeek, Member aMember, ClimbSafe aClimbSafe)
  {
    startWeek = aStartWeek;
    endWeek = aEndWeek;
    refundAmount = 0;
    paymentCode = null;
    boolean didAddMember = setMember(aMember);
    if (!didAddMember)
    {
      throw new RuntimeException("Unable to create assignment due to member. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddClimbSafe = setClimbSafe(aClimbSafe);
    if (!didAddClimbSafe)
    {
      throw new RuntimeException("Unable to create assignment due to climbSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setStatus(Status.Assigned);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartWeek(int aStartWeek)
  {
    boolean wasSet = false;
    startWeek = aStartWeek;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndWeek(int aEndWeek)
  {
    boolean wasSet = false;
    endWeek = aEndWeek;
    wasSet = true;
    return wasSet;
  }

  public boolean setPaymentCode(String aPaymentCode)
  {
    boolean wasSet = false;
    paymentCode = aPaymentCode;
    wasSet = true;
    return wasSet;
  }

  public int getStartWeek()
  {
    return startWeek;
  }

  public int getEndWeek()
  {
    return endWeek;
  }

  public String getPaymentCode()
  {
    return paymentCode;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean pay(String authorizationCode)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Assigned:
        if (isValid(authorizationCode))
        {
        // line 27 "../../../../../ClimbSafeStates.ump"
          setCode(authorizationCode);
          setStatus(Status.Paid);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startTrip()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Assigned:
        setStatus(Status.Banned);
        wasEventProcessed = true;
        break;
      case Paid:
        setStatus(Status.Started);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancel()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Assigned:
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 35 "../../../../../ClimbSafeStates.ump"
        refund(50);
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      case Started:
        // line 39 "../../../../../ClimbSafeStates.ump"
        refund(10);
        setStatus(Status.Cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finish()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        setStatus(Status.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean seasonEnd()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Finished:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      case Cancelled:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;

    // entry actions and do activities
    switch(status)
    {
      case Final:
        delete();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Member getMember()
  {
    return member;
  }
  /* Code from template association_GetOne */
  public Guide getGuide()
  {
    return guide;
  }

  public boolean hasGuide()
  {
    boolean has = guide != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Hotel getHotel()
  {
    return hotel;
  }

  public boolean hasHotel()
  {
    boolean has = hotel != null;
    return has;
  }
  /* Code from template association_GetOne */
  public ClimbSafe getClimbSafe()
  {
    return climbSafe;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setMember(Member aNewMember)
  {
    boolean wasSet = false;
    if (aNewMember == null)
    {
      //Unable to setMember to null, as assignment must always be associated to a member
      return wasSet;
    }
    
    Assignment existingAssignment = aNewMember.getAssignment();
    if (existingAssignment != null && !equals(existingAssignment))
    {
      //Unable to setMember, the current member already has a assignment, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Member anOldMember = member;
    member = aNewMember;
    member.setAssignment(this);

    if (anOldMember != null)
    {
      anOldMember.setAssignment(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setGuide(Guide aGuide)
  {
    boolean wasSet = false;
    Guide existingGuide = guide;
    guide = aGuide;
    if (existingGuide != null && !existingGuide.equals(aGuide))
    {
      existingGuide.removeAssignment(this);
    }
    if (aGuide != null)
    {
      aGuide.addAssignment(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setHotel(Hotel aHotel)
  {
    boolean wasSet = false;
    Hotel existingHotel = hotel;
    hotel = aHotel;
    if (existingHotel != null && !existingHotel.equals(aHotel))
    {
      existingHotel.removeAssignment(this);
    }
    if (aHotel != null)
    {
      aHotel.addAssignment(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setClimbSafe(ClimbSafe aClimbSafe)
  {
    boolean wasSet = false;
    if (aClimbSafe == null)
    {
      return wasSet;
    }

    ClimbSafe existingClimbSafe = climbSafe;
    climbSafe = aClimbSafe;
    if (existingClimbSafe != null && !existingClimbSafe.equals(aClimbSafe))
    {
      existingClimbSafe.removeAssignment(this);
    }
    climbSafe.addAssignment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Member existingMember = member;
    member = null;
    if (existingMember != null)
    {
      existingMember.setAssignment(null);
    }
    if (guide != null)
    {
      Guide placeholderGuide = guide;
      this.guide = null;
      placeholderGuide.removeAssignment(this);
    }
    if (hotel != null)
    {
      Hotel placeholderHotel = hotel;
      this.hotel = null;
      placeholderHotel.removeAssignment(this);
    }
    ClimbSafe placeholderClimbSafe = climbSafe;
    this.climbSafe = null;
    if(placeholderClimbSafe != null)
    {
      placeholderClimbSafe.removeAssignment(this);
    }
  }

  // line 51 "../../../../../ClimbSafeStates.ump"
   private void refund(Integer percentage){
    refundAmount = percentage;
  }

  // line 55 "../../../../../ClimbSafeStates.ump"
   public Integer getRefund(){
    return refundAmount;
  }

  // line 59 "../../../../../ClimbSafeStates.ump"
   private Boolean isValid(String code){
    return code!=null && !code.trim().equals("");
  }

  // line 63 "../../../../../ClimbSafeStates.ump"
   private void setCode(String code){
    paymentCode = code;
  }


  public String toString()
  {
    return super.toString() + "["+
            "startWeek" + ":" + getStartWeek()+ "," +
            "endWeek" + ":" + getEndWeek()+ "," +
            "paymentCode" + ":" + getPaymentCode()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "member = "+(getMember()!=null?Integer.toHexString(System.identityHashCode(getMember())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "guide = "+(getGuide()!=null?Integer.toHexString(System.identityHashCode(getGuide())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "hotel = "+(getHotel()!=null?Integer.toHexString(System.identityHashCode(getHotel())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "climbSafe = "+(getClimbSafe()!=null?Integer.toHexString(System.identityHashCode(getClimbSafe())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 132 "../../../../../ClimbSafePersistence.ump"
  private static final long serialVersionUID = 11L ;

  
}