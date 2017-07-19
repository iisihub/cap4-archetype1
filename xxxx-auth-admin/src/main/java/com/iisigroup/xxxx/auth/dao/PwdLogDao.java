package com.iisigroup.xxxx.auth.dao;

import java.util.List;

import com.iisigroup.cap.db.dao.GenericDao;
import com.iisigroup.xxxx.auth.model.PwdLog;

public interface PwdLogDao extends GenericDao<PwdLog> {
    List<PwdLog> findByUserCode(String userCode, int maxHistory);
}
