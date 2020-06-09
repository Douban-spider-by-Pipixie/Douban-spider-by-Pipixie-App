package cn.itcast.ppx.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.itcast.ppx.domain.BooksTab;
import cn.itcast.ppx.domain.CommentsTab;

public class JsonParse {
    public static List<BooksTab> getBooksTab(String json){
        Gson gson=new Gson();
        Type listType =new TypeToken<List<BooksTab>>(){}.getType();
        List<BooksTab> mBooksTabs=gson.fromJson(json,listType);
        return mBooksTabs;
    }
    public static List<CommentsTab> getCommentsTab(String json){
        Gson gson=new Gson();
        Type listType =new TypeToken<List<CommentsTab>>(){}.getType();
        List<CommentsTab> mCommentsTabs=gson.fromJson(json,listType);
        return mCommentsTabs;
    }
}
