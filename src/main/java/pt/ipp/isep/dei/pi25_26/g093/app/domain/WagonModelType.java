package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class WagonModelType {

    private final int id;
    private final String typeName;

    public WagonModelType(int id) {
        this.id = id;
        if(id == 1){

            this.typeName = "Wood wagon";

        }else if(id == 2){

            this.typeName = "Cereal wagon";

        }else if(id == 3){

            this.typeName = "Covered wagon with sliding door";

        }else if(id == 4){

            this.typeName = "Container wagon (max 40' HC)";

        }else if(id == 5){

            this.typeName = "Container wagon (max 40' HC)";
        }else{

            this.typeName = null;

        }

    }
    public String getTypeName() {
        return typeName;
    }
}
