package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Coordinate;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PickPathSequencer;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PickPathService {

    private PickPathSequencer sequencer = new PickPathSequencer();

    public void generatePickPath(TrolleyPlan plan){


        if(plan==null){
            System.out.println("Erro: plano nulo.");
            return;
        }
        Set<Coordinate> pickList= new HashSet<>();



        for(TrolleyItem item : plan.getItems()){
            int aisle = item.getAisle();
            int bay = item.getBay();
            Coordinate pickPoint = new Coordinate(aisle, bay);

            pickList.add(pickPoint);

        }


    }


}
