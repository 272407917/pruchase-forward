package forward.service;

import forward.pojo.Users;

/**
 * @author 瞿琮
 * @create 2020-02-29 11:37
 */
public interface UserService {
    //注册用户
    boolean registerUser(Users user);

    //检查用户名是否重复
    boolean checkuName(String username);

    //用户登录，检查是否注册
    boolean login(Users user);
}
