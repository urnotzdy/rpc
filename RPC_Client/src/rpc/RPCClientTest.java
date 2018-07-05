package rpc;

import domain.BookInfo;
import service.IBookService;

import java.net.InetSocketAddress;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 17:52.
 */
public class RPCClientTest {

    public static void main(String[] args) {
        RPCClient client=new RPCClient();
        IBookService service=client.getRemoteProxy(IBookService.class,new InetSocketAddress("localhost",9213));
        BookInfo bookInfo=new BookInfo();
        bookInfo.setBookId(123);
        bookInfo.setBookName("三国演义");
        String result=service.addBook(bookInfo);
        System.out.println(result);
    }

}
