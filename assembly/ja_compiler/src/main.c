#include "ast.h"
#include <stdio.h>

int yyparse(struct Function **result);

int main(void) {
	struct Function *root = nullptr;

	if (yyparse(&root) == 0) {
		printf("Parsing succeeded\n");
		traverse_function(root);
	} else {
		printf("Parsing failed\n");
	}

	return 0;
}
