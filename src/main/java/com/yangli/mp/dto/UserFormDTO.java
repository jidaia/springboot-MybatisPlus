package com.yangli.mp.dto;

import com.yangli.mp.domain.po.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//dto 前端传过来的数据格式 data transfer object
@Data
@Schema(description = "用户表单实体")
public class UserFormDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "注册手机号")
    private String phone;

    @Schema(description = "详细信息，JSON对象")
    private UserInfo info;

    @Schema(description = "账户余额")
    private Integer balance;
}
