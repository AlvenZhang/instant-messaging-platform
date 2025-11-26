# 安全拦截器实现说明

## 概述

在 `com.im.platform.common.interceptor.impl` 包下实现了完整的安全拦截器链，包括 XSS漏洞校验、滑动窗口IP限流、访问资源限制和账号安全校验等功能。

## 组件说明

### 1. XSS漏洞校验

#### XssUtils 工具类
- **功能**: 提供XSS攻击检测和防护功能
- **核心方法**:
  - `checkXss(String input)`: 检查字符串是否包含XSS攻击特征
  - `checkXss(HttpServletRequest request)`: 检查HTTP请求中是否包含XSS攻击
  - `cleanXss(String input)`: 清理XSS攻击代码
- **检测范围**:
  - 请求参数、请求头、请求体
  - Script标签、JavaScript事件处理器、危险HTML标签
  - HTML实体编码绕过检测

#### XssRuleChainService 拦截器服务
- **优先级**: 5
- **功能**: 在请求处理前进行XSS检查，发现攻击时拒绝请求
- **处理方式**: 返回400错误，记录攻击日志

### 2. 滑动窗口限流

#### SlidingWindowLimitService 接口
- **核心方法**:
  - `passThough(String key, long windowSizeMs, int maxRequests)`: 检查是否通过限流
  - `getCurrentCount(String key, long windowSizeMs)`: 获取当前计数
  - `reset(String key)`: 重置计数
  - `setLimitConfig(String key, long windowSizeMs, int maxRequests)`: 设置限流配置

#### RedisSlidingWindowLimitService 实现类
- **技术**: 使用Redis ZSET数据结构实现滑动窗口
- **特性**:
  - 原子性操作保证准确性
  - 自动过期防止内存泄漏
  - 支持动态配置
  - 提供详细统计信息

### 3. IP限制拦截器

#### IPRuleChainService
- **优先级**: 3
- **限流键**: IP地址
- **配置**:
  - 普通IP: 100 requests / 60 seconds
  - 特殊IP(内网): 20 requests / 30 seconds
- **特殊IP识别**: 192.168.*, 10.*, 172.*, 127.0.0.1, IPv6本地地址

### 4. 访问资源限制拦截器

#### PathRuleChainService
- **优先级**: 4
- **限流键**: `路径:用户ID:终端ID`
- **路径分类**:
  - 默认路径: 200 requests / 60 seconds
  - 敏感路径: 50 requests / 60 seconds
  - 高频路径: 10 requests / 10 seconds
- **敏感路径**: `/api/user/login`, `/api/user/register`, `/api/payment/`, `/api/admin/`
- **高频路径**: `/api/message/send`, `/api/file/upload`, `/api/search/query`

### 5. 账号安全校验拦截器

#### AuthRuleChainService
- **优先级**: 10
- **功能**: 验证用户是否已登录
- **检查项**:
  - Session是否存在
  - 用户名是否有效
  - 用户昵称完整性检查
- **错误处理**:
  - 未登录: 401 Unauthorized
  - 用户信息无效: 403 Forbidden

## 拦截器执行顺序

```
1. LogRuleChainService (优先级: 10) - 日志记录
2. XssRuleChainService (优先级: 5) - XSS检查
3. IPRuleChainService (优先级: 3) - IP限流
4. PathRuleChainService (优先级: 4) - 资源限流
5. AuthRuleChainService (优先级: 10) - 账号校验
```

## 使用示例

### 1. 自定义限流规则

```java
@Autowired
private SlidingWindowLimitService slidingWindowLimitService;

// 自定义限流检查
boolean allowed = slidingWindowLimitService.passThough("custom_key", 30000, 50);
if (!allowed) {
    // 处理限流逻辑
}
```

### 2. 重置限流计数

```java
@Autowired
private IPRuleChainService ipRuleChainService;

// 重置IP限流
ipRuleChainService.resetIpLimit("192.168.1.100");

@Autowired
private PathRuleChainService pathRuleChainService;

// 重置资源限流
pathRuleChainService.resetPathLimit("/api/data/export", "user123", "mobile");
```

### 3. 获取统计信息

```java
// 获取IP限流统计
String ipStats = ipRuleChainService.getIpStatistics("192.168.1.100");

// 获取资源限流统计
String pathStats = pathRuleChainService.getPathStatistics("/api/data/export", "user123", "mobile");
```

## 配置说明

### Redis配置要求
```properties
# Redis连接配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=yourpassword
spring.redis.database=0

# 连接池配置
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
```

### 拦截器排除路径
```java
// 在 MvcConfig 中配置排除路径
.excludePathPatterns(
    "/login",           // 登录接口
    "/register",        // 注册接口
    "/error",           // 错误页面
    "/actuator/**",     // 监控端点
    "/swagger-ui/**",   // Swagger UI
    "/static/**"        // 静态资源
)
```

## 监控和日志

### 日志级别
- **正常访问**: INFO级别
- **限流触发**: WARN级别
- **安全攻击**: ERROR级别

### 监控指标
- XSS攻击检测次数
- IP限流触发次数
- 资源限流触发次数
- 未授权访问次数

## 性能考虑

### Redis性能
- 使用管道操作减少网络往返
- 设置合理的过期时间
- 监控Redis内存使用

### 滑动窗口优化
- 避免过小的窗口大小
- 合理设置最大请求数
- 定期清理过期键

### XSS检测优化
- 缓存检测结果
- 避免重复检查
- 优化正则表达式

## 安全建议

### 1. 配置管理
- 使用配置中心动态调整限流参数
- 根据业务高峰期调整限制
- 监控系统负载及时调整

### 2. 异常处理
- Redis异常时的降级策略
- 网络异常时的容错处理
- 日志记录和告警机制

### 3. 扩展功能
- 集成WAF增强防护
- 添加机器学习检测
- 实现分布式限流

## TODO项目

### 1. 增强XSS防护
- 集成更多XSS检测规则
- 支持自定义白名单
- 添加CSP头部支持

### 2. 智能限流
- 基于用户行为的动态限流
- 支持限流规则热更新
- 集成限流效果分析

### 3. 安全监控
- 添加攻击模式分析
- 实现安全事件告警
- 集成安全报表

### 4. 性能优化
- 实现本地缓存
- 优化Redis操作
- 添加性能指标收集

## 故障排查

### 常见问题

1. **限流过于严格**
   - 检查窗口大小和最大请求数配置
   - 验证限流键生成逻辑
   - 查看Redis中的实际数据

2. **XSS误报**
   - 检查正则表达式规则
   - 验证输入数据格式
   - 查看具体匹配的内容

3. **认证失败**
   - 检查Token验证逻辑
   - 验证Session存储
   - 查看用户信息完整性

### 调试方法

1. **启用详细日志**
```properties
logging.level.com.im.platform.common.interceptor.impl=DEBUG
```

2. **监控Redis状态**
```bash
# 查看限流相关键
redis-cli keys "rate_limit:*"

# 查看键的详细信息
redis-cli zrange rate_limit:ip:192.168.1.100 0 -1 WITHSCORES
```

3. **分析请求流程**
- 使用AOP记录拦截器执行时间
- 监控各阶段耗时
- 分析异常发生位置

