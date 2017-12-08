package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationResponse {
    @SerializedName("success")
    Boolean success;

    @SerializedName("data")
    List<Reservation> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Reservation> getData() {
        return data;
    }

    public void setData(List<Reservation> data) {
        this.data = data;
    }
}
