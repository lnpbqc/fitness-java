# 待创建 API（教练端小程序）

以下接口在现有 Swagger 中缺失，需要后端补充。所有接口均以 `/api/coach` 开头，仅供教练端小程序使用。

## 1. 教练端预约详情列表

- **接口**: `GET /api/coach/bookings`
- **说明**: 获取当前教练的预约列表，支持按日期过滤
- **请求参数**:
  - `date` (query, optional): 日期 `YYYY-MM-DD`，不传返回全部
- **建议返回**: `ApiBooking[]`
  - `id`, `userId`, `userName`, `coachId`, `coachName`, `coachAvatar`, `specialty`
  - `bookingDate`, `startTime`, `endTime`, `location`, `status`, `statusText`, `source`
  - `packageOrderId`, `phone`

## 2. 教练端改期接口

- **接口**: `PUT /api/coach/bookings/{bookingId}/reschedule`
- **说明**: 教练为指定预约申请改期，提交后需学员确认
- **请求参数**:
  - `bookingId` (path): 预约 ID
  - `scheduleSlotId` (body): 新的时段 ID
  - `reason` (body, optional): 改期原因
- **建议返回**: 成功状态

## 3. 教练端代客预约接口

- **接口**: `POST /api/coach/bookings/proxy`
- **说明**: 教练为指定会员代约课程
- **请求参数**:
  - `clientId` (body): 会员 ID
  - `coachId` (body): 教练 ID
  - `scheduleSlotId` (body): 时段 ID
  - `location` (body, optional): 地点
  - `packageOrderId` (body, optional): 套餐订单 ID
  - `remark` (body, optional): 备注
- **建议返回**: 成功状态

## 4. 教练端扫码核销接口

- **接口**: `POST /api/coach/checkins/scan`
- **说明**: 教练扫码后校验二维码、核销预约、扣减课时
- **请求参数**:
  - `qrCode` (body): 二维码内容
- **建议返回**:
  - `bookingId`, `clientId`, `clientName`, `clientAvatar`
  - `classType`, `scheduledTime`
  - `remainingSessionsBefore`, `remainingSessionsAfter`
  - `checkinStatus`

## 5. 教练端学员体测记录接口

- **接口**: `GET /api/coach/clients/{clientId}/assessment-records`
- **说明**: 获取指定学员的体测记录列表
- **建议返回**: `ApiBodyAssessmentRecord[]`
  - 复用 `BodyAssessmentRecord` schema，补充 `coachId`、`coachName`

- **接口**: `GET /api/coach/clients/{clientId}/assessment-records/{recordId}`
- **说明**: 获取指定体测记录详情

- **接口**: `POST /api/coach/clients/{clientId}/assessment-records`
- **说明**: 教练为指定学员录入体测数据

- **接口**: `PUT /api/coach/clients/{clientId}/assessment-records/{recordId}`
- **说明**: 教练更新指定学员的体测数据

## 6. 教练端学员训练记录接口

- **接口**: `GET /api/coach/clients/{clientId}/training-records`
- **说明**: 获取指定学员的训练记录
- **建议返回字段**:
  - `id`, `clientId`, `coachId`, `date`, `title`, `duration`, `status`, `content`, `comment`

## 7. 教练端绩效统计接口

- **接口**: `GET /api/coach/performance/summary`
- **说明**: 获取教练业绩统计数据
- **建议返回字段**:
  - `memberCount`, `monthlyClassHours`, `monthlyCommission`, `courseIncome`
  - `commissionTrend`, `incomeTrend`
  - `salesData`: `[{ month, amount }]`
  - `classTypeData`: `[{ name, value, color }]`
  - `achievements`: `[{ title, desc, color, icon }]`

- **接口**: `GET /api/coach/performance/trend`
- **说明**: 获取教练收入趋势数据
- **建议返回字段**: `[{ date, amount }]`

## 8. 教练端通知接口

- **接口**: `GET /api/coach/notifications`
- **说明**: 获取当前教练的通知列表

- **接口**: `GET /api/coach/notifications/unread`
- **说明**: 获取未读通知

- **接口**: `GET /api/coach/notifications/unread/count`
- **说明**: 获取未读通知数量
- **建议返回**: `{ count: number }`

- **接口**: `PUT /api/coach/notifications/{id}/read`
- **说明**: 标记通知为已读

- **接口**: `POST /api/coach/notifications/read-all`
- **说明**: 全部标记为已读

## 9. 教练端首页概览接口

- **接口**: `GET /api/coach/dashboard`
- **说明**: 获取教练工作台首页数据
- **建议返回字段**:
  - `todayBookingCount`, `pendingBookingCount`
  - `todayClassCount`, `pendingClassCount`
  - `estimatedIncome`, `unreadNotificationCount`

## 10. `GET /api/coach/clients/{id}` 需要补全 Swagger schema

- **当前返回定义**: `type: object`, `additionalProperties: {}`
- **问题**: 前端无法从文档确认字段结构
- **建议**: 将返回 schema 明确定义为 `Client` 对象，字段覆盖：
  - 基础会员档案（nickname, avatar, phone, gender, age, tags 等）
  - 当前生效课程订单摘要
  - 教练绑定信息
  - 训练记录摘要
  - 体测摘要
