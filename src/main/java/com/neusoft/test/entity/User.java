package com.neusoft.test.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auth lyn
 * @Date 2022/3/24 16:34
 * version 1.0
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "User类的value",description = "user类的description")
public class User {
    @ApiModelProperty(value = "姓名",name = "name",example = "武晶晶",required = true)
    private String name;
    @ApiModelProperty(value="喜欢的人",name="lover",example = "李亚南")
    private String lover;
}
