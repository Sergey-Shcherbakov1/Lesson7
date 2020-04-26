package Lesson7.Lesson7_1;

import java.lang.reflect.*;

public class TestsChecker {

    public static void start(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        try {
            int count1 = 0;
            for (Method o : methods) {

                if (o.getAnnotation(BeforeSuite.class) != null) {
                    if (count1 == 1) throw new RuntimeException();
                    System.out.println(o.getName());
                    count1++;
                }
            }
            for (int i = 1; i < methods.length; i++) {
                for (Method o : methods) {
                    if (o.getAnnotation(Test.class) != null) {
                        Test test = o.getAnnotation(Test.class);
                        if (test.priority() == i) {
                            System.out.printf("метод %s priority: %d", o.getName(),test.priority());
                            System.out.println();
                        }
                    }
                }
            }
            int count2 = 0;
            for (Method o : methods) {
                if (o.getAnnotation(AfterSuite.class) != null) {
                    if (count2 == 1) throw new RuntimeException();
                    System.out.println(o.getName());
                    count2++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
