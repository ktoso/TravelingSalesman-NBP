/*
 */
package pl.edu.netbeans.generators;

import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import pl.edu.netbeans.toolbox.Pair;
import prefuse.data.Node;
import prefuse.util.display.DisplayLib;

/**
 *
 * @author ktoso
 */
class CityNameProvider {

    private List<String> names = new LinkedList<String>();
    private static final String DATA_FOLDER = "data/";
    private static final String CITIES_FILE = "cities.txt";
    private Random random = new Random();
    private final static int NODE_SPACING = 5;
    Set<Pair<Integer, Integer>> taken = new HashSet<Pair<Integer, Integer>>();

    public CityNameProvider() {
        File file = new File(DATA_FOLDER + File.separator + CITIES_FILE);
        BufferedReader reader = null;

        try {
            System.out.println(file.getAbsolutePath());
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                names.add(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(names.size() == 0){
            throw new RuntimeException("No city names where read!");
        }
    }

    /**
     * Zwraca listę nazw miast
     * @return lista nazw miast do wykorzystania przez wizualizację
     */
    public List<String> getNames() {
        return names;
    }

    public String getRandomName() {
        return names.get(random.nextInt(names.size()));
    }

    /**
     *
     * @param max maksymalne X/Y po jakich rozrzucamy nasze obiekty
     * @return na pewno nie zajęta jeszcze pozycja!
     */
    public Pair<Integer, Integer> getRandomPosition(int max) {
        Pair<Integer, Integer> ent = null;

        int x = 0;
        int y = 0;


        /** Na wypadek jakby nawet po 100 iteracjach ciągle znajdywał zajęte miejsca */
        int antiLock = 0;
        while (placeIsTaken(x, y) && antiLock < 100) {
            x = random.nextInt(max) * NODE_SPACING;
            y = random.nextInt(max) * NODE_SPACING;

            antiLock++;
        }

        ent = new Pair<Integer, Integer>(x, y);

        taken.add(ent);

        return ent;
    }

    /**
     * Oblicza odległość między dwoma punktami
     * @param self pierwszy node
     * @param target drugi node
     * @return
     */
    public int calculateDistance(Node self, Node target) {
        int x1 = self.getInt("x");
        int y1 = self.getInt("y");

        int x2 = target.getInt("x");
        int y2 = target.getInt("y");

        double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

        return (int) Math.round(distance);
    }

    /**
     * Sprawdza czy obecne miejsce jest już zajęte czy nie
     * @param x pozycja x sprawdzanego miejsca
     * @param y pozycja y sprawdzanego miejsca
     * @return wynik sprawdzenia czy miejsce jest zajęte
     */
    private boolean placeIsTaken(int x, int y) {
        return taken.contains(new Pair<Integer, Integer>(x, y));
    }
}
