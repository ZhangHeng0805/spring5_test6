# spring5_test6
# Spring5框架—事务操作
## 事务概念
   ### 1、什么是事务
        （1）事务是数据库操作最基本单元，逻辑上一组操作，要么都成功，如果有一个失败所有操作都失败
        （2）典型场景：银行转账
   ### 2、事务四个特性（ACID）
        （1）原子性
        （2）一致性
        （3）隔离性
        （4）持久性

## 事务操作（搭建事务操作环境）
   ### 1、创建数据库表，添加记录
   ### 2、创建service，搭建dao，完成对象创建和注入关系
        （1）service注入dao，在dao注入JdbcTemplate，在JdbcTemplate注入DataSource
            @Service
            public class UserService {
                @Autowired
                private UserDao userDao;
            }

            public class UserDaoImpl implements UserDao {
                @Autowired
                private JdbcTemplate jdbcTemplate;
            }
   ### 3、在dao创建两个方法：多钱和少钱的方法，在service创建方法（转账的方法）
        //lucy少100
        @Override
            public void reduceMoney() {
                String sql="update t_account set money=money-? where username=?";
                jdbcTemplate.update(sql,100,"lucy");
            }
            //mary多100
            @Override
            public void addMoney() {
                String sql="update t_account set money=money+? where username=?";
                jdbcTemplate.update(sql,100,"mary");
            }
        //service中调用转账的方法
        public void accountMoney(){
                userDao.reduceMoney();
                userDao.addMoney();
            }
   ### 4、上面代码，如果正常执行没有问题，但是如果代码执行过程中出现异常，就有问题
        （1）上面问题如何解决？
            *使用事务解决
        （2）事务操作过程
        public void accountMoney(){
                try {
                    //第一步 开启事务

                    //第二步 进行业务操作

                    userDao.reduceMoney();

                    //模拟异常
                    int i = 1 / 0;

                    userDao.addMoney();

                    //第三步 没有发生异常，提交事务
                }catch (Exception e){
                    //第四步 出现异常 事务回滚
                }
            }
 ## 事务操作（Spring事务管理操作）
   ### 1、事务添加到JavaEE三层结构里面Service层（业务逻辑层）
   ### 2、在Spring进行事务管理操作
        （1）有两种方式：编程式事务管理，声明式事务管理（通常使用）
   ### 3、声明式事务管理
        （1）基于注解方式（方便）
        （2）基于xml配置文件方式
   ### 4、在spring进行声明式事务管理，底层使用AOP原理
   ### 5、Spring事务管理API
        （1）提供了一个接口（PlatformTransactionManager），代表事务管理器，这个接口针对不同的框架提供不同的实现类
## 事务操作（注解声明式事务管理）
   ### 1、在spring配置文件中配置事务管理器
        <!--创建事务管理器-->
            <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
                <!--注入数据源-->
                <property name="dataSource" ref="dataSource"></property>
            </bean>
   ### 2、在spring配置文件，开启事务注解
        （1）在spring配置文件引入名称空间tx
            xmlns:tx="http://www.springframework.org/schema/tx"
            http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
        （2）开启事务注解
        <!--开启事务注解-->
            <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
   ### 3、在service类上面（service类里面方法上面）添加事务注解
        （1）@Transactional（可以添加到类上面，也可以添加到方法上面）
        （2）如果这个注解添加到类上面，这个类里面所有的方法都添加上了事务
        （3）如果这个注解添加到方法上面，为这个方法添加了事务
            @Service
            @Transactional
            public class UserService {
## 事务管理（声明式事务管理参数配置）
   ### 1、在service类上面添加@Transactional，在这个注解里面可以配置事务相关参数
        （1）propagation：事务传播行为
            1.多事务方法之间进行调用，这个过程是如何进行管理的
            2.@Service
              @Transactional(propagation = Propagation.REQUIRED )
              public class UserService {
        （2）isolation：事务隔离级别
            1.事务有特性成为隔离性，多事务操作之间不会产生影响。不考虑隔离产生很多问题
            2.有三个问题：脏读、不可重复读、虚（幻）读
                *脏读：一个未提交事务读取到另一个未提交事务的数据
                *不可重复读：一个未提交的事务读取到另一个提交事务修改数据
                *虚（幻）读：一个未提交事务读取到另一个提交事务添加数据
            3.解决：通过设置事务隔离级别，解决问题
            @Service
            @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
            public class UserService {
        （3）timeout：超时时间
            1.事务需要在一定的时间内进行提交，如果不提交进行回滚
            2.默认值-1(不超时)，设置时间以秒为单位进行计算
        （4）readOnly：是否只读
            1.读：查询操作，写：添加修改删除操作
            2.readOnly默认值false，表示可以查询，可以添加修改删除
            3.设置readOnly值为true，就只能查询
        （5）rollbackFor：回滚
            1.设置出现哪些异常进行回滚（异常类型+class）
            2.示例：RollbackFor = NullPointerException.class
        （6）noRollbackFor：不回滚
            1.设置出现哪些异常不进行回滚（异常类型+class）
            2.示例：noRollbackFor = NullPointerException.class
## 事务操作（xml声明式事务管理）
   ### 1、在spring配置文件中进行配置
        第一步 配置事务管理器
        第二步 配置通知
        第三步 配置切入点和切面
        <!--创建事务管理器-->
            <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
                <!--注入数据源-->
                <property name="dataSource" ref="dataSource"></property>
            </bean>
            <!--配置通知-->
            <tx:advice id="txadvice">
                <tx:attributes>
                    <!--指定哪种规则的方法上面添加事务-->
                    <tx:method name="accountMoney" propagation="REQUIRED"/>
                    <!--<tx:method name="account*"/>-->
                </tx:attributes>
            </tx:advice>
            <!--配置切入点和切面-->
            <aop:config>
                <!--配置切入点-->
                <aop:pointcut id="pt" expression="execution(* zh.spring5.service.UserService.*(..))"/>
                <!--配置切面-->
                <aop:advisor advice-ref="txadvice" pointcut-ref="pt"/>
            </aop:config>
## 事务操作（完全注解声明式事务管理）
   ### 1、创建配置类
        @Configuration//配置类
        @ComponentScan(basePackages = "zh.spring5")//组件扫描
        @EnableTransactionManagement//开启事务
        public class TxConfig {
            //创建数据库的连接池
            @Bean
            public DruidDataSource getDruidDataSource(){
                DruidDataSource dataSource=new DruidDataSource();
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql:///user_db");
                dataSource.setUsername("root");
                dataSource.setPassword("root");
                return dataSource;
            }
            //创建JdbcTemplate对象
            @Bean
            public JdbcTemplate getJdbcTemplate(DataSource dataSource){
                //到IOC容器中根据类型找到dataSource
                JdbcTemplate jdbcTemplate=new JdbcTemplate();
                //注入dataSource
                jdbcTemplate.setDataSource(dataSource);
                return jdbcTemplate;
            }
            //创建事务管理器
            @Bean
            public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource){
                DataSourceTransactionManager transactionManager=new DataSourceTransactionManager();
                transactionManager.setDataSource(dataSource);
                return transactionManager;
            }
        }

## Spring5框架新功能
   ### 1、整个Spring5框架的代码基于Java8，运行时兼容JDK9，许多不建议使用的类和方法在代码库中删除
   ### 2、Spring5框架自带通用的日志封装
        （1）Spring5已经移除了Log4jConfigListener，官方建议使用Log4j2
        （2）Spring5框架整合Log4j2
            第一步 引入jar包
                log4j-api-2.11.2.jar
                log4j-core-2.11.2.jar
                log4j-slf4j-impl-2.11.2.jar
                slf4j-api-1.7.30.jar
            第二步 创建log4j2.xml配置文件
                <?xml version="1.0" encoding="UTF-8"?>
                <!--日志优先级：OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
                <!--Configuration后面的status用于设置log4j2自身内部的信息输出，可以不设置，当设置为trace时，可以看到log4j2内部各种详细输出-->
                <Configuration status="INFO">
                    <!--先定义所有的appenders-->
                    <Appenders>
                        <!--输出日志信息到控制台-->
                        <Console name="Console" target="SYSTEM_OUT">
                            <!--控制日志输出格式-->
                            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
                        </Console>
                    </Appenders>
                    <!--然后定义logger,只有定义了logger并引入的appender才会生效-->
                    <!--root：用于指定项目的根目录，如果没有单独指定logger，则会使用root作为默认的日志输出-->
                    <Loggers>
                        <Root level="info">
                            <AppenderRef ref="Console"/>
                        </Root>
                    </Loggers>
                </Configuration>
   ### 3、Spring5框架核心容器支持@Nullable注解
        （1）@Nullable注解可以使用在方法上面，属性上面，参数上面，表示方法返回值可以为空，属性值可以为空
   ### 4、Spring5核心容器支持函数式风格GenericApplicationContext
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
   ### 5、Spring5支持整合JUnit5（测试方面）
        （1）整合JUnit4
            第一步 引入Spring相关针对测试依赖
                spring-test-5.2.6.RELEASE.jar
                JUnit4库
            第二步 创建测试类，使用注解方式完成
                @RunWith(SpringJUnit4ClassRunner.class)//指定单元测试框架
                @ContextConfiguration("classpath:bean1.xml")//加载配置文件
                public class JTest4 {
                    @Autowired
                    private UserService userService;
                    @Test//使用的是import org.junit.Test;
                    public void test1(){
                        userService.accountMoney();
                    }
                }
        （2）Spring5整合JUnit5
            第一步 引入JUnit5的依赖（JUnit5.3）
                @ExtendWith(SpringExtension.class)
                @ContextConfiguration("classpath:bean1.xml")
                public class JTest5 {
                    @Autowired
                    private UserService userService;
                    @Test//使用的是import org.junit.jupiter.api.Test;
                    public void test1(){
                        userService.accountMoney();
                    }
                }
        （3）使用复合注解代替上面两个注解完成整合
            @SpringJUnitConfig(locations = "classpath:bean1.xml")
            public class JTest5 {
                @Autowired
                private UserService userService;
                @Test
                public void test1(){
                    userService.accountMoney();
                }
            }
