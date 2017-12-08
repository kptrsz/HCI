package ptr.hf.network;

import com.google.gson.annotations.Expose;

class UserSettings {
    @Expose
    private String userId;
    @Expose
    private String connectorType;
    @Expose
    private int distance;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
