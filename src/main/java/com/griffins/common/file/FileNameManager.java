package com.griffins.common.file;

import com.griffins.common.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일명 : griffins.common.model.FileNameManager
 * *
 * 하나의 파일객체명(objName) 에 대한 실제파일명과 저장파일명의 리스트를 저장 및 관리
 * ===============================================
 *
 * @author 이재철
 * @since 2017-04-30
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class FileNameManager extends ArrayList<FileNameVO> {

    private String objName;

    public FileNameManager(String objName) {
        this.objName = objName;
    }

    public void addFileNamesWithFile(File file, String originalName, String saveName) {
        FileNameVO vo = new FileNameVO();
        vo.setOriginalName(StringUtil.getOnlyFilename(originalName));
        vo.setSaveName(saveName);
        vo.setFile(file);
        add(vo);
    }

    public void addFileNames(String originalName, String saveName) {
        addFileNamesWithFile(null, StringUtil.getOnlyFilename(originalName), saveName);
    }


    public List<String> getOriginalFilenames() {
        List<String> oriNames = new ArrayList<>();
        for (FileNameVO vo : this) {
            String name = StringUtil.encodeScript(StringUtil.decodeScript(vo.getOriginalName()));
            if (StringUtil.isNotEmpty(name))
                oriNames.add(name);
        }
        return oriNames;
    }

    public String getOriginalFilename() {
        return getOriginalFilenameAt(0);
    }

    public String getOriginalFilenameAt(int index) {
        String oriName = null;
        if (size() > index)
            oriName = StringUtil.encodeScript(StringUtil.decodeScript(get(index).getOriginalName()));
        return oriName;
    }


    public List<String> getSaveFilenames() {
        List<String> saveNames = new ArrayList<>();
        for (FileNameVO vo : this) {
            String name = vo.getSaveName();
            if (StringUtil.isNotEmpty(name))
                saveNames.add(name);
        }
        return saveNames;
    }

    public String getSaveFilename() {
        return getSaveFilenameAt(0);
    }

    public String getSaveFilenameAt(int index) {
        String saveName = null;
        if (size() > index)
            saveName = get(index).getSaveName();
        return saveName;
    }


    public List<FileNameVO> getBothFilenames() {
        return this;
    }

    public FileNameVO getBothFilename() {
        return getBothFilenameAt(0);
    }

    public FileNameVO getBothFilenameAt(int index) {
        if (size() > index)
            return get(index);
        else
            return null;
    }


    public List<File> getFiles() {
        List<File> fileList = new ArrayList<>();
        for (FileNameVO vo : this) {
            File file = vo.getFile();
            if (file != null && file.exists())
                fileList.add(file);
        }
        return fileList;
    }

    public File getFile() {
        return getFileAt(0);
    }

    public File getFileAt(int index) {
        File file = null;
        if (size() > index) {
            file = get(index).getFile();
        }
        return file;
    }


    public String getObjectName() {
        return this.objName;
    }

    public boolean hasFile() {
        return size() > 0;
    }

}
