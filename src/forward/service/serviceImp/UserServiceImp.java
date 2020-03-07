package forward.service.serviceImp;

import forward.dao.UserDao;
import forward.pojo.Users;
import forward.service.UserService;

/**
 * @author 瞿琮
 * @create 2020-02-29 11:37
 */
public class UserServiceImp implements UserService {
    @Override
    public boolean checkUser(Users user) {
        UserDao userDao=new UserDao();
        return userDao.queryUser(user);
    }
}
