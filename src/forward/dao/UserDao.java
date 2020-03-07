package forward.dao;

import forward.pojo.Users;
import forward.util.DbPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 瞿琮
 * @create 2020-02-29 11:38
 */
public class UserDao {
    BasicDao bd=new BasicDao();

    public boolean queryUser(Users user){
        boolean resulet=false;
        //通过模型层service传过来的封装类得到输入的用户名密码
        String username = user.getUsername();
        String password = user.getPassword();
        //通过工具类DbPool得到连接对象
        Connection con = DbPool.getConnection();
        String sql="select count(*) from user where username=? and password=? and rule=0";
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con, pst, username, password);
            int count=0;
            if (rs!=null&&rs.next()){
                count=rs.getInt(1);
            }
            if (count==1){
                resulet=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            bd.releaseResource(rs,pst,con);
        }
        return resulet;
    }
}
