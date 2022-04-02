package com.neusoft.test;


import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Auth lyn
 * @Date 2022/3/31 8:54
 * version 1.0
 */

public class Jdk8NewFutureTest {
    /**
     * 测试方法引用:本质上就是用引用的方法的方法体来创建对应的函数式接口的匿名内部类
     * (对应方法的参数列表)->{引用的方法的方法体}
     */
    //1、构造方法引用: UserInfo::new本质上是用UserInfo类的构造方法来实现目标接口的对应方法,
    //会自动根据目标方法的参数列表选择对应参数列表的构造方法
    @Test
    public void test1(){
        Supplier<UserInfo> supplier = UserInfo::new;
        UserInfo userInfo = supplier.get();
        System.out.println(userInfo);

        BiConsumer<String,Integer> biConsumer = UserInfo::new;
        biConsumer.accept("武晶晶",25);

    }

    //2、静态方法引用：UserInfo::getStaticSomething；需要注意的是接收的方法的方法列表要和引用方法的参数列表一致
    @Test
    public void test2(){
        BiFunction<String,Integer,UserInfo> biFunction = UserInfo::getStaticSomething;
        //下面的语句效果一样
        BiFunction<String,Integer,UserInfo> biFunction2 = (name,age)->{return UserInfo.getStaticSomething(name,age);};
        System.out.println(biFunction.apply("武晶晶",25));
        System.out.println(biFunction2.apply("李亚南",25));
    }

    //3、用类名引用实例方法：<函数式接口> <变量名> = <被引用类>::<被引用类的实例方法名>
    //与引用类方法不同的是，接口的方法第一个参数应该是被引用类的实例，从第二个参数开始
    //与被引用类的实列方法的参数列表相同
    //（1）首先定义一个函数式接口
    @FunctionalInterface
    public interface RefClassInstance{
        String returnLoveString(UserInfo userInfo,String name1, String name2);
    }
    //(2)使用方法引用创建该接口的实例即可。
    @Test
    public void test3(){
        RefClassInstance refClassInstance = UserInfo::getMyLoveString;
        String s = refClassInstance.returnLoveString(new UserInfo(), "李亚南", "武晶晶");
        System.out.println(s);
    }

    //4、实例对象引用实例方法.如下所示只需要userInfo的getMyLoveString方法和BiFunction的apply方法参数列表一致，即可自动创建
    @Test
    public void test4(){
        UserInfo userInfo = new UserInfo();
        BiFunction<String,String,String> biFunction =userInfo::getMyLoveString;
        String s = biFunction.apply("武晶晶", "李亚南");
        System.out.println(s);
    }

    public static class UserInfo{
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public UserInfo() {
            name="李亚南";
            age=25;
        }

        public UserInfo(String name, Integer age) {
            this.name = name;
            this.age = age;
            System.out.println(this);
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        public static UserInfo getStaticSomething(String name,Integer age){
            return new UserInfo(name,age);
        }

        public String getMyLoveString(String name1,String name2){
            return name1+" love "+name2;
        }
    }


    //*******************************************************************//
    /**
     * 测试Stream
     */
    //*******************************************************************//

    @Test
    public void testStream(){
        List<UserInfo> userInfoList = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++){
            userInfoList.add(new UserInfo("name"+i,(int)Math.round(Math.random()*100)));
        }
        userInfoList.forEach(System.out::println);
        long sum = userInfoList.stream().filter(userInfo -> userInfo.getAge() <50)
                .mapToInt(UserInfo::getAge).sum();

        System.out.println(sum);
        System.out.println();
        userInfoList.stream().filter(userInfo -> userInfo.getAge() < 50)
                .mapToInt(UserInfo::getAge)
                .forEach(System.out::println);
        System.out.println();
        userInfoList.stream().sorted((u1,u2)->u1.getAge()-u2.getAge())
                .forEach(System.out::println);
    }

    @Test
    public void testStream2(){
        //Stream.of方法生成Stream
        Stream.of(1,2,3,5)
                .peek(e->System.out.println("elements:"+e))
                .collect(Collectors.toList());
        //Stream.generate()方法生成Stream
        Stream stream1 = Stream.generate(Math::random);//生成无限元素的集合
        Stream stream2 = Stream.generate(()->Math.random());
        Stream stream3 = Stream.generate(new Supplier<Double>() {
            public Double get(){
                return Math.random();
            }
        });
        stream1.limit(20).forEach(System.out::println);//取前20个打印
        //Stream.ieterate方法生成Stream
        Stream.iterate(1,item->item+1).limit(20).forEach(System.out::println);
        //还有就是根据上面那个方法中的集合Collection.stream()方法获得了。
    }

    @Test
    public void testStream3(){
       /* Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList()); */
       //测试Stream和ParallelStream
        int max = 1000000;
        List<String> list = new ArrayList<>(max);
        for(int i=0;i<max;i++){
            list.add(UUID.randomUUID().toString());
        }
        //串行排序
        long t0 = System.nanoTime();
        long count = list.stream().sorted().count();
        System.out.println(count);
        long t1 = System.nanoTime();
        long mills = TimeUnit.NANOSECONDS.toMillis(t1-t0);
        System.out.println(String.format("Sequential sort took %d mills",mills));
        //并行排序
        long t2 = System.nanoTime();
        long count1 = list.parallelStream().sorted().count();
        System.out.println(count1);
        long t3 = System.nanoTime();
        long mills2 = TimeUnit.NANOSECONDS.toMillis(t3-t2);
        System.out.println(String.format("Parallel sort took %d mills",mills2));
    }

    @Test
    public void testStream4(){
        List<Integer> list = new ArrayList<>();
        list.add(123);
        list.add(1);
        list.add(23);
        list.add(53);
        list.add(58);
        list.add(17);
        list.add(26);
        list.add(23);
        list.add(1);
        //Stream的一些常用api
        System.out.println("count:"+list.parallelStream().count());
        System.out.println("max:"+list.parallelStream().max((a,b)->a-b).get());
        System.out.println("min:"+list.parallelStream().min((a,b)->a-b).get());
        System.out.println("排序后的结果：");
        list.stream().sorted((a,b)->a-b).forEach(System.out::println);  //排序
        System.out.println("去重后的结果：");
        list.parallelStream().distinct().forEach(System.out::println);//去重
        System.out.println("10-100之间的书：");
        list.parallelStream().filter(a->a>10).filter(a->a<100).forEach(System.out::println);//筛选出10和100之间的元素
        System.out.println("reduce计和："+list.parallelStream().reduce(0,Integer::sum));//计和
        System.out.println("IntStream计和："+list.parallelStream().mapToInt(Integer::intValue).sum());//转换成IntStream再计和
        System.out.println("集合元素平方加2的结果：");
        list.stream().map(e->e*e+2).forEach(System.out::println);//对集合元素转换一下
        System.out.println("从第二个开始取5个元素");
        list.stream().skip(2).limit(5).forEach(System.out::println);//跳过前两个元素，然后取5个元素
        System.out.println("Match匹配");
        System.out.println("AnyMatch(至少有一个大于5的吗):"+list.stream().anyMatch(a->a>5));
        System.out.println("AllMatch(都大于5吗):"+list.stream().allMatch(a->a>5));
        System.out.println("NoneMatch(都不大于5吗):"+list.stream().noneMatch(a->a>5));
        //reduce规约，将多个元素规约为一个元素
        System.out.println("reduce:"+list.stream().reduce((a,b)->a+b).get());
        //collect
        System.out.println("collect(Collectors.toList):"+list.stream().limit(5).collect(Collectors.toList()));
        System.out.println(Stream.generate(UserInfo::new).limit(10).collect(Collectors.groupingBy(UserInfo::getName)));


    }

}
