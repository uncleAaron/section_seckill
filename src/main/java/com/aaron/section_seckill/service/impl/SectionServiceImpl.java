package com.aaron.section_seckill.service.impl;

import com.aaron.section_seckill.entity.Section;
import com.aaron.section_seckill.mapper.SectionMapper;
import com.aaron.section_seckill.service.SectionService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/7
 */
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    SectionMapper sectionMapper;

    @Override
    public List<Section> findSectionByPage(int page, int size) {
        PageHelper.startPage(page, size);
        return sectionMapper.selectList(null);
    }

    @Override
    public Integer countTakesOfSec(String sec_id) {
        return null;
    }

    @Override
    public Section findSectionById(String sec_id) {
        return null;
    }

    @Override
    public List<Section> findAllSections() {
        return null;
    }
}
