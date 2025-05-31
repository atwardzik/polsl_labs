#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * Grammar
 * */
struct Expr {
        enum { EXPR_LEAF, EXPR_BINARY, EXPR_UNARY } type;
        enum { OP_ADD, OP_SUB, OP_MUL, OP_NEG } operator;
        union {
                struct {
                        struct Expr *left;
                        struct Expr *right;
                } binary;

                struct {
                        struct Expr *operand;
                } unary;

                char *leaf_name;
        };
};

struct Function {
        char **param_list;
        int parameter_count;
        char *name;
        struct Expr *expr;
};

struct FunctionCall {
        char *name;
        struct Expr **param_list;
        int parameter_count;
};

struct VariableOperation {
        char *name;
        enum { OP_EQ, OP_ADDEQ, OP_SUBEQ, OP_MULEQ } operator;
};

struct ExecUnit {
        struct Function **functions;
        struct Expr **body_expressions;
};


/*
 * Visitors
 * */

struct GeneratedAssembly {
        char *code;
        size_t length;
};

void resize_if_not_fit(struct GeneratedAssembly *assembly, size_t appended_length) {
        size_t code_length = strlen(assembly->code);
        if (code_length + appended_length >= assembly->length) {
                char *new_code = (char *) realloc(assembly->code, code_length + 1024);

                if (new_code) {
                        assembly->code = new_code;
                }
        }
}

void append_code(struct GeneratedAssembly *assembly, const char *appended_format, ...) {
        va_list(args);
        va_start(args, appended_format);

        size_t appended_length = vsnprintf(nullptr, 0, appended_format, args);
        resize_if_not_fit(assembly, appended_length);

        size_t current_length = strlen(assembly->code);
        vsnprintf(assembly->code + current_length, assembly->length - current_length, appended_format, args);
}

struct VariableLocation {
        char *name;
        char *reg;
};

void visitExpr(struct GeneratedAssembly *assembly, struct Expr *expr, struct VariableLocation **variableLocations) {
        if (expr->type == EXPR_UNARY && expr->operator == OP_NEG) {
                append_code(assembly, "cmp %s, #0\nbeq .one\nmov %s, #0\nb .end\n.one:\nmov %s, #1\n.end:\n");
        }
        else if (expr->type == EXPR_BINARY) {
                
        }
}

char *visitFunction(struct Function *fn) {
        char *code = (char *) calloc(sizeof(char) * 1024, sizeof(char));
        struct GeneratedAssembly assembly = {code, 1024};

        const char *fn_header = ".global _%s\n.align 4\n%s:\n";
        append_code(&assembly, fn_header, fn->name, fn->name);

        //calling convention in registers, r0, r1, r2, r3, then stack
        if (fn->parameter_count > 4) {
                perror("[E] Only 4 arguments supported.\n");
                goto cleanup;
        }

        if (fn->expr->type == EXPR_LEAF) {
                // identity function, argument already in r0
                append_code(&assembly, "bx lr\n");
        }
        else {
                visitExpr(&assembly, fn->expr, nullptr);
        }



        return assembly.code;

cleanup:
        free(assembly.code);
        return nullptr;
}

int main(void) {
        struct Expr leaf_left = {
                        .type = EXPR_LEAF,
                        .leaf_name = "x",
        };

        struct Expr leaf_right = {
                        .type = EXPR_LEAF,
                        .leaf_name = "y",
        };

        struct Expr fn_body = {
                        .type = EXPR_BINARY,
                        .operator= OP_ADD,
                        .binary = {&leaf_left, &leaf_right},

        };

        char *fn_params[] = {"x", "y"};
        struct Function fn = {fn_params, 2, "foo", &fn_body};

        printf("Left function operand is: %s\n", fn.expr->binary.left->leaf_name);
        printf("Right function operand is: %s\n", fn.expr->binary.right->leaf_name);

        char *assembly = visitFunction(&fn);
        if (assembly) {
                printf("Generated assembly: \n\n%s\n", assembly);

                free(assembly);
        }

        return 0;
}
