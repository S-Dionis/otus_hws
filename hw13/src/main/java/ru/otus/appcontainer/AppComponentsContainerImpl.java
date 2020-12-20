package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        sortClassByOrderAndProcess(initialConfigClass);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        Class<?>[] classes = new Class[typesAnnotatedWith.size()];

        int i = 0;
        for (Class<?> aClass : typesAnnotatedWith) {
            classes[i++] = aClass;
        }
        sortClassByOrderAndProcess(classes);
    }

    private void sortClassByOrderAndProcess(Class<?>[] classes) {
        Arrays.stream(classes)
                .filter(clazz -> clazz.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Method[] methods = configClass.getMethods();
        Object classInstance = getClassInstance(configClass);

        List<Method> sortedMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        for (Method method : sortedMethods) {
            Object[] parameters = new Object[method.getParameterCount()];
            Class<?>[] parameterTypes = method.getParameterTypes();

            int i = 0;
            for (Class<?> parameterType : parameterTypes) {
                Optional<Object> first = appComponents.stream()
                        .filter(appComponent -> parameterType.isAssignableFrom(appComponent.getClass()))
                        .findFirst();

                parameters[i++] = first.orElseThrow();
            }

            try {
                Object methodResult = method.invoke(classInstance, parameters);
                appComponents.add(methodResult);
                appComponentsByName.put(method.getName(), methodResult);
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }

    }

    private Object getClassInstance(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("can't find default constructor for class: " + configClass.getName() + ex.getMessage());
        }

    }

    private static int methodCompare(Method o1, Method o2) {
        int order1 = o1.getAnnotation(AppComponent.class).order();
        int order2 = o2.getAnnotation(AppComponent.class).order();

        return Integer.compare(order1, order2);
    }

    private static int classCompare(Class<?> o1, Class<?> o2) {
        int order1 = o1.getAnnotation(AppComponentsContainerConfig.class).order();
        int order2 = o2.getAnnotation(AppComponentsContainerConfig.class).order();

        return Integer.compare(order1, order2);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
                .findFirst()
                .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
