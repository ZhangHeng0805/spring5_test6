package zh.spring5.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import zh.spring5.config.TxConfig;
import zh.spring5.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class TestBook {
//
//    @Test
//    public void testAccount(){
//        ApplicationContext context=new ClassPathXmlApplicationContext("bean1.xml");
//        UserService userService = context.getBean("userService", UserService.class);
//        userService.accountMoney();
//    }
    /*@Test
    public void testAccount1(){
        ApplicationContext context=new ClassPathXmlApplicationContext("bean2.xml");
        UserService userService = context.getBean("userService", UserService.class);
        userService.accountMoney();
    }*/

    /*@Test//完全注解
    public void testAccount2(){

        ApplicationContext context=new AnnotationConfigApplicationContext(TxConfig.class);
        UserService userService = context.getBean("userService", UserService.class);
        userService.accountMoney();
    }*/
    @Test//函数式风格创建对象，交给spring进行管理
    public void testGenericApplicationContext(){
        //创建GenericApplicationContext对象
        GenericApplicationContext context=new GenericApplicationContext();
        //调用context的方法对象注册
        context.refresh();
        context.registerBean("user1",User.class,() ->new User());
        //获取在spring注册的对象
//        User user = (User) context.getBean("zh.spring5.test.User");
        User user = (User) context.getBean("user1");
        System.out.println(user);
    }


}
