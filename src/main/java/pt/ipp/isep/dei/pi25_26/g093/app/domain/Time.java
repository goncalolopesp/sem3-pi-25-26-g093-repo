package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Time {
    int hour;
    int minute;
    int second;

    /**
     * Creates a new Time with the given parameters.
     * @param time the time in the format HH:MM:SS
     */
    public Time(String time){
        String[] times = time.split(":");
        this.hour = Integer.parseInt(times[0]);
        this.minute = Integer.parseInt(times[1]);
        this.second = Integer.parseInt(times[2]);
    }

    /**
     * Gets the hour of the time.
     * @return the hour of the time.
     */
    public int getHour(){
        return hour;
    }

    /**
     * Gets the minute of the time.
     * @return the minute of the time.
     */
    public int getMinute(){
        return minute;
    }

    /**
     * Gets the second of the time.
     * @return the second of the time.
     */
    public int getSecond(){
        return second;
    }

    /**
     * Gets the time representation in a String in the format HH:MM:SS.
     * @return the time representation in a String in the format HH:MM:SS.
     */
    public String toString(){
        return hour + ":" + minute + ":" + second;
    }

    /**
     * Compares this Time to another Time.
     * @param other target of comparing
     * @return 1 if this Time is later, -1 if the Time is earlier, 0 if the Times are equal.
     */
    public int compareTo(Time other) {
        if (this.hour != other.hour)
            return Integer.compare(this.hour, other.hour);
        if (this.minute != other.minute)
            return Integer.compare(this.minute, other.minute);
        return Integer.compare(this.second, other.second);
    }
}
