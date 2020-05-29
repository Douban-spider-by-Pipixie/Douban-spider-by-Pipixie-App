package cn.itcast.ppx.domain;

public class BooksDetailTab {

    private String authorIntroduction;
    private String bookIntroduction;
    private String table;
    private String id;

    public String getAuthorIntroduction() {
        return authorIntroduction;
    }

    public void setAuthorIntroduction(String authorIntroduction) {
        this.authorIntroduction = authorIntroduction;
    }

    public String getBookIntroduction() {
        return bookIntroduction;
    }

    public void setBookIntroduction(String bookIntroduction) {
        this.bookIntroduction = bookIntroduction;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BooksDetailTab{" +
                "authorIntroduction='" + authorIntroduction + '\'' +
                ", bookIntroduction='" + bookIntroduction + '\'' +
                ", table='" + table + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
