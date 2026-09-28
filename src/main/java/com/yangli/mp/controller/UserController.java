package com.yangli.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.yangli.mp.domain.po.User;
import com.yangli.mp.dto.PageDto;
import com.yangli.mp.dto.UserFormDTO;
import com.yangli.mp.query.PageQuery;
import com.yangli.mp.query.UserQuery;
import com.yangli.mp.service.IUserService;
import com.yangli.mp.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "用户管理接口")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(summary = "新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        //1. 把DTO拷贝到po
        User user = new User();
        BeanUtils.copyProperties(userFormDTO, user);
        //2. 新增
        userService.save(user);
    }


    @Operation(summary = "删除用户接口")
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id ) {
        userService.removeById(id);
    }


    @Operation(summary = "根据id查询用户接口")
    @GetMapping("/{id}")
    public UserVO queryUserById(@PathVariable long id) {
        //1. 查询用户po
        User user = userService.getById(id);
        //2.把po拷贝到vo
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


    @Operation(summary = "根据id批量查询")
    @GetMapping
    public List<UserVO> queryUserByIds(@RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }


    @Operation(summary = "扣减用户余额")
    @PutMapping("/{id}/deduction")
    public void deductMoneyById(@PathVariable("id") long id, @RequestParam("money") Integer money) {
        userService.deductBalance(id, money);
    }

    @Operation(summary = "根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery query) {
       List<User> users = userService.queryUsers(query.getName(),query.getStatus(),query.getMinBalance(),query.getMaxBalance());
       return BeanUtil.copyToList(users,UserVO.class);
    }

    @Operation(summary = "条件分页查询")
    @GetMapping("/page")
    public PageDto<UserVO> queryUserPage(UserQuery query) {
        return userService.queryUserPages(query);
    }

}
