package Lesson7.Lesson7_1;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface AfterSuite {
}
