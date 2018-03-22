package code.artist.cms.core.controller.systam;

import code.artist.cms.core.constants.Constants.WebSession;
import code.artist.common.constants.Constants.HTTP_CODE;
import code.artist.common.result.RestResponse;
import code.artist.core.facade.system.IRoleService;
import code.artist.core.model.system.Role;
import code.artist.core.model.system.User;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 后台管理系统权限角色接口
 *
 * @author 艾江南
 */
@RestController
@RequestMapping("role")
public class RoleController {

    final private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private IRoleService roleService;

    /**
     * 管理员分配角色
     *
     * @param userId  管理员ID
     * @param roleIds 角色ID
     * @return 返回结果
     */
    @RequestMapping(value = "allot", method = RequestMethod.POST)
    public RestResponse allotRole(String userId, String roleIds) {
        logger.info("userId: {}", userId);
        logger.info("roleIds: {}", roleIds);
        List roleIdList = JSON.parseObject(roleIds, List.class);
        logger.info("roleIdList: {}", JSON.toJSONString(roleIdList));
        int flag = roleService.insertUserRole(userId, roleIdList);
        if (flag > 0) {
            return new RestResponse(HTTP_CODE.SUCCESS.getMessage());
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 查询所有角色
     *
     * @param page  页码
     * @param limit 每页大小
     * @return 返回结果
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public RestResponse showRole(int page, int limit) {
        String rolePage = roleService.selectEntityPage(page, limit);
        if (!StringUtils.isEmpty(rolePage)) {
            return new RestResponse(rolePage);
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 查询所有角色（不分页）
     *
     * @return 返回结果
     */
    @RequestMapping(method = RequestMethod.GET)
    public RestResponse showRole() {
        List<Role> roleList = roleService.selectEntityList();
        if (!CollectionUtils.isEmpty(roleList)) {
            return new RestResponse(roleList);
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 新增角色
     *
     * @param role 新增角色信息
     * @return 返回结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public RestResponse addRole(Role role, HttpSession session) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        logger.info("addRole: {}", JSON.toJSONString(role));
        role.setCreateUser(loginUser.getRealname());
        role.setUpdateUser(loginUser.getRealname());
        int flag = roleService.insertEntity(role);
        if (flag == 1) {
            return new RestResponse(HTTP_CODE.SUCCESS);
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 修改角色信息
     *
     * @param role 修改角色信息
     * @return 返回结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public RestResponse editRole(Role role, HttpSession session) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        logger.info("editRole: {}", JSON.toJSONString(role));
        role.setUpdateUser(loginUser.getRealname());
        int flag = roleService.updateEntityById(role);
        if (flag == 1) {
            return new RestResponse(HTTP_CODE.SUCCESS);
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 删除角色
     *
     * @return 返回结果
     */
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public RestResponse deleteRole(HttpSession session, @PathVariable("id") Integer id) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        Role role = new Role();
        role.setId(id);
        role.setStatus(0);
        role.setUpdateUser(loginUser.getRealname());
        int flag = roleService.updateEntityById(role);
        if (flag == 1) {
            return new RestResponse(HTTP_CODE.SUCCESS.getMessage());
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 通过管理员ID查询角色ID列表
     *
     * @param id 管理员ID
     * @return 返回结果
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public RestResponse chooseRole(@PathVariable("id") String id) {
        List<Integer> roleIdList = roleService.selectRoleIdsByUserId(id);
        logger.info("roleIdList: {}", JSON.toJSONString(roleIdList));
        return new RestResponse(roleIdList);
    }

}
