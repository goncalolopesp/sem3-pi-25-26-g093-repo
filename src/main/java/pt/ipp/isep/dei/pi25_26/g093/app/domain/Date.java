package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Date {
    int year;
    int month;
    int day;

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

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    /**
     * Mantemos o formato original de input: YYYY-MM-DD
     */
    @Override
    public String toString(){
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public int compareTo(Date other) {
        if (this.year != other.getYear())
            return Integer.compare(this.year, other.getYear());
        if (this.month != other.getMonth())
            return Integer.compare(this.month, other.getMonth());
        return Integer.compare(this.day, other.getDay());
    }
}
