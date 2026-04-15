# 教练端 API 清单（小程序）

> 所有接口均以 `/api/coach` 开头，需携带 `Authorization` 请求头（`Bearer <token>`）。
> 教练登录通过 `POST /api/coach/login` 获取 token。

---

## 1. 教练端预约列表

- **接口**: `GET /api/coach/bookings`
- **Controller**: `coach/CoachBookingController`
- **说明**: 获取当前教练的预约列表，支持按日期过滤
- **请求参数**:
  - `date` (query, optional): 日期 `YYYY-MM-DD`，不传返回全部
- **返回**: `BookingVO[]`
  - `id`, `userId`, `userName`, `coachId`, `coachName`, `coachAvatar`, `specialty`
  - `bookingDate`, `startTime`, `endTime`, `location`, `status`, `statusText`, `source`
  - `packageOrderId`, `phone`

## 2. 教练端改期

- **接口**: `PUT /api/coach/bookings/{bookingId}/reschedule`
- **Controller**: `coach/CoachBookingController`
- **说明**: 教练为指定预约申请改期，提交后需学员确认
- **请求体**: `CoachRescheduleRequest`
  - `scheduleSlotId` (int): 新的时段 ID
  - `reason` (string, optional): 改期原因
- **返回**: `Booking`

## 3. 教练端代客预约

- **接口**: `POST /api/coach/bookings/proxy`
- **Controller**: `coach/CoachBookingController`
- **说明**: 教练为指定会员代约课程
- **请求体**: `CoachProxyBookingRequest`
  - `clientId` (int): 会员 ID
  - `coachId` (int, optional): 教练 ID，不传默认当前教练
  - `scheduleSlotId` (int): 时段 ID
  - `location` (string, optional): 地点
  - `packageOrderId` (string, optional): 套餐订单 ID
  - `remark` (string, optional): 备注
- **返回**: `Booking`

## 4. 教练端扫码核销

- **接口**: `POST /api/coach/checkins/scan`
- **Controller**: `coach/CoachCheckinController`
- **说明**: 教练扫码后校验二维码、核销预约、扣减课时
- **请求体**: `CoachCheckinScanRequest`
  - `qrCode` (string): 二维码内容
- **返回**: `CoachCheckinResponse`
  - `bookingId`, `clientId`, `clientName`, `clientAvatar`
  - `classType`, `scheduledTime`
  - `remainingSessionsBefore`, `remainingSessionsAfter`
  - `checkinStatus`

## 5. 教练端学员体测记录

- **Controller**: `coach/CoachClientAssessmentController`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/coach/clients/{clientId}/assessment-records` | 获取学员体测记录列表 |
| GET | `/api/coach/clients/{clientId}/assessment-records/{recordId}` | 获取体测记录详情 |
| POST | `/api/coach/clients/{clientId}/assessment-records` | 录入体测数据 |
| PUT | `/api/coach/clients/{clientId}/assessment-records/{recordId}` | 更新体测数据 |

- **请求/返回体**: `BodyAssessmentRecord`（含 height, weight, bodyFat, muscleMass, bmi, chest, waist, hips 等）

## 6. 教练端学员训练记录

- **接口**: `GET /api/coach/clients/{clientId}/training-records`
- **Controller**: `coach/CoachClientTrainingController`
- **说明**: 获取指定学员的训练记录
- **返回**: `TrainingRecord[]`
  - `id`, `clientId`, `coachId`, `date`, `title`, `duration`, `status`, `content`, `comment`

## 7. 教练端绩效统计

- **Controller**: `coach/CoachPerformanceController`

| 方法 | 路径 | 说明 | 返回 |
|------|------|------|------|
| GET | `/api/coach/performance/summary` | 业绩统计 | `CoachPerformanceSummaryVO` |
| GET | `/api/coach/performance/trend` | 收入趋势 | `[{ date, amount }]` |

- `CoachPerformanceSummaryVO` 字段: `memberCount`, `monthlyClassHours`, `monthlyCommission`, `courseIncome`, `commissionTrend`, `incomeTrend`, `salesData`, `classTypeData`, `achievements`

## 8. 教练端通知

- **Controller**: `coach/CoachNotificationController`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/coach/notifications` | 获取通知列表 |
| GET | `/api/coach/notifications/unread` | 获取未读通知 |
| GET | `/api/coach/notifications/unread/count` | 获取未读数量，返回 `{ count: number }` |
| PUT | `/api/coach/notifications/{id}/read` | 标记通知为已读 |
| POST | `/api/coach/notifications/read-all` | 全部标记为已读 |

## 9. 教练端首页概览

- **接口**: `GET /api/coach/dashboard`
- **Controller**: `coach/CoachDashboardController`
- **说明**: 获取教练工作台首页数据
- **返回**: `CoachDashboardVO`
  - `todayBookingCount`, `pendingBookingCount`
  - `todayClassCount`, `pendingClassCount`
  - `estimatedIncome`, `unreadNotificationCount`

## 10. 教练端学员管理

- **Controller**: `CoachClientController`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/coach/clients` | 获取当前教练的客户列表 |
| GET | `/api/coach/clients/{id}` | 获取客户详情，返回 `CoachClientDetailVO`（含会员档案、课程订单摘要、教练绑定信息、训练/体测摘要） |
| POST | `/api/coach/clients/bind` | 教练添加学员（body 传 `clientId` 或 `phone`） |
| DELETE | `/api/coach/clients/{clientId}/unbind` | 教练解绑学员 |

## 11. 已有接口（复用）

| 方法 | 路径 | 说明 | Controller |
|------|------|------|------------|
| GET | `/api/coach?tabType=mine` | 学员获取已绑定的教练列表 | `CoachController` |
| GET | `/api/coach/recommended` | 获取推荐教练 | `CoachController` |
| GET | `/api/coach/{id}` | 获取教练详情 | `CoachController` |
| POST | `/api/coach/login` | 教练小程序登录 | `CoachController` |
| GET | `/api/coach/me` | 获取当前教练信息 | `CoachController` |
