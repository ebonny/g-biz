package com.griffins.common.file;

import com.griffins.common.GConstants;
import com.griffins.common.exception.DownloadFailureException;
import com.griffins.common.util.CommonUtil;
import com.griffins.common.util.FileUtil;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.common.ConfigUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;

/**
 * The Class File Downloader Util.
 */
public class FileDownloader {
    private static String notFound = ConfigUtil.MSG.getMessage("download.notfound.msg");
    private static String notFile = ConfigUtil.MSG.getMessage("download.notfile.msg");
    private static String downFail = ConfigUtil.MSG.getMessage("download.fail.msg");

    public static void download(HttpServletRequest request, HttpServletResponse response, File file, String filename) throws DownloadFailureException {
        download(request, response, file.getAbsolutePath(), filename);
    }

    /**
     * 파일을 다운로드 한다.
     *
     * @param response   HttpServletResponse
     * @param sourcePath 다운로드 파일의 풀경로.("/some/path/filname.extention")
     * @param filename   사용자에게 보여줄 이름.(ex. wordfile.doc)
     * @param ieFlag     브라우저별 다국어 처리용.( 1 = IE , 0 = 기타 브라우저)
     * @return
     */
    public static void download(HttpServletRequest request
            , HttpServletResponse response
            , String sourcePath
            , String filename) throws DownloadFailureException {

        final File file = new File(sourcePath);

        if (!file.exists()) {
            throw new DownloadFailureException(notFound);
        }

        if (file.isDirectory()) {
            throw new DownloadFailureException(notFile);
        }

        if (StringUtil.isEmpty(filename)) {
            LogUtil.info("Filename is empty. use 'no_name' instead", LogUtil.CONSOLE_ONLY);
            filename = "no_name";
        }

        try {
            filename = StringUtil.decodeScript(filename);
            filename = checkFilename(request, filename);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }

        //=== set header
        // response.setContentType("application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Length", "" + file.length());

        int length = 0;
        byte[] byteBuffer = new byte[1024];

        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
             BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream(), 1024);) {
            while ((inputStream != null) && ((length = inputStream.read(byteBuffer)) != -1)) {
                outStream.write(byteBuffer, 0, length);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        if (file.getName().startsWith("esmp_copied"))
                            file.delete();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            outStream.flush();
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
            throw new DownloadFailureException(downFail);
        }
    }

    public static void downloadString(HttpServletRequest request
            , HttpServletResponse response
            , String filename
            , String content) throws DownloadFailureException {
        downloadString(request, response, filename, content, GConstants.CRLF_CONVERT_NONE, null);
    }

    public static void downloadString(HttpServletRequest request
            , HttpServletResponse response
            , String filename
            , String content
            , int crlfType
            , String toEncoding) throws DownloadFailureException {

        File file = null;

        switch (crlfType) {
            case GConstants.LF_TO_CRLF:
                content = content.replaceAll("(\r|\n|\r\n)+", "\r\n");
//            content = content.replaceAll("(\r|\n|\r\n)+", "\n").replaceAll("\n", "\r\n");
                break;
            case GConstants.CRLF_TO_LF:
                content = content.replaceAll("(\r\n)+", "\n");
                break;
        }

        if (StringUtil.isNotEmpty(toEncoding) && !toEncoding.equalsIgnoreCase("UTF8") && !toEncoding.equalsIgnoreCase("UTF-8")) {
            file = FileUtil.writeNewFromString(content, CommonUtil.getRealPath("upload/temp") + filename, toEncoding);
        } else {
            file = FileUtil.writeFromString(content, CommonUtil.getRealPath("upload/temp") + filename);
        }

        if (!file.exists()) {
            throw new DownloadFailureException(notFound);
        }

        if (file.isDirectory()) {
            throw new DownloadFailureException(notFile);
        }

        if (StringUtil.isEmpty(filename)) {
            LogUtil.info("Filename is empty. use 'no_name' instead", LogUtil.CONSOLE_ONLY);
            filename = "no_name";
        }

        try {
            filename = StringUtil.decodeScript(filename);
            filename = checkFilename(request, filename);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }

        //=== set header
        // response.setContentType("application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Length", "" + file.length());

        int length = 0;
        byte[] byteBuffer = new byte[1024];

        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
             BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream(), 1024);) {
            while ((inputStream != null) && ((length = inputStream.read(byteBuffer)) != -1)) {
                outStream.write(byteBuffer, 0, length);
            }

//         new Thread(new Runnable() {
//            @Override
//            public void run() {
//               try {
//                  Thread.sleep(3000);
//                  file.delete();
//               } catch (InterruptedException e) {
//                  e.printStackTrace();
//               }
//            }
//         }).start();

            outStream.flush();

            Thread.sleep(1000);
            file.delete();
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
            throw new DownloadFailureException(downFail);
        }
    }

    public static void download(HttpServletRequest request
            , HttpServletResponse response
            , String sourcePath
            , String filename
            , byte[] fileData) {

        if (StringUtil.isEmpty(filename)) {
            filename = "no_name";
        }

        try {
            filename = checkFilename(request, filename);
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }

        /* set header */
        // response.setContentType("application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Length", "" + fileData.length);

        byte[] byteBuffer = fileData;
        try {
            if (fileData.length != 0) {
                BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream(), fileData.length);
                outStream.write(byteBuffer, 0, fileData.length);
                outStream.flush();
            }
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }

    }

    private static String checkFilename(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
        if (StringUtil.isNotEmpty(filename))
            filename = URLDecoder.decode(filename, "UTF-8").replaceAll(Matcher.quoteReplacement("/"), "");
        String header = request.getHeader("user-agent");
        if (header.contains("MSIE")) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else if (header.contains("Trident")) {
            filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "8859_1");
        }
        return filename;
    }

}
