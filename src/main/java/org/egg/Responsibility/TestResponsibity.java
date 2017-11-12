package org.egg.Responsibility;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 21:41
 */
public class TestResponsibity {
   public static void test() {
        TestRequest testRequest = new TestRequest();
        TestResult testResult = new TestResult();
        TestContext testContext = new TestContext();
        testContext.setTestRequest(testRequest);
        testContext.setTestResult(testResult);
        DemoResponlibity demoResponlibity = new DemoResponlibity();
        DemoResponsibilityHandler demoResponsibilityHandler = new DemoResponsibilityHandler();
        Demo2ResponsibilityHandler demo2ResponsibilityHandler = new Demo2ResponsibilityHandler();
        demoResponlibity.addHandler(demoResponsibilityHandler);
        demoResponlibity.addHandler(demo2ResponsibilityHandler);
        demoResponlibity.execute(testContext);
    }

}
