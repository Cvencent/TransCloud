package com.vencent.transcloudgatewayzuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 过滤请求，用于权限校验
 * @author: vencent
 * @create 2020-03-23
 **/
public class PreRequestAuthFilter extends ZuulFilter {



    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = httpServletRequest.getRequestURI();

        //请求过滤
        if(httpServletRequest.getSession().getAttribute("userID")== null&&(!uri.equals("/user/user/login"))&&(!uri.equals("/user/user/register"))&&(!uri.equals("/translate/translate/upload"))){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }else{
            ctx.setSendZuulResponse(true);  // 对该请求进行路由
            ctx.setResponseStatusCode(200); // 返回200正确响应
            return null;
        }

    }
}
