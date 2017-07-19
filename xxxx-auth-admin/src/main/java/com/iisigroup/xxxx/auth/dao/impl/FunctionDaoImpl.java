/*
 * Copyright (c) 2009-2012 International Integrated System, Inc.
 * 11F, No.133, Sec.4, Minsheng E. Rd., Taipei, 10574, Taiwan, R.O.C.
 * All Rights Reserved.
 *
 * Licensed Materials - Property of International Integrated System, Inc.
 *
 * This software is confidential and proprietary information of
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */

package com.iisigroup.xxxx.auth.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.iisigroup.cap.db.constants.SearchMode;
import com.iisigroup.cap.db.dao.SearchSetting;
import com.iisigroup.cap.db.dao.impl.GenericDaoImpl;
import com.iisigroup.cap.db.model.Page;
import com.iisigroup.cap.utils.CapString;
import com.iisigroup.xxxx.auth.dao.FunctionDao;
import com.iisigroup.xxxx.auth.model.DefaultFunction;
import com.iisigroup.xxxx.auth.support.FunctionRowMapper;

/**
 * <pre>
 * DAO
 * </pre>
 *
 * @since 2013/12/20
 * @author tammy
 * @version
 *          <ul>
 *          <li>2013/12/20,tammy,new
 *          </ul>
 */
@Repository
public class FunctionDaoImpl extends GenericDaoImpl<DefaultFunction> implements FunctionDao {

    private static final int NO_PARENT = -1;

    @Override
    public List<DefaultFunction> findAll(String system) {
        SearchSetting search = createSearchTemplete();
        search.addSearchModeParameters(SearchMode.EQUALS, "sysType", system);
        search.addSearchModeParameters(SearchMode.EQUALS, "status", "1"); // 啟用
        search.addOrderBy("level");
        search.addOrderBy("parent");
        search.addOrderBy("sequence");
        return find(search);
    }

    @Override
    public List<DefaultFunction> findBySysTypeAndLevel(String sysType, String level) {
        SearchSetting search = createSearchTemplete();
        search.addSearchModeParameters(SearchMode.EQUALS, "sysType", sysType);
        search.addSearchModeParameters(SearchMode.EQUALS, "level", level);
        return find(search);
    }

    @Override
    public Page<Map<String, Object>> findPageByRoleCode(String roleCode, int firstResult, int maxResults) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleCode", roleCode);
        return getNamedJdbcTemplate().queryForPage("function_getFuncByRoldCode", param, firstResult, maxResults);
    }

    @Override
    public Page<Map<String, Object>> findPageUnselected(String roleCode, String sysType, String parent, int firstResult, int maxResults) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleCode", roleCode);
        param.put("parent", parent);
        param.put("sysType", sysType);
        return getNamedJdbcTemplate().queryForPage("function_getEditFuncByRole", param, firstResult, maxResults);
    }

    @Override
    public DefaultFunction findByCodeAndSysType(int code, String sysType) {
        return getFuncsBySysType(sysType).get(code);
    }

    @Override
    public List<DefaultFunction> findByParentAndLevels(String pgmDept, Set<String> roles, int parent, String sysType, int... levels) {

        Set<Integer> pSet = new HashSet<Integer>();
        pSet.add(parent);

        Set<DefaultFunction> set = new HashSet<DefaultFunction>();
        if (roles == null) {
            roles = Collections.emptySet();
        }
        Arrays.sort(levels);

        for (String role : roles) {
            for (int step : levels) {
                String key = getRoleStepKey(role, step);
                List<DefaultFunction> stepCodes = getRoleLevelFuncsBySysType(sysType).get(key);

                if (stepCodes == null) {
                    continue;
                } else if (parent == NO_PARENT) {
                    set.addAll(stepCodes);
                    for (DefaultFunction code : stepCodes) {
                        pSet.add(code.getCode());

                    }
                } else {
                    for (DefaultFunction code : stepCodes) {
                        if (pSet.contains(code.getParent())) {
                            set.add(code);
                            pSet.add(code.getCode());
                        }
                    }
                }
            }
        }
        return Arrays.asList(set.toArray(new DefaultFunction[set.size()]));
    }

    @Override
    public List<DefaultFunction> findByLevels(Set<String> roles, String sysType, int... levels) {
        return findByParentAndLevels(roles, NO_PARENT, sysType, levels);
    }

    @Override
    public List<DefaultFunction> findBySysTypeAndParent(Set<String> roles, int parent, String sysType) {
        int level = getFuncsBySysType(sysType).get(parent).getLevel() + 1;
        return findByParentAndLevels(roles, parent, sysType, new int[] { level });
    }

    @Override
    public List<DefaultFunction> findByParentAndLevels(Set<String> roles, int parent, String sysType, int... levels) {
        Set<Integer> pSet = new HashSet<Integer>();
        pSet.add(parent);
        Set<DefaultFunction> set = new HashSet<DefaultFunction>();
        if (roles == null) {
            roles = Collections.emptySet();
        }
        Arrays.sort(levels);
        for (String role : roles) {
            for (int step : levels) {
                String key = getRoleStepKey(role, step);
                List<DefaultFunction> stepCodes = getRoleLevelFuncsBySysType(sysType).get(key);

                if (stepCodes == null) {
                    continue;
                } else if (parent == NO_PARENT) {
                    set.addAll(stepCodes);
                    for (DefaultFunction code : stepCodes) {
                        pSet.add(code.getCode());
                    }
                } else {
                    for (DefaultFunction code : stepCodes) {
                        if (pSet.contains(code.getParent())) {
                            set.add(code);
                            pSet.add(code.getCode());
                        }
                    }
                }
            }
        }
        return Arrays.asList(set.toArray(new DefaultFunction[set.size()]));
    }

    private String getRoleStepKey(String role, int level) {
        return role + "_" + level;
    }

    private Map<Integer, DefaultFunction> getFuncsBySysType(String sysType) {
        Map<Integer, DefaultFunction> result = new HashMap<Integer, DefaultFunction>();
        List<DefaultFunction> list = findAll(sysType);
        for (DefaultFunction item : list) {
            result.put(item.getCode(), item);
        }
        return result;
    }

    private Map<String, List<DefaultFunction>> getRoleLevelFuncsBySysType(String sysType) {
        Map<String, List<DefaultFunction>> roleLevelCodes = new HashMap<String, List<DefaultFunction>>();
        final Map<String, Map<Integer, Integer>> roleAuthes = new ConcurrentHashMap<String, Map<Integer, Integer>>();
        Map<String, Set<Integer>> roleSteps = new ConcurrentHashMap<String, Set<Integer>>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("sysType", sysType);

        getNamedJdbcTemplate().query("function_auth", param, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String role = CapString.trimNull(rs.getString("ROLE"));
                Map<Integer, Integer> authes = roleAuthes.get(role);
                if (authes == null) {
                    authes = new HashMap<Integer, Integer>();
                    roleAuthes.put(role, authes);
                }
                authes.put(rs.getInt("AUTHCODE"), rs.getInt("AUTHCODE"));
            }
        });

        for (Entry<String, Map<Integer, Integer>> entry : roleAuthes.entrySet()) {
            String role = entry.getKey();
            Set<Integer> levels = roleSteps.get(role);
            if (levels == null) {
                levels = new HashSet<Integer>();
                roleSteps.get(levels);
            }
            for (Integer auth : entry.getValue().keySet()) {
                DefaultFunction code = getFuncsBySysType(sysType).get(auth);
                if (code == null)
                    continue;
                String key = getRoleStepKey(role, code.getLevel());
                List<DefaultFunction> stepCodes = roleLevelCodes.get(key);
                if (stepCodes == null) {
                    stepCodes = new LinkedList<DefaultFunction>();
                    roleLevelCodes.put(key, stepCodes);
                }
                stepCodes.add(code);
                levels.add(code.getLevel());
            }
            roleSteps.put(role, levels);
        }
        return roleLevelCodes;
    }

    @Override
    public List<DefaultFunction> findMenuDataByRoles(Set<String> roles, String systemType) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleCodes", roles);
        param.put("sysType", systemType);
        return getNamedJdbcTemplate().query("function_findMenu", "", param, new FunctionRowMapper());
    }

    @Override
    public DefaultFunction findByCode(int code) {
        SearchSetting search = createSearchTemplete();
        search.addSearchModeParameters(SearchMode.EQUALS, "code", code);
        return findUniqueOrNone(search);
    }

}
