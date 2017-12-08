package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

public class ReservationData {

    @SerializedName("_id")
    String ReservationId;

    public String getReservationId() {
        return ReservationId;
    }

    public void setReservationId(String reservationId) {
        ReservationId = reservationId;
    }
}
