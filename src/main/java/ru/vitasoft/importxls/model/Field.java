package ru.vitasoft.importxls.model;

public class Field {
    private String name;
    private String defValue;
    private String type;
    private boolean uniq;
    private Integer colNumber;

    public Field(String name, String defValue, String type, boolean uniq, Integer colNumber) {
        this.name = name;
        this.defValue = defValue;
        this.type = type;
        this.uniq = uniq;
        this.colNumber = colNumber;
    }

    public String getName() {
        return name;
    }

    public String getDefValue() {
        return defValue;
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

    public void setDefValue(String defValue) {
        this.defValue = defValue;
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
                ", defValue='" + defValue + '\'' +
                ", type='" + type + '\'' +
                ", uniq=" + uniq +
                ", colNumber=" + colNumber +
                '}';
    }
}
