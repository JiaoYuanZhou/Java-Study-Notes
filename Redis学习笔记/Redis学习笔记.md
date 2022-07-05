## 五种数据类型

### String

- **set** key1  1
- **get** key1
- **APPEND** key1 "hello"   在后面追加字符串
- **STRLEN** key1   截取字符串长度
- **EXISTS** key1   判断字符串是否存在
- **incr** key1    使用一次就加1，可做浏览量
  - **INCRBY** key1 10   加10
- **decr** key1 减1
  - **DECRBY** key1 10 减10
- **flushdb**  清空数据库
- **flushall** 清空所有数据库
- **GETRANGE** key1  0  3  截取字符串[0,3]
- **SETRANGE** key1  1 "hello"   从索引1开始替换
- **setex** key1 30 "hello" 设置key1过期时间为30秒
- **setnx** key1 ”redis“  如果key1已经存在则key1保存原样
- **mset** k1 v1 k2 v2 k3 v3 批量操作
- **msetnx** k1 v1 k2 v2原子性操作
- **mset ** user:1:name user:1:age   设置对象
- **mget** user:1
- **set** user:1 {name:zhangsan, age:5}
- **getset** key1 "hello" 先获得再设置



### List

**所有的list命令都是以l开头的**   Redis不区分大小写  可以不加引号，会自动识别

- **LPUSH  list one**  从左边添加值
  - **LPOP list**  移左边第一个 
- **LRANGE list 0 -1 ** 获取所有值
- **RPUSH list two** 从右边插入
  - **RPOP list**  移右边第一个
- **Lindex list 0** 通过下标获得某一个值
- **Llen list**  返回列表长度
- **Lrem list 1 one** 移除one这个值  1代表移除一个 从左到右
- **Ltrim list 1  2** 截取索引1到2的值 【1,2】
- **RpopLpush list otherlist**  将list中的最后一个**移到**otherlist第一个
- **Lset list 0 item** 将list中的第0位设置成item，如果list不存在则设置失败
- **Linsert list before "world"  “other”** 在list列表中的world前面插入other
