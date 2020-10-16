# Mysql数据更新测试

测试表中已有2000万条数据

### 每次更新七个字段，更新100万条，试运行了10次。

## 源代码：

```Java
package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateTest {
    //连接参数
    public static String url = "jdbc:mysql://localhost:3306/test1?useUnicode=true&rewriteBatchedStatements=true&characterEncoding=UTF8&useSSL=false&serverTimezone=UTC";
    public static String username = "root";
    public static String password = "iesapp";

    //注册驱动
    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void update(int updateNum){
        //开始时间
        Long begin = System.currentTimeMillis();
        System.out.println("Start update Date...");

        try {
            //获取数据库连接
            Connection conn = DriverManager.getConnection(url, username, password);
            //实例化Statem
            //将sql语句中要更新的数据用 ? 代替
            String sql = "UPDATE test1 SET user_name = ?, password = ?, randomchar1 = ?, randomchar2 = ?, randomchar3 = ?, randomchar4 = ?, randomchar5 = ? WHERE id = ?";

            conn.setAutoCommit(false);
            //执行动态sql语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //对动态sql进行赋值
            for (int i = 1; i <= updateNum; i++){
                pstmt.setString(1, MySQLTest.randomStr(8));
                pstmt.setString(2, MySQLTest.randomStr(8));
                pstmt.setString(3, MySQLTest.randomStr(8));
                pstmt.setString(4, MySQLTest.randomStr(8));
                pstmt.setString(5, MySQLTest.randomStr(8));
                pstmt.setString(6, MySQLTest.randomStr(8));
                pstmt.setString(7, MySQLTest.randomStr(8));
                pstmt.setInt(8,i);
                pstmt.addBatch();
            }
            pstmt.addBatch();
            pstmt.executeBatch();
            //提交
            conn.commit();
            //关闭连接
            pstmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //结束时间
        Long end = System.currentTimeMillis();
        System.out.println("Update" + updateNum + "data is completed!");
        System.out.println("Time-consuming : "+ (end - begin) / 1000 + "seconds");

    }

    public static void main(String[] args) {

        //更新100万条数据，更新10次
        for (int i = 1; i <= 10; i++){
            update(1000000);
        }

    }

}

```

### 结果显示，每次更新完成时间在79秒左右。