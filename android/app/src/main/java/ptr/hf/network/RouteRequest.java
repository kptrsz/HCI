package ptr.hf.network;

import com.google.gson.annotations.Expose;

import retrofit2.http.Body;

class RouteRequest {

    @Expose
    private Integer dist;
    @Expose
    private Start start;
    @Expose
    private Start end;

    public RouteRequest(Integer dist, Start start, Start end) {
        this.dist = dist;
        this.start = start;
        this.end = end;
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public Start getEnd() {
        return end;
    }

    public void setEnd(Start end) {
        this.end = end;
    }
}
