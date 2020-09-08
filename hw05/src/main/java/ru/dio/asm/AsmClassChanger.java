package ru.dio.asm;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class AsmClassChanger {

    public static byte[] addLogging(String className) {
        ClassReader classReader = null;
        try {
            classReader = new ClassReader(className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitorImpl(classWriter);
        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

    private static class ClassVisitorImpl extends ClassVisitor {

        public ClassVisitorImpl(ClassWriter classWriter) {
            super(Opcodes.ASM8, classWriter);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            var mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            return new MethodVisitorImpl(mv, name, descriptor, access == Opcodes.ACC_STATIC);
        }

    }

    private static class MethodVisitorImpl extends MethodVisitor {

        private final String name;
        private final String descriptor;
        private final boolean isStatic;

        public MethodVisitorImpl(MethodVisitor methodVisitor, String name, String descriptor, boolean isStatic) {
            super(Opcodes.ASM8, methodVisitor);
            this.name = name;
            this.descriptor = descriptor.substring(descriptor.indexOf('('), descriptor.indexOf(')') + 1);
            this.isStatic = isStatic;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String annotationDescriptor, boolean visible) {
            final String s = "Lru/dio/asm/Log;";
            if (annotationDescriptor.equals(s) && visible) {
                //handle only methods with primitive type parameters
                if (!descriptor.contains("L") && !descriptor.contains("[")) {
                    handle();
                }
            }
            return super.visitAnnotation(annotationDescriptor, visible);
        }

        private void handle() {
            StringBuilder methodArgs = new StringBuilder();
            char[] split = getParameterTypes();
            int localVariablePos = isStatic ? 0 : 1;

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            if (split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    final char c = split[i];
                    switch (c) {
                        case 'Z', 'C', 'B', 'S', 'I' -> mv.visitVarInsn(Opcodes.ILOAD, localVariablePos);
                        case 'F' -> mv.visitVarInsn(Opcodes.FLOAD, localVariablePos);
                        case 'J' -> {
                            mv.visitVarInsn(Opcodes.LLOAD, localVariablePos);
                            localVariablePos++;
                        }
                        case 'D' -> {
                            mv.visitVarInsn(Opcodes.DLOAD, localVariablePos);
                            localVariablePos++;
                        }
                    }
                    localVariablePos++;

                    methodArgs.append("Param").append(i).append(": \u0001; ");
                }

                Handle handle = new Handle(
                        H_INVOKESTATIC,
                        Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                        "makeConcatWithConstants",
                        MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                        false);

                mv.visitInvokeDynamicInsn("makeConcatWithConstants", descriptor + "Ljava/lang/String;", handle, methodArgs.toString());
            } else {
                mv.visitLdcInsn("No param");
            }

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        private char[] getParameterTypes() {
            StringBuilder sb = new StringBuilder(descriptor);
            String params = sb.deleteCharAt(sb.indexOf(")"))
                    .deleteCharAt(sb.indexOf("("))
                    .toString();
            return params.toCharArray();
        }

    }

}
