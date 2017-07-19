package com.iisigroup.xxxx.auth.dao;

import java.util.List;

import com.iisigroup.cap.db.dao.GenericDao;
import com.iisigroup.xxxx.auth.model.Department;

/**
 * <pre>
 * 分行資訊DAO
 * </pre>
 * 
 * @since 2011/8/30
 * @author Fantasy
 * @version
 *          <ul>
 *          <li>2011/8/30,Fantasy,new
 *          </ul>
 */
public interface DepartmentDao extends GenericDao<Department> {

    /**
     * 取得所有單位資訊(含已停業)
     * 
     * @return List<Department>
     */
    List<Department> findByAllDepartment();

    /**
     * 取得所有單位資訊(不含已停業)
     * 
     * @return List<Department>
     */
    List<Department> findByAllActDepartment();

    /**
     * 查詢單位
     * 
     * @param code
     *            單位代碼
     * @return Department
     */
    Department findByCode(String code);

}
