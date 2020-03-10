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

    //判断用户名是否重复
    public boolean queryUserByUserName(String username){
        boolean resulet=false;
        //通过工具类DbPool得到连接对象
        Connection con = DbPool.getConnection();
        String sql="select count(*) from user where username=? and rule=1";
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con, pst, username);
            int count=0;
            if (rs!=null&&rs.next()){
                count=rs.getInt(1);
            }
            if (count!=0){
                resulet=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            bd.releaseResource(rs,pst,con);
        }
        return resulet;
    }


    //用户注册
    public boolean registerUser(Users user){
        boolean result=false;
        Connection con = DbPool.getConnection();
        String sql="insert into user(username,password,rule,email,qq) value(?,?,1,?,?)";
        PreparedStatement pst=null;
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            bd.execUpdate(con,pst,user.getUsername(),user.getPassword(),user.getEmail(),user.getQq());
            con.commit();
            result=true;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            bd.releaseResource(null,pst,con);
        }
        return result;
    }

    //检查登录用户
    public boolean queryUserLogin(Users user){
        boolean resulet=false;
        Connection con = DbPool.getConnection();
        String sql="select count(*) from user where username=? and password=? and rule=1";
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con, pst, user.getUsername(),user.getPassword());
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
