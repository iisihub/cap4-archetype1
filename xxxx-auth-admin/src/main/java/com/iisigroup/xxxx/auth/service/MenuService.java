package com.iisigroup.xxxx.auth.service;

import java.util.Set;

import com.iisigroup.xxxx.auth.service.impl.MenuServiceImpl.MenuItem;

public interface MenuService {

    MenuItem getMenuByRoles(Set<String> roles);

}