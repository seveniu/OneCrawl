package com.seveniu.util;

import com.seveniu.common.str.StrUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientGenerator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: niu
 * Date: 2014/6/18
 * Time: 12:36
 * Project: dhlz-spider
 */
public abstract class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public void execute(String url) {
        CloseableHttpClient httpClient = getHttpClient(Site.me());
        CloseableHttpResponse responseGet;
        HttpGet httpGet = null;

        try {
            // 以get方法执行请求
            httpGet = getHttpGet(url);
            // 获得服务器响应的所有信息
            responseGet = httpClient.execute(httpGet);
            HttpEntity entity = responseGet.getEntity();
            process(entity, url);
        } catch (IOException e) {
//			e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    protected abstract void process(HttpEntity entity, String url);


    public static HttpGet getHttpGet(String strUrl) {
        URI uri = null;
        try {
            uri = new URI(strUrl);
        } catch (Exception e) {
            URL url;
            try {
                url = new URL(strUrl);
                uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            } catch (MalformedURLException | URISyntaxException e1) {
                e1.printStackTrace();
            }

        }
        HttpGet get = new HttpGet(uri);
        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//			get.addHeader("Accept-Charset", "utf-8");
        get.addHeader("Accept-Encoding", "gzip,,deflate,sdch");
        get.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,en-US,en");
        get.addHeader("User-Agent", UserAgent.getUserAgent());
        return get;
    }

    public static CloseableHttpClient getHttpClient1() {

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        // Increase max connections for localhost:80 to 50

        HttpHost localhost = new HttpHost("locahost", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
        return org.apache.http.impl.client.HttpClients.custom()
                .setConnectionManager(cm)
                .setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22")
                .build();
    }


    public static String getCharset(String html) {
        // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        Pattern p = Pattern.compile("<[\\s]*?meta[\\s\\S]*?content=\\s*['\"][\\s\\S]*?charset=([\\d\\w\\W\\-]*?)\\s*['\"]", Pattern.CASE_INSENSITIVE); //html4
        // 2.2、html5 <meta charset="UTF-8" />
        Pattern p2 = Pattern.compile("<[\\s]*?meta[\\s\\S]*?charset\\s*=\\s*['\"]*([^\\s;'\"]*)", Pattern.CASE_INSENSITIVE); //html5
        Matcher m = p.matcher(html);
        if (m.find()) {
            return m.group(1);
        } else {
            m = p2.matcher(html);
            if (m.find())
                return m.group(1);
        }

        return null;
    }

    public static String getHtmlCharset(HttpEntity entity, byte[] contentBytes, String defalutCharset) throws IOException {
        return compareMessyCode(contentBytes);


//        String charset = null;
//        // charset
//        // 1、encoding in http header Content-Type
//        Header header = entity.getContentType();
//        if (header != null) {
//            String value = header.getValue();
//            Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)", Pattern.CASE_INSENSITIVE);
//            Matcher m = patternForCharset.matcher(value);
//            if (m.find()) {
//                charset = m.group(1);
//            }
//        }
//        if (charset != null && StringUtils.isNotBlank(charset)) {
//            charset = charset.trim();
//        } else {// 2、charset in meta
//
//            // use default charset to decode first time
//            Charset defaultCharset = Charset.defaultCharset();
//            String content = new String(contentBytes, defaultCharset.name());
//            if (StringUtils.isNotEmpty(content)) {
//                charset = getCharset(content);
//            }
//        }
//        // 3、todo use tools as cpdetector for content decode
//
//
//        // 4、gb2312 --> gbk
//        if (charset != null && charset.equals("gb2312")) {
//            charset = "gbk";
//        }
//
//        // 5、null --> utf-8
//        if (charset == null) {
//            if (defalutCharset == null)
//                charset = "gbk";
//            else charset = defalutCharset;
//        }
//        return charset;
    }

    public static String compareMessyCode(byte[] contentBytes) {
        String charset = null;

        float utf8MessyCode = 0f;
        String content;
        try {
            content = new String(contentBytes, "UTF-8");
            if (StrUtil.isNotEmpty(content)) {
                utf8MessyCode = MessyCodeCheck.isMessyCode(content);
            }
            float gbkMessyCode = 0f;
            content = new String(contentBytes, "gbk");
            if (StrUtil.isNotEmpty(content)) {
                gbkMessyCode = MessyCodeCheck.isMessyCode(content);
            }
            if (utf8MessyCode > gbkMessyCode) {
                charset = "gbk";
            } else {
                charset = "utf-8";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return charset;
    }

    public static CloseableHttpClient getHttpClient(Site site) {
        HttpClientGenerator httpClientGenerator = new HttpClientGenerator().setPoolSize(10);
        return httpClientGenerator.getClient(site);
    }

    public static HttpResponse getHttpResponse(String url) {

        return getHttpResponse(url, Site.me().setUserAgent(UserAgent.getUserAgent()));
    }

    public static HttpResponse getHttpResponse(String url, Site site) {
        HttpResponse responseGet;
        HttpGet httpGet = null;

        try {
            // 以get方法执行请求
            httpGet = HttpUtil.getHttpGet(url);
            // 获得服务器响应的所有信息
            HttpClient httpClient = getHttpClient(site);

            responseGet = httpClient.execute(httpGet);
            if (responseGet.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            return responseGet;
        } catch (Exception e) {
            logger.info("FILE-DOWNLOAD", "文件下载，出现错误的 url :" + url);
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    public static String getHttpContent(String url, String charset, Site site) throws IOException {
        HttpResponse httpResponse;
        HttpGet httpGet = null;

        try {
            // 以get方法执行请求
            httpGet = HttpUtil.getHttpGet(url);
            for (Map.Entry<String, String> entry : site.getHeaders().entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
            // 获得服务器响应的所有信息
            HttpClient httpClient = getHttpClient(site);

            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                logger.debug( "状态码：" + httpResponse.getStatusLine().getStatusCode());
                return null;
            }
//            HttpResponse httpResponse = getHttpResponse(url);
            if (charset == null) {
                byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
                String htmlCharset = HttpUtil.getHtmlCharset(httpResponse.getEntity(), contentBytes, Charset.defaultCharset().toString());
//                System.out.println("页面编码 ：：： " + htmlCharset);
//                System.out.println(new String(contentBytes,htmlCharset));
//                System.out.println(new String(contentBytes,"UTF-8"));
//                return new String(contentBytes,"UTF-8");
                if (htmlCharset != null) {
                    return new String(contentBytes, htmlCharset);
                } else {
                    logger.debug( "Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()");
                    return new String(contentBytes);
                }
            } else {
                return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
            }
        } catch (Exception e) {
            logger.info("WEB-PAGE-DOWNLOAD", "，出现错误的 url :" + url + "  error : " + e.getMessage());
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.nlkfq.gov.cn/news.asp?lb=%E5%9B%AD%E5%8C%BA%E6%96%B0%E9%97%BB";
        url = "http://www.airitilibrary.com/Publication/alDetailedMesh?DocID=10243131-201407-201409020002-201409020002-63-82";
        String result = HttpUtil.getHttpContent(url, null,
                Site.me()
                        .setUserAgent(UserAgent.getUserAgent())
                        .addHeader("Referer", UrlUtil.getHost(url)));

    }
}
