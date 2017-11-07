package nl.anwb.menoa.model;

/**
 * In deze class staat de data die van de telefoon
 * afkomstig is.
 */
public class NoodoproepTelefoon {

    private int mannr;      //Man nummer
    private int hvnr;       //Hulpverlener nummer
    private float lat;      //Latitude
    private float lng;      //Longitude
    private String adres;   //Adres van de noodmelding

    /**
     * Nodig om de noodoproeptTelefoon aan te maken en later te vullen met JSON data
     * Misschien niet nodig
     */
    public NoodoproepTelefoon() {
    }

    public NoodoproepTelefoon(int mannr, int hvnr, double lat, double lng, String adres) {
        this.mannr = mannr;
        this.hvnr = hvnr;
        this.lat = (float) lat;
        this.lng = (float) lng;
        this.adres = adres;
    }

    public int getMannr() {
        return mannr;
    }

    public void setMannr(int mannr) {
        this.mannr = mannr;
    }

    public int getHvnr() {
        return hvnr;
    }

    public void setHvnr(int hvnr) {
        this.hvnr = hvnr;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    @Override
    public String toString() {
        return "mannr=" + mannr +
                "\nhvnr=" + hvnr +
                "\nlat=" + lat +
                " ,lng=" + lng +
                "\nadres=" + adres;
    }
}