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

    public boolean insertGood(Goods good){
        boolean result=false;
        Connection con = DbPool.getConnection();
        String sql="insert into good(goodname,goodtype,price,pic) value(?,?,?,?)";
        PreparedStatement pst=null;
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            bd.execUpdate(con,pst,good.getGoodname(),good.getGoodtype(),good.getPrice(),good.getPic());
            int i = pst.executeUpdate();
            if (i==1){
                result=true;
            }
            con.commit();
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


    //按条件查询商品
    public List<Goods> queryByCritria(Goods good){
        //拼写查询语句
        String sql="select id,goodname,goodtype,price from good";
        //拼写条件语句
        String critria="";
        if (good.getId()!=-1){//不为空，有具体的数字
            critria+=" id="+good.getId();
        }
        if (!good.getGoodname().trim().isEmpty()){//去掉该字符串的空格，判断是否为空串，不是空串则继续
            if (critria.isEmpty()){//判断是否为空串
                critria+=" goodname like'%"+good.getGoodname()+"%'";//为空串则id没有值可以不用加and,使用模糊查询
            }else {
                critria+=" and goodname like'%"+good.getGoodname()+"%'";
            }
        }
        if (!good.getGoodtype().trim().isEmpty()){
            if (critria.isEmpty()){
                critria+=" goodtype like'%"+good.getGoodtype()+"%'";
            }else {
                critria+=" and goodtype like'%"+good.getGoodtype()+"%'";
            }
        }
        //拼接字符串
        List<Goods> result=new ArrayList<>();
        sql+=" where"+critria;
        Connection con = DbPool.getConnection();
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con,pst,null);//这里的赋值为null因为在拼写字符串时已经写入了条件值，
            // 则需要将BasicDao中针对占位符传参加上不为null值的条件
            while (rs!=null&&rs.next()){
                //将查询的结果一次放进封装类Goods的对象中
                Goods goodBean=new Goods();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
                //将对象放进返回集合list中
                result.add(goodBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            bd.releaseResource(rs,pst,con);
        }
        return result;
    }



    //得到当前页的记录
    public List<Goods> queryGoodAll(int pageNow,int pageSize){
        //拼写查询所有语句
        String sql="select * from good order by id limit ?,?";
        List<Goods> result=new ArrayList<>();
        Connection con = DbPool.getConnection();
        PreparedStatement pst=null;
        ResultSet rs=null;
        try {
            pst = con.prepareStatement(sql);
            rs = bd.execQuery(con,pst,(pageNow-1)*pageSize,pageSize);
            while (rs!=null&&rs.next()){
                Goods goodBean=new Goods();
                goodBean.setId(rs.getInt(1));
                goodBean.setGoodname(rs.getString(2));
                goodBean.setGoodtype(rs.getString(3));
                goodBean.setPrice(rs.getDouble(4));
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

    //修改商品信息
    public boolean modGood(Goods good){
        boolean result=false;
        Connection con = DbPool.getConnection();
        String sql="update good set goodname=?,goodtype=?,price=? where id=?";
        PreparedStatement pst=null;
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            bd.execUpdate(con,pst,good.getGoodname(),good.getGoodtype(),good.getPrice(),good.getId());
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


    //删除商品
    public boolean removeGood(int id){
        boolean result=false;
        Connection con = DbPool.getConnection();
        String sql="delete from good where id=?";
        PreparedStatement pst=null;
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            bd.execUpdate(con,pst,id);
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
}
