# 新工资计算器 - 规格文档

## 1. 项目概述

**项目名称**: WageCalc  
**包名**: com.wagecalc.app  
**性质**: 安卓原生应用（Kotlin + Jetpack Compose）  
**核心功能**: 日历式工资录入与统计，支持每日详细工资构成、月度汇总、预设功能，以及升级后数据不丢失

---

## 2. UI/UX 设计

### 2.1 页面结构

**单页面设计**（底部 Tab 切换）:
- **录入 Tab**: 日历 + 当日工资录入表单
- **汇总 Tab**: 本月收入列表 + 总和

### 2.2 颜色主题

| 用途 | 颜色 |
|------|------|
| 主色 | #2196F3（蓝色） |
| 次色 | #1976D2 |
| 强调色 | #4CAF50（绿色，收入） |
| 背景 | #FAFAFA |
| 卡片背景 | #FFFFFF |
| 文字主色 | #212121 |
| 文字次色 | #757575 |

### 2.3 字体

- 标题: 20sp, Bold
- 副标题: 16sp, Medium
- 正文: 14sp, Regular
- 金额大数字: 28sp, Bold, 强调色

---

## 3. 功能详细设计

### 3.1 日历组件（录入页顶部）

- 显示当前月份，左右箭头切换月份
- 日期格可点击，点击后选中该天
- 选中日期高亮（主色背景）
- 有记录的日期显示小圆点标记

### 3.2 当日工资录入表单（选中日期后显示）

| 字段 | 类型 | 说明 |
|------|------|------|
| 基本工资 | 数字输入 | 必填 |
| 基本工资倍数 | 数字输入 | 默认 1.0 |
| 加班小时数 | 数字输入 | 可为小数，如 1.5 |
| 预设小时工资 | 数字输入 + 勾选框 | 勾选后生效 |
| 预设日工资 | 数字输入 + 勾选框 | 勾选后生效 |
| 额外收入 | 数字输入 | 可为空或0 |

**工资计算公式**:
```
当日工资 = (基本工资 × 倍数) + (加班小时数 × 小时工资) + 额外收入 + 全天工资
```
其中:
- 小时工资 = 预设小时工资（勾选时生效）× 倍数
- 全天工资 = 预设日工资（勾选时生效）

### 3.3 保存按钮

- 点击后计算当日工资并存入数据库
- 显示该日工资预览
- 刷新日历上的标记

### 3.4 本月收入汇总（汇总 Tab）

- **本月总收入**: 顶部大字显示
- **每日明细列表**: 
  - 日期 | 工资构成摘要 | 当日合计
  - 点击可编辑

### 3.5 数据持久化策略

**升级保护方案**:
- 使用 **Room 数据库** 存储在 app 内部存储
- package 名固定: `com.wagecalc.app`
- Android 系统升级 APK 时，`/data/data/com.wagecalc.app/` 目录数据自动保留
- 使用 Room Migration 应对数据库结构变更

**数据结构**:
```
DailyWageRecord:
  - id: Long (主键，自增)
  - date: String (格式: yyyy-MM-dd)
  - basicWage: Double
  - multiplier: Double
  - overtimeHours: Double
  - hourlyWagePreset: Double (勾选值，未勾选存0)
  - hourlyWageEnabled: Boolean
  - dailyWagePreset: Double (勾选值，未勾选存0)
  - dailyWageEnabled: Boolean
  - extraIncome: Double
  - totalWage: Double (计算后的当日工资)
  - createdAt: Long
  - updatedAt: Long
```

---

## 4. 技术架构

### 4.1 技术栈

- **语言**: Kotlin 1.9.x
- **UI**: Jetpack Compose (BOM 2024.02.00)
- **数据库**: Room 2.6.x
- **架构**: MVVM + Repository
- **依赖注入**: Hilt
- **构建**: Gradle 8.4 + AGP 8.2.0
- **最低 SDK**: 26 (Android 8.0)
- **目标 SDK**: 34 (Android 14)

### 4.2 项目结构

```
app/
├── src/main/
│   ├── java/com/wagecalc/app/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── DailyWageRecord.kt
│   │   │   │   └── DailyWageDao.kt
│   │   │   └── repository/
│   │   │       └── WageRepository.kt
│   │   ├── di/
│   │   │   └── AppModule.kt
│   │   ├── ui/
│   │   │   ├── theme/
│   │   │   ├── screens/
│   │   │   │   ├──录入/InputScreen.kt
│   │   │   │   └──汇总/SummaryScreen.kt
│   │   │   └── components/
│   │   │       ├── Calendar.kt
│   │   │       └── WageForm.kt
│   │   └── MainActivity.kt
│   └── res/
```

---

## 5. 构建与发布

- **仓库**: sting1023/WageCalc
- **签名**: release 构建使用 v2 APK Signature Scheme
- **输出**: 签名 APK 直接安装使用
- **版本**: 1.0.0 (初始版本)

---

## 6. 升级策略

- 数据库表设计预留扩展字段
- Room Migration 脚本应对结构变更
- SharedPreferences 存储用户预设（小时工资默认值、日工资默认值）
- 所有数据存在 app 内部目录，升级不丢失
