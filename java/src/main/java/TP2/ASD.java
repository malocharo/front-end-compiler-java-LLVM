package TP2;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ASD {
  static public class Program {
    Func e; // What a program contains. TODO : change when you extend the language

    public Program(Func e) {
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
        return e.toIR().ir;

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
      return new RetExpression(retCond.ir,retCond.type,tmp);
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

  static public class IfThenElse extends Instruction {
    Condition cond;
    List<Instruction> thenInst;
    List<Instruction> elseInst;

    public IfThenElse(Condition c, List<Instruction> t, List<Instruction> e) {
      this.cond = c;
      this.thenInst = t;
      this.elseInst = e;
    }

    @java.lang.Override
    public String pp() {
      String s = "IF "+this.cond.pp()+"\nTHEN\n";
      for(Instruction i : thenInst)
      s += i.pp();

      if(this.elseInst != null) {
        s += "\nELSE\n";
        for(Instruction i : elseInst)
         s+= i.pp();
      }

      s += "\nFI\n";
      return s;
    }

    @java.lang.Override
    public TP2.ASD.Instruction.RetInstruction toIr() throws TypeException {
      Llvm.IR ifIR = new Llvm.IR(Llvm.empty(),Llvm.empty());

      //label ie %then7 %else8
      String then_lab = Utils.newlab("then");
      String fi_lab = Utils.newlab("fi");
      String else_lab = null;

      Llvm.Instruction then_lab_inst = new Llvm.Label(then_lab);
      Llvm.Instruction fi_lab_inst = new Llvm.Label(fi_lab);
      Llvm.Instruction else_lab_inst = null;


      Expression.RetExpression cond_ret = this.cond.toIR();

      if(this.elseInst != null) {
        else_lab = Utils.newlab("else");
        else_lab_inst = new Llvm.Label(else_lab);
      }

      // goto
      Llvm.Instruction goto_fi = new Llvm.Goto(fi_lab);
      Llvm.Instruction jump = new Llvm.IfThenElse(cond_ret.result,then_lab,else_lab,fi_lab);

      ifIR.append(cond_ret.ir);
      ifIR.appendCode(jump);
      ifIR.appendCode(then_lab_inst);
      for(Instruction i : thenInst) {
        Instruction.RetInstruction thenInstRet = i.toIr();
        ifIR.append(thenInstRet.ir);
      }
      ifIR.appendCode(goto_fi); //this could be useless but idk how to optimize it

      if(this.elseInst != null) {
        ifIR.appendCode(else_lab_inst);
        for(Instruction i : elseInst) {
          Instruction.RetInstruction elseInstRet = i.toIr();
          ifIR.append(elseInstRet.ir);
        }
        ifIR.appendCode(goto_fi);
      }

      ifIR.appendCode(fi_lab_inst);

      return new RetInstruction(ifIR,null,null);
    }
  }

  static public class While extends Instruction  {
    Condition cond;
    List<Instruction> instList;

    public While(Condition c, List<Instruction> l) {
      this.cond = c;
      this.instList = l;
    }

    @java.lang.Override
    public String pp() {
      String s = "WHILE" + this.cond.pp() + "\nDO\n{\n";
      for(Instruction i : instList)
        s += i.pp();
      s += "\n}\nDONE\n";
      return s;
    }

    @java.lang.Override
    public TP2.ASD.Instruction.RetInstruction toIr() throws TypeException {
      Llvm.IR whileIR = new Llvm.IR(Llvm.empty(),Llvm.empty());

      //label ie %do6 %done7

      String do_label = Utils.newlab("do");
      String done_label = Utils.newlab("done");
      String while_label = Utils.newlab("while");

      Llvm.Instruction while_lab_inst = new  Llvm.Label(while_label);
      Llvm.Instruction do_label_inst = new Llvm.Label(do_label);
      Llvm.Instruction done_label_inst = new Llvm.Label(done_label);


      Expression.RetExpression cond_ret = this.cond.toIR();

      Llvm.Instruction goto_while = new Llvm.Goto(while_label);
      Llvm.Instruction jump = new  Llvm.IfThenElse(cond_ret.result,do_label,done_label,while_label);

      whileIR.appendCode(goto_while);
      whileIR.appendCode(while_lab_inst);
      whileIR.append(cond_ret.ir);
      whileIR.appendCode(jump);
      whileIR.appendCode(do_label_inst);
      for(Instruction i : this.instList) {
        Instruction.RetInstruction retInst = i.toIr();
        whileIR.append(retInst.ir);
      }
      whileIR.appendCode(goto_while);
      whileIR.appendCode(done_label_inst);

      return new RetInstruction(whileIR,null,null);
    }
  }

  static public abstract class Function {
    public abstract String pp();
    public abstract RetFunction toIR() throws TypeException;

    static public class RetFunction {
      public Llvm.IR ir;
      public Type type;
      public String res;

      public RetFunction(Llvm.IR ir, Type t, String r) {
        this.ir = ir;
        this.type = t;
        this.res = r;
      }

    }
  }

  static public class Proto extends Function {
    public Type type;
    public String id;
    public Expression expr;

    public Proto(Type t, String s,Expression e) {
      this.type = t;
      this.id = s;
      this.expr = e;
    }

    @java.lang.Override
    public String pp() {
      String s ="";
      if(this.expr != null)
        s = "PROTO " + type.pp() + " " + id + "("+this.expr.pp()+")\n";
      else
        s = "PROTO " + type.pp() + " " + id + "( )\n";

      return s;
    }

    @java.lang.Override
    public TP2.ASD.Function.RetFunction toIR() throws TypeException {
      return null;
    }
  }

  static public class Func extends Function {
    public Type type;
    public String id;
    public Expression expr;
    public Block blk;

    public Func(Type t, String s, Expression e, Block b) {
      this.type = t;
      this.id = s;
      this.expr = e;
      this.blk = b;
    }

    @java.lang.Override
    public String pp() {
      String s ="";
      s = "FUNC " + this.type.pp() + " "
              + this.id;
      if(this.expr != null)
        s += "(" + this.expr.pp() + ")\n";
      else
        s += "()\n";
      s += this.blk.pp();
      return s;
    }

    @java.lang.Override
    public TP2.ASD.Function.RetFunction toIR() throws TypeException {
      Llvm.IR funcIR = new Llvm.IR(Llvm.empty(),Llvm.empty());
      Llvm.Instruction fn;
      if(this.expr != null)
         fn = new Llvm.FuncImpl(this.type.toLlvmType(),this.id,this.expr.pp(),new ASD.Int().toLlvmType());
      else
         fn = new Llvm.FuncImpl(this.type.toLlvmType(),this.id,null,null);

      funcIR.appendCode(fn);
      funcIR.append(this.blk.toIr().ir);

      return new RetFunction(funcIR,null,null);
              //todo pas obligé de parametres
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

  static class Void extends Type {
    @java.lang.Override
    public String pp() {
      return "VOID";
    }

    public boolean equals(Object obj) {
      return obj instanceof Void;
    }

    @java.lang.Override
    public Llvm.Type toLlvmType() {
      return new Llvm.Void();
    }
  }
}
