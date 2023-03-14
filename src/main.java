import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.List;

public class main {

    public static void main(String[] args) throws Exception {

        boolean kont = true;

        List<Punkt> punktsTraining;
        List<Punkt> punktsTesting;

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Testowanie modeli czy konkretnych punktów na modelach");
        System.out.println("1 - testowanie modeli");
        System.out.println("2 - testowanie punktów na modelach");
        System.out.println("Wpisz 1 lub 2");
        int c1 = Integer.parseInt(bf.readLine());


        if (c1 == 1) {
            do{


                System.out.print("Podaj nazwę modelu: ");
                String nazwaModelu = bf.readLine();
                System.out.println();

                System.out.println("Wybrano model " + nazwaModelu);

                punktsTraining = getListePunktow("D:\\NAI\\KNN\\resources\\" + nazwaModelu + ".data");
                punktsTesting = getListePunktow("D:\\NAI\\KNN\\resources\\" + nazwaModelu +".test.data");

                System.out.print("Podaj k");
                int k = Integer.parseInt(bf.readLine());
                System.out.println();

                if (k > punktsTraining.size())
                    throw new RuntimeException("K wychodzi poza zakres. Dla podanego modelu jest to od 0 do " + (punktsTraining.size()));

                System.out.println("Czy chcesz wyswietlac wszystkie punkty?");
                System.out.println("t/n");
                String show = bf.readLine();
                boolean pokaz;
                if (show.equals("t"))
                    pokaz = true;
                else
                    pokaz = false;

                doKNN(punktsTesting, punktsTraining, k, pokaz, true);

                System.out.println("Czy chcesz jeszcze raz");
                System.out.println("t/n");
                String koniec = bf.readLine();
                if (koniec.equals("n"))
                    kont = false;

            } while (kont);
        }

        else if (c1 == 2){

            do {

                System.out.print("Podaj k");
                int k = Integer.parseInt(bf.readLine());
                System.out.println();

                System.out.print("Podaj nazwę modelu: ");
                String nazwaModelu = bf.readLine();
                System.out.println();

                System.out.println("Wybrano model " + nazwaModelu);

                punktsTraining = getListePunktow("D:\\NAI\\KNN\\resources\\" + nazwaModelu + ".data");

                System.out.println("Czy chcesz wyswietlac wszystkie punkty?");
                System.out.println("t/n");
                String show = bf.readLine();
                boolean pokaz;
                if (show.equals("t"))
                    pokaz = true;
                else
                    pokaz = false;

                List<Punkt> testowy = new ArrayList<>();

                List<Double> wymiary = new ArrayList<>();
                for (int i = 0; i < punktsTraining.get(0).getPunkty().size(); i++) {
                    System.out.println("Wpisz wymiar " + i+1);
                    wymiary.add(Double.parseDouble(bf.readLine()));
                }
                System.out.println();


                testowy.add(new Punkt(wymiary,""));
                String gatunekTestowego = doKNN(testowy,punktsTraining, k, false, false);

                Punkt p = new Punkt(wymiary, gatunekTestowego);
                System.out.println(p);

                System.out.println("Czy chcesz jeszcze raz");
                System.out.println("t/n");
                String koniec = bf.readLine();
                if (koniec.equals("n"))
                    kont = false;

            } while (kont);

        }




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


        public static String doKNN(List<Punkt> listaTestowa, List<Punkt> listaTreningowa, int k, boolean pokazWszystko, boolean pokazDokladnosc){


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

            if (pokazDokladnosc || pokazWszystko){
                double a = poprawnie;
                double b = listaTestowa.size();
                double procent = a / b * 100;
                System.out.println("Poprawnie " + poprawnie + "/" + listaTestowa.size() + ",    poprawnosc: " + procent + "%");
            }
        return rozwiazanie;

    }

}
