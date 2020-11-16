package com.lz.ht.base.tree;

import com.github.abel533.sql.SqlMapper;
import com.google.gson.Gson;
import com.lz.ht.quartz.SpringContextHolder;
import com.lz.ht.util.ToolKit;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class ZTreeConverter<T> {

    /**将列表转换为TreeJson
     * 使用的数据结构为：
     *  var zNodes =[
     *         { id:1, pId:0, name:"can check 1", open:true},
     *         { id:11, pId:1, name:"can check 1-1", open:true},
     *         { id:111, pId:11, name:"no radio 1-1-1", nocheck:true},
     *         { id:112, pId:11, name:"can check 1-1-2"},
     *         { id:12, pId:1, name:"no radio 1-2", nocheck:true, open:true},
     *         { id:121, pId:12, name:"no radio 1-2-1"},
     *         { id:122, pId:12, name:"no radio 1-2-2"},
     *         { id:2, pId:0, name:"can check 2", checked:true, open:true},
     *         { id:21, pId:2, name:"can check 2-1"},
     *         { id:22, pId:2, name:"can check 2-2", open:true},
     *         { id:221, pId:22, name:"can check 2-2-1", checked:true},
     *         { id:222, pId:22, name:"can check 2-2-2"},
     *         { id:23, pId:2, name:"can check 2-3"}
     *     ];
     * */


    private  String idColumn;//  id;

    private  String nameColumn;//deptName;

    private String pidColumn;//parentId;

    private String openOrNot ;// open: true


    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getPidColumn() {
        return pidColumn;
    }

    public void setPidColumn(String pidColumn) {
        this.pidColumn = pidColumn;
    }

    public String getOpenOrNot() {
        return openOrNot;
    }

    public void setOpenOrNot(String openOrNot) {
        this.openOrNot = openOrNot;
    }



    public ZTreeConverter(ConverterInfo converterInfo)  {
        try {
            BeanUtils.copyProperties(converterInfo,this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public ZTreeConverter(String idColumn, String nameColumn, String pidColumn, String openOrNot) {
        this.idColumn = idColumn;
        this.nameColumn = nameColumn;
        this.pidColumn = pidColumn;
        this.openOrNot = openOrNot;
    }
    /**转换为ZTree 格式json*/
    public String converterZTreeJsonStr(List<T> dataList) throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        for (T t:dataList) {
            HashMap<String, Object> hashMap = ToolKit.javaBeanToMap(t);
            String idFiledValue = getJavaBeanFiledValue(t, idColumn);
            String nameFiledValue = getJavaBeanFiledValue(t, nameColumn);
            String pidFiledValue = getJavaBeanFiledValue(t, pidColumn);
            hashMap.put("id",idFiledValue);
            hashMap.put("name",nameFiledValue);
            hashMap.put("pId",pidFiledValue);
            hashMap.put("open",openOrNot);
            list.add(hashMap);
        }
        Gson g = new Gson();
        String s = g.toJson(list);
        return s;
    }
    /**转换为ZTree 格式json*/
    public String converterZTreeJsonStr(String sql){
        List<Map<String, Object>> mapList = converter(sql);
        Gson g = new Gson();
        String s = g.toJson(mapList);
        return s;
    }

    /**转换为ZTree 格式List*/
    public List<Map<String, Object>> converterZTreeList(String sql){
        List<Map<String, Object>> mapList = converter(sql);
        return mapList;
    }


    private  String getJavaBeanFiledValue(T t,String filedColumn) throws NoSuchFieldException, IllegalAccessException {
        Field field = t.getClass().getDeclaredField(filedColumn);
        String filedValue = field.get(t).toString();
        return filedValue;
    }

    private List<Map<String,Object>>  findList(String sql){
        SqlMapper sqlMapper = (SqlMapper)SpringContextHolder.getBean("sqlMapper");
        List<Map<String, Object>> mapList = sqlMapper.selectList(sql);
        return mapList;
    }

    private List<Map<String, Object>> converter(String sql) {
        List<Map<String, Object>> mapList = findList(sql);
        for (Map<String, Object> t : mapList) {
            t.put("id", t.get(idColumn));
            t.put("name", t.get(nameColumn));
            t.put("pId", t.get(pidColumn));
            t.put("open", openOrNot);
        }
        return mapList;
    }


}
