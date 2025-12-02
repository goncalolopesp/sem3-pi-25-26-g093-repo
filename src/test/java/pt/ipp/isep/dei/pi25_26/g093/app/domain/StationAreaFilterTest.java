package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for StationAreaFilter (JUnit 4).
 *
 * Tests all filter combinations:
 * - isCity filter (true, false, null)
 * - isMainStation filter (true, false, null)
 * - country filter ("PT", "ES", "ALL", null)
 * - timeZone filter (case-insensitive)
 * - timeZoneGroup filter (case-insensitive)
 * - Multiple filters combined (AND logic)
 */
public class StationAreaFilterTest {

    // ==================== HELPER METHODS ====================

    private CompleteStation createStation(String country, String timeZone, String timeZoneGroup,
                                          String name, boolean isCity, boolean isMainStation) {
        return new CompleteStation(country, timeZone, timeZoneGroup, name, 40.0, -8.0, isCity, isMainStation, false);
    }

    // ==================== ISCITY FILTER TESTS ====================

    @Test
    public void testFilter_IsCityTrue_MatchesOnlyCityStations() {
        CompleteStation city = createStation("PT", "Europe/Lisbon", "WET", "City", true, false);
        CompleteStation nonCity = createStation("PT", "Europe/Lisbon", "WET", "NonCity", false, false);

        StationAreaFilter filter = new StationAreaFilter(true, null, null, null, null);

        assertTrue(filter.matches(city));
        assertFalse(filter.matches(nonCity));
    }

    @Test
    public void testFilter_IsCityFalse_MatchesOnlyNonCityStations() {
        CompleteStation city = createStation("PT", "Europe/Lisbon", "WET", "City", true, false);
        CompleteStation nonCity = createStation("PT", "Europe/Lisbon", "WET", "NonCity", false, false);

        StationAreaFilter filter = new StationAreaFilter(false, null, null, null, null);

        assertFalse(filter.matches(city));
        assertTrue(filter.matches(nonCity));
    }

    @Test
    public void testFilter_IsCityNull_MatchesAll() {
        CompleteStation city = createStation("PT", "Europe/Lisbon", "WET", "City", true, false);
        CompleteStation nonCity = createStation("PT", "Europe/Lisbon", "WET", "NonCity", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        assertTrue(filter.matches(city));
        assertTrue(filter.matches(nonCity));
    }

    // ==================== ISMAINSTATION FILTER TESTS ====================

    @Test
    public void testFilter_IsMainStationTrue_MatchesOnlyMainStations() {
        CompleteStation main = createStation("PT", "Europe/Lisbon", "WET", "Main", false, true);
        CompleteStation nonMain = createStation("PT", "Europe/Lisbon", "WET", "NonMain", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, true, null, null, null);

        assertTrue(filter.matches(main));
        assertFalse(filter.matches(nonMain));
    }

    @Test
    public void testFilter_IsMainStationFalse_MatchesOnlyNonMainStations() {
        CompleteStation main = createStation("PT", "Europe/Lisbon", "WET", "Main", false, true);
        CompleteStation nonMain = createStation("PT", "Europe/Lisbon", "WET", "NonMain", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, false, null, null, null);

        assertFalse(filter.matches(main));
        assertTrue(filter.matches(nonMain));
    }

    @Test
    public void testFilter_IsMainStationNull_MatchesAll() {
        CompleteStation main = createStation("PT", "Europe/Lisbon", "WET", "Main", false, true);
        CompleteStation nonMain = createStation("PT", "Europe/Lisbon", "WET", "NonMain", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        assertTrue(filter.matches(main));
        assertTrue(filter.matches(nonMain));
    }

    // ==================== COUNTRY FILTER TESTS ====================

    @Test
    public void testFilter_CountryPT_MatchesOnlyPortugueseStations() {
        CompleteStation pt = createStation("PT", "Europe/Lisbon", "WET", "PT", false, false);
        CompleteStation es = createStation("ES", "Europe/Madrid", "CET", "ES", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, "PT", null, null);

        assertTrue(filter.matches(pt));
        assertFalse(filter.matches(es));
    }

    @Test
    public void testFilter_CountryES_MatchesOnlySpanishStations() {
        CompleteStation pt = createStation("PT", "Europe/Lisbon", "WET", "PT", false, false);
        CompleteStation es = createStation("ES", "Europe/Madrid", "CET", "ES", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, "ES", null, null);

        assertFalse(filter.matches(pt));
        assertTrue(filter.matches(es));
    }

    @Test
    public void testFilter_CountryALL_MatchesAll() {
        CompleteStation pt = createStation("PT", "Europe/Lisbon", "WET", "PT", false, false);
        CompleteStation es = createStation("ES", "Europe/Madrid", "CET", "ES", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL", null, null);

        assertTrue(filter.matches(pt));
        assertTrue(filter.matches(es));
    }

    @Test
    public void testFilter_CountryNull_MatchesAll() {
        CompleteStation pt = createStation("PT", "Europe/Lisbon", "WET", "PT", false, false);
        CompleteStation es = createStation("ES", "Europe/Madrid", "CET", "ES", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        assertTrue(filter.matches(pt));
        assertTrue(filter.matches(es));
    }

    @Test
    public void testFilter_CountryCaseInsensitive() {
        CompleteStation pt = createStation("PT", "Europe/Lisbon", "WET", "PT", false, false);

        StationAreaFilter filterLower = new StationAreaFilter(null, null, "pt", null, null);
        StationAreaFilter filterUpper = new StationAreaFilter(null, null, "PT", null, null);

        assertTrue(filterLower.matches(pt));
        assertTrue(filterUpper.matches(pt));
    }

    // ==================== TIMEZONE FILTER TESTS ====================

    @Test
    public void testFilter_TimeZone_MatchesExactTimeZone() {
        CompleteStation lisbon = createStation("PT", "Europe/Lisbon", "WET", "Lisbon", false, false);
        CompleteStation madrid = createStation("ES", "Europe/Madrid", "CET", "Madrid", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, "Europe/Lisbon", null);

        assertTrue(filter.matches(lisbon));
        assertFalse(filter.matches(madrid));
    }

    @Test
    public void testFilter_TimeZoneCaseInsensitive() {
        CompleteStation station = createStation("PT", "Europe/Lisbon", "WET", "Station", false, false);

        StationAreaFilter filterLower = new StationAreaFilter(null, null, null, "europe/lisbon", null);
        StationAreaFilter filterMixed = new StationAreaFilter(null, null, null, "Europe/Lisbon", null);
        StationAreaFilter filterUpper = new StationAreaFilter(null, null, null, "EUROPE/LISBON", null);

        assertTrue(filterLower.matches(station));
        assertTrue(filterMixed.matches(station));
        assertTrue(filterUpper.matches(station));
    }

    @Test
    public void testFilter_TimeZoneNull_MatchesAll() {
        CompleteStation lisbon = createStation("PT", "Europe/Lisbon", "WET", "Lisbon", false, false);
        CompleteStation madrid = createStation("ES", "Europe/Madrid", "CET", "Madrid", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        assertTrue(filter.matches(lisbon));
        assertTrue(filter.matches(madrid));
    }

    @Test
    public void testFilter_TimeZoneNullStation_DoesNotMatch() {
        CompleteStation nullTZ = new CompleteStation("PT", null, "WET", "NullTZ", 40.0, -8.0, false, false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, "Europe/Lisbon", null);

        assertFalse(filter.matches(nullTZ));
    }

    // ==================== TIMEZONEGROUP FILTER TESTS ====================

    @Test
    public void testFilter_TimeZoneGroup_MatchesExactTimeZoneGroup() {
        CompleteStation wet = createStation("PT", "Europe/Lisbon", "WET", "WET", false, false);
        CompleteStation cet = createStation("ES", "Europe/Madrid", "CET", "CET", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, "WET");

        assertTrue(filter.matches(wet));
        assertFalse(filter.matches(cet));
    }

    @Test
    public void testFilter_TimeZoneGroupCaseInsensitive() {
        CompleteStation station = createStation("PT", "Europe/Lisbon", "WET", "Station", false, false);

        StationAreaFilter filterLower = new StationAreaFilter(null, null, null, null, "wet");
        StationAreaFilter filterMixed = new StationAreaFilter(null, null, null, null, "WET");
        StationAreaFilter filterUpper = new StationAreaFilter(null, null, null, null, "WET");

        assertTrue(filterLower.matches(station));
        assertTrue(filterMixed.matches(station));
        assertTrue(filterUpper.matches(station));
    }

    @Test
    public void testFilter_TimeZoneGroupNull_MatchesAll() {
        CompleteStation wet = createStation("PT", "Europe/Lisbon", "WET", "WET", false, false);
        CompleteStation cet = createStation("ES", "Europe/Madrid", "CET", "CET", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        assertTrue(filter.matches(wet));
        assertTrue(filter.matches(cet));
    }

    @Test
    public void testFilter_TimeZoneGroupNullStation_DoesNotMatch() {
        CompleteStation nullTZG = new CompleteStation("PT", "Europe/Lisbon", null, "NullTZG", 40.0, -8.0, false, false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, "WET");

        assertFalse(filter.matches(nullTZG));
    }

    // ==================== COMBINED FILTER TESTS ====================

    @Test
    public void testFilter_AllFilters_AllMustMatch() {
        CompleteStation match = createStation("PT", "Europe/Lisbon", "WET", "Match", true, true);
        CompleteStation wrongCountry = createStation("ES", "Europe/Lisbon", "WET", "Wrong", true, true);
        CompleteStation wrongCity = createStation("PT", "Europe/Lisbon", "WET", "Wrong", false, true);
        CompleteStation wrongMain = createStation("PT", "Europe/Lisbon", "WET", "Wrong", true, false);
        CompleteStation wrongTZ = createStation("PT", "Europe/Madrid", "WET", "Wrong", true, true);
        CompleteStation wrongTZG = createStation("PT", "Europe/Lisbon", "CET", "Wrong", true, true);

        StationAreaFilter filter = new StationAreaFilter(true, true, "PT", "Europe/Lisbon", "WET");

        assertTrue(filter.matches(match));
        assertFalse(filter.matches(wrongCountry));
        assertFalse(filter.matches(wrongCity));
        assertFalse(filter.matches(wrongMain));
        assertFalse(filter.matches(wrongTZ));
        assertFalse(filter.matches(wrongTZG));
    }

    @Test
    public void testFilter_NoFilters_MatchesAll() {
        CompleteStation station1 = createStation("PT", "Europe/Lisbon", "WET", "S1", true, true);
        CompleteStation station2 = createStation("ES", "Europe/Madrid", "CET", "S2", false, false);

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        assertTrue(filter.matches(station1));
        assertTrue(filter.matches(station2));
    }

    // ==================== APPLY METHOD TESTS ====================

    @Test
    public void testApply_FiltersList_ReturnsFilteredList() {
        List<CompleteStation> stations = new ArrayList<>();
        stations.add(createStation("PT", "Europe/Lisbon", "WET", "PT1", true, false));
        stations.add(createStation("PT", "Europe/Lisbon", "WET", "PT2", true, false));
        stations.add(createStation("ES", "Europe/Madrid", "CET", "ES1", true, false));
        stations.add(createStation("PT", "Europe/Lisbon", "WET", "PT3", false, false));

        StationAreaFilter filter = new StationAreaFilter(true, null, "PT", null, null);

        List<CompleteStation> result = filter.apply(stations);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.isCity() && "PT".equals(s.getCountry())));
    }

    @Test
    public void testApply_EmptyList_ReturnsEmptyList() {
        List<CompleteStation> stations = new ArrayList<>();
        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        List<CompleteStation> result = filter.apply(stations);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testApply_NullList_ReturnsEmptyList() {
        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        List<CompleteStation> result = filter.apply(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testApply_NullStationsInList_Ignored() {
        List<CompleteStation> stations = new ArrayList<>();
        stations.add(createStation("PT", "Europe/Lisbon", "WET", "S1", false, false));
        stations.add(null);
        stations.add(createStation("PT", "Europe/Lisbon", "WET", "S2", false, false));

        StationAreaFilter filter = new StationAreaFilter(null, null, null, null, null);

        List<CompleteStation> result = filter.apply(stations);

        assertEquals(2, result.size());
        assertFalse(result.contains(null));
    }

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    public void testConstructor_OldConstructor_StillWorks() {
        StationAreaFilter filter = new StationAreaFilter(true, false, "PT");

        assertTrue(filter.getIsCity());
        assertFalse(filter.getIsMainStation());
        assertEquals("PT", filter.getCountry());
        assertNull(filter.getTimeZone());
        assertNull(filter.getTimeZoneGroup());
    }

    @Test
    public void testConstructor_NewConstructor_WithTimeZones() {
        StationAreaFilter filter = new StationAreaFilter(true, false, "PT", "Europe/Lisbon", "WET");

        assertTrue(filter.getIsCity());
        assertFalse(filter.getIsMainStation());
        assertEquals("PT", filter.getCountry());
        assertEquals("Europe/Lisbon", filter.getTimeZone());
        assertEquals("WET", filter.getTimeZoneGroup());
    }
}
