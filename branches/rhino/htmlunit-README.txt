Steps needs to make this project:

- Get latest rhino version (currently 1.7R4)
- Make eclipse project, with 'src' 'testsrc' and 'toolsrc' folder as source folders, while
  making build/classes as the output folder 
- Add 'lib' and 'build' to svn:ignore
- Add 'htmlunittestsrc' which contains 'build.xml' and the test cases
- Let build.xml reference htmlunittestsrc/build.xml, with target 'junit'