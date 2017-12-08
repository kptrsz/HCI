package ptr.hf.network;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Station implements Serializable {
    @SerializedName("ID")
    private int id;
    @SerializedName("AddressInfo")
    private Address address;
    @SerializedName("Connections")
    private Connection[] connections;
    @SerializedName("OperatorInfo")
    private Operator operator;
    public LatLng getLatLng() {
        return new LatLng(address.lat, address.lng);
    }

    public int getOperatorID() {
        if (operator == null) return 45;
        return operator.getId();
    }

    public String getOperatorTitle() {
        if (operator == null) return "(Business owner at location)";
        return operator.getTitle();
    }

    public ConnectionType[] getPlugTypes() {
        ConnectionType[] ret = new ConnectionType[connections.length];
        for (int i = 0; i < connections.length; i++) {
            ret[i] = new ConnectionType();
            ret[i].setId(connections[i].type.id);
            ret[i].setTitle(connections[i].type.Title);
        }
        return ret;
    }

    public String getAddressTitle() {
        return address.title;
    }

    public String getAddress() {
        return address.getAddressLine1() + address.getAddressLine2();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public class Operator implements Serializable{
        @SerializedName("ID")
        private int id;
        @SerializedName("Title")
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    public class Connection implements Serializable {
        @SerializedName("ConnectionType")
        private ConnectionType type;

    }

    public class Address implements Serializable{
        @SerializedName("Title")
        private String title;
        @SerializedName("AddressLine1")
        private String address1;
        @SerializedName("AddressLine2")
        private String address2;
        @SerializedName("Latitude")
        private float lat;
        @SerializedName("Longitude")
        private float lng;

        public String getAddressLine1() {
            return address1 == null ? "" : address1;
        }

        public String getAddressLine2() {
            return address2 == null ? "" : "\n" + address2;
        }
    }

    public class ConnectionType implements Serializable{
        @SerializedName("Title")
        private String Title;

        public void setTitle(String title) {
            Title = title;
        }

        public void setId(int id) {
            this.id = id;
        }

        @SerializedName("ID")
        private int id;

        public String getTitle() {
            return Title;
        }

        public int getId() {
            return id;
        }
    }
}
