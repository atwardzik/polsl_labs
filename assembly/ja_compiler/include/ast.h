#ifndef AST_H
#define AST_H

enum Type { EXPR_LEAF, EXPR_LEAF_LITERAL, EXPR_BINARY, EXPR_UNARY };

enum Operator { OP_ADD, OP_SUB, OP_MUL, OP_NEG };

struct Expr {
        enum Type type;
        enum Operator operator;

        union {
                struct {
                        struct Expr *left;
                        struct Expr *right;
                } binary;

                struct {
                        struct Expr *operand;
                } unary;

                char *leaf_name;
                int leaf_literal;
        };
};

struct Function {
        char **param_list;
        int param_count;
        char *name;
        struct Expr *body;
};

struct ParamList {
        char **names;
        int count;
};

#if 0
struct FunctionCall {
        char *name;
        struct Expr **param_list;
        int parameter_count;
};

struct ExecUnit {
        struct Function **functions;
        struct Expr **body_expressions;
};
#endif

//
// Expressions
//

struct Expr *create_binary_expr(enum Operator op, struct Expr *left, struct Expr *right);

struct Expr *create_unary_expr(enum Operator op, struct Expr *left);

struct Expr *create_leaf_expr(char *leaf_name);

struct Expr *create_leaf_literal_expr(int leaf_literal);

void traverse_expr(const struct Expr *expr, int level);

//
// Functions
//

struct Function *create_function(char *name, struct ParamList params, struct Expr *body);

void traverse_function(const struct Function *function);

#endif
