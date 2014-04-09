@echo off
rem  使用Lexer 
rem  运行Lexer时需要三个参数
rem  	arg1 - 关键字词典
rem 	arg2 - 符号词典
rem 	arg3 - 待分析的*.c源文件
@echo on

java -cp ./bin Lexer ./lib/keyword.dic ./lib/punctuation.dic sample.c
pause