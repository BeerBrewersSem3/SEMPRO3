package beerbrewers.batch;

public enum BrewEnum {

    PILSNER(0, 4, 2, 1, 1, 4, "Pilsner"),
    WHEAT(1, 1, 4, 6, 1, 3,"Wheat"),
    IPA(2, 4, 1, 4, 5, 1,"IPA"),
    STOUT(3, 3, 4, 1, 6, 2,"Stout"),
    ALE(4, 4, 6, 2, 2, 8,"Ale"),
    ALCOHOL_FREE(5, 1, 1, 5, 4, 0,"Alcohol Free");


    private final int brewId;
    private final int barley;
    private final int hops;
    private final int wheat;
    private final int malt;
    private final int yeast;
    private final String name;

    BrewEnum(int brewId, int barley, int hops, int wheat, int malt, int yeast, String name) {
        this.brewId = brewId;
        this.barley = barley;
        this.hops = hops;
        this.wheat = wheat;
        this.malt = malt;
        this.yeast = yeast;
        this.name = name;
    }
    public String getBrewName(){
        return name;
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

    public static BrewEnum getBrewFromId(int brewId) {
        BrewEnum[] brewEnums = {BrewEnum.PILSNER,
                                BrewEnum.WHEAT,
                                BrewEnum.IPA,
                                BrewEnum.STOUT,
                                BrewEnum.ALE,
                                BrewEnum.ALCOHOL_FREE};
        return brewEnums[brewId];
    }
}
