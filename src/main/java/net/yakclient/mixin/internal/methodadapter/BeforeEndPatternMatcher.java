package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.MethodInstructionForwarder;
import org.objectweb.asm.*;

import java.util.PriorityQueue;
import java.util.Queue;

public class BeforeEndPatternMatcher extends MethodInjectionPatternMatcher {
    private static final int FOUND_RETURN = 1;
    private static final int FOUND_LAST_RETURN = 2;

    private int returnType;

    public BeforeEndPatternMatcher(MethodVisitor visitor, Queue<BytecodeMethodModifier.PriorityInstruction> instructions, PatternFlag<?>... flags) {
        super(visitor, instructions, flags);
        this.returnType = Opcodes.RETURN;
    }

    @Override
    public void visitInsn(int opcode) {
        if (isReturn(opcode)) {
            this.state = FOUND_RETURN;
            this.returnType = opcode;
            return;
        }
        super.visitInsn(opcode);
    }

//    //Visit code
//    //Visit mixin
//    //Visit return
//    //Visit Maxs
//    //Visit End
//

    //Label: L1190654826
    //Line: 23 Label: L1190654826
    //ILOAD VAR: 1
    //ACC_FINAL OPERAND: 10
    //Jump: IF_ICMPGE Label: L636718812

    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: THis
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V

    //Label: L636718812
    //Line: 24 Label: L636718812
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: Other this
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V

    //Label: L445051633
    //Line: 30 Label: L445051633
    //Label: L1051754451



    //Label: L1190654826
    //Line: 23 Label: L1190654826
    //ILOAD VAR: 1
    //ACC_FINAL OPERAND: 10
    //Jump: IF_ICMPGE Label: L636718812

    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: THis
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V

    //Label: L636718812
    //Line: 24 Label: L636718812
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: Other this
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V

    //Label: L1349277854
    //Line: 30 Label: L1349277854
    //Label: L1775282465


    //Label: L584634336
    //Line: 23 Label: L584634336
    //ILOAD VAR: 1
    //ACC_FINAL OPERAND: 10
    //Jump: IF_ICMPGE Label: L1099983479
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: THis
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V
    //Label: L1099983479
    //Line: 24 Label: L1099983479
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: Other this
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V
    //Label: L1268447657
    //Line: 30 Label: L1268447657
    //Label: L1401420256



    //BREAK -------------------------------------



    //Label: L1851691492
    //Line: 23 Label: L1851691492
    //ILOAD VAR: 1
    //ACC_FINAL OPERAND: 10
    //Jump: IF_ICMPGE Label: L752848266
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: THis
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V
    //Label: L752848266
    //Line: 24 Label: L752848266
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream;
    //LDC: Other this
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V
    //Label: L815033865
    //Line: 30 Label: L815033865
    //Label: L1555093762


    @Override
    public void visitEnd() {
        //Pointless but is nice for cleanup
        if (this.state == FOUND_RETURN)
            this.state = FOUND_LAST_RETURN;

        final Queue<BytecodeMethodModifier.PriorityInstruction> instructions = this.instructions;
        this.executeInsn(instructions);

//
//        InstructionInterceptor interceptor = new InstructionInterceptor(null);

//        MethodInstructionForwarder forwarder = new MethodInstructionForwarder(null, );

        //"net.questcraft.apitests.MixinTestCase", "net.questcraft.apitests.MixinSourceClassTest"

        Instruction.InstructionBuilder builder = new Instruction.InstructionBuilder();



//        final Label l1 = new Label();
//        final Label l2 = new Label();
//        final Label l3 = new Label();
//        final Label l4 = new Label();
//
//        builder.addInstruction(mv -> mv.visitLabel(l1));
//        builder.addInstruction(mv -> mv.visitLineNumber(23, l1));
//        builder.addInstruction(mv -> mv.visitVarInsn(Opcodes.ILOAD, 1));
//        final int operand = 10;
//        builder.addInstruction(mv -> mv.visitIntInsn(Opcodes.BIPUSH, operand));
//        builder.addInstruction(mv -> mv.visitJumpInsn(Opcodes.IF_ICMPGE, l2));
//
//        builder.addInstruction(mv -> mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
//        builder.addInstruction(mv -> mv.visitLdcInsn("THis"));
//        builder.addInstruction(mv -> mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
//
//        builder.addInstruction(mv -> mv.visitLabel(l2));
//        builder.addInstruction(mv -> mv.visitLineNumber(24, l2));
//
//        builder.addInstruction(mv -> mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
//        builder.addInstruction(mv -> mv.visitLdcInsn("Other this"));
//        builder.addInstruction(mv -> mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
//
//        builder.addInstruction(mv -> mv.visitLabel(l3));
//        builder.addInstruction(mv -> mv.visitLineNumber(30, l3));
//        builder.addInstruction(mv -> mv.visitLabel(l4));
//
////        builder.build().execute(this.mv);
//        final PriorityQueue<BytecodeMethodModifier.PriorityInstruction> queue = new PriorityQueue<>();
//        final BytecodeMethodModifier.PriorityInstruction disdfsdf = new BytecodeMethodModifier.PriorityInstruction(5, builder.build());
//        queue.add(disdfsdf);
//        System.out.println("whatever");
//
//        this.executeInsn(queue);

//        InstructionInterceptor interceptor = new InstructionInterceptor(null);
//        InstructionInterceptor interceptor2 = new InstructionInterceptor(null);

//        builder.build().execute(interceptor);
//        this.instructions.peek().getInsn().execute(interceptor2);
//
//        this.instructions.
//
//        interceptor.print();
//        System.out.println("BREAK -------------------------------------");
//        interceptor2.print();


        //        final Label L1051754451 = new Label();
//        final Label L1349277854 = new Label();
//        final Label L2040495657 = new Label();
//        final Label L1267032364 = new Label();
//
//        this.mv.visitLabel(L1051754451);
//        this.mv.visitLineNumber(23, L1051754451);
//        this.mv.visitVarInsn(Opcodes.ILOAD, 1);
//        this.mv.visitIntInsn(Opcodes.ACC_FINAL, 10);
//        this.mv.visitJumpInsn(Opcodes.IF_ICMPGE, L1349277854);
//
//        this.mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        this.mv.visitLdcInsn("THis");
//        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        this.mv.visitLabel(L1349277854);
//        this.mv.visitLineNumber(24, L1349277854);
//
//        this.mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        this.mv.visitLdcInsn("Other this");
//        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        this.mv.visitLabel(L2040495657);
//        this.mv.visitLineNumber(30, L2040495657);
//        this.mv.visitLabel(L1267032364);

//        Label l1 = new Label();
//        Label l2 = new Label();
//        Label l3 = new Label();
//        Label l4 = new Label();
//        Label l5 = new Label();
//        Label l6 = new Label();
//        Label l7 = new Label();
//
////        this.mv.visitLabel(l1);
////        this.mv.visitLineNumber(16, l1);
////        this.mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
////        this.mv.visitLdcInsn("ok");
////        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
////        this.mv.visitLabel(l2);
////        this.mv.visitLineNumber(21, l2);
////        this.mv.visitLabel(l3);
//        this.mv.visitLabel(l4);
//        this.mv.visitLineNumber(23, l4);
//        this.mv.visitVarInsn(Opcodes.ILOAD, 1);
//        this.mv.visitIntInsn(Opcodes.ACC_FINAL, 10);
//        this.mv.visitJumpInsn(Opcodes.IF_ICMPGE, l5);
//        this.mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        this.mv.visitLdcInsn("This");
//        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//        this.mv.visitLabel(l5);
//        this.mv.visitLineNumber(24, l5);
//
//        this.mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        this.mv.visitLdcInsn("Other THis");
//        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        this.mv.visitLabel(l6);
//        this.mv.visitLineNumber(30, l6);
//        this.mv.visitLabel(l7);
//        this.mv.visitInsn(Opcodes.RETURN);
//        this.mv.
//        this.mv.


//        Label before = new Label();
//        Label after = new Label();
//
//        mv.visitLabel(before);
//        mv.visitVarInsn(Opcodes.ILOAD, 1);
//        mv.visitVarInsn(Opcodes.BIPUSH, 10);
//        mv.visitJumpInsn(Opcodes.IF_ICMPGT, after);
//
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("This is where we do print stuff");
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        mv.visitLabel(after);
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("If This gets called and not the first then we know it worked");
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//

        super.visitInsn(this.returnType);

        super.visitMaxs(0, 0); /* Should be calculated by the ClassWriter, otherwise it will throw a verify error */
        super.visitEnd();
    }

    @Override
    public void visitInsn() {
        if (state == FOUND_RETURN) {
            state = NOT_MATCHED;
            this.visitInsn(this.returnType);
        }
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) { /* Unused because we have no need to provide debugging tools */ }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return null; /* We dont need annotations to none-existent references */
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) { /* Should be calculated automatically so again we have no need to provide it */ }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) { /* We dont want Max's to be called before we inject */ }

    //Label - "First label"
    //Line: 16 - "Mark as line 16"
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream; - "Starting of basic print statement, getting out and adding it to the operand stack"
    //LDC: AAAY - "Adding our object onto the operand stack"
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V - "Invoking which our operand stack now goes back down to 0"
    //Label - "New label, Operand stack should be 0 at this point"
    //Line: 21 - "Marking as line 21"
    //Label - "Creating another label, ~useless~"
    //Label - "Creating another label ~useless~"
    //Line: 24 - "Marking line ~useless~"
    //ILOAD VAR: 1 - "Loading the first param which will be a int"
    //ACC_FINAL OPERAND: 16 - "This would signify marking something as final, I dont see why that would be used especially with the given operand stack position"
    //Jump: IF_ICMPGE - "Doing the comparison, dont get were the 10 is coming from though"
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream; - "Start of the print statement, next two lines are pretty obvious"
    //LDC: This
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V
    //Label - "Visiting label"
    //Line: 25 - "Line 25"
    //GETSTATIC owner: java/lang/System name: out descriptor: Ljava/io/PrintStream; - "Again, easy print statement"
    //LDC: Other this
    //INVOKEVIRTUAL owner: java/io/PrintStream name: println descriptor: (Ljava/lang/String;)V
    //Label - "Label"
    //Line: 29 - "Label"
    //Label - "Label"
    //RETURN - "Return"
    //Stack: 0 Locals: 0

    //Thoughts: Alot of pointless labels, I dont think this should affect anything too much(at all) However we are getting a constant pool complain so it is most likely the constants from printing OR
}
