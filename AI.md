for testing in week 2, used chatgpt find why test fails despite expected and actual look the same. and used it togenerate these command sed -i '' 's/[[:space:]]*$//' ACTUAL.TXT
sed -i '' 's/[[:space:]]*$//' EXPECTED.TXT
to fix the issue

for week2 level 5, used chatgpt for debugging 
and used it to regnerate the runtest.sh to make sure 
it can be used to test error handling. I also used 
it to generate some test scripts for error handling
.
for level 5, gpt suggest me to use trim method to remove the redundant whitespace that can cause unexpected behaviour in error handling 