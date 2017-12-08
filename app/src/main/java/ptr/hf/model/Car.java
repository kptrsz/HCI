package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Car implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("type")
    private String type;
    @SerializedName("licence_plate_number")
    private String plate;
    @SerializedName("battery_capacity")
    private float capacity;
    @SerializedName("plugs")
    private ArrayList<Integer> plugTypes;

    public Car() {
        id = -1;
        manufacturer = "";
        type = "";
        plate = "";
        capacity = 0;
        plugTypes = new ArrayList<>();
    }

    public Car(Car other) {
        id = other.getId();
        manufacturer = String.copyValueOf(other.getManufacturer().toCharArray());
        type = String.copyValueOf(other.getType().toCharArray());
        plate = String.copyValueOf(other.getPlate().toCharArray());
        capacity = other.getCapacity();
        plugTypes = new ArrayList<>(other.getPlugTypes());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public ArrayList<Integer> getPlugTypes() {
        return plugTypes;
    }

    public void setPlugTypes(ArrayList<Integer> plugTypes) {
        this.plugTypes = plugTypes;
    }

    public boolean isPlugTypeCompatible(int id) {
        for (int i : plugTypes) {
            if (i == id) return true;
        }
        return false;
    }

    public void updatePlugCompatibility(int id, boolean isCompatible) {
        if (isCompatible) {
            getPlugTypes().add(id);
        } else {
            removeCompatibility(getPlugTypes(), id);
        }
    }

    private void removeCompatibility(ArrayList<Integer> list, int value) {
        for (Integer i : list) {
            if (i.intValue() == value) {
                list.remove(i);
                return;
            }
        }
    }

}