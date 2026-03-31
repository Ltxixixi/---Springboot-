package com.xiaobaitiao.springbootinit.model.dto.spotOrder;

import com.xiaobaitiao.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 查询景点订单表请求
 *
 * @author toxi
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpotOrderQueryRequest extends PageRequest implements Serializable {


    /**
     * id
     */
    private Long id;



    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 门票id
     */
    private Long spotFeeId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 支付状态（1已支付，0未支付）默认0
     */
    private Integer payStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}