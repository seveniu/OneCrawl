import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine

/**
 * User: seveniu
 * Date: 11/24/15
 * Time: 12:17
 * Project: DhlzWeb
 *
 */

String parseAuthor(def authors) {
    def html = ''
    if (authors) {
        authors.each {
            html += '<a href="#">' + it + '</a>'
        }
    }
    return html;
}

String parseDate(def date) {
    if (date) {
        return '<span>日期：</span>' + date;
    }
    return '';
}

String parseSource(def source) {
    if (source) {
        return '<a href="http://' + source.domain + '">' + source.name + '</a>';
    }
    return '';
}

String parseTags(def tags) {
    def html = '';
    if (tags) tags.each { html += '<a href="#">' + it.name + '</a>' }
    return html;
}

String parseCategories(def categories) {
    def html = '';
    if (categories) categories.each { html += '<a href="#">' + it.name + '</a>' }
    return html;
}

String parseClassified(def classified) {
    def html = '';
    if (classified) classified.each { html += '<a href="#">' + it.name + '</a>' }
    return html;
}

String parseAuthorCompany(def authorCompany) {
    def html = '';
    if (authorCompany) {
        html += '<tr> <td> <div class="td_1">工作单位</div> </td><td> <div class="td_2"> ';
        authorCompany.each {
            html += '<a href="#">' + it + '</a>'
        }
        html += '</div> </td> </tr>'
    }
    return html;
}

String parseCountry(def country) {
    if (country) {
        return '<tr> <td> <div class="td_1">国家</div> </td><td> <div class="td_2"> <a href="#">' + country + '</a></div> </td> </tr>'
    }
    return ''
}

String parseLanguage(def language) {
    if (language) {
        return '<tr> <td> <div class="td_1">语言</div> </td><td> <div class="td_2"> <a href="#">' + language + '</a></div> </td> </tr>'
    }
    return ''
}

String parseUrl(def url) {
    if (url) {
        return '<tr> <td> <div class="td_1">原文链接</div> </td><td> <div class="td_2"> <a href="url">' + url + '</a></div> </td> </tr>'
    }
    return ''
}

String parseUrl1(def url) {
    if (url) {
        return '<tr> <td> <div class="td_1">原文链接2</div> </td><td> <div class="td_2"> <a href="url">' + url + '</a></div> </td> </tr>'
    }
    return ''
}

String parseAbstract(def abstractStr) {
    if (abstractStr) {
        return '<tr> <td> <div class="td_1">摘要</div> </td><td> <div class="td_2">'+abstractStr+'</div> </td> </tr>'
    }
    return ''
}

String parsePeriodical(def periodical) {
    if (periodical) {
        return '<tr><td><div class="td_1">出版品名称</div></td>' +
                '<td><div class="td_2"><a href="#">' + periodical.name + '</a></div></td></tr>' +
                '<tr><td><div class="td_1">ISBN</div></td>' +
                '<td><div class="td_2">' + periodical.issn + '</div></td></tr>' +
                '<tr><td><div class="td_1">期次</div></td>' +
                '<td><div class="td_2"><span>' + periodical.issueYear + '年</span><a href="#">' + periodical.issue + '</a></div></td></tr>'
    }
    return ''
}

//def parsePage(def page) {
//    if (page) {
//        return '<div class="pageTitle">' + page.title + '</div>' +
//                '<div class="pageUrl">' + page.url + '</div>' +
//                '<div class="pageContent">' + page.content + '</div>'
//    }
//    return ""
//}

List<String> parseContent(String node) {
    JsonSlurper jsonSlurper = new JsonSlurper();
//    node = '{"abstract":null,"tags":[{"id":8483,"name":"到原网页看","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":18,"weight":-1,"descr":"","synonym":"","webix_kids":0},{"id":8799,"name":"中国人民大学","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":20,"weight":-1,"descr":null,"synonym":null,"webix_kids":0}],"id":11492658,"author":["来源：清史所　作者：清史所　点击数：1818","　更新时间：2007-7-2"],"title":"文本阅读与研讨测试","source":{"id":2116,"name":"人大清史研究所","domain":"www.iqh.net.cn"},"nodes":[{"contentId":11492658,"id":"11492658-[0]","url":"http://www.iqh.net.cn/info.asp?column_id=2445","title":"文本阅读与研讨测试","pages":[{"id":"11492658-[0]-0","url":"http://www.iqh.net.cn/info.asp?column_id=2445","title":"文本阅读与研讨测试","content":"<tr> \\n<td height=\\"710\\" valign=\\"top\\" class=\\"info\\"></td> \\n</tr>","author":["来源：清史所　","点击数：1818　更新时间：2007-7-2"],"date":"来源：清史所　作者：清史所　点击数：1818　更新时间：2007-7-2","introduction":null,"abstractStr":null}]}],"classified":[{"id":4,"name":"论文"}],"categories":[{"id":31,"name":"文化","parentId":0,"status":1,"contentCount":0,"htmlType":0,"employeeId":17,"weight":-1,"descr":null,"synonym":null,"webix_kids":10}],"language":null,"date":"来源：清史所　作者：清史所　点击数：1818　更新时间：2007-7-2","url":"http://www.iqh.net.cn/info.asp?column_id=2445","country":null}';
//    node = '{"abstract":"&lt;正&gt;一、托福考试及其题型变更的由来美国普林斯顿教育考试服务处(Educa-tional Testing Service,Princeton)主办的托福(TOEFL)考试自1963年开始迄今已有34载。其目的是衡量非英语国家考生是否具备在英语环境中,尤其是在美国与加拿大接受高等教育的能力。长期以来托福考试内容由三部分组成:听力,语法结构和书面表达能力以及阅读理解能力与词汇量,题型全部为客观性多项选择题。","tags":[{"id":69494,"name":"索引","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":18,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":95671,"name":"英语专业","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96020,"name":"TOEFL","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96021,"name":"测试效度","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96022,"name":"多项选择题","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96023,"name":"非英语国家","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96024,"name":"英语环境","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96025,"name":"书面表达能力","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96026,"name":"英语水平测试","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96027,"name":"普林斯","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0},{"id":96028,"name":"教育考试","parentId":-1,"status":1,"contentCount":0,"htmlType":0,"employeeId":-2,"weight":-1,"descr":null,"synonym":null,"webix_kids":0}],"classified":[{"id":4,"name":"论文"}],"date":null,"url":"http://acad.cnki.net/kns/detail/detail.aspx?filename=TJDS199702019&DBName=cjfdtotal&dbcode=cjfd","country":null,"authorCompany":["同济大学外语系!系主任、教授"],"id":13514,"author":["汪家树"],"title":"从美国TOEFL题型变更谈外语测试效度的意义","source":{"id":4096,"name":"知网期刊","domain":"epub.cnki.net"},"nodes":[{"contentId":13514,"id":"13514-[0]","url":"http://acad.cnki.net/kns/detail/detail.aspx?filename=TJDS199702019&DBName=cjfdtotal&dbcode=cjfd","title":"从美国TOEFL题型变更谈外语测试效度的意义","pages":[{"id":"13514-[0]-0","url":"http://acad.cnki.net/kns/detail/detail.aspx?filename=TJDS199702019&DBName=cjfdtotal&dbcode=cjfd","title":"从美国TOEFL题型变更谈外语测试效度的意义","content":null,"author":"[\\"汪家树\\"]","authorCompany":"[\\"同济大学外语系!系主任、教授\\"]","abstractStr":"&lt;正&gt;一、托福考试及其题型变更的由来美国普林斯顿教育考试服务处(Educa-tional Testing Service,Princeton)主办的托福(TOEFL)考试自1963年开始迄今已有34载。其目的是衡量非英语国家考生是否具备在英语环境中,尤其是在美国与加拿大接受高等教育的能力。长期以来托福考试内容由三部分组成:听力,语法结构和书面表达能力以及阅读理解能力与词汇量,题型全部为客观性多项选择题。","date":null,"introduction":null}]}],"categories":[{"id":27,"name":"语言文字","parentId":0,"status":1,"contentCount":0,"htmlType":0,"employeeId":17,"weight":-1,"descr":null,"synonym":null,"webix_kids":21}],"language":null,"periodical":{"issueId":539,"issue":"02","issueYear":1997,"periodicalId":8,"issn":"1009-3060","name":"同济大学学报(社会科学版)","pressId":8,"pressName":"同济大学"}}';
    def result = jsonSlurper.parseText(node);

    def title = result.title;
    def id = result.id;
    def url = result.url;
    def content = result.content;
    def tags = result.tags.findAll { it.id != 8483 && it.id != 57361 };
//    result.authorCompany = result.authorCompany ? result.authorCompany : null
    def prop = ["authorCompany", "periodical"];
    prop.each { result[it] = result[it] ? result[it] : null }

    result.tags = tags;

    def page = result.pages[0];

    result.favorite = 27;

    def listIndex = result.categories[0].id;

    def binding = [
            title        : result.title,
            author       : parseAuthor(result.author),
            date         : parseDate(result.date),
            source       : parseSource(result.source),
            tags         : parseTags(result.tags),
            categories   : parseCategories(result.categories),
            authorCompany: parseAuthorCompany(result.authorCompany),
            periodical   : parsePeriodical(result.periodical),
            classified   : parseClassified(result.classified),
            country      : parseCountry(result.country),
            language     : parseLanguage(result.language),
            url          : parseUrl(result.url),
            url1         : parseUrl(result.url),
            abstractHtml     : parseAbstract(result.abstract)
    ]

    def pageBind = [
            pageTitle       : page.title,
            pageUrl         : page.url,
            pageContent     : page.content
    ]


    def template = '''
<div class="title">
<a href="#">${title}</a>
</div>
<div>
<div class="sub_title"> <span>作者：</span>${author}</div>
<div class="sub_title"><span>日期：</span>${date}</div>
<div class="sub_title"><span>来源：</span>${source}</div>
</div>
<table class="table_details">
<tr>
<td style='border: 1px solid #ccc;'>
关键词
</td>
<td>

${tags}

</td>
</tr>
<tr>
<td>
学科分类
</td>
<td>

${categories}

</td>
</tr>
${authorCompany}
${periodical}
<tr>
<td>
类型
</td>
<td>

${classified}

</td>
</tr>
${country}
${language}
<tr>
<td>
OCLC
</td>
<td>
<a href="#">检索</a>
</td>
</tr>
${url}
${url1}
${abstractHtml}
</table>
''';

    def pageTemplate = '''<html><body><div>${pageTitle}</div><div>${pageUrl}</div><div><table>${pageContent}</table></div></body></html>''';


    def engine = new SimpleTemplateEngine()
    def t = engine.createTemplate(template).make(binding)
    def pageHtml = engine.createTemplate(pageTemplate).make(pageBind)
//

    def pagesHtml = []
    pagesHtml.add(t.toString());
    pagesHtml.add(pageHtml.toString())
//    result.nodes[0].each {
//        pagesHtml.add(parsePage(it))
//    }
    return pagesHtml
}
