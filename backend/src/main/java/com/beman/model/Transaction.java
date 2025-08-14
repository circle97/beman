package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("transaction")
public class Transaction {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 交易类型：1-收入，2-支出
     */
    @TableField("type")
    private Integer type;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 分类名称（冗余字段）
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 分类图标
     */
    @TableField("category_icon")
    private String categoryIcon;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 交易日期
     */
    @TableField("transaction_date")
    private LocalDateTime transactionDate;

    /**
     * 支付方式：1-现金，2-支付宝，3-微信，4-银行卡
     */
    @TableField("payment_method")
    private Integer paymentMethod;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：0-待确认，1-已确认，2-已取消
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
