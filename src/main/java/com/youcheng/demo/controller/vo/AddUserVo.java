package com.youcheng.demo.controller.vo;

import com.youcheng.demo.enums.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserVo {
    private Long userId;
    private List<String> endpoint;
}
