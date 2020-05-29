package cn.itcast.ppx.domain;

//推荐图书网络数据
public class BooksTab  {

    private String author;
    private String commentCount;
    private String date;
    private String id;
    private String img;
    private String name;
    private String price;
    private String publish;
    private String star;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    @Override
        public String toString() {
            return "BooksTab{" +
                    "author='" + author + '\'' +
                    ", name='" + name + '\'' +
                    ", commentCount='" + commentCount + '\'' +
                    ", date='" + date + '\'' +
                    ", id='" + id + '\'' +
                    ", img='" + img + '\'' +
                    ", price='" + price + '\'' +
                    ", publish='" + publish + '\'' +
                    ", star='" + star + '\'' +
                    '}';

    }
}
