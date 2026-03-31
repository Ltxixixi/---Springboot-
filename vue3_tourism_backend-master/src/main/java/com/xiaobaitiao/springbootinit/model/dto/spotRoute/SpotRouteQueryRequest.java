package com.xiaobaitiao.springbootinit.model.dto.spotRoute;

import com.xiaobaitiao.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询景点路线表请求
 *
 * @author toxi
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpotRouteQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * adminId
     */
    private Long adminId;
    /**
     * 景点 id（字符串数组，用逗号分割，顺序从前往后)
     */
    private List<String> spotIdList;

    /**
     * 路线封面图
     */
    private String spotRouteAvatar;

    /**
     * 路线描述
     */
    private String spotRouteDescription;



    private static final long serialVersionUID = 1L;
}