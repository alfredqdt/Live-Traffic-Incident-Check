package sg.edu.rp.webservices.livetrafficincidentcheck;

public class CarPark {
    String development;
    String lotType;
    String location;
    String availableLots;

    public CarPark(String development, String lotType, String location, String availableLots) {
        this.development = development;
        this.lotType = lotType;
        this.location = location;
        this.availableLots = availableLots;
    }

    public String getDevelopment() {
        return development;
    }

    public void setDevelopment(String development) {
        this.development = development;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailableLots() {
        return availableLots;
    }

    public void setAvailableLots(String availableLots) {
        this.availableLots = availableLots;
    }
}
