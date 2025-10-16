package test;

import flight.FlightSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test class for FlightSearch.
 * Covers all 12 validation rules with boundary and valid examples.
 */
public class FlightSearchTest {

    private FlightSearch search;

    @BeforeEach
    void setUp() {
        search = new FlightSearch();
    }

    // Method to print test result
    private void printResult(String message, boolean result, boolean expected) {
        if (result == expected) {
            System.out.println(message + " → Passed");
        } else {
            System.out.println(message + " → Failed (expected " + expected + ", got " + result + ")");
        }
    }

    // ---------- TC 1: Total passengers -----------
    @Test
    void testTotalPassengersLessThan1() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 0, 0, 0);
        printResult("TC1-1: Total passengers <1", result, false);
        assertFalse(result);
    }

    @Test
    void testTotalPassengersGreaterThan9() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 10, 0, 0);
        printResult("TC1-2: Total passengers >9", result, false);
        assertFalse(result);
    }

    // ---------- TC 2: Children in restricted seats -----------
    @Test
    void testChildrenInEmergencyRow() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", true,
                "25/12/2025", "mel", "economy", 1, 1, 0);
        printResult("TC2-1: Children in emergency row", result, false);
        assertFalse(result);
    }

    @Test
    void testChildrenInFirstClass() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "first", 1, 1, 0);
        printResult("TC2-2: Children in first class", result, false);
        assertFalse(result);
    }

    // ---------- TC 3: Infants in restricted seats -----------
    @Test
    void testInfantsInEmergencyRow() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", true,
                "25/12/2025", "mel", "economy", 1, 0, 1);
        printResult("TC3-1: Infant in emergency row", result, false);
        assertFalse(result);
    }

    @Test
    void testInfantsInBusinessClass() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "business", 1, 0, 1);
        printResult("TC3-2: Infant in business class", result, false);
        assertFalse(result);
    }

    // ---------- TC 4: Child/adult ratio -----------
    @Test
    void testTooManyChildrenPerAdult() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 1, 3, 0);
        printResult("TC4-1: Too many children per adult", result, false);
        assertFalse(result);
    }

    @Test
    void testChildrenWithoutAdults() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 0, 2, 0);
        printResult("TC4-2: Children without adults", result, false);
        assertFalse(result);
    }

    // ---------- TC 5: Infant/adult ratio -----------
    @Test
    void testTooManyInfantsPerAdult() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 1, 0, 2);
        printResult("TC5-1: Too many infants per adult", result, false);
        assertFalse(result);
    }

    @Test
    void testInfantPerAdultAllowed() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 2, 0, 2);
        printResult("TC5-2: Infant per adult allowed", result, true);
        assertTrue(result);
    }

    // ---------- TC 6: Departure in past -----------
    @Test
    void testDepartureDateInPast1() {
        boolean result = search.runFlightSearch("01/01/2020", "syd", false,
                "25/12/2025", "mel", "economy", 1, 0, 0);
        printResult("TC6-1: Departure date in past", result, false);
        assertFalse(result);
    }

    @Test
    void testDepartureDateInPast2() {
        boolean result = search.runFlightSearch("15/08/2023", "mel", false,
                "20/08/2023", "syd", "economy", 2, 0, 0);
        printResult("TC6-2: Departure date in past", result, false);
        assertFalse(result);
    }

    // ---------- TC 7: Invalid date format -----------
    @Test
    void testInvalidDateFormat1() {
        boolean result = search.runFlightSearch("32/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 1, 0, 0);
        printResult("TC7-1: Invalid date format", result, false);
        assertFalse(result);
    }

    @Test
    void testInvalidDateFormat2() {
        boolean result = search.runFlightSearch("29/02/2025", "mel", false,
                "05/03/2025", "syd", "economy", 1, 0, 0); // 2025 is not leap year
        printResult("TC7-2: Invalid date format (non-leap year Feb 29)", result, false);
        assertFalse(result);
    }

    // ---------- TC 8: Return before departure -----------
    @Test
    void testReturnBeforeDeparture1() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "10/12/2025", "mel", "economy", 1, 0, 0);
        printResult("TC8-1: Return before departure", result, false);
        assertFalse(result);
    }

    @Test
    void testReturnBeforeDeparture2() {
        boolean result = search.runFlightSearch("15/11/2025", "mel", false,
                "14/11/2025", "syd", "economy", 2, 0, 0);
        printResult("TC8-2: Return before departure", result, false);
        assertFalse(result);
    }

    // ---------- TC 9: Invalid seating class -----------
    @Test
    void testInvalidSeatingClass1() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "luxury", 1, 0, 0);
        printResult("TC9-1: Invalid seating class", result, false);
        assertFalse(result);
    }

    @Test
    void testInvalidSeatingClass2() {
        boolean result = search.runFlightSearch("15/12/2025", "mel", false,
                "20/12/2025", "syd", "ultra", 1, 0, 0);
        printResult("TC9-2: Invalid seating class", result, false);
        assertFalse(result);
    }

    // ---------- TC 10: Emergency row but not economy -----------
    @Test
    void testEmergencyRowNonEconomy1() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", true,
                "25/12/2025", "mel", "business", 1, 0, 0);
        printResult("TC10-1: Emergency row non-economy", result, false);
        assertFalse(result);
    }

    @Test
    void testEmergencyRowNonEconomy2() {
        boolean result = search.runFlightSearch("15/12/2025", "mel", true,
                "20/12/2025", "syd", "first", 2, 0, 0);
        printResult("TC10-2: Emergency row non-economy", result, false);
        assertFalse(result);
    }


    // ---------- TC 11: Invalid or same airport -----------
    @Test
    void testInvalidAirportCode() {
        boolean result = search.runFlightSearch("20/12/2025", "xyz", false,
                "25/12/2025", "mel", "economy", 1, 0, 0);
        printResult("TC11-1: Invalid Airport Code", result, false);
        assertFalse(result);
    }

    @Test
    void testSameAirportCode() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "syd", "economy", 1, 0, 0);
        printResult("TC11-2: Same Airport Code", result, false);
        assertFalse(result);
    }

    // ---------- TC 12: All valid combinations -----------
    @Test
    void testAllInputsValidEconomy() {
        boolean result = search.runFlightSearch("20/12/2025", "syd", false,
                "25/12/2025", "mel", "economy", 1, 0, 0);
        printResult("TC12-1: All Inputs Valid Economy", result, true);
        assertTrue(result);
    }

    @Test
    void testAllInputsValidEmergencyEconomy() {
        boolean result = search.runFlightSearch("20/12/2025", "mel", true,
                "25/12/2025", "pvg", "economy", 2, 0, 0);
        printResult("TC12-2: All Inputs Valid Emergency Economy", result, true);
        assertTrue(result);
    }

    @Test
    void testAllInputsValidBusiness() {
        boolean result = search.runFlightSearch("15/11/2025", "del", false,
                "25/11/2025", "cdg", "business", 1, 0, 0);
        printResult("TC12-3: All Inputs Valid Business", result, true);
        assertTrue(result);
    }

    @Test
    void testAllInputsValidFirstClass() {
        boolean result = search.runFlightSearch("10/12/2025", "lax", false,
                "20/12/2025", "doh", "first", 2, 0, 0);
        printResult("TC12-4: All Inputs Valid First Class", result, true);
        assertTrue(result);
    }
}
