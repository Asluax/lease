package com.asluax.lease.web.admin.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collections;
import java.util.List;

public class GetPages {
    public static  <T> IPage<T> getPageFromList(List<T> list, long pageNum, long pageSize) {
        Page<T> page = new Page<>(pageNum, pageSize); // 创建分页对象
        int total = list.size(); // 总条数
        long fromIndex = (pageNum - 1) * pageSize;
        long toIndex = Math.min(fromIndex + pageSize, total);

        if (fromIndex > total) {
            page.setRecords(Collections.emptyList()); // 超出范围返回空列表
        } else {
            page.setRecords(list.subList((int) fromIndex, (int) toIndex)); // 取分页数据
        }

        page.setTotal(total); // 设置总条数
        return page;
    }
}
