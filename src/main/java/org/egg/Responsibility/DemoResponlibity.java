package org.egg.Responsibility;

import java.util.Vector;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 21:27
 */
public class DemoResponlibity {
    private  Vector<Responsibility> vectors;

    public DemoResponlibity() {
        vectors = new Vector<>();
    }

    synchronized  void addHandler(Responsibility responsibilitie) {
        vectors.add(responsibilitie);
    }
//XXX 每一次执行，都重新构建了责任链
    void execute(CommonContext commonContext) {
        for (int i = 0; i < vectors.size(); i++) {
            if (i==vectors.size()-1) {
                break;
            }
            vectors.get(i).setNextHandler(vectors.get(i+1));
        }
        System.out.println("====构建责任链完成===");
        vectors.get(0).handleExe(commonContext);
    }
}
