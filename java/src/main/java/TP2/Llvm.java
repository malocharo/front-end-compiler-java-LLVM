package TP2;

import java.util.List;
import java.util.ArrayList;

// This file contains a simple LLVM IR representation
// and methods to generate its string representation

public class Llvm {
  static public class IR {
    List<Instruction> header; // IR instructions to be placed before the code (global definitions)
    List<Instruction> code;   // main code

    public IR(List<Instruction> header, List<Instruction> code) {
      this.header = header;
      this.code = code;
    }

    // append an other IR
    public IR append(IR other) {
      header.addAll(other.header);
      code.addAll(other.code);
      return this;
    }

    // append a code instruction
    public IR appendCode(Instruction inst) {
      code.add(inst);
      return this;
    }

    // append a code header
    public IR appendHeader(Instruction inst) {
      header.add(inst);
      return this;
    }

    // Final string generation
    public String toString() {
      // This header describe to LLVM the target
      // and declare the external function printf
      StringBuilder r = new StringBuilder("; Target\n" +
        "target triple = \"x86_64-unknown-linux-gnu\"\n" +
        "; External declaration of the printf function\n" +
        "declare i32 @printf(i8* noalias nocapture, ...)\n" +
        "\n; Actual code begins\n\n");

      for(Instruction inst: header)
        r.append(inst);

      r.append("\n\n");

      // We create the function main
      // TODO : remove this when you extend the language
      //r.append("define i32 @main() \n");


      for(Instruction inst: code)
        r.append(inst);

      // TODO : remove this when you extend the language
      //r.append("}\n");

      return r.toString();
    }
  }

  // Returns a new empty list of instruction, handy
  static public List<Instruction> empty() {
    return new ArrayList<Instruction>();
  }


  // LLVM Types
  static public abstract class Type {
    public abstract String toString();
  }

  static public class Int extends Type {
    public String toString() {
      return "i32";
    }
  }

  static public class Void extends Type {
    @java.lang.Override
    public String toString() {
      return "void";
    }

  }

  // TODO : other types


  // LLVM IR Instructions
  static public abstract class Instruction {
    public abstract String toString();
  }

  static public class Add extends Instruction {
    Type type;
    String left;
    String right;
    String lvalue;

    public Add(Type type, String left, String right, String lvalue) {
      this.type = type;
      this.left = left;
      this.right = right;
      this.lvalue = lvalue;
    }

    public String toString() {
      return lvalue + " = add " + type + " " + left + ", " + right +  "\n";
    }
  }

  static public class Return extends Instruction {
    Type type;
    String value;

    public Return(Type type, String value) {
      this.type = type;
      this.value = value;
    }

    public String toString() {
      return "ret " + type + " " + value + "\n";
    }
  }

  static public class Sub extends Instruction {
    Type type;
    String left;
    String right;
    String lvalue;

    public Sub(Type t, String l, String r, String lvalue) {
      this.type = t;
      this.left = l;
      this.right = r;
      this.lvalue = lvalue;
    }
    public String toString() { return lvalue + " = sub " +type+" "+left+", "+right+"\n";}
  }

  static public class Mul extends Instruction {
    Type type;
    String left;
    String right;
    String lvalue;

    public Mul(Type t, String l, String r, String lvalue) {
      this.type = t;
      this.left = l;
      this.right = r;
      this.lvalue = lvalue;
    }
    public String toString() {
      return lvalue + " = mul " +type+" "+left+", "+right+"\n";
    }
  }

  static public class Div extends Instruction {
    Type type;
    String left;
    String right;
    String lvalue;

    public Div(Type t, String l, String r, String lvalue) {
      this.type = t;
      this.left = l;
      this.right = r;
      this.lvalue = lvalue;
    }
    public String toString() {
      return lvalue + " = udiv " +type+" "+left+", "+right+"\n";
    }
  }

  static public class Aff extends Instruction {
    Type type;
    String val;
    String id;

    public Aff(Type type, String val, String id) {
      this.type = type;
      this.val = val;
      this.id = id;
    }

    @java.lang.Override
    public String toString() {
      return "store "+type+" "+val+", "+type+"* %"+id+"\n";

    }
  }

  // load <=> affecter une valeur contenu dans la memoire dans une variable
  /*%ptr = alloca i32                              ; yields i32*:ptr
   store i32 3, i32* %ptr                          ; yields void
   %val = load i32, i32* %ptr                      ; yields i32:val = i32 3
  */

  static public class Load extends Instruction {
    Type type;
    String id;
    String tmp;

    public Load(Type t, String id, String tmp) {
      this.id = id;
      this.type = t;
      this.tmp = tmp;
    }

    @Override
    public String toString() {
      return  tmp + "= load "+type+", "+type+"* %"+id+"\n";
    }
  }

  static public class VarDeclaration extends Instruction {
    String id;
    Type type;

    public VarDeclaration(Type t, String s) {
      this.id = s;
      this.type = t;
    }

    @java.lang.Override
    public String toString() {
      return "%"+id+" = alloca "+type+"\n";
    }
  }

  static public class debBlock extends Instruction {
    public debBlock(){

    }

    @java.lang.Override
    public String toString() {
      return "{\n";
    }
  }

  static public class finBlock extends Instruction {
    public finBlock(){}

    @java.lang.Override
    public String toString() {
      return "}\n";
    }
  }

  static public class Cond extends Instruction {
    Type type;
    String res;
    Boolean b;
    String tmp;

    public Cond(Type t,String r, Boolean b,String tmp) {
      this.type = t;
      this.res = r;
      this.b = b;
      this.tmp = tmp;
    }

    @java.lang.Override
    public String toString() {
      String test = new String();
      if(b)
        test = "eq";
      else
        test = "ne";
      return tmp +" = icmp " + test + " " + type + " " +res +", 0\n";
    }
  }

  static public class Label extends Instruction {
    String label;

    public Label(String s) {
      this.label = s;
    }

    @java.lang.Override
    public String toString() {
      return "\n" + this.label + ":\n";
    }
  }

  static public class Goto extends Instruction {
    String label;

    public Goto(String s) {
      this.label = s;
    }

    @java.lang.Override
    public String toString() {
      return "br label %"+label+"\n";
    }
  }

  static public class IfThenElse extends Instruction {
    String thenLab;
    String elseLab;
    String fiLab;
    String condRes;

    public IfThenElse(String c, String t, String e, String f) {
      this.condRes = c;
      this.thenLab = t;
      this.elseLab = e;
      this.fiLab = f;
    }

    @java.lang.Override
    public String toString() {
      if(this.elseLab == null)
        return "br i1 "+this.condRes + ", label %"+ this.thenLab + ", label %" +this.fiLab +"\n";
      return "br i1 " +this.condRes + ", label %"+this.thenLab + ", label %" +this.elseLab +"\n";

    }
  }

  static public class FuncImpl extends Instruction {
    Type type;
    String id;
    String expr;
    Type type_expr;
    public FuncImpl(Type t, String id, String e, Type te) {
      this.type = t;
      this.id = id;
      this.expr = e;
      this.type_expr = te;

    }
    @java.lang.Override
    public String toString() {
      String s ="";
      s = "define "+ type + " @"+id;
      if(this.expr != null)
        s += "("+type_expr+" "+expr+")\n";
      else
        s += "()\n";
      return s;
    }
  }
  // TODO : other instructions
}
