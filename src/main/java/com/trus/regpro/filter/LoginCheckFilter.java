package com.trus.regpro.filter;


import com.alibaba.fastjson.JSON;
import com.trus.regpro.Common.BaseContext;
import com.trus.regpro.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;


        log.info("拦截到请求：{}",request.getRequestURI());

//        1、获取本次请求的URI
        String requestURI = request.getRequestURI();


        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
        };

//        2、判断本次请求是否需要处理

        boolean check = check(urls,requestURI);

//        3、如果不需要处理，则直接放行

        if(check){
            filterChain.doFilter(request,response);
            return;
        }

//        4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
//            System.err.println("************************");

            Long empId = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            log.info("当前线程的id:{}",Thread.currentThread().getId());
            log.info("拿到线程{}", BaseContext.getCurrentId());
            filterChain.doFilter(request,response);
            return;
        }

        //        4-2、判断{用户}登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user")!=null){
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            log.info("当前线程的id:{}",Thread.currentThread().getId());
            log.info("拿到线程{}", BaseContext.getCurrentId());
            filterChain.doFilter(request,response);
            return;
        }

//        5、如果未登录则返回未登录结果

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));



            return;



    }

    public static boolean check(String[] urls,String requestURI){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url,requestURI);
            if(match) return true;
        }
        return false;
    }



}
