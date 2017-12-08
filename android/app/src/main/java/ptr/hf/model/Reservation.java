package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Reservation {
    @SerializedName("chargerId")
    String chargerId;

    @SerializedName("userId")
    String userId;

    @SerializedName("from")
    Integer from;

    @SerializedName("to")
    Integer to;

    @SerializedName("_id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
