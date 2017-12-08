package ptr.hf.network;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("name")
    private String name;
    @SerializedName("message")
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}