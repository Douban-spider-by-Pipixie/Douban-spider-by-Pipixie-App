package cn.itcast.ppx.domain;

//评论网络数据
public class CommentsTab {

    private String comment;
    private String date;
    private String start;
    private String useful;
    private String user;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getUseful() {
        return useful;
    }

    public void setUseful(String useful) {
        this.useful = useful;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CommentsTab{" +
                "comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                ", start='" + start + '\'' +
                ", useful='" + useful + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
