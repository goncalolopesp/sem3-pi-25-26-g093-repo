package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Order {

    private String id;
    private Date dueDate;
    private int priority;

    public Order(String id, String dueDate, int priority)
    {
        this.id = id;
        this.dueDate = new Date(dueDate);
        this.priority = priority;
    }

    public Order(String id, Date dueDate, int priority)
    {
        this.id = id;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public String getId() {return id;}
    public Date getDueDate() {return dueDate;}
    public int getPriority() {return priority;}
}
