package forward.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 瞿琮
 * @create 2020-02-29 10:43
 */
//该类用来实现数据库的操作，数据库操作分为两种查询和修改，实现一些公共的功能（很多地方需要调用）
public class BasicDao {
    //公共的查询功能,返回的是结果集,参数分别为 数据库连接对象 预编译参数
    // 查询语句中对应的？数据（因为不知道是什么类型有多少个所以用Object和...）
    //这里写的是一些公用的方法所以不要写太死
    //sql语句是要先放进pst中的( PreparedStatement pst = con.prepareStatement("sql");)
    public ResultSet execQuery(Connection con, PreparedStatement pst,Object...params){
        ResultSet rs=null;
        try {
            //针对占位符传参
            if (params!=null){
                for (int i = 0; i < params.length; i++) {
                    pst.setObject(i+1,params[i]);
                }
            }
            //查询得到的结果集
            rs = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    //公共修改功能
    public void execUpdate(Connection con,PreparedStatement pst,Object...params) throws SQLException {
            //针对占位符传参,这个地方的异常要抛不能包，因为这是底层的方法要被其他地方调用，要是包的话出现异常就会在
            //底层解决，无法返回是什么异常，所以异常要向调用它的地方抛
            if (params!=null){
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i+1,params[i]);
               }
            }
            pst.executeUpdate();
    }

    //公共释放资源功能
    public void releaseResource(ResultSet rs,PreparedStatement pst,Connection con){
        try {
            if (rs!=null){
                rs.close();
            }
            if (pst!=null){
                pst.close();
            }
            if (con!=null){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
