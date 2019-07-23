window.onload = function () {
    var app = new Vue({
        el: "#app",
        data: {
            //查询结果集
            resultMap: {},
            //搜索条件集{keyword:关键字} category: 商品分类, brand: 品牌,
            // spec: {'网络'：'移动4G','机身内存':'64G',price:'价格区间'}
            searchMap: {keyword: '', category: '', brand: '', spec: {}, price: ''}
        },
        methods: {
            search: function () {
                axios.post("/item/search.do", this.searchMap).then(function (response) {
                    app.resultMap = response.data;
                });
            },
            /**
             * 添加搜索项
             * @param key
             * @param value
             */
            addSearchItem: function (key, value) {
                //如果点击的是分类或者是品牌
                if (key == 'category' || key == 'brand' || key == 'price') {
                    app.$set(this.searchMap, key, value);
                } else {
                    app.$set(this.searchMap.spec, key, value)
                }
                //刷新页面
                this.search();
            },
            /**
             * 删除搜索项
             * @param key
             */
            removeSearchItem: function (key) {
                //如果分类是品牌
                if (key == 'category' || key == 'brand' || key == 'price') {
                    app.$set(this.searchMap, key, "")
                } else {//否则是规格
                    app.$delete(this.searchMap.spec, key)//移除此属性
                }
                //刷新页面
                this.search();
            }
        },
        created: function () {
            //初始化查询所有商品
            this.search();
        }
    });
}