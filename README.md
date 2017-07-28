# CAP 4 Archetype Type 1
## Download & Install Archetype
* git clone https://github.com/iisihub/cap4-archetype1.git type1
* cd type1
* mvn clean archetype:create-from-project
* cd target/generated-sources/archetype
* mvn install
* mvn archetype:update-local-catalog

## Generate Project from Archetype
* mvn archetype:generate -DarchetypeCatalog=local
* Choose the number of local -> com.iisigroup.xxxx:xxxx-archetype (xxxx-archetype)
<pre>
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] >>> maven-archetype-plugin:3.0.1:generate (default-cli) @ standalone-pom >>>
[INFO]
[INFO] <<< maven-archetype-plugin:3.0.1:generate (default-cli) @ standalone-pom <<<
[INFO]
[INFO] --- maven-archetype-plugin:3.0.1:generate (default-cli) @ standalone-pom ---
[INFO] Generating project in Interactive mode
[INFO] No archetype defined. Using maven-archetype-quickstart (org.apache.maven.archetypes:maven-archetype-quickstart:1.0)
Choose archetype:
1: local -> com.iisi.ss:ssms-archetype (ssms-archetype)
2: local -> com.iisigroup.cap:cap-archetype (cap-archetype)
3: local -> com.iisigroup.cap:xxxx-archetype (xxxx-archetype)
<span style="color:red">4: local -> com.iisigroup.xxxx:xxxx-archetype (xxxx-archetype)</span>
Choose a number or apply filter (format: [groupId:]artifactId, case sensitive contains): : <span style="color:red">4</span><span style="color:blue"> (according to the NO. above)</span>
Define value for property 'groupId': <span style="color:red">com.somecompany.somesystem</span><span style="color:blue"> (depends on project's requirement)</span>
Define value for property 'artifactId': <span style="color:red">someproject</span><span style="color:blue"> (depends on project's requirement)</span>
Define value for property 'version' 1.0-SNAPSHOT: : <span style="color:red">1.0.0</span><span style="color:blue"> (depends on project's requirement)</span>
Define value for property 'package' com.somecompany.somesystem: :
Confirm properties configuration:
groupId: com.somecompany.somesystem
artifactId: someproject
version: 1.0.0
package: com.somecompany.somesystem
 Y: :
[INFO] ----------------------------------------------------------------------------
[INFO] Using following parameters for creating project from Archetype: xxxx-archetype:4.0.1-archetype1
[INFO] ----------------------------------------------------------------------------
[INFO] Parameter: groupId, Value: com.somecompany.somesystem
[INFO] Parameter: artifactId, Value: someproject
[INFO] Parameter: version, Value: 1.0.0
[INFO] Parameter: package, Value: com.somecompany.somesystem
[INFO] Parameter: packageInPathFormat, Value: com/somecompany/somesystem
[INFO] Parameter: package, Value: com.somecompany.somesystem
[INFO] Parameter: version, Value: 1.0.0
[INFO] Parameter: groupId, Value: com.somecompany.somesystem
[INFO] Parameter: artifactId, Value: someproject
[INFO] Parent element not overwritten in E:\@IISI\@Product\CAP4\archetype\someproject\someproject-config\pom.xml
[INFO] Parent element not overwritten in E:\@IISI\@Product\CAP4\archetype\someproject\someproject-web\pom.xml
[INFO] Parent element not overwritten in E:\@IISI\@Product\CAP4\archetype\someproject\someproject-ap\pom.xml
[INFO] Parent element not overwritten in E:\@IISI\@Product\CAP4\archetype\someproject\someproject-auth-admin\pom.xml
[INFO] Project created from Archetype in dir: E:\@IISI\@Product\CAP4\archetype\someproject
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 2:50.930s
[INFO] Finished at: Wed Jul 19 12:22:54 CST 2017
[INFO] Final Memory: 10M/65M
[INFO] ------------------------------------------------------------------------
</pre>
* import maven project from "someproject" folder
