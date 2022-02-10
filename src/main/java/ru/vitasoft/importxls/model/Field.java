package ru.vitasoft.importxls.model;

public class Field {
    private String name;
    private String defValue;
    private boolean required;
    private String type;
    private boolean uniq;
    private int colNumber;

    public Field(String name, String defValue, boolean required, String type, boolean uniq, int colNumber) {
        this.name = name;
        this.defValue = defValue;
        this.required = required;
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

    public boolean getRequired() {
        return required;
    }

    public String getType() {
        return type;
    }

    public boolean isUniq() {
        return uniq;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUniq(boolean uniq) {
        this.uniq = uniq;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", defValue='" + defValue + '\'' +
                ", required=" + required +
                ", type='" + type + '\'' +
                ", uniq=" + uniq +
                ", colNumber=" + colNumber +
                '}';
    }
}
