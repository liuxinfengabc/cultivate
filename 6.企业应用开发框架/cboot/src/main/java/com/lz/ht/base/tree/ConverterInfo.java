package com.lz.ht.base.tree;

public class ConverterInfo {

    private  String idColumn;//  id;

    private  String nameColumn;//deptName;

    private String pidColumn;//parentId;

    private String openOrNot ;// open: true

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getPidColumn() {
        return pidColumn;
    }

    public void setPidColumn(String pidColumn) {
        this.pidColumn = pidColumn;
    }

    public String getOpenOrNot() {
        return openOrNot;
    }

    public void setOpenOrNot(String openOrNot) {
        this.openOrNot = openOrNot;
    }
}
