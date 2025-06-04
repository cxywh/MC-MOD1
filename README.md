```mermaid
graph TD
    subgraph 表示层(Presentation Layer)
        A1[学生管理界面]
        A2[课程管理界面]
        A3[选课管理界面]
        A4[系统概览界面]
    end

    subgraph 业务逻辑层(Business Logic Layer)
        B1[学生管理模块]
        B2[课程管理模块]
        B3[选课管理模块]
        B4[规则验证\n(冲突检测/优先级)]
    end

    subgraph 数据访问层(Data Access Layer)
        C1[内存数据存储\n(集合类)]
        C2[可扩展接口\n支持未来数据库]
    end

    表示层 -->|用户请求| 业务逻辑层
    业务逻辑层 -->|读写数据| 数据访问层
    A1 --> B1
    A2 --> B2
    A3 --> B3 & B4
    B1 & B2 & B3 --> C1
    C1 -.->|实现| C2
```
