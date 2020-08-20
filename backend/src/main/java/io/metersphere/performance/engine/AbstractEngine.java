package io.metersphere.performance.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.metersphere.base.domain.LoadTestWithBLOBs;
import io.metersphere.base.domain.TestResource;
import io.metersphere.base.domain.TestResourcePool;
import io.metersphere.commons.constants.PerformanceTestStatus;
import io.metersphere.commons.constants.ResourcePoolTypeEnum;
import io.metersphere.commons.exception.MSException;
import io.metersphere.commons.utils.CommonBeanFactory;
import io.metersphere.config.JmeterProperties;
import io.metersphere.performance.service.PerformanceTestService;
import io.metersphere.service.TestResourcePoolService;
import io.metersphere.service.TestResourceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public abstract class AbstractEngine implements Engine {
    protected String JMETER_IMAGE;
    private Long startTime;
    private String reportId;
    protected LoadTestWithBLOBs loadTest;
    protected PerformanceTestService performanceTestService;
    protected Integer threadNum;
    protected List<TestResource> resourceList;

    private TestResourcePoolService testResourcePoolService;
    private TestResourceService testResourceService;

    public AbstractEngine() {
        testResourcePoolService = CommonBeanFactory.getBean(TestResourcePoolService.class);
        testResourceService = CommonBeanFactory.getBean(TestResourceService.class);
        JMETER_IMAGE = CommonBeanFactory.getBean(JmeterProperties.class).getImage();
        this.startTime = System.currentTimeMillis();
        this.reportId = UUID.randomUUID().toString();
    }

    protected void init(LoadTestWithBLOBs loadTest) {
        if (loadTest == null) {
            MSException.throwException("LoadTest is null.");
        }
        this.loadTest = loadTest;

        this.performanceTestService = CommonBeanFactory.getBean(PerformanceTestService.class);

        threadNum = getThreadNum(loadTest);
        String resourcePoolId = loadTest.getTestResourcePoolId();
        if (StringUtils.isBlank(resourcePoolId)) {
            MSException.throwException("Resource Pool ID is empty");
        }
        TestResourcePool resourcePool = testResourcePoolService.getResourcePool(resourcePoolId);
        if (resourcePool == null) {
            MSException.throwException("Resource Pool is empty");
        }
        if (!ResourcePoolTypeEnum.NODE.name().equals(resourcePool.getType())) {
            MSException.throwException("Invalid Resource Pool type.");
        }
        this.resourceList = testResourceService.getResourcesByPoolId(resourcePool.getId());
        if (CollectionUtils.isEmpty(this.resourceList)) {
            MSException.throwException("Test Resource is empty");
        }
    }

    protected Integer getRunningThreadNum() {
        List<LoadTestWithBLOBs> loadTests = performanceTestService.selectByTestResourcePoolId(loadTest.getTestResourcePoolId());
        // 使用当前资源池正在运行的测试占用的并发数
        return loadTests.stream()
                .filter(t -> PerformanceTestStatus.Running.name().equals(t.getStatus()))
                .map(this::getThreadNum)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private Integer getThreadNum(LoadTestWithBLOBs t) {
        Integer s = 0;
        String loadConfiguration = t.getLoadConfiguration();
        JSONArray jsonArray = JSON.parseArray(loadConfiguration);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            if (StringUtils.equals(o.getString("key"), "TargetLevel")) {
                s = o.getInteger("value");
            }
        }
        return s;
    }

    @Override
    public Long getStartTime() {
        return startTime;
    }

    @Override
    public String getReportId() {
        return reportId;
    }
}
