%{
#include "y.tab.h"
%}

%option yylineno

%% //rules

[a-zA-Z]+[a-zA-Z0-9]*           {
        yylval.str = strdup(yytext);
        return IDENTIFIER;
}
[-]?[0-9]+                      {
        yylval.intval = strtol(yytext, NULL, 10);
        return LITERAL;
}
[(),:!+\-*]                     { return yytext[0]; }   /* character literals */
[ \t\n]                         { /* skip whitespace */ }
.                               { printf("Unknown token: %s\n", yytext); }

%% //subroutines

int yywrap() {
        return 1;
}
