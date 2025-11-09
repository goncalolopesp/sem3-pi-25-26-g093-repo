package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String id;
    private Date dueDate;
    private int priority;
    private List<OrderLine> lines;

    public Order(String id, String dueDate, int priority)
    {
        this.id = id;
        this.dueDate = new Date(dueDate);
        this.priority = priority;
        lines = new ArrayList<>();
    }

    public Order(String id, Date dueDate, int priority)
    {
        this.id = id;
        this.dueDate = dueDate;
        this.priority = priority;
        lines = new ArrayList<>();
    }

    public String getId() {return id;}
    public int getIntId() {return Integer.parseInt(id.substring(3));}
    public Date getDueDate() {return dueDate;}
    public int getPriority() {return priority;}
    public List<OrderLine> getLines() {return lines;}

    public void addNewLine(OrderLine line) {lines.add(line);}
    public void addNewLines(List<OrderLine> lines) {this.lines.addAll(lines);}
}
