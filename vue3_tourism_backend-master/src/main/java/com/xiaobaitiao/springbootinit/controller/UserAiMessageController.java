package com.xiaobaitiao.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaobaitiao.springbootinit.annotation.AuthCheck;
import com.xiaobaitiao.springbootinit.common.BaseResponse;
import com.xiaobaitiao.springbootinit.common.DeleteRequest;
import com.xiaobaitiao.springbootinit.common.ErrorCode;
import com.xiaobaitiao.springbootinit.common.ResultUtils;
import com.xiaobaitiao.springbootinit.constant.UserConstant;
import com.xiaobaitiao.springbootinit.exception.BusinessException;
import com.xiaobaitiao.springbootinit.exception.ThrowUtils;
import com.xiaobaitiao.springbootinit.manager.TourismAiClient;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageAddRequest;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageEditRequest;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageQueryRequest;
import com.xiaobaitiao.springbootinit.model.dto.userAiMessage.UserAiMessageUpdateRequest;
import com.xiaobaitiao.springbootinit.model.entity.Spot;
import com.xiaobaitiao.springbootinit.model.entity.User;
import com.xiaobaitiao.springbootinit.model.entity.UserAiMessage;
import com.xiaobaitiao.springbootinit.model.vo.UserAiMessageVO;
import com.xiaobaitiao.springbootinit.service.SpotService;
import com.xiaobaitiao.springbootinit.service.UserAiMessageService;
import com.xiaobaitiao.springbootinit.service.UserService;
import com.xiaobaitiao.springbootinit.utils.WordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户对话表接口
 *
 * @author toxi
 * 
 */
@RestController
@RequestMapping("/userAiMessage")
@Slf4j
public class UserAiMessageController {

    @Resource
    private UserAiMessageService userAiMessageService;
    @Resource
    private SpotService spotService;
    @Resource
    private UserService userService;
    @Resource
    private TourismAiClient tourismAiClient;

    // region 增删改查

    /**
     * 创建用户对话表
     *
     * @param userAiMessageAddRequest
     * @param request
     * @return UserAiMessage
     */
    @PostMapping("/add")
    public BaseResponse<UserAiMessage> addUserAiMessage(@RequestBody UserAiMessageAddRequest userAiMessageAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userAiMessageAddRequest == null, ErrorCode.PARAMS_ERROR);
        String userInputText = userAiMessageAddRequest.getUserInputText();
        if (WordUtils.containsForbiddenWords(userInputText)) {
            ThrowUtils.throwIf(WordUtils.containsForbiddenWords(userInputText), ErrorCode.WORD_FORBIDDEN_ERROR, "包含违禁词");
        }
        UserAiMessage userAiMessage = new UserAiMessage();
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        userAiMessage.setUserId(loginUser.getId());
        userAiMessage.setUserInputText(userInputText);
        String response = tourismAiClient.chat(buildUserPrompt(userInputText));
        userAiMessageAddRequest.setAiGenerateText(response);

        // 复制属性
        BeanUtils.copyProperties(userAiMessageAddRequest, userAiMessage);

        // 校验数据
        userAiMessageService.validUserAiMessage(userAiMessage, true);


        // 插入数据库
        boolean result = userAiMessageService.save(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newUserAiMessageId = userAiMessage.getId();
        UserAiMessage generateAnswer = userAiMessageService.getById(newUserAiMessageId);
        return ResultUtils.success(generateAnswer);
    }

    private String buildUserPrompt(String userInputText) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("你是一个旅游景点推荐官，请根据用户喜好，从下列景点中选择最多三个景点，");
        promptBuilder.append("先给出简短推荐理由，再给出简洁路线建议，使用中文回答。\n");
        promptBuilder.append("当前景点库：");
        List<String> spotNameList = spotService.list().stream()
                .map(Spot::getSpotName)
                .filter(StringUtils::isNotBlank)
                .limit(80)
                .collect(Collectors.toList());
        promptBuilder.append(String.join("、", spotNameList));
        promptBuilder.append("\n用户喜好信息：");
        promptBuilder.append(StringUtils.defaultString(userInputText));
        return promptBuilder.toString();
    }


    /**
     * 删除用户对话表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserAiMessage(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserAiMessage oldUserAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(oldUserAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserAiMessage.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userAiMessageService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户对话表（仅管理员可用）
     *
     * @param userAiMessageUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserAiMessage(@RequestBody UserAiMessageUpdateRequest userAiMessageUpdateRequest) {
        if (userAiMessageUpdateRequest == null || userAiMessageUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserAiMessage userAiMessage = new UserAiMessage();
        BeanUtils.copyProperties(userAiMessageUpdateRequest, userAiMessage);
        // 数据校验
        userAiMessageService.validUserAiMessage(userAiMessage, false);
        // 判断是否存在
        long id = userAiMessageUpdateRequest.getId();
        UserAiMessage oldUserAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(oldUserAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = userAiMessageService.updateById(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户对话表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserAiMessageVO> getUserAiMessageVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        UserAiMessage userAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(userAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(userAiMessageService.getUserAiMessageVO(userAiMessage, request));
    }

    /**
     * 分页获取用户对话表列表（仅管理员可用）
     *
     * @param userAiMessageQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserAiMessage>> listUserAiMessageByPage(@RequestBody UserAiMessageQueryRequest userAiMessageQueryRequest) {
        long current = userAiMessageQueryRequest.getCurrent();
        long size = userAiMessageQueryRequest.getPageSize();
        // 查询数据库
        Page<UserAiMessage> userAiMessagePage = userAiMessageService.page(new Page<>(current, size),
                userAiMessageService.getQueryWrapper(userAiMessageQueryRequest));
        return ResultUtils.success(userAiMessagePage);
    }

    /**
     * 分页获取用户对话表列表（封装类）
     *
     * @param userAiMessageQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserAiMessageVO>> listUserAiMessageVOByPage(@RequestBody UserAiMessageQueryRequest userAiMessageQueryRequest,
                                                                         HttpServletRequest request) {
        long current = userAiMessageQueryRequest.getCurrent();
        long size = userAiMessageQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserAiMessage> userAiMessagePage = userAiMessageService.page(new Page<>(current, size),
                userAiMessageService.getQueryWrapper(userAiMessageQueryRequest));
        // 获取封装类
        return ResultUtils.success(userAiMessageService.getUserAiMessageVOPage(userAiMessagePage, request));
    }

    /**
     * 分页获取当前登录用户创建的用户对话表列表
     *
     * @param userAiMessageQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<UserAiMessageVO>> listMyUserAiMessageVOByPage(@RequestBody UserAiMessageQueryRequest userAiMessageQueryRequest,
                                                                           HttpServletRequest request) {
        ThrowUtils.throwIf(userAiMessageQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        userAiMessageQueryRequest.setUserId(loginUser.getId());
        long current = userAiMessageQueryRequest.getCurrent();
        long size = userAiMessageQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<UserAiMessage> userAiMessagePage = userAiMessageService.page(new Page<>(current, size),
                userAiMessageService.getQueryWrapper(userAiMessageQueryRequest));
        // 获取封装类
        return ResultUtils.success(userAiMessageService.getUserAiMessageVOPage(userAiMessagePage, request));
    }

    /**
     * 编辑用户对话表（给用户使用）
     *
     * @param userAiMessageEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editUserAiMessage(@RequestBody UserAiMessageEditRequest userAiMessageEditRequest, HttpServletRequest request) {
        if (userAiMessageEditRequest == null || userAiMessageEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        UserAiMessage userAiMessage = new UserAiMessage();
        BeanUtils.copyProperties(userAiMessageEditRequest, userAiMessage);
        // 数据校验
        userAiMessageService.validUserAiMessage(userAiMessage, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = userAiMessageEditRequest.getId();
        UserAiMessage oldUserAiMessage = userAiMessageService.getById(id);
        ThrowUtils.throwIf(oldUserAiMessage == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldUserAiMessage.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = userAiMessageService.updateById(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


}
