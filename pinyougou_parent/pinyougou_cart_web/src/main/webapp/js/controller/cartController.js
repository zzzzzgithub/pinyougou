window.onload = function () {
    var app = new Vue({
        el: "#app",
        data: {
            //购物车列表
            cartList: [],
            //统计数据
            totalValue: {totalNum: 0, totalMoney: 0.0},
            //收件人列表
            addressList: [],
            //用户当前选中的地址
            address: {},
            //支付方式
            order: {paymentType: '1'}

        },
        methods: {
            //查询当前用户的购物车列表
            findCartList: function () {
                axios.get("/cart/findCartList.do").then(function (response) {
                    app.cartList = response.data;

                    //每次查询后重新计算金额与数量
                    app.totalValue = {totalNum: 0, totalMoney: 0.00};
                    //统计数量与金额
                    for (let i = 0; i < app.cartList.length; i++) {
                        let cart = app.cartList[i];
                        for (let j = 0; j < cart.orderItemList.length; j++) {
                            //读取购物车明细，统计数量与金额
                            let orderItem = cart.orderItemList[j];
                            app.totalValue.totalNum += orderItem.num;
                            app.totalValue.totalMoney += orderItem.totalFee;
                        }
                    }
                })

            },
            /**
             * 购物车操作逻辑
             * @param itemId 操作的商品
             * @param num 修改的数量
             */
            addGoodsToCartList: function (itemId, num) {
                axios.get("/cart/addGoodsToCartList.do?itemId=" + itemId + "&num=" + num)
                    .then(function (response) {
                        if (response.data.success) {
                            //刷新数据
                            app.findCartList();
                        } else {
                            alert(response.data.message);
                        }
                    })
            },
            //查询收件人列表
            findAddressList: function () {
                axios.get("/address/findListByUserId.do").then(function (response) {
                    app.addressList = response.data;
                    //设置默认地址读取
                    for (let i = 0; i < app.addressList.length; i++) {
                        //找到默认地址
                        if (1 == app.addressList[i].isDefault) {
                            app.address = app.addressList[i];
                            return;

                        }
                    }

                })
            },
            //用户选择收件人
            selectAddress: function (address) {
                this.address = address;

            },
            //选择支付方式
            selectPayType: function (type) {
                this.order.paymentType = type;
            },
            //保存订单
            submitOrder: function () {
                //把收件人相关信息读取并关联
                this.order.receiverAreaName = this.address.address;
                this.order.receiverMobile = this.address.mobile;
                this.order.receiver = this.address.contact;
                //保存订单
                axios.post("/order/add.do", this.order).then(function (response) {
                    alert(response.data.message);
                    if (response.data.success) {
                        if (app.order.patmentType == 1) {
                            window.location.href = "pay.html";
                        } else {
                            window.location.href = "paysuccess.html";
                        }
                    }
                })
            }
        },
        created: function () {
            this.findCartList();
            this.findAddressList();
        }
    });
}