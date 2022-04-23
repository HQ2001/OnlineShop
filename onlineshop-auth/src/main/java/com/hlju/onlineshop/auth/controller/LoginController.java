package com.hlju.onlineshop.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.hlju.common.constant.AuthConstant;
import com.hlju.common.exception.BizCodeEnum;
import com.hlju.common.utils.R;
import com.hlju.common.valid.ValidateUtils;
import com.hlju.onlineshop.auth.dto.UserLoginDTO;
import com.hlju.onlineshop.auth.dto.UserRegisterDTO;
import com.hlju.common.vo.UserResponseVO;
import com.hlju.onlineshop.auth.feign.ThirdPartyFeignService;
import com.hlju.onlineshop.auth.feign.UserFeignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author haoqiang
 * @email 3277427547@qq.com
 * @date 2022/4/19 11:06
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UserFeignService userFeignService;

    @ResponseBody
    @GetMapping("/register/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        if (!ValidateUtils.isMobile(phone)) {
            return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), "电话号码格式错误");
        }
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotEmpty(redisCode)) {
            long timeMillions = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - timeMillions < 60 * 1000) {
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(),
                        BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        String codeRange = "0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int idx = RandomUtils.nextInt(codeRange.length());
            code.append(codeRange.charAt(idx));
        }

        // 放到缓存当中 value中带有时间，防止频繁刷新
        redisTemplate.opsForValue().set(
                AuthConstant.SMS_CODE_CACHE_PREFIX + phone,
                code + "_" + System.currentTimeMillis(),
                10, TimeUnit.MINUTES
        );

        try {
            thirdPartyFeignService.sendCode(phone, code.toString());
        } catch (Exception e) {
            log.debug("远程调用失败 ", e);
        }
        return R.ok();
    }

    /**
     * 注册
     * @return 注册成功返回首页
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterDTO userRegisterDTO, BindingResult result,
                           RedirectAttributes redirectAttributes) {
        String registerPage = "redirect:http://auth.onlineshop.hlju.com/register.html";
        if (result.hasErrors()) {
            Map<String, String> errorMap = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errorMap", errorMap);
            return registerPage;
        }
        String code = userRegisterDTO.getCode();
        String phone = userRegisterDTO.getPhone();
        String key = AuthConstant.SMS_CODE_CACHE_PREFIX + phone;
        String redisCode = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(redisCode) && code.equals(redisCode.split("_")[0])) {
            // 验证通过，进行删除。令牌机制
            redisTemplate.delete(key);
            R r = userFeignService.register(userRegisterDTO);
            if (r.getCode() == 0) {
                // 成功注册
                return "redirect:http://auth.onlineshop.hlju.com/login.html";
            } else {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("msg", r.getData("msg", new TypeReference<String>(){}));
                redirectAttributes.addFlashAttribute("errorMap", errorMap);
                return registerPage;
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errorMap", errorMap);
            return registerPage;
        }
    }

    @PostMapping("/login")
    public String login(UserLoginDTO loginDTO, RedirectAttributes redirectAttributes, HttpSession session) {
        R r = userFeignService.login(loginDTO);
        if (r.getCode() == 0) {
            // 放到session中
            // 放大域名，不然父域名或父域名下其他域名都访问不到
            session.setAttribute(AuthConstant.LOGIN_USER, r.getData(AuthConstant.LOGIN_USER, new TypeReference<UserResponseVO>(){}));
            return "redirect:http://onlineshop.hlju.com";
        }
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("msg", r.getData("msg", new TypeReference<String>(){}));
        redirectAttributes.addFlashAttribute("errorMap", errorMap);
        return "redirect:http://auth.onlineshop.hlju.com/login.html";
    }

    @GetMapping({"/", "/login.html"})
    public String loginPage(HttpSession session) {
        Object loginUser = session.getAttribute(AuthConstant.LOGIN_USER);
        if (Objects.isNull(loginUser)) {
            return "login";
        } else {
            return "redirect:http://onlineshop.hlju.com";
        }
    }

}
