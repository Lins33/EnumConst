# EnumCons

先编译包目录下的注解处理器BosEnumProcessor  
编译语句：`javac -encoding UTF-8 com\enumconst\BosEnumProcessor.java`


再编译带注解的类， -processor + 注解处理器的包位置+*.java  
编译语句：`javac -encoding UTF-8 -processor com.enumconst.BosEnumProcessor com\enumconst\*.java`
