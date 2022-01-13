package ru.vitasoft.model;

public class Field {
    private String name;
    private String type;
    private boolean uniq;
    private Integer colNumber;

    public Field(String name, String type, boolean uniq, Integer colNumber) {
        this.name = name;
        this.type = type;
        this.uniq = uniq;
        this.colNumber = colNumber;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isUniq() {
        return uniq;
    }

    public Integer getColNumber() {
        return colNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUniq(boolean uniq) {
        this.uniq = uniq;
    }

    public void setColNumber(Integer colNumber) {
        this.colNumber = colNumber;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", uniq=" + uniq +
                ", colNumber=" + colNumber +
                '}';
    }
}
