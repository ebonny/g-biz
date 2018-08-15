package com.griffins.common.file;

import com.google.common.collect.Lists;
import com.griffins.common.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 파일명 : griffins.common.model.FileNameManager
 * *
 * 파일객체명(objName) 별로 실제파일명과 저장파일명의 리스트를 저장 및 관리
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
public class FileNamesManager extends HashMap<String, List> {
    private List<FileNameVO> listByName;

    public FileNamesManager() {
        listByName = new ArrayList<>();
    }

    public void addFileNamesWithFile(File file, String objName, String originalName, String saveName) {
        List<FileNameVO> list = get(objName);
        if (list == null) {
            list = new ArrayList<>();
            put(objName, list);
        }
        FileNameVO vo = new FileNameVO();
        vo.setOriginalName(StringUtil.getOnlyFilename(originalName));
        vo.setSaveName(saveName);
        vo.setFile(file);
        list.add(vo);
    }

    public void addFileNames(String objName, String originalName, String saveName) {
        addFileNamesWithFile(null, objName, StringUtil.getOnlyFilename(originalName), saveName);
    }

    public List<String> getOriginalFilenames(String objName) {
        List<FileNameVO> names = get(objName);
        List<String> oriNames = new ArrayList<>();
        if (names != null) {
            for (FileNameVO vo : names) {
                String name = StringUtil.encodeScript(StringUtil.decodeScript(vo.getOriginalName()));
                if (StringUtil.isNotEmpty(name))
                    oriNames.add(name);
            }
        }
        return oriNames;
    }

    public String getOriginalFilename(String objName) {
        return getOriginalFilenameAt(0, objName);
    }

    public String getOriginalFilenameAt(int index, String objName) {
        List<FileNameVO> names = get(objName);
        String oriName = null;
        if (names != null && names.size() > index)
            oriName = StringUtil.encodeScript(StringUtil.decodeScript(names.get(index).getOriginalName()));
        return oriName;
    }


    public List<String> getSaveFilenames(String objName) {
        List<FileNameVO> names = get(objName);
        List<String> saveNames = new ArrayList<>();
        if (names != null) {
            for (FileNameVO vo : names) {
                String name = vo.getSaveName();
                if (StringUtil.isNotEmpty(name))
                    saveNames.add(name);
            }
        }
        return saveNames;
    }

    public String getSaveFilename(String objName) {
        return getSaveFilenameAt(0, objName);
    }

    public String getSaveFilenameAt(int index, String objName) {
        List<FileNameVO> names = get(objName);
        String saveName = null;
        if (names != null && names.size() > index)
            saveName = names.get(index).getSaveName();
        return saveName;
    }


    public List<File> getFiles(String objName) {
        List<FileNameVO> names = get(objName);
        List<File> fileList = new ArrayList<>();
        if (names != null) {
            for (FileNameVO vo : names) {
                File file = vo.getFile();
                if (file != null && file.exists())
                    fileList.add(file);
            }
        }
        return fileList;
    }

    public File getFile(String objName) {
        return getFileAt(0, objName);
    }

    public File getFileAt(int index, String objName) {
        List<FileNameVO> names = get(objName);
        File file = null;
        if (names != null && names.size() > index)
            file = names.get(index).getFile();
        return file;
    }


    public List<FileNameVO> getBothFilenames(String objName) {
        return get(objName);
    }

    public FileNameVO getBothFilename(String objName) {
        return getBothFilenameAt(0, objName);
    }

    public FileNameVO getBothFilenameAt(int index, String objName) {
        List<FileNameVO> list = get(objName);
        if (list != null && list.size() > index)
            return list.get(index);
        else
            return null;
    }


    public List<String> getObjectNames() {
        Set<String> keys = this.keySet();
        return Lists.newArrayList(keys);
    }

    public boolean hasFile(String objName) {
        List<FileNameVO> list = this.get(objName);
        return list != null && list.size() > 0;
    }


    /* 파일을 하나만 업로드할 경우 파일객체명을 굳이 몰라도 아래 메소드들을 사용하면 된다 */
    private String getFirstName() {
        return getObjectNames().get(0);
    }

    public List<String> getOriginalFilenames() {
        List<String> names = getObjectNames();
        List<String> all = new ArrayList<>();
        for (String name : names) {
            all.addAll(getOriginalFilenames(name));
        }
        return all;
    }

    public String getOriginalFilename() {
        return getOriginalFilename(getFirstName());
    }

    public String getOriginalFilenameAt(int index) {
        return getOriginalFilenameAt(index, getFirstName());
    }

    public List<String> getSaveFilenames() {
        List<String> names = getObjectNames();
        List<String> all = new ArrayList<>();
        for (String name : names) {
            all.addAll(getSaveFilenames(name));
        }
        return all;
    }

    public String getSaveFilename() {
        return getSaveFilename(getFirstName());
    }

    public String getSaveFilenameAt(int index) {
        return getSaveFilenameAt(index, getFirstName());
    }

    public List<File> getFiles() {
        List<String> names = getObjectNames();
        List<File> all = new ArrayList<>();
        for (String name : names) {
            all.addAll(getFiles(name));
        }
        return all;
    }

    public File getFile() {
        return getFile(getFirstName());
    }

    public File getFileAt(int index) {
        return getFileAt(index, getFirstName());
    }

    public List<FileNameVO> getBothFilenames() {
        List<String> names = getObjectNames();
        List<FileNameVO> all = new ArrayList<>();
        for (String name : names) {
            all.addAll(getBothFilenames(name));
        }
        return all;
    }

    public FileNameVO getBothFilename() {
        return getBothFilename(getFirstName());
    }

    public FileNameVO getBothFilenameAt(int index) {
        return getBothFilenameAt(index, getFirstName());
    }

}
