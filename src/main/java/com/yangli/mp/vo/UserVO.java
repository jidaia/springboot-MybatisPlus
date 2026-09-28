package com.yangli.mp.vo;

import com.yangli.mp.domain.po.UserInfo;
import com.yangli.mp.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//vo  view object 返回给前端的数据
@Data
@Schema(description = "用户VO实体")
public class UserVO {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "详细信息")
    private UserInfo info;

    @Schema(description = "使用状态（1正常 2冻结）")
    private UserStatus status;

    @Schema(description = "账户余额")
    private Integer balance;
}
