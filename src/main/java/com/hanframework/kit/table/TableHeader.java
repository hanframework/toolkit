package com.hanframework.kit.table;


/**
 * @author liuxin
 * 2019-10-08 23:52
 */
public class TableHeader {
    /**
     * 宽度
     */
    private int width;

    private String tableHeaderDesc;


    public TableHeader(String tableHeaderDesc) {
        this.tableHeaderDesc = tableHeaderDesc;
    }

    public int getWidth() {
        return tableHeaderDesc.length();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTableHeaderDesc() {
        return tableHeaderDesc;
    }

    public void setTableHeaderDesc(String tableHeaderDesc) {
        this.tableHeaderDesc = tableHeaderDesc;
    }
}
