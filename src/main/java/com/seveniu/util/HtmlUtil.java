package com.seveniu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by seveniu on 5/25/16.
 * HtmlUtil
 */
public class HtmlUtil {
    public static String getPlainText(String html) {
        html = delHTMLTagsWithContent(html);
        html = removeIdAndClass(html);
        html = delAllTMLTags(html);
        html = delHtmlBlankLines(html);
        return html;
    }
    public static String getSimpleHtml(String html) {
        html = delHTMLTagsWithContent(html);
        html = removeIdAndClass(html);
        html = delHtmlBlankLines(html);
        return html;
    }

    private static String delAllTMLTags(String html) {
        return html.replaceAll("(?is)<.*?>", "");
    }

    private static String delHTMLTagsWithContent(String html) {

        html = html.replaceAll("(?is)<!DOCTYPE.*?>", "");
        html = html.replaceAll("(?is)<!--.*?-->", "");                // remove html comment
        html = html.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
        html = html.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
        html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");            // remove special char
        return html;
    }

    private static Pattern delAttr = Pattern.compile("<(\\w+)[\\s\\S]*?([\\/]*)>");

    private static String[] retainClassAndId = new String[]{
            "a",
            "img",
            "table",
            "thead",
            "tbody",
            "tr",
            "td",
            "tfoot",
    };

    private static String removeIdAndClass(String html) {
        Matcher m = delAttr.matcher(html);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String tag = m.group(1).trim();
            if (arrayContain(retainClassAndId, tag))
                continue;
            m.appendReplacement(sb, "<" + m.group(1) + m.group(2) + ">");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static boolean arrayContain(String[] array, String r) {
        for (String str : array) {
            if (str.equals(r)) {
                return true;
            }
        }
        return false;
    }

    private static String delHtmlBlankLines(String html) {
        return html.replaceAll("(?m)^[ \t]*\r?\n", "");
    }
}
