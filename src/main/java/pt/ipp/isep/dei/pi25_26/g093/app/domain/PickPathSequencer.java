package pt.ipp.isep.dei.pi25_26.g093.app.domain;

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

    }
}
