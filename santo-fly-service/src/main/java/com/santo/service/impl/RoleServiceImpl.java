package com.santo.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.santo.base.BusinessException;
import com.santo.base.Constant;
import com.santo.entity.Menu;
import com.santo.entity.RoleToMenu;
import com.santo.entity.UserToRole;
import com.santo.model.RoleModel;
import com.santo.service.IMenuService;
import com.santo.service.IRoleService;
import com.santo.entity.Role;
import com.santo.mapper.RoleMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.santo.service.IRoleToMenuService;
import com.santo.service.IUserToRoleService;
import com.santo.util.ComUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huliangjun
 * @since 2018-05-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Autowired
    private IRoleToMenuService roleToMenuService;

    @Autowired
    private IUserToRoleService userToRoleService;

    @Autowired
    private IMenuService menuService;
    @Override
    public boolean addRoleAndPermission(RoleModel roleModel) throws Exception{
        Role role = new Role();
        BeanUtils.copyProperties(roleModel,role);
        boolean result = this.insert(role);
        if (! result) {
            throw  new BusinessException("更新角色信息失败");
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        return result;
    }

    @Override
    public boolean updateRoleInfo(RoleModel roleModel) throws Exception{
        Role role = this.selectById(roleModel.getRoleCode());
        if (ComUtil.isEmpty(role)) {
            return false;
        }
        BeanUtils.copyProperties(roleModel,role);
        boolean result = this.updateById(role);
        if (! result) {
            throw  new BusinessException("更新角色信息失败");
        }
        result = roleToMenuService.delete(new EntityWrapper<RoleToMenu>().eq("role_code",roleModel.getRoleCode()));
        if (! result) {
            throw  new BusinessException("删除权限信息失败");
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        if (! result) {
            throw  new BusinessException("更新权限信息失败");
        }
        return result;

    }

    @Override
    public void getRoleIsAdminByUserNo(String userNo) throws Exception {
        UserToRole userToRole = userToRoleService.selectByUserNo(userNo);
        Role role = this.selectById(userToRole.getRoleCode());
        if(role.getRoleName().equals(Constant.RoleType.SYS_ASMIN_ROLE)){
            throw new BusinessException("不能修改管理员信息!");
        }
    }


    @Override
    public Map<String, Object> getMenuByRoleCode(String roleCode) {
        Map<String,Object> retMap   =new HashMap<>();
        List<Menu> menuList = menuService.findMenuByRoleCode(roleCode);
        List<Menu> buttonList = new ArrayList<Menu>();
        List<Menu> retMenuList = menuService.treeMenuList(Constant.ROOT_MENU, menuList);
        for (Menu buttonMenu : menuList) {
            if(buttonMenu.getMenuType() == Constant.TYPE_BUTTON){
                buttonList.add(buttonMenu);
            }
        }
        retMap.put("menuList",retMenuList);
        retMap.put("buttonList",buttonList);
        return retMap;
    }
}
