package io.metersphere.base.mapper;

import io.metersphere.base.domain.ApiTestFile;
import io.metersphere.base.domain.ApiTestFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiTestFileMapper {
    long countByExample(ApiTestFileExample example);

    int deleteByExample(ApiTestFileExample example);

    int insert(ApiTestFile record);

    int insertSelective(ApiTestFile record);

    List<ApiTestFile> selectByExample(ApiTestFileExample example);

    int updateByExampleSelective(@Param("record") ApiTestFile record, @Param("example") ApiTestFileExample example);

    int updateByExample(@Param("record") ApiTestFile record, @Param("example") ApiTestFileExample example);
}