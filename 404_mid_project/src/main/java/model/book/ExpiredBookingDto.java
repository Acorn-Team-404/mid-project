package model.book;


public class ExpiredBookingDto {
    private String bookNum;
    private long BOOK_USERS_NUM;
    private long BOOK_STAY_NUM;

    public ExpiredBookingDto(String bookNum, long BOOK_USERS_NUM, long BOOK_STAY_NUM) {
        this.bookNum = bookNum;
        this.BOOK_USERS_NUM = BOOK_USERS_NUM;
        this.BOOK_STAY_NUM = BOOK_STAY_NUM;
    }

    public String getBookNum() {
        return bookNum;
    }

    public long getBookUsersNum() {
        return BOOK_USERS_NUM;
    }
    
    public long getBookStayNum() {
		return BOOK_STAY_NUM;
	}
}
