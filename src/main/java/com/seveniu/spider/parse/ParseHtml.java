package com.seveniu.spider.parse;

import com.seveniu.template.def.Field;
import com.seveniu.template.def.FieldType;
import com.seveniu.template.def.Template;
import com.seveniu.util.HtmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

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
        // 去除 script
        html.getDocument().getElementsByTag("script").remove();
        return parseHtml.parsePage();
    }


    /////////////////////////////    parse page    ////////////////////////////

    private ParseResult parsePage() {
        if (template == null) {
            logger.error("url : {} ,template is null", url);
            parseResult.setParseError(new ParseError(null, ParseErrorType.TEMPLATE_ERROR));
            return parseResult;
        }
        if (template.getFields() == null) {
            logger.error("url : {} ,template field is null", url);
            parseResult.setParseError(new ParseError(null, ParseErrorType.TEMPLATE_ERROR));
            return parseResult;
        }

        for (Field field : template.getFields()) {
            if (parseResult.getParseError() != null) {
                break;
            }
            FieldType type = FieldType.getType(field.getType());
            if (type == null) {
                logger.error("field html type is not found : {}", field.getType());
                parseResult.setParseError(new ParseError(field, ParseErrorType.NO_FIELD_TYPE));
                return parseResult;
            }
            switch (type) {
                case TARGET_LINK:
                    parseLinkLabel(field);
                    break;
                case HTML_TEXT:
                    parseContent(field);
                    break;
                case NEXT_LINK:
                    parseNextLinkLabel(field);
                    break;
                case TEXT_LINK:
                    parseTextLinkLabel(field);
                    break;
                case PURE_TEXT:
                    parsePureContent(field);
                    break;
                case DOWNLOAD_LINK:
                    parseDownloadLinkLabel(field);
                    break;
                default:
                    logger.error("field html type is not found : {}", field.getType());
                    return null;
            }
        }
        if (!parseResult.hasError() && !parseResult.isMatchField()) {
            parseResult.setParseError(new ParseError(null, ParseErrorType.NO_FIELD_MATCH));
        }
        return parseResult;
    }


    /////////////////////////////    parse label    ////////////////////////////
    private Selectable parseBase(Field field) {
        String xpath = field.getXpath();
        if (xpath != null && xpath.length() > 0) {
            Selectable selectable = html.xpath(xpath);
            if (selectable.match()) {

                List<String> regexes = field.getRegex();
                if (regexes != null && regexes.size() > 0) {
                    for (String regex : regexes) {
                        selectable = selectable.regex(regex);
                    }
                }
            } else {
                if (field.isMust()) {
                    parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
                }
            }
            return selectable;
        }

        return null;
    }

    private void parseContent(Field field) {
        Selectable selectable = parseBase(field);
        if (selectable == null || !selectable.match()) {
            return;
        }
        String content = selectable.get();
        if (content == null || content.length() == 0) {
            content = field.getDefaultValue();
        }
        parseResult.addFieldResult(new FieldResult(field.getId(), field.getType(), field.getName(), content));
    }

    private void parsePureContent(Field field) {
        Selectable selectable = parseBase(field);
        if (selectable == null || !selectable.match()) {
            return;
        }
        String content = selectable.get();
        content = HtmlUtil.getPlainText(content);
        if (content == null || content.length() == 0) {
            content = field.getDefaultValue();
        }
        parseResult.addFieldResult(new FieldResult(field.getId(), field.getType(), field.getName(), content));
    }

    private void parseLinkLabel(Field field) {
        Selectable selectable = parseBase(field);
        if (selectable == null) {
            return;
        }
        List<String> hrefs = null;
        List<String> texts = null;
        if (selectable instanceof HtmlNode) {
            hrefs = selectable.links().all();
            texts = selectable.xpath("/text()").all();
        } else if (selectable instanceof PlainText) {
            hrefs = selectable.all();
        }
        if (hrefs == null || hrefs.size() == 0) {
            if (field.isMust()) {
                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        } else {
            for (int i = 0; i < hrefs.size(); i++) {
                String href = hrefs.get(i).trim();
                String text = "";
                if (texts != null) {
                    try {
                        text = texts.get(i);
                    } catch (Exception ignored) {
                    }
                }
                if (href.length() > 0) {
                    parseResult.addLink(new Link(text, href));
                }
            }
        }
    }


    /**
     * 不接受 regex 处理
     */
    private void parseNextLinkLabel(Field field) {
        String xpath = field.getXpath();
        List<String> listUrls = html.xpath(xpath + "/@href").all();
        List<String> listTexts = html.xpath(xpath + "/text()").all();
        if (listUrls == null || listUrls.size() == 0) {
            if (field.isMust()) {
                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
            }
        } else {

            if (listUrls.size() == 1) {
                String nextUrl = listUrls.get(0);
                if (nextUrl.length() > 0) {
                    parseResult.addPageLink(new Link(listTexts.get(0), nextUrl));
                }
            } else {
                int index = findNextLink(listTexts);
                if (index > -1) {
                    String nextUrl = listUrls.get(index);
                    if (nextUrl.length() > 0) {
                        parseResult.addPageLink(new Link(listTexts.get(index), nextUrl));
                    }
                }
            }
        }
    }

    /**
     * 不接受 regex 处理
     */
    private void parseDownloadLinkLabel(Field field) {
        parseTextLinkLabel(field);
    }

    /**
     * 不接受 regex 处理
     */
    private void parseTextLinkLabel(Field field) {
        String xpath = field.getXpath();
        Selectable selectable = html.xpath(xpath);
        if (selectable != null && selectable.match()) {
            List<String> hrefs = selectable.xpath("//a/@href").all();
            if (hrefs != null && hrefs.size() > 0) {
                List<String> titles = selectable.xpath("//a/allText()").all();
                List<Link> links = new ArrayList<>(titles.size());
                for (int i = 0; i < titles.size(); i++) {
                    String title = titles.get(i).trim();
                    String url = hrefs.get(i).trim();
                    if (url.length() > 0) {
                        links.add(new Link(title, url));
                    }
                }
                parseResult.addFieldResult(new FieldResult(field.getId(), field.getType(), field.getName(), links));
            }
        } else {
            if (field.isMust()) {
                parseResult.setParseError(new ParseError(field, ParseErrorType.NOT_FOUND_XPATH));
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
//                if (label.getType() == Field.NEXT_LINK)
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
