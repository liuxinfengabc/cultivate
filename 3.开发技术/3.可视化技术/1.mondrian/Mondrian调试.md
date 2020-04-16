### 1.安装部署



#### 1.1 org.apache.jasper.JasperException:

```
页面报的异常为： 
org.apache.jasper.JasperException: An exception occurred processing JSP page/test/param1.jsp at line 23.  
 
解决方案：将web.xml中的filter的url­-pattern值由“testpage.jsp”改为“*.jsp” 即可！ 
```





```
The origin server did not find a current representation for the target resource or is not willing to disclose that one exists.
```

### 2.配置

#### 2.1 Cube配置

![img](http://wiki.smartbi.com.cn:18081/download/attachments/3866724/4.jpg?version=1&modificationDate=1396679874000&api=v2)

#### 2.2维度配置

##### 2.2 .1普通维度

![img](http://wiki.smartbi.com.cn:18081/download/attachments/3866724/6.jpg?version=1&modificationDate=1396679986000&api=v2)



| **属性**       | **含义**                               |
| :------------- | :------------------------------------- |
| column         | 级别对应维度表的列                     |
| Dimension name | 定义一个维度，给出维度名               |
| foreingKey     | 事实表的外键                           |
| hasAll         | 表示是否有“ALL”成员                    |
| Hierarchy      | 定义一个层次（可以定义多个 Hierarchy） |
| Level name     | 定义一个级别，给出名称                 |
| primaryKey     | 维度表的主键                           |
| Table name     | 维度表名                               |
| uniqueMembers  | 维度表的成员是否唯一                   |



##### 2.2.2 时间维

![img](http://wiki.smartbi.com.cn:18081/download/attachments/3866724/7.jpg?version=1&modificationDate=1396680037000&api=v2)

| **属性**             | **含义**             |
| :------------------- | :------------------- |
| Hierarchy            | 多个层次并列申明     |
| levelType            | 定义时间维级别的类型 |
| type="TimeDimension" | 声明这是一个时间维   |

##### 2.2.3 雪花维

![img](http://wiki.smartbi.com.cn:18081/download/attachments/3866724/8.jpg?version=1&modificationDate=1396680082000&api=v2)

雪花维与普通维度区别有两点：

- 通过 primaryKeyTable定义维度表

- 在<Join>中定维度表间的连接。

#### 2.3Measures定义



