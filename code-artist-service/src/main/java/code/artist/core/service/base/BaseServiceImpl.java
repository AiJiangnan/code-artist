package code.artist.core.service.base;

import code.artist.common.base.BaseMapper;
import code.artist.common.base.BaseModel;
import code.artist.common.base.IBaseService;
import code.artist.core.base.PageInfo;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class BaseServiceImpl<T extends BaseModel> implements IBaseService<T> {

    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public List<T> selectEntityList() {
        return baseMapper.selEntityList();
    }

    @Override
    public String selectEntityPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> tList = baseMapper.selEntityList();
        PageInfo<T> tPageInfo = new PageInfo<>(tList);
        return JSON.toJSONString(tPageInfo);
    }

    @Override
    public T selectEntityById(Object id) {
        return baseMapper.selEntityById(id);
    }

    @Override
    public int insertEntity(T entity) {
        Date date = new Date();
        entity.setCreateTime(date);
        entity.setUpdateTime(date);
        return baseMapper.insEntity(entity);
    }

    @Override
    public int updateEntityById(T entity) {
        entity.setUpdateTime(new Date());
        return baseMapper.updEntityById(entity);
    }
}