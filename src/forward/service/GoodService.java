package forward.service;

import forward.pojo.Goods;

import java.util.List;

/**
 * @author 瞿琮
 * @create 2020-03-01 9:47
 */
public interface GoodService {
    //取出全部商品类型
    List<String> findAllGoodType();
    //取出同一类型的全部商品
    List<Goods> findGoodsByType(String type,int pageNow,int pageSize);


}
