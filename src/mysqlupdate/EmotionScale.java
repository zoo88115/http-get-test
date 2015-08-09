/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlupdate;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zoo88115
 */
public class EmotionScale {
    JSONArray seg,sym;
    public EmotionScale(JSONObject jsonObject)throws Exception{
        seg=jsonObject.getJSONArray("segments");
        sym=jsonObject.getJSONArray("symbols");
        System.out.println(seg);
        System.out.println(sym);
    }
    public JSONObject emotionScale() throws Exception{
        String charCode[]={"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};//16位元轉換
        double weights[]={1,1.1};//比重
        JSONObject rseg=segScale();
        JSONObject rsym=symScale();
        JSONObject result=new JSONObject();
        result.put("seg_ids", rseg.getJSONArray("ids"));//在JSONObject中放入找到情緒的斷詞
        result.put("sym_ids",rsym.getJSONArray("ids"));//在JSONObject中放入找到情緒的表情符號
        //計算方法
        //seg_emotion_inner乘上比重，加上sym_emotion_inner乘上比重，除以seg_count+sym_count
        double num1=(rseg.getInt("emotion_inner")*weights[0]+rsym.getInt("emotion_inner")*weights[1])/(rseg.getInt("count")+rsym.getInt("count"));
        double num2=(rseg.getInt("emotion_outer")*weights[0]+rsym.getInt("emotion_outer")*weights[1])/(rseg.getInt("count")+rsym.getInt("count"));
        System.out.println(num1+"\t"+num2);
        String code=charCode[(int)num1]+charCode[(int)num2];//將inner與outer轉換為16位元並相加成字串
        result.put("major_emotion", code);
        JSONArray minor=new JSONArray();
        System.out.println(code);
        for(int x=-1;x<=1;x++){//以主要情緒為中心，放入周圍九宮格之相關可能情緒
            for(int y=-1;y<=1;y++){
                if((int)num1+x>=0&&(int)num1+x<=15 && (int)num2+y>=0&&(int)num2+y<=15 &&!(x==0 && y==0))
                    minor.put(charCode[(int)num1+x]+charCode[(int)num2+y]);
            }
        }
        result.put("minor_emotion", minor);
        return result;
    }
    private JSONObject segScale() throws Exception{
        System.out.println("=======================================");
        int count=0,emotion_inner=0,emotion_outer=0,total=seg.length();
        JSONArray id=new JSONArray();
        JSONObject result=new JSONObject();
        for(int i=0;i<seg.length();i++){
            JSONObject sql=new JSONObject();
            JSONArray key=new JSONArray();
            sql.put("sqlCommand","select * from dictionary where keyword='"+seg.getString(i)+"'");
            key.put("emotion_inner");
            key.put("emotion_outer");
            sql.put("keys",key);
            MySQL mysql=new MySQL("127.0.0.1","chinesesegment_dictionary","root","1234");
            sql=mysql.SelectKeyword(sql);
            if(sql!=null){
                count++;
                id.put(sql.get("seg_dic_id"));
                
                emotion_inner+=sql.getJSONArray("values").getInt(0);
                emotion_outer+=sql.getJSONArray("values").getInt(1);
                System.out.println(sql.get("seg_dic_id")+"\t"+sql.getJSONArray("values").getInt(0)+"\t"+sql.getJSONArray("values").getInt(1));
            }
        }
                System.out.println("=======================================");
        result.put("ids",id);
        result.put("count",count);
        result.put("emotion_inner",emotion_inner);
        result.put("emotion_outer",emotion_outer);
        result.put("total", total);
        return result;
    }
    private JSONObject symScale() throws Exception{
        int count=0,emotion_inner=0,emotion_outer=0,total=sym.length();
        JSONArray id=new JSONArray();
        JSONObject result=new JSONObject();
        for(int i=0;i<sym.length();i++){
            JSONObject sql=new JSONObject();
            JSONArray key=new JSONArray();
            sql.put("sqlCommand","select * from dictionary where keyword='"+sym.getString(i)+"'");
            key.put("emotion_inner");
            key.put("emotion_outer");
            sql.put("keys",key);
            MySQL mysql=new MySQL("127.0.0.1","chinesesegment_dictionary","root","1234");
            sql=mysql.SelectKeyword(sql);
            if(sql!=null){
                count++;
                id.put(sql.get("seg_dic_id"));
                
                emotion_inner+=sql.getJSONArray("values").getInt(0);
                emotion_outer+=sql.getJSONArray("values").getInt(1);
                System.out.println(sql.get("seg_dic_id")+"\t"+sql.getJSONArray("values").getInt(0)+"\t"+sql.getJSONArray("values").getInt(1));
            }
        }
                System.out.println("=======================================");
        result.put("ids",id);
        result.put("count",count);
        result.put("emotion_inner",emotion_inner);
        result.put("emotion_outer",emotion_outer);
        result.put("total", total);
        return result;
    }
}
