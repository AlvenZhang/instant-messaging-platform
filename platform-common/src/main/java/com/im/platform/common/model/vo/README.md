# 模型值对象 (VO) 说明

## 概述

`com.im.platform.common.model.vo` 包包含了即时消息平台系统中所有的值对象（Value Object），用于在不同层之间传递数据和对外提供API响应。

## VO类列表

### 1. UserVO - 用户信息VO
**用途**: 用户基本信息的展示和传输

**属性**:
- `id`: 用户ID
- `userName`: 用户名
- `nickName`: 用户昵称
- `sex`: 性别
- `type`: 用户类型 (1:普通用户 2:审核账户)
- `signature`: 个性签名
- `headImage`: 头像
- `headImageThumb`: 头像缩略图
- `online`: 是否在线

**使用场景**:
- 用户信息查询
- 用户列表展示
- 用户资料编辑
- 在线状态显示

### 2. GroupVO - 群信息VO
**用途**: 群聊信息的展示和传输

**属性**:
- `id`: 群ID
- `name`: 群名称
- `ownerId`: 群主ID
- `headImage`: 头像
- `headImageThumb`: 头像缩略图
- `notice`: 群公告
- `aliasName`: 用户在群显示昵称
- `remark`: 群聊显示备注

**使用场景**:
- 群聊列表展示
- 群聊详情查看
- 群聊创建/编辑
- 群聊搜索

### 3. LoginVO - 用户登录VO
**用途**: 登录成功后的token信息返回

**属性**:
- `accessToken`: 访问令牌（每次请求都必须在header中携带）
- `accessTokenExpiresIn`: 访问令牌过期时间(秒)
- `refreshToken`: 刷新令牌（用于获取新的访问令牌）
- `refreshTokenExpiresIn`: 刷新令牌过期时间(秒)

**使用场景**:
- 用户登录响应
- 令牌刷新响应
- 第三方系统集成

### 4. FriendVO - 好友信息VO
**用途**: 好友信息的展示和传输

**属性**:
- `id`: 好友ID
- `nickName`: 好友昵称
- `headImage`: 好友头像

**使用场景**:
- 好友列表展示
- 好友搜索
- 好友详情查看
- 聊天对象选择

### 5. GroupInviteVO - 邀请好友进群请求VO
**用途**: 邀请好友进群的请求数据

**属性**:
- `groupId`: 群ID
- `friendIds`: 好友ID列表

**使用场景**:
- 邀请好友进群
- 批量邀请处理
- 群成员管理

### 6. GroupMemberSimpleVO - 群成员简易信息VO
**用途**: 群成员的基本信息展示

**属性**:
- `aliasName`: 群内显示名称
- `quit`: 是否已退出
- `groupId`: 群组ID
- `createdTime`: 创建时间

**使用场景**:
- 群成员列表（简易版）
- 群成员状态查询
- 群管理操作

### 7. GroupMemberVO - 群成员信息VO
**用途**: 完整的群成员信息（继承自GroupMemberSimpleVO）

**属性**:
- 继承属性: `aliasName`, `quit`, `groupId`, `createdTime`
- `userId`: 用户ID
- `headImage`: 头像
- `online`: 是否在线
- `remark`: 备注

**使用场景**:
- 群成员详情查看
- 群管理界面
- 在线状态显示
- 权限管理

### 8. UploadImageVO - 图片上传VO
**用途**: 图片上传后的返回结果

**属性**:
- `originUrl`: 原图URL
- `thumbUrl`: 缩略图URL

**使用场景**:
- 头像上传
- 聊天图片发送
- 文件上传响应
- 图片处理结果

### 9. GroupMessageVO - 群消息VO
**用途**: 群聊消息的传输和存储

**属性**:
- `id`: 消息ID
- `groupId`: 群聊ID
- `sendId`: 发送者ID
- `sendNickName`: 发送者昵称
- `content`: 消息内容
- `type`: 消息内容类型（具体枚举值由应用层定义）
- `atUserIds`: @用户列表
- `atUserIdsStr`: @用户列表（字符串格式）
- `status`: 状态
- `sendTime`: 发送时间

**使用场景**:
- 群聊消息发送/接收
- 消息历史查询
- 消息状态同步
- @功能实现

### 10. OnlineTerminalVO - 在线终端VO
**用途**: 用户在线终端信息

**属性**:
- `userId`: 用户ID
- `terminals`: 在线终端类型列表

**使用场景**:
- 在线状态显示
- 多端登录管理
- 消息推送策略
- 终端状态同步

### 11. PrivateMessageVO - 私聊消息VO
**用途**: 私聊消息的传输和存储

**属性**:
- `id`: 消息ID
- `sendId`: 发送者ID
- `recvId`: 接收者ID
- `content`: 发送内容
- `type`: 消息内容类型（IMCmdType）
- `status`: 状态
- `sendTime`: 发送时间

**使用场景**:
- 私聊消息发送/接收
- 消息历史查询
- 消息状态同步
- 好友聊天

## 设计原则

### 1. 序列化支持
- 所有VO类都实现`Serializable`接口
- 定义了`serialVersionUID`用于版本控制

### 2. 完整性
- 提供无参构造函数
- 提供有参构造函数（常用属性）
- 完整的getter/setter方法
- 重写toString()方法便于调试

### 3. 继承设计
- `GroupMemberVO`继承自`GroupMemberSimpleVO`
- 体现了IS-A关系，避免代码重复

### 4. 数据类型安全
- 使用包装类型（Long, Integer, Boolean）避免空值问题
- 时间字段使用`java.util.Date`类型

## 使用建议

### 1. API响应
- 所有对外API的响应都应该使用VO对象
- 避免直接返回实体类（Entity）对象

### 2. 数据转换
- 在Service层进行Entity到VO的转换
- 使用BeanUtils或MapStruct等工具简化转换

### 3. 验证
- 在Controller层对VO进行参数验证
- 使用JSR-303注解进行验证规则定义

### 4. 文档
- 为API接口生成文档时，VO类的属性会自动包含
- 保持属性名称的语义化

## 扩展指南

### 1. 新增VO类
- 继承`Serializable`接口
- 定义`serialVersionUID`
- 提供完整的构造函数和方法

### 2. 属性扩展
- 保持向后兼容性
- 使用包装类型
- 添加适当的注释

### 3. 特殊需求
- 对于复杂逻辑，考虑添加计算属性
- 对于性能要求高的场景，考虑使用DTO模式

## 常见问题

### Q: 为什么不直接使用Entity对象？
A: VO对象专注于展示和传输，避免将内部实现细节暴露给外部，同时可以根据前端需求灵活调整数据结构。

### Q: VO和DTO的区别？
A: VO主要用于展示层，DTO主要用于数据传输。在这个项目中，VO承担了两个角色。

### Q: 如何处理敏感信息？
A: 对于敏感信息，应该在转换VO时进行脱敏处理，不要在VO中直接存储敏感数据。

### Q: 如何处理大数据量？
A: 对于列表查询，考虑使用分页VO或简化版VO，避免传输过多不必要的数据。

