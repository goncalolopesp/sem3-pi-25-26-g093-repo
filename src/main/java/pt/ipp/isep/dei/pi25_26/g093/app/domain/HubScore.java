package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.List;

public class HubScore {
    private int id;
    private String stationName;
    private int degree;
    private double strength;
    private double betweenness;
    private double closeness;
    private double hubScore;

    public HubScore(int id, String stationName, int degree, double strength, double betweenness, double closeness, double hubScore)
    {
        this.id = id;
        this.stationName = stationName;
        this.degree = degree;
        this.strength = strength;
        this.betweenness = betweenness;
        this.closeness = closeness;
        this.hubScore = hubScore;
    }

    public int getId() {return id;}
    public String getStationName() {return stationName;}
    public int getDegree() {return degree;}
    public double getStrength() {return strength;}
    public double getBetweenness() {return betweenness;}
    public double getCloseness() {return closeness;}
    public double getHubScore() {return hubScore;}
}
