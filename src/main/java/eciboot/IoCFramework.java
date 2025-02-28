package eciboot;

import eciboot.annotation.GetMapping;
import eciboot.annotation.RequestParam;
import eciboot.annotation.RestController;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;

public class IoCFramework {
    private static final Map<String, Method> routes = new HashMap<>();
    private static final String packagePath = "eciboot.controller";

    public static void loadControllers() throws ClassNotFoundException {
        List<Class<?>> classes = getClasses();
        for (Class<?> classIteration : classes) {
            if (classIteration.isAnnotationPresent(RestController.class)) {
                String className = classIteration.getName();
                Class<?> classProv = Class.forName(className);
                Method[] methods = classProv.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        routes.put(method.getAnnotation(GetMapping.class).value(), method);
                    }
                }
            }
        }
    }

    private static List<Class<?>> getClasses() {
        List<Class<?>> classes = new ArrayList<>();
        String path = packagePath.replace(".", "/");
        try {
            for (String classPath : getClassPaths()) {
                File file = new File(classPath + "/" + path);
                getFilesClasses(file, classes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void getFilesClasses(File file, List<Class<?>> classes) throws ClassNotFoundException {
        if (file.exists() && file.isDirectory()) {
            for (File classFile : Objects.requireNonNull(file.listFiles())) {
                if (classFile.isFile() && classFile.getName().endsWith(".class")) {
                    String className = packagePath + "." + classFile.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        }
    }

    private static List<String> getClassPaths() {
        String classPath = System.getProperty("java.class.path");
        String[] classPaths = classPath.split(System.getProperty("path.separator"));
        return new ArrayList<>(Arrays.asList(classPaths));
    }

    public static String handleRequest(String path) {
        String query = "";
        String[] route = path.split("\\?");
        String dom = route[0];

        if (route.length >= 2) {
            query = route[1];
        }

        if (routes.containsKey(dom)) {
            try {
                Method method = routes.get(dom);
                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];
                Map<String, String> paramMap = parseQuery(query);

                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                        RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                        args[i] = paramMap.getOrDefault(requestParam.value(), requestParam.defaultValue());
                    }
                }

                Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                return "HTTP/1.1 200 Ok\r\n\r\n" + method.invoke(instance, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "HTTP/1.1 404 Not Found\r\n\r\nPágina no encontrada";
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> paramMap = new HashMap<>();
        for (String param : query.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                paramMap.put(keyValue[0], keyValue[1]);
            }
        }
        return paramMap;
    }

    public static Map<String, Method> getRoutes() {
        return routes;
    }
}
