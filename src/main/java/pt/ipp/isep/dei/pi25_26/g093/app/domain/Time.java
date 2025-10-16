package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Time {
    int hour;
    int minute;
    int second;

    public Time(String time){
        String[] times = time.split(":");
        this.hour = Integer.parseInt(times[0]);
        this.minute = Integer.parseInt(times[1]);
        this.second = Integer.parseInt(times[2]);
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

    public String toString(){
        return hour + ":" + minute + ":" + second;
    }

    public int compareTo(Time other) {
        if (this.hour != other.hour)
            return Integer.compare(this.hour, other.hour);
        if (this.minute != other.minute)
            return Integer.compare(this.minute, other.minute);
        return Integer.compare(this.second, other.second);
    }
}
