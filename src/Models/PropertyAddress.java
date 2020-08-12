package Models;

public class PropertyAddress {
    private int property_id;
    private String country, state, city, locality, landmark;
    private int pincode;

    public PropertyAddress(int property_id, String country, String state, String city, int pincode, String locality, String landmark) {
        this.property_id = property_id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.locality = locality;
        this.landmark = landmark;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }
}
