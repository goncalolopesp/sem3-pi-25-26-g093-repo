package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class DateTime {
    Date date;
    Time time;

    public DateTime(String completeDate){
        String[] parameters = completeDate.split("T");
        this.date = new Date(parameters[0]);
        this.time = new Time(parameters[1]);
    }

    public Date getDate(){
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String toString(){
        return date + "T" + time;
    }

    public int compareTo(DateTime other) {
        int dateCompare = this.date.compareTo(other.getDate());
        if (dateCompare != 0) {
            return dateCompare; // earlier/later day
        }

        int timeCompare = this.time.compareTo(other.getTime());
        return timeCompare; // earlier/later time
    }
}
