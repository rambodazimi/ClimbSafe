/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.climbsafe.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// line 2 "../../../../../ClimbSafeStates.ump"
// line 62 "../../../../../ClimbSafePersistence.ump"
// line 43 "../../../../../ClimbSafe.ump"
public class Guide extends NamedUser implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Guide Attributes
  private int remainingWeeks;

  //Guide State Machines
  public enum AvailabilityStatus { Available, Full }
  private AvailabilityStatus availabilityStatus;

  //Guide Associations
  private ClimbSafe climbSafe;
  private List<Assignment> assignments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Guide(String aEmail, String aPassword, String aName, String aEmergencyContact, ClimbSafe aClimbSafe)
  {
    super(aEmail, aPassword, aName, aEmergencyContact);
    remainingWeeks = aClimbSafe.getNrWeeks();
    boolean didAddClimbSafe = setClimbSafe(aClimbSafe);
    if (!didAddClimbSafe)
    {
      throw new RuntimeException("Unable to create guide due to climbSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    assignments = new ArrayList<Assignment>();
    setAvailabilityStatus(AvailabilityStatus.Available);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRemainingWeeks(int aRemainingWeeks)
  {
    boolean wasSet = false;
    remainingWeeks = aRemainingWeeks;
    wasSet = true;
    return wasSet;
  }

  public int getRemainingWeeks()
  {
    return remainingWeeks;
  }

  public String getAvailabilityStatusFullName()
  {
    String answer = availabilityStatus.toString();
    return answer;
  }

  public AvailabilityStatus getAvailabilityStatus()
  {
    return availabilityStatus;
  }

  public boolean assign(Assignment assignment,Integer demandWeeks)
  {
    boolean wasEventProcessed = false;
    
    AvailabilityStatus aAvailabilityStatus = availabilityStatus;
    switch (aAvailabilityStatus)
    {
      case Available:
        if (remainingWeeks>demandWeeks)
        {
        // line 5 "../../../../../ClimbSafeStates.ump"
          doAssign( assignment,  demandWeeks);
          setAvailabilityStatus(AvailabilityStatus.Available);
          wasEventProcessed = true;
          break;
        }
        if (demandWeeks.equals(remainingWeeks))
        {
        // line 8 "../../../../../ClimbSafeStates.ump"
          doAssign( assignment,  demandWeeks);
          setAvailabilityStatus(AvailabilityStatus.Full);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setAvailabilityStatus(AvailabilityStatus aAvailabilityStatus)
  {
    availabilityStatus = aAvailabilityStatus;
  }
  /* Code from template association_GetOne */
  public ClimbSafe getClimbSafe()
  {
    return climbSafe;
  }
  /* Code from template association_GetMany */
  public Assignment getAssignment(int index)
  {
    Assignment aAssignment = assignments.get(index);
    return aAssignment;
  }

  public List<Assignment> getAssignments()
  {
    List<Assignment> newAssignments = Collections.unmodifiableList(assignments);
    return newAssignments;
  }

  public int numberOfAssignments()
  {
    int number = assignments.size();
    return number;
  }

  public boolean hasAssignments()
  {
    boolean has = assignments.size() > 0;
    return has;
  }

  public int indexOfAssignment(Assignment aAssignment)
  {
    int index = assignments.indexOf(aAssignment);
    return index;
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
      existingClimbSafe.removeGuide(this);
    }
    climbSafe.addGuide(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAssignments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addAssignment(Assignment aAssignment)
  {
    boolean wasAdded = false;
    if (assignments.contains(aAssignment)) { return false; }
    Guide existingGuide = aAssignment.getGuide();
    if (existingGuide == null)
    {
      aAssignment.setGuide(this);
    }
    else if (!this.equals(existingGuide))
    {
      existingGuide.removeAssignment(aAssignment);
      addAssignment(aAssignment);
    }
    else
    {
      assignments.add(aAssignment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAssignment(Assignment aAssignment)
  {
    boolean wasRemoved = false;
    if (assignments.contains(aAssignment))
    {
      assignments.remove(aAssignment);
      aAssignment.setGuide(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAssignmentAt(Assignment aAssignment, int index)
  {  
    boolean wasAdded = false;
    if(addAssignment(aAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignments()) { index = numberOfAssignments() - 1; }
      assignments.remove(aAssignment);
      assignments.add(index, aAssignment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAssignmentAt(Assignment aAssignment, int index)
  {
    boolean wasAdded = false;
    if(assignments.contains(aAssignment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAssignments()) { index = numberOfAssignments() - 1; }
      assignments.remove(aAssignment);
      assignments.add(index, aAssignment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAssignmentAt(aAssignment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ClimbSafe placeholderClimbSafe = climbSafe;
    this.climbSafe = null;
    if(placeholderClimbSafe != null)
    {
      placeholderClimbSafe.removeGuide(this);
    }
    while( !assignments.isEmpty() )
    {
      assignments.get(0).setGuide(null);
    }
    super.delete();
  }

  // line 17 "../../../../../ClimbSafeStates.ump"
   private void doAssign(Assignment assignment, Integer demandWeeks){
    assignment.setGuide(this);
    	this.setRemainingWeeks(getRemainingWeeks()-demandWeeks);
  }


  public String toString()
  {
    return super.toString() + "["+
            "remainingWeeks" + ":" + getRemainingWeeks()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "climbSafe = "+(getClimbSafe()!=null?Integer.toHexString(System.identityHashCode(getClimbSafe())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 65 "../../../../../ClimbSafePersistence.ump"
  private static final long serialVersionUID = 5L ;

  
}