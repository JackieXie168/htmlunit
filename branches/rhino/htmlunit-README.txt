Steps needs to make this project:

- Get latest rhino version (currently 1.7R4)
- Make eclipse project, with 'src' 'testsrc' and 'toolsrc' folder as source folders, while
  making build/classes as the output folder 
- Add 'lib' and 'build' to svn:ignore
- Add 'htmlunittestsrc' which contains 'build.xml' and the test cases
- Use only 'htmlunit.xml' to 'test' by:
    ant -f htmlunit.xml test
   
- Deploy snapshot:
    ant -f htmlunit.xml deploy-snapshot

- Generate bundle for release:
    ant -f htmlunit.xml deploy-snapshot


TODO:
    - GPG automated from ant (in "bundle" target)
    - Git generation of diffRhino.txt
    - Snapshot upload of sources and javadoc
    - 'Get' maven-ant-tasks-2.1.3.jar to lib folder automatically
    