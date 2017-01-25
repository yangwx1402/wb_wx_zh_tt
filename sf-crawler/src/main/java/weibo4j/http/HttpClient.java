package weibo4j.http;

import com.letv.sf.http.HttpMethod;
import org.apache.log4j.Logger;
import weibo4j.model.Configuration;
import weibo4j.model.Paging;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;

import java.io.File;
import java.net.URLEncoder;

/**
 * @author sinaWeibo
 */
public class HttpClient implements java.io.Serializable {

    private static final long serialVersionUID = -176092625883595547L;
    private static final int OK = 200;// OK: Success!
    private static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    private static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    private static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    private static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    private static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    private static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Weibo team can investigate.
    private static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or being upgraded.
    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

    private String proxyHost = Configuration.getProxyHost();
    private int proxyPort = Configuration.getProxyPort();
    private String proxyAuthUser = Configuration.getProxyUser();
    private String proxyAuthPassword = Configuration.getProxyPassword();

    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * Sets proxy host. System property -Dsinat4j.http.proxyHost or
     * http.proxyHost overrides this attribute.
     *
     * @param proxyHost
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = Configuration.getProxyHost(proxyHost);
    }

    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets proxy port. System property -Dsinat4j.http.proxyPort or
     * -Dhttp.proxyPort overrides this attribute.
     *
     * @param proxyPort
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = Configuration.getProxyPort(proxyPort);
    }

    public String getProxyAuthUser() {
        return proxyAuthUser;
    }

    /**
     * Sets proxy authentication user. System property -Dsinat4j.http.proxyUser
     * overrides this attribute.
     *
     * @param proxyAuthUser
     */
    public void setProxyAuthUser(String proxyAuthUser) {
        this.proxyAuthUser = Configuration.getProxyUser(proxyAuthUser);
    }

    public String getProxyAuthPassword() {
        return proxyAuthPassword;
    }

    /**
     * Sets proxy authentication password. System property
     * -Dsinat4j.http.proxyPassword overrides this attribute.
     *
     * @param proxyAuthPassword
     */
    public void setProxyAuthPassword(String proxyAuthPassword) {
        this.proxyAuthPassword = Configuration
                .getProxyPassword(proxyAuthPassword);
    }

    private final static boolean DEBUG = Configuration.getDebug();
    static Logger log = Logger.getLogger(HttpClient.class.getName());

    private int maxSize;

    public HttpClient() {
        this(150, 30000, 30000, 1024 * 1024);
    }

    public HttpClient(int maxConPerHost, int conTimeOutMs, int soTimeOutMs,
                      int maxSize) {

    }

    /**
     * log调试
     */
    private static void log(String message) {
        if (DEBUG) {
            log.debug(message);
        }
    }

    /**
     * 处理http getmethod 请求
     */

    public Response get(String url, String token) throws WeiboException {

        return get(url, new PostParameter[0], token);

    }

    public Response get(String url, PostParameter[] params, String token)
            throws WeiboException {
        return null;

    }

    public Response get(String url, PostParameter[] params, Paging paging, String token)
            throws WeiboException {
        return null;
    }

    /**
     * 处理http deletemethod请求
     */

    public Response delete(String url, PostParameter[] params, String token)
            throws WeiboException {
        return null;
    }

    /**
     * 处理http post请求
     */

    public Response post(String url, PostParameter[] params, String token)
            throws WeiboException {
        return post(url, params, true, token);

    }

    public Response post(String url, PostParameter[] params,
                         Boolean WithTokenHeader, String token) throws WeiboException {
        return null;
    }

    /**
     * 支持multipart方式上传图片
     */
    public Response multPartURL(String url, PostParameter[] params,
                                ImageItem item, String token) throws WeiboException {
        return null;
    }

    public Response multPartURL(String fileParamName, String url,
                                PostParameter[] params, File file, boolean authenticated, String token)
            throws WeiboException {
        return null;
    }

    public Response httpRequest(HttpMethod method, String token) throws WeiboException {
        return httpRequest(method, true, token);
    }

    public Response httpRequest(HttpMethod method, Boolean WithTokenHeader, String token)
            throws WeiboException {
        return null;
    }

    /*
     * 对parameters进行encode处理
     */
    public static String encodeParameters(PostParameter[] postParams) {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < postParams.length; j++) {
            if (j != 0) {
                buf.append("&");
            }
            try {
                buf.append(URLEncoder.encode(postParams[j].getName(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(postParams[j].getValue(),
                                "UTF-8"));
            } catch (java.io.UnsupportedEncodingException neverHappen) {
            }
        }
        return buf.toString();
    }


    private static String getCause(int statusCode) {
        return null;
    }


}
