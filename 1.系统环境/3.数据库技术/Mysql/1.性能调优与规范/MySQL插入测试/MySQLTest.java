package test;

import java.sql.*;

public class MySQLTest {

    //连接参数
    //url中的 rewriteBatchedStatements=true 参数，能够提高插入效率
    public static String url = "jdbc:mysql://localhost:3306/test1?useUnicode=true&rewriteBatchedStatements=true&characterEncoding=UTF8&useSSL=false&serverTimezone=UTC";
    public static String username = "root";
    public static String password = "iesapp";

    //注册驱动
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    //随机生成字符串
    public static String randomStr(int size) {
        //Define an empty string
        String result = "";
        for (int i = 0; i < size; ++i) {
            //Generate an int type integer between 97 ~ 122
            int intVal = (int) (Math.random() * 26 + 97);
            //Force conversion (char) intVal Convert the corresponding value to the corresponding character, and splicing the characters
            result = result + (char) intVal;
        }
        //Output string
        return result;
    }
    //创建数据表
    public static void createTables() throws SQLException {

        try {
            //获取数据库连接
            Connection conn = conn = DriverManager.getConnection(url, username, password);
            //实例化Statement
            //最后的 ENGINE=MyISAM DEFAULT   实现更改数据库引擎的作用
            String sql = "CREATE TABLE `test1` (`id` int(11) DEFAULT NULL,  `user_name` varchar(100) DEFAULT NULL,  `password` varchar(100) DEFAULT NULL, `randomchar1` varchar(100) DEFAULT NULL,  `randomchar2` varchar(100) DEFAULT NULL,  `randomchar3` varchar(100) DEFAULT NULL,  `randomchar4` varchar(100) DEFAULT NULL,  `randomchar5` varchar(100) DEFAULT NULL) ENGINE=MyISAM DEFAULT CHARSET=utf8;";

            //运行静态sql语句
            Statement pstmt = conn.createStatement() ;
            int rest = pstmt.executeUpdate(sql) ;
            //关闭连接
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insert(int insertNum){

        //开始时间
        Long begin = System.currentTimeMillis();
        System.out.println("Start Inserting Data...");

        try {
            //获取数据库连接
            Connection conn = conn = DriverManager.getConnection(url, username, password);
            //实例化Statement
            //将sql语句中要插入的数据，用 ? 来代替
            String sql = "INSERT INTO test1 (id, user_name, password, randomchar1, randomchar2, randomchar3, randomchar4, randomchar5) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            conn.setAutoCommit(false);
            //执行动态sql语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //对动态sql语句的 ? 进行逐个赋值     有8个 ? 所以，要对8个数据进行set（setInt 或 setString）
            for(int i = 0; i < insertNum; i++) {
                pstmt.setInt(1, i);
                pstmt.setString(2,randomStr(8));
                pstmt.setString(3, randomStr(8));
                pstmt.setString(4, randomStr(8));
                pstmt.setString(5, randomStr(8));
                pstmt.setString(6, randomStr(8));
                pstmt.setString(7, randomStr(8));
                pstmt.setString(8, randomStr(8));
                pstmt.addBatch();
            }
            pstmt.addBatch();
            pstmt.executeBatch();
            //提交事务
            conn.commit();

            //关闭连接
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //结束时间
        Long end = System.currentTimeMillis();
        System.out.println("insert "+insertNum+" data is completed!");
        System.out.println("Time-consuming : " + (end - begin) / 1000 + "seconds");
    }

    public static void main(String[] args) throws SQLException {

        createTables();
        insert(1000000);
    }

}
