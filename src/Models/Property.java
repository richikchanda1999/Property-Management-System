package Models;

import java.util.Date;

public class Property {
    private int property_id;
    private int no_of_bathrooms, no_of_bedrooms, no_of_balconies, storey;
    private double carpet_area, size_of_kitchen;
    private String type_of_property;
    private boolean elevator, terrace_access;
    private PropertyAddress address;
    private Date dateAdded;

    public static final int ERROR = -1, IN_MARKET = 0, SOLD = 1, RENTED = 2;

    public Property(int property_id, int no_of_bathrooms, int no_of_bedrooms, int no_of_balconies, double carpet_area, boolean elevator, int storey, String type_of_property, double size_of_kitchen, boolean terrace_access, PropertyAddress address, Date dateAdded) {
        this.property_id = property_id;
        this.no_of_bathrooms = no_of_bathrooms;
        this.no_of_bedrooms = no_of_bedrooms;
        this.no_of_balconies = no_of_balconies;
        this.carpet_area = carpet_area;
        this.elevator = elevator;
        this.storey = storey;
        this.type_of_property = type_of_property;
        this.size_of_kitchen = size_of_kitchen;
        this.terrace_access = terrace_access;
        this.address = address;
        this.dateAdded = dateAdded;
    }

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }

    public int getNo_of_bathrooms() {
        return no_of_bathrooms;
    }

    public void setNo_of_bathrooms(int no_of_bathrooms) {
        this.no_of_bathrooms = no_of_bathrooms;
    }

    public int getNo_of_bedrooms() {
        return no_of_bedrooms;
    }

    public void setNo_of_bedrooms(int no_of_bedrooms) {
        this.no_of_bedrooms = no_of_bedrooms;
    }

    public int getNo_of_balconies() {
        return no_of_balconies;
    }

    public void setNo_of_balconies(int no_of_balconies) {
        this.no_of_balconies = no_of_balconies;
    }

    public int getStorey() {
        return storey;
    }

    public void setStorey(int storey) {
        this.storey = storey;
    }

    public double getSize_of_kitchen() {
        return size_of_kitchen;
    }

    public void setSize_of_kitchen(double size_of_kitchen) {
        this.size_of_kitchen = size_of_kitchen;
    }

    public double getCarpet_area() {
        return carpet_area;
    }

    public void setCarpet_area(double carpet_area) {
        this.carpet_area = carpet_area;
    }

    public String getType_of_property() {
        return type_of_property;
    }

    public void setType_of_property(String type_of_property) {
        this.type_of_property = type_of_property;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public boolean isTerrace_access() {
        return terrace_access;
    }

    public void setTerrace_access(boolean terrace_access) {
        this.terrace_access = terrace_access;
    }

    public PropertyAddress getAddress() {
        return address;
    }

    public void setAddress(PropertyAddress address) {
        this.address = address;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String[] toArray() {
        String[] arr = new String[17];
        arr[0] = property_id + "";
        arr[1] = no_of_bathrooms + "";
        arr[2] = no_of_bedrooms + "";
        arr[3] = no_of_bedrooms + "";
        arr[4] = carpet_area + "";
        arr[5] = elevator ? "Yes" : "No";
        arr[6] = storey + "";
        arr[7] = type_of_property;
        arr[8] = size_of_kitchen + "";
        arr[9] = terrace_access ? "Yes" : "No";
        arr[10] = address.getCountry();
        arr[11] = address.getState();
        arr[12] = address.getCity();
        arr[13] = address.getPincode() + "";
        arr[14] = address.getLocality();
        arr[15] = address.getLandmark();
        arr[16] = dateAdded.toString();
        return arr;
    }
}
