#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void print_error(const char *format, ...) {
        char buffer[256];

        va_list args;
        va_start(args, format);
        vsnprintf(buffer, 256, format, args);

        perror(buffer);

        va_end(args);
}


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

struct GeneratedAssembly *create_assembly_buffer(size_t size) {
        char *code = (char *) calloc(sizeof(char) * size, sizeof(char));

        if (code == nullptr) {
                return nullptr;
        }

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

        va_end(args);
}

struct VariableDescriptor {
        enum { SP_RELATIVE, REG } position;
        union {
                enum { X0, X1, X2, X3, X4, X5, X6, X7 } reg;
                size_t sp_offset;
        };
        const char *variable_name;
};

struct VariableDescriptorTable {
        struct VariableDescriptor **var_desc;
        size_t size;
};

constexpr int REG_COUNT = 8;
struct RegisterDescriptorTable {
        bool reg_used[REG_COUNT];
};

struct VariableDescriptorTable *create_variable_descriptor_table(size_t size) {
        auto descriptors = (struct VariableDescriptor **) malloc(sizeof(struct VariableDescriptor) * size);

        if (descriptors == nullptr) {
                return nullptr;
        }

        auto descriptor_table = (struct VariableDescriptorTable *) malloc(sizeof(struct VariableDescriptorTable));
        descriptor_table->var_desc = descriptors;
        descriptor_table->size = size;

        return descriptor_table;
}

struct VariableDescriptor *get_variable_position(struct VariableDescriptorTable *descriptors, const char *name) {
        for (size_t i = 0; i < descriptors->size; ++i) {
                if (strcmp(descriptors->var_desc[i]->variable_name, name) == 0) {
                        return descriptors->var_desc[i];
                }
        }

        return nullptr;
}

struct RegisterDescriptorTable *create_register_descriptor_table() {
        auto descriptors = (struct RegisterDescriptorTable *) malloc(sizeof(struct RegisterDescriptorTable));

        if (descriptors == nullptr) {
                return nullptr;
        }

        for (size_t i = 0; i < REG_COUNT; ++i) {
                descriptors->reg_used[i] = false;
        }

        return descriptors;
}

int get_reg(struct RegisterDescriptorTable *descriptors) {
        for (int i = 0; i < REG_COUNT; ++i) {
                if (descriptors->reg_used[i] == false) {
                        descriptors->reg_used[i] = true;
                        return i;
                }
        }

        return -1;
}

/*
 * Generate assembly code for expressions
 *
 * @param: assembly     generated code
 * @return: register where the result is stored.
 * */
int visit_expr(struct GeneratedAssembly *assembly, struct Expr *expr, struct VariableDescriptorTable *var_descriptors,
               struct RegisterDescriptorTable *reg_descriptors) {
        if (expr->type == EXPR_LEAF) {
                auto descriptor = get_variable_position(var_descriptors, expr->leaf_name);

                if (descriptor == nullptr) {
                        print_error("[E] no such variable %s in a scope", expr->leaf_name);
                        exit(-1);
                }

                int destination_register = get_reg(reg_descriptors);

                append_code(assembly, "ldr x%i, [sp, #%zu]\n", destination_register, descriptor->sp_offset);

                return destination_register;
        }
        else if (expr->type == EXPR_UNARY && expr->operator== OP_NEG) {
                int destination_register = visit_expr(assembly, expr->unary.operand, var_descriptors, reg_descriptors);

                append_code(assembly, "cmp x%i, #0\nbeq .one\nmov x%i, #0\nb .end\n.one:\nmov x%i, #1\n.end:\n",
                            destination_register, destination_register, destination_register);

                return destination_register;
        }
        else if (expr->type == EXPR_BINARY) {
                int first_destination_register =
                                visit_expr(assembly, expr->binary.left, var_descriptors, reg_descriptors);

                int left_storage_register = get_reg(reg_descriptors);

                if (first_destination_register != left_storage_register) {
                        append_code(assembly, "mov x%i, x%i\n", left_storage_register, first_destination_register);
                }


                int second_destination_register =
                                visit_expr(assembly, expr->binary.right, var_descriptors, reg_descriptors);

                switch (expr->operator) {
                        case OP_ADD:
                                append_code(assembly, "add x%i, x%i, x%i\n", left_storage_register,
                                            left_storage_register, second_destination_register);
                                break;
                        case OP_SUB:
                                append_code(assembly, "sub x%i, x%i, x%i\n", left_storage_register,
                                            left_storage_register, second_destination_register);
                                break;
                        case OP_MUL:
                                append_code(assembly, "mul x%i, x%i, x%i\n", left_storage_register,
                                            left_storage_register, second_destination_register);
                                break;
                        case OP_NEG:
                        default:
                                break;
                }

                return left_storage_register;
        }

        return -1;
}

char *visit_function(struct Function *fn) {
        auto assembly = create_assembly_buffer(1024);

        const char *fn_header = ".global _%s\n.align 4\n_%s:\n";
        append_code(assembly, fn_header, fn->name, fn->name);

        // Calling convention in registers, r0, r1, r2, r3, then stack.
        //
        // Function calls CANNOT be made (for now) from other function,
        // due to the link register not being saved.

        const char *fn_entrance = "sub sp, sp, #%i\n";
        size_t stack_shift = 32; // ((fn->parameter_count * sizeof(int)) / 16 + 1) * 16;
        append_code(assembly, fn_entrance, stack_shift);

        auto var_descriptors = create_variable_descriptor_table(fn->parameter_count);

        const char *save_reg = "str x%i, [sp, #%i]\n";
        const size_t total_offset = sizeof(size_t) * fn->parameter_count;
        for (int i = 0; i < fn->parameter_count; ++i) {
                if (i == 4) {
                        break;
                }

                size_t variable_offset = total_offset - (i + 1) * sizeof(size_t);

                append_code(assembly, save_reg, i, variable_offset);

                auto descriptor = (struct VariableDescriptor *) malloc(sizeof(struct VariableDescriptor));
                descriptor->position = SP_RELATIVE;
                descriptor->sp_offset = variable_offset;
                descriptor->variable_name = fn->param_list[i];

                var_descriptors->var_desc[i] = descriptor;
        }

        if (fn->parameter_count > 4) {
                size_t stack_parameters_offset = stack_shift;
                for (int i = 4; i < fn->parameter_count; ++i) {
                        auto descriptor = (struct VariableDescriptor *) malloc(sizeof(struct VariableDescriptor));
                        descriptor->position = SP_RELATIVE;
                        descriptor->sp_offset = stack_parameters_offset + i * sizeof(size_t);
                        descriptor->variable_name = fn->param_list[i];

                        var_descriptors->var_desc[i] = descriptor;
                }
        }


        auto reg_descriptors = create_register_descriptor_table();
        int ret_pos = visit_expr(assembly, fn->expr, var_descriptors, reg_descriptors);
        if (ret_pos) {
                append_code(assembly, "mov x0, x%i\n", ret_pos);
        }
        append_code(assembly, "ret\n");

        char *code = assembly->code;
        free(assembly);
        for (size_t i = 0; i < var_descriptors->size; ++i) {
                free(var_descriptors->var_desc[i]);
        }
        free(var_descriptors->var_desc);
        free(var_descriptors);
        free(reg_descriptors);
        return code;
}

int main(void) {
        // atom expressions - leafs
        struct Expr leaf_x = {
                        .type = EXPR_LEAF,
                        .leaf_name = "x",
        };

        struct Expr leaf_y = {
                        .type = EXPR_LEAF,
                        .leaf_name = "y",
        };

        struct Expr leaf_z = {
                        .type = EXPR_LEAF,
                        .leaf_name = "z",
        };


        struct Expr leaf_t = {
                        .type = EXPR_LEAF,
                        .leaf_name = "t",
        };

        // combined expressions - operations

        struct Expr unary_expr = {
                        .type = EXPR_UNARY,
                        .operator= OP_NEG,
                        .unary = {&leaf_x},
        };

        struct Expr binary_expr = {
                        .type = EXPR_BINARY,
                        .operator= OP_ADD,
                        .binary = {&leaf_x, &leaf_y},

        };

        struct Expr complex_expr = {
                        .type = EXPR_BINARY,
                        .operator= OP_ADD,
                        .binary = {&leaf_y, &unary_expr},
        };

        struct Expr multiply_expr = {.type = EXPR_BINARY, .operator= OP_MUL, .binary = {&leaf_z, &leaf_t}};
        struct Expr combined_expr = {.type = EXPR_BINARY, .operator= OP_SUB, .binary = {&complex_expr, &multiply_expr}};


        char *fn_params[] = {"x", "y", "z", "t"};
        struct Function fn_bin_expr = {fn_params, 2, "foo", &binary_expr};
        struct Function fn_unary_expr = {fn_params, 2, "bar", &unary_expr};
        struct Function fn_complex_expr = {fn_params, 2, "car", &complex_expr};
        struct Function fn_multiple_args = {fn_params, 4, "cpx", &combined_expr};


        puts("Unary Expression `bar(x,y): !x`: \n");
        char *assembly = visit_function(&fn_unary_expr);
        if (assembly) {
                printf("Generated assembly: \n\n%s\n", assembly);

                FILE *f = fopen("output_unary.s", "w");
                if (f) {
                        fputs(assembly, f);
                        fclose(f);
                }

                free(assembly);
        }

        puts("Binary Expression `foo(x,y): x + y`: \n");
        assembly = visit_function(&fn_bin_expr);
        if (assembly) {
                printf("Generated assembly: \n\n%s\n", assembly);

                FILE *f = fopen("output_binary.s", "w");
                if (f) {
                        fputs(assembly, f);
                        fclose(f);
                }

                free(assembly);
        }

        puts("Complex Expression `car(x,y): y + !x`: \n");
        assembly = visit_function(&fn_complex_expr);
        if (assembly) {
                printf("Generated assembly: \n\n%s\n", assembly);

                FILE *f = fopen("output_binary_complex.s", "w");
                if (f) {
                        fputs(assembly, f);
                        fclose(f);
                }

                free(assembly);
        }

        puts("Combined Expression `cpx(x,y,z,t): y + !x - z * t`: \n");
        assembly = visit_function(&fn_multiple_args);
        if (assembly) {
                printf("Generated assembly: \n\n%s\n", assembly);

                FILE *f = fopen("output_binary_complex_multiple.s", "w");
                if (f) {
                        fputs(assembly, f);

                        fputs(".text\n.global _start\n.align 4\n_start:\nmov x0, #0\nmov x1, #31\nmov x2, #3\nmov x3, #9\nbl _cpx\nmov w8, #93\nsvc #0\n", f);

                        fclose(f);
                }

                free(assembly);
        }


        return 0;
}
