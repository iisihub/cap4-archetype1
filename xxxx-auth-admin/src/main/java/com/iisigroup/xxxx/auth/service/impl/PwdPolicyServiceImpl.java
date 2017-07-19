package com.iisigroup.xxxx.auth.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iisigroup.cap.base.model.SysParm;
import com.iisigroup.cap.component.Request;
import com.iisigroup.cap.db.dao.CommonDao;
import com.iisigroup.cap.security.CapSecurityContext;
import com.iisigroup.cap.security.constants.SecConstants.PwdPolicyKeys;
import com.iisigroup.cap.utils.CapAppContext;
import com.iisigroup.cap.utils.CapDate;
import com.iisigroup.xxxx.auth.dao.UserDao;
import com.iisigroup.xxxx.auth.service.PwdPolicyService;

@Service
public class PwdPolicyServiceImpl implements PwdPolicyService {

    @Resource
    CommonDao commonDao;
    @Resource
    UserDao userDao;

    @Override
    public void updatePwdPolicy(Request request) {
        Map<String, Integer> policy = new HashMap<String, Integer>();
        for (PwdPolicyKeys value : PwdPolicyKeys.values()) {
            String key = value.toString().toLowerCase();
            String data = request.get(key.substring(4));
            policy.put(key, Integer.parseInt(data));
            SysParm parm = commonDao.findById(SysParm.class, key);
            if (parm == null) {
                parm = new SysParm();
            }
            parm.setParmId(key);
            parm.setParmValue(data);
            parm.setParmDesc(CapAppContext.getMessage("pwdpolicy." + key.substring(4)));
            parm.setUpdater(CapSecurityContext.getUserId());
            parm.setUpdateTime(CapDate.getCurrentTimestamp());
            commonDao.save(parm);
        }
        // 針對 pwd_expired_day, pwd_account_disable, pwd_account_delete 的處理
        userDao.processUserStatus(policy.get(PwdPolicyKeys.PWD_EXPIRED_DAY.toString().toLowerCase()), policy.get(PwdPolicyKeys.PWD_ACCOUNT_DISABLE.toString().toLowerCase()),
                policy.get(PwdPolicyKeys.PWD_ACCOUNT_DELETE.toString().toLowerCase()));
    }

}
