/**
 * 注册时查找用户名是否存在
 */
function verifyUser() {
    $.ajax({
        type:"GET",
        url:"/verifyUser",
        data:{username:$("#username").val()},
        success: function(data){
            if (data=="SUCCESS"){
                alert("用户名可以注册");
            }
            else if(data=="ERROR"){
                alert("用户名已经存在，请更换！！");
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            // 状态码
            console.log(XMLHttpRequest.status);
            // 状态
            console.log(XMLHttpRequest.readyState);
            // 错误信息
            console.log(textStatus);
            alert("ajax配置有错，请检查url与type是否正确");
        }
    });
}

function register(){
    $.ajax({
        type:"POST",
        url:"/register",
        data:{username:$("#username").val(),
            password:$("#password").val(),
            worknumber:$("#worknumber").val()
        },
        success:function (data) {
            if(data=="success"){
                alert("注册成功，请登录");
                window.location.replace("/login");
            }
            else if(data=="error1"){
                alert("用户名已被注册");
                window.location.replace("/register");
            }
            else if(data=="error2"){
                alert("用户信息不全");
                window.location.replace("/register");
            }

        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            // 状态码
            console.log(XMLHttpRequest.status);
            // 状态
            console.log(XMLHttpRequest.readyState);
            // 错误信息
            console.log(textStatus);
            alert("注册失败")
        }
    })
}

/**
 * 判断两次输入的密码是否一致
 */
function checkpassword() {
    var password = $("#password").val();
    var confirm_password = $("#confirm_password").val();
    //console.log("看看调用没"+password);
    if (password != confirm_password) {
        //console.log("修改密码时两次不一致");
        alert("提示:两次输入的密码不一致，请重新输入!!!");
        $("#registerbutton").prop("disabled",true);
    }
    else{
        console.log("修改密码时两次一致");
        $("#registerbutton").prop("disabled",false);
    }
}

// function checkchange() {
//     var password = $("#password").val();
//     var confirm_password = $("#confirm_password").val();
//     if (password != confirm_password) {
//         console.log("修改密码时两次不一致");
//         alert("提示:两次输入的密码不一致，请重新输入!!!");
//         $("#registerbutton").prop("disabled",true);
//     }
//     else{
//         console.log("修改密码时两次一致");
//         $("#registerbutton").prop("disabled",false);
//     }
// }

/**
 *判断工号（此时是手机号）格式是否符合要求
 */
function checkworknumber(){
    var worknumber=$("#worknumber").val();
    var phonemsg=document.getElementById("phonemsg");
    if(/^1[34578]\d{9}$/.test(worknumber)){
        phonemsg.style.color="#25ff6b";
        phonemsg.innerHTML="手机号格式正确";
        //alert("手机号格式正确");
        $("#registerbutton").prop("disabled",false);
        return true;
    }else{
        phonemsg.style.color="#ff0000";
        phonemsg.innerHTML="手机号不符合格式";
        //alert("请输入正确的手机号");
        $("#registerbutton").prop("disabled",true);
    }
}

/**
 * 修改密码时判断用户名是否存在
 */
function checkexistence(){
    $.ajax({
        type:"get",
        url:"/verifyUser",
        data:{username:$("#username").val()},
        success:function(data){
            if(data=="SUCCESS"){
                alert("该用户未注册");
                $("#findpasswordbutton").prop("disabled",true);
            }
            else if(data=="Have no qualification to change!"){
                alert("Have no qualification to change!");
            }
            else if(data=="ERROR"){
                $("#findpasswordbutton").prop("disabled",false);
            }
        },
        error:function (XMLHttpRequest) {
            // 状态码
            console.log(XMLHttpRequest.status);
            alert("ajax配置出错");
        }
    })
}

/**
 * 忘记密码
 */
function retrievepassword(){
    $.ajax({
        type:"post",
        url:"/retrievepassword",
        data:{
            password:$("#confirm_password").val(),
            username:$("#username").val(),
            phonemsg:$("#phonemsg").val()
        },
        success:function(data){
            if(data=="ERROR1"){
                alert("账号密码不能为空！")
            }
            else if(data=="ERROR2"){
                alert("手机验证码错误");
            }
            else if(data=="ERROR3"){
                alert("该用户未注册")
            }
            else
            {
                alert("密码找回成功，请登录");
                window.location.replace("/login");
            }
        },
        error:function(XMLHttpRequest){
            console.log(XMLHttpRequest.status);
            alert("ajax配置出错，找回密码出错");
        }
    })
}


/**
 *手机验证码功能
 * 此时没有发送，后台检测输入666666为正确
 */
function sendMessage() {
    var code="";
    var count=60;
    //设置button效果，开始计时
    var phone=$("#phone").val();//手机号码
    var reg_phone = /1\d{10}/;
    if(!reg_phone.test(phone)){ //验证手机是否符合格式
        alert("填写手机号");
        return false;
    }
    $("#btnSendCode").attr("disabled", "true");
    $("#btnSendCode").html("请在" + count + "秒内输入验证码");
    var timer = setInterval(function(){
        count--;
        $("#btnSendCode").html("倒计时" + count + "秒");
        if (count==0) {
            clearInterval(timer);
            $("#btnSendCode").attr("disabled",false);//启用按钮
            $("#btnSendCode").html("重新发送验证码");
            code = "";//清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
        }
    },1000);
    //InterValObj = window.setInterval(SetRemainTime(), 1000); //启动计时器，1秒执行一次

    //向后台发送处理数据
    // $.ajax({
    //     type: "POST", //用POST方式传输
    //     url: "/checkmessage", //目标地址
    //     data: {
    //         phonemsg:$("#phonemsg").val()
    //     },
    //     error: function (XMLHttpRequest, textStatus, errorThrown) {
    //         console.log(XMLHttpRequest.status);
    //         alert("ajax配置出错，验证码错误");
    //     },
    //     success: function (data){
    //         if(data=="RIGHT"){
    //             phonemsg.style.color="#a1ff9d";
    //             phonemsg.innerHTML="验证码正确";
    //             $("#findpasswordbutton").prop("disabled",false);
    //         }
    //
    //         else if(data=="WRONG"){
    //             phonemsg.style.color="#ff0000";
    //             phonemsg.innerHTML="验证码错误";
    //             $("#findpasswordbutton").prop("disabled",true);
    //         }
    //     }
    // });
}


//timer处理函数
function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        $("#btnSendCode").removeAttr("disabled");//启用按钮
        $("#btnSendCode").html("重新发送验证码");
    }
    else {
        curCount--;
        $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
    }
}

/**
 * 修改密码时判断输入的账号密码是否正确
 */
function confirmuser() {
    $.ajax({
        type:"post",
        url:"/confirmuser",
        data:{
            username:$("#username").val(),
            password:$("#oldpassword").val()
        },
        success:function(data){
            console.log("看看到底有没有作用"+data);
            if(data=="Please check the password"){
                alert("请正确输入用户名密码!");
                $("#changpasswordbutton").prop("disabled","true");
            }
            else if(data="Have permission to modify!"){
                $("#changpasswordbutton").prop("disabled","false");
            }

        },
        error:function(XMLHttpRequest){
            console.log(XMLHttpRequest.status);
            alert("ajax配置出错，请输入正确的用户名密码用于验证修改密码权限");
        }
    })
}

function recheck() {
    var oldpassword=$("#oldpassword").val();
    var password=$("#password").val();
    if(oldpassword==password){
        alert("密码不能相同！");
    }
}

function changepassword(){
    var username=$("#username").val();
    var oldpassword=$("#oldpassword").val();
    var password=$("#password").val();
    var confirm_password=$("#confirm_password").val();
    // if( localStorage.getItem("username")!=''&&
    //     localStorage.getItem("oldpassword")!=''&&
    //     localStorage.getItem("password")!=''&&
    //     localStorage.getItem("confirm_password")!='')
    //输入为空格或为空
    if((username.indexOf(" ") >= 0 || username == '')||
        (oldpassword.indexOf(" ") >= 0 || oldpassword == '')||
        (password.indexOf(" ") >= 0 || password == '')||
        (confirm_password.indexOf(" ") >= 0 || confirm_password == '')){
            alert("信息填写不全!!");
        }
    else if(oldpassword==password){
        alert("修改密码不应相同！");
    }
    else{
        // localStorage.removeItem("username");
        // localStorage.removeItem("oldpassword");
        // localStorage.removeItem("password");
        // localStorage.removeItem("confirm_password");
        //console.log("信息完整否？");
        $.ajax({
            type:"post",
            url:"/changepassword",
            data:{
                username:$("#username").val(),
                oldpassword:$("#oldpassword").val(),
                password:$("#confirm_password").val()
            },
            success:function (data) {
                console.log("修改密码时的data是"+data);
                switch (data) {
                    case "Have no qualification to change!":
                        alert("Have no qualification to change!");
                        break;
                    case "ERROR3":
                        alert("该用户未注册");
                        break;
                    case "error!!!":
                        alert("修改密码出错");
                        break;
                    default:
                        window.location.replace("/logout");
                        alert("密码修改成功，请登录");
                        window.location.replace("/login");
                        break;
                }
            },
            error:function(XMLHttpRequest){
                console.log(XMLHttpRequest.status);
                alert("ajax配置出错，修改密码出错");
            }
        })
    }


}