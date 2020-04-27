package Lesson7.Lesson7_2;
//1 Написать программу для проверки ДЗ
//(Проанализировать папку с компилированными классами и вызвать методы, проверить результат)

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public class Lesson7_2 {

    public static void findClass() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        File file = new File("D:\\Class");
        String[] fileList = file.list();
        Class<?> classToAnalyze;
//просматриваем заранее заданную папку на предмет файлов с расширением типа class
        if (fileList!=null) {
            for (String str : fileList) {
                out.println(str);
                String[] array = str.split("\\.");
                if ( !array[1].equalsIgnoreCase("class") ) {
                    throw new RuntimeException(str, new Exception());
                }
//загружаем в память найденный класс
                classToAnalyze = URLClassLoader.newInstance(new URL[]
                        {file.toURI().toURL()}).
                        loadClass(array[0]);
                getMethods(classToAnalyze);
            }
        }
    }

    private static void getMethods(Class<?> classToAnalyze) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//создаем список методов этого класса
//в данном случае нас интересуют методы, возвращающие значение (опционально)
        Method[] methods = classToAnalyze.getDeclaredMethods();
        List<Method> arrayOfMethods = new ArrayList<>();
        out.println("Список методов, возвращающих значение:");
        for (int i = 0; i < methods.length; i++) {
            if ( !methods[i].getReturnType().equals(Void.TYPE) ) {
                out.println(methods[i].getName()
                        + " " + methods[i].getReturnType()
                        + " " + Arrays.toString(methods[i].getParameterTypes()));
                arrayOfMethods.add(methods[i]);
            }
        }
        out.println("==============================");
        checkMethods(arrayOfMethods, classToAnalyze);
    }

    private static void checkMethods(List<Method> methods, Class<?> classToAnalyze) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        out.println("Проверка методов");
//создаем конструктор класса, он будет использоваться в методе invoke ниже
        Constructor<?> constructor = classToAnalyze.getConstructor();
        Object object = constructor.newInstance();
//создаем список проверяемых параметров для метода invoke,
//они все будут разного типа, поэтому обозначаем все как Object
        ArrayList<Object> parameters = new ArrayList<>();
//проверяем на наличие маркеров-переменных, в данном случае int и float
//создаем массив аргументов для getDeclaredMethod
        for (int i = 0; i < methods.size(); i++) {
            Class<?>[] classArguments = new Class[methods.get(i).getParameterTypes().length];
            for (int j = 0; j < methods.get(i).getParameterTypes().length; j++) {
                if ( methods.get(i).getParameterTypes()[j].toString().equals("int") ) {
                    parameters.add((int) (Math.random() * 2020));
                    classArguments[j] = Integer.TYPE;
                } else if ( methods.get(i).getParameterTypes()[j].toString().equals("float") ) {
                    parameters.add((float) (Math.random() * 2020));
                    classArguments[j] = Float.TYPE;
                }
            }

            classToAnalyze.getDeclaredMethod(methods.get(i).getName(), classArguments);
            methods.get(i).setAccessible(true);
//            if ( methods.get(i).getModifiers() == Modifier.PRIVATE ) {
//                methods.get(i).setAccessible(true);
//            }
            out.printf("%s = %s при значении переменной: %s%n",
                    methods.get(i).getName(),
                    methods.get(i).invoke(object, parameters.toArray()),
                    parameters);
            parameters.clear();
//очищаем каждый раз список параметров, чтобы использовать в следующем методе
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        findClass();
    }
}
