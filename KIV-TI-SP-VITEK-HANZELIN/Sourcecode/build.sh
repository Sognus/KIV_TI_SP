mkdir bin
mkdir lib
javac -d bin -cp src src/cz/zcu/kiv/ti/sp/Main.java
ant/bin/ant -buildfile ./build.xml jar