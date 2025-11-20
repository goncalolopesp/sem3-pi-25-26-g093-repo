package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Represents a date in the format YYYY-MM-DD.
 */
public class Date {
    int year;
    int month;
    int day;

    /**
     * Constructor that parses a date string in the format YYYY-MM-DD.
     * @param date the date string to parse
     */
    public Date(String date){
        if (date == null || date.trim().isEmpty()){
            date = "9999-12-31";
        }
        if (date.contains("T")) {
            date = date.split("T")[0];
        }
        String[] parts = date.split("-");
        this.year = Integer.parseInt(parts[0]);
        this.month = Integer.parseInt(parts[1]);
        this.day = Integer.parseInt(parts[2]);
    }

    /**
     * Gets the year component of the date.
     * @return the year
     */
    public int getYear(){
        return year;
    }

    /**
     * Gets the month component of the date.
     * @return the month
     */
    public int getMonth(){
        return month;
    }

    /**
     * Gets the day component of the date.
     * @return the day
     */
    public int getDay(){
        return day;
    }

    /**
     * Gets the String representation of the date
     * @return the date in the format YYYY-MM-DD
     */
    @Override
    public String toString(){
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Compares this date to another date.
     * @param other the other date to compare to
     * @return a negative integer, zero, or a positive integer as this date is less than, equal to, or greater than the specified date
     */
    public int compareTo(Date other) {
        if (this.year != other.getYear())
            return Integer.compare(this.year, other.getYear());
        if (this.month != other.getMonth())
            return Integer.compare(this.month, other.getMonth());
        return Integer.compare(this.day, other.getDay());
    }
}
