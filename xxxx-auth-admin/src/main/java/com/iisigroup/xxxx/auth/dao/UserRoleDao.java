package com.iisigroup.xxxx.auth.dao;

import java.util.List;

import com.iisigroup.cap.db.dao.GenericDao;
import com.iisigroup.xxxx.auth.model.UserRole;

public interface UserRoleDao extends GenericDao<UserRole> {

    int deleteByRoleCodeAndUserCodes(String roleCode, List<String> delUsr);

    UserRole findByUserCodeAndRoleCode(String userCode, String roleCode);

    void deleteByUserCode(String userCode);
}
