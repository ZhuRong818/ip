#!/usr/bin/env bash

# Use first argument as input prefix (defaults to 'input')
INPUT_PREFIX=${1:-input}
INPUT_FILE="${INPUT_PREFIX}.txt"
EXPECTED_FILE="${INPUT_PREFIX}_EXPECTED-UNIX.TXT"

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]; then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]; then
    rm ACTUAL.TXT
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/*.java; then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# check input files
if [ ! -f "$INPUT_FILE" ]; then
    echo "❌ $INPUT_FILE: No such file"
    exit 1
fi

if [ ! -f "$EXPECTED_FILE" ]; then
    echo "❌ $EXPECTED_FILE: No such file"
    exit 1
fi

# run the program, feed commands from the input file and redirect output
java -classpath ../bin Manbo < "$INPUT_FILE" > ACTUAL.TXT

# convert to UNIX format
cp "$EXPECTED_FILE" EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# remove trailing whitespace
sed -i '' 's/[[:space:]]*$//' ACTUAL.TXT
sed -i '' 's/[[:space:]]*$//' EXPECTED-UNIX.TXT

# compare the output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]; then
    echo "Test result: ✅ PASSED"
    exit 0
else
    echo "Test result: ❌ FAILED"
    exit 1
fi
