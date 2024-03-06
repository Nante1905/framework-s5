set tomcatPath="C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"

dir src /a-d /b /s *.java > sources.txt
javac -cp .;lib/* -d bin @sources.txt
del sources.txt

cd bin
jar -cvf ../web/WEB-INF/lib/AkanjoApiv2.jar *

xcopy "*" "../web/WEB-INF/classes" /E /Y /I

cd ../lib
xcopy "*" "../web/WEB-INF/lib" /E /Y /I

cd ../web
jar -cvf %tomcatPath%/AkanjoApiv2.war *
