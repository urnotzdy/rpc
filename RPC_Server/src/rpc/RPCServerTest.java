package rpc;

import service.IBookService;
import service.impl.BookService;

import java.io.IOException;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 16:08.
 */
public class RPCServerTest {

    public static void main(String[] args) {
        RPCServer rpcServer=new RPCServer(9213);
        rpcServer.registerService(IBookService.class, BookService.class);
        try {
            rpcServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
