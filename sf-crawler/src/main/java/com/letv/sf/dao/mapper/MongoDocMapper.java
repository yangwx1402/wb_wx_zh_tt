package com.letv.sf.dao.mapper;

import org.bson.Document;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public interface MongoDocMapper<T> {

    public Document mapper(T t);

    public T deMapper(Document document);
}
