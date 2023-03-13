import java.util.List;

public class Punkt{

    private final List<Double> punkty;
    private final String gatunek;

    public Punkt(List<Double> punkty, String nGatunku) {
        this.punkty = punkty;
        this.gatunek = nGatunku;
    }

    public List<Double> getPunkty() {
        return punkty;
    }

    public String getGatunek() {
        return gatunek;
    }

    @Override
    public String toString() {
        return "{ Wymiary= " + punkty + ", nazwa gatunku= " + gatunek + " }\n";
    }

}
