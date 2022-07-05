package R;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 在version0中返回的数据是User对象，在一个应用中不可能只传输一种类型的数据
 * 由此我们将传输对象抽象成为Object
 * RPC需要经过网络传输，有可能失败，类似于http，引入状态码和状态信息表示服务调用成功还是失败
 *
 */
@Data
@Builder
public class RPCResponse implements Serializable {
    //状态信息
    private int code;
    private String message;
    //具体数据
    private Object data;

    public static RPCResponse success(Object data) {
        return RPCResponse.builder().code(200).data(data).build();
    }

    public static RPCResponse fail() {
        return RPCResponse.builder().code(500).message("服务器发生错误").build();
    }
}
