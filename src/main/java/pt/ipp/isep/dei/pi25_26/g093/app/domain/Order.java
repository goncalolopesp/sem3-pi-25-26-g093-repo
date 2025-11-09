package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Order {

    private int id;
    private Date dueDate;
    private int priority;

    public Order(int id, String dueDate, int priority)
    {
        this.id = id;
        this.dueDate = new Date(dueDate);
        this.priority = priority;
    }

    public Order(int id, Date dueDate, int priority)
    {
        this.id = id;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public int getId() {return id;}
    public Date getDueDate() {return dueDate;}
    public int getPriority() {return priority;}
}
