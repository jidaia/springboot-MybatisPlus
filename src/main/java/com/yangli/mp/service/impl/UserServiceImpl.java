package com.yangli.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.yangli.mp.domain.po.User;
import com.yangli.mp.dto.PageDto;
import com.yangli.mp.enums.UserStatus;
import com.yangli.mp.mapper.UserMapper;
import com.yangli.mp.query.UserQuery;
import com.yangli.mp.service.IUserService;
import com.yangli.mp.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deductBalance(long id, Integer money) {
        // 1.查询用户
        User user = getById(id);

        //2. 效验用户状态
        if (user == null || user.getStatus()==UserStatus.FROZEN) {
            throw  new  RuntimeException("用户状态异常！");
        }

        //3. 效验用户的余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足!");
        }

        //4. 扣减余额 一定要加 update
        int remianBalance = user.getBalance() - money;
        user.setBalance(remianBalance);
        lambdaUpdate()
                .set(User::getBalance, remianBalance)
                .set(remianBalance==0,User::getStatus,UserStatus.FROZEN)
                .eq(User::getId,id)
                .eq(User::getId,user.getId()) //乐观锁
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        List<User> list = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, toUserStatus(status))
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
        return list;
    }

    /**
     * Integer 状态值转枚举（查询入口参数是 Integer，转成枚举再交给 MP）
     */
    private UserStatus toUserStatus(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserStatus s : UserStatus.values()) {
            if (s.getValue() == value) {
                return s;
            }
        }
        return null;
    }

    @Override
    public PageDto<UserVO> queryUserPages(UserQuery query) {
        //1.构建分页参数（没传就默认第1页、每页10条）
        Integer pageNo = query.getPageNo() == null ? 1 : query.getPageNo();
        Integer pageSize = query.getPageSize() == null ? 10 : query.getPageSize();
        Page<User> page = new Page<>(pageNo, pageSize);

        //2.构建排序（默认按 update_time；sortBy 必须过白名单，防止 SQL 注入）
        boolean asc = query.getIstAsc() == null || query.getIstAsc();
        String sortBy = query.getSortBy();
        if (sortBy != null && isSafeSortColumn(sortBy)) {
            page.addOrder(asc ? OrderItem.asc(sortBy) : OrderItem.desc(sortBy));
        } else {
            page.addOrder(asc ? OrderItem.asc("update_time") : OrderItem.desc("update_time"));
        }

        //3.条件 + 分页查询
        Page<User> page1 = lambdaQuery()
                .like(query.getName() != null, User::getUsername, query.getName())
                .eq(query.getStatus() != null, User::getStatus, toUserStatus(query.getStatus()))
                .ge(query.getMinBalance() != null, User::getBalance, query.getMinBalance())
                .le(query.getMaxBalance() != null, User::getBalance, query.getMaxBalance())
                .page(page);

        //4.转 VO 返回
        PageDto<UserVO> dto = new PageDto<>();
        dto.setTotal(page1.getTotal());
        dto.setPages(page1.getPages());
        dto.setResult(BeanUtil.copyToList(page1.getRecords(), UserVO.class));
        return dto;
    }

    /**
     * 排序字段白名单：OrderItem 的列名会直接拼进 SQL，必须限制合法列，防止注入
     */
    private boolean isSafeSortColumn(String column) {
        return "id".equals(column) || "username".equals(column)
                || "balance".equals(column) || "create_time".equals(column)
                || "update_time".equals(column);
    }
}
