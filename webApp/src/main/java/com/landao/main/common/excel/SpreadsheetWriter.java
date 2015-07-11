package com.landao.main.common.excel;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
/**
 * Writes spreadsheet data in a Writer.
 * (YK: in future it may evolve in a full-featured API for streaming data in Excel)
 */
public class SpreadsheetWriter {
	private final Writer _out;
    private int _rownum;

    public SpreadsheetWriter(Writer out) {
      this._out = out;
    }

    public void beginWorkSheet() throws IOException {
      this._out
          .write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
    }

    public void beginSheet() throws IOException {
      this._out.write("<sheetData>\n");
    }

    public void endSheet() throws IOException {
      this._out.write("</sheetData>");
      // 合并单元格
    }

    public void endWorkSheet() throws IOException {
      this._out.write("</worksheet>");
    }

    //插入行 不带高度
    public void insertRow(int rownum) throws IOException {
      this._out.write("<row r=\"" + (rownum + 1) + "\">\n");
      this._rownum = rownum;
    }

    public void endRow() throws IOException {
      this._out.write("</row>\n");
    }

    //插入行且设置高度
    public void insertRowWithheight(int rownum, int columnNum, double height)
        throws IOException {
      this._out.write("<row r=\"" + (rownum + 1) + "\" spans=\"1:"
          + columnNum + "\" ht=\"" + height
          + "\" customHeight=\"1\">\n");
      this._rownum = rownum;
    }

    public void endWithheight() throws IOException {
      this._out.write("</row>\n");
    }

    public void beginSetColWidth() throws IOException {
      this._out.write("<cols>\n");
    }

    // 设置列宽 下标从0开始
    public void setColWidthBeforeSheet(int columnIndex, double columnWidth)
        throws IOException {
      this._out.write("<col min=\"" + (columnIndex + 1) + "\" max=\""
          + (columnIndex + 1) + "\" width=\"" + columnWidth
          + "\" customWidth=\"1\"/>\n");
    }

    public void endSetColWidth() throws IOException {
      this._out.write("</cols>\n");
    }

    public void beginMergerCell() throws IOException {
      this._out.write("<mergeCells>\n");
    }

    public void endMergerCell() throws IOException {
      this._out.write("</mergeCells>\n");
    }

    // 合并单元格 下标从0开始
    public void setMergeCell(int beginColumn, int beginCell, int endColumn,
        int endCell) throws IOException {
      this._out.write("<mergeCell ref=\"" + getExcelName(beginCell + 1)
          + (beginColumn + 1) + ":" + getExcelName(endCell + 1)
          + (endColumn + 1) + "\"/>\n");// 列行:列行
    }

    public void createCell(int columnIndex, String value, int styleIndex)
        throws IOException {
      String ref = new CellReference(this._rownum, columnIndex)
          .formatAsString();
      this._out.write("<c r=\"" + ref + "\" t=\"inlineStr\"");
      if (styleIndex != -1)
        this._out.write(" s=\"" + styleIndex + "\"");
      this._out.write(">");
      this._out.write("<is><t>" + value + "</t></is>");
      this._out.write("</c>");
    }

    public void createCell(int columnIndex, String value)
        throws IOException {
      createCell(columnIndex, value, -1);
    }

    public void createCell(int columnIndex, double value, int styleIndex)
        throws IOException {
      String ref = new CellReference(this._rownum, columnIndex)
          .formatAsString();
      this._out.write("<c r=\"" + ref + "\" t=\"n\"");
      if (styleIndex != -1)
        this._out.write(" s=\"" + styleIndex + "\"");
      this._out.write(">");
      this._out.write("<v>" + value + "</v>");
      this._out.write("</c>");
    }

    public void createCell(int columnIndex, double value)
        throws IOException {
      createCell(columnIndex, value, -1);
    }

    public void createCell(int columnIndex, Calendar value, int styleIndex)
        throws IOException {
      createCell(columnIndex, DateUtil.getExcelDate(value, false),
          styleIndex);
    }

    //10 进制转26进制
    private String getExcelName(int i) {
      char[] allChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
      StringBuilder sb = new StringBuilder();
      while (i > 0) {
        sb.append(allChar[i % 26 - 1]);
        i /= 26;
      }
      return sb.reverse().toString();
    }
}
