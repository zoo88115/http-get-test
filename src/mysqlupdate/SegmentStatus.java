/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlupdate;

import java.util.HashMap;
import org.json.JSONArray;

/**
 *
 * @author zoo88115
 */
public class SegmentStatus implements Comparable{
    String segmentName;
    int time,piece;
    public SegmentStatus(String segmentName,int time,int piece){
        this.segmentName=segmentName;
        this.time=time;
        this.piece=piece;
    }
    public void echo(){
        System.out.println(segmentName+"\t"+time+"\t"+piece);
    }
    public String echoString(){
        return segmentName+"\t"+time+"\t"+piece;
    }
    public JSONArray echoJSONArray(){
        JSONArray jsonArray=new JSONArray();
        jsonArray.put(this.segmentName);
        jsonArray.put(this.time);
        jsonArray.put(this.piece);
        return jsonArray;
    }
    
    public void setNewValue(int newTime){
        this.time+=newTime;
        this.piece++;
    }

    @Override
    public int compareTo(Object t) {
        try{
            SegmentStatus temp=(SegmentStatus)t;
            int last=this.segmentName.compareTo(temp.segmentName);
            return last;
        }
        catch(Exception e){
            return 0;
        }
    }
}
