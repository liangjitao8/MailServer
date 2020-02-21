$(function() {
    var registerUrl="/mail/registe";
    $('#submit').click(function(){
        var localAuth = {};
        var personInfo = {};
        localAuth.userName=$('#username').val();
        localAuth.password=$('#password').val();
        personInfo.name=$('#name').val();
        personInfo.email=$('#email').val();
        personInfo.phone=$('#phone').val();
        localAuth.personInfo=personInfo;
        var formData=new FormData();
        formData.append('localAuthStr', JSON.stringify(localAuth));
        $.ajax({
            url:registerUrl,
            type:'POST',
            data:formData,
            contentType : false,
            processData : false,
            cache : false,
            success :function (data) {
                if(data.success){
                    $.toast(data.msg);
                }else
                    $.toast(data.errMsg);
            }
        });
    });
});