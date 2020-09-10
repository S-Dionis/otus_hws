package ru.dio.asm;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.security.ProtectionDomain;

public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader,
                                    String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("ru/dio/asm/TestClass")) {
                        return AsmClassChanger.addLogging(className);
                }
                return classfileBuffer;
            }
        });
    }

    /*tests only*/
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        class MyClassLoader extends ClassLoader {

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                final byte[] bytes = AsmClassChanger.addLogging(name);
                try (OutputStream fw = new FileOutputStream(new File("Test.class"))) {
                    fw.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return defineClass(name, bytes, 0, bytes.length);
            }
        }

        final MyClassLoader myClassLoader = new MyClassLoader();
        myClassLoader.findClass("ru.dio.asm.TestClass");
    }

}
