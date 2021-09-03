package sg.edu.rp.webservices.livetrafficincidentcheck;

public class CameraImage {
    String cameraID, imageLink;
    double latitude, longitude;

    public CameraImage(String cameraID, String imageLink, double latitude, double longitude) {
        this.cameraID = cameraID;
        this.imageLink = imageLink;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCameraID() {
        return cameraID;
    }

    public void setCameraID(String cameraID) {
        this.cameraID = cameraID;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}