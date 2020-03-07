package forward.service.serviceImp;

import forward.dao.GoodDao;
import forward.pojo.Goods;
import forward.service.GoodService;

import java.util.List;

/**
 * @author 瞿琮
 * @create 2020-03-01 9:48
 */
public class GoodServiceImp implements GoodService {
    @Override
    public boolean addGood(Goods good) {
        GoodDao goodDao=new GoodDao();
        return goodDao.insertGood(good);
    }

    @Override
    public List<Goods> queryByCritria(Goods good) {
        GoodDao goodDao=new GoodDao();
        return goodDao.queryByCritria(good);
    }

    @Override
    public List<Goods> queryGoodAll(int pageNow, int pageSize) {
        GoodDao goodDao=new GoodDao();
        return goodDao.queryGoodAll(pageNow,pageSize);
    }

    @Override
    public int finTotalRecord() {
        GoodDao goodDao=new GoodDao();
        return goodDao.finTotalRecord();
    }

    @Override
    public boolean modGood(Goods good) {
        GoodDao goodDao=new GoodDao();
        return goodDao.modGood(good);
    }

    @Override
    public boolean removeGood(int id) {
        GoodDao goodDao=new GoodDao();
        return goodDao.removeGood(id);
    }
}
