package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Date {
    int year;
    int month;
    int day;

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

    public int getYear(){
        return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }

    public String toString(){
        return day + "-" + month + "-" + year;
    }

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
