package Map.Second;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import Map.Map;
import Map.Third.Situation;

public class EdibleGenerator {

    public static List<Edible> generateEdibles(Map map, JPanel yemPanel, Situation situation) {
        List<Edible> edibles = new ArrayList<>();
        int tileSize = map.getTileSize();

        for (int y = 0; y < map.getMap().length; y++) {
            for (int x = 0; x < map.getMap()[0].length; x++) {
                char tile = map.getMap()[y][x];
                Edible edible = createEdible(tile, x, y, tileSize, situation);
                if (edible != null) {
                    edibles.add(edible);
                    yemPanel.add(edible); // Add edible to yemPanel
                }
            }
        }
        return edibles;
    }

    private static Edible createEdible(char tile, int x, int y, int tileSize, Situation situation) {
        switch (tile) {
            case 'o':
                return new Point(x, y, tileSize, situation);
            case 'O':
                return new BigPoint(x, y, tileSize, situation);
            default:
                return null; // No edible on this tile
        }
    }
}
