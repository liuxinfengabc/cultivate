package com.lz.ht.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * cost_project
 * @author 
 */
@Data
public class CostProject{
    private Integer projectId;

    private Date projectPlantime;

    private String projectName;

    private Integer projectNum;

    private Integer projectMoney;

    private BigDecimal projectInmoney;

    private Date startDate;

    private String userName;

    private Integer customerId;

    private Integer userId;

    private static final long serialVersionUID = 1L;
}