#include <stdio.h>
#include <stdlib.h>

//全域變數


void sum(int input_a, int input_b)
{
  total = input_a + input_b;
}

// 全域變數
int total_2=-1;
void multiply(int input_a, int input_b)
{
  // 區域變數會覆蓋全域變數，但是效力只有這個function內部
  int total_2=-1;
  total_2=input_a*input_b;
}

int main()
{
	int total=-1;
  sum(3, 4);
  printf(" total: %d\n", total);
  
  multiply(5, 6);
  printf(" total_2: %d\n", total_2);
  
  return 1;
}
