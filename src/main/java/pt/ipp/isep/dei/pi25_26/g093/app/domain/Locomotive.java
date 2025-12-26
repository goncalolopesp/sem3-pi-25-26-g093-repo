package pt.ipp.isep.dei.pi25_26.g093.app.domain;

public class Locomotive {

    private final int id;
    private final String identifier;
    private final String modelName;
    private final LocomotiveModel model;
    private final int operatorId;
    private int currentFacilityId;
    private boolean busy = false;


    public Locomotive(int id,
                      String identifier,
                      String modelName,
                      LocomotiveModel model,
                      int operatorId,
                      int currentFacilityId) {
        this.id = id;
        this.identifier = identifier;
        this.modelName = modelName;
        this.model = model;
        this.operatorId = operatorId;
        this.currentFacilityId = currentFacilityId;
    }

    public int getId() { return id; }
    public String getIdentifier() { return identifier; }
    public LocomotiveModel getModel() { return model; }
    public int getOperator() { return operatorId; }

    public int getCurrentFacilityId() {
        return currentFacilityId;
    }

    public void setCurrentFacilityId(int currentFacilityId) {
        this.currentFacilityId = currentFacilityId;
    }

    public double getLengthMeters() { return model.getLengthMeters(); }
    public double getMaxSpeedKmh() { return model.getMaxSpeedKmh(); }
    public double getPowerKw() { return model.getPowerKw(); }

    public String getModelName() {
        return model != null ? model.getName() : "UNKNOWN";
    }
    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isAvailable() {
        return !busy;
    }

    public void setAvailable(boolean available) {
        this.busy = !available;
    }




}

