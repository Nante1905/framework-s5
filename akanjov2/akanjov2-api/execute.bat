set tomcatPath="C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"

dir src /a-d /b /s *.java > sources.txt
javac -cp .;lib/* -d bin @sources.txt
del sources.txt

cd bin
jar -cvf ../web/WEB-INF/lib/Akanjov2.jar *

xcopy "*" "../web/WEB-INF/classes" /E /Y /I

cd ../lib
xcopy "*" "../web/WEB-INF/lib" /E /Y /I

cd ..
copy "credentials.json" "./web/config"

cd ./web
jar -cvf %tomcatPath%/Akanjov2.war *
