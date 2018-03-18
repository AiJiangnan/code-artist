package code.artist.cms.core.controller.systam;

import code.artist.cms.core.constants.Constants.WebSession;
import code.artist.common.constants.Constants.HTTP_CODE;
import code.artist.common.result.RestResponse;
import code.artist.core.facade.system.IUserService;
import code.artist.core.model.system.Menu;
import code.artist.core.model.system.User;
import code.artist.utils.common.DesUtil;
import code.artist.utils.common.IDUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
 * 后台管理系统权限管理员接口
 *
 * @author 艾江南
 */
@RestController
@RequestMapping("user")
public class UserController {

    final private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    /**
     * 管理员登陆
     *
     * @param user 登陆管理员管理员名和密码
     * @return 返回结果
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public RestResponse login(User user, HttpSession session) {
        logger.info("loginInfo: {}", JSON.toJSONString(user));
        Subject subject = SecurityUtils.getSubject();
        try {
            String password = DesUtil.encrypt(user.getPassword(), DesUtil.getKey());
            subject.login(new UsernamePasswordToken(user.getUsername(), password));
        } catch (AuthenticationException e) {
            logger.error("======登陆异常=======");
            return new RestResponse(HTTP_CODE.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponse(HTTP_CODE.ERROR);
        }
        logger.info("======登陆成功=======");
        logger.info("loginUser: {}", JSON.toJSONString(subject.getPrincipal()));
        session.setAttribute(WebSession.CURRENT_LOGIN_USER_SESSION, subject.getPrincipal());
        return new RestResponse(subject.getPrincipal());
    }

    /**
     * 管理员注销登录
     *
     * @return
     */
    @RequestMapping(value = "loginout", method = RequestMethod.GET)
    public RestResponse loginout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        logger.info("======注销成功=======");
        return new RestResponse(HTTP_CODE.SUCCESS);
    }

    /**
     * 显示菜单
     *
     * @param session 当前登陆管理员
     * @return 返回结果
     */
    @RequestMapping(value = "menu", method = RequestMethod.GET)
    public RestResponse showMenu(HttpSession session) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        List<Menu> menuList = userService.showMenu(loginUser.getId());
        if (!CollectionUtils.isEmpty(menuList)) {
            return new RestResponse(menuList);
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 查询所有管理员
     *
     * @param page  页码
     * @param limit 每页大小
     * @return 返回结果
     */
    @RequestMapping(method = RequestMethod.GET)
    public RestResponse showUserPage(int page, int limit) {
        String userPage = userService.selectEntityPage(page, limit);
        if (!StringUtils.isEmpty(userPage)) {
            return new RestResponse(userPage);
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 新增管理员
     *
     * @param user 注册管理员信息
     * @return 返回结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public RestResponse addUser(User user, HttpSession session) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        logger.info("addUser: {}", JSON.toJSONString(user));
        user.setId(IDUtil.getUUID());
        user.setCreateUser(loginUser.getRealname());
        user.setUpdateUser(loginUser.getRealname());
        try {
            user.setPassword(DesUtil.encrypt(user.getPassword(), DesUtil.getKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int flag = userService.insertEntity(user);
        if (flag == 1) {
            return new RestResponse(user.getRealname());
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 修改管理员信息
     *
     * @param user 修改参数
     * @return 返回结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public RestResponse editUser(User user, HttpSession session) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        logger.info("editUser: {}", JSON.toJSONString(user));
        user.setUpdateUser(loginUser.getRealname());
        if (!StringUtils.isEmpty(user.getPassword())) {
            try {
                user.setPassword(DesUtil.encrypt(user.getPassword(), DesUtil.getKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int flag = userService.updateEntityById(user);
        if (flag == 1) {
            return new RestResponse(user.getRealname());
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

    /**
     * 删除管理员
     *
     * @param id 删除管理员ID
     * @return 返回结果
     */
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public RestResponse deleteUser(HttpSession session, @PathVariable("id") String id) {
        User loginUser = (User) session.getAttribute(WebSession.CURRENT_LOGIN_USER_SESSION);
        User user = new User();
        user.setId(id);
        user.setStatus(0);
        user.setUpdateUser(loginUser.getRealname());
        int flag = userService.updateEntityById(user);
        if (flag == 1) {
            return new RestResponse(HTTP_CODE.SUCCESS.getMessage());
        } else {
            return new RestResponse(HTTP_CODE.ERROR);
        }
    }

}
