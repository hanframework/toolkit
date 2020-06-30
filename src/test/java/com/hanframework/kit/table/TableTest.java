package com.hanframework.kit.table;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxin
 * 2020-06-30 22:24
 */

public class TableTest {

    @Test
    public void test(){
        tableEnglishTest();
        tableChineseTest();
    }

    public static void tableEnglishTest(){
        //对齐方式,英文模式
        Table table = new Table(AlignStyle.CENTER,TextType.ENGLISH);

        List<TableHeader> tableHeaders = new ArrayList<>();
        tableHeaders.add(new TableHeader("name"));
        tableHeaders.add(new TableHeader("age"));
        tableHeaders.add(new TableHeader("six"));
        tableHeaders.add(new TableHeader("pet"));
        table.setTableHeaders(tableHeaders);

        List<TableRow> tableRows = new ArrayList<>();
        TableRow tableRow = new TableRow();
        List<TableCell> tableCells = new ArrayList<>();
        tableCells.add(new TableCell("xiaohuang"));
        tableCells.add(new TableCell("ersi"));
        tableCells.add(new TableCell("nanxing"));
        tableCells.add(new TableCell("tianyuanqvan"));
        tableRow.setTableCells(tableCells);
        tableRows.add(tableRow);
        tableRows.add(tableRow);
        table.setTableRows(tableRows);
        table.print();
    }


    public static void tableChineseTest(){
        Table table = new Table(AlignStyle.CENTER,TextType.CHINESE);

        List<TableHeader> tableHeaders = new ArrayList<>();
        tableHeaders.add(new TableHeader("名字"));
        tableHeaders.add(new TableHeader("年龄"));
        tableHeaders.add(new TableHeader("性别"));
        tableHeaders.add(new TableHeader("宠物"));
        table.setTableHeaders(tableHeaders);


        List<TableRow> tableRows = new ArrayList<>();


        TableRow tableRow = new TableRow();
        List<TableCell> tableCells = new ArrayList<>();
        tableCells.add(new TableCell("小黄"));
        tableCells.add(new TableCell("二四"));
        tableCells.add(new TableCell("中国男性"));
        tableCells.add(new TableCell("中华田园犬"));
        tableRow.setTableCells(tableCells);

        tableRows.add(tableRow);
        tableRows.add(tableRow);


        table.setTableRows(tableRows);

        table.print();
    }

}
