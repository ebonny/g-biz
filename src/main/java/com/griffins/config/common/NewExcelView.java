package com.griffins.config.common;

import com.griffins.common.util.StringUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * 파일명 : NewExcelView.java
 * <p>
 * 엑셀 파일 생성 및 다운로드 (2007 이상 버전 지원)
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 10. 10.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class NewExcelView extends AbstractView {

    @SuppressWarnings({"unchecked", "resource"})
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        String title = (String) model.get("title");
        XSSFSheet worksheet = workbook.createSheet(title);
        List<Map<String, Object>> datalist = (List<Map<String, Object>>) model.get("data");
        List<String> idlist = (List<String>) model.get("idlist");
        List<String> namelist = (List<String>) model.get("namelist");
        XSSFRow row = null;
        XSSFCell cell = null;
        int line = 0;

        //=== 정합성 체크
        if (StringUtil.isEmpty(title))
            throw new Exception("메뉴 이름이 없습니다");
        if (idlist == null || idlist.size() == 0 || namelist == null || namelist.size() == 0 || idlist.size() != namelist.size())
            throw new Exception("컬럼 데이터가 잘못되었습니다");

        //=== 스타일 정의
        /*타이틀 스타일*/
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);            // 스타일인스턴스의 속성
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = (XSSFFont) workbook.createFont();                // 폰트 설정
        font.setBold(false);     // setBoldweight((short) 300);
        font.setFontHeightInPoints((short) 15);
        titleStyle.setFont(font);

        /*제목 스타일*/
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);                // 스타일인스턴스의 속성
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(new XSSFColor(Color.decode("0xd3d3d3")));    // 셀 배경색 설정
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);        // 테두리 설정
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        font = workbook.createFont();                                    //폰트 설정
        font.setBold(true);     // setBoldweight((short) 700);
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);

        /*본문 스타일 (얇은 테두리)*/
        XSSFCellStyle thinStyle = workbook.createCellStyle();
        thinStyle.setAlignment(HorizontalAlignment.CENTER);
        thinStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        thinStyle.setBorderRight(BorderStyle.THIN);
        thinStyle.setBorderLeft(BorderStyle.THIN);
        thinStyle.setBorderTop(BorderStyle.THIN);
        thinStyle.setBorderBottom(BorderStyle.THIN);

        //=== 타이틀 입력
        row = worksheet.createRow(line++);
        row.setHeight((short) 700);
        cell = row.createCell(0);
        cell.setCellStyle(titleStyle);
        cell.setCellValue(title);

        //=== 제목 입력
        row = worksheet.createRow(line++);
        row.setHeight((short) 512);
        for (int i = 0; i < idlist.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(namelist.get(i));
        }

        //=== 내용 입력
        for (Map<String, Object> map : datalist) {
            row = worksheet.createRow(line++);
            row.setHeight((short) 400);
            for (int i = 0; i < idlist.size(); i++) {
                String id = idlist.get(i);
                cell = row.createCell(i);
                cell.setCellStyle(thinStyle);
                cell.setCellValue(StringUtil.nvl(map.get(id)));
            }
        }

        //=== 타이틀 셀 병합
        worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, idlist.size() - 1));

        //=== 제목 행 틀고정
        worksheet.createFreezePane(0, 2);

        //=== 자동 너비 조절
        for (int i = 0; i < idlist.size(); i++) {
            worksheet.autoSizeColumn(i);
            worksheet.setColumnWidth(i, (worksheet.getColumnWidth(i)) + 512); // 자동조정한 사이즈에 (short)512를 추가해 보기좋게 처리
        }

        //=== 다운로드
        title = java.net.URLEncoder.encode(title, "UTF-8");    // 파일명 한글처리
        title = title.replaceAll("\\+", "%20");        // 파일명 공백 설정
        response.setContentType("application/msexcel");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + title + ".xlsx\";");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        if (out != null)
            out.close();
    }

}
