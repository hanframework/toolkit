package com.hanframework.kit.table;


/**
 * 单元格
 *
 * @author liuxin
 * 2019-10-08 23:55
 */
public class TableCell {
    /**
     * 宽度
     */
    private int width;

    private String tableCellDesc;

    public TableCell(String tableCellDesc) {
        this.tableCellDesc = tableCellDesc;
    }

    public int getWidth() {
        return tableCellDesc.length();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTableCellDesc() {
        return tableCellDesc;
    }

    public void setTableCellDesc(String tableCellDesc) {
        this.tableCellDesc = tableCellDesc;
    }
}
