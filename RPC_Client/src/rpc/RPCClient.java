package rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 16:11.
 */
public class RPCClient {

    public <T> T getRemoteProxy(Class<?> interfaceClass, InetSocketAddress address){
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //定义socket
                Socket socket=null;
                //定义序列化句柄
                ObjectOutputStream serializer=null;
                //定义反序列化句柄
                ObjectInputStream deSerializer=null;
                try{
                    //创建与服务器的连接
                    socket=new Socket();
                    socket.connect(address);
                    //创建序列化对象
                    serializer=new ObjectOutputStream(socket.getOutputStream());
                    //序列化接口
                    serializer.writeUTF(interfaceClass.getName());
                    //序列化方法
                    serializer.writeUTF(method.getName());
                    //序列化参数类型
                    serializer.writeObject(method.getParameterTypes());
                    //序列化参数
                    serializer.writeObject(args);
                    //创建反序列化句柄
                    deSerializer=new ObjectInputStream(socket.getInputStream());
                    return deSerializer.readObject();
                }catch (Exception e){
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
                        if(deSerializer!=null){ deSerializer.close();}
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    try{
                        if(socket!=null){ socket.close();}
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }

}
