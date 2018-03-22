package code.artist.core.base;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页包装类
 *
 * @author 艾江南
 */
public class PageInfo<T> implements Serializable {

    private int code;
    private String msg;
    private long count;
    private List<T> data;

    public PageInfo(List<T> list) {
        this.code = 0;
        this.msg = "success";
        if (list instanceof Page) {
            Page page = (Page) list;
            this.count = page.getTotal();
            this.data = page;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
