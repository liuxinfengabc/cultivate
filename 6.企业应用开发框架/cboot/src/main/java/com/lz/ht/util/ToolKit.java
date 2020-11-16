package com.lz.ht.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ToolKit {

        /**
         * javaBean转map
         * @param obj
         * @return
         * @throws Exception
         */
        public static HashMap<String,Object> javaBeanToMap(Object obj) throws Exception{
            HashMap<String, Object> map = new HashMap<>();
            // 获取javaBean的BeanInfo对象
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(),Object.class);
            // 获取属性描述器
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                // 获取属性名
                String key = propertyDescriptor.getName();
                // 获取该属性的值
                Method readMethod = propertyDescriptor.getReadMethod();
                // 通过反射来调用javaBean定义的getName()方法
                Object value = readMethod.invoke(obj);
                map.put(key, value);
            }
            return map;
        }



    /**
     * map转javaBean
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T>T mapToJavaBean(Map<String,Object> map , Class<T> clazz) throws Exception{
        // new 出一个对象
        T obj = clazz.newInstance();
        // 获取javaBean的BeanInfo对象
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        // 获取属性描述器
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            // 获取属性名
            String key = propertyDescriptor.getName();
            Object value = map.get(key);
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (map.containsKey(key)){
                // 解决 argument type mismatch 的问题，转换成对应的javaBean属性类型
                String typeName = propertyDescriptor.getPropertyType().getTypeName();
                // System.out.println(key +"<==>"+ typeName);
                if ("java.lang.Integer".equals(typeName)){
                    value = Integer.parseInt(value.toString());
                }
                if ("java.lang.Long".equals(typeName)){
                    value = Long.parseLong(value.toString());
                }
                if ("java.util.Date".equals(typeName)){
                    value = new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
                }
            }
            // 通过反射来调用javaBean定义的setName()方法
            writeMethod.invoke(obj,value);
        }
        return obj;
    }
    
     public static String uuidString() {
    	 return UUID.randomUUID().toString().replaceAll("-", "");
     }
    
    
}
