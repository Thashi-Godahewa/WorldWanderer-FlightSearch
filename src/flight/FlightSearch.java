package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * FlightSearch class validates flight search criteria according to business rules.
 * If all validations pass, the search details are stored and the method returns true.
 * Otherwise, it returns false without modifying any existing attributes.
 */
public class FlightSearch {

    // ----------------------------
    // Instance variables
    // ----------------------------
    private String departureDate;
    private String departureAirportCode;
    private boolean emergencyRowSeating;
    private String returnDate;
    private String destinationAirportCode;
    private String seatingClass;
    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;

    // ----------------------------
    // Constants
    // ----------------------------
    private static final Set<String> VALID_AIRPORTS = new HashSet<>(Arrays.asList(
            "syd", "mel", "lax", "cdg", "del", "pvg", "doh"
    ));

    private static final Set<String> VALID_CLASSES = new HashSet<>(Arrays.asList(
            "economy", "premium economy", "business", "first"
    ));

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT); // ensures strict date validation

    // ----------------------------
    // Public method to validate and set details
    // ----------------------------
    public boolean runFlightSearch(String departureDate, String departureAirportCode, boolean emergencyRowSeating,
                                   String returnDate, String destinationAirportCode, String seatingClass,
                                   int adultPassengerCount, int childPassengerCount, int infantPassengerCount) {

        // Condition 1: Total passengers between 1 and 9
        int totalPassengers = adultPassengerCount + childPassengerCount + infantPassengerCount;
        if (totalPassengers < 1 || totalPassengers > 9) return false;

        // Condition 9: Seating class must be valid
        if (!VALID_CLASSES.contains(seatingClass)) return false;

        // Condition 10: Only economy class can have emergency row seating
        if (emergencyRowSeating && !seatingClass.equals("economy")) return false;

        // Condition 11: Valid airports and must not be the same
        if (!VALID_AIRPORTS.contains(departureAirportCode) || !VALID_AIRPORTS.contains(destinationAirportCode))
            return false;
        if (departureAirportCode.equals(destinationAirportCode)) return false;

        // Condition 2: Children cannot sit in emergency row or first class
        if (childPassengerCount > 0 &&
                (emergencyRowSeating || seatingClass.equals("first"))) return false;

        // Condition 3: Infants cannot sit in emergency row or business class
        if (infantPassengerCount > 0 &&
                (emergencyRowSeating || seatingClass.equals("business"))) return false;

        // Condition 4: Max 2 children per adult
        if (adultPassengerCount == 0 && childPassengerCount > 0) return false; // no adults but children exist
        if (childPassengerCount > adultPassengerCount * 2) return false;

        // Condition 5: Max 1 infant per adult
        if (infantPassengerCount > adultPassengerCount) return false;

        // Condition 7: Validate date format and correctness
        LocalDate depDate = parseDateStrict(departureDate);
        LocalDate retDate = parseDateStrict(returnDate);
        if (depDate == null || retDate == null) return false;

        // Condition 6: Departure date cannot be in the past
        if (depDate.isBefore(LocalDate.now())) return false;

        // Condition 8: Return date cannot be before departure date
        if (retDate.isBefore(depDate)) return false;

        // All validations passed → set class attributes
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.emergencyRowSeating = emergencyRowSeating;
        this.returnDate = returnDate;
        this.destinationAirportCode = destinationAirportCode;
        this.seatingClass = seatingClass;
        this.adultPassengerCount = adultPassengerCount;
        this.childPassengerCount = childPassengerCount;
        this.infantPassengerCount = infantPassengerCount;

        return true; // valid search
    }

    // ----------------------------
    // Helper method: Strict date validation
    // ----------------------------
    private LocalDate parseDateStrict(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null; // invalid date format or invalid day/month combo
        }
    }

    // ----------------------------
    // Getters (for testing or inspection)
    // ----------------------------
    public String getDepartureDate() { return departureDate; }
    public String getReturnDate() { return returnDate; }
    public String getDepartureAirportCode() { return departureAirportCode; }
    public String getDestinationAirportCode() { return destinationAirportCode; }
    public boolean isEmergencyRowSeating() { return emergencyRowSeating; }
    public String getSeatingClass() { return seatingClass; }
    public int getAdultPassengerCount() { return adultPassengerCount; }
    public int getChildPassengerCount() { return childPassengerCount; }
    public int getInfantPassengerCount() { return infantPassengerCount; }

}

