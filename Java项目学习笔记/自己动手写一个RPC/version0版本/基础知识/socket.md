## 什么是Socket？

- **网络上两个程序通过一个双向的通信连接实现数据的交换，这个连接的一端就称为一个Socket**
- **支持TCP/IP协议的网络通信的基本操作单元，包含进行网络通信必须的五种信息**
  - 本地主机的IP地址
  - 本地进程的协议端口
  - 远地主机的IP地址
  - 远地进程的协议端口
- **Socket的本质是编程接口（API），对TCP/IP的封装，TCP/IP也要提供可供程序员做网络开发所用的接口，这就是Socket编程接口**



## Socket的原理

​	Socket实质上提供了进程通信的端点，进程通信之前，双方首先必须各自创建一个端点，否则没有办法建立联系并相互通信的。

​	**Socket之间的分为三步：1、服务器监听，2、客户端请求，3、连接确认**

- **服务器监听：**服务器端Socket并不定位具体的客户端Socket，而是处于等待连接的状态，实时监控网络状态
- **客户端请求：**由客户端的Socket提出连接请求，要连接的目标是服务端的Socket，首先客户端必须描述它要连接的服务器的Scoket，指出服务端Scoket的地址和端口号，然后发出请求
- **连接确认：**是指服务端接收到客户端的连接请求，它就响应客户端Scoket的请求，**建立一个新的线程**，把服务端Socket的描述发给客户端，**一旦客户端确认了此描述，连接就建立好了**，而服务端Socket继续处于监听状态，继续接收其他客户端Socket的连接请求



## 基于java的Socket网络编程实现

​	**Server端Listen监听某个端口是否有连接请求，Client端向Server发出连接请求，Server端向Client 端发回Accpet接收消息。这样一个连接就建立起来了。Server端和Client端都可以通过Send，Write等方法与对方通信**

​	**工作过程：**

1. 创建Socket
2. 打开连接到Socket的输入/出流
3. 按照一定的协议对Socket进行读/写操作
4. 关闭Socket



### 基于TCP的socket实现

**SocketClient**

```java
import java.io.*;
import java.net.Socket;

public class SocketClient {
    /**
     * Socket工作过程四个步骤：
     * 1、创建Socket
     * 2、打开连接到Socket的输入/出流
     * 3、按照一定的协议对Socket进行读/写操作；
     * 4、关闭Socket
     * @param args
     */
    public static void main(String[] args) throws IOException {
        //1、创建Socket，和服务端建立连接
        Socket socket = new Socket("localhost",8088);

        //2、要发送给客户端的消息
        OutputStream outputStream = socket.getOutputStream();

        //PrintWriter:向输出流里写入东西
        PrintWriter pw = new PrintWriter(outputStream);
        pw.write("hahah,我是客户端啦");
        pw.flush();  //清空缓冲区

        socket.shutdownOutput(); //禁用输出流

        //3、从服务端得到的信息
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String info = null;
        while((info = bufferedReader.readLine())!=null) {
            System.out.println("我是客户端，服务器返回信息：" + info);
        }

        bufferedReader.close();
        inputStream.close();
        outputStream.close();
        pw.close();
        socket.close();

        }
}

```



**SocketServer**

```java
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void main(String[] args) {
        try {
            // 创建服务端socket
            ServerSocket serverSocket = new ServerSocket(8088);

            // 创建客户端socket
            Socket socket = new Socket();

            //循环监听等待客户端的连接
            while(true){
                // 监听客户端
                socket = serverSocket.accept();

                ServerThread thread = new ServerThread(socket);
                thread.start();

                InetAddress address=socket.getInetAddress();
                System.out.println("当前客户端的IP："+address.getHostAddress());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
```



**ServerThread**

```java
import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{

    private Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        OutputStream os=null;
        PrintWriter pw=null;
        try {
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String info = null;

            while((info=br.readLine())!=null){
                System.out.println("我是服务器，客户端说："+info);
            }
            socket.shutdownInput();

            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write("服务器欢迎你");

            pw.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally{
            //关闭资源
            try {
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
                if(br!=null)
                    br.close();
                if(isr!=null)
                    isr.close();
                if(is!=null)
                    is.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
```



### 基于UDP的socket实现

```java
public class SocketClient {
	
	public static void main(String[] args) {
		try {
            // 要发送的消息
            String sendMsg = "客户端发送的消息";
            
            // 获取服务器的地址
            InetAddress addr = InetAddress.getByName("localhost");
            
            // 创建packet包对象，封装要发送的包数据和服务器地址和端口号
            DatagramPacket packet = new DatagramPacket(sendMsg.getBytes(),
            		sendMsg.getBytes().length, addr, 8088); 
            
            // 创建Socket对象
            DatagramSocket socket = new DatagramSocket();
            
            // 发送消息到服务器
            socket.send(packet);
 
            // 关闭socket
            socket.close();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}
```



```java
public class SocketServer {
	
	public static void main(String[] args) {
		try {
			 // 要接收的报文
			byte[] bytes = new byte[1024];
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
			
			// 创建socket并指定端口
			DatagramSocket socket = new DatagramSocket(8088);
			
			// 接收socket客户端发送的数据。如果未收到会一致阻塞
			socket.receive(packet);
			String receiveMsg = new String(packet.getData(),0,packet.getLength());
			System.out.println(packet.getLength());
			System.out.println(receiveMsg);
			
			// 关闭socket
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
 
}
```



