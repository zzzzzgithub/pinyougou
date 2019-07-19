window.onload = function () {
    var app = new Vue({
        el: "#app",
        data: {
            //广告列表
            contentList: []
        },
        methods: {
            //查询广告列表
            findContentList: function () {
                //查询广告轮播图
                axios.get("/content/findByCategoryId.do?categoryId=1").then(function (response) {
                    //app.contentList[1] = response.data;
                    app.$set(app.contentList, 1, response.data)
                })
            }
        },
        //初始化调用
        created: function () {
            //查询广告列表
            this.findContentList();
        }
    });
}


