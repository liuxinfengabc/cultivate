package com.lz.ht.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.sql.SqlMapper;
import com.lz.ht.model.User;
import com.lz.ht.model.UserRole;
import com.lz.ht.service.RoleResourcesService;
import com.lz.ht.service.UserRoleService;
import com.lz.ht.service.UserService;

public class CustomRealm extends AuthorizingRealm {
    private UserService userService;
    private UserRoleService userRoleService;
    private RoleResourcesService roleResourcesService; 
    private SqlMapper sqlMapper;
    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    private void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }
    @Autowired
    private void setRoleResourcesService(RoleResourcesService roleResourcesService) {
        this.roleResourcesService = roleResourcesService;
    } 
    @Autowired
    private void setSqlMapper(SqlMapper sqlMapper) {
        this.sqlMapper = sqlMapper;
    }
    /**
     * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 从数据库获取对应用户名密码的用户
       // String password = userService.getPassword(token.getUsername());
        User user = new User();
        user.setUserName(token.getUsername());
        User one = userService.findOne(user);
        String password = one.getPassword();
        String credentials = new String((char[]) token.getCredentials());
        if (null == password) {
            throw new AccountException("用户名不正确");
        } else if (!password.equals(credentials)) {
            throw new AccountException("密码不正确");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("————权限认证————");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        //String role = userMapper.getRole(username);
        User user = new User();
        user.setUserName(username);
        User one = userService.findOne(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(one.getId());
        List<UserRole> userRoleList = userRoleService.findList(userRole);
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        userRoleList.stream().forEach(r->{
            set.add(r.getRoleKey());
        });
        //设置该用户拥有的角色
        info.setRoles(set); 
        //1.根据角色获取所有的资源
        //2.将资源作为权限设置到shiro中
        //3.页面上通过hasPemission 判断有没有权限
        Set<String> hasPemissionSet = new HashSet<>();
        String sql =   "SELECT   res.resUrl FROM " +  
        		"  t_resources res " + 
        		"  WHERE res.resKey IN " + 
        		"  (SELECT " + 
        		"    tmp.resKey " + 
        		"  FROM " + 
        		"    (SELECT DISTINCT " + 
        		"      rr.resKey " + 
        		"    FROM " + 
        		"      t_role_resources rr " + 
        		"    WHERE rr.roleKey IN " + 
        		"      (SELECT DISTINCT " + 
        		"        ur.roleKey " + 
        		"      FROM\r\n" + 
        		"        t_user_role ur " + 
        		"      WHERE ur.userId = #{userId})) tmp)";
        List<Map> resList = sqlMapper.selectList(sql, one.getId(), Map.class);
        for (Map map : resList) {
        	hasPemissionSet.add(map.get("resUrl").toString());
		} 
        System.out.println(hasPemissionSet.toString());
        //给超级管理员（userId==1）特殊权限，可以分配权限的权限，即有三个资源超级管理一定有：
        // #  role/list  role/roleResource，保证可以分配自己的权限
        if(one.getId().intValue()==1) {
        	hasPemissionSet.add("#");
        	hasPemissionSet.add("role/list");
        	hasPemissionSet.add("role/roleResource");
        }
        info.setStringPermissions(hasPemissionSet);
        return info;
    }
}