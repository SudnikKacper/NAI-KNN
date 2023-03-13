public class ObliczanieOdleglosci implements Comparable<ObliczanieOdleglosci>{
    private final Punkt testModel;
    private final Punkt trainModel;
    private final double odleglosc;
    public ObliczanieOdleglosci(Punkt testModel, Punkt trainModel, double odleglosc){
        this.testModel = testModel;
        this.trainModel = trainModel;
        this.odleglosc = odleglosc;

    }

    @Override
    public int compareTo(ObliczanieOdleglosci o) {
        return Double.compare(this.odleglosc, o.odleglosc);
    }

    public double getOdleglosc() {
        return odleglosc;
    }

    public Punkt getTrainModel() {
        return trainModel;
    }

    public Punkt getTestModel() {
        return testModel;
    }
}
