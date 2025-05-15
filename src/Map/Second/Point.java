package Map.Second;

import Map.Third.Situation;

public class Point extends Edible {
    private Situation situation;

    public Point(int x, int y, int tileSize, Situation situation) {
        super(x, y, tileSize, "res/edibles/tileYem.png");
        this.situation = situation;
    }

    @Override
    public void onEaten() {
        if (!isEaten()) {
            setEaten(true);
            this.setVisible(false);
            situation.setTotalPoints(situation.getTotalPoints() + 1);
        }
    }
}
