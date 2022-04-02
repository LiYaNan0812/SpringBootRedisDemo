package com.neusoft.test.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auth lyn
 * @Date 2022/3/24 16:34
 * version 1.0
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "User类的value",description = "user类的description")
public class User {
    @ApiModelProperty(value = "姓名",name = "name",example = "武晶晶",required = true)
    private String name;
    @ApiModelProperty(value="喜欢的人",name="lover",example = "李亚南")
    private String lover;
    public User(){
        name="李亚南";
        lover="武晶晶";
    }

    @Override
    public String toString() {
        return "User["+name+" ever love "+lover+"]";
    }
}
