package com.seveniu.util;


import javax.servlet.http.HttpSession;

/**
 * Remark:
 * <p/>
 * Author: Tim (Xuan Ma)
 * Date: 12/9/13 15:15
 */
public class SessionUtil {

    public static final String SESSION_LOGIN_VALID = "SESSION_LOGIN_VALID";
    public static final String SESSION_USER_NAME = "SESSION_USER_NAME";
    public static final String SESSION_USER_ID = "SESSION_USER_ID";
    public static final String SESSION_USER_GROUP = "SESSION_USER_GROUP";
    public static final String SESSION_IS_ADMIN = "SESSION_IS_ADMIN";
    public static final String SESSION_IS_OUTER = "SESSION_IS_OUTER";

    public static boolean isValid(HttpSession session) {
        try {
            return ((Boolean) session.getAttribute(SESSION_LOGIN_VALID));
        } catch (Exception e) {
            return false;
        }
    }

    public static void login(HttpSession session, int userId) {
        session.setAttribute(SESSION_USER_ID, userId);
        session.setAttribute(SESSION_LOGIN_VALID, true);
    }
    public static void login(HttpSession session, String username, int userId,  boolean isAdmin, boolean isOuter) {
        session.setAttribute(SESSION_USER_NAME, username);
        session.setAttribute(SESSION_USER_ID, userId);
        session.setAttribute(SESSION_IS_ADMIN, isAdmin);
        session.setAttribute(SESSION_IS_OUTER, isOuter);
        session.setAttribute(SESSION_LOGIN_VALID, true);
    }
    
    public static void destroy(HttpSession session) {
        session.removeAttribute(SESSION_USER_NAME);
        session.removeAttribute(SESSION_USER_ID);
        session.removeAttribute(SESSION_USER_GROUP);
        session.removeAttribute(SESSION_LOGIN_VALID);
        session.removeAttribute(SESSION_IS_ADMIN);
        session.removeAttribute(SESSION_IS_OUTER);
    }

    public static void dump(HttpSession session) {
        System.out.println(session.getAttribute(SESSION_LOGIN_VALID));
        System.out.println(session.getAttribute(SESSION_USER_NAME));
        System.out.println(session.getAttribute(SESSION_USER_ID));
        System.out.println(session.getAttribute(SESSION_USER_GROUP));
        System.out.println(session.getAttribute(SESSION_IS_ADMIN));
        System.out.println(session.getAttribute(SESSION_IS_OUTER));
    }

    public static String username(HttpSession session) {
        return (String) session.getAttribute(SESSION_USER_NAME);
    }

    public static int userId(HttpSession session) {
        return (int) session.getAttribute(SESSION_USER_ID);
    }

    public static boolean isAdmin(HttpSession session) {
        try {
            return (Boolean) session.getAttribute(SESSION_IS_ADMIN);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasGroup(HttpSession session) {
        try {
            return (Boolean) session.getAttribute(SESSION_IS_ADMIN);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isOuter(HttpSession session) {
        try {
            return (Boolean) session.getAttribute(SESSION_IS_OUTER);
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean hasAuth(HttpSession session, String uri) {
//        uri = uri.replace("/c/","/");
//        if (uri.startsWith("/flame")) return true;
//        Set<GmAdminFunc> groups = (Set<GmAdminFunc>) session.getAttribute(SESSION_USER_GROUP);
//        for (GmAdminFunc group : groups) {
//            if (uri.startsWith(group.str())) return true;
//        }
        return false;
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
