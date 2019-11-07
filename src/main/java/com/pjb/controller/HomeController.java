package com.pjb.controller;

import com.alibaba.fastjson.JSONObject;
import com.pjb.entity.UserInfo;
import com.pjb.mapper.UserInfoMapper;
import com.pjb.service.UserInfoService;
import com.pjb.service.impl.UserInfoServiceImpl;
import com.pjb.util.UserRegisteAndLogin;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author lsj
 * @date 2019.7
 */
@Controller
public class HomeController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping({"/","/index"})
    public String index(){
        return"/index";
    }

    // 这里如果不写method参数的话，默认支持所有请求，如果想缩小请求范围，还是要添加method来支持get, post等等某个请求。
    @RequestMapping("/login")
    public String login(HttpServletRequest request, Map<String, Object> map) {
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.

        //抛出的异常是正确的，但是instanceof方法结果一直为false，不知道原因是否没有实例化
//        Object exception = request.getAttribute("shiroLoginFailure");
//        System.out.println("exception是："+exception);
//        System.out.println(exception instanceof UnknownAccountException);
//        String msg = "";
//        if (exception != null) {
//            if (exception instanceof UnknownAccountException) {
//                msg = "账户不存在";
//            } else if (exception instanceof IncorrectCredentialsException) {
//                msg = "账户不存在或密码不正确";
//            } else {
//                msg = "其他异常";
//            }
//        }
        String exception=(String)request.getAttribute("shiroLoginFailure");
        String msg = "";
        if(exception!=null){
            if(UnknownAccountException.class.getName().equals(exception)){
                msg = "账户不存在";
            }
            else if(IncorrectCredentialsException.class.getName().equals(exception)){
                msg = "用户名或密码不正确";
            }
            else{
                msg = "其他异常";
            }
        }

        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理.
        return "login";
    }

//    @RequestMapping("/login")
//    @ExceptionHandler(ShiroException.class)
//    public Object doHandleShiroException(ShiroException e){
//        String msg = "";
//        if (e != null) {
//            if (e instanceof IncorrectCredentialsException) {
//                System.out.println("密码不正确");
//                msg = "账户不存在或密码不正确";
//            } else if (e instanceof UnknownAccountException) {
//                System.out.println("此账户不存在");
//                msg = "账户不存在";
//            } else {
//                System.out.println(e);
//                msg = "其他异常";
//            }
//        }
//        return "login";
//
//    }

//    @PostMapping("/login")
//    public String userLogin(UserInfo userInfo,Model model)
//    {
//        UserInfo curUserInfo=null;
//
//        curUserInfo.setPassword(UserRegisteAndLogin.getInputPasswordCiph(userInfo.getPassword(), userInfoService.findSaltByUsername(userInfo.getUsername())));
//
//        return UserRegisteAndLogin.userLogin(curUserInfo,model);
//    }






    @RequestMapping("/403")
    public String unauthorizedRole(){
        return "403";
    }



    @GetMapping("/register")
    public String register(){
        return "register";
    }
    /**
     * 处理用户的注册请求
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String userRegister(String username,String password,String worknumber)
    {
        if(username.equals(null)||password.equals(null)||worknumber.equals(null)){
            return "error2";
        }
        UserInfo user = userInfoMapper.findByUsername(username);
        if(user!=null){
            return "error1";
        }
        System.out.println(username+password+worknumber);
        String[] saltAndCiphertext = UserRegisteAndLogin.encryptPassword(password,username);

        UserInfo curUserInfo=new UserInfo();

        curUserInfo.setState(1);
        curUserInfo.setUsername(username);
        curUserInfo.setWorknumber(worknumber);
        curUserInfo.setSalt(saltAndCiphertext[0]);
        curUserInfo.setPassword(saltAndCiphertext[1]);



        userInfoService.addUser(username,curUserInfo.getPassword(),
                worknumber,curUserInfo.getSalt(),curUserInfo.getState());

        //return "redirect:/login"; //使用户注册后立马登录
        return "success";
    }
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * @param username
     * 用于检测当前找回密码用户是否存在
     * 和缓存中的登录用户名比较，只允许更改当前用户密码
     * @return
     */
    AuthenticationToken token;
    @ResponseBody
    @RequestMapping(value = "/verifyUser", method = RequestMethod.GET)
    public String verifyUser(String username){
        UserInfo curuser = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        //String curusername = (String) SecurityUtils.getSubject().getPrincipal();
        //String curusername = (String)token.getPrincipal();
        String curusername=curuser.getUsername();
        System.out.println(curusername);
        if(!username.equals(curusername)){
            return "Have no qualification to change!";
        }

        System.out.println(username);
        return userInfoService.verifyUser(username);
    }

    @GetMapping("/retrievepassword")
    public String retrieve () {
        return ("/retrievepassword");
    }

    /**
     *
     * @param password
     * @param username
     * @param phonemsg
     * 处理找回密码请求
     * ERROR1指代验证用户名或密码为空；ERROR2表示手机验证码错误；
     * ERROR3指代该用户不存在，虽然前端有提示用户名不存在问题，
     * 但是怕当前用户执迷不悟继续提交
     * 写一个返回信息的封装类比较好
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/retrievepassword",method = RequestMethod.POST)
    public String retrievePassword(String password,String username,String phonemsg){
        if (username.equals("") || password.equals(""))
            return ("ERROR1");
        System.out.println(phonemsg);
        if(!phonemsg.equals("666666")) {
            return ("ERROR2");
        }
        UserInfo userInfo = userInfoMapper.findByUsername(username);
        if (userInfo == null) {
            return ("ERROR3");
        }
        return userInfoService.retrievePassword(password,username);
    }

    //验证用户的手机验证码是否正确，这里先模拟有固定的验证码666666
    @ResponseBody
    @RequestMapping(value="/checkmessage",method = RequestMethod.POST)
        public String checkMessage(String phonemsg){
        if(phonemsg=="666666"){
            return "RIGHT";
        }
        else{
            return "WRONG";
        }
    }

    /**
     * 用于验证修改密码账户密码是否正确
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/confirmuser",method=RequestMethod.POST)
        public String confirmUser(String username,String password){
        System.out.println("修改密码"+username+password);
        return userInfoService.findPasswordByUsername(username,password);
    }

    @GetMapping(value="/changepassword")
    public String changePassword(){
        return ("/changepassword");
    }

    /**
     * 先判断修改密码用户是否为当前登录用户
     * 再判断数据库是否存在该用户（这一步似乎可以省去了）及账号密码是否正确
     * 虽然在输入时已经判断了，但防止有人故意忽略提示，在此处又判断了一次
     *
     * @param username
     * @param oldpassword
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/changepassword",method = RequestMethod.POST)
    public String changePassword(String username,String oldpassword,String password){
        UserInfo curuser = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        String curusername=curuser.getUsername();
        if(!username.equals(curusername)){
            return "Have no qualification to change!";
        }
        UserInfo userInfo = userInfoMapper.findByUsername(username);
        if (userInfo == null) {
            return ("ERROR3");
        }

        String flag=userInfoService.findPasswordByUsername(username,oldpassword);
        if(flag.equals("Please check the password")){
            return ("error!!!");
        }
        return userInfoService.retrievePassword(password,username);
    }





    /**
     * 用户名重复性检查
     * 数据库中已有该用户名返回
     *
     * @param jsonObject
     * @return
     */
//    @RequestMapping(value = "/check", method = RequestMethod.POST,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public String checkData(@RequestBody JSONObject jsonObject) {
//        String username = jsonObject.getString("username");
//        System.out.println(username);
//        UserInfo user=null;
//                user = userInfoMapper.findByUsername(username);
//        // 这里代表如果通过用户名没有查询到用户信息，即代表未重名返回SUCCESS，否则返回ERROR代表以重名
//        if(user == null){
//            return "SUCCESS";
//        }else{
//            return "ERROR";
//        }
 //   }
}
