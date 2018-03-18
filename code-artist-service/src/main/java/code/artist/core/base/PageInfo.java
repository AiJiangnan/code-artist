package code.artist.core.base;

import java.util.List;

/**
 * 分页
 *
 * @author 艾江南
 */
public class PageInfo<T> extends com.github.pagehelper.PageInfo<T> {

    private int code;
    private String msg;
    private long count;
    private List<T> data;

    public PageInfo(List<T> list) {
        super(list);
        this.code = 0;
        this.msg = "success";
        this.count = super.getTotal();
        this.data = super.getList();
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
