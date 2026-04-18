# 本次问题排查与修复总结（2026-04-18）

## 一、背景与问题清单

本次根据反馈集中排查了管理端与后端在图片能力上的问题，核心问题如下：

1. 新增轮播图时上传接口路径缺少 `/api` 前缀，导致请求地址错误。
2. 商品管理缺少图片上传能力，仅能依赖已有字段，无法在后台直接上传图片。
3. 教练头像设置问题本次不处理（按需求仅改前后端中的后台与 Java 服务，不改小程序端）。

## 二、根因分析

### 1) 轮播图上传接口路径错误

- 后端实际接口为：`/api/admin/banners/upload`
- 前端请求路径曾为：`/admin/banners/upload`（缺少 `/api`）
- 因此前端在某些环境下会出现上传失败（404/路由不匹配）。

### 2) 商品图片功能不完整

- 后端仅有轮播图上传接口，无商品图片上传接口。
- 管理端商品页面无图片上传控件，列表也仅显示占位图标。

## 三、已完成修复内容

## A. 管理端（fitness-admin-web）

### A1. 修复轮播图上传路径

- 文件：`fitness-admin-web/src/app/components/pages/Banners.tsx`
- 关键修复：
  - 上传地址改为：`/api/admin/banners/upload`
  - 统一引入 `API_ROOT_URL` 处理基础地址，规避环境变量带/不带 `/api` 的拼接问题。

### A2. 商品管理新增图片能力

- 文件：`fitness-admin-web/src/app/components/pages/ProductList.tsx`
- 新增能力：
  - 商品新增/编辑弹窗支持图片上传（文件选择 -> 调用后端接口 -> 回填图片路径）
  - 支持图片预览
  - 保留“图片链接”手动输入框（兼容外链与历史数据）
  - 商品列表优先展示图片，无图时显示原占位图标
  - 上传校验：类型（JPG/PNG/GIF/WebP）与大小（2MB）

### A3. 管理端 API 封装补齐

- 文件：`fitness-admin-web/src/lib/api.ts`
- 新增：
  - `UploadImageResponse` 类型
  - `adminApi.products.uploadImage(file)` 方法
  - 上传接口地址：`/api/admin/products/upload`

## B. 后端（fitness-java）

### B1. 新增商品图片上传接口

- 文件：`src/main/java/org/example/fitnessjava/controller/admin/AdminProductController.java`
- 新增接口：
  - `POST /api/admin/products/upload`
  - `multipart/form-data` 入参：`file`
  - 返回：`{ "image": "/uploads/products/<filename>" }`

### B2. 新增商品上传服务定义与实现

- 文件：`src/main/java/org/example/fitnessjava/service/ProductService.java`
  - 新增方法：`String uploadProductImage(MultipartFile file) throws Exception;`

- 文件：`src/main/java/org/example/fitnessjava/service/impl/ProductServiceImpl.java`
  - 新增 `upload.path` 配置读取
  - 文件保存目录：`<upload.path>/products`
  - 自动创建目录
  - 使用 UUID 生成文件名，保留扩展名
  - 返回访问路径：`/uploads/products/<filename>`

## 四、验证结果

已完成本地构建/编译验证：

1. `fitness-admin-web` 执行 `npm run build` 成功。
2. `fitness-java` 执行 `mvn -q -DskipTests compile` 成功。

说明本次改动在编译层面通过，主要功能路径已闭环。

## 五、当前范围说明

- 本次仅改：管理端（Web）与后端（Java）。
- 未改：小程序端（用户端/教练端）。
- 未实现：教练头像上传接口与后台头像上传 UI（按本次需求暂不处理）。

## 六、后续可选优化（非本次必需）

1. 抽取通用图片上传服务（banner/product/coach 共用），减少重复代码。
2. 增加后端 MIME 与扩展名白名单校验，提升安全性。
3. 增加图片删除与垃圾文件清理策略（避免历史无引用文件累积）。
4. 对上传接口补充审计日志与限流策略。
