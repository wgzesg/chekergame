javac *.java
javac checker/src/*.java
javac checker/src/*/*.java
jar cfe checker.jar Main Main.class checker/src/*.class checker/src/*/*.class
