package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Date {
    int year;
    int month;
    int day;

    /**
     * Creates a new Date with the given parameters.
     * @param date Date in the format YYYY-MM-DD
     */
    public Date(String date){
        if (date == null || date.length() == 0){
            date = "9999-12-31";
        }
        if (date.contains("T")) {
            date = date.split("T")[0];
        }
        String[] newDate = date.split("-");
        this.year = Integer.parseInt(newDate[0]);
        this.month = Integer.parseInt(newDate[1]);
        this.day = Integer.parseInt(newDate[2]);
    }

    /**
     * Gets the year of the date
     * @return the year of the date.
     */
    public int getYear(){
        return year;
    }

    /**
     * Gets the month of the date
     * @return the month of the date
     */
    public int getMonth(){
        return month;
    }

    /**
     * Gets the day of the date.
     * @return the day of the date.
     */
    public int getDay(){
        return day;
    }

    /**
     * Gets a string representation of the date.
     * @return a string representation of the date in the format DD-MM-YYYY.
     */
    public String toString(){
        return day + "-" + month + "-" + year;
    }

    /**
     * Compares this Date to another Date.
     * @param date the date to be compared with.
     * @return 1 if this date is greater, -1 if the date is lesser, 0 if the dates are equal.
     */
    public int compareTo(Date date) {
        if (this.year > date.getYear()) {
            return 1;
        } else if (this.year < date.getYear()) {
            return -1;
        } else { // anos iguais
            if (this.month > date.getMonth()) {
                return 1;
            } else if (this.month < date.getMonth()) {
                return -1;
            } else { // meses iguais
                if (this.day > date.getDay()) {
                    return 1;
                } else if (this.day < date.getDay()) {
                    return -1;
                } else {
                    return 0; // exatamente a mesma data
                }
            }
        }
    }

}
