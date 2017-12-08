package ptr.hf.network;

import com.google.gson.annotations.Expose;

class ReservationRequest {
    @Expose
    private String chargerId;
    @Expose
    private String userId;
    @Expose
    private String from;
    @Expose
    private String to;
    @Expose
    private String id;

    public ReservationRequest(String chargerId, String userId, String from, String to) {
        this.chargerId = chargerId;
        this.userId = userId;
        this.from = from;
        this.to = to;
    }

    public ReservationRequest(String id) {
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
