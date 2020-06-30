package com.hanframework.kit.table;

import com.hanframework.kit.collection.CollectionTools;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuxin
 * 2019-10-08 23:51
 */
public class Table {
    /**
     * 头部
     */
    private List<TableHeader> tableHeaders;

    private List<TableRow> tableRows;

    /**
     * 对齐方式
     */
    private AlignStyle alignStyle = AlignStyle.CENTER;

    private TextType textType = TextType.ENGLISH;


    private static final String LINE_MARK = "-";

    private static final String CORNER = "+";

    /**
     * 占位符
     */
    private static final String POSITION = " ";

    /**
     * 换行符
     */
    public static final String NEW_LINE = "\n";

    public List<TableHeader> getTableHeaders() {
        return tableHeaders;
    }

    public void setTableHeaders(List<TableHeader> tableHeaders) {
        this.tableHeaders = tableHeaders;
    }

    public List<TableRow> getTableRows() {
        return tableRows;
    }

    public void setTableRows(List<TableRow> tableRows) {
        this.tableRows = tableRows;
    }

    public Table(AlignStyle alignStyle) {
        this.alignStyle = alignStyle;
    }

    public Table(TextType textType) {
        this.textType = textType;
    }

    public Table(AlignStyle alignStyle, TextType textType) {
        this.alignStyle = alignStyle;
        this.textType = textType;
    }

    /**
     * 获取表格最大长度宽度
     * 为了让表格看起来更漂亮些,最大长度根据最大文本来决定
     *
     * @return int
     */
    private int getMaximumWidth() {
        List<Integer> calculationContainer = new ArrayList<>();
        for (TableRow tableRow : tableRows) {
            for (TableCell tableCell : tableRow.getTableCells()) {
                int width = tableCell.getWidth();
                calculationContainer.add(width);
            }
        }
        for (TableHeader tableHeader : tableHeaders) {
            int width = tableHeader.getWidth();
            calculationContainer.add(width);
        }
        return CollectionTools.max(calculationContainer) + 2;
    }


    private String adaptiveOutputLength(String desc) {
        int maximumWidth = getMaximumWidth();
        int length = desc.length();
        String tempDesc = desc;
        if (maximumWidth > length) {
            if (alignStyle == AlignStyle.LEFT) {
                tempDesc = adaptiveOutputLength(tempDesc + POSITION);
            } else if (alignStyle == AlignStyle.RIGHT) {
                tempDesc = adaptiveOutputLength(POSITION + tempDesc);
            } else {
                tempDesc = adaptiveOutputLength(POSITION + tempDesc + POSITION);
            }
        } else if (maximumWidth < length) {
            int i = length - maximumWidth;
            tempDesc = desc.substring(0, desc.length() - i);
        }
        return tempDesc;
    }

    private int getRowWidth() {
        int cellWidth = getMaximumWidth();
        int size = getTableHeaders().size();
        return size * cellWidth;
    }

    private String line(String desc) {
        int rowWidth = getRowWidth() + 3;
        if (textType == TextType.CHINESE) {
            rowWidth = getRowWidth() + 9;
        }
        int length = desc.length();
        desc = desc + LINE_MARK;
        if (length < rowWidth) {
            desc = line(desc);
        }
        return desc;
    }

    @SuppressWarnings("all")
    public void print() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(line(CORNER)).append(CORNER).append(NEW_LINE);
        stringBuilder.append("|");
        for (TableHeader tableHeader : tableHeaders) {
            String tableHeaderDesc = tableHeader.getTableHeaderDesc();
            stringBuilder.append(adaptiveOutputLength(tableHeaderDesc)).append("|");
        }
        stringBuilder.append(NEW_LINE);
        stringBuilder.append(line(CORNER)).append(CORNER).append(NEW_LINE);

        for (TableRow tableRow : tableRows) {
            stringBuilder.append("|");
            List<TableCell> tableCells = tableRow.getTableCells();
            for (TableCell tableCell : tableCells) {
                stringBuilder.append(adaptiveOutputLength(tableCell.getTableCellDesc())).append("|");
            }
            stringBuilder.append(NEW_LINE);
        }
        stringBuilder.append(line(CORNER)).append(CORNER).append(NEW_LINE);
        System.out.println(stringBuilder.toString());
    }



    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
