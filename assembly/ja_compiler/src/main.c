#include "ast.h"
#include "compiler.h"

#include <stdio.h>
#include <stdlib.h>

int yyparse(struct Function **result);

int main(void) {
        struct Function *root = nullptr;

        if (yyparse(&root) == 0) {
                printf("Parsing succeeded\n");
                traverse_function(root);

                char *assembly = visit_function(root);
                if (assembly) {
                        printf("Generated assembly: \n\n%s\n", assembly);

                        FILE *f = fopen("output.s", "w");
                        if (f) {
                                fputs(assembly, f);
                                fclose(f);
                        }

                        free(assembly);
                }
        }
        else {
                printf("Parsing failed\n");
        }

        return 0;
}
