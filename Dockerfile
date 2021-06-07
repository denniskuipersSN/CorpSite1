FROM tomcat:jdk8-corretto

ADD  target/globex-web.war /usr/local/tomcat/webapps/globex-web.war

EXPOSE 8080

CMD ["catalina.sh", "run"]