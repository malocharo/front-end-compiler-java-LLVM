parser grammar VSLParser;

options {
  language = Java;
  tokenVocab = VSLLexer;
}

@header {
  package TP2;

  import java.util.stream.Collectors;
  import java.util.Arrays;
}


// TODO : other rules

program returns [ASD.Program out]
    : e=block EOF { $out = new ASD.Program($e.out); } // TODO : change when you extend the language
    ;

expression returns [ASD.Expression out]
    : r = expressionBprio{$out = $r.out;}
    ;

expressionBprio returns [ASD.Expression out]
    : l=expressionHprio (
        PLUS  r=expressionHprio{$l.out = new ASD.AddExpression($l.out, $r.out);}
      | MINUS r=expressionHprio{$l.out = new ASD.SubExpression($l.out, $r.out);}
    )*{$out = $l.out;}
    |  r=expressionHprio{$out = $r.out;}
    ;

expressionHprio returns [ASD.Expression out]
    : l=prim (
        MUL r= prim{$l.out = new ASD.MulExpression($l.out, $r.out);}
      | DIV r= prim{$l.out = new ASD.DivExpression($l.out, $r.out);}
    )*{$out = $l.out;}
    | r = prim {$out = $r.out;}
    ;

instruction returns [ASD.Instruction out]
    : IDENT AFF e = expression { $out = new ASD.AffInstruction($e.out, $IDENT.text); }
    | RET e = expression { $out = new ASD.Ret(new ASD.Int(),$e.out);}
    ;

block returns [ASD.Block out]
    : DBK { List<ASD.VarDecla> varList = null; List<ASD.Instruction> instList = new ArrayList<>();}
    (v = varDecla {varList = $v.out;})*
    (i = instruction {instList.add($i.out);})*
    FBK { $out = new ASD.Block(varList, instList); }
    ;

factor returns [ASD.Expression out]
    : p=prim { $out = $p.out; }
    // TODO : that's all?
    ;

prim returns [ASD.Expression out]
    : INTEGER { $out = new ASD.IntegerExpression($INTEGER.int); }
    | IDENT   { $out = new ASD.IdExpression(new ASD.Int(), $IDENT.text);}
    // TODO : that's all?
    ;
varDecla returns [List<ASD.VarDecla> out]
    : INT {List<ASD.VarDecla> varList = new ArrayList<>();}
    (IDENT {varList.add(new ASD.VarDecla(new ASD.Int(), $IDENT.text)); } COMA)*
    IDENT  {varList.add(new ASD.VarDecla(new ASD.Int(), $IDENT.text)); }
    { $out = varList; }
    ;