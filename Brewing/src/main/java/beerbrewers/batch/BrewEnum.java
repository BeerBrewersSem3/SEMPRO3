package beerbrewers.batch;

public enum BrewEnum {

    PILSNER("Pilsner",0),
    WHEAT("Wheat",1),
    IPA("IPA",2),
    STOUT("Stout",3),
    ALE("Ale",4),
    ALCOHOL_FREE("Alcohol Free",5);

    private final String name;
    private final int id;
    BrewEnum(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public int getI
}
