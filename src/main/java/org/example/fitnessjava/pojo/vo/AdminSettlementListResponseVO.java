package org.example.fitnessjava.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminSettlementListResponseVO {
    private List<AdminSettlementListItemVO> items;
    private Long total;
    private Integer page;
    private Integer pageSize;
}
