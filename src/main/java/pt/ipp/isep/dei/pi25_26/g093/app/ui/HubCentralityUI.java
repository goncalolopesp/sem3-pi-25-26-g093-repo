package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.HubCentralityController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.HubScore;

import java.util.List;

public class HubCentralityUI {

    public void run() {showData();}

    public void showData()
    {
        System.out.println("Hub Scores are being calculated, this may take a while depending on the graph's size!");

        HubCentralityController controller = new HubCentralityController();
        controller.HubCentrality();

        List<HubScore> scores = HubScore.getScoreList();

        System.out.printf("%-5s | %-10s | %-10s | %-10s | %-12s | %-12s | %-8s\n",
                "ID",
                "Name",
                "Degree",
                "Strength",
                "Betweenness",
                "Harmonic Closeness",
                "Hub Score");

        for(HubScore score : scores)
        {
            System.out.printf("%-5d | %-10s | %-10s | %-10.4f | %-12.4f | %-18.4f | %-8.4f\n",
                    score.getId(),
                    score.getStationName(),
                    score.getDegree(),
                    score.getStrength(),
                    score.getBetweenness(),
                    score.getCloseness(),
                    score.getHubScore());
        }
    }
}
