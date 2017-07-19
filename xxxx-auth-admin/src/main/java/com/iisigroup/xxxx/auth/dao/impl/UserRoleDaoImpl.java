package com.iisigroup.xxxx.auth.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.iisigroup.cap.db.constants.SearchMode;
import com.iisigroup.cap.db.dao.SearchSetting;
import com.iisigroup.cap.db.dao.impl.GenericDaoImpl;
import com.iisigroup.xxxx.auth.dao.UserRoleDao;
import com.iisigroup.xxxx.auth.model.UserRole;

@Repository
public class UserRoleDaoImpl extends GenericDaoImpl<UserRole> implements UserRoleDao {

    @Override
    public int deleteByRoleCodeAndUserCodes(String roleCode, List<String> delUsr) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleCode", roleCode);
        param.put("delUsrs", delUsr);
        return getNamedJdbcTemplate().update("userRole_deleteUserRole", param);
    }

    @Override
    public UserRole findByUserCodeAndRoleCode(String userCode, String roleCode) {
        SearchSetting search = createSearchTemplete();
        search.addSearchModeParameters(SearchMode.EQUALS, "userCode", userCode);
        search.addSearchModeParameters(SearchMode.EQUALS, "roleCode", roleCode);
        return findUniqueOrNone(search);
    }

    @Override
    public void deleteByUserCode(String userCode) {
        SearchSetting search = createSearchTemplete();
        search.addSearchModeParameters(SearchMode.EQUALS, "userCode", userCode);
        delete(find(search));
    }
}
