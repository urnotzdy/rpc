package service;

import domain.BookInfo;

import java.io.Serializable;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 15:33.
 */
public interface IBookService extends Serializable{

    //新增图书
    public String addBook(BookInfo book);

}
