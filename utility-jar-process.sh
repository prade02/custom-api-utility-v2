# find all the jar in the respository and do an individual jar extract - this outputs the extracted content in the current directory
find ~/.gradle/caches/modules-2/files-2.1 "*.jar" -exec jar -xvf {} \;

# archive/jar all the class files in the current directory
jar -cvf all.jar **/*.class

# compile all java source files(this folder and sub folder) which has depencecies. create compiled files(classes) into the -d directory
# javac -d bin/ **/*.java -cp /path/to/a/single/jar/file/:/path/to/a/single/jar/file
# javac -d bin/ **/*.java -cp /path/to/a/single/consolidated/jar/file

# run compiled java class - the class should have a public static void main(String[] args) method
# java fully.qualified.name.ClassName

# run a java application with dependencies
# java -cp .:/path/to/the/jar fully.qualified.class.name

# creating jar - non runnable - just a java library. class files are from this and subdirectory
# jar -cvf file-name.jar **/*.class

# create a runnable jar
# manifest.mf has Main-Class: fully.qualified.class.name.ClassName
# jar -cvfm file-name.jar manifest.mf **/*.class

# run a jar file - runnable JAR
# java -jar jar-file-name.jar
