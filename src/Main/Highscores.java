package Main;

import java.io.*;
import java.util.ArrayList;

public class Highscores implements Serializable {
    private final static String filePath = "res/highscores.txt";
    public String name;
    public int score;

    public Highscores(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public static ArrayList<Highscores> read() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
            return (ArrayList<Highscores>) objectInputStream.readObject();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(ArrayList<Highscores> highscoresArrayList) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
            objectOutputStream.writeObject(highscoresArrayList);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
