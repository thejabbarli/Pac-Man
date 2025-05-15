package Map.Second;

import Map.Third.Situation;

public class BigPoint extends Edible {
    private Situation situation;

    public BigPoint(int x, int y, int tileSize, Situation situation) {
        super(x, y, tileSize, "res/edibles/tileYemBig.png");
        this.situation = situation;
    }

    @Override
    public void onEaten() {
        if (!isEaten()) {
            setEaten(true);
            this.setVisible(false);
            situation.setTotalPoints(situation.getTotalPoints() + 5);
        }
    }
}

