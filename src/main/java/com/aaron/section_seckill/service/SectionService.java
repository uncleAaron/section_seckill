package com.aaron.section_seckill.service;

import com.aaron.section_seckill.entity.Section;

import java.util.List;

/**
 * <p>
 * for section_manage
 * </p>
 *
 * @author AaronHuang
 * @since 2018/9/7
 */
public interface SectionService {

    /**
     * 通过secID找到section(内置信息齐全)
     * @param sec_id
     * @return
     * @throws Exception
     */
    Section findSectionById(String sec_id);

    List<Section> findAllSections();

    List<Section> findSectionByPage(int page, int size);

    Integer countTakesOfSec(String sec_id);
}
