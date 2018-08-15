package com.griffins.common.util;

import com.griffins.common.file.FileNameManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.common.util.FileUtil
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since #{DATE}
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class FileUtil {
    public static FileNameManager uploadSpecificFile(MultipartFile file, String propertiesPathKey, String basePath, boolean useFile) throws IOException {
        FileNameManager fileMgr = new FileNameManager(file.getName());

        if (file != null && !file.isEmpty()) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String originFileName = file.getOriginalFilename();
            String saveFilename = CommonUtil.shortenUUID(UUID.randomUUID()) + StringUtil.getExtention(originFileName);
            String fileUploadPath = CommonUtil.getPathProperty(propertiesPathKey, basePath);

            File realDir = new File(fileUploadPath);
            if (!realDir.exists()) {
                realDir.mkdirs();
            }

            File writeFile = new File(fileUploadPath + saveFilename);
            file.transferTo(writeFile);
            if (useFile)
                fileMgr.addFileNamesWithFile(writeFile, originFileName, saveFilename);
            else
                fileMgr.addFileNames(originFileName, saveFilename);
        }
        return fileMgr;
    }

    public static FileNameManager uploadSpecificFile(MultipartFile file, String propertiesPathKey, String basePath) throws IOException {
        return uploadSpecificFile(file, propertiesPathKey, basePath, false);
    }

    public static FileNameManager uploadSpecificFile(MultipartFile file, String propertiesPathKey, boolean useFile) throws IOException {
        return uploadSpecificFile(file, propertiesPathKey, "upload", useFile);
    }

    public static FileNameManager uploadSpecificFile(MultipartFile file, String propertiesPathKey) throws IOException {
        return uploadSpecificFile(file, propertiesPathKey, "upload", false);
    }

    public static FileNameManager uploadSpecificFile(HttpServletRequest request, String objName, String propertiesPathKey, String basePath, boolean useFile) throws Exception {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multiRequest.getFiles(objName);
        FileNameManager fileMgr = new FileNameManager(objName);

        if (files != null && files.size() > 0) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String originFileName = file.getOriginalFilename();
                    String saveFilename = CommonUtil.shortenUUID(UUID.randomUUID()) + StringUtil.getExtention(originFileName);
                    String fileUploadPath = CommonUtil.getPathProperty(propertiesPathKey, basePath);

                    File realDir = new File(fileUploadPath);
                    if (!realDir.exists()) {
                        realDir.mkdirs();
                    }

                    File writeFile = new File(fileUploadPath + saveFilename);
                    file.transferTo(writeFile);
                    if (useFile)
                        fileMgr.addFileNamesWithFile(writeFile, originFileName, saveFilename);
                    else
                        fileMgr.addFileNames(originFileName, saveFilename);
                }
            }
        }
        return fileMgr;
    }

    /* request, 파일객체명, 프로퍼티key */
    public static FileNameManager uploadSpecificFile(HttpServletRequest request, String objName, String propertiesPathKey) throws Exception {
        return uploadSpecificFile(request, objName, propertiesPathKey, "upload", false);
    }

    /* request, 파일객체명, 프로퍼티key, 업로드된파일 사용여부 */
    public static FileNameManager uploadSpecificFile(HttpServletRequest request, String objName, String propertiesPathKey, boolean useFile) throws Exception {
        return uploadSpecificFile(request, objName, propertiesPathKey, "upload", useFile);
    }

    /* request, 파일객체명, 프로퍼티key, 업로드 Root 경로 */
    public static FileNameManager uploadSpecificFile(HttpServletRequest request, String objName, String propertiesPathKey, String basePath) throws Exception {
        return uploadSpecificFile(request, objName, propertiesPathKey, basePath, false);
    }


    public static List<String> readAsList(String fullPath) {
        List<String> lines = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(fullPath));
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return lines;
    }

    public static String readAsString(String fullPath) {
        String content = "";
        try (FileInputStream fis = new FileInputStream(new File(fullPath));
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return content;
    }

    public static String readAsString(String fullPath, String encoding) {
        String content = "";
        try (FileInputStream fis = new FileInputStream(new File(fullPath));
             InputStreamReader isr = StringUtil.isEmpty(encoding) ? new InputStreamReader(fis) : new InputStreamReader(fis, encoding);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return content;
    }

    public static String readAsString(File file) {
        String content = "";
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return content;
    }

    public static String readAsString(File file, String encoding) {
        String content = "";
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = StringUtil.isEmpty(encoding) ? new InputStreamReader(fis) : new InputStreamReader(fis, encoding);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                content += line + "\n";
            }
        } catch (IOException e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return content;
    }


    /**
     * String 을 텍스트형식의 파일로 만들어서 반환
     *
     * @param content
     * @param fullPath
     * @param toEncoding
     * @return
     */
    public static File writeFromString(String content, String fullPath, String toEncoding) {
        File file = new File(fullPath);
        try (
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath), toEncoding));
        ) {
            out.write(content);
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return file;
    }

    public static File writeFromString(String content, String fullPath) {
        return writeFromString(content, fullPath, "UTF8");
    }

    /**
     * encoding 방식을 바꿔서 save as 한것처럼 새로 저장된 파일을 반환
     * 주로 DB 서버 OS 와 파일을 사용할 OS 가 다를때 사용
     *
     * @param content
     * @param fullPath
     * @param fromEncoding
     * @param toEncoding
     * @return
     */
    public static File writeNewFromString(String content, String fullPath, String fromEncoding, String toEncoding, String lineSeparator) {
        File tmpfile = new File(fullPath + ".bak");
        File file = new File(fullPath);
        String s = "";

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath + ".bak"), fromEncoding))) {
            out.write(content);
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }

        try (
                BufferedReader in = new BufferedReader(new FileReader(tmpfile));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath), toEncoding))
        ) {
            while ((s = in.readLine()) != null) {
                out.write(s + lineSeparator);
//               out.newLine();
            }
            tmpfile.delete();
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
        return file;
    }

    public static File writeNewFromString(String content, String fullPath, String toEncoding) {
        return writeNewFromString(content, fullPath, System.getProperty("file.encoding"), toEncoding, "\r\n");
    }
}
