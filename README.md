-format mba atao milailay amlay Controller
-ilay App tsy atao test tsony

- génération front:
  \*génération import: (anaty Test.java misy exemple)
  \_ ohatra hoe ampiana <import> eo amlay template pour indiquer hoe mila manao génération import XD
  \_mampiasa FrontGeneration.generateImport
  \_ilay import type mila atao manuel:

  - ampiana an'ilay type anaty PageImport[] zay vao antsoina ilay generateImport

- gestion import amlay form satria dynamique
  -mnu koa mila ovaina ilay nom app

  - mila ovaina jackson json lay gson (anaty toJson handyman) fa tsy haiko mampiditra jar an'ilay jackson anaty jar an'ilay handyman ref buildena lay izy

  generate form:

- String generateForm(Entity e)
- alaina ilay template
- alaina template input, inputNumber, select, date
- alaina template foreignKeyGetter
- manao var String inputs, foreignKey, pageImport
- tetezina ny e.getFields() d ampiana ny var inputs
  - si isForeignKey false
    +si type = date => alaina template date, ovaina ny variable ao (inputLabel) d manampy pageImport
    +si Integer ou int => alaina template inputNumber, ovaina ny variable ao (inputLabel) d manampy pageImport  
     +sinon => alaina template input, ovaina ny variable ao (inputLabel) d manampy pageImport
    \*sinon :
    - alaina template select, ovaina variable (inputLabel, entityFkField, entityFkPk)
    - alaina template foreignKeyGetter, ovaina ny variable ao (entityMin) d ampiana ny var foreignKey
- ny entityMaj ovaina entityMaj
- FrontPage.addImport(pageImport)
- ny <import>, <foreignKey>, <input> anaty template ovaina
- generer fichier

- ilay status amin'ilay exception
- génération code asiana meethod ny URLMapping

front: json tsotra no alefa amlay post sy put

- bug import
- génération interface
- lay landing page sy fanovana mbola tsy tafiditra

- lay localDate tsy hain'ny jackson json mi parse an'azy
  com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: java.util.HashMap["data"]->veda.godao.utils.Paginated["items"]->akanjov3.entities.Utilisateur[0]->akanjov3.entities.Utilisateur["jour"])
  at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
  at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1330)
  at com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:35)
  at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
  at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:770)
  at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:183)
  at com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer.serializeContents(ObjectArraySerializer.java:253)
  at com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer.serialize(ObjectArraySerializer.java:214)
  at com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer.serialize(ObjectArraySerializer.java:23)
  at com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
  at com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:770)
  at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:183)
  at com.fasterxml.jackson.databind.ser.std.MapSerializer.serializeFields(MapSerializer.java:808)
  at com.fasterxml.jackson.databind.ser.std.MapSerializer.serializeWithoutTypeInfo(MapSerializer.java:764)
  at com.fasterxml.jackson.databind.ser.std.MapSerializer.serialize(MapSerializer.java:720)
  at com.fasterxml.jackson.databind.ser.std.MapSerializer.serialize(MapSerializer.java:35)
  at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.\_serialize(DefaultSerializerProvider.java:502)
  at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:341)
  at com.fasterxml.jackson.databind.ObjectMapper.\_writeValueAndClose(ObjectMapper.java:4793)
  at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:4038)
  at eriq.flamework.utils.Util.deserialize(Util.java:64)
  at eriq.flamework.servlet.FrontServlet.processRequest(FrontServlet.java:248)
  at eriq.flamework.servlet.FrontServlet.doGet(FrontServlet.java:266)
  at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:537)
  at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:631)
  at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205)
  at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
  at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
  at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
  at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
  at eriq.flamework.conf.CorsFilter.doFilter(CorsFilter.java:26)
  at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)
  at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)
  at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:166)
  at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)
  at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:493)
  at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)
  at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)
  at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:676)
  at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
  at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:341)
  at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:390)
  at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)
  at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:894)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1741)
  at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)
  at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
  at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
  at java.base/java.lang.Thread.run(Thread.java:833)
