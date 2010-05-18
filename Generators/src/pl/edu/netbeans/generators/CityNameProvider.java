/*
 */
package pl.edu.netbeans.generators;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author ktoso
 */
class CityNameProvider {

    private List<String> names = new LinkedList<String>();
    private String filePath = "data/cities.txt";
    private Random random = new Random();

    public CityNameProvider() {
        File file = new File(filePath);
        BufferedReader reader = null;

        try {
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
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getNames() {
        return names;
    }

    public String getRandomName() {
        return names.get(random.nextInt(names.size()));
    }

    /*
     * TODO: FIXME: Usuń mnie, jestem tylko dla celów prototypowania!!!!!
     */
    int getRandomDistance() {
        return 1 + random.nextInt(10);
    }

    int getRandomPosition(int max) {
        return random.nextInt(max);
    }
}
