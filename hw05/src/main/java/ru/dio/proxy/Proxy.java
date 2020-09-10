package ru.dio.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Proxy {

    private Proxy() {}

    static Calculation getCalculation() {
        final InvocationHandlerImpl handler = new InvocationHandlerImpl(new CalculationImpl());
        return (Calculation) java.lang.reflect.Proxy.newProxyInstance(Proxy.class.getClassLoader(), new Class<?>[]{Calculation.class}, handler);
    }

    static class InvocationHandlerImpl implements InvocationHandler {

        private final Calculation calculation;

        InvocationHandlerImpl(Calculation calculation) {
            this.calculation = calculation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (isAnnotatedWithLog(method)) {
                if (args == null) {
                    System.out.println("No parameters");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < args.length; i++) {
                        sb.append("param").append(i).append(": ").append(args[i]).append("; ");
                    }
                    System.out.println(sb);
                }
            }
            return method.invoke(calculation, args);
        }

        private boolean isAnnotatedWithLog(Method method) throws NoSuchMethodException {
            final Method method1 = calculation.getClass().getMethod(method.getName(), method.getParameterTypes());
            return method1.getAnnotation(Log.class) != null;
        }
    }
}
