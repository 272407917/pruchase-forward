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

    private GoodDao goodDao = new GoodDao();

    @Override
    public List<String> findAllGoodType() {
        return goodDao.findAllGoodType();
    }

    @Override
    public List<Goods> findGoodsByType(String type, int pageNow, int pageSize) {
        return goodDao.findGoodsByType(type,pageNow,pageSize);
    }

}
