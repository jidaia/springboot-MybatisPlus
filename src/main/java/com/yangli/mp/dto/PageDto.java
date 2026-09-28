package com.yangli.mp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分页结果")
public class PageDto<T> {
    @Schema(description = "总条数")
    private Long total;
    @Schema(description = "总页数")
    private Long pages;
    @Schema(description = "对象集合")
    private List<T> result;
}
