package com.xiaobaitiao.springbootinit.model.dto.spot;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新景点表请求
 *
 * @author toxi
 * 
 */
@Data
public class SpotUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}