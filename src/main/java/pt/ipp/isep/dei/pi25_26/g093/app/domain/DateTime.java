package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class DateTime {
    Date date;
    Time time;

    /**
     * Creates a new DateTime with the given parameters.
     * @param completeDate the date and time in the format YYYY-MM-DDTHH:MM:SS
     */
    public DateTime(String completeDate){
        String[] parameters = completeDate.split("T");
        this.date = new Date(parameters[0]);
        this.time = new Time(parameters[1]);
    }

    /**
     * Gets the date (without the time)
     * @return the date
     */
    public Date getDate(){
        return date;
    }

    /**
     * Gets the time (without the date)
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    /**
     * Gets a String representation of the DateTime.
     * @return a String representation of the DateTime in the format DD-MM-YYYYTHH:MM:SS.
     */
    public String toString(){
        return date + "T" + time;
    }

    /**
     * Compares this DateTime to another DateTime.
     * @param other the DateTime to be compared with.
     * @return 1 if this DateTime is later, -1 if the DateTime is earlier, 0 if the DateTimes are equal.
     */
    public int compareTo(DateTime other) {
        int dateCompare = this.date.compareTo(other.getDate());
        if (dateCompare != 0) {
            return dateCompare;
        }

        int timeCompare = this.time.compareTo(other.getTime());
        return timeCompare;
    }
}
