package forward.dao;

import forward.pojo.Goods;
import forward.util.DbPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 瞿琮
 * @create 2020-03-01 9:49
 */
public class GoodDao {
    BasicDao bd=new BasicDao();

    //按条件查询商品
    public List<String> findAllGoodType(){
        //查询所有商品类别，使用排序固定顺序
        String sql="SELECT DISTINCT goodtype from good ORDER BY goodtype";
        //拼接字符串
        List<String> goodTypeList=new ArrayList<>();
        Connection con = DbPool.getConnection();
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con,pst,null);//这里的赋值为null因为在拼写字符串时已经写入了条件值，
            // 则需要将BasicDao中针对占位符传参加上不为null值的条件
            while (rs!=null&&rs.next()){//控制指针一行一行的移动
                //选择一行中那一列的数据
                String goodType = rs.getString(1);
                goodTypeList.add(goodType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            bd.releaseResource(rs,pst,con);
        }
        return goodTypeList;
    }



    //得到同一类型的所有商品
    public List<Goods> findGoodsByType(String type,int pageNow,int pageSize){
        //拼写查询所有语句
        String sql="select * from good where goodtype=? order by id limit ?,?";
        List<Goods> result=new ArrayList<>();
        Connection con = DbPool.getConnection();
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con,pst,type,(pageNow-1)*pageSize,pageSize);
            while (rs!=null&&rs.next()){
                Goods goodBean=new Goods();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
                goodBean.setPic(rs.getString(5));
                result.add(goodBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            bd.releaseResource(rs,pst,con);
        }
        return result;
    }


    //得到总记录数
    public int finTotalRecord(){
        //拼写查询所有语句
        String sql="select count(*) from good";
        Connection con = DbPool.getConnection();
        PreparedStatement pst=null;
        ResultSet rs=null;
        int result=0;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con,pst,null);
            while (rs!=null&&rs.next()){
                result=rs.getInt(1);//得到总记录数
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            bd.releaseResource(rs,pst,con);
        }
        return result;
    }

}
