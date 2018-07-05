package service.impl;

import domain.BookInfo;
import service.IBookService;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 15:34.
 */
public class BookService implements IBookService{

    @Override
    public String addBook(BookInfo book) {

        System.out.println("新增一本书");
        return book.toString();
    }
}
