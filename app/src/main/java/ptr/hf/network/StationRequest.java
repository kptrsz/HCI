package ptr.hf.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class StationRequest {
    @Expose
    @SerializedName("compact")
    private Boolean compact;
    @Expose
    @SerializedName("verbose")
    private Boolean verbose;
    @Expose
    @SerializedName("latitude")
    private Double latitude;
    @Expose
    @SerializedName("longitude")
    private Double longitude;
    @Expose
    @SerializedName("distance")
    private Integer distance;
    @Expose
    @SerializedName("maxResults")
    private int maxResults;

    public StationRequest(Boolean compact, Boolean verbose, Double latitude, Double longitude, Integer distance, int maxResults) {
        this.compact = compact;
        this.verbose = verbose;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.maxResults = maxResults;
    }

    public Boolean getCompact() {
        return compact;
    }

    public void setCompact(Boolean compact) {
        this.compact = compact;
    }

    public Boolean getVerbose() {
        return verbose;
    }

    public void setVerbose(Boolean verbose) {
        this.verbose = verbose;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
