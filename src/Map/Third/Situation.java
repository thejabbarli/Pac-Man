package Map.Third;

public class Situation {
    private int totalPoints;

    public Situation() {
        this.totalPoints = 0;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void increaseTotalPoints() {
        totalPoints++;
    }

    public void reset() {
        totalPoints = 0;
    }
}
