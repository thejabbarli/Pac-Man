package Map;

import java.util.HashMap;

public class SetMaps {

    private HashMap<String, char[][]> maps;
    private String myMap;

    public SetMaps() {
        maps = new HashMap<>();
        myMap = null;
    }

    public void addMap(String mapName, char[][] grid) {
        maps.put(mapName, grid);
        if (myMap == null) {
            myMap = mapName;
        }
    }

    public void switchMap(String mapName) {
        if (maps.containsKey(mapName)) {
            myMap = mapName;
        } else {
            System.out.println("Map not found: " + mapName);
        }
    }

    public boolean canMove(int nextPlayerX, int nextPlayerY) {
        char[][] currentMap = maps.get(myMap);
        if (currentMap == null) {
            System.out.println("No current map set.");
            return false;
        }

        if (nextPlayerX < 0 || nextPlayerX >= currentMap.length || nextPlayerY < 0 || nextPlayerY >= currentMap[0].length) {
            return false;

        }
        return currentMap[nextPlayerX][nextPlayerY] != 'o';
    }
}
