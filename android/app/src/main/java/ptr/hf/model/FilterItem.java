package ptr.hf.model;

import java.io.Serializable;

public class FilterItem implements Serializable {
    private String title;
    private int id;
    private boolean isPlugType;
    private boolean shouldShow;

    public FilterItem(String title, int id, boolean isPlugType) {
        this.title = title;
        this.id = id;
        this.isPlugType = isPlugType;
        shouldShow = true;

    }

    public boolean shouldShow() {
        return shouldShow;
    }

    public void setShouldShow(boolean shouldShow) {
        this.shouldShow = shouldShow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPlugType() {
        return isPlugType;
    }

    public void setPlugType(boolean plugType) {
        isPlugType = plugType;
    }
}
