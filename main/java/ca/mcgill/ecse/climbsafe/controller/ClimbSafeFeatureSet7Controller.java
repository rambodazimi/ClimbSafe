package ca.mcgill.ecse.climbsafe.controller;

import ca.mcgill.ecse.climbsafe.application.ClimbSafeApplication;
import ca.mcgill.ecse.climbsafe.model.ClimbSafe;
import ca.mcgill.ecse.climbsafe.model.Hotel;
import ca.mcgill.ecse.climbsafe.model.Hotel.HotelRating;
import ca.mcgill.ecse.climbsafe.persistence.ClimbSafePersistence;

public class ClimbSafeFeatureSet7Controller {

	/**
	 * @author AbdelrahmanAli
	 * @param name
	 * @param address
	 * @param nrStars
	 * @throws InvalidInputException
	 */
	public static void addHotel(String name, String address, int nrStars) throws InvalidInputException {
		ClimbSafe climbSafeApplication = ClimbSafeApplication.getClimbSafe();

		if (name == null || name.equals(""))
			throw new InvalidInputException("Name cannot be empty");
		if (Hotel.getWithName(name) != null)
			throw new InvalidInputException("Hotel already exists in the system");
		if (address == null || address.equals(""))
			throw new InvalidInputException("Address cannot be empty");

		if (nrStars > 5 || nrStars < 1)
			throw new InvalidInputException("Number of stars must be between 1 and 5");

		HotelRating rate = null;
		switch (nrStars) {
		case 1:
			rate = HotelRating.OneStar;
			break;
		case 2:
			rate = HotelRating.TwoStars;
			break;
		case 3:
			rate = HotelRating.ThreeStars;
			break;
		case 4:
			rate = HotelRating.FourStars;
			break;
		case 5:
			rate = HotelRating.FiveStars;
			break;
		}

		climbSafeApplication.addHotel(name, address, rate);

		try {
			ClimbSafePersistence.save(); // Update the persistence layer
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * @author AbdelrahmanAli
	 * @param oldName
	 * @param newName
	 * @param newAddress
	 * @param newNrStars
	 * @throws InvalidInputException
	 */
	public static void updateHotel(String oldName, String newName, String newAddress, int newNrStars)
			throws InvalidInputException {

		if (oldName == null || oldName.equals("") || newName == null || newName.equals(""))
			throw new InvalidInputException("Name cannot be empty");

		if (Hotel.getWithName(oldName) == null )
			throw new InvalidInputException("Hotel does not exist in the system");

		if (Hotel.getWithName(newName) != null && !oldName.equals(newName))
			throw new InvalidInputException("New name already linked to another hotel");

		if (newAddress == null || newAddress.equals(""))
			throw new InvalidInputException("Address cannot be empty");

		if (newNrStars > 5 || newNrStars < 1)
			throw new InvalidInputException("Number of stars must be between 1 and 5");

		HotelRating rate = null;
		switch (newNrStars) {
		case 1:
			rate = HotelRating.OneStar;
			break;
		case 2:
			rate = HotelRating.TwoStars;
			break;
		case 3:
			rate = HotelRating.ThreeStars;
			break;
		case 4:
			rate = HotelRating.FourStars;
			break;
		case 5:
			rate = HotelRating.FiveStars;
		}

		Hotel.getWithName(oldName).setAddress(newAddress);
		Hotel.getWithName(oldName).setRating(rate);
		Hotel.getWithName(oldName).setName(newName);
		try {
			ClimbSafePersistence.save(); // Update the persistence layer
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

}
