package com.griffins.common.log.chase;

import com.griffins.common.util.DateUtil;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.StringUtil;

/**
 * 프로젝트명 : esmp
 * 파일명 : griffins.common.util.log.chase.MessageDecorator
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
public class MessageDecorator {
    private Integer logOption;
    private String message;
    private String level;

    public MessageDecorator(String logLevel, Integer logOption) {
        this.level = logLevel;
        this.logOption = logOption;
        this.message = "";
    }

    public MessageDecorator() {
        this.message = "";
    }

    public MessageDecorator start() {
        addHeader();
        return this;
    }

    public MessageDecorator title(String title) {
        addTitle(title);
        return this;
    }

    public MessageDecorator append(String key, String value, String symbol) {
        addPrefix(key, symbol);
        addDesc(value);
        return this;
    }

    public String end() {
        addFooter();

        if (StringUtil.isNotEmpty(level)) {
            int option = StringUtil.isNotEmpty(logOption) ? logOption : LogUtil.CHASING_FILE;
            switch (level) {
                case "trace":
                    LogUtil.trace(message, option);
                    break;
                case "debug":
                    LogUtil.debug(message, option);
                    break;
                case "info":
                    LogUtil.info(message, option);
                    break;
                case "warn":
                    LogUtil.warn(message, option);
                    break;
                case "error":
                    LogUtil.error(message, option);
                    break;
            }
        }

        return message;
    }

    protected void addHeader() {
        message += "\n\n>##############\t" + DateUtil.getNowString("HH:mm:ss") + "\t##############\n";
    }

    protected void addTitle(String title) {
        message += "\n##\t▶ " + title + " ◀";
    }

    protected void addPrefix(String prefix, String symbol) {
        message += "\n#" + symbol + "#\t" + prefix + " : ";
    }

    protected void addDesc(String desc) {
        message += desc;
    }

    protected void addFooter() {
        message += "\n\n<################################################################\n\n";
    }

}
