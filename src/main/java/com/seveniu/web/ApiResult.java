package com.seveniu.web;

import com.seveniu.util.Json;

import java.util.List;
import java.util.Map;

/**
 * Created by seveniu on 5/20/16.
 * ApiResult
 */
public class ApiResult {

    /*
    code说明

    第1 位代表系统或服务级：1是系统级,2是服务级
    第2,3位代表模块编
    第4,5,6位为错误标识
    .(点)后面的是错误细分
     */
    public static final String SUCCESS = "200000";// 操作成功
    public static final String A = "200001";// 服务端验证-全局提示
    public static final String B = "200002";// 服务端验证-字段验证
    public static final String PARAM_ERROR = "200400";// 参数错误
    public static final String EXIST = "200400.1";// 参数错误
    public static final String NOT_FOUND = "200404";// 该页面不存在
    public static final String PERMISSIOND_ENIED = "200403";// 没有权限
    public static final String PERMISSIOND_ENIED_PASSWORD_CHANGE = "200403.11";// 没有权限：密码更改而导致无权查看
    public static final String PERMISSIOND_ENIED_LOGOFF = "200403.13";// 没有权限：管理员注销了当前用户
    public static final String PERMISSIOND_ENIED_NO_SESSION = "200403.17";// 没有权限：session失效
    public static final String PERMISSIOND_ENIED_LOGIN_OTHER = "200403.18";// 没有权限：当前用户在别的终端登录，被注销
    public static final String EXCEPTION = "100500";// 出现异常

    private String code;
    private String message;
    private Result result;
    private Page page;
    private Map<String, List<Validation>> fields;

    public String getCode() {
        return code;
    }

    public ApiResult setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public ApiResult setResult(Result result) {
        this.result = result;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public ApiResult setPage(Page page) {
        this.page = page;
        return this;
    }

    public Map<String, List<Validation>> getFields() {
        return fields;
    }

    public ApiResult setFields(Map<String, List<Validation>> fields) {
        this.fields = fields;
        return this;
    }

    public static class Page {
        private int current;
        private int pagesize;
        private int total;

        public Page(int current, int pagesize, int total) {
            this.current = current;
            this.pagesize = pagesize;
            this.total = total;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPagesize() {
            return pagesize;
        }

        public void setPagesize(int pagesize) {
            this.pagesize = pagesize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class Result<T> {
        private Map<String, Object> summary;
        private List<T> items;

        public Result(List<T> items) {
            this.items = items;
        }

        public Map<String, Object> getSummary() {
            return summary;
        }

        public void setSummary(Map<String, Object> summary) {
            this.summary = summary;
        }

        public List<T> getItems() {
            return items;
        }

        public void setItems(List<T> items) {
            this.items = items;
        }
    }

    public static class Validation {
        String code;
        String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static ApiResult get() {
        return new ApiResult();
    }

    public static ApiResult success() {
        return ApiResult.get().setCode(SUCCESS);
    }

    public static ApiResult exception(Exception e) {
        return ApiResult.get().setCode(EXCEPTION).setMessage(e.getMessage());
    }

    public String toJson() {
        return Json.toJson(this);
    }
}
