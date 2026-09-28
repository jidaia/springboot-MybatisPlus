package com.yangli.mp.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "分页查询实体")
@Data
public class PageQuery {
    @Schema(description = "页码")
    private  Integer pageNo;
    @Schema(description = "每页查询的数量")
    private  Integer pageSize;
    @Schema(description = "排序字段")
    private  String sortBy;
    @Schema(description = "是否升序")
    private  Boolean istAsc;
}
