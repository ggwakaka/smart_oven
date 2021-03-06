package com.project.repository;

import com.project.entity.CodeString;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CodeStringRepository extends CrudRepository<CodeString,Integer> {

    CodeString findFirstByMobileNumOrderByCreateTimeDesc(String mobileNum);

    List<CodeString> findAllByMobileNumAndCreateTimeAfter(String mobileNum, Long createTime);
}
