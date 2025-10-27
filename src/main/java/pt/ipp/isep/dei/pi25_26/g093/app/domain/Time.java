package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Time {
    int hour;
    int minute;
    int second;

    public Time(String time){
        String[] parts = time.split(":");
        this.hour = Integer.parseInt(parts[0]);
        this.minute = Integer.parseInt(parts[1]);
        this.second = Integer.parseInt(parts[2]);
    }

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return minute;
    }

    public int getSecond(){
        return second;
    }

    @Override
    public String toString(){
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public int compareTo(Time other) {
        if (this.hour != other.hour)
            return Integer.compare(this.hour, other.hour);
        if (this.minute != other.minute)
            return Integer.compare(this.minute, other.minute);
        return Integer.compare(this.second, other.second);
    }
}
