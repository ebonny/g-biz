package com.griffins.config.common;

import com.griffins.common.util.StringUtil;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.List;

/**
 * 파일명 : PdfView.java
 * <p>
 * PDF 파일 생성 및 다운로드 ===============================================
 *
 * @author 이재철
 * @since 2016. 10. 11.
 * <p>
 * 수정자 수정일 수정내용 ------------- ------------- -----------------
 * <p>
 * =============================================== Copyright (C) by ESMP
 * All right reserved.
 */
public class PdfView extends AbstractPdfView {

    @SuppressWarnings("unchecked")
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String title = (String) model.get("title");
        List<Map<String, Object>> datalist = (List<Map<String, Object>>) model.get("data");
        List<String> idlist = (List<String>) model.get("idlist");
        List<String> namelist = (List<String>) model.get("namelist");
        PdfPTable table = new PdfPTable(idlist.size()); // 테이블의 컬럼 수 지정
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidthPercentage(100);
        PdfPCell cell = null;

        //=== 정합성 체크
        if (StringUtil.isEmpty(title))
            throw new Exception("메뉴 이름이 없습니다");
        if (idlist == null || idlist.size() == 0 || namelist == null || namelist.size() == 0 || idlist.size() != namelist.size())
            throw new Exception("컬럼 데이터가 잘못되었습니다");

        //=== 스타일 지정
//		BaseFont bf = BaseFont.createFont("HTGoThic-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED);
        BaseFont bf = BaseFont.createFont(getServletContext().getRealPath("/static/font/HMKMRHD.TTF"), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        FontFactory.register(getServletContext().getRealPath("/static/font/NanumBarunGothic.ttf"));
        Font titleFont = new Font(bf, 16, Font.BOLD);
        Font columnFont = FontFactory.getFont("나눔바른고딕", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8); //new Font(bf, 8);
        Font nomalFont = FontFactory.getFont("나눔바른고딕", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7); //new Font(bf, 7, Font.NORMAL);

        //=== 제목 입력
        Paragraph txt = new Paragraph(title, titleFont);
        txt.setAlignment(Element.ALIGN_CENTER);
        txt.setSpacingAfter(20);
        document.add(txt);

        //=== 컬럼 입력
        for (int i = 0; i < idlist.size(); i++) {
            cell = new PdfPCell(new Paragraph(namelist.get(i), columnFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingBottom(5);
            cell.setBackgroundColor(Color.decode("0xd3d3d3"));
            cell.setMinimumHeight(20);
            table.addCell(cell);
        }

        //=== 내용 입력
        for (Map<String, Object> map : datalist) {
            for (int i = 0; i < idlist.size(); i++) {
                String id = idlist.get(i);
                cell = new PdfPCell();
                cell.setPhrase(new Paragraph(StringUtil.nvl(map.get(id)), nomalFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingBottom(5);
                table.addCell(cell);
            }
        }
        document.add(table);

        //=== 다운로드를 위한 response 처리
        title = URLEncoder.encode(title, "UTF-8"); // 파일명 한글처리
        title = title.replaceAll("\\+", "%20"); // 파일명 공백 설정
        response.setContentType("application/pdf");
        response.setHeader("Content-Transper-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + title + ".pdf\"");

    }

}
