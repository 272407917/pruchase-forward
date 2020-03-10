package forward.service.serviceImp;

import forward.dao.UserDao;
import forward.pojo.Users;
import forward.service.UserService;
import forward.util.Tools;

/**
 * @author 瞿琮
 * @create 2020-02-29 11:37
 */
public class UserServiceImp implements UserService {

    private UserDao userDao=new UserDao();

    //注册用户
    @Override
    public boolean registerUser(Users user) {
        //密码加密,属于逻辑层处理
        user.setPassword(Tools.md5(user.getPassword()));
        boolean result = userDao.registerUser(user);
        return result;
    }

    //检查用户名是否重复
    @Override
    public boolean checkuName(String username) {
        return userDao.queryUserByUserName(username);
    }

    //检查登录用户
    @Override
    public boolean login(Users user) {
        user.setPassword(Tools.md5(user.getPassword()));
        return userDao.queryUserLogin(user);
    }
}
