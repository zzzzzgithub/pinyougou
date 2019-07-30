window.onload = function () {
    var app = new Vue({
        el: "#app",
        data: {
            loginName: ""
        },
        methods: {
            //获取登录名
            showUserInfo: function () {
                axios.get("/login/name.do").then(function (response) {
                    app.loginName = response.data.loginName;
                })
            }
        },
        created: function () {
            this.showUserInfo();
        }

    })
}