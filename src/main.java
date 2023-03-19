import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                System.out.println("Chcesz sprawdzić dokładnośc względem k to wpisz 1 jak nie to cokolwiek");
                String dd = bf.readLine();



                    System.out.print("Podaj nazwę modelu: ");
                    String nazwaModelu = bf.readLine();
                    System.out.println();

                    System.out.println("Wybrano model " + nazwaModelu);

                    punktsTraining = getListePunktow("D:\\NAI\\KNN\\resources\\" + nazwaModelu + ".data");
                    punktsTesting = getListePunktow("D:\\NAI\\KNN\\resources\\" + nazwaModelu + ".test.data");

                if (!dd.equals("1")) {
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
                }
                else {

                    System.out.println("K;  Poprawnie;  Poprawność");
                    for (int i = 0; i < punktsTraining.size(); i++) {
                        int k = i;
                        System.out.print(k + ";  ");
                        doKNN(punktsTesting, punktsTraining, k, false, true);
                    }
                }

                System.out.println("Czy chcesz jeszcze raz");
                System.out.println("t/n");
                String koniec = bf.readLine();
                if (koniec.equals("n"))
                    kont = false;

            } while (kont);
        }

        else if (c1 == 2){

            do {

                System.out.print("Podaj k: ");
                int k = Integer.parseInt(bf.readLine());
                System.out.println();

                System.out.print("Podaj nazwę modelu: ");
                String nazwaModelu = bf.readLine();
                System.out.println();

                System.out.println("Wybrano model " + nazwaModelu);

                punktsTraining = getListePunktow("D:\\NAI\\KNN\\resources\\" + nazwaModelu + ".data");


                List<Punkt> testowy = new ArrayList<>();

                List<Double> wymiary = new ArrayList<>();
                for (int i = 0; i < punktsTraining.get(0).getPunkty().size(); i++) {
                    System.out.println("Wpisz wymiar " + (i+1));
                    wymiary.add(Double.parseDouble(bf.readLine()));
                }
                System.out.println();


                testowy.add(new Punkt(wymiary,""));
                String gatunekTestowego = punktoweKNN(testowy,punktsTraining, k);

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

    /***
     * Tworzenie listy punktów
     * @param fAdres, adres pliku
     * @return listę punktów z danego pliku
     * @throws IOException
     */

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


    /***
     * Obliczanie odległości między dwoma punktami
     * @param a, pierwszy punkt
     * @param b, drugi punkt
     * @return odległość między tymi punktami
     */
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


    /***
     * Algorytm K-NN
     * @param listaTestowa, lista wektorów która będzie twstowana według zbioru treningowego
     * @param listaTreningowa, lista wektorów treningowych
     * @param k, ilość najbliższych punktów które będą decydować o kategori wektora
     * @param pokazWszystko, wypisywanie decyzji dla każdego wektora
     * @param pokazDokladnosc, wypisywanie podsumowania dla modelu, niedostępne dla vektorów bez sprawdzonego
     * @return nazwę jaką algorytm wybrał dla ostatniego wektora w liście
     */
        public static String doKNN(List<Punkt> listaTestowa, List<Punkt> listaTreningowa, int k, boolean pokazWszystko, boolean pokazDokladnosc){


        int poprawnie = 0;
        String rozwiazanie = "";



        for (Punkt pTest :
                listaTestowa) {

            List<ObliczanieOdleglosci> odlegloscis = new ArrayList<>();

            //obliczanie odległości dla każdego względem listy treningowej i dodawanie do listy
            for (Punkt pTren : listaTreningowa)
                odlegloscis.add(new ObliczanieOdleglosci(pTest, pTren, distAB(pTest, pTren)));

            Collections.sort(odlegloscis);

            List<String> listaNazwyGatunkowTreningowych = new ArrayList<>();
            Set<String> setNazwGatunkowTreningowych = new HashSet<>();

            //zbieram nazwy gatunków z najbliższych wektorów
            for (int i = 0; i < k; i++) {
                listaNazwyGatunkowTreningowych.add(odlegloscis.get(i).getTrainModel().getGatunek());
                setNazwGatunkowTreningowych.add(odlegloscis.get(i).getTrainModel().getGatunek());
            }

            //sprawdzam którego jest najwięcej i ustawiam nazwę rozwiązania jako ten gatunek
            int wystapienia = 0;
            for (String gatunek :
                    setNazwGatunkowTreningowych) {
                if (Collections.frequency(listaNazwyGatunkowTreningowych,gatunek)>wystapienia){
                    wystapienia = Collections.frequency(listaNazwyGatunkowTreningowych,gatunek);
                    rozwiazanie = gatunek;
                }
            }


            //sprawdzam czy jest poprawne względem testu
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
                System.out.println(poprawnie + "/" + listaTestowa.size() + ";   " + procent + "%");
            }
        return rozwiazanie;

    }


    public static String punktoweKNN(List<Punkt> listaTestowa, List<Punkt> listaTreningowa, int k){
            return doKNN(listaTestowa, listaTreningowa,k, false, false);
    }
}
