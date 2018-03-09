package code.artist.cms.core.controller.systam;

import code.artist.common.constants.Constants;
import code.artist.common.result.RestResponse;
import code.artist.core.facade.system.IRoleService;
import code.artist.core.model.system.Role;
import code.artist.core.model.system.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
     * @param paramJson 传入参数
     * @return 返回结果
     */
    @RequestMapping(value = "allot", method = RequestMethod.POST)
    public RestResponse allotRole(String paramJson) {
        logger.info("allotRole: {}", paramJson);
        List<Integer> roleIdList = new ArrayList();
        JSONObject jsonObject = JSON.parseObject(paramJson);
        String userId = jsonObject.getString("userId");
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("roleIds[]");
            for (int i = 0, n = jsonArray.size(); i < n; i++) {
                roleIdList.add(Integer.valueOf((String) jsonArray.get(i)));
            }
        } catch (NullPointerException ignored) {

        } catch (ClassCastException e) {
            roleIdList.add(jsonObject.getInteger("roleIds[]"));
        }
        logger.info("Array: {}", JSON.toJSONString(roleIdList));
        int flag = roleService.insertUserRole(userId, roleIdList);
        if (flag > 0) {
            return new RestResponse("分配角色成功！");
        } else {
            return new RestResponse(Constants.HTTP_CODE.ERROR);
        }
    }

    /**
     * 查询所有角色
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 返回结果
     */
    @RequestMapping(value = "{pageNum}/{pageSize}", method = RequestMethod.GET)
    public RestResponse showRole(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize) {
        String rolePage = roleService.selectEntityPage(pageNum, pageSize);
        if (!StringUtils.isEmpty(rolePage)) {
            return new RestResponse(rolePage);
        } else {
            return new RestResponse(Constants.HTTP_CODE.ERROR);
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
            return new RestResponse(Constants.HTTP_CODE.ERROR);
        }
    }

    /**
     * 新增角色
     *
     * @param userJson  当前登录管理员
     * @param paramJson 新增角色信息
     * @return 返回结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public RestResponse addRole(String userJson, String paramJson) {
        User loginUser = JSON.parseObject(userJson, User.class);
        Role role = JSON.parseObject(paramJson, Role.class);
        logger.info("role: {}", paramJson);
        role.setCreateUser(loginUser.getRealname());
        role.setUpdateUser(loginUser.getRealname());
        int flag = roleService.insertEntity(role);
        if (flag == 1) {
            return new RestResponse(Constants.HTTP_CODE.SUCCESS);
        } else {
            return new RestResponse(Constants.HTTP_CODE.ERROR);
        }
    }

    /**
     * 修改角色信息
     *
     * @param userJson  当前登录管理员
     * @param paramJson 修改登录管理员
     * @return 返回结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public RestResponse editRole(String userJson, String paramJson) {
        User loginUser = JSON.parseObject(userJson, User.class);
        Role role = JSON.parseObject(paramJson, Role.class);
        logger.info("role: {}", paramJson);
        logger.info("roleCloss: {}", JSON.toJSONString(role));
        role.setUpdateUser(loginUser.getRealname());
        int flag = roleService.updateEntityById(role);
        if (flag == 1) {
            return new RestResponse(Constants.HTTP_CODE.SUCCESS);
        } else {
            return new RestResponse(Constants.HTTP_CODE.ERROR);
        }
    }

    /**
     * 删除角色
     *
     * @param userJson 当前登录管理员
     * @return 返回结果
     */
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public RestResponse deleteRole(String userJson, @PathVariable("id") Integer id) {
        User loginUser = JSON.parseObject(userJson, User.class);
        Role role = new Role();
        role.setId(id);
        role.setStatus(0);
        role.setUpdateUser(loginUser.getRealname());
        int flag = roleService.updateEntityById(role);
        if (flag == 1) {
            return new RestResponse("删除成功！");
        } else {
            return new RestResponse(Constants.HTTP_CODE.ERROR);
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
