# MyBatis Race Condition

This project can reproduce the race condition when MyBatis builds `mappedStatements`:

1. Set a breakpoint on line 709 of `HashMap`(Oracle JDK 1.8.0_321), and set the breakpoint pause mode to `Thread`, and set the breakpoint condition to: `this.getClass().getName().equals("org.apache.ibatis.session.Configuration$StrictMap")`.
2. Start `me.tianshuang.MybatisRaceConditionApplication` in debug mode.
3. Whenever a breakpoint is reached, hit `Resume Program` and watch the console output, because `table` does not contain the `volatile` modifier, other threads may not be able to see the latest `table` in real time, so we may need hit `Resume Program` multiple times, and when other threads access the latest `table` and the migration of elements in the `oldTab` has not yet completed, `org.apache.ibatis.session.Configuration.StrictMap#get` will throw `java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for me.tianshuang.mapper.Mapper1.select1`.


Example of breakpoint setting:

![resize-breakpoint-1](https://storage.tianshuang.me/2022/10/resize-breakpoint-1.jpg)

Breakpoint detail:

![resize-breakpoint-2](https://storage.tianshuang.me/2022/10/resize-breakpoint-2.jpg)


The exception information caused by race condition is as follows:

```text
current i: 4303256
org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for me.tianshuang.mapper.Mapper1.select1
### Cause: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for me.tianshuang.mapper.Mapper1.select1
	at org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:96)
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:441)
	at com.sun.proxy.$Proxy47.selectOne(Unknown Source)
	at org.mybatis.spring.SqlSessionTemplate.selectOne(SqlSessionTemplate.java:160)
	at org.apache.ibatis.binding.MapperMethod.execute(MapperMethod.java:87)
	at org.apache.ibatis.binding.MapperProxy$PlainMethodInvoker.invoke(MapperProxy.java:145)
	at org.apache.ibatis.binding.MapperProxy.invoke(MapperProxy.java:86)
	at com.sun.proxy.$Proxy50.select1(Unknown Source)
	at me.tianshuang.MybatisRaceConditionApplication.lambda$raceConditionTest$0(MybatisRaceConditionApplication.java:25)
	at java.lang.Thread.run(Thread.java:750)
Caused by: org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for me.tianshuang.mapper.Mapper1.select1
### Cause: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for me.tianshuang.mapper.Mapper1.select1
	at org.apache.ibatis.exceptions.ExceptionFactory.wrapException(ExceptionFactory.java:30)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:153)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:145)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:140)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(DefaultSqlSession.java:76)
	at sun.reflect.GeneratedMethodAccessor34.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:427)
	... 8 more
Caused by: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for me.tianshuang.mapper.Mapper1.select1
	at org.apache.ibatis.session.Configuration$StrictMap.get(Configuration.java:1054)
	at org.apache.ibatis.session.Configuration.getMappedStatement(Configuration.java:844)
	at org.apache.ibatis.session.Configuration.getMappedStatement(Configuration.java:837)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:150)
	... 15 more
```
