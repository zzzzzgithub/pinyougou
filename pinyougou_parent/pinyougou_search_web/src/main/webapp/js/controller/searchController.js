window.onload = function () {
    var app = new Vue({
            el: "#app",
            data: {
                //查询结果集
                resultMap: {brandList: []},
                //搜索条件集{keyword:关键字} category: 商品分类, brand: 品牌,
                // spec: {'网络'：'移动4G','机身内存':'64G',price:'价格区间',pageNo:'当前页'
                // ,pageSize:'每页显示几条',sortFiled:'排序域名' sort:'排序方式asc | desc'}
                searchMap: {
                    keyword: '', category: '', brand: '', spec: {}, price: '',
                    pageNo: '1', pageSize: 20, sortFiled: '', sort: ''
                },
                //pageLable:[分页标签列表]
                pageLable: [],
                //标识分页插件中是否显示前面的省略号
                firstDot:
                    true,
                //标识分页插件中是否显示后面的省略号
                lastDot:
                    true,
                //用于记录查询按钮点击后searchMap.keyword的值
                searchKeyword: ''
            },
            methods: {
                search: function () {
                    axios.post("/item/search.do", this.searchMap).then(function (response) {
                        app.resultMap = response.data;
                        //刷新分页标签
                        app.buildPageLabel();

                    });
                }
                ,
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
                }
                ,
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
                ,
                /**
                 * 构建分页标签
                 */
                buildPageLabel: function () {
                    //查询完数据后,从新开始计算分页标签
                    this.pageLable = [];
                    let firstPage = 1;//开始页码
                    let lastPage = this.resultMap.totalPages;//截至页码
                    this.firstDot = true;//前面有点
                    this.lastDot = true;//后边有点

                    //如果总页数>5
                    if (this.resultMap.totalPages > 5) {
                        //如果当前页<=3,显示前5页
                        if (this.searchMap.pageNo < 3) {
                            lastPage = 5;
                            this.firstDot = false;//前面没点
                            //如果当前页码 >= (总页数-2)，显示后5页

                        } else if (this.searchMap.pageNo >
                            (this.searchMap.pageNo - 2)) {
                            firstPage = this.resultMap.totalPages - 4;
                            this.lastDot = false;//后面没点
                        } else {
                            //显示当前页为中心的5个页码
                            firstPage = this.searchMap.pageNo - 2;
                            lastPage = this.searchMap.pageNo + 2;
                        }

                    } else {
                        this.firstDot = false;//前面没点
                        this.lastDot = false;//后面没点
                    }
                    for (let i = firstPage; i <= lastPage; i++) {
                        this.pageLable.push(i)
                    }
                }
                ,
                /**
                 * 页面跳转-点击事件
                 */
                queryByPage: function (pageNo) {
                    if (pageNo < 1 || pageNo > this.resultMap.totalPages) {
                        alert("请输入正确的页码!")
                        return;
                    }
                    //修改当前页
                    this.searchMap.pageNo = pageNo;
                    //刷新数据
                    this.search();
                },
                /**
                 * 识别关键字是否包含品牌
                 * @return {结果true|false}
                 */
                keywordsIsBrand: function () {
                    for (let i = 0; i < this.resultMap.brandList.length; i++) {
                        //判断是否包含品牌
                        if (this.searchKeyword.indexOf(this.resultMap.brandList[i].text) > -1) {
                            return true;
                        }
                    }
                    return false;
                },
                /**
                 * 排序查询
                 * @param sort 排序方式 asc|desc
                 * @param sortField 域名
                 */
                sortSearch: function (sort, sortField) {
                    this.searchMap.sort = sort;
                    this.searchMap.sortField = sortField;
                    //刷新数据
                    this.search();
                },
                /**
                 * 解析一个url中所有的参数
                 * @return {参数名:参数值}
                 */
                /**
                 * 解析一个url中所有的参数
                 * @return {参数名:参数值}
                 */
                getUrlParam: function () {
                    //url上的所有参数
                    var paramMap = {};
                    //获取当前页面的url
                    var url = document.location.toString();
                    //获取问号后面的参数
                    var arrObj = url.split("?");
                    //如果有参数
                    if (arrObj.length > 1) {
                        //解析问号后的参数
                        var arrParam = arrObj[1].split("&");
                        //读取到的每一个参数,解析成数组
                        var arr;
                        for (var i = 0; i < arrParam.length; i++) {
                            //以等于号解析参数：[0]是参数名，[1]是参数值
                            arr = arrParam[i].split("=");
                            if (arr != null) {
                                paramMap[arr[0]] = arr[1];
                            }
                        }
                    }
                    return paramMap;
                },
                //加载查询字符串
                loadkeywords: function () {
                    //读取参数
                    let keyword = this.getUrlParam()['keyword'];
                    if (keyword != null) {
                        //decodeURI-把url的中文转换回来
                        this.searchMap.keyword = decodeURI(keyword);
                        console.log(this.searchMap.keyword);
                    }
                    //查询商品
                    this.search();
                }
            }
                ,
            created: function () {
                //初始化查询所有商品
                this.search();
                //读取关键字查询
                this.loadkeywords();

            }
        })
    ;
}