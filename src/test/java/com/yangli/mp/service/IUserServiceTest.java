package com.yangli.mp.service;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.yangli.mp.domain.po.User;
import com.yangli.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    void testsaveUser() {
        User user = new User();
        user.setUsername("pomian");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(new UserInfo(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
    }

    @Test
    void testQuery() {
        List<User> users = userService.listByIds(List.of(1, 2, 4));
        for (User user : users) {
            System.out.println(user);
        }
    }

    public User buildUser(int i) {
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone(""+(18688990011L+i));
        user.setBalance(2000);
        user.setInfo(new UserInfo(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时: "+(e - b));
    }


    //要批处理 配置文件 rewriteBatchedStatements=true
    @Test
    void testSaveBatch() {
        //100000 每次插入1000 插100次
        List<User> users = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <=100000; i++) {
            users.add(buildUser(i));
            if(i%1000==0){
                userService.saveBatch(users);
                users.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时: "+(e - b));
    }

    @Test
    void testDbGetById() {
        User user = Db.getById(1L, User.class);
        System.out.println("user = " + user);
    }

    @Test
    void testDbLambdaQuery() {
        List<User> users = Db.lambdaQuery(User.class)
                .like(User::getUsername, "o")
                .list();
        users.forEach(System.out::println);
    }

    @Test
    void testDbCount() {
        long count = Db.count(User.class);
        System.out.println("总用户数 = " + count);
    }

    @Test
    void testDbSaveUpdateDelete() {
        //1. 新增（用户名带时间戳，保证不重复）
        User user = new User();
        user.setUsername("db_test_" + System.currentTimeMillis());
        user.setPassword("123");
        user.setPhone("13800138000");
        user.setBalance(6666);
        user.setInfo(new UserInfo(20, null, null));
        Db.save(user);
        System.out.println("新增成功，id = " + user.getId());

        //2. 修改
        user.setBalance(8888);
        Db.updateById(user);
        System.out.println("修改后 = " + Db.getById(user.getId(), User.class));

        //3. 删除
        Db.removeById(user.getId(), User.class);
        System.out.println("删除成功");
    }
}
