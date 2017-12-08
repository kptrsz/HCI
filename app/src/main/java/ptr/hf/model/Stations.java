package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ptr.hf.network.Station;

public class Stations {
    @SerializedName("")
    ArrayList<Station> stations;

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}
