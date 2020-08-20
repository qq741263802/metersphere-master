package io.metersphere.excel.utils;

import com.alibaba.excel.EasyExcel;
import io.metersphere.commons.utils.LogUtil;
import io.metersphere.exception.ExcelException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class EasyExcelExporter {

    EasyExcelI18nTranslator easyExcelI18nTranslator;

    private Class clazz;

    public EasyExcelExporter(Class clazz) {
        this.clazz = clazz;
        //防止多线程修改运行时类注解后，saveOriginalExcelProperty保存的是修改后的值
        synchronized (EasyExcelI18nTranslator.class) {
            easyExcelI18nTranslator = new EasyExcelI18nTranslator(clazz);
            easyExcelI18nTranslator.translateExcelProperty();
        }

    }

    public void export(HttpServletResponse response, List data, String fileName, String sheetName) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), this.clazz).sheet(sheetName).doWrite(data);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(e.getMessage(), e);
            throw new ExcelException("Utf-8 encoding is not supported");
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), e);
            throw new ExcelException("IO exception");
        }
    }

    public void close() {
        easyExcelI18nTranslator.resetExcelProperty();
    }

}