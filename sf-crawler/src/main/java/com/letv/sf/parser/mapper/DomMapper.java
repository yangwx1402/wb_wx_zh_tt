package com.letv.sf.parser.mapper;

import org.jsoup.nodes.Element;

/**
 * Created by yangyong3 on 2016/12/5.
 */
public interface DomMapper<T> {
    public T mapper(Element element);
}
