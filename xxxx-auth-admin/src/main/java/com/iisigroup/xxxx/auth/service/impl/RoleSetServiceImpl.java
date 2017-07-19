package com.iisigroup.xxxx.auth.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.iisigroup.cap.db.dao.SearchSetting;
import com.iisigroup.cap.db.model.Page;
import com.iisigroup.xxxx.auth.dao.DepartmentDao;
import com.iisigroup.xxxx.auth.dao.FunctionDao;
import com.iisigroup.xxxx.auth.dao.RoleDao;
import com.iisigroup.xxxx.auth.dao.RoleFunctionDao;
import com.iisigroup.xxxx.auth.dao.UserDao;
import com.iisigroup.xxxx.auth.dao.UserRoleDao;
import com.iisigroup.xxxx.auth.model.DefaultFunction;
import com.iisigroup.xxxx.auth.model.DefaultRole;
import com.iisigroup.xxxx.auth.model.Department;
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
@Service("roleSetService")
public class RoleSetServiceImpl implements RoleSetService {

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private FunctionDao functionDao;

    @Resource
    private RoleFunctionDao roleFunctionDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private UserDao userDao;

    @Override
    public Page<Map<String, Object>> findPageUser(SearchSetting search, String roleCode) {
        return userDao.findPageByRoleCode(roleCode, search.getFirstResult(), search.getMaxResults());
    }

    @Override
    public Page<Map<String, Object>> findPageEditUsr(SearchSetting search, String roleCode, String depCode) {
        return userDao.findPageUnselectedByRoleCodeAndDepCode(roleCode, depCode, search.getFirstResult(), search.getMaxResults());
    }

    @Override
    public Page<Map<String, Object>> findPageEditFunc(SearchSetting search, String roleCode, String sysType, String parent) {
        return functionDao.findPageUnselected(roleCode, sysType, parent, search.getFirstResult(), search.getMaxResults());
    }

    @Override
    public Page<Map<String, Object>> findPageFunc(SearchSetting search, String code) {
        return functionDao.findPageByRoleCode(code, search.getFirstResult(), search.getMaxResults());
    }

    @Override
    public Map<String, String> findAllDepartment() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        List<Department> deps = departmentDao.findByAllDepartment();

        if (!CollectionUtils.isEmpty(deps)) {
            for (Department dep : deps) {
                map.put(dep.getCode(), dep.getName());
            }
        }
        return map;
    }

    @Override
    public Map<String, String> findAllFunc(String sysType) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        List<DefaultFunction> funcs = functionDao.findBySysTypeAndLevel(sysType, "1");

        if (!CollectionUtils.isEmpty(funcs)) {
            for (DefaultFunction func : funcs) {
                map.put(Integer.toString(func.getCode()), func.getName());
            }
        }
        return map;
    }

    @Override
    public int deleteUrList(String rolCode, List<String> delUsr) {
        return userRoleDao.deleteByRoleCodeAndUserCodes(rolCode, delUsr);
    }

    @Override
    public int deleteRfList(String rolCode, List<String> delFunc) {
        return roleFunctionDao.deleteByRoleCodeAndFuncCodes(rolCode, delFunc);
    }

    @Override
    public List<Map<String, Object>> findAllRoleWithSelectedByUserCode(String userCode) {
        return roleDao.findAllWithSelectedByUserCode(userCode);
    }

    @Override
    public DefaultRole findRoleByCode(String code) {
        return roleDao.findByCode(code);
    }

}
