package com.trus.regpro.Controller;

import cn.hutool.core.lang.PatternPool;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trus.regpro.Common.R;
import com.trus.regpro.Entity.User;
import com.trus.regpro.Service.UserService;
import com.trus.regpro.Utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 发送验证码
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
    R<String> sendMessage(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        // 校验手机号
        Pattern mobile = PatternPool.MOBILE;
        Matcher matcher = mobile.matcher(phone);
        if(!matcher.matches()){
            return R.error("手机号格式有误");
        }


        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
//            SMSUtils.sendMessage("外卖","",code,phone);
            session.setAttribute(phone,code);
            return R.success("短信发送成功");
        }
        return R.error("错误");
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    R<User> login(@RequestBody Map map, HttpSession session){

        String phone = map.get("phone").toString();
        String code = map.get("code").toString();


        String codeInSession = (String) session.getAttribute(phone);

        //修改
//        log.info("比对{},{}",codeInSession,code);
//        if(codeInSession !=null && !code.equals(codeInSession)){
        if(true){
//            log.info("输入的code{}",code);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);

            if(user == null){

                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }
        return R.error("登陆失败");
    }

    @RequestMapping(value = ("/loginout"),method = RequestMethod.POST)
    R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
