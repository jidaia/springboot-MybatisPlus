package com.yangli.mp.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.yangli.mp.domain.po.User;
import com.yangli.mp.dto.PageDto;
import com.yangli.mp.query.UserQuery;
import com.yangli.mp.vo.UserVO;

import java.util.List;

//点接口名 alt+enter 自动创建单元·测试类
public interface IUserService extends IService<User> {
    void deductBalance(long id, Integer money);

    List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance);

    PageDto<UserVO> queryUserPages(UserQuery query);
}
