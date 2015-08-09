/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlupdate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zoo88115
 */
public class MysqlUpdate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        String a = "今年6月休學在家的林姓反課綱微調北高發言人今天上午被家人發現死在臥室地上，疑燒炭輕生，由於未發現遺書，全案正由警方調查中。";
        String b = "警消今天上午8時30分左右接獲報案，家住板橋區民生路20歲男子在家中房間內反鎖，經破門進入房內才發現男子沒生命跡象。";
        JSONObject j1 = new JSONObject();
        JSONObject j2 = new JSONObject();
        j1.put("article", a);
        j1.put("wordSpilt", "\t");
        j2.put("article", b);
        j2.put("wordSpilt", "\t");
        SegChinese seg = new SegChinese();
//        Random rd=new Random();
//        MySQL sql=new MySQL("127.0.0.1","chinesesegment_dictionary","root","1234");
//        for(int i=24493;i<30614;i++){
//            String s="update chinesesegment_dictionary.dictionary set emotion_inner="+rd.nextInt(15)+
//                    ",emotion_outer="+rd.nextInt(15)+
//                    " where seg_dic_id="+i;
//            sql.update(s);
//        }
        JSONArray segment1 = seg.getAfterSegment(seg.segWords(j1));
        JSONArray segment2 = seg.getAfterSegment(seg.segWords(j2));
        JSONArray ss = new JSONArray();
        JSONArray ss2 = new JSONArray();
        for (int i = 0; i < segment1.length(); i++) {
            ss.put(((SegmentStatus) segment1.get(i)).segmentName);
        }
        for (int i = 0; i < segment2.length(); i++) {
            ss2.put(((SegmentStatus) segment2.get(i)).segmentName);
        }
        JSONObject aa = new JSONObject();
        aa.put("segments", ss);
        aa.put("symbols", ss2);
        EmotionScale emotion = new EmotionScale(aa);
        System.out.println("\n\n\n" + emotion.emotionScale());
    }

    // HTTP GET request
    private JSONObject sendGet(String raw) throws Exception {

        String url = "http://203.64.84.29:8080/token/tokenMessage?raw="+raw;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        JSONObject result=new JSONObject(response.toString());
        return result;
    }
}
