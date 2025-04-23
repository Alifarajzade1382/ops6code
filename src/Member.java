package model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member {

    private StringProperty id;
    private StringProperty name;

    // سازنده با آرگومان‌ها
    public Member(String id, String name) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    // Getter و Setter برای id
    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    // Getter و Setter برای name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // متدی برای نمایش عضو به صورت متنی
    @Override
    public String toString() {
        return "Member [id=" + getId() + ", name=" + getName() + "]";
    }
}

