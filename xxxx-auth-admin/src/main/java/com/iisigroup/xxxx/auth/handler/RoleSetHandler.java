/* 
 * RoleSetHandler.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.iisigroup.cap.annotation.HandlerType;
import com.iisigroup.cap.annotation.HandlerType.HandlerTypeEnum;
import com.iisigroup.cap.component.Request;
import com.iisigroup.cap.component.Result;
import com.iisigroup.cap.component.impl.AjaxFormResult;
import com.iisigroup.cap.component.impl.BeanGridResult;
import com.iisigroup.cap.component.impl.MapGridResult;
import com.iisigroup.cap.db.dao.SearchSetting;
import com.iisigroup.cap.db.model.Page;
import com.iisigroup.cap.db.service.CommonService;
import com.iisigroup.cap.db.utils.CapEntityUtil;
import com.iisigroup.cap.exception.CapException;
import com.iisigroup.cap.exception.CapFormatException;
import com.iisigroup.cap.exception.CapMessageException;
import com.iisigroup.cap.formatter.BeanFormatter;
import com.iisigroup.cap.formatter.Formatter;
import com.iisigroup.cap.mvc.handler.MFormHandler;
import com.iisigroup.cap.security.CapSecurityContext;
import com.iisigroup.cap.utils.CapAppContext;
import com.iisigroup.cap.utils.CapBeanUtil;
import com.iisigroup.cap.utils.CapDate;
import com.iisigroup.cap.utils.CapString;
import com.iisigroup.cap.utils.GsonUtil;
import com.iisigroup.xxxx.auth.model.DefaultRole;
import com.iisigroup.xxxx.auth.model.RoleFunction;
import com.iisigroup.xxxx.auth.model.UserRole;
import com.iisigroup.xxxx.auth.service.RoleSetService;

/**
 * <pre>
 * 角色權限維護
 * </pre>
 * 
 * @since 2014/1/16
 * @author tammy
 * @version
 *          <ul>
 *          <li>2014/1/16,tammy,new
 *          </ul>
 */
@Controller("rolesethandler")
public class RoleSetHandler extends MFormHandler {

    @Resource
    private CommonService commonSrv;

    @Resource
    private RoleSetService roleSetService;

    @HandlerType(HandlerTypeEnum.GRID)
    public BeanGridResult query(SearchSetting search, Request params) {

        Map<String, Formatter> fmt = new HashMap<String, Formatter>();
        fmt.put("userCount", new BeanFormatter() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            public Integer reformat(Object in) throws CapFormatException {
                if (in instanceof DefaultRole) {
                    return ((DefaultRole) in).getUrList().size();
                }
                return 0;
            }
        });

        Page<DefaultRole> page = commonSrv.findPage(DefaultRole.class, search);
        return new BeanGridResult(page.getContent(), page.getTotalRow(), fmt);
    }

    @HandlerType(HandlerTypeEnum.GRID)
    public MapGridResult queryGridUser(SearchSetting search, Request params) {
        String code = params.get("code");
        if (CapString.isEmpty(code)) {
            return new MapGridResult();
        }

        Page<Map<String, Object>> page = roleSetService.findPageUser(search, code);
        return new MapGridResult(page.getContent(), page.getTotalRow(), null);
    }

    @HandlerType(HandlerTypeEnum.GRID)
    public MapGridResult queryGridFunc(SearchSetting search, Request params) {
        String code = params.get("code");
        if (CapString.isEmpty(code)) {
            return new MapGridResult();
        }

        Page<Map<String, Object>> page = roleSetService.findPageFunc(search, code);
        return new MapGridResult(page.getContent(), page.getTotalRow(), null);
    }

    @HandlerType(HandlerTypeEnum.GRID)
    public MapGridResult queryEditUsr(SearchSetting search, Request params) throws CapException {
        String depCode = params.get("depCode");
        String roleCode = params.get("roleCode");

        Page<Map<String, Object>> page = roleSetService.findPageEditUsr(search, roleCode, depCode);
        return new MapGridResult(page.getContent(), page.getTotalRow(), null);
    }

    @HandlerType(HandlerTypeEnum.GRID)
    public MapGridResult queryEditFunc(SearchSetting search, Request params) throws CapException {
        String parent = params.get("parent");
        String code = params.get("code");
        String sysType = params.get("sysType");

        Page<Map<String, Object>> page = roleSetService.findPageEditFunc(search, code, sysType, parent);
        return new MapGridResult(page.getContent(), page.getTotalRow(), null);
    }

    public Result queryForm(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        DefaultRole role = null;

        if (!CapString.isEmpty(code)) {
            role = roleSetService.findRoleByCode(code);
        }

        if (role != null) {
            result.putAll(new AjaxFormResult(role.toJSONObject(CapEntityUtil.getColumnName(role), null)));
        }

        return result;
    }

    public Result getAllDepartment(Request request) throws CapException {
        AjaxFormResult result = new AjaxFormResult();
        result.set("All", CapAppContext.getMessage("All"));
        result.putAll(roleSetService.findAllDepartment());

        return result;
    }

    public Result getAllFunc(Request request) throws CapException {
        AjaxFormResult result = new AjaxFormResult();
        String sysType = request.get("sysType");
        if (CapString.isEmpty(sysType)) {
            return result;
        }

        result.set("All", CapAppContext.getMessage("All"));
        result.putAll(roleSetService.findAllFunc(sysType));

        return result;
    }

    /**
     * 編輯資料
     * 
     * @param request
     *            IRequest
     * @return {@link tw.com.iisi.cap.response.Result}
     * @throws CapException
     */
    public Result save(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        String isNew = request.get("isNew");
        DefaultRole role = null;

        if (!CapString.isEmpty(code)) {
            role = roleSetService.findRoleByCode(code);
            if (isNew.equals("true") && role != null) {
                throw new CapMessageException(CapAppContext.getMessage("js.data.exists"), RoleSetHandler.class);
            }
        } else {
            throw new CapMessageException(CapAppContext.getMessage("EXCUE_ERROR"), RoleSetHandler.class);
        }
        if (role == null) {
            role = new DefaultRole();
        }
        CapBeanUtil.map2Bean(request, role, DefaultRole.class);
        role.setUpdater(CapSecurityContext.getUserId());
        role.setUpdateTime(CapDate.getCurrentTimestamp());

        commonSrv.save(role);

        return result;
    }

    /**
     * 編輯資料
     * 
     * @param request
     *            IRequest
     * @return {@link tw.com.iisi.cap.response.Result}
     * @throws CapException
     */
    public Result saveUrList(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        List<Object> users = GsonUtil.jsonToObjectList(request.get("users"));
        DefaultRole role = null;

        if (!CapString.isEmpty(code)) {
            role = roleSetService.findRoleByCode(code);
        }
        if (role == null) {
            throw new CapMessageException(CapAppContext.getMessage("EXCUE_ERROR"), RoleSetHandler.class);
        }

        List<UserRole> list = new ArrayList<UserRole>();
        if (users != null) {
            for (Object item : users) {
                Map<String, Object> user = GsonUtil.objToMap(item);
                UserRole userRole = new UserRole();
                userRole.setUserCode((String) user.get("code"));
                userRole.setRoleCode(role.getCode());
                userRole.setUpdater(CapSecurityContext.getUserId());
                userRole.setUpdateTime(CapDate.getCurrentTimestamp());
                list.add(userRole);
            }
        }
        commonSrv.save(list);

        return result;
    }

    /**
     * 編輯資料
     * 
     * @param request
     *            IRequest
     * @return {@link tw.com.iisi.cap.response.Result}
     * @throws CapException
     */
    public Result saveRfList(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        List<Object> funcItem = GsonUtil.jsonToObjectList(request.get("funcItem"));
        DefaultRole role = null;

        if (!CapString.isEmpty(code)) {
            role = roleSetService.findRoleByCode(code);
        }
        if (role == null) {
            throw new CapMessageException(CapAppContext.getMessage("EXCUE_ERROR"), RoleSetHandler.class);
        }

        List<RoleFunction> list = new ArrayList<RoleFunction>();
        if (funcItem != null) {
            for (Object item : funcItem) {
                Map<String, Object> func = GsonUtil.objToMap(item);
                RoleFunction roleFunc = new RoleFunction();
                roleFunc.setRoleCode(role.getCode());
                roleFunc.setFuncCode((String) func.get("code"));
                roleFunc.setUpdater(CapSecurityContext.getUserId());
                roleFunc.setUpdateTime(CapDate.getCurrentTimestamp());
                list.add(roleFunc);
            }
        }
        commonSrv.save(list);

        return result;
    }

    /**
     * 刪除資料
     * 
     * @param request
     *            IRequest
     * @return {@link tw.com.iisi.cap.response.Result}
     * @throws CapException
     */
    public Result delete(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        DefaultRole role = roleSetService.findRoleByCode(code);
        if (role != null) {
            commonSrv.delete(role);
        }
        return result;
    }

    /**
     * 刪除資料
     * 
     * @param request
     *            IRequest
     * @return {@link tw.com.iisi.cap.response.Result}
     * @throws CapException
     */
    public Result deleteUrList(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        List<Object> users = GsonUtil.jsonToObjectList(request.get("users"));

        if (CapString.isEmpty(code)) {
            throw new CapMessageException(CapAppContext.getMessage("EXCUE_ERROR"), RoleSetHandler.class);
        }

        List<String> delUsr = new ArrayList<String>();
        if (users != null) {
            for (Object item : users) {
                Map<String, Object> usr = GsonUtil.objToMap(item);
                delUsr.add((String) usr.get("code"));
            }
        }
        roleSetService.deleteUrList(code, delUsr);

        return result;
    }

    /**
     * 刪除資料
     * 
     * @param request
     *            IRequest
     * @return {@link tw.com.iisi.cap.response.Result}
     * @throws CapException
     */
    public Result deleteRfList(Request request) {
        AjaxFormResult result = new AjaxFormResult();
        String code = request.get("code");
        List<Object> funcItem = GsonUtil.jsonToObjectList(request.get("funcItem"));

        if (CapString.isEmpty(code)) {
            throw new CapMessageException(CapAppContext.getMessage("EXCUE_ERROR"), RoleSetHandler.class);
        }

        List<String> delFunc = new ArrayList<String>();
        if (funcItem != null) {
            for (Object item : funcItem) {
                Map<String, Object> usr = GsonUtil.objToMap(item);
                delFunc.add((String) usr.get("code"));
            }
        }
        roleSetService.deleteRfList(code, delFunc);

        return result;
    }

}
