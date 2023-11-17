package beerbrewers.batch;

public enum BrewEnum {
    PILSNER(0, 4, 2, 1, 1, 4),
    WHEAT(1, 1, 4, 6, 1, 3),
    IPA(2, 4, 1, 4, 5, 1),
    STOUT(3, 3, 4, 1, 6, 2),
    ALE(4, 4, 6, 2, 2, 8),
    ALCOHOL_FREE(5, 1, 1, 5, 4, 0);


    private final int brewId;
    private final int barley;
    private final int hops;
    private final int wheat;
    private final int malt;
    private final int yeast;

    BrewEnum(int brewId, int barley, int hops, int wheat, int malt, int yeast) {
        this.brewId = brewId;
        this.barley = barley;
        this.hops = hops;
        this.wheat = wheat;
        this.malt = malt;
        this.yeast = yeast;
    }

    public int getBrewId() {
        return brewId;
    }

    public int getBarley() {
        return barley;
    }

    public int getHops() {
        return hops;
    }

    public int getWheat() {
        return wheat;
    }

    public int getMalt() {
        return malt;
    }

    public int getYeast() {
        return yeast;
    }
}
