# How to publish to Sonatype Nexus
1. mvn clean package
    This will ensure your module build and package correctly.
2. mvn release:prepare
    This will prepare the release, more doc here:
    http://maven.apache.org/maven-release/maven-release-plugin/examples/prepare-release.html
    You may choose your release version, the default will only a minor version upgrade.
3. mvn release:perform
    This step will build/publish to nexus and also tag release on git repository, more doc here
    (http://maven.apache.org/maven-release/maven-release-plugin/examples/perform-release.html)
4. mvn release:clean
    Cleans up your local env

## Troubleshooting
1. In case auto-release fails e.g. because of issues with signing or POM file, check out:
    https://central.sonatype.org/pages/releasing-the-deployment.html

## Create artifacts based on specific POM file
1. mvn compile jar:jar
	which creates jar file target/test-drop-in-framework-<version>.jar
2. mvn install:install-file -Dfile=target/test-drop-in-framework-<version>.jar -DpomFile=pom.xml
	which deploys the jar file to local repository
