package cn.com.zxd.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by zxd on 2019/8/6.
 */
public class ShiroTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();


    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("zxd", "123456","admin");
    }

    @Test
    public void testAuthentication() throws Exception {

        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2.主体对象提交认证请求对象。
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();


        //3.认证用户
        UsernamePasswordToken token = new UsernamePasswordToken("zxd", "123456");
        subject.login(token);

        System.out.println("认证的结果是:" + subject.isAuthenticated());

        subject.checkRole("admin");

    }
}
