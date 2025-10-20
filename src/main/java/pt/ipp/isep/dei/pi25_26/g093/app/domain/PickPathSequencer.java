package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PickPathSequencer {


    private static int distanceCoordinates(Coordinate c1, Coordinate c2){
        int distance=0;

        int a1=c1.getAisle();
        int b1=c1.getBay();
        int a2=c2.getAisle();
        int b2=c2.getBay();

        if(a1==a2){
            distance=Math.abs(b1-b2);
        }
        else{
            distance = b1 + Math.abs(a1-a2)*3 +b2;
        }
        return distance;

    }

    public int totalDistance(List<Coordinate>coor){

        int totalDis=0;
        Coordinate start= new Coordinate(0,0);

        if (coor==null){
            return 0;
        }

        else if (coor.size()==1){
            totalDis=distanceCoordinates(start, coor.get(0));
            return totalDis;
        }

        int firstDist=distanceCoordinates(start, coor.get(0));
        return firstDist+totalDistance(coor.subList(1,coor.size()));
    }

    public int strategy1Dis(List<Coordinate>bays){

        if(bays==null || bays.isEmpty()){
            return 0;
        }

        List<Coordinate> ordered = new ArrayList<>(bays);


        Comparator<Coordinate> comp= new Comparator<Coordinate>(){

            @Override
            public int compare(Coordinate c1, Coordinate c2){
                if (c1.getAisle()<c2.getAisle()){
                    return -1;
                } else if (c1.getAisle()>c2.getAisle()) {
                    return 1;
                }

                else{
                    if (c1.getBay()<c2.getBay()){
                        return -1;
                    }
                    else if (c1.getBay()>c2.getBay()){
                        return 1;
                    }
                }
                return 0;
            }
        };
        ordered.sort(comp);
        return totalDistance(ordered);
    }

    public int strategy2Dis(List<Coordinate>bays){

        Coordinate start = new Coordinate(0,0);

        List<Coordinate> noVisited = new ArrayList<>(bays);
        List<Coordinate> visited = new ArrayList<>();
        if (bays == null || bays.isEmpty()) {
            return 0;
        }

        while (!noVisited.isEmpty()){

            Coordinate maisProximo=null;
            int menorDistancia= Integer.MAX_VALUE;

            for(Coordinate c : noVisited){

                if(distanceCoordinates(start, c)<menorDistancia) {
                    maisProximo=c;
                    menorDistancia=distanceCoordinates(start, c);

                }

            }
            visited.add(maisProximo);
            noVisited.remove(maisProximo);
            start=maisProximo;

        }

        return totalDistance(visited);


    }
}
