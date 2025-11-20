package pt.ipp.isep.dei.pi25_26.g093.app.domain;

/**
 * Represents a specific time of day in hours, minutes, and seconds.
 */
public class Time {
    int hour;
    int minute;
    int second;

    /**
     * Creates a Time object from a string in the format "HH:MM:SS".
     * @param time the time string to parse.
     */
    public Time(String time){
        String[] parts = time.split(":");
        this.hour = Integer.parseInt(parts[0]);
        this.minute = Integer.parseInt(parts[1]);
        this.second = Integer.parseInt(parts[2]);
    }

    /**
     * Gets the hour component of the time.
     * @return the hour.
     */
    public int getHour(){
        return hour;
    }

    /**
     * Gets the minute component of the time.
     * @return the minute.
     */
    public int getMinute(){
        return minute;
    }

    /**
     * Gets the second component of the time.
     * @return the second.
     */
    public int getSecond(){
        return second;
    }

    /**
     * Returns a string representation of the time in "HH:MM:SS" format.
     * @return the formatted time string.
     */
    @Override
    public String toString(){
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Compares this Time object with another Time object.
     * @param other the other Time object to compare to.
     * @return a negative integer, zero, or a positive integer as this Time is less than, equal to, or greater than the specified Time.
     */
    public int compareTo(Time other) {
        if (this.hour != other.hour)
            return Integer.compare(this.hour, other.hour);
        if (this.minute != other.minute)
            return Integer.compare(this.minute, other.minute);
        return Integer.compare(this.second, other.second);
    }
}
