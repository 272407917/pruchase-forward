package forward.service;

import forward.pojo.Goods;

import java.util.List;

/**
 * @author 瞿琮
 * @create 2020-03-01 9:47
 */
public interface GoodService {
    boolean addGood(Goods good);
    List<Goods> queryByCritria(Goods good);
    List<Goods> queryGoodAll(int pageNow,int pageSize);
    int finTotalRecord();
    boolean modGood(Goods good);
    boolean removeGood(int id);
}
