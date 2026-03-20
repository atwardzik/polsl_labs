%{
#include <stdio.h>
#include <string.h>
#include "ast.h"
#include "lex.yy.c"

void yyerror(struct Function **res, const char *s);
int yylex(void);
int yywrap(void);

%}

%parse-param { struct Function **fn }

%union {
        char *str;
        int intval;
        struct Expr *expression;
        struct Function *fn;
        struct ParamList params;
}

%token <str> IDENTIFIER
%token <intval> LITERAL

%type <expression> expr additive_expr mult_expr unary_expr primary_expr
%type <fn> fn_declaration
%type <params> param_list

%%
fn_declaration  : IDENTIFIER '(' param_list ')' ':' expr {
                        $$ = create_function($1, $3, $6);
                        *fn = $$;
                };

param_list      : /*empty*/ {
                        $$.names = NULL;
                        $$.count = 0;
                }
                | IDENTIFIER {
                        $$.names = malloc(sizeof(char *));
                        $$.names[0] = $1;
                        $$.count = 1;
                }
                | param_list ',' IDENTIFIER {
                        $$ = $1;
                        $$.names = realloc($1.names, ($1.count + 1) * sizeof(char *));
                        $$.names[$1.count] = $3;
                        $$.count = $1.count + 1;
                }
                ;

expr            : additive_expr { $$ = $1; }
                ;

additive_expr   : mult_expr { $$ = $1; }
                | additive_expr '+' mult_expr {
                        $$ = create_binary_expr(OP_ADD, $1, $3);
                }
                | additive_expr '-' mult_expr {
                        $$ = create_binary_expr(OP_SUB, $1, $3);
                }
                ;

mult_expr       : unary_expr { $$ = $1; }
                | mult_expr '*' unary_expr {
                        $$ = create_binary_expr(OP_MUL, $1, $3);
                }
                ;

unary_expr      : primary_expr { $$ = $1; }
                | '!' primary_expr {
                        $$ = create_unary_expr(OP_NEG, $2);
                }
                ;

primary_expr    : LITERAL { $$ = create_leaf_literal_expr($1); }
                | IDENTIFIER { $$ = create_leaf_expr($1); }
                | '(' expr ')' { $$ = $2; }
                ;

%%



void yyerror(struct Function **res, const char *msg) {
        fprintf(stderr, "%s\n", msg);
}
