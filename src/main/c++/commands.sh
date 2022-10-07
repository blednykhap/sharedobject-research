gcc -c -Wall -Werror -fpic calc.c
gcc -shared -o libcalc.so calc.o