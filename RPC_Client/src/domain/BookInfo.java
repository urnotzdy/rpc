package domain;

import java.io.Serializable;

/**
 * Description:
 * User: zhangdanyang
 * Date: 2018/7/4 16:10.
 */
public class BookInfo implements Serializable{

    private Integer bookId;
    private String bookName;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
