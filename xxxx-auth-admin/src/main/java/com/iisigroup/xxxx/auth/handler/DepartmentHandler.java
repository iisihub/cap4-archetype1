/* 
 * BranchHandler.java
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.iisigroup.cap.annotation.HandlerType;
import com.iisigroup.cap.annotation.HandlerType.HandlerTypeEnum;
import com.iisigroup.cap.component.Result;
import com.iisigroup.cap.component.Request;
import com.iisigroup.cap.component.impl.AjaxFormResult;
import com.iisigroup.cap.component.impl.BeanGridResult;
import com.iisigroup.cap.db.dao.SearchSetting;
import com.iisigroup.cap.db.model.Page;
import com.iisigroup.cap.db.service.CommonService;
import com.iisigroup.cap.exception.CapException;
import com.iisigroup.cap.formatter.Formatter;
import com.iisigroup.cap.mvc.handler.MFormHandler;
import com.iisigroup.cap.security.CapSecurityContext;
import com.iisigroup.cap.utils.CapBeanUtil;
import com.iisigroup.cap.utils.CapDate;
import com.iisigroup.cap.utils.CapString;
import com.iisigroup.xxxx.auth.model.Department;
import com.iisigroup.xxxx.auth.service.DepartmentService;

/**
 * <pre>
 * 分行維護
 * </pre>
 * 
 * @since 2014/1/13
 * @author tammy
 * @version
 *          <ul>
 *          <li>2014/1/13,tammy,new
 *          </ul>
 */
@Controller("departmenthandler")
public class DepartmentHandler extends MFormHandler {

    @Resource
    private CommonService commonSrv;

    @Resource
    private DepartmentService departmentService;

    @HandlerType(HandlerTypeEnum.GRID)
    public BeanGridResult query(SearchSetting search, Request params) {
        search.addOrderBy("code");

        Map<String, Formatter> fmt = new HashMap<String, Formatter>();

        Page<Department> page = commonSrv.findPage(Department.class, search);
        return new BeanGridResult(page.getContent(), page.getTotalRow(), fmt);
    }

    /**
     * 編輯資料
     * 
     * @param request
     *            IRequest
     * @return {@link Result.com.iisi.cap.response.IResult}
     * @throws CapException
     */
    public Result save(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String oid = request.get("oid");
        String brNo = request.get("code");
        Department branch = null;

        if (CapString.isEmpty(oid)) {
            branch = departmentService.findByBrno(brNo);
            if (branch != null) {
                result.set("exist", Boolean.TRUE);
                return result;
            }
        } else {
            branch = commonSrv.findById(Department.class, oid);
        }

        if (branch == null) {
            branch = new Department();
            branch.setOid(null);
        }
        CapBeanUtil.map2Bean(request, branch, Department.class);
        branch.setUpdater(CapSecurityContext.getUserId());
        branch.setUpdateTime(CapDate.getCurrentTimestamp());
        departmentService.save(branch);

        return result;
    }

    /**
     * 刪除資料
     * 
     * @param request
     *            IRequest
     * @return {@link Result.com.iisi.cap.response.IResult}
     * @throws CapException
     */
    public Result delete(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        Department code = commonSrv.findById(Department.class, request.get("oid"));
        if (code != null) {
            commonSrv.delete(code);
        }
        return result;
    }

}
