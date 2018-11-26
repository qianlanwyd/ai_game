import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.TreeMap;
public class test {
    private static String mychangestr(String str) {
        String tempStr;
        int index1=str.indexOf('[');
        int index2=str.indexOf(',');
        tempStr = str.substring(index1 + 1, index2);
        String tempStr2=tempStr+':'+tempStr;
        String res=str.replace(tempStr,tempStr2);
        while(str.indexOf('|',index2+1)!=-1 && str.indexOf(',',index2+1)!=-1){
            String tempStr3;
            index1=str.indexOf('|',index2+1);
            index2=str.indexOf(',',index2+1);
            tempStr3 = str.substring(index1+1, index2);
            String tempStr4=tempStr3+':'+tempStr3;
            res=res.replace(tempStr3,tempStr4);
        }
        return res;
    }
    private static String findlabel(String str) {
       double max=0;
       String res="";
       HashMap<String, Double> hm = new HashMap<>();
       String tempStr;
       int index1=str.indexOf('[');
       int index2=str.indexOf('|');
       tempStr = str.substring(index1 + 1, index2);
       String labelname1=labelname(tempStr);
       double labelweight1=labelweight(tempStr);
       if(hm.containsKey(labelname1)){
           double newweight=hm.get(labelname1)+labelweight1;
           hm.put(labelname1,newweight);
       }
       else {
           hm.put(labelname1, labelweight1);
       }
       index1=str.indexOf('|',index2);
       index2=str.indexOf('|',index1+1);
       while(index1!=-1 &&index2!=-1){
           tempStr = str.substring(index1 + 1, index2);
           //System.out.println(tempStr);
           String labelname2=labelname(tempStr);
           //System.out.println(labelname2);
           double labelweight2=labelweight(tempStr);
           if(hm.containsKey(labelname2)){
               //System.out.println(labelname2);
               //System.out.println(hm.get(labelname2));
               double newweight=hm.get(labelname2)+labelweight2;
               hm.put(labelname2,newweight);
           }
           else {
               hm.put(labelname2, labelweight2);
           }
           index1=str.indexOf('|',index2);
           index2=str.indexOf('|',index1+1);
       }
       index2=str.indexOf(']');
       tempStr = str.substring(index1 + 1, index2);
       //System.out.println(tempStr);
       String labelname3=labelname(tempStr);
       double labelweight3=labelweight(tempStr);
        if(hm.containsKey(labelname3)){
            double newweight=hm.get(labelname3)+labelweight3;
            hm.put(labelname3,newweight);
        }
        else {
            hm.put(labelname3, labelweight3);
        }
        for (String key : hm.keySet()) {
            if(hm.get(key)>max){
                max=hm.get(key);
                res=key;
            }

        }
        return res;
    }
    private static  String labelname(String str){
        int index1=str.indexOf(':');
        int index2=str.indexOf(',');
        String tempStr = str.substring(index1 + 1, index2);
        return tempStr;
    }
    private static  double labelweight(String str){
        //System.out.println(str);
        int index1=str.indexOf(',');
        int index2=str.length();
        String tempStr;
        tempStr = str.substring(index1 + 1, index2);
        double res=Double.parseDouble(tempStr);
        return res;
    }
    private static String replacestr(String str){
        String []res=str.split(":");
        res[1]="null";
        return res[0]+':'+res[1];
    }
    public static void main(String[] args){
        //String str="[戚芳,0.33333|戚三,0.333333|戚二,0.666|卜垣,0.333333]";
       String str="[戚芳:戚三,0.33333|戚三:戚三,0.333333|戚二:戚二,0.666|卜垣:戚二,0.333333]";
        //init=mychangestr(init);
        //System.out.println(init);
       // str=findlabel(str);
       // System.out.println(str);
        String res="[";
        String tempStr;
        int index1=str.indexOf('[');
        int index2=str.indexOf('|');
        tempStr = str.substring(index1 + 1, index2);
        String []ts=tempStr.split(",");
        ts[0]=replacestr(ts[0]);
        res=res+ts[0]+","+ts[1];
        //System.out.println(tempStr);
        while(str.indexOf('|',index2+1)!=-1 && str.indexOf('|',index2+1)!=-1){
            String tempStr2;
            index1=str.indexOf('|',index2);
            index2=str.indexOf('|',index1+1);
            tempStr2 = str.substring(index1+1, index2);
            String []ts2=tempStr2.split(",");
            ts2[0]=replacestr(ts2[0]);
            res=res+"|"+ts2[0]+","+ts2[1];
            //System.out.println(tempStr2);
        }
        index1=index2;
        index2=str.indexOf(']');
        tempStr = str.substring(index1 + 1, index2);
        String []ts3=tempStr.split(",");
        ts3[0]=replacestr(ts3[0]);
        res=res+"|"+ts3[0]+","+ts3[1]+"]";
        System.out.println(res);

    }

}
