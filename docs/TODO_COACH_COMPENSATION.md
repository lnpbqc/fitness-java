# 教练授课费用配置改造 TODO

## 目标

- 实现“课时单价 + 分成比例”的全局配置，由老板在管理端维护。
- 课程核销后按配置生成教练授课费用流水，保证可追溯、不可重复结算。
- 教练端业绩和收入趋势改为基于真实结算流水统计。

## 实施清单

### 1. 后端数据模型

- [x] 新增系统配置实体（保存 `lessonUnitPrice`、`coachRatio`）。
- [x] 新增教练授课费用流水实体（绑定 `bookingId` 唯一）。
- [x] 新增对应 Repository。

### 2. 后端业务逻辑

- [x] 新增配置服务：读取/更新课时单价与比例，做参数校验。
- [x] 新增结算服务：根据配置计算教练费与老板收益并落库。
- [x] 在核销成功后触发结算，并保证幂等（同一 booking 只结算一次）。

### 3. 后端接口

- [x] 新增管理端接口：获取当前配置。
- [x] 新增管理端接口：更新配置。
- [x] 更新教练端业绩统计服务，改为读取结算流水。
- [x] 更新教练端工作台预估收入逻辑，改为读取配置。

### 4. 管理端前端

- [x] 新增“薪酬配置”页面（展示、编辑、保存）。
- [x] 新增侧边栏菜单与路由。
- [x] 新增 `adminApi.settings` 方法。
- [x] 提交前端校验（单价>=0，比例 0~1）。

### 5. 验证

- [x] 后端 `mvn compile` / `mvn clean compile`。
- [x] 前端 `npm run build`。
- [ ] 手工验证：修改配置后，新核销课程结算金额按新规则生效。

## 业务规则（本次确认）

- 费用与套餐订单金额无关。
- 教练每节课费用 = 全局课时单价 × 全局分成比例。
- 老板收益 = 全局课时单价 - 教练每节课费用。
- 配置修改仅影响之后的新结算，不回溯历史流水。

## 已完成工作说明

### 已新增文件

- `fitness-java/src/main/java/org/example/fitnessjava/pojo/SystemConfig.java`
  - 作用：系统参数持久化实体，保存 `lessonUnitPrice`、`coachRatio` 等全局配置。
- `fitness-java/src/main/java/org/example/fitnessjava/pojo/CoachLessonSettlement.java`
  - 作用：教练授课结算流水实体，按 `bookingId` 唯一约束保证幂等。
- `fitness-java/src/main/java/org/example/fitnessjava/dao/SystemConfigRepository.java`
  - 作用：系统参数读写仓库，提供按 `configKey` 查询能力。
- `fitness-java/src/main/java/org/example/fitnessjava/dao/CoachLessonSettlementRepository.java`
  - 作用：结算流水读写仓库，支持按 `bookingId`、`coachId` 查询。
- `fitness-java/src/main/java/org/example/fitnessjava/service/CoachCompensationService.java`
  - 作用：薪酬配置服务接口，统一定义“获取配置/更新配置/计算每节预估教练费”。
- `fitness-java/src/main/java/org/example/fitnessjava/service/CoachLessonSettlementService.java`
  - 作用：结算服务接口，统一定义“按核销预约生成结算流水”。
- `fitness-java/src/main/java/org/example/fitnessjava/service/impl/CoachCompensationServiceImpl.java`
  - 作用：薪酬配置核心实现。负责参数校验、默认值兜底、派生值计算（教练实得/老板留存）。
- `fitness-java/src/main/java/org/example/fitnessjava/service/impl/CoachLessonSettlementServiceImpl.java`
  - 作用：核销结算核心实现。根据当前配置计算金额并落库，重复核销时自动跳过。
- `fitness-java/src/main/java/org/example/fitnessjava/controller/admin/AdminSettingsController.java`
  - 作用：管理端薪酬配置 API 控制器，提供查询和更新接口。
- `fitness-java/src/main/java/org/example/fitnessjava/pojo/vo/CoachCompensationSettingsVO.java`
  - 作用：返回给前端的薪酬配置展示对象（含派生字段）。
- `fitness-java/src/main/java/org/example/fitnessjava/pojo/vo/UpdateCoachCompensationSettingsRequest.java`
  - 作用：管理端更新薪酬配置入参对象。
- `fitness-admin-web/src/app/components/pages/CompensationSettings.tsx`
  - 作用：管理端“薪酬配置”页面，支持参数编辑、预览、保存。

### 已修改文件

- `fitness-java/src/main/java/org/example/fitnessjava/service/impl/CoachCheckinServiceImpl.java`
  - 核销成功后触发结算流水生成。
- `fitness-java/src/main/java/org/example/fitnessjava/service/impl/CoachPerformanceServiceImpl.java`
  - 业绩统计从硬编码金额切换为结算流水统计。
- `fitness-java/src/main/java/org/example/fitnessjava/service/impl/CoachDashboardServiceImpl.java`
  - 工作台预估收入切换为“配置驱动”。
- `fitness-admin-web/src/lib/api.ts`
  - 增加 `adminApi.settings` 及薪酬配置类型定义。
- `fitness-admin-web/src/app/routes.tsx`
  - 新增 `/settings` 路由。
- `fitness-admin-web/src/app/components/Root.tsx`
  - 侧边栏新增“薪酬配置”菜单。

### 本次编译结果

- 后端：`mvn clean compile` 成功。
- 前端：`npm run build` 成功。

## 关键类职责说明

- `CoachCompensationServiceImpl`
  - 定位：配置中心 + 计价规则提供者。
  - 输入：系统配置参数（课时单价、分成比例）。
  - 输出：当前配置、派生值（教练每节实得、老板每节留存）、每节预估教练费。
  - 主要被谁调用：`AdminSettingsController`（读写配置）、`CoachDashboardServiceImpl`（预估收入）、`CoachLessonSettlementServiceImpl`（实际结算）。

- `CoachDashboardServiceImpl`
  - 定位：教练工作台聚合服务。
  - 输入：预约数据、通知数据、薪酬配置服务输出。
  - 输出：教练工作台首页 `CoachDashboardVO`。
  - 关键变化：`estimatedIncome` 不再写死金额，改为“今日课程数 × 当前配置的每节教练费”。

## 流程图

### 1. 配置修改流程（管理端）

```text
管理员页面 CompensationSettings
        |
        | PUT /api/admin/settings/coach-compensation
        v
AdminSettingsController
        |
        v
CoachCompensationServiceImpl (校验参数, 计算派生值)
        |
        v
SystemConfigRepository -> system_config
        |
        v
返回最新配置给前端
```

### 2. 核销结算流程（教练端）

```text
教练核销成功 (CoachCheckinServiceImpl)
        |
        v
CoachLessonSettlementServiceImpl.settleForBooking(booking)
        |
        |-- 查询 CoachLessonSettlementRepository.findByBookingId
        |      (已存在 -> 跳过, 保证幂等)
        |
        |-- 查询 CoachCompensationServiceImpl.getSettings
        |      (获取课时单价/比例)
        |
        v
计算: coachFee = lessonUnitPrice * coachRatio
计算: bossFee = lessonUnitPrice - coachFee
        |
        v
CoachLessonSettlementRepository.save -> coach_lesson_settlement
```

### 3. 教练看板与业绩读取流程

```text
教练工作台接口
   -> CoachDashboardServiceImpl
   -> CoachCompensationServiceImpl.estimateCoachFeePerClass
   -> 计算 estimatedIncome

教练业绩接口
   -> CoachPerformanceServiceImpl
   -> CoachLessonSettlementRepository (结算流水聚合)
   -> 返回月度提成/趋势数据
```
