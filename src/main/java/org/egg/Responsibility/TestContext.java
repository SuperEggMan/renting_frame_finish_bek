package org.egg.Responsibility;

import java.io.Serializable;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 21:07*/


public class TestContext extends CommonContext implements Serializable{
    private static final long serialVersionUID = 6774411689205239053L;
    private TestRequest testRequest;

    private TestResult testResult;

    public void setTestRequest(TestRequest testRequest) {
        this.testRequest = testRequest;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    @Override
    public Object getRequest() {
        return testRequest;
    }

    @Override
    public Object getResponse() {
        return testResult;
    }
}
