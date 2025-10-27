package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class DateTime {
    Date date;
    Time time;

    public DateTime(String completeDate){
        String[] parts = completeDate.split("T");
        this.date = new Date(parts[0]);
        this.time = new Time(parts[1]);
    }

    public Date getDate(){
        return date;
    }

    public Time getTime() {
        return time;
    }

    /**
     * Representação segura e estável
     * YYYY-MM-DDTHH:MM:SS
     */
    @Override
    public String toString(){
        return date.toString() + "T" + time.toString();
    }

    public int compareTo(DateTime other) {
        int cmp = this.date.compareTo(other.date);
        return (cmp != 0) ? cmp : this.time.compareTo(other.time);
    }
}
