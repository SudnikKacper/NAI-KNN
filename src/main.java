import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException {


        List<Punkt> punktsTraining;
        List<Punkt> punktsTesting;

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("K: \t");
        int k = Integer.parseInt(bf.readLine());
        System.out.println("Nazwa modelu (iris/wdbc) domyslnie jest iris");
        String modelName = bf.readLine();
        if (modelName.equals("wdbc") || modelName.equals("WDBC")){
            System.out.println("Wybrano model wdbc");
            punktsTraining =getListePunktow("D:\\NAI\\KNN\\resources\\wdbc.data");
            punktsTesting = getListePunktow("D:\\NAI\\KNN\\resources\\wdbc.test.data");
        }
        else  {
            System.out.println("Wybrano model iris");
            punktsTraining =getListePunktow("D:\\NAI\\KNN\\resources\\iris.data");
            punktsTesting = getListePunktow("D:\\NAI\\KNN\\resources\\iris.test.data");
        }


//        System.out.println(punktsTraining);
//        System.out.println(punktsTesting);
        System.out.println(punktsTesting.get(5));
        System.out.println(punktsTraining.get(10));
        System.out.println(distAB(punktsTesting.get(5), punktsTraining.get(10)));

    }

    public static List<Punkt> getListePunktow(String fAdres) throws IOException {
        String line;
        List<Punkt> punkts = new ArrayList<>();

        FileReader fr = new FileReader(fAdres);
        BufferedReader bf = new BufferedReader(fr);

        while ((line= bf.readLine())!=null && (!line.equals(""))){
            String[] content = line.split(",");

            List<Double> listaKolumn = new ArrayList<>();
            for (int i = 0; i < content.length-1; i++) {
                listaKolumn.add(Double.parseDouble(content[i]));
            }

            punkts.add(new Punkt(listaKolumn, content[content.length-1]));
        }
        return punkts;
    }


    public static double distAB(Punkt a, Punkt b) {
        if (a == null) {
            System.out.println("A nie istnieje");
            return -1;
        }
        if (b == null) {
            System.out.println("B nie istnieje");
            return -1;
        }

        double dist = 0;
        for (int i = 0; i < a.getPunkty().size(); i++) {
            dist += Math.pow(a.getPunkty().get(i)-b.getPunkty().get(i),2);
        }
        return dist;
    }
}
