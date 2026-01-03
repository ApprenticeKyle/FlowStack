# Client DTO 说明

本包下的所有DTO都是**远程服务DTO（RemoteDTO）**，用于Feign客户端调用其他微服务时接收数据。

## 命名规范

- 所有远程DTO都以 `RemoteDTO` 结尾
- 例如：`TeamRemoteDTO`、`UserRemoteDTO`、`TaskRemoteDTO`

## 与 interfaces.web.dto 的区别

- **`client.dto.*`** - 用于接收远程服务返回的数据（RPC调用）
- **`interfaces.web.dto.*`** - 用于Controller返回给前端的数据（对外API）

## 数据转换

Service层负责将RemoteDTO转换为interfaces层的DTO，确保：
1. 服务间解耦（不直接依赖其他服务的DTO）
2. 对外接口稳定（前端只看到interfaces层的DTO）
3. 内部实现灵活（可以随时调整RemoteDTO结构）

