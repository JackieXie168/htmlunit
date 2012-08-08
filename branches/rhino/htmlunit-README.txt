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

- To manually generate rhinoDiff.txt:
    - git clone https://github.com/mozilla/rhino.git
    - cd rhino
    - git checkout Rhino1_7R4_RELEASE
    - apply the changes
    - git diff >rhinoDiff.txt

- To release:
  1- Change version in pom.xml and htmlunit.xml
  2- Make sure rhinoDiff.txt is updated 
  3- Deploy the release files into Sonatype staging repository:
      ant -f htmlunit.xml jar-all
      mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=build/htmlunit-core-js-2.11.jar
      mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=build/htmlunit-core-js-2.11-sources.jar -Dclassifier=sources
      mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=build/htmlunit-core-js-2.11-javadoc.jar -Dclassifier=javadoc

TODO:
    - GPG automated from ant (in "bundle" target), if possible
