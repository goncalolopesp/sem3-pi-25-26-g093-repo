package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * DateTime class that encapsulates Date and Time.
 */
public class DateTime {
    Date date;
    Time time;

    /**
     * Constructor that takes a complete date-time string in the format "YYYY-MM-DDTHH:MM:SS".
     * @param completeDate the complete date-time string
     */
    public DateTime(String completeDate){
        String[] parts = completeDate.split("T");
        this.date = new Date(parts[0]);
        this.time = new Time(parts[1]);
    }

    /**
     * Gets the date part of the DateTime.
     * @return the date
     */
    public Date getDate(){
        return date;
    }

    /**
     * Gets the time part of the DateTime.
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    /**
     * String representation of the DateTime in the format "YYYY-MM-DDTHH:MM:SS".
     * @return the string representation
     */
    @Override
    public String toString(){
        return date.toString() + "T" + time.toString();
    }

    /**
     * Compares this DateTime with another DateTime.
     * @param other the other DateTime to compare with
     * @return a negative integer, zero, or a positive integer as this DateTime is less than, equal to, or greater than the specified DateTime
     */
    public int compareTo(DateTime other) {
        int cmp = this.date.compareTo(other.date);
        return (cmp != 0) ? cmp : this.time.compareTo(other.time);
    }
}
