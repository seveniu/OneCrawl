package com.seveniu.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seveniu.template.def.Field;
import com.seveniu.template.def.FieldHtmlType;
import com.seveniu.template.def.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by seveniu on 5/12/16.
 * ParseHtml
 */
public class ParseHtml {
    private Logger logger = LoggerFactory.getLogger(ParseHtml.class);

    private ParseHtml() {
    }

    private ParseResult parseResult;
    private Html html;
    private Template template;
    private String url;

    public static ParseResult parseHtml(String url, Html html, Template template) {

        ParseHtml parseHtml = new ParseHtml();
        parseHtml.url = url;
        parseHtml.html = html;
        parseHtml.template = template;
        parseHtml.parseResult = new ParseResult(url);
        return parseHtml.parsePage();
    }


    /////////////////////////////    parse page    ////////////////////////////

    private ParseResult parsePage() {

        for (Field field : template.getFields()) {

//            String xpath = field.getXpath();
//            if (xpath != null) {
//
//                // 去除 html tag
//
//                String idReg = "(\\/html[\\s\\S]*?\\/)";
//                Pattern pattern = Pattern.compile(idReg);
//                Matcher matcher = pattern.matcher(xpath);
//
//                if (matcher.find()) {
//                    xpath = "//" + xpath.substring(matcher.group().length());
//                }
//            }
            if (parseResult.getParseError() != null) {
                break;
            }
            FieldHtmlType type = FieldHtmlType.getType(field.getHtmlType());
            if (type == null) {
                logger.error("field html type is not found : {}", field.getHtmlType());
                return parseResult;
            }
            switch (type) {
                case TARGET_LINK:
                    parseLinkLabel(field);
                    break;
                case TEXT:
                    parseContent(field);
                    break;
                case NEXT_LINK:
                    parseNextLinkLabel(field);
                    break;
                case TEXT_LINK:
                    parseTextLinkLabel(field);
                    break;
                default:
                    logger.error("field html type is not found : {}", field.getHtmlType());
                    return null;
            }
        }
        return parseResult;
    }


    /////////////////////////////    parse label    ////////////////////////////

    private void parseContent(Field field) {
        String xpath = field.getXpath();
        String content = "";
        if (!(xpath == null || xpath.equals(""))) {
            content = html.xpath(xpath).get();
            if (content != null) {
                try {
                    List<String> regexes = field.getRegex();
                    if (regexes != null) {

                        String temp = content;
                        List<String> result = new ArrayList<>();
                        for (int i = 0; i < regexes.size(); i++) {
                            String regex = regexes.get(i);
                            if (regex != null) {

                                Pattern pattern = Pattern.compile(regex);
                                Matcher m = pattern.matcher(temp);
                                if (i < regexes.size() - 1) {

                                    if (m.find()) {
                                        temp = m.group(1);
                                    } else {
                                        break;
                                    }
                                } else {
                                    while (m.find()) {
                                        result.add(m.group(1));
                                    }
                                }
                            }
                        }

                        ObjectMapper objectMapper = new ObjectMapper();
                        content = objectMapper.writeValueAsString(result);
                    }
                    if (!field.isTrim()) {
                        content = content.trim();
                    }
                } catch (Exception e) {
                    logger.warn("parse content error : {}", e.getMessage());
                    if (field.isMust()) {
                        parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
                    }
                    return;
                }
            } else {
                if (field.isMust()) {
                    parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
                }
            }
        }
        String defaultValue = field.getDefaultValue();
        if (defaultValue == null) {
            defaultValue = "";
        }
        if (content == null || content.length() == 0) {
            content = defaultValue;
        }

        parseResult.addFieldResult(new FieldResult(field.getContentType(), field.getName(), content));
    }


    private void parseLinkLabel(Field field) {
        String xpath = field.getXpath();
        List<String> href = html.xpath(xpath + "/@href").all();
        if (href == null || href.size() == 0) {
            if (field.isMust()) {
                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        }
        if (href != null && href.size() > 0) {
//            List<String> titles = html.xpath(xpath + "/allText()").all();
            for (int i = 0; i < href.size(); i++) {
//                String title = titles.get(i).trim();
                String url = href.get(i).trim();
                if (url.length() > 0) {
                    parseResult.addLink(url);
                }
            }
        }
    }


    private void parseNextLinkLabel(Field field) {
        String xpath = field.getXpath();
        List<String> listUrls = html.xpath(xpath + "/@href").all();
        List<String> listTexts = html.xpath(xpath + "/text()").all();
        if (listUrls == null || listUrls.size() == 0) {
            if (field.isMust()) {
                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        }
        if (listUrls.size() == 1) {
            String nextUrl = listUrls.get(0);
            if (nextUrl.length() > 0) {
                String nextText = listTexts.get(0);
                parseResult.addPageLink(nextUrl);
            }
        } else if (listUrls.size() > 1) {
            int index = findNextLink(listTexts);
            if (index > -1) {
                String nextUrl = listUrls.get(index);
                if (nextUrl.length() > 0) {
                    String nextText = listTexts.get(index);
                    parseResult.addPageLink(nextUrl);
                }
            }
        }
    }

    private void parseTextLinkLabel(Field field) {
        String xpath = field.getXpath();
        List<String> hrefs = html.xpath(xpath + "/@href").all();
        if (hrefs == null || hrefs.size() == 0) {
            if (field.isMust()) {
                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        }
        if (hrefs != null && hrefs.size() > 0) {
            List<String> titles = html.xpath(xpath + "/allText()").all();
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i).trim();
                String url = hrefs.get(i).trim();
                if (url.length() > 0) {
                    parseResult.addFieldResult(new FieldResult(field.getContentType(), field.getName(), "[" + title + "](" + url + ")"));
                }
            }
        }
    }


    private static final String[] nextLinkText = new String[]{"下页", "[下一页]", "下一页", ">", "下页", "next"};
    private static final String[] containerNextLinkText = new String[]{"下一页", "下页", "next"};

    private static int findNextLink(List<String> linkTexts) {
        for (int i = 0; i < linkTexts.size(); i++) {
            String linkText = linkTexts.get(i);
            linkText = linkText.trim();
            for (String s : nextLinkText) {
                if (s.equals(linkText)) {
                    return i;
                }
            }
        }
        for (int i = 0; i < linkTexts.size(); i++) {
            String linkText = linkTexts.get(i);
            linkText = linkText.trim();
            for (String s : containerNextLinkText) {
                if (linkText.contains(s)) {
                    return i;
                }
            }
        }


        return -1;
    }

//    /////////////////////////////    检测是否解析出内容    ////////////////////////////
//
//    /**
//     * 解析 page 后,通过检测 must 为 true 的label 是否有 value 来判断，page 解析的是否正确
//     */
//    private static void isGetResult(PageDefine pageDefine, NodeCreateInfo pr) {
//        List<Field> labels = pageDefine.getLabels();
//        List<String> errorLabel = new ArrayList<>();
//        for (Field label : labels) {
//            if (label.isMust()) {
//                if (label.getHtmlType() == Field.NEXT_LINK)
//                    continue;
//                if (label.getValue() == null) {
//                    System.out.println("label parse error : " + label.getText() + " xpath 错误");
//                    System.out.println("错误的Xpath:" + label.getXpath());
//                    errorLabel.add(label.getText());
//                }
//            }
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            if (errorLabel.size() > 0) {
//                pr.setSuccess(false);
//                pr.setErrorType(NodeCreateInfo.ERROR_LABEL);
//                pr.setMessage(mapper.writeValueAsString(errorLabel));
//            } else {
//                pr.setSuccess(true);
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }

}
