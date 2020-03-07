package forward.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author 瞿琮
 * @create 2020-02-29 9:53
 */

//该类是一个工具类用来得到数据库的连接
public class DbPool {
    private static ComboPooledDataSource ds;

    static {
        //得到连接池对象,并设置属性
        ds=new ComboPooledDataSource();
        //读取文件dp.properties，使用文件中的值来设置连接池属性，这样灵活性高
        Properties properties=new Properties();
        try {
            properties.load(DbPool.class.getClassLoader().getResourceAsStream("dp.properties"));
            ds.setDriverClass(properties.getProperty("driverName"));
            ds.setUser(properties.getProperty("username"));
            ds.setPassword(properties.getProperty("password"));
            ds.setJdbcUrl(properties.getProperty("url"));
            ds.setInitialPoolSize(Integer.valueOf(properties.getProperty("InitialPoolSize")));
            ds.setMaxPoolSize(Integer.valueOf(properties.getProperty("MaxPoolSize")));
            ds.setMinPoolSize(Integer.valueOf(properties.getProperty("MinPoolSize")));
            ds.setMaxStatements(Integer.valueOf(properties.getProperty("MaxStatements")));
            ds.setMaxIdleTime(Integer.valueOf(properties.getProperty("MaxIdleTime")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
