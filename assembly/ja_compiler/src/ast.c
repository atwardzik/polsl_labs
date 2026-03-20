#include "ast.h"

#include <stdio.h>
#include <stdlib.h>

struct Expr *create_binary_expr(enum Operator op, struct Expr *left, struct Expr *right) {
        struct Expr *expr = malloc(sizeof(struct Expr));

        expr->type = EXPR_BINARY;
        expr->operator = op;
        expr->binary.left = left;
        expr->binary.right = right;

        return expr;
}

struct Expr *create_unary_expr(enum Operator op, struct Expr *left) {
        struct Expr *expr = malloc(sizeof(struct Expr));

        expr->type = EXPR_UNARY;
        expr->operator = op;
        expr->unary.operand = left;

        return expr;
}

struct Expr *create_leaf_expr(char *leaf_name) {
        struct Expr *expr = malloc(sizeof(struct Expr));

        expr->type = EXPR_LEAF;
        expr->leaf_name = leaf_name;

        return expr;
}

struct Expr *create_leaf_literal_expr(int leaf_literal) {
        struct Expr *expr = malloc(sizeof(struct Expr));

        expr->type = EXPR_LEAF_LITERAL;
        expr->leaf_literal = leaf_literal;

        return expr;
}

static void print_indent(int level) {
        printf("|");
        for (int i = 0; i < level; ++i) {
                printf("\t");
        }
}

void traverse_expr(const struct Expr *expr, const int level) {
        print_indent(level);
        if (expr == nullptr) {
                printf("NULL\n");
                return;
        }


        if (expr->type == EXPR_LEAF) {
                printf("[LEAF]: %s\n", expr->leaf_name);
        }
        else if (expr->type == EXPR_LEAF_LITERAL) {
                printf("[LEAF]: %i\n", expr->leaf_literal);
        }
        else if (expr->type == EXPR_UNARY) {
                printf("[UNARY]: %s\n", "OP_NEG");
                traverse_expr(expr->unary.operand, level + 1);
        }
        else if (expr->type == EXPR_BINARY) {
                printf("[BINARY]: ");
                switch (expr->operator) {
                        case OP_ADD:
                                printf("OP_ADD");
                                break;
                        case OP_SUB:
                                printf("OP_SUB");
                                break;
                        case OP_MUL:
                                printf("OP_MUL");
                                break;
                        default:
                                printf("<unknown>");
                }
                printf("\n");

                print_indent(level);
                printf("[LEFT]:\n");
                traverse_expr(expr->binary.left, level + 1);

                print_indent(level);
                printf("[RIGHT]:\n");
                traverse_expr(expr->binary.right, level + 1);
        }
}

struct Function *create_function(char *name, const struct ParamList params, struct Expr *body) {
        struct Function *fn = malloc(sizeof(struct Function));

        fn->name = name;
        fn->param_list = params.names;
        fn->param_count = params.count;
        fn->body = body;

        return fn;
}

void traverse_function(const struct Function *function) {
        printf("[FUNCTION]: %s\n", function->name);

        print_indent(1);
        printf("[PARAMS COUNT]: %i\n", function->param_count);

        print_indent(1);
        printf("[PARAMS]: ");
        for (int i = 0; i < function->param_count; ++i) {
                printf("%s ", function->param_list[i]);
        }
        printf("\n");

        print_indent(1);
        printf("[BODY]:\n");
        traverse_expr(function->body, 2);

        printf("+\n");
}
