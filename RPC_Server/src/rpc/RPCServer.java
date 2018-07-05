package rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 15:36.
 */
public class RPCServer {

    //定义端口
    private int serverPort;
    //定义一个控制并发数量的线程池
    ThreadPoolExecutor executor=new ThreadPoolExecutor(5,30,200,
            TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(15));

    //定义map来注册服务
    Map<String,Class<?>> serverRegister=new HashMap<>();

    public RPCServer(){};

    public RPCServer(int port){ this.serverPort=port; }

    /**
     * 定义一个暴露服务接口的方法
     * @param serviceInterface 暴露的服务接口名称
     * @param serviceImpl 暴露的服务接口的对应实现类
     */
    public void registerService(Class<?> serviceInterface,Class<?> serviceImpl){
        serverRegister.put(serviceInterface.getName(),serviceImpl);
    }

    /**
     * 定义启动服务的方法
     */
    public void start() throws IOException {
        //建立网络通信
        ServerSocket serverSocket=new ServerSocket();
        serverSocket.bind(new InetSocketAddress(serverPort));
        System.out.println("rpc服务启动...");
        try {
            while (true) {
                executor.execute(new RPCTask(serverSocket.accept()));
            }
        }finally {
            if(serverSocket!=null){
                serverSocket.close();
            }
        }
    }

    /**
     * 定义关闭服务的方法
     */
    public void stop(){
        executor.shutdown();
    }

    class RPCTask implements Runnable{

        private final Socket socket;

        public RPCTask(Socket socket) {
            this.socket=socket;
        }

        @Override
        public void run() {
            //定义反序列化的对象句柄
            ObjectInputStream deSerializer=null;
            //定义序列化的对象句柄
            ObjectOutputStream serializer=null;
            //创建一个反序列化句柄
            try {
                deSerializer=new ObjectInputStream(socket.getInputStream());
                //获取调用的接口名称
                String interfaceName=deSerializer.readUTF();
                //获取调用的方法名称
                String methodName=deSerializer.readUTF();
                //获取方法的参数类型列表
                Class<?>[] parameterTypes=(Class<?>[]) deSerializer.readObject();
                //获取方法的参数列表
                Object[] parameters= (Object[]) deSerializer.readObject();
                //通过暴露的服务接口获取接口的实现类
                Class<?> serviceInstance=serverRegister.get(interfaceName);
                //反射创建一个方法
                Method method=serviceInstance.getDeclaredMethod(methodName,parameterTypes);
                //通过反射调用方法
                Object result=method.invoke(serviceInstance.newInstance(),parameters);
                //把服务端调用处理的结果返回给客户端
                serializer=new ObjectOutputStream(socket.getOutputStream());
                //把结果序列化给客户端
                serializer.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try{
                    if(serializer!=null){
                        serializer.flush();
                        serializer.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                try{
                    if(socket!=null){ socket.close();}
                }catch (IOException e){
                    e.printStackTrace();
                }
                try{
                    if(deSerializer!=null){ deSerializer.close();}
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

}
