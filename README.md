> # controller 
> 在client则是client专用
> 在 admin 则是管理端专用
> 在 coach 则是教练端专用
> 在外面则是通用

#### API: http://localhost:8081/swagger-ui/index.html

`/src/main/resources/dropDB.sql` 
> 清除所有文件


套餐
+ 私教次数卡
+ 体验卡
+ 评估卡

教练预约的时候只搜  SESSION_CARD

预约的时候绑定对应的套餐

核销的时候在套餐订单中扣减

问卷

体检
