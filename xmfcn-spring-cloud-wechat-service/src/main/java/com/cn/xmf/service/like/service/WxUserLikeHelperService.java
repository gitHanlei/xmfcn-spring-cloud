package com.cn.xmf.service.like.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.model.wx.WxUserLike;
import com.cn.xmf.service.like.dao.WxUserLikeDao;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * WxUserLike Service(微信点赞辅助类)
 * @author rufei.cn
 * @version 2019-10-15
 */
@Service
@SuppressWarnings("all")
public class WxUserLikeHelperService  {
    private static Logger logger = LoggerFactory.getLogger(WxUserLikeHelperService.class);

	@Autowired
	private WxUserLikeDao wxUserLikeDao;

	/**
	 * 获取分页总记录数
	 *
	 * @param map
	 * @return
	 */
	public long getTotalCount(JSONObject map) {
		long resCount = 0;
		Long totalCount = wxUserLikeDao.getTotalCount(map);
		if (totalCount != null) {
			resCount = totalCount;
		}
		return resCount;
	}

	/**
	 * 获取单条数据
	 *
	 * @param bizId
	 * @param type
	 * @return
	 * @author rufei.cn
	 */
	public long getPhotoLikeCount(String bizId, String type) {
		long count = 0;
		JSONObject map = new JSONObject();
		map.put("type", type);
		map.put("bizid", bizId);
		String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "getPhotoLikeCount" + bizId + type;
		String cache = LocalCacheUtil.getCache(key);
		if (StringUtil.isNotBlank(cache)) {
			count = StringUtil.stringToLong(cache);
		}
		if (count > 0) {
			return count;
		}
		Long likeCount = wxUserLikeDao.getLikeCount(map);
		if(likeCount!=null)
		{
			count=likeCount;
		}
		LocalCacheUtil.saveCache(key, String.valueOf(count), 60);
		return count;
	}

    /*
     * save(保存微信点赞)
     * @param wxUserLike
     * @author rufei.cn
     * @date 2018/1/30 14:59
     */
   @Transactional(propagation = Propagation.REQUIRED,readOnly =false,isolation = Isolation.REPEATABLE_READ,timeout = 30,rollbackFor = Exception.class)
    public WxUserLike save(WxUserLike wxUserLike) throws  Exception {
       WxUserLike ret=null;
        if (wxUserLike == null) {
            return ret;
        }
        if (wxUserLike.getId() != null && wxUserLike.getId() > 0) {
            updateById(wxUserLike);
            ret=wxUserLike;
        } else {
            wxUserLike.setId(null);
            wxUserLikeDao.add(wxUserLike);
            ret=wxUserLike;
        }
        return ret;
    }

     /**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	 public WxUserLike getWxUserLikeById (long id)
	 {
	      return wxUserLikeDao.getWxUserLikeById(id);
	 }

    /**
	 * 获取单条数据
	 * @param wxUserLike
	 * @author rufei.cn
	 * @return
	 */
	 public WxUserLike getSignleWxUserLike(WxUserLike wxUserLike)
	 {
	      return wxUserLikeDao.getSignleWxUserLike(wxUserLike);
	 }
	 /**
	 * 修改单条数据
	 * @param id
	 * @return
	 */
	  private void updateById(WxUserLike wxUserLike)
	  {
	     wxUserLikeDao.updateById(wxUserLike);
	  }
	
}