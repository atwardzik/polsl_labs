//
// Created by Artur Twardzik on 20/03/2026.
//

#ifndef JA_COMPILER_COMPILER_H
#define JA_COMPILER_COMPILER_H

#include "ast.h"

#include <stddef.h>

struct GeneratedAssembly {
        char *code;
        size_t length;
};

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


int visit_expr(
        struct GeneratedAssembly *assembly, struct Expr *expr, struct VariableDescriptorTable *var_descriptors,
        struct RegisterDescriptorTable *reg_descriptors
);

char *visit_function(struct Function *fn);

#endif //JA_COMPILER_COMPILER_H
