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
    : e=expression EOF { $out = new ASD.Program($e.out); } // TODO : change when you extend the language
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
    : l=primary (
        MUL r= primary{$l.out = new ASD.MulExpression($l.out, $r.out);}
      | DIV r= primary{$l.out = new ASD.DivExpression($l.out, $r.out);}
    )*{$out = $l.out;}
    | r = primary {$out = $r.out;}
    ;

factor returns [ASD.Expression out]
    : p=primary { $out = $p.out; }
    // TODO : that's all?
    ;

primary returns [ASD.Expression out]
    : INTEGER { $out = new ASD.IntegerExpression($INTEGER.int); }
    // TODO : that's all?
    ;
