package com.letv.sf.dao.beidou;

import com.letv.sf.config.ContentType;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import weibo4j.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public interface BeidouDao {
    public List<BeidouEntity> getKeywords(ContentType type,String date);

    public void saveBeidouMapping(BeidouMapping beidou, User user);

    public void saveBeidouMappings(BeidouMapping beidou, Collection<User> users);

    public List<BeidouEntity> listAllBeidou();

    public List<BeidouEntity> getMoiveOfficial(String date);
}
