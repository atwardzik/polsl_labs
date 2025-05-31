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

struct GeneratedAssembly *createAssemblyBuffer(size_t size) {
        char *code = (char *) calloc(sizeof(char) * size, sizeof(char));

        struct GeneratedAssembly *buffer = (struct GeneratedAssembly *) malloc(sizeof(struct GeneratedAssembly));
        buffer->code = code;
        buffer->length = size;

        return buffer;
}

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

struct RegisterDescriptor {
        enum { X0, X1, X2, X3 } reg;
        const char *variable;
};

/*
 * Generate assembly code for expressions
 *
 * Return: Register number where the result is stored.
 * */
int visitExpr(struct GeneratedAssembly *assembly, struct Expr *expr, struct RegisterDescriptor **reg_desc) {
        if (expr->type == EXPR_UNARY && expr->operator== OP_NEG) {
                append_code(assembly, "cmp %s, #0\nbeq .one\nmov %s, #0\nb .end\n.one:\nmov %s, #1\n.end:\n");
                // return
        }
        else if (expr->type == EXPR_BINARY) {
                auto left_assembly = createAssemblyBuffer(1024);
                auto right_assembly = createAssemblyBuffer(1024);

                visitExpr(left_assembly, expr->binary.right, reg_desc);
                visitExpr(right_assembly, expr->binary.right, reg_desc);
        }

        return 0;
}

char *visitFunction(struct Function *fn) {
        auto assembly = createAssemblyBuffer(1024);

        const char *fn_header = ".global %s\n.align 4\n%s:\n";
        append_code(assembly, fn_header, fn->name, fn->name);

        const char *fn_entrance = "sub sp, sp, %i\n";
        //size_t stack_alignment = ((fn->parameter_count * sizeof(int)) / 16 + 1) * 16;
        append_code(assembly, fn_entrance, 16);


        const char *save_reg = "str x%i, [sp, %i]\n";
        const size_t total_offset = sizeof(int) * fn->parameter_count;
        int i = 0;
        while (i < fn->parameter_count && i < 4) {
                size_t variable_offset = total_offset - i * sizeof(int);

                append_code(assembly, save_reg, i, variable_offset);

                i += 1;
        }

        // calling convention in registers, r0, r1, r2, r3, then stack
        if (fn->parameter_count > 4) {
                perror("[E] Only 4 arguments supported.\n");
                goto cleanup;
        }

        struct RegisterDescriptor r0 = {.reg = X0, .variable = fn->param_list[0]};
        struct RegisterDescriptor r1 = {.reg = X1, .variable = fn->param_list[1]};
        struct RegisterDescriptor r2 = {.reg = X2, .variable = fn->param_list[2]};
        struct RegisterDescriptor r3 = {.reg = X3, .variable = fn->param_list[3]};

        struct RegisterDescriptor *reg_desc[4] = {&r0, &r1, &r2, &r3};

        if (fn->expr->type == EXPR_LEAF) {
                // identity function, argument already in r0
                append_code(assembly, "bx lr\n");
        }
        else {
                visitExpr(assembly, fn->expr, reg_desc);
        }


        char *code = assembly->code;
        free(assembly);
        return code;

cleanup:
        free(assembly->code);
        free(assembly);
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
