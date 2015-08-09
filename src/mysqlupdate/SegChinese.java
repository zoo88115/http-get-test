/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlupdate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zoo88115
 */
public class SegChinese {
    protected Dictionary dic;
	
    public SegChinese() {
            System.setProperty("mmseg.dic.path", "C:\\Users\\zoo88_000\\Documents\\NetBeansProjects\\ChineseSegmentClient\\build\\classes\\chinesesegmentclient\\data");	//這裡可以指定自訂詞庫
            dic = Dictionary.getInstance();
    }

    protected Seg getSeg() {
            return new ComplexSeg(dic);
    }
    
    public JSONObject segWords(JSONObject jsonObject) throws Exception {
        String wordSpilt=jsonObject.getString("wordSpilt");
	Reader input = new StringReader(jsonObject.getString("article"));
	StringBuilder sb = new StringBuilder();
	Seg seg = getSeg();
	MMSeg mmSeg = new MMSeg(input, seg);
	Word word = null;
	boolean first = true;
	while((word=mmSeg.next())!=null) {
		if(!first) {
			sb.append(wordSpilt);
		}
		String w = word.getString();
		sb.append(w);
		first = false;		
	}
        //System.out.println(sb.toString());
        //印出回傳字串
        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("segments", sb.toString());
	return jsonObject2;
    }
    
    protected JSONArray getSegment(JSONObject jsonObject) throws Exception {//從字串開始一次到底
        ArrayList<SegmentStatus> arrayList = new ArrayList<SegmentStatus>();
        jsonObject.put("wordSpilt", "\t");
	if(jsonObject.getString("article").length() == 0) {
                jsonObject.put("article", "執行失敗，如果未被取代");
	}
	JSONObject jsonArray=segWords(jsonObject);
        String tempArray[]=jsonArray.getString("segments").split("\t");
//        for(String d:tempArray)
//            System.out.print(d+"\t");
//        //印出切割後字串，查看是否正確
        Arrays.sort(tempArray);
        for(int i=0;i<tempArray.length;i++){
            int count=1;
            int k=i;
            for(int j=i+1;j<tempArray.length;j++){
                if(tempArray[i].equals(tempArray[j])==true){
                    count++;
                    k=j;//如果有重複  外層回圈可以跳過
                }
                else 
                    break;
            }
            arrayList.add(new SegmentStatus(tempArray[i],count,1));
            i=k;
        }
        Collections.sort(arrayList);//排序
        JSONArray jsonArray2=new JSONArray();
        for(int i=0;i<arrayList.size();i++)
            jsonArray2.put(arrayList.get(i));
        return jsonArray2;
    }
    protected JSONArray getAfterSegment(JSONObject temp) throws Exception {//接收以tab分開的字串做斷詞
        
        ArrayList<SegmentStatus> arrayList = new ArrayList<SegmentStatus>();
        String tempArray[]=temp.getString("segments").split("\t");
        Arrays.sort(tempArray);
        for(int i=0;i<tempArray.length;i++){
            int count=1;
            int k=i;
            for(int j=i+1;j<tempArray.length;j++){
                if(tempArray[i].equals(tempArray[j])==true){
                    count++;
                    k=j;//如果有重複  外層回圈可以跳過
                }
                else 
                    break;
            }
            arrayList.add(new SegmentStatus(tempArray[i],count,1));
            i=k;
        }
        Collections.sort(arrayList);//排序
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<arrayList.size();i++)
            jsonArray.put(arrayList.get(i));
        return jsonArray;
    }
}
