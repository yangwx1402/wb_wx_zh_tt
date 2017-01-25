package com.letv.sf.jd;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.http.HttpResult;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/19.
 */
public class JsonClientTest {

    private static String user_agent = SpiderConfig.getCrawlerUserAgent();

    private static final int timeout = 3000;

    private static Connection getConnection(String url, Map<String, String> cookies) {
        Connection connection = HttpConnection.connect(url).timeout(timeout).ignoreContentType(true);
        if (!StringUtils.isBlank(user_agent)) {
            connection.userAgent(user_agent);
        }

        if (cookies != null && cookies.size() > 0) {
            connection.cookies(cookies);
        }
        return connection;
    }

    private static HttpResult sendRequest(Connection connection) throws IOException {
        Connection.Response response = connection.execute();
        HttpResult result = new HttpResult();
        result.setCode(response.statusCode());
        result.setContent(response.body());
        return result;
    }

    public static String fetchUrl(String url, String cookie) throws IOException {
        String[] temp = cookie.split(";");
        String[] temp1 = null;
        Map<String, String> cookies = new HashMap<>();
        for (String tt : temp) {
            int index = tt.indexOf("=");
            String key = tt.substring(0,index);
            String value = tt.substring(index+1,tt.length());
            cookies.put(key, value);
        }
        Connection connection = getConnection(url, cookies);
        HttpResult result = sendRequest(connection);
        return result.getContent();
    }

    public static String getJdTalkList() throws IOException {
        String url = "http://kf.jd.com/waiterSession/queryList.action?page=1&pageSize=15&startTime=2017-01-18&endTime=2017-01-18&sessionType=&sessionStatus=&degree=&customerPin=&servicePin=%E4%B9%90%E8%A7%86-%E5%90%BE%E9%9B%81";
        String cookie = "unpl=V2_ZzNsbUNVQBFwXRQHZ0sOUGIfF1VDX19FIlgVSCkcWQJjAEIOF1JHQWlJKFRzEVQZJkB8VUFLE1AbCEZQfClfBGcFDlxCS0MXZAhBX3sdVwBvMxJtQlZCFHQLQFdyGGw1VwMQbUNnQxRxCkRVeRpdAlcHE1xFUUESdBRABnIdQABkVhBBEQITFWkPRQZ6S14BNwpBDUFnRRM%3D; __jdv=122270672|kong|t_1000027280_1156|zssc|03254dcb-ccd4-4909-afab-c44652abd45e-p_1999-pr_92-at_1156|1484295323968; user-key=0af339fe-bafc-40fa-9e2b-ae2214d26de8; cn=0; __jda=122270672.1053303206.1478744297.1484640039.1484789642.3; __jdb=122270672.1.1053303206|3.1484789642; __jdc=122270672; _jrda=2; _jrdb=1484789643401; 3AB9D23F7A4B3C9B=VXVBDE37PLZYYE6UX2BDDXKV4WQUCV2NSPZKVJFTQFT7BYJVVJ2EVRQ7NQ3S35W7WD7HL4PBV2EXE5Z6PRDN2WG3H4; __jdu=1053303206; TrackID=1LKUmVKxh_qZ8FwPJusWIZpWe6f1kZ3hLmkio9-yDDxI5l7m5B2GCKu7Bls9Tj3bNrWcofNRTJ1EdFabEKhA-eNSbApdvpuMhVKz3UaEDApQ; pinId=7Qvlbtti21C-oBQjYOw0VA; pin=%E4%B9%90%E8%A7%86%E6%97%97%E8%88%B0%E5%BA%97; unick=%E5%9C%9F%E8%B1%86%E8%A5%BF%E5%85%B0%E8%8A%B1; thor=FB4716A72292709EA574882CECFEA6D234D74BEB6D02D59735611C64503A770457D04855CC5AA10CAC3E82ECE217EE9CB32F208C978AB58D5B2AAEACFA846C1336FB6A978D27312023A561408D97A9DBE47AD3338B341C2448E58E9E3031D622624BF5310F45C75098616245C954D34DA3108DFAFA9A9055B237B2A74CE87C80; _tp=UKkaqPJp2AFLg4NJI1xtj%2FIkiD1sMbaom2UYoNEFlKTk7ddRyvVjjJkat2DlqIVc; logining=1; _pst=%E4%B9%90%E8%A7%86%E6%97%97%E8%88%B0%E5%BA%97; ceshi3.com=53yZbxsrc-ul6BgPEReC0rHMsn6XpEE4iinKetB4gx3AowiSYTB8JP22hH_-WeUl; JSESSIONID=04FE92F6E7E6151F7F9486D92A724B98.s1";
        return fetchUrl(url, cookie);
    }

    public static String getJdTalkDetail() throws IOException {
        String url = "http://kf.jd.com/waiterSession/queryChatLog?cid=43bd31983bf24a2ed97d91836da6d2ed";
        String cookie = "unpl=V2_ZzNsbUNVQBFwXRQHZ0sOUGIfF1VDX19FIlgVSCkcWQJjAEIOF1JHQWlJKFRzEVQZJkB8VUFLE1AbCEZQfClfBGcFDlxCS0MXZAhBX3sdVwBvMxJtQlZCFHQLQFdyGGw1VwMQbUNnQxRxCkRVeRpdAlcHE1xFUUESdBRABnIdQABkVhBBEQITFWkPRQZ6S14BNwpBDUFnRRM%3D; __jdv=122270672|kong|t_1000027280_1156|zssc|03254dcb-ccd4-4909-afab-c44652abd45e-p_1999-pr_92-at_1156|1484295323968; user-key=0af339fe-bafc-40fa-9e2b-ae2214d26de8; cn=0; __jda=122270672.1053303206.1478744297.1484789642.1484809945.4; __jdb=122270672.1.1053303206|4.1484809945; __jdc=122270672; _jrda=3; _jrdb=1484809946343; 3AB9D23F7A4B3C9B=VXVBDE37PLZYYE6UX2BDDXKV4WQUCV2NSPZKVJFTQFT7BYJVVJ2EVRQ7NQ3S35W7WD7HL4PBV2EXE5Z6PRDN2WG3H4; TrackID=1B0SZCZhA2QhOuL0tJD1unMOX2kZCKnzAjsXcxCt7404Cp_VoY4l-7FGRwY3z8cKLIBZK4nvSHbvDvX-7UtTiVaNjbHbAL70z3EPV_culE3U; pinId=7Qvlbtti21C-oBQjYOw0VA; pin=%E4%B9%90%E8%A7%86%E6%97%97%E8%88%B0%E5%BA%97; unick=%E5%9C%9F%E8%B1%86%E8%A5%BF%E5%85%B0%E8%8A%B1; thor=68A7F8EEC397689E5C0512CC9670281C50EA6D65AEF0B90C0DAF0CC51B5C26206A041135ABB7DF8E9EA0C0D859432C1C5DE2F254EA2BCA81F4F4C56B1CB0126238A28273C122A5F0529569ED5D696B7188DB67FE86755453A9DE0C8F90FFB7FB244DC2B1192823043DA04CF5E92E34A22A76809B3CC10CC1E06EB056EBFC7016; _tp=UKkaqPJp2AFLg4NJI1xtj%2FIkiD1sMbaom2UYoNEFlKTk7ddRyvVjjJkat2DlqIVc; logining=1; _pst=%E4%B9%90%E8%A7%86%E6%97%97%E8%88%B0%E5%BA%97; ceshi3.com=53yZbxsrc-ul6BgPEReC0rHMsn6XpEE4iinKetB4gx3AowiSYTB8JP22hH_-WeUl; __jdu=1053303206; JSESSIONID=DCAC407CC6B901A2975ABF90D5FAA540.s1";
        return fetchUrl(url,cookie);
    }

    public static String getTaobaoCustomList() throws IOException {
        String url = "https://zizhanghao.taobao.com/subaccount/monitor/chatRecordJson.htm?action=/subaccount/monitor/ChatRecordQueryAction&eventSubmitDoQueryChatRealtion=anything&_tb_token_=f33e1e753a58d&_input_charset=utf-8&chatRelationQuery=%7B%22employeeNick%22%3A%22%E4%B9%90%E8%A7%86%E5%AE%98%E6%96%B9%E6%97%97%E8%88%B0%E5%BA%97%3A%E6%8F%B4%E7%88%B1%22%2C%22customerNick%22%3A%22%22%2C%22start%22%3A%222017-01-12%22%2C%22end%22%3A%222017-01-19%22%2C%22beginKey%22%3Anull%2C%22endKey%22%3Anull%2C%22employeeAll%22%3Afalse%2C%22customerAll%22%3Atrue%7D&_=1484810845972";
        String cookie = "cna=AMeqED4tZUICAXt9GuKTIEMQ; thw=cn; _tb_token_=f33e1e753a58d; x=2043230365; uc3=sg2=U7Af3epKKemwJ1DVq%2Fe922foPAJRp5htOaXRyN5ABjg%3D&nk2=&id2=&lg2=; uss=AV1w64A7Ztf1PwIkt1oX0Sw%2F%2Bro6ZhyXkZpt21XCsib0pW%2FiNrD09bjB; tracknick=; sn=%E4%B9%90%E8%A7%86%E5%AE%98%E6%96%B9%E6%97%97%E8%88%B0%E5%BA%97%3A%E8%92%99%E5%A4%9A; skt=22d2a45377746039; v=0; cookie2=17b6cd1de57806882606fa571e74d2ca; unb=2612537007; t=54a0371132a9690937d05963e5bbd7e1; uc1=cookie14=UoW%2FWXmsiJ2ZCA%3D%3D&lng=zh_CN; l=As3NHzJulUDqJowSmMOhFCnrXeNBWQFu; isg=AvDwKR08XHPMUADMbq_gLugmwb70__16L4_PRupHT8sMpYtPlUnpEvXTixo_";
        return fetchUrl(url,cookie);
    }

    public static String getTaobaoTalkDetail() throws IOException {
        String url = "https://zizhanghao.taobao.com/subaccount/monitor/chatRecordHtml.htm?action=/subaccount/monitor/ChatRecordQueryAction&eventSubmitDoQueryChatContent=anything&_tb_token_=f33e1e753a58d&_input_charset=utf-8&chatContentQuery=%7B%22employeeUserNick%22%3A%5B%22cntaobao%E4%B9%90%E8%A7%86%E5%AE%98%E6%96%B9%E6%97%97%E8%88%B0%E5%BA%97%3A%E6%8F%B4%E7%88%B1%22%5D%2C%22customerUserNick%22%3A%5B%22cntaobaobcjhsy%22%5D%2C%22employeeAll%22%3Afalse%2C%22customerAll%22%3Afalse%2C%22start%22%3A%222017-01-12%22%2C%22end%22%3A%222017-01-19%22%2C%22beginKey%22%3Anull%2C%22endKey%22%3Anull%7D&_=1484810952741";
        String cookie = "cna=AMeqED4tZUICAXt9GuKTIEMQ; thw=cn; _tb_token_=f33e1e753a58d; x=2043230365; uc3=sg2=U7Af3epKKemwJ1DVq%2Fe922foPAJRp5htOaXRyN5ABjg%3D&nk2=&id2=&lg2=; uss=AV1w64A7Ztf1PwIkt1oX0Sw%2F%2Bro6ZhyXkZpt21XCsib0pW%2FiNrD09bjB; tracknick=; sn=%E4%B9%90%E8%A7%86%E5%AE%98%E6%96%B9%E6%97%97%E8%88%B0%E5%BA%97%3A%E8%92%99%E5%A4%9A; skt=22d2a45377746039; v=0; cookie2=17b6cd1de57806882606fa571e74d2ca; unb=2612537007; t=54a0371132a9690937d05963e5bbd7e1; uc1=cookie14=UoW%2FWXmsiJwVCw%3D%3D&lng=zh_CN; l=Ao2N0vIu1YCq5szS2APh1Gm-HaMNLMEp; isg=AlRUCpmI8IcACWRwarsc2jR6JZJwa4dSY0PrGu4_pl932ekjHbxJJlofr2Y7";
        return fetchUrl(url,cookie);
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(getJdTalkList());
        //System.out.println(getJdTalkDetail());
        //System.out.println(getTaobaoCustomList());
        System.out.println(Jsoup.parse(getTaobaoTalkDetail()).text());

    }

}
