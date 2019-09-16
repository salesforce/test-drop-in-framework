# How to publish to Nexus
1. mvn clean package
    This will ensure your module build and package correctly.
2. mvn compile jar:jar
	which creates jar file target/test-drop-in-framework-<version>.jar
3. mvn install:install-file -Dfile=target/test-drop-in-framework-<version>.jar -DpomFile=pom.xml
	which deploys the jar file to local repository
4. mvn release:prepare
    This will prepare the release, more doc here:
    http://maven.apache.org/maven-release/maven-release-plugin/examples/prepare-release.html
    You may choose your release version, the default will only a minor version upgrade.
5. mvn release:perform
    This step will build/publish to nexus and also tag release on git repository, more doc here
    (http://maven.apache.org/maven-release/maven-release-plugin/examples/perform-release.html)
6. mvn release:clean
    Cleans up your local env

Manual approach:
mvn deploy:deploy-file -Dfile=target/test-drop-in-framework-2.0.8.jar -DrepositoryId=nexus -Durl=https://nexus.soma.salesforce.com/nexus/content/repositories/releases -DgroupId=com.salesforce.cqe -DartifactId=test-drop-in-framework -Dversion=2.0.8 -Dpackaging=jar