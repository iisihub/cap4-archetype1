/* 
 * MenuHandler.java
 * 
 * Copyright (c) 2009-2012 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.xxxx.auth.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.iisigroup.cap.component.Request;
import com.iisigroup.cap.component.Result;
import com.iisigroup.cap.component.impl.AjaxFormResult;
import com.iisigroup.cap.mvc.handler.MFormHandler;
import com.iisigroup.cap.security.CapSecurityContext;
import com.iisigroup.xxxx.auth.service.MenuService;
import com.iisigroup.xxxx.auth.service.impl.MenuServiceImpl.MenuItem;

/**
 * <pre>
 * Sample Handler
 * </pre>
 * 
 * @since 2012/9/24
 * @author iristu
 * @version
 *          <ul>
 *          <li>2012/9/24,iristu,new
 *          <li>2013/12/26,tammy,前端組menu修改
 *          </ul>
 */
@Controller("menuhandler")
public class MenuHandler extends MFormHandler {

    @Resource
    private MenuService menuSrv;

    public Result queryMenu(Request request) {

        MenuItem menu = menuSrv.getMenuByRoles(CapSecurityContext.getRoleIds());
        if (menu != null) {
            return new AjaxFormResult(menu);
        }
        return new AjaxFormResult();
    }

}
