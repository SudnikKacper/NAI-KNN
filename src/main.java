import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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



        doKNN(punktsTesting, punktsTraining, k, false);

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


    public static void doKNN(List<Punkt> listaTestowa, List<Punkt> listaTreningowa, int k, boolean pokazWszystko){


        int poprawnie = 0;
        String rozwiazanie = "";


        for (Punkt pTest :
                listaTestowa) {

            List<ObliczanieOdleglosci> odlegloscis = new ArrayList<>();

            for (Punkt pTren : listaTreningowa)
                odlegloscis.add(new ObliczanieOdleglosci(pTest, pTren, distAB(pTest, pTren)));

            Collections.sort(odlegloscis);
//
            List<String> listaNazwyGatunkowTreningowych = new ArrayList<>();
            Set<String> setNazwGatunkowTreningowych = new HashSet<>();

            for (int i = 0; i < k; i++) {
                listaNazwyGatunkowTreningowych.add(odlegloscis.get(i).getTrainModel().getGatunek());
                setNazwGatunkowTreningowych.add(odlegloscis.get(i).getTrainModel().getGatunek());
            }

            int wystapienia = 0;
            for (String gatunek :
                    setNazwGatunkowTreningowych) {
                if (Collections.frequency(listaNazwyGatunkowTreningowych,gatunek)>wystapienia){
                    wystapienia = Collections.frequency(listaNazwyGatunkowTreningowych,gatunek);
                    rozwiazanie = gatunek;
                }
            }


            if (rozwiazanie.equals(pTest.getGatunek()))
                poprawnie++;


            if (pokazWszystko){
                System.out.println("K = " + k + ", KNN result = " + rozwiazanie);
            }

        }

        double a = poprawnie;
        double b = listaTestowa.size();
        double procent = a/b*100;
        System.out.println("Poprawnie " + poprawnie + "/" + listaTestowa.size() + ",    poprawnosc: " + procent + "%");



    }

}
