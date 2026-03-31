package com.xiaobaitiao.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 景点表视图
 *
 * @author toxi
 * 
 */
@Data
public class SpotVO implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * adminId
     */
    private Long adminId;
    /**
     * 景点名
     */
    private String spotName;

    /**
     * 景点封面图
     */
    private String spotAvatar;

    /**
     * 景点所在地
     */
    private String spotLocation;
    /**
     * 景点介绍
     */
    private String spotDescription;

    /**
     * 景点纬度
     */
    private BigDecimal latitude;

    /**
     * 景点经度
     */
    private BigDecimal longitude;

    /**
     * 建议游玩时长（分钟）
     */
    private Integer visitDurationMinutes;

    /**
     * 开放时间，格式 HH:mm
     */
    private String openTime;

    /**
     * 关闭时间，格式 HH:mm
     */
    private String closeTime;

    /**
     * 景点标签（JSON字符串数组）
     */
    private List<String> spotTagList;
    /**
     * 收藏量
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 景点状态（1开放，0关闭，默认关闭）
     */
    private Integer spotStatus;

    /**
     * 推荐理由
     */
    private String recommendReason;

    /**
     * 推荐游玩时段
     */
    private String recommendedTimeBlock;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;



    private static final long serialVersionUID = 1L;
    /**
     * 封装类转对象
     *
     * @param spotVO
     * @return
     */
    public static Spot voToObj(SpotVO spotVO) {
        if (spotVO == null) {
            return null;
        }
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotVO, spot);
        List<String> tagList = spotVO.getSpotTagList();
        spot.setSpotTags(JSONUtil.toJsonStr(tagList));
        return spot;
    }

    /**
     * 对象转封装类
     *
     * @param spot
     * @return
     */
    public static SpotVO objToVo(Spot spot) {
        if (spot == null) {
            return null;
        }
        SpotVO spotVO = new SpotVO();
        BeanUtils.copyProperties(spot, spotVO);
        spotVO.setSpotTagList(JSONUtil.toList(spot.getSpotTags(), String.class));
        return spotVO;
    }
}
