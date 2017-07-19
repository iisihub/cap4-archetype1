package com.iisigroup.xxxx.auth.dao;

import java.util.List;

import com.iisigroup.cap.db.dao.GenericDao;
import com.iisigroup.xxxx.auth.model.RoleFunction;

public interface RoleFunctionDao extends GenericDao<RoleFunction> {
    int deleteByRoleCodeAndFuncCodes(String roleCode, List<String> delFunc);

    int deleteByFuncCodeAndRoleCodes(String funcCode, List<String> delRole);
}
