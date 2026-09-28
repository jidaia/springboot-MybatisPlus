package com.yangli.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangli.mp.domain.po.User;
import com.yangli.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("luo1");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(new UserInfo(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectByIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(4L);
    }

    @Test
    void testQueryWrapper(){
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id","username","info","balance")
                .like("username","o")
                .ge("balance",1000);
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWrapper(){
        User user = new User();
        user.setBalance(2000);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username","jack");
        userMapper.update(user,wrapper);
    }

    @Test
    void testUpdateWrapper(){
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance=balance-200")
                .in("id", List.of(1L, 2L, 3L, 4L));
        userMapper.update(null ,wrapper);
    }


   //推荐使用LambdaQueryMapper 避免硬编码 常用来构建select delete update where 部分
   //LambdaUpdateWrapper 在有特殊的set语句才会使用
    @Test
    void testLambdaQueryWrapper(){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId,User::getUsername,User::getInfo,User::getBalance)
                .like(User::getUsername,"o")
                .ge(User::getBalance,1000);
        List<User> users = userMapper.selectList(userLambdaQueryWrapper);
        users.forEach(System.out::println);
    }


    /*
    * 自定义SQL
    * */
    @Test
    void testCustomUpdate(){
        //1.跟新条件
        List<Integer> ids = List.of(1, 2, 3);
        int amount = 200;
        //2.自定义条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getId,ids);
        // 3.调用自定义方法
        userMapper.updateBalanceByIds(wrapper,amount);
    }

    @Test
    void testPage() {
        Page<User> page = new Page<>(1, 2);
        page.addOrder(OrderItem.asc("balance"),OrderItem.desc("id"));   // ← 必须在 selectPage 之前
        Page<User> result = userMapper.selectPage(page, null);
        System.out.println("总条数 total = " + result.getTotal());
        System.out.println("总页数 pages = " + result.getPages());
        System.out.println("当前页数据 = ");
        result.getRecords().forEach(System.out::println);
    }

}
