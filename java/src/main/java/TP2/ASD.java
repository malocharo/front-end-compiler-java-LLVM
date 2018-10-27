package TP2;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ASD {
  static public class Program {
    Block e; // What a program contains. TODO : change when you extend the language

    public Program(Block e) {
      this.e = e;
    }


    // Pretty-printer
    public String pp() {

        return e.pp();

    }

    // IR generation
    public Llvm.IR toIR() throws TypeException {
        // TODO : change when you extend the language

        // computes the IR of the expression
        //Expression.RetExpression retExpr = e.toIR();
        // add a return instruction
        //Llvm.Instruction ret = new Llvm.Return(retExpr.type.toLlvmType(), retExpr.result);
        //retExpr.ir.appendCode(ret);

        //return retExpr.ir;
        return e.toIr().ir;

    }
  }
  static public abstract class Declaration {
    public abstract String pp();
    public abstract RetDeclaration toIR() throws TypeException;

    static public class RetDeclaration {
      public Llvm.IR ir;
      public RetDeclaration(Llvm.IR ir) {
        this.ir = ir;
      }
    }
  }
  static public class VarDecla extends Declaration {
    public String id;
    public Type type;

    public VarDecla(Type t, String i) {
      this.type = t;
      this.id = i;
    }

    public String pp() {
      return this.id;
    }

    public RetDeclaration toIR() throws TypeException {
      Llvm.IR irVarDecla = new Llvm.IR(Llvm.empty(),Llvm.empty());
      Llvm.Instruction isVarDecla = new Llvm.VarDeclaration(type.toLlvmType(), id);
      irVarDecla.appendCode(isVarDecla);
      return new RetDeclaration(irVarDecla);
    }
  }

  // All toIR methods returns the IR, plus extra information (synthesized attributes)
  // They can take extra arguments (inherited attributes)

  static public abstract class Expression {
    public abstract String pp();
    public abstract RetExpression toIR() throws TypeException;

    // Object returned by toIR on expressions, with IR + synthesized attributes
    static public class RetExpression {
      // The LLVM IR:
      public Llvm.IR ir;
      // And additional stuff:
      public Type type; // The type of the expression
      public String result; // The name containing the expression's result
      // (either an identifier, or an immediate value)

      public RetExpression(Llvm.IR ir, Type type, String result) {
        this.ir = ir;
        this.type = type;
        this.result = result;
      }
    }
  }
  static public class MulExpression extends Expression {
    Expression left;
    Expression right;

    public MulExpression(Expression left, Expression right) {
      this.left = left;
      this.right = right;
    }

    // Pretty-printer
    public String pp() {
      return "(" + left.pp() + " * " + right.pp() + ")";
    }

    // IR generation
    public RetExpression toIR() throws TypeException {
      RetExpression leftRet = left.toIR();
      RetExpression rightRet = right.toIR();

      // We check if the types mismatches
      if(!leftRet.type.equals(rightRet.type)) {
        throw new TypeException("type mismatch: have " + leftRet.type + " and " + rightRet.type);
      }

      // We base our build on the left generated IR:
      // append right code
      leftRet.ir.append(rightRet.ir);

      // allocate a new identifier for the result
      String result = Utils.newtmp();

      // new add instruction result = left + right
      Llvm.Instruction mul = new Llvm.Mul(leftRet.type.toLlvmType(), leftRet.result, rightRet.result, result);

      // append this instruction
      leftRet.ir.appendCode(mul);

      // return the generated IR, plus the type of this expression
      // and where to find its result
      return new RetExpression(leftRet.ir, leftRet.type, result);
    }
  }

  static public class DivExpression extends Expression {
    Expression left;
    Expression right;

    public DivExpression(Expression left, Expression right) {
      this.left = left;
      this.right = right;
    }

    // Pretty-printer
    public String pp() {
      return "(" + left.pp() + " * " + right.pp() + ")";
    }

    // IR generation
    public RetExpression toIR() throws TypeException {
      RetExpression leftRet = left.toIR();
      RetExpression rightRet = right.toIR();

      // We check if the types mismatches
      if(!leftRet.type.equals(rightRet.type)) {
        throw new TypeException("type mismatch: have " + leftRet.type + " and " + rightRet.type);
      }

      // We base our build on the left generated IR:
      // append right code
      leftRet.ir.append(rightRet.ir);

      // allocate a new identifier for the result
      String result = Utils.newtmp();

      // new add instruction result = left + right
      Llvm.Instruction div = new Llvm.Div(leftRet.type.toLlvmType(), leftRet.result, rightRet.result, result);

      // append this instruction
      leftRet.ir.appendCode(div);

      // return the generated IR, plus the type of this expression
      // and where to find its result
      return new RetExpression(leftRet.ir, leftRet.type, result);
    }
  }

  // Concrete class for Expression: add case
  static public class AddExpression extends Expression {
    Expression left;
    Expression right;

    public AddExpression(Expression left, Expression right) {
      this.left = left;
      this.right = right;
    }

    // Pretty-printer
    public String pp() {
      return "(" + left.pp() + " + " + right.pp() + ")";
    }

    // IR generation
    public RetExpression toIR() throws TypeException {
      RetExpression leftRet = left.toIR();
      RetExpression rightRet = right.toIR();

      // We check if the types mismatches
      if(!leftRet.type.equals(rightRet.type)) {
        throw new TypeException("type mismatch: have " + leftRet.type + " and " + rightRet.type);
      }

      // We base our build on the left generated IR:
      // append right code
      leftRet.ir.append(rightRet.ir);

      // allocate a new identifier for the result
      String result = Utils.newtmp();

      // new add instruction result = left + right
      Llvm.Instruction add = new Llvm.Add(leftRet.type.toLlvmType(), leftRet.result, rightRet.result, result);

      // append this instruction
      leftRet.ir.appendCode(add);

      // return the generated IR, plus the type of this expression
      // and where to find its result
      return new RetExpression(leftRet.ir, leftRet.type, result);
    }
  }

  static public class SubExpression extends Expression {
    Expression left;
    Expression right;

    public SubExpression(Expression l, Expression r) {
      this.left = l;
      this.right = r;
    }
    public String pp() {
      return "(" + this.left + " - " + this.right + ")";
    }

    // IR generation
    public RetExpression toIR() throws TypeException {
      RetExpression leftRet = left.toIR();
      RetExpression rightRet = right.toIR();

      // We check if the types mismatches
      if(!leftRet.type.equals(rightRet.type)) {
        throw new TypeException("type mismatch: have " + leftRet.type + " and " + rightRet.type);
      }

      // We base our build on the left generated IR:
      // append right code
      leftRet.ir.append(rightRet.ir);

      // allocate a new identifier for the result
      String result = Utils.newtmp();

      // new add instruction result = left + right
      Llvm.Instruction sub = new Llvm.Sub(leftRet.type.toLlvmType(), leftRet.result, rightRet.result, result);

      // append this instruction
      leftRet.ir.appendCode(sub);

      // return the generated IR, plus the type of this expression
      // and where to find its result
      return new RetExpression(leftRet.ir, leftRet.type, result);
    }

  }

  // Concrete class for Expression: constant (integer) case
  static public class IntegerExpression extends Expression {
    int value;
    public IntegerExpression(int value) {
      this.value = value;
    }

    public String pp() {
      return "" + value;
    }

    public RetExpression toIR() {
      // Here we simply return an empty IR
      // the `result' of this expression is the integer itself (as string)
      return new RetExpression(new Llvm.IR(Llvm.empty(), Llvm.empty()), new Int(), "" + value);
    }
  }

  static public class IdExpression extends Expression {
    Type type;
    String id;

    public IdExpression(Type t, String s) {
      this.type = t;
      this.id = s;
    }

    public String pp() {
      return this.id;
    }

    @Override
    public RetExpression toIR() {
      Llvm.IR irId = new Llvm.IR(Llvm.empty(), Llvm.empty());
      String tmp = Utils.newtmp();
      Llvm.Instruction idExpr = new Llvm.Load(type.toLlvmType(), id, tmp);
      irId.appendCode(idExpr);
      return new RetExpression(irId,type,tmp);

    }
  }

  static public class Condition extends Expression {
    Expression expr;
    boolean b;
    public Condition(Expression e, boolean b) {
      this.b = b;
      this.expr = e;
    }

    @java.lang.Override
    public String pp() {
      if(b)
        return this.expr.pp();
      return "NOT "+this.expr.pp();

    }

    @java.lang.Override
    public TP2.ASD.Expression.RetExpression toIR() throws TypeException {
      Expression.RetExpression retCond = this.expr.toIR();
      String tmp = Utils.newtmp();
      Llvm.Instruction instCond = new Llvm.Cond(retCond.type.toLlvmType(),retCond.result,b,tmp);
      retCond.ir.appendCode(instCond);
      return new RetExpression(retCond.ir,retCond.type,retCond.result);
    }
  }
  static public abstract class Instruction {
    public abstract String pp();
    public abstract RetInstruction toIr() throws TypeException;

    static public class RetInstruction {
      public Llvm.IR ir;

      public Type type;
      public String res;

      public RetInstruction(Llvm.IR ir, Type type, String res) {
        this.ir = ir;
        this.type = type;
        this.res = res;
      }
    }
  }


  static public class AffInstruction extends Instruction {
    Type type;
    String id;
    Expression expr;

    public AffInstruction(Expression expr, String id) {
      this.expr = expr;
      this.id = id;
    }

    @java.lang.Override
    public String pp() {
      return id+" := " +expr.pp();
    }

    public RetInstruction toIr() throws TypeException {
      Expression.RetExpression exprNew = this.expr.toIR();
      Llvm.Instruction aff = new Llvm.Aff(exprNew.type.toLlvmType(),exprNew.result,id);
      exprNew.ir.appendCode(aff);
      return new RetInstruction(exprNew.ir,exprNew.type,id);
    }
  }
  static public class Ret extends Instruction {
    Expression expr;
    Type type;

    public Ret( Type t, Expression e) {
      this.expr = e;
      this.type = t;
    }

    @java.lang.Override
    public String pp() {
      return "RETURN "+type.pp()+" "+expr.pp();
    }

    @java.lang.Override
    public RetInstruction toIr() throws TypeException {
      Expression.RetExpression newExpr = this.expr.toIR();
      Llvm.Return retu = new Llvm.Return(this.type.toLlvmType(),newExpr.result);
      newExpr.ir.appendCode(retu);
      return new RetInstruction(newExpr.ir,this.type,newExpr.result);

    }
  }

 static public class Block extends Instruction {
    List<VarDecla> varList;
    List<Instruction> instList;

    public Block(List<VarDecla> v, List<Instruction> i) {
      this.varList = v;
      this.instList = i;
    }

    @java.lang.Override
    public String pp() {
      String s ="{";
      for(VarDecla v : varList)
        s += v.pp();
      for(Instruction i : instList)
        s += i.pp();
      s +="}";
      return s;
    }

    @java.lang.Override
    public RetInstruction toIr() throws TypeException {
      Llvm.IR irBlock = new Llvm.IR(Llvm.empty(),Llvm.empty());

      Llvm.Instruction db = new Llvm.debBlock();
      irBlock.appendCode(db);

      if(varList != null) {
        for (VarDecla v : varList) {
          Declaration.RetDeclaration retVar = v.toIR();
          irBlock.append(retVar.ir);
        }
      }

      for(Instruction i : instList){
        Instruction.RetInstruction retInst = i.toIr();
        irBlock.append(retInst.ir);

      }

      Llvm.Instruction fb = new Llvm.finBlock();
      irBlock.appendCode(fb);

      return new RetInstruction(irBlock,null,null);


    }
  }
  // Warning: this is the type from VSL+, not the LLVM types!
  static public abstract class Type {
    public abstract String pp();
    public abstract Llvm.Type toLlvmType();
  }

  static class Int extends Type {
    public String pp() {
      return "INT";
    }

    @Override public boolean equals(Object obj) {
      return obj instanceof Int;
    }

    public Llvm.Type toLlvmType() {
      return new Llvm.Int();
    }
  }
}
