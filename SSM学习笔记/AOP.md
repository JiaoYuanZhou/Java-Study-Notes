### 什么是AOP？

Aspect Oriented Programming ：面向切面编程

通过**预编译方式**和**运行期动态代理**实现程序功能的一种技术



### AOP动态代理技术

- JDK动态代理
- cglib动态代理



### AOP相关概念

- Target（目标对象）：代理的目标对象
- Proxy（代理）：一个类被 AOP 织入增强后，就产生一个结果代理类
- Joinpoint（连接点）：所谓连接点是指那些被拦截到的点。在spring中,这些点指的是方法，因为spring只支持方法类型的连接点
- Pointcut（切入点）：所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义
- Advice（通知/增强）：所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知
- Aspect（切面）：是切入点和通知（引介）的结合
- Weaving（织入）：是指把增强应用到目标对象来创建新的代理对象的过程。spring采用动态代理织入，而AspectJ采用编译期织入和类装载期织入



### 基于XML的AOP开发

#### 开发步骤：

1. 导入AOP相关坐标
2. 创建目标接口和目标类（内部有切点）
3. 创建切面类（内部有增强方法）
4. 将目标类和切面类的对象创建权交给spring
5. 在applicationContext.xml中织入关系



### 基于注解的AOP开发

@Aspect：表示哪个是切面类

@Before（"execution(* com.itheima.aop.*.*(..))"）：在增强方法上面写

表示哪个是增强方法和切点

