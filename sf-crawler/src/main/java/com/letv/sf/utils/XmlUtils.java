package com.letv.sf.utils;

import com.thoughtworks.xstream.XStream;

/**
 * Created by yangyong3 on 2016/11/12.
 * 解析xml，采用XStream实现
 */
public class XmlUtils {

    private XStream xStream;

    public XmlUtils(Class... clazz){
        xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(clazz);
    }

    public String toXml(Object object){
        return xStream.toXML(object);
    }

    public <T> T fromXml(String xml,Class<T> tClass){
        return (T) xStream.fromXML(xml);
    }

}
