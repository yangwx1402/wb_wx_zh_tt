package com.letv.sf.dataexport;

/**
 * Created by yangyong3 on 2016/12/8.
 */
public class CommentDataMapper implements DataMapper {

    private static final String split = "\t";

    public String mapper(Object[] objs) {
        String location = objs[7].toString();
        String[] temp = location.split(" +");
        String bir = objs[6]==null?"-":objs[6].toString();
        String gender = objs[5].toString();
        if ("f".equals(gender))
            gender = "女";
        else if ("m".equals(gender)) {
            gender = "男";
        } else gender = "其他";
        StringBuffer sb = new StringBuffer();
        sb.append(objs[0]==null?"-":objs[0] + split);
        sb.append(objs[1]==null?"-":objs[1] + split);

        if(temp.length==2){
            sb.append(temp[0]+split+temp[1]+split);
        }else if(temp.length==1){
            sb.append(temp[0]+split+"-"+split);
        }else{
            sb.append("-"+split+"-");
        }
        if(bir!=null&&bir.length()>8){
            try {
                sb.append(2016 - Integer.parseInt(bir.substring(0, 4))+split);
            }catch (Exception e){
                sb.append("-"+split);
            }
        }else{
            sb.append("-"+split);
        }
        sb.append(gender+split);
        sb.append(objs[8]==null?"-":objs[8].toString());
       return sb.toString();
    }
}
